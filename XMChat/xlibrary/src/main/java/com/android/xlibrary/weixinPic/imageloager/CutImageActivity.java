package com.android.xlibrary.weixinPic.imageloager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


import com.android.xlibrary.R;
import com.android.xlibrary.util.StatusBarUtil;
import com.android.xlibrary.weixinPic.utils.ImageTools;
import com.android.xlibrary.weixinPic.utils.ToastUtil;
import com.android.xlibrary.weixinPic.view.ActionbarView;
import com.android.xlibrary.weixinPic.view.ClipImageLayout;

import java.io.File;


/**
 * http://blog.csdn.net/lmj623565791/article/details/39761281
 *
 * @author zhy
 */
public class CutImageActivity extends Activity {
    private ClipImageLayout mClipImageLayout;
    private ActionbarView actionbar;
    private String path;
    private String dirPath;
    private String photoName;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pic_cutimage_activity);
//		DisplayMetrics metrics = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(metrics);
        path = getIntent().getStringExtra("path");
        dirPath = getIntent().getStringExtra("dirPath");
        photoName = getIntent().getStringExtra("photoName");
        actionbar = (ActionbarView) findViewById(R.id.pic_cutimage_actionbar);
        mClipImageLayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
        // degree = ImageTools.readPictureDegree(path);
        bitmap = ImageTools.getResizedBitmap(path, 480, 800);
        // if(degree != 0){
        // bitmap = ImageTools.rotateBitmap(bitmap, degree);
        // }
        // bitmap = BitmapFactory.decodeFile(path);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this,R.color.sybOrange);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            actionbar.setPadding(0, getStatusBarHeight(),0,0);
        }
        mClipImageLayout.setImage(bitmap);
        actionbar.setTitle("裁剪图片");
        actionbar.setRightText(R.string.pic_confirm);
        actionbar.setOnRightClick(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (ImageTools.RemainingSpace() / 1024 < 300) {
                    ToastUtil.show(CutImageActivity.this, "SD卡存储空间不足!");
                    return;
                }
                Bitmap bitmap = mClipImageLayout.clip();
                File file = ImageTools.savePhotoToSDCard(bitmap, dirPath,
                        photoName);
                Intent intent = new Intent();
                if (file == null) {
                    intent.putExtra("path", "");
                } else {
                    intent.putExtra("path", file.getAbsolutePath());
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    // 获取状态栏高度
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
        }
    }

}
