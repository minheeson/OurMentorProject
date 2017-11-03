package com.ourmentor.ymh.lck.smh.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ourmentor.ymh.lck.smh.R;
import com.ourmentor.ymh.lck.smh.common.RankObject;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPagerFragment extends Fragment {
    private RankObject rankObject;
    private String title;
    private CircleImageView viewPager_img;
    private TextView viewPager_textView_name;
    private TextView viewPager_textView_title;
    private TextView viewPager_textView_hashTag01;
    private TextView viewPager_textView_hashTag02;
    private TextView viewPager_textView_hashTag03;
    private int userNo;

    public ViewPagerFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_view_pager, container, false);

        Bundle b = getArguments();
        rankObject = new RankObject();
        rankObject = b.getParcelable("object");
        title = b.getString("title");

        viewPager_img = (CircleImageView)v.findViewById(R.id.viewPager_img);
        viewPager_textView_name = (TextView)v.findViewById(R.id.viewPager_textView_name);
        viewPager_textView_title = (TextView)v.findViewById(R.id.viewPager_textView_title);
        viewPager_textView_hashTag01 = (TextView)v.findViewById(R.id.viewPager_textView_hashTag01);
        viewPager_textView_hashTag02 = (TextView)v.findViewById(R.id.viewPager_textView_hashTag02);
        viewPager_textView_hashTag03 = (TextView)v.findViewById(R.id.viewPager_textView_hashTag03);

        Glide.with(getContext()).load(rankObject.pic).into(viewPager_img);
        viewPager_textView_name.setText(rankObject.name);
        viewPager_textView_title.setText(title);
        viewPager_textView_hashTag01.setText(rankObject.h_tag1);
        viewPager_textView_hashTag02.setText(rankObject.h_tag2);
        viewPager_textView_hashTag03.setText(rankObject.h_tag3);
        userNo = rankObject.userno;
        return v;
    }


}
