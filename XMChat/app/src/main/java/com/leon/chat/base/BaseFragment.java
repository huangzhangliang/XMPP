package com.leon.chat.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.android.xlibrary.LoadingLayout.LoadingLayout;
import com.leon.chat.R;
import com.leon.chat.databinding.FragmentBaseBinding;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;
import java.util.Map;

import static com.leon.chat.activity.NavigationActivity.INTENT_FRAGMENT_INDEX_KEY;


public abstract class BaseFragment<T extends Parcelable> extends Fragment {

    public static final String PARCELABLE_TAG = "Parcelable_TAG";

    public LoadingLayout mLoadingLayout;
    public FragmentBaseBinding mSuperBinding;
    public BaseActivity mActivity;
    private boolean isAutoLoadingData = true;
    //    public T mData;
//    public Parcelable[] mDataArray;
//    public ArrayList<T> mDataArrayList;
//    public SparseArray<T> mDataSparseArray;
    public BaseNavigationBar mNavigationBar;
    private boolean isInitNavigationBar;
    public boolean mNotInit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initTransit();
        mActivity.getLoadingDialog().setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                OkHttpUtils.cancelTag(this);
            }
        });
    }

    public View initView(LayoutInflater inflater) {
        mActivity = (BaseActivity) getActivity();
        mSuperBinding = DataBindingUtil.bind(inflater.inflate(R.layout.fragment_base, null));
//        mSuperBinding = DataBindingUtil.bind(View.inflate(getContext(), R.layout.fragment_base, null));
        if (mLoadingLayout == null) {// 第一次执行
            mLoadingLayout = new LoadingLayout(getContext()) {
                @Override
                public void initData() {
                    BaseFragment.this.initData();
                }

                @Override
                public View initSuccessView() {
                    View tempView = BaseFragment.this.initSuccessView();
                    initListener();
                    return tempView;
                }


            };
        } else {// 第2次执行
            ((ViewGroup) mLoadingLayout.getParent()).removeView(mLoadingLayout);
        }
        mSuperBinding.contentContainer.addView(mLoadingLayout);
        // 初始化导航条
        if (mNavigationBar == null) {
            mNavigationBar = new BaseNavigationBar(mActivity);
            setupNavigationBar(mNavigationBar);
        }else{
            ((ViewGroup)mNavigationBar.getParent()).removeView(mNavigationBar);
        }
        mSuperBinding.headerContainer.addView(mNavigationBar);
        return mSuperBinding.superContainer;
    }

    public void setupNavigationBar(BaseNavigationBar navigationBar){

    }

    public void initTransit(){
        initData();
    }

    public void initListener(){

    }

    /**
     * @des 真正加载数据, 但是BaseFragment不知道具体实现, 必须实现, 定义成为抽象方法, 让子类具体实现
     * @call loadData()方法被调用的时候
     */
    public abstract void initData();

    /**
     * @des 返回成功视图, 但是不知道具体实现, 所以定义成抽象方法
     * @call 正在加载数据完成之后, 并且数据加载成功, 我们必须告知具体的成功视图
     */
    public abstract View initSuccessView();

    /**
     * @des 初始化一个头部导航条
     */
    public void initHeaderView() {
        if (mNavigationBar == null) {
            mNavigationBar = new BaseNavigationBar(mActivity);
        }
    }


    /**
     * @des 删除导航条
     */
    public void notNavigationBar() {
        if (mSuperBinding != null) {
            mSuperBinding.headerContainer.removeAllViews();
        }
    }


    /**
     * @clazz 跳转的activity.class
     */
    public void launchActivity(Class clazz) {
        Intent intent = new Intent(mActivity, clazz);
        startActivity(intent);
    }

    /**
     * @clazz 跳转的activity.class
     * @fragmentIndex fragment的index
     */
    public void launchActivity(Class clazz, int fragmentIndex) {
        Intent intent = new Intent(mActivity, clazz);
        intent.putExtra(INTENT_FRAGMENT_INDEX_KEY, fragmentIndex);
        startActivityForResult(intent, fragmentIndex);
    }

    /**
     * @clazz 跳转的activity.class
     * @fragmentIndex fragment的index
     */
    public void launchActivity(Class clazz, int fragmentIndex, String... values) {
        Intent intent = new Intent(mActivity, clazz);
        for (String v : values) {
            intent.putExtra(v, v);
        }
        intent.putExtra(INTENT_FRAGMENT_INDEX_KEY, fragmentIndex);
        startActivityForResult(intent, fragmentIndex);
    }

    /**
     * @clazz 跳转的activity.class
     * @parcelable 携带的Parcelable类型参数
     * @fragmentIndex fragment的index
     */
    public void launchActivity(Class clazz, int fragmentIndex,Parcelable parcelable) {
        Intent intent = new Intent(mActivity, clazz);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCELABLE_TAG, parcelable);
        intent.putExtras(bundle);
        intent.putExtra(INTENT_FRAGMENT_INDEX_KEY, fragmentIndex);
        startActivityForResult(intent, fragmentIndex);
    }


    /**
     * @clazz 跳转的activity.class
     * @parcelable 携带的Parcelable类型参数
     */
    public void launchActivity(Class clazz,Parcelable parcelable) {
        Intent intent = new Intent(mActivity, clazz);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCELABLE_TAG, parcelable);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * @clazz 跳转的activity.class
     * @parcelable 携带的Parcelable类型参数集合
     * @fragmentIndex fragment的index
     */
    public void launchParcelablesActivity(Class clazz, Map<String, Parcelable> parcelables, int fragmentIndex) {
        Intent intent = new Intent(mActivity, clazz);
        Bundle bundle = new Bundle();
        if (parcelables != null) {
            for (Map.Entry<String, Parcelable> info : parcelables.entrySet()) {
                bundle.putParcelable(info.getKey(), info.getValue());
            }
        }
        intent.putExtras(bundle);
        intent.putExtra("fragment_index", fragmentIndex);
        startActivityForResult(intent, fragmentIndex);
    }

    /**
     * @des 设置导航条标题
     * @title 标题文字
     */
    public void setNavTitle(String title) {
        if (mNavigationBar != null) {
            mNavigationBar.setNavTitle(title);
        }
    }


    /**
     * @des 隐藏/显示导航条的返回按钮
     * @isHide true=隐藏/false=显示
     */
    public void setHideBackBtn(boolean isHide) {
        if (mNavigationBar != null) {
            if (isHide){
                mNavigationBar.mNavigationBinding.btnNavBack.setVisibility(View.GONE);
            }else{
                mNavigationBar.mNavigationBinding.btnNavBack.setVisibility(View.VISIBLE);
            }
        }

    }




    /**
     * @des 网络数据Json化对象检测
     */
    public void checkState(Object obj) {
        if (obj == null) {
            mLoadingLayout.refreshUI(LoadingLayout.LoadedResult.EMPTY);
        } else if (obj instanceof List) {
            if (((List) obj).size() == 0) {
                mLoadingLayout.refreshUI(LoadingLayout.LoadedResult.EMPTY);
            } else {
                mLoadingLayout.refreshUI(LoadingLayout.LoadedResult.SUCCESS);
            }
        } else if (obj instanceof Map) {
            if (((Map) obj).size() == 0) {
                mLoadingLayout.refreshUI(LoadingLayout.LoadedResult.EMPTY);
            } else {
                mLoadingLayout.refreshUI(LoadingLayout.LoadedResult.SUCCESS);
            }
        } else {
            mLoadingLayout.refreshUI(LoadingLayout.LoadedResult.SUCCESS);
        }
    }
}
