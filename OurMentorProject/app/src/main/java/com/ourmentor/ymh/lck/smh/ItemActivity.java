package com.ourmentor.ymh.lck.smh;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class ItemActivity extends AppCompatActivity {
    private ImageView item_navi_btn;
    private int itemNo;
    private ImageView itemImg[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        item_navi_btn = (ImageView)findViewById(R.id.item_navi_btn);
        itemImg = new ImageView[12];

        for(int i =0 ; i<12; i ++)
        {
            itemImg[i] = (ImageView)findViewById(R.id.itemImg01+i);
        }
        Intent intent = getIntent();
        itemNo=intent.getIntExtra("itemNo",0);

        for(int i = 0 ; i<itemNo; i++)
        {
            itemImg[i].setImageResource(R.drawable.item01+i);
        }
        item_navi_btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                onBackPressed();
                return true;
            }
        });
    }
}
