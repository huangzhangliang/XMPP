package com.android.xlibrary.modal;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.xlibrary.R;

/**
 * Created by leon on 16/7/1.
 */
public class SexModalDialog extends Dialog implements View.OnClickListener {

    private ImageView mClose;
    private OnModalFormClick mListener;


    public SexModalDialog(Context context) {
        this(context, R.style.quick_option_dialog);
    }

    public SexModalDialog(Context context, String mobile) {
        this(context, R.style.quick_option_dialog);
    }

    public SexModalDialog(Context context, String mobile, String type) {
        this(context, R.style.quick_option_dialog,type);
    }


    public SexModalDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    @SuppressLint("InflateParams")
    private SexModalDialog(Context context, int defStyle) {
        super(context, defStyle);
        View dialogModal = getLayoutInflater().inflate(R.layout.dialog_modal_sex, null);
        dialogModal.findViewById(R.id.clickMan).setOnClickListener(this);
        dialogModal.findViewById(R.id.clickWoman).setOnClickListener(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogModal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SexModalDialog.this.dismiss();
                return true;
            }
        });
        super.setContentView(dialogModal);
    }

    @SuppressLint("InflateParams")
    private SexModalDialog(Context context, int defStyle, String type) {
        super(context, defStyle);
        View dialogModal = getLayoutInflater().inflate(R.layout.dialog_modal, null);
        dialogModal.findViewById(R.id.clickMan).setOnClickListener(this);
        dialogModal.findViewById(R.id.clickWoman).setOnClickListener(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogModal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SexModalDialog.this.dismiss();
                return true;
            }
        });
        super.setContentView(dialogModal);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.BOTTOM);
        WindowManager wm = getWindow().getWindowManager();
        Display defaultDisplay = wm.getDefaultDisplay();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = defaultDisplay.getWidth();
        attributes.alpha = 0.95f;
        getWindow().setAttributes(attributes);
    }

    public void setOnModalDialogClickListener(OnModalFormClick lis) {
        mListener = lis;
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onModalClick(v);
        }
        dismiss();
    }

    public interface OnModalFormClick {
        void onModalClick(View v);
    }
}
