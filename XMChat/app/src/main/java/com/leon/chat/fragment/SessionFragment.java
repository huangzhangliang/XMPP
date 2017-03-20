package com.leon.chat.fragment;

import android.database.ContentObserver;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.leon.chat.R;
import com.leon.chat.activity.ChatActivity;
import com.leon.chat.adapter.SessionAdapter;
import com.leon.chat.base.BaseFragment;
import com.leon.chat.base.BaseNavigationBar;
import com.leon.chat.bean.ContactBean;
import com.leon.chat.bean.SessionBean;
import com.leon.chat.databinding.FragmentSwipeRefreshBinding;
import com.leon.chat.dbhelper.SmsOpenHelper;
import com.leon.chat.provider.SmsProvider;
import com.leon.chat.service.IMService;
import com.leon.chat.utils.LogUtils;
import com.leon.chat.utils.ThreadPoolFactory;
import com.leon.chat.utils.UIUtils;
import com.leon.chat.view.RecyclerAdapter;
import com.leon.chat.view.RecyclerViewFactory;
import com.leon.chat.view.SimpleDividerItemDecoration;

import java.util.ArrayList;


/**
 * Created by leon on 16/10/25.
 */

public class SessionFragment extends BaseFragment{

    private FragmentSwipeRefreshBinding mFragmentBinding;
    private RecyclerView mRecyclerView;
    private SessionAdapter mAdapter;
    private ArrayList<SessionBean> mData;
    private SessionContentObserver mSessionContentObserver = new SessionContentObserver(new Handler());

    @Override
    public void setupNavigationBar(BaseNavigationBar navigationBar) {
        super.setupNavigationBar(navigationBar);
        navigationBar.setNavTitle("会话");
        navigationBar.mNavigationBinding.btnNavBack.setVisibility(View.GONE);
    }

    @Override
    public void initTransit() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mFragmentBinding == null && mNotInit) {
            if (mActivity != null) {
                initUI();
            }
        }
    }

    @Override
    public void initData() {
        if (mFragmentBinding == null) {
            if (mActivity != null) {
                initUI();
            } else {
                mNotInit = true;
            }
        }
    }

    private void initUI(){
        mNotInit = false;
        registerSessionContentObserver();
        loadData();
    }

    @Override
    public View initSuccessView() {
        View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_swipe_refresh,null);
        mFragmentBinding = DataBindingUtil.bind(v);
        mRecyclerView = RecyclerViewFactory.createVerticalRecyclerView(mActivity);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(mActivity,1));
        mFragmentBinding.swipeContainer.addView(mRecyclerView);
        mFragmentBinding.swipeContainer.setColorSchemeColors(UIUtils.getColor(R.color.colorOrange));
        mAdapter = new SessionAdapter(mActivity,mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    @Override
    public void initListener() {
        mAdapter.addOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapter.BaseViewHolder vh, int position) {
                ContactBean mContact = new ContactBean();
                mContact.account = mAdapter.getData().get(position).account;
                mContact.nickname = mAdapter.getData().get(position).nickname;
                mContact.account = mAdapter.getData().get(position).account;
                launchActivity(ChatActivity.class,mContact);
            }
        });
    }


    private void loadData() {
        ThreadPoolFactory.getNormalProxy().execute(new Runnable() {
            @Override
            public void run() {
                setOrUpdateAdapter();
            }
        });
    }

    private void setOrUpdateAdapter(){
        final Cursor c = mActivity.getContentResolver().query(SmsProvider.URI_SESSION,null,null,new String[]{IMService.curAccount,IMService.curAccount},null);

        if (c != null){
            mData =  new ArrayList<>();
            SessionBean sessionBean;
            while (c.moveToNext()){
                sessionBean = new SessionBean();
                sessionBean.account = c.getString(c.getColumnIndex(SmsOpenHelper.SmsTable.SESSION_ACCOUNT));
                sessionBean.message = c.getString(c.getColumnIndex(SmsOpenHelper.SmsTable.BODY));
                sessionBean.nickname = sessionBean.account.substring(0,sessionBean.account.indexOf("@"));
                mData.add(sessionBean);
            }
        }

        UIUtils.postTaskSafely(new Runnable() {
            @Override
            public void run() {
                checkState(mData);
                if (mFragmentBinding != null) {
                    mFragmentBinding.swipeContainer.setRefreshing(false);
                }
                if (mAdapter != null){
                    mAdapter.setData(mData);
                }
            }
        });
    }


    /**
     * 注册监听
     */
    private void registerSessionContentObserver(){
        mActivity.getContentResolver().registerContentObserver(SmsProvider.URI_SMS,true,mSessionContentObserver);
    }

    /**
     * 注销监听
     */
    private void unregisterSessionContentObserver(){
        mActivity.getContentResolver().unregisterContentObserver(mSessionContentObserver);
    }


    private class SessionContentObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public SessionContentObserver(Handler handler) {
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
            LogUtils.sf("onChange:"+uri);
            setOrUpdateAdapter();
        }
    }


    @Override
    public void onDestroy() {
        unregisterSessionContentObserver();
        super.onDestroy();
    }
}
