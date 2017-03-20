package com.leon.chat.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leon.chat.R;
import com.leon.chat.bean.ContactBean;
import com.leon.chat.bean.SessionBean;
import com.leon.chat.databinding.FragmentContactItemBinding;
import com.leon.chat.view.RecyclerAdapter;

/**
 * Created by leon on 17/2/8.
 */

public class SessionAdapter extends RecyclerAdapter<SessionBean> {

    public SessionAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        return new VideoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_contact_item,
                parent, false));
    }

    @Override
    protected void convert(BaseViewHolder holder, SessionBean item) {
        ((VideoHolder) holder).bind(item);
    }

    private class VideoHolder extends BaseViewHolder {
        private FragmentContactItemBinding mItemBinding;

        public VideoHolder(View view) {
            super(view);
            mItemBinding = DataBindingUtil.bind(view);
        }

        public void bind(final SessionBean item) {
            mItemBinding.tvUserName.setText(item.nickname);
            mItemBinding.tvUserAccount.setText(item.message);
        }
    }


}
