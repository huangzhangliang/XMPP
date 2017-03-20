package com.leon.chat.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;

import com.android.xlibrary.FlyTabLayout.TabBean;
import com.android.xlibrary.FlyTabLayout.listener.CustomTabEntity;
import com.android.xlibrary.FlyTabLayout.listener.OnTabSelectListener;
import com.android.xlibrary.util.StatusBarUtil;
import com.leon.chat.R;
import com.leon.chat.base.BaseActivity;
import com.leon.chat.base.BaseFragment;
import com.leon.chat.databinding.ActivityMainBinding;
import com.leon.chat.fragment.ContactFragment;
import com.leon.chat.fragment.SessionFragment;

import java.util.ArrayList;


public class MainActivity extends BaseActivity {

    private ActivityMainBinding mMainBinding;
    private String[] mTitles = {"会话", "联系人"};
    private int[] mIconUnSelectIds = {
            R.mipmap.icon_session, R.mipmap.icon_contact};
    private int[] mIconSelectIds = {
            R.mipmap.icon_session_select, R.mipmap.icon_contact_select};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private FragmentPagerAdapter mPagerAdapter;
    private SparseArrayCompat<BaseFragment> cachesFragment = new SparseArrayCompat<>();
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt("mPosition",0);
        }
        mMainBinding  = DataBindingUtil.setContentView(this,R.layout.activity_main);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this,R.color.colorOrange);
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabBean(mTitles[i], mIconSelectIds[i], mIconUnSelectIds[i]));
        }
        if (mTabEntities != null){
            mMainBinding.mainMenu.setTabData(mTabEntities);
        }


        if (cachesFragment.get(0) == null){
            cachesFragment.put(0,new SessionFragment());
        }
        if (cachesFragment.get(1) == null){
            cachesFragment.put(1,new ContactFragment());
        }

        mPagerAdapter = new FragmentPagerAdapter(MainActivity.this.getSupportFragmentManager()){
            @Override
            public int getCount() {
                return cachesFragment.size();
            }
            @Override
            public Fragment getItem(int position) {
                return cachesFragment.get(position);
            }
        };
        mMainBinding.viewPagerContainer.setAdapter(mPagerAdapter);


        mMainBinding.mainMenu.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mMainBinding.viewPagerContainer.setCurrentItem(position,false);
                cachesFragment.get(position).initData();
            }
            @Override
            public void onTabReselect(int position) {

            }
        });
        cachesFragment.get(mPosition).initData();


    }

}
