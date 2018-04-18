package com.arcsoft.demo.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import com.arcsoft.demo.R;
import com.arcsoft.demo.View.XListView;

import java.util.ArrayList;
import java.util.HashMap;

public class NewsActivity extends Activity implements XListView.IXListViewListener {
    private XListView mListView;
    private SimpleAdapter mAdapter1;//？？？
    private Handler mHandler;
    private ArrayList<HashMap<String, Object>> dlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newslist);
        Toolbar toolbar3 = (Toolbar) findViewById(R.id.toolbar3);
        toolbar3.setNavigationIcon(R.drawable.backlog);
        toolbar3.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /** 下拉刷新，上拉加载 */
        dlist = new ArrayList<HashMap<String, Object>>();
        mListView = (XListView)findViewById(R.id.techan_xListView);// 这个listview是在这个layout里面
        mListView.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
        mAdapter1 = new SimpleAdapter(this, getData(), R.layout.scenic_item_list, new String[]{"name", "img", "content", "date"}, new int[]{R.id.title, R.id.img, R.id.content, R.id.date});
        mListView.setAdapter(mAdapter1);
        mListView.setXListViewListener(this);
        mHandler = new Handler();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clickItem(position);
            }
        });
    }
    private void clickItem(int position) {
        switch (position) {
            case 1:
                Intent i1 = new Intent(NewsActivity.this, NewsdetailActivity.class);
                i1.putExtra("URL", "http://www.cnblogs.com/mengdd/archive/2013/03/01/2938295.html");
                startActivity(i1);
                break;
            case 2:
                Intent i2 = new Intent(NewsActivity.this, NewsdetailActivity.class);
                i2.putExtra("URL", "http://news.cri.cn/20180213/b17691a1-88d3-fc3a-4f63-4345bf22d0dc.html");
                startActivity(i2);
                break;
            case 3:
                Intent i3 = new Intent(NewsActivity.this, NewsdetailActivity.class);
                i3.putExtra("URL", "http://news.cri.cn/20180212/bc601b98-ac5d-5e1a-9f72-b0c42a1453f9.html");
                startActivity(i3);
                break;
            case 4:
                Intent i4 = new Intent(NewsActivity.this, NewsdetailActivity.class);
                i4.putExtra("URL", "http://news.cri.cn/20180213/c4f2d2db-782b-c108-6b89-30b1d4cb5a46.html");
                startActivity(i4);
                break;
            case 5:
                Intent i5 = new Intent(NewsActivity.this, NewsdetailActivity.class);
                i5.putExtra("URL", "https://item.btime.com/34kvn4ct620907asbupmugc5q30?from=gjl");
                startActivity(i5);
                break;
            case 6:
                Intent i6 = new Intent(NewsActivity.this, NewsdetailActivity.class);
                i6.putExtra("URL", "https://item.btime.com/31t92jo31gp9p2b87u64a9jo67b?from=gjl");
                startActivity(i6);
                break;

        }
    }
    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
                mListView.setAdapter(mAdapter1);
                onLoad();
            }
        }, 2000);
    }
    @Override
    public void onLoadMore() {//停止刷新
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                getData();
                mAdapter1.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);
    }

    /** 停止刷新， */
    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime("刚刚");
    }

    /** 初始化本地数据 */
    String data[] = new String[] { "Android WebView使用基础","特朗普政府公布1.5亿美元基建计划",  "俄罗斯客机失事",
            "伊拉克重建国际会议在科威特开幕","勿忘真情,习近平深情叮嘱你回家过年", "熊孩子飞机上狂吼8小时" };
    String data1[] = new String[] { "Android WebView使用基础","计划未来10年内利用２０００亿美元联邦资金撬动1.5万亿美元的地方政府和社会投资",  "俄失事客机坠地后爆炸起火 残骸散落区达30公顷",
            "新华社科威特城2月12日电（记者王薇聂云鹏）为期三天的伊拉克重建国际会议12日在科威特城开幕","农历腊月二十七，习近平来到成都市郫都区战旗村，向全体村民和全国人民拜年", "熊孩子飞机上狂吼8小时 乘客崩溃：好像做噩梦" };
    String data2[]=new String[] {"2013-3-1","2018-2-13","2018-2-12","2018-2-13","2018-2-17","2018-2-17"};

    int bitmaps[]=new int[]{R.drawable.news1,R.drawable.news2,R.drawable.news3,R.drawable.news4,R.drawable.news5,R.drawable.news6};

    private ArrayList<HashMap<String, Object>> getData() {

        for (int i = 0; i < data.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("name", data[i]);
            map.put("content", data1[i]);
            map.put("date",data2[i]);
            map.put("img", bitmaps[i]);
            dlist.add(map);
        }
        return dlist;
    }
}