package com.frico.usct.ui.activity.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.frico.usct.R;
import com.frico.usct.ui.activity.adapter.base.BaseRVAdapter;
import com.frico.usct.ui.activity.adapter.base.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.util.List;

/**
 * Created by hongchuangwang on 2018/6/14.
 */
public class MyPublicRecAdapter extends BaseRVAdapter<String> {

    private List<String> mPaths;
    private Context context;
    private ItemRourseClickListener onItemDoSomeClickListener;
    private String drawableUrl;

    /**
     * 构造器
     *
     * @param context 上下文
     * @param list    数据集合
     */
    public MyPublicRecAdapter(Context context, List<String> list) {
        super(context, list);
        this.context = context;
        this.mPaths = list;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_advice_image;
    }

    @Override
    public void onBind(BaseViewHolder holder, int position) {
        ImageView mDel_img = holder.getImageView(R.id.item_publish_del);
        ImageView mImg = holder.getImageView(R.id.item_publish_image);

        mImg.setBackgroundResource(0);

        if (!TextUtils.isEmpty(mPaths.get(position)))
            if (mPaths.get(position).startsWith("http")) {
                ImageLoader.getInstance().displayImage(mPaths.get(position), mImg);
            } else {
                if (position == mPaths.size() - 1) {
                    //加载默认图片
                    drawableUrl = ImageDownloader.Scheme.DRAWABLE.wrap(mPaths.get(position));
                } else {
                    drawableUrl = ImageDownloader.Scheme.FILE.wrap(mPaths.get(position));
                }

                ImageLoader.getInstance().displayImage(drawableUrl, mImg);
            }

        if (position == mPaths.size() - 1) {
            mDel_img.setVisibility(View.INVISIBLE);
        } else {
            mDel_img.setVisibility(View.VISIBLE);
        }

        mImg.setTag(position);
        mDel_img.setTag(position);
        mImg.setOnClickListener(view -> {
            if (onItemDoSomeClickListener != null) {
                onItemDoSomeClickListener.onItemChooseClickListener(position, view);
            }
        });

        mDel_img.setOnClickListener(view -> {
            if (onItemDoSomeClickListener != null) {
                onItemDoSomeClickListener.onItemDeteleClickListener(position, view);
            }
        });
    }

    //各种按钮点击回调
    public interface ItemRourseClickListener {
        //删除
        void onItemDeteleClickListener(int position, View view);

        //增加
        void onItemChooseClickListener(int position, View view);
    }

    //供外部调用实现回调
    public void setOnItemRourseClickListener(ItemRourseClickListener listener) {
        onItemDoSomeClickListener = listener;
    }
}
