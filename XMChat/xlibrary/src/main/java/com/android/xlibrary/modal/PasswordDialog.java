package com.android.xlibrary.modal;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.xlibrary.R;
import com.android.xlibrary.databinding.DialogPasswordBinding;

/**
 * Created by leon on 16/7/1.
 */
public  abstract class PasswordDialog extends Dialog implements View.OnClickListener {

    private OnModalFormClick mListener;
    private TextView mTvMobile;
    private String type;
    private String mPassword = "";
    private DialogPasswordBinding mPasswordBinding;
    private View mDialogModal;

    public String getPassword() {
        return mPassword;
    }

    public PasswordDialog(Context context) {
        this(context, R.style.quick_option_dialog);
    }

    public PasswordDialog(Context context, String mobile) {
        this(context, R.style.quick_option_dialog);
        if (mTvMobile != null){
            mTvMobile.setText(mobile);
        }
    }

    public PasswordDialog(Context context, String mobile, String type) {
        this(context, R.style.quick_option_dialog,type);
        if (mTvMobile != null){
            mTvMobile.setText(mobile);
        }

    }


    public PasswordDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }

    @SuppressLint("InflateParams")
    private PasswordDialog(Context context, int defStyle) {
        super(context, defStyle);
        mDialogModal = getLayoutInflater().inflate(R.layout.dialog_password, null);
        mPasswordBinding = DataBindingUtil.bind(mDialogModal);
        mPasswordBinding.btnBackspace.setOnClickListener(this);
        mPasswordBinding.btnClose.setOnClickListener(this);
        mPasswordBinding.btnForgetPassword.setOnClickListener(this);
        mPasswordBinding.btnNumber0.setOnClickListener(this);
        mPasswordBinding.btnNumber1.setOnClickListener(this);
        mPasswordBinding.btnNumber2.setOnClickListener(this);
        mPasswordBinding.btnNumber3.setOnClickListener(this);
        mPasswordBinding.btnNumber4.setOnClickListener(this);
        mPasswordBinding.btnNumber5.setOnClickListener(this);
        mPasswordBinding.btnNumber6.setOnClickListener(this);
        mPasswordBinding.btnNumber7.setOnClickListener(this);
        mPasswordBinding.btnNumber8.setOnClickListener(this);
        mPasswordBinding.btnNumber9.setOnClickListener(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(mDialogModal);
    }

    @SuppressLint("InflateParams")
    private PasswordDialog(Context context, int defStyle, String type) {
        super(context, defStyle);
        this.type = type;
        mDialogModal = getLayoutInflater().inflate(R.layout.dialog_password, null);
        mPasswordBinding = DataBindingUtil.bind(mDialogModal);
        mPasswordBinding.btnBackspace.setOnClickListener(this);
        mPasswordBinding.btnClose.setOnClickListener(this);
        mPasswordBinding.btnForgetPassword.setOnClickListener(this);
        mPasswordBinding.btnNumber0.setOnClickListener(this);
        mPasswordBinding.btnNumber1.setOnClickListener(this);
        mPasswordBinding.btnNumber2.setOnClickListener(this);
        mPasswordBinding.btnNumber3.setOnClickListener(this);
        mPasswordBinding.btnNumber4.setOnClickListener(this);
        mPasswordBinding.btnNumber5.setOnClickListener(this);
        mPasswordBinding.btnNumber6.setOnClickListener(this);
        mPasswordBinding.btnNumber7.setOnClickListener(this);
        mPasswordBinding.btnNumber8.setOnClickListener(this);
        mPasswordBinding.btnNumber9.setOnClickListener(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setContentView(mDialogModal);
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

    private int[] ids = {R.id.btnNumber1,R.id.btnNumber2,R.id.btnNumber3,
            R.id.btnNumber4,R.id.btnNumber5,R.id.btnNumber6,
            R.id.btnNumber7,R.id.btnNumber8,R.id.btnNumber9,
            R.id.btnNumber0};
    private int[] vIds = {R.id.VP1,R.id.VP2,R.id.VP3,R.id.VP4,R.id.VP5,R.id.VP6};

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnClose) {
            PasswordDialog.this.dismiss();
        }else if (i == R.id.btnForgetPassword) {
            forgetPassword();
//            if (mListener != null) {
//                mListener.onModalClick(v);
//            }
        }else if (i == R.id.btnBackspace){
            if (mPassword.length() > 0){
                mPassword = mPassword.substring(0,mPassword.length() - 1);
                mDialogModal.findViewById(vIds[mPassword.length()]).setVisibility(View.GONE);
            }
        }if (mPassword.length() < 6) {
            for (int id : ids) {
                if (i == id) {
                    TextView t = (TextView) mDialogModal.findViewById(id);
                    mPassword = mPassword + t.getText();
                    mDialogModal.findViewById(vIds[mPassword.length() - 1]).setVisibility(View.VISIBLE);
                }
            }
        }
        if (mPassword.length() == 6){
            inputFinish();
        }
    }

    /**
     * 输入完成后执行
     */
    public abstract void inputFinish();

    /**
     * 忘记密码
     */
    public abstract void forgetPassword();

    public interface OnModalFormClick {
        void onModalClick(View v);
    }
}
