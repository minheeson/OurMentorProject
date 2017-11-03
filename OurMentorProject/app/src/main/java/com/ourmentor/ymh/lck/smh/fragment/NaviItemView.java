package com.ourmentor.ymh.lck.smh.fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ourmentor.ymh.lck.smh.R;
import com.ourmentor.ymh.lck.smh.fragment.NaviItemData;

/**
 * Created by ccei on 2016-01-26.
 */
public class NaviItemView extends FrameLayout{

    public NaviItemView(Context context) {
        super(context);
        init();
    }

    public NaviItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    ImageView imageView;
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.navi_listview, this);
        imageView = (ImageView)findViewById(R.id.navi_listview_item);
    }

    NaviItemData mData;
    public void setItemData(NaviItemData data)
    {
        mData = data;
        imageView.setImageResource(data.navi_listview_imageId);
    }
}
