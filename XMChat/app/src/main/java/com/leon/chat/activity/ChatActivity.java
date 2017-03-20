package com.leon.chat.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.android.xlibrary.softKeyListener.SoftKeyBoardListener;
import com.android.xlibrary.util.StatusBarUtil;
import com.leon.chat.R;
import com.leon.chat.adapter.ChatAdapter;
import com.leon.chat.base.BaseActivity;
import com.leon.chat.base.BaseFragment;
import com.leon.chat.bean.ChatItemBean;
import com.leon.chat.bean.ContactBean;
import com.leon.chat.databinding.ActivityChatBinding;
import com.leon.chat.dbhelper.SmsOpenHelper;
import com.leon.chat.provider.SmsProvider;
import com.leon.chat.service.IMService;
import com.leon.chat.utils.LogUtils;
import com.leon.chat.utils.ThreadPoolFactory;
import com.leon.chat.utils.UIUtils;

import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;

/**
 * Created by leon on 17/2/8.
 */

public class ChatActivity extends BaseActivity implements View.OnClickListener{

    private ActivityChatBinding mChatBinding;
    private ContactBean mContact;
    private ArrayList<ChatItemBean> mChats;
    private ChatAdapter mChatAdapter;
    private LinearLayoutManager mLayoutManager;
    private IMService.OnChatMessageReceiveListener mOnChatMessageReceiveListener;
    private ChatServiceConnection mChatServiceConnection;
    private IMService mImService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContact = getIntent().getParcelableExtra(BaseFragment.PARCELABLE_TAG);
        mChatBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this,R.color.colorOrange);
//        registerContactContentObserver();
        initData();
        initListener();
    }

    private void initData(){
        mChatBinding.tvTitle.setText("正在与"+mContact.nickname+"聊天中");
        mChatBinding.contentView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mChatBinding.contentView.setLayoutManager(mLayoutManager);
        mChatAdapter = new ChatAdapter(this,mChatBinding.contentView);
        mChatBinding.contentView.setAdapter(mChatAdapter);
        mChats = new ArrayList<>();
        mChatAdapter.setData(mChats);
        loadData();
    }

    /**
     * 从数据库加载数据
     */
    private void loadData(){
        ThreadPoolFactory.getNormalProxy().execute(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = getContentResolver()
                        .query(SmsProvider.URI_SMS,
                                null,
                                SmsOpenHelper.SmsTable.SESSION_ACCOUNT +"=?",
                                new String[]{mContact.account},
                                null);
                if (cursor != null){
                    ChatItemBean chatItemBean;
                    while (cursor.moveToNext()){
                        chatItemBean = new ChatItemBean();
                        chatItemBean.fromAccount =cursor.getString(cursor.getColumnIndex(SmsOpenHelper.SmsTable.FROM_ACCOUNT));
                        chatItemBean.toAccount = cursor.getString(cursor.getColumnIndex(SmsOpenHelper.SmsTable.TO_ACCOUNT));
                        chatItemBean.message = cursor.getString(cursor.getColumnIndex(SmsOpenHelper.SmsTable.BODY));
                        chatItemBean.messageType = cursor.getString(cursor.getColumnIndex(SmsOpenHelper.SmsTable.MESSAGE_TYPE));
                        chatItemBean.status = cursor.getString(cursor.getColumnIndex(SmsOpenHelper.SmsTable.STATUS));
                        chatItemBean.sessionAccount = cursor.getString(cursor.getColumnIndex(SmsOpenHelper.SmsTable.SESSION_ACCOUNT));
                        chatItemBean.time = cursor.getString(cursor.getColumnIndex(SmsOpenHelper.SmsTable.TIME));
                        LogUtils.sf("From:"+chatItemBean.fromAccount);
                        if (chatItemBean.fromAccount.equals(mContact.account)){
                            chatItemBean.type = 1;
                        }else {
                            chatItemBean.type = 0;
                        }
                        mChats.add(chatItemBean);
                    }
                    UIUtils.postTaskSafely(new Runnable() {
                        @Override
                        public void run() {
                            mChatAdapter.notifyDataSetChanged();
                            toChatBottom();
                        }
                    });
                }
            }
        });

    }




    /**
     * 初始化监听
     */
    private void initListener(){
        // 绑定服务
        mChatServiceConnection = new ChatServiceConnection();
        Intent intent = new Intent(this,IMService.class);
        bindService(intent,mChatServiceConnection, Service.BIND_AUTO_CREATE);

        mChatBinding.btnSend.setOnClickListener(this);
        mChatBinding.btnBack.setOnClickListener(this);
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                mChatBinding.contentView.setPadding(0,0,0,0);
                toChatBottom();
            }

            @Override
            public void keyBoardHide(int height) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSend:
                sendMessage();
                break;
            case R.id.btnBack:
                finish();
                break;
        }
    }


    /**
     * 发送消息
     */
    private void sendMessage(){
        String text = mChatBinding.etText.getText().toString().trim();
        final ChatItemBean chatItemBean = new ChatItemBean();
        if (text.equals("")){
            chatItemBean.message = "测试消息测试消息测试消息测";
        }else {
            chatItemBean.message = text;
        }
        chatItemBean.nickname = mContact.nickname;
        chatItemBean.type = 0;
        chatItemBean.time = String.valueOf(System.currentTimeMillis());
        mChatAdapter.getData().add(chatItemBean);
        mChatAdapter.notifyDataSetChanged();
        mChatBinding.etText.setText("");
        toChatBottom();

        ThreadPoolFactory.getNormalProxy().execute(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.setFrom(IMService.curAccount);
                message.setTo(mContact.account);
                message.setBody(chatItemBean.message);
                message.setType(Message.Type.chat);
                message.setProperty("key","value");
                mImService.sendMessage(message);
            }
        });
    }


    /**
     * 跳到RecyclerView底部
     */
    private void toChatBottom(){
        if (mLayoutManager != null){
            mLayoutManager.scrollToPositionWithOffset(mLayoutManager.getItemCount() - 1,0);
        }
    }


    // 数据库观察者
    ChatContentObserver mChatContentObserver = new ChatContentObserver(new Handler());

    /**
     * 注册监听
     */
    private void registerContactContentObserver(){
        getContentResolver().registerContentObserver(SmsProvider.URI_SMS,true,mChatContentObserver);
    }

    /**
     * 注销监听
     */
    private void unregisterContactContentObserver(){
        getContentResolver().unregisterContentObserver(mChatContentObserver);
    }

    private class ChatContentObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public ChatContentObserver(Handler handler) {
            super(handler);
        }

        /**
         * 如果数据库记录发生改变会调用该方法
         * @param selfChange
         * @param uri
         */
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            loadData();
        }
    }

    private void createMessageReceiveListeners(){
        mOnChatMessageReceiveListener = new IMService.OnChatMessageReceiveListener() {
            @Override
            public void onChatMessageReceive(Message message) {
                final String text = message.getBody();
                LogUtils.sf("ChatActivity onChatMessageReceive");
                if (text != null && !text.equals("")){
                    UIUtils.postTaskSafely(new Runnable() {
                        @Override
                        public void run() {
                            ChatItemBean chatItemBean = new ChatItemBean();
                            chatItemBean.message = text;
                            chatItemBean.type = 1;
                            chatItemBean.time = String.valueOf(System.currentTimeMillis());
                            mChatAdapter.getData().add(chatItemBean);
                            mChatAdapter.notifyDataSetChanged();
                            toChatBottom();
                        }
                    });
                }
            }
        };
        mImService.mOnChatMessageReceiveListeners.add(mOnChatMessageReceiveListener);
    }

    /**
     * Activity和Service创建连接
     */
    private class ChatServiceConnection implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            IMService.IMBinder imBinder = (IMService.IMBinder) service;
            mImService =  imBinder.getService();
            createMessageReceiveListeners();
        }

        @Override

        public void onServiceDisconnected(ComponentName name) {

        }
    }


    @Override
    protected void onDestroy() {
//        unregisterContactContentObserver();
        if (mImService != null && mOnChatMessageReceiveListener != null){
            mImService.mOnChatMessageReceiveListeners.remove(mOnChatMessageReceiveListener);
        }
        if (mChatServiceConnection != null){
            unbindService(mChatServiceConnection);
        }
        super.onDestroy();
    }
}
