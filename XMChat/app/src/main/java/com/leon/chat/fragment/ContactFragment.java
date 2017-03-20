package com.leon.chat.fragment;

import android.database.ContentObserver;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.leon.chat.activity.DetailsActivity;
import com.leon.chat.bean.ContactBean;
import com.leon.chat.R;
import com.leon.chat.activity.ChatActivity;
import com.leon.chat.adapter.ContactAdapter;
import com.leon.chat.base.BaseFragment;
import com.leon.chat.base.BaseNavigationBar;
import com.leon.chat.databinding.FragmentSwipeRefreshBinding;
import com.leon.chat.dbhelper.ContactOpenHelper;
import com.leon.chat.provider.ContactsProvider;
import com.leon.chat.utils.LogUtils;
import com.leon.chat.utils.ThreadPoolFactory;
import com.leon.chat.utils.UIUtils;
import com.leon.chat.view.RecyclerAdapter;
import com.leon.chat.view.RecyclerViewFactory;
import com.leon.chat.view.SimpleDividerItemDecoration;


import java.util.ArrayList;


/**
 * @explain
 * @author leon.
 * @time 17/2/8 下午4:00.
 */
public class ContactFragment extends BaseFragment {

    private static final String TAG = "ContactFragment";

    private FragmentSwipeRefreshBinding mFragmentBinding;
    private RecyclerView mRecyclerView;
    private ArrayList<ContactBean> mData;
    private ContactAdapter mAdapter;

    @Override
    public void setupNavigationBar(BaseNavigationBar navigationBar) {
        super.setupNavigationBar(navigationBar);
        navigationBar.setNavTitle("联系人");
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

    private void initUI() {
        mNotInit = false;
        registerContactContentObserver();
        loadData();
    }


    @Override
    public View initSuccessView() {
        View v = mActivity.getLayoutInflater().inflate(R.layout.fragment_swipe_refresh, null);
        mFragmentBinding = DataBindingUtil.bind(v);
        mRecyclerView = RecyclerViewFactory.createVerticalRecyclerView(mActivity);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(mActivity,1));

        mFragmentBinding.swipeContainer.addView(mRecyclerView);
        mFragmentBinding.swipeContainer.setColorSchemeColors(UIUtils.getColor(R.color.colorOrange));
        mAdapter = new ContactAdapter(mActivity,mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    @Override
    public void initListener() {
        mAdapter.addOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapter.BaseViewHolder vh, int position) {
//                launchActivity(DetailsActivity.class,DetailsActivity.FRAGMENT_CHAT,mAdapter.getData().get(position));
                launchActivity(ChatActivity.class,mAdapter.getData().get(position));
            }
        });
        mFragmentBinding.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mFragmentBinding.swipeContainer.setRefreshing(false);
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
        final Cursor c = mActivity.getContentResolver().query(ContactsProvider.URI_CONTACT,null,null,null,null);
        if (c != null){
            mData =  new ArrayList<>();
            ContactBean contactBean;
            while (c.moveToNext()){
                contactBean = new ContactBean();
                contactBean.account = c.getString(c.getColumnIndex(ContactOpenHelper.ContactTable.ACCOUNT));
                contactBean.nickname = c
                        .getString(c.getColumnIndex(ContactOpenHelper.ContactTable.NICKNAME));
                contactBean.avatar = c.getString(c.getColumnIndex(ContactOpenHelper.ContactTable.AVATAR));
                contactBean.pinyin = c.getString(c.getColumnIndex(ContactOpenHelper.ContactTable.PINYIN));
                mData.add(contactBean);
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
    }


    ContactContentObserver mContactContentObserver = new ContactContentObserver(new Handler());

    /**
     * 注册监听
     */
    private void registerContactContentObserver(){
        mActivity.getContentResolver().registerContentObserver(ContactsProvider.URI_CONTACT,true,mContactContentObserver);
    }

    /**
     * 注销监听
     */
    private void unregisterContactContentObserver(){
        mActivity.getContentResolver().unregisterContentObserver(mContactContentObserver);
    }


    private class ContactContentObserver extends ContentObserver{

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public ContactContentObserver(Handler handler) {
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
        unregisterContactContentObserver();
        super.onDestroy();
    }
}
