package com.android.xlibrary.weixinPic.imageloager;


import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


import com.android.xlibrary.R;
import com.android.xlibrary.weixinPic.bean.ImageFloder;
import com.android.xlibrary.weixinPic.utils.BasePopupWindowForListView;
import com.android.xlibrary.weixinPic.utils.CommonAdapter;
import com.android.xlibrary.weixinPic.utils.ViewHolder;

import java.util.List;


public class ListImageDirPopupWindow extends
        BasePopupWindowForListView<ImageFloder> {
    private ListView mListDir;

    public ListImageDirPopupWindow(int width, int height,
                                   List<ImageFloder> datas, View convertView) {

        super(convertView, width, height, true, datas);
    }

    @Override
    public void initViews() {
        mListDir = (ListView) findViewById(R.id.pic_list_dir);
        mListDir.setAdapter(new CommonAdapter<ImageFloder>(context, mDatas,
                R.layout.pic_list_dir_item) {
            @Override
            public void convert(ViewHolder helper, ImageFloder item) {
                helper.setText(R.id.pic_dir_item_name,
                        item.getName().replace("/", ""));
                if (item.getFirstImagePath() != null) {
                    helper.setImageByUrl(R.id.pic_dir_item_image,
                            item.getFirstImagePath());
                    if (helper.getPosition() == 0) {
                        helper.setText(R.id.pic_dir_item_count, null);
                    } else {
                        helper.setText(R.id.pic_dir_item_count, item.getCount()
                                + "张");
                    }
                }
            }
        });

    }

    public interface OnImageDirSelected {
        void selected(ImageFloder floder, boolean isAllPic);
    }

    private OnImageDirSelected mImageDirSelected;

    public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected) {
        this.mImageDirSelected = mImageDirSelected;
    }

    @Override
    public void initEvents() {
        mListDir.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (mImageDirSelected != null && position != 0) {
                    mImageDirSelected.selected(mDatas.get(position), false);
                } else {
                    mImageDirSelected.selected(mDatas.get(position), true);
                }
            }
        });
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void beforeInitWeNeedSomeParams(Object... params) {
        // TODO Auto-generated method stub
    }

}
