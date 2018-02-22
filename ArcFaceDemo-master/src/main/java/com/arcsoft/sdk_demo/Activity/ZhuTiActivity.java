package com.arcsoft.sdk_demo.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.arcsoft.sdk_demo.R;

public class ZhuTiActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhu_ti);
        Toolbar toolbar3= (Toolbar) findViewById(R.id.toolbar3);
        toolbar3.setNavigationIcon(R.drawable.backlog);
        toolbar3.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
