package com.arcsoft.sdk_demo.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

import com.arcsoft.sdk_demo.R;

/**
 * Created by Toky on 2018/2/4.
 */

public class NewsdetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsdetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        //setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backlog);
        toolbar.setTitleMarginStart(1);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsdetailActivity.this.finish();
            }
        });
        WebView webView = (WebView) findViewById(R.id.webView);
        String url = getIntent().getStringExtra("URL");
        webView.loadUrl(url);
    }
}
