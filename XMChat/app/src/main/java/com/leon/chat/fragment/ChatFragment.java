package com.leon.chat.fragment;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.android.xlibrary.LoadingLayout.LoadingLayout;
import com.android.xlibrary.softKeyListener.SoftKeyBoardListener;
import com.leon.chat.R;
import com.leon.chat.activity.ChatActivity;
import com.leon.chat.adapter.ChatAdapter;
import com.leon.chat.base.BaseFragment;
import com.leon.chat.bean.ChatItemBean;
import com.leon.chat.bean.ContactBean;
import com.leon.chat.databinding.FragmentChatBinding;
import com.leon.chat.service.IMService;
import com.leon.chat.utils.LogUtils;
import com.leon.chat.utils.UIUtils;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.ArrayList;


/**
 * Created by leon on 16/11/1.
 */

public class ChatFragment extends BaseFragment implements View.OnClickListener{

    private ContactBean mContact;
    private ArrayList<ChatItemBean> mChats;
    private FragmentChatBinding mChatBinding;
    private ChatAdapter mChatAdapter;
    private int mContentViewHeight,mTvTitleHeight,mFooterContainerHeight;
    private boolean mIsMeasureContentView, mIsMeasureNavigationBar,mIsMeasureFooterContainer;
    private int mDifference = 1;
    private ChatMessageListener mChatMessageListener;
    private Chat mChat;
    private ChatManager mChatManager;
    private LinearLayoutManager mLayoutManager;

    @Override
    public void initData() {
        mContact = mActivity.getIntent().getParcelableExtra(BaseFragment.PARCELABLE_TAG);
        mLoadingLayout.refreshUI(LoadingLayout.LoadedResult.SUCCESS);
    }


    @Override
    public View initSuccessView() {
        View view = View.inflate(mActivity, R.layout.fragment_chat, null);
        mChatBinding = DataBindingUtil.bind(view);
        mChatBinding.contentView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(mActivity,0,false);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mChatBinding.contentView.setLayoutManager(mLayoutManager);

        setNavTitle("正在与" + mContact.nickname + "聊天中");

        mChatAdapter = new ChatAdapter(mActivity, mChatBinding.contentView);
        mChatBinding.contentView.setAdapter(mChatAdapter);
        mChats = new ArrayList<>();
        ChatItemBean chatItemBean = new ChatItemBean();
        ChatItemBean chatItemBean2 = new ChatItemBean();
        chatItemBean.message = "测试消息测试消息测试消息测试消息";
        chatItemBean.nickname = mContact.nickname;
        chatItemBean.type = 0;
        chatItemBean2.message = "测试消息测试消息测试消息测试消息测试消息测试消息测试消息测试消息";
        chatItemBean2.nickname = mContact.nickname;
        chatItemBean2.type = 1;
        mChats.add(chatItemBean);
        mChats.add(chatItemBean2);
        mChats.add(chatItemBean);
        mChats.add(chatItemBean2);
        mChats.add(chatItemBean);
        mChats.add(chatItemBean2);
        mChats.add(chatItemBean);
        mChats.add(chatItemBean2);
        mChatAdapter.setData(mChats);
        toChatBottom();
        initChat();
        return view;
    }

    @Override
    public void initListener() {
        mChatBinding.btnSend.setOnClickListener(this);
        SoftKeyBoardListener.setListener(mActivity, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                mChatBinding.contentView.setPadding(0,0,0,0);
                toChatBottom();
            }

            @Override
            public void keyBoardHide(int height) {
                measureHeight();
            }
        });
    }

    private void initChat(){
        if (mChatManager == null){
            mChatManager =  IMService.conn.getChatManager();
        }
        if (mChatMessageListener == null){
            mChatMessageListener = new ChatMessageListener();
        }
        if (mChat == null){
            mChat = mChatManager.createChat(mContact.account,mChatMessageListener);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSend:
                String text = mChatBinding.etText.getText().toString().trim();
                ChatItemBean chatItemBean = new ChatItemBean();
                if (text.equals("")){
                    chatItemBean.message = "测试消息测试消息测试消息测试消息";
                }else {
                    chatItemBean.message = text;
                }
                chatItemBean.nickname = mContact.nickname;
                chatItemBean.type = 0;
                chatItemBean.sendTime = System.currentTimeMillis();
                mChatAdapter.getData().add(chatItemBean);
                mChatAdapter.notifyDataSetChanged();
                toChatBottom();
                mChatBinding.etText.setText("");
                break;
        }
    }


    private void initLayoutManagerReverse(){
        if (mIsMeasureContentView && mIsMeasureNavigationBar && mIsMeasureFooterContainer){
            mDifference = UIUtils.getScreensHeight() - (mContentViewHeight + mTvTitleHeight + mFooterContainerHeight + UIUtils.getStatusBarHeight());
            if (mDifference >= 0){
                mChatBinding.contentView.setPadding(0,0,0,mDifference);
            }
            mIsMeasureContentView = false;
            mIsMeasureNavigationBar = false;
            mIsMeasureFooterContainer = false;
        }

    }

    private void measureHeight(){
        mChatBinding.contentView.post(new Runnable() {
            @Override
            public void run() {
                mContentViewHeight= mChatBinding.contentView.getMeasuredHeight();
                mIsMeasureContentView = true;
                initLayoutManagerReverse();
            }
        });

        mNavigationBar.post(new Runnable() {
            @Override
            public void run() {
                mTvTitleHeight = mNavigationBar.getMeasuredHeight();
                mIsMeasureNavigationBar = true;
                initLayoutManagerReverse();
            }
        });

        mChatBinding.footerContainer.post(new Runnable() {
            @Override
            public void run() {
                mFooterContainerHeight = mChatBinding.footerContainer.getMeasuredHeight();
                mIsMeasureFooterContainer = true;
                initLayoutManagerReverse();
                LogUtils.sf("mFooterContainerHeight:"+mFooterContainerHeight);
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


    class ChatMessageListener implements MessageListener {
        @Override
        public void processMessage(Chat chat, final Message message) {
            final String text = message.getBody();
            UIUtils.postTaskSafely(new Runnable() {
                @Override
                public void run() {
                    if (text != null && !text.equals("")){
                        ChatItemBean chatItemBean = new ChatItemBean();
                        chatItemBean.message = text;
                        chatItemBean.nickname = mContact.nickname;
                        chatItemBean.type = 1;
                        chatItemBean.sendTime = System.currentTimeMillis();
                        mChatAdapter.getData().add(chatItemBean);
                        mChatAdapter.notifyDataSetChanged();
                        toChatBottom();
                    }
                }
            });
        }
    }
}
