package com.leon.chat.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.LoadingMoreFooter;
import com.leon.chat.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leon on 16/8/4.
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter {

    private static final int VIEW_ITEM = -1;
    private static final int VIEW_PROG = -2;
    private final Context mContext;
    private final RecyclerView mRecyclerView;
    private List<T> mData;
    private final LayoutInflater inflater;
    private boolean isLoading;
    private int totalItemCount;
    private int lastVisibleItemPosition;
    //当前滚动的position下面最小的items的临界值
    private int visibleThreshold = 5;
    private final ArrayList<OnItemClickListener> mOnItemClickListeners =
            new ArrayList<>();
    private final ArrayList<OnItemLongClickListener> mOnItemLongClickListeners =
            new ArrayList<>();


    public RecyclerAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        mRecyclerView = recyclerView;
        if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            //mRecyclerView添加滑动事件监听
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                    LogUtils.sf("totalItemCount =" + totalItemCount + "-----" + "lastVisibleItemPosition =" + lastVisibleItemPosition);
                    if (!isLoading && totalItemCount <= (lastVisibleItemPosition + visibleThreshold)) {
                        //此时是刷新状态
                        if (mMoreDataListener != null)
                            mMoreDataListener.onLoadMoreData();
                        isLoading = true;
                    }
                }
            });
        }

    }

    private boolean isSlideToBottom(RecyclerView recyclerView) {
        return recyclerView != null && recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange();
    }

//    private boolean isSlideToBottom(LinearLayoutManager lm) {
//        return lm.findViewByPosition(lm.findFirstVisibleItemPosition()).getTop()==0 && lm.findFirstVisibleItemPosition()==0;
//    }

    public void setLoaded() {
        isLoading = false;
    }

    public void setNoLoadMore() {
        isLoading = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == VIEW_PROG) {
            holder = new FootViewHolder(new LoadingMoreFooter(mContext));
        } else {
            holder = onCreateDefViewHolder(parent, viewType);
            dispatchItemClickListener((BaseViewHolder) holder);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FootViewHolder) {
        } else {
            convert((BaseViewHolder) holder, mData.get(position));
        }
    }

    abstract protected BaseViewHolder onCreateDefViewHolder(ViewGroup parent, int viewType);

    abstract protected void convert(BaseViewHolder holder, T item);

    public static class FootViewHolder extends RecyclerView.ViewHolder {
        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    //根据不同的数据返回不同的viewType
    @Override
    public int getItemViewType(int position) {
        return mData.get(position) != null ? getDefItemViewType() : VIEW_PROG;
    }


    public int getDefItemViewType() {
        return VIEW_ITEM;
    }


    public interface LoadMoreDataListener {
        void onLoadMoreData();
    }

    public List<T> getData() {
        return mData;
    }

    //设置数据的方法
    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    private LoadMoreDataListener mMoreDataListener;

    //加载更多监听方法
    public void setOnMoreDataLoadListener(LoadMoreDataListener onMoreDataLoadListener) {
        mMoreDataListener = onMoreDataLoadListener;
    }

    // 点击事件监听方法
    public void addOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListeners.add(listener);
    }

    // 长按事件监听方法
    public void addOnItemLongClickListener(OnItemLongClickListener listener) {
        mOnItemLongClickListeners.add(listener);
    }


    private void dispatchItemClickListener(final BaseViewHolder vh) {
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListeners != null && mOnItemClickListeners.size() > 0) {
                    for (int i = 0; i < mOnItemClickListeners.size(); i++) {
                        final OnItemClickListener listener = mOnItemClickListeners.get(i);
                        listener.onItemClick(vh, vh.getLayoutPosition());
                    }
                }
            }
        });
        vh.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListeners != null && mOnItemLongClickListeners.size() > 0) {
                    for (int i = 0; i < mOnItemLongClickListeners.size(); i++) {
                        final OnItemLongClickListener listener = mOnItemLongClickListeners.get(i);
                        listener.onItemLongClick(vh, vh.getLayoutPosition());
                    }
                    return true;
                }
                return false;
            }
        });
    }


    public static class BaseViewHolder extends RecyclerView.ViewHolder {

        private final SparseArray<View> mViews;

        public void bind(Object item) {

        }

        public BaseViewHolder(View view) {
            super(view);
            this.mViews = new SparseArray<>();
        }

        @SuppressWarnings("unchecked")
        public <Type extends View> Type findViewById(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (Type) view;
        }

    }

    public interface OnItemClickListener {
        void onItemClick(BaseViewHolder vh, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(BaseViewHolder vh, int position);
    }




}