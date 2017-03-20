package com.android.xlibrary.modal;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.xlibrary.R;

/**
 * Created by leon on 16/7/1.
 */
public class ModalDialog extends Dialog implements View.OnClickListener {

    private ImageView mClose;
    private OnModalFormClick mListener;
    public LinearLayout mItemContainer;
    private LinearLayout mSuperContainer;
    private Context mContext;
    private Button mBtnCancel;
    private Button mBtnDetermine;
    public TextView mTvTitle;


    public ModalDialog(Context context) {
        this(context, com.android.xlibrary.R.style.quick_option_dialog);
        mContext = context;
    }

    public ModalDialog(Context context,String mobile) {
        this(context, com.android.xlibrary.R.style.quick_option_dialog);
        mContext = context;
    }

    public ModalDialog(Context context, boolean flag, OnCancelListener listener) {
        super(context, flag, listener);
    }


    public void addItem(String text,int tag){
        View view = View.inflate(mContext,R.layout.dialog_modal_item,null);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.item);
        relativeLayout.setOnClickListener(this);
        relativeLayout.setTag(tag);
        TextView textView = (TextView) view.findViewById(R.id.itemtv);
        textView.setText(Html.fromHtml(text));
        if (mItemContainer != null){
            if (mItemContainer.getChildCount() == 0){
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)relativeLayout.getLayoutParams();
                layoutParams.setMargins(0,0,0,0);
                relativeLayout.setLayoutParams(layoutParams);
            }
            mItemContainer.addView(view);
        }
    }


    public void addItem(View itemView,int tag){
        if (mItemContainer != null){
            mItemContainer.addView(itemView);
        }
    }

    public void hideBtn(){
        mBtnCancel.setVisibility(View.GONE);
        mBtnDetermine.setVisibility(View.GONE);
    }


    @SuppressLint("InflateParams")
    private ModalDialog(Context context, int defStyle) {
        super(context, defStyle);
        View dialogModal = getLayoutInflater().inflate(com.android.xlibrary.R.layout.dialog_modal, null);
        mItemContainer = (LinearLayout) dialogModal.findViewById(R.id.itemContainer);
        mSuperContainer = (LinearLayout) dialogModal.findViewById(R.id.superContainer);
        mBtnCancel = (Button) dialogModal.findViewById(R.id.btnCancel);
        mBtnDetermine = (Button) dialogModal.findViewById(R.id.btnDetermine);
        mTvTitle = (TextView) dialogModal.findViewById(R.id.tvTitle);
        mBtnCancel.setOnClickListener(this);
        mBtnDetermine.setOnClickListener(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogModal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ModalDialog.this.dismiss();
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
