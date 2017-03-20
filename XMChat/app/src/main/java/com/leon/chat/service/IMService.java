package com.leon.chat.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.leon.chat.base.BaseApplication;
import com.leon.chat.dbhelper.ContactOpenHelper;
import com.leon.chat.dbhelper.SmsOpenHelper;
import com.leon.chat.provider.ContactsProvider;
import com.leon.chat.provider.SmsProvider;
import com.leon.chat.utils.LogUtils;
import com.leon.chat.utils.PinyinUtils;
import com.leon.chat.utils.ThreadPoolFactory;
import com.leon.chat.utils.UIUtils;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by leon on 17/2/7.
 */

public class IMService extends Service{

    public static XMPPConnection conn;
    public static String curAccount;
    public List<OnChatMessageReceiveListener> mOnChatMessageReceiveListeners = new ArrayList<>();
    public Map<String,Chat> mChatMap = new HashMap<>();

    public Chat mCurrChat;
    private Roster mRoster;
    private Presence mPresence;
    private ContactRosterListener mContactRosterListener;
    private ChatManager mChatManager;
    private ChatMessageListener mChatMessageListener;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new IMBinder();
    }

    public class IMBinder extends Binder{
        public IMService getService(){
            return IMService.this;
        }
    }

    @Override
    public void onCreate() {

        // Roster 所有联系人
        // RosterGroup 联系人组
        // RosterEntry 单个联系人

        /*==================== 同步联系人 Start ====================*/

        try {

            mRoster = IMService.conn.getRoster();
            final Collection<RosterEntry> entries = mRoster.getEntries();



            for (RosterEntry entry : entries) {
                //获取好友在线状态
                Presence presence = mRoster.getPresence(entry.getUser());
                LogUtils.sf("RosterEntry："+presence.isAvailable());
                if(presence.isAvailable())   //判断好友是否在线
                {

                    //相应的逻辑操作
                }
            }

            mContactRosterListener = new ContactRosterListener();
            mRoster.addRosterListener(mContactRosterListener);

            ThreadPoolFactory.getNormalProxy().execute(new Runnable() {
                @Override
                public void run() {
                    for (RosterEntry entry : entries) {
                        saveOrUpdateData(entry);
                    }
                }
            });
        }catch (Exception e){
            UIUtils.showToast("连接失败，请重试。");
        }

        /*
        * entry.getUser() ----> admin@hzlleon.com (JID用户的唯一标识)
        * entry.getName() ----> null (用户别名)
        * entry.getGroups() ----> []
        * entry.getStatus() ----> null
        * entry.getType() ----> from
        */

        /*==================== 同步联系人 End ====================*/
        
        
        
        /*==================== 创建消息管理者 Start ====================*/


        try {
            if (mChatManager == null && IMService.conn.getChatManager() != null){
                mChatManager =  IMService.conn.getChatManager();
            }
            if (mChatMessageListener == null){
                mChatMessageListener = new ChatMessageListener();
            }
            mChatManager.addChatListener(mIMChatManagerListener);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        /*==================== 创建消息管理者 End ====================*/

        
        super.onCreate();
    }


    /**
     * 保存或更新数据
     * @param entry
     */
    private void saveOrUpdateData(final RosterEntry entry) {
        mPresence = mRoster.getPresence(entry.getUser());
        ContentValues values = new ContentValues();
        values.put(ContactOpenHelper.ContactTable.ACCOUNT, entry.getUser());
        values.put(ContactOpenHelper.ContactTable.AVATAR, "0");
//        values.put(ContactOpenHelper.ContactTable.ONLINE, mPresence.isAvailable()?1:0);
        String nickName = entry.getName();
        if (nickName == null || nickName.equals("")){
            if (nickName != null && !nickName.equals("")) {
                nickName = entry.getUser().substring(0, entry.getUser().indexOf("@")); // hzl@hzlleon.com->hzl
            }
        }
        values.put(ContactOpenHelper.ContactTable.NICKNAME, nickName);
        values.put(ContactOpenHelper.ContactTable.PINYIN, PinyinUtils.getPinyin(nickName));
        // 先update，后insert
        int updateCount = getContentResolver().update(ContactsProvider.URI_CONTACT, values, ContactOpenHelper.ContactTable.ACCOUNT + "=?",
                new String[]{entry.getUser()});

        if (updateCount <= 0) { // 如果更新数据时影响到的条数为0，则相应的数据不存在，去新增
            getContentResolver().insert(ContactsProvider.URI_CONTACT, values);
        }
    }

    /**
     * 创建当前Chat
     * @param curAccount
     */
    public void createCurrChat(String curAccount){
        if (mChatMap.containsKey(curAccount)){
            mCurrChat = mChatMap.get(curAccount);
        }else {
            mCurrChat = mChatManager.createChat(curAccount,mChatMessageListener);
            mChatMap.put(curAccount,mCurrChat);
        }
    }


    /**
     * 发送消息
     */
    public void sendMessage(final Message message){
        try {
            createCurrChat(filterUserAccount(message.getTo()));
            mCurrChat.sendMessage(message);
            saveMessage(filterUserAccount(message.getTo()),filterUserAccount(message.getFrom()),message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }


    IMChatManagerListener mIMChatManagerListener = new IMChatManagerListener();

    class IMChatManagerListener implements ChatManagerListener{

        @Override
        public void chatCreated(Chat chat, boolean b) {
            String participant = filterUserAccount(chat.getParticipant());
            mCurrChat = chat;
            if (!mChatMap.containsKey(participant)){
                mChatMap.put(participant,mCurrChat);
            }
            mCurrChat.addMessageListener(mChatMessageListener);

            if (b){ // 我创建的会话

            }else { // 别人创建的会话

            }
        }
    }


    public interface OnChatMessageReceiveListener{
        void onChatMessageReceive(Message message);
    }


    class ChatMessageListener implements MessageListener {
        @Override
        public void processMessage(Chat chat, final Message message) {
            final String text = message.getBody();
            if (text != null && !text.equals("")){
                UIUtils.postTaskSafely(new Runnable() {
                    @Override
                    public void run() {
                        UIUtils.showToast(text);
                    }
                });
                saveMessage(filterUserAccount(chat.getParticipant()),filterUserAccount(chat.getParticipant()),message);
                if (mOnChatMessageReceiveListeners != null && mOnChatMessageReceiveListeners.size() > 0){
                    LogUtils.sf("IMService ChatMessageListener");
                    for (OnChatMessageReceiveListener receiveListener : mOnChatMessageReceiveListeners)
                        receiveListener.onChatMessageReceive(message);
                }
            }
        }
    }

    private String filterUserAccount(String account){
        return account.substring(0,account.indexOf("@")) + "@" + BaseApplication.REALM_NAME;
    }



    /**
     * 保存消息
     * @param message
     */
    private void saveMessage(String sessionAccount,String from,Message message) {
        ContentValues values = new ContentValues();
        LogUtils.sf("saveMessage:"+from);
        LogUtils.sf("saveMessage:"+message.getTo());
        values.put(SmsOpenHelper.SmsTable.FROM_ACCOUNT,from);
        values.put(SmsOpenHelper.SmsTable.TO_ACCOUNT,filterUserAccount(message.getTo()));
        values.put(SmsOpenHelper.SmsTable.BODY,message.getBody());
        values.put(SmsOpenHelper.SmsTable.STATUS,"offlein");
        values.put(SmsOpenHelper.SmsTable.MESSAGE_TYPE,message.getType().name());
        values.put(SmsOpenHelper.SmsTable.TIME,System.currentTimeMillis());
        values.put(SmsOpenHelper.SmsTable.SESSION_ACCOUNT,sessionAccount);
        try {
            getContentResolver().insert(SmsProvider.URI_SMS,values);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }

    }



    private class ContactRosterListener implements RosterListener {
        @Override
        public void entriesAdded(Collection<String> collection) {//添加
            final Collection<RosterEntry> entries = mRoster.getEntries();
            for (String s : collection){
                for (RosterEntry entry : entries) {
                    if (s.equals(entry.getUser())){
                        saveOrUpdateData(entry);
                    }
                }
            }
        }

        @Override
        public void entriesUpdated(Collection<String> collection) { //修改
            final Collection<RosterEntry> entries = mRoster.getEntries();
            for (String s : collection){
                for (RosterEntry entry : entries) {
                    if (s.equals(entry.getUser())){
                        saveOrUpdateData(entry);
                    }
                }
            }
        }

        @Override
        public void entriesDeleted(Collection<String> collection) { //删除
            for (String s : collection){
                getContentResolver().delete(ContactsProvider.URI_CONTACT,ContactOpenHelper.ContactTable.ACCOUNT + "=?",
                        new String[]{s});
            }
        }

        @Override
        public void presenceChanged(Presence presence) { //状态改变
            LogUtils.sf("presenceChanged "+presence.getTo()+" "+presence.getStatus());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mRoster != null && mContactRosterListener !=null ){
            mRoster.removeRosterListener(mContactRosterListener);
        }

        if (mCurrChat != null && mChatMessageListener != null){
            mCurrChat.removeMessageListener(mChatMessageListener);
            mChatMessageListener = null;
            mCurrChat = null;
        }
        super.onDestroy();
    }



}
