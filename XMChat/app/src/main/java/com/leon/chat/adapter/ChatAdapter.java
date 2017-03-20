package com.leon.chat.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.leon.chat.R;
import com.leon.chat.bean.ChatItemBean;
import com.leon.chat.bean.ContactBean;
import com.leon.chat.databinding.ActivityChatItemBinding;
import com.leon.chat.databinding.ActivityChatItemMeBinding;
import com.leon.chat.databinding.ActivityChatItemOtherBinding;
import com.leon.chat.databinding.FragmentContactItemBinding;
import com.leon.chat.utils.UnixUtils;
import com.leon.chat.view.RecyclerAdapter;

/**
 * Created by leon on 17/2/8.
 */

public class ChatAdapter extends RecyclerAdapter<ChatItemBean> {

    public ChatAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
    }

    @Override
    public int getItemViewType(int position) {
        return getData().get(position).type;
    }

    @Override
    protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case 0:
                return new MeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chat_item_me,
                        parent, false));
        }
        return new OtherHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chat_item_other,
                parent, false));
    }

    @Override
    protected void convert(BaseViewHolder holder, ChatItemBean item) {
        switch (item.type){
            case 0:
                ((MeHolder) holder).bind(item);
                break;
            case 1:
                ((OtherHolder) holder).bind(item);
                break;
        }

    }


    private class MeHolder extends BaseViewHolder {
        private ActivityChatItemMeBinding mItemBinding;

        public MeHolder(View view) {
            super(view);
            mItemBinding = DataBindingUtil.bind(view);
        }

        public void bind(final ChatItemBean item) {
            mItemBinding.tvMessage.setText(item.message);
            mItemBinding.tvMessageDate.setText(UnixUtils.timestamp2StringAll(Long.valueOf(item.time) / 1000));
        }
    }

    private class OtherHolder extends BaseViewHolder {
        private ActivityChatItemOtherBinding mItemBinding;

        public OtherHolder(View view) {
            super(view);
            mItemBinding = DataBindingUtil.bind(view);
        }

        public void bind(final ChatItemBean item) {
            mItemBinding.tvMessage.setText(item.message);
            mItemBinding.tvMessageDate.setText(UnixUtils.timestamp2StringAll(Long.valueOf(item.time) / 1000));
        }
    }




}
