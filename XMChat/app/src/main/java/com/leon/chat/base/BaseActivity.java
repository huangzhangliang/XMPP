package com.leon.chat.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.android.xlibrary.LoadingDialog.LoadingDialog;
import com.leon.chat.activity.MainActivity;
import com.leon.chat.utils.UIUtils;


public class BaseActivity extends AppCompatActivity {

    private LoadingDialog mLoadingDialog;

    public BaseActivity() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadingDialog = new LoadingDialog(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }


//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        if (mData != null){
//            outState.putParcelable("mData", mData);
//            LogUtils.sf("BaseActivity:" + mData.toString());
//        }
//        if (mDataArray != null){
//            outState.putParcelableArray("mDataArray", mDataArray);
//        }
//        if (mDataArrayList != null){
//            outState.putParcelableArrayList("mDataArrayList", mDataArrayList);
//        }
//        if (mDataSparseArray != null){
//            outState.putSparseParcelableArray("mDataSparseArray", mDataSparseArray);
//        }
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        if (savedInstanceState.containsKey("mData")){
//            mData = savedInstanceState.getParcelable("mData");
//        }
//        if (savedInstanceState.containsKey("mDataArray")){
//            mDataArray = savedInstanceState.getParcelableArray("mDataArray");
//        }
//        if (savedInstanceState.containsKey("mDataArrayList")){
//            mDataArrayList = savedInstanceState.getParcelableArrayList("mDataArrayList");
//        }
//        if (savedInstanceState.containsKey("mDataSparseArray")){
//            mDataSparseArray = savedInstanceState.getSparseParcelableArray("mDataSparseArray");
//        }
//    }


    public LoadingDialog getLoadingDialog(){
        return mLoadingDialog;
    }

    /**
     * 显示加载对话框
     */
    public void showLoadingDialog() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }


    /**
     * 隐藏加载对话框
     */
    public void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    public void launchActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 监听手机home键事件
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 过滤按键动作
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && this instanceof MainActivity) {
            moveTaskToBack(true);
            return  false;
        } else{
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onDestroy() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        if (this instanceof MainActivity) {
            moveTaskToBack(true);
        } else {
            super.onBackPressed();
        }

//        if (this instanceof HomeActivity){
//            if (System.currentTimeMillis() - mPerTime > 2000){
//                Toast.makeText(UIUtils.getContext(),"再按一次退出应用",Toast.LENGTH_SHORT).show();
//                mPerTime = System.currentTimeMillis();
//                return;
//            }
//        }

    }


    /**
     * Activity销毁方法
     * 延迟执行
     */
    public void destroy (){
        UIUtils.postTaskDelay(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },100);
    }

}
