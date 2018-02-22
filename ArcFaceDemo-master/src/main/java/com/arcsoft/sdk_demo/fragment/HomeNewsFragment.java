package com.arcsoft.sdk_demo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.arcsoft.sdk_demo.Activity.NewsdetailActivity;
import com.arcsoft.sdk_demo.Adapter.LPagerAdapter;
import com.arcsoft.sdk_demo.R;
import com.arcsoft.sdk_demo.View.XListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Toky on 2018/1/13.
 */

public class HomeNewsFragment extends BaseFragment implements XListView.IXListViewListener {
    private Context ctx;
    //viewpager初始化
    private ViewPager viewPager;
    private List<View> list;
    private LPagerAdapter adapter;
    //轮播控制
    private Handler handler;
    private Timer timer;
    private TimerTask task;
    private int mPosition;
    private LinearLayout point_container;
    private ImageView[] imgs;
    private List<ImageTip> tips;
    private TextView tip1;
    View view;
    private XListView mListView;
    private SimpleAdapter mAdapter1;//？？？
    private Handler mHandler;
    private ArrayList<HashMap<String, Object>> dlist;
    //初始化
   // private ListView listView;
   // private DiskLruCache mDiskLruCache;
   // private boolean mDiskCacheStarting = true;
    public HomeNewsFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home,container,false);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tip1 = (TextView)view.findViewById(R.id.tv_tip1);
        ctx = getContext();
        initdata();
        initpoint();
        initTip();
        //自定义adapter
        adapter = new LPagerAdapter(ctx, list, new LPagerImgClickListener() {
            @Override
            public void ImgClick(int position) {
            }
        });
        viewPager.setPageTransformer(true,new DepthPageTransformer());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 2) {
                    mPosition = viewPager.getCurrentItem();
                    for (int i = 0; i < list.size(); i++) {
                        imgs[i].setImageResource(R.drawable.login_point);
                    }
                    imgs[mPosition].setImageResource(R.drawable.login_point_selected);
                    tip1.setText(tips.get(mPosition).getTip1());
                }
            }
        });
        imgPlay();
        /** 下拉刷新，上拉加载 */
        dlist = new ArrayList<HashMap<String, Object>>();
        mListView = (XListView) view.findViewById(R.id.techan_xListView);// 这个listview是在这个layout里面
        mListView.setPullLoadEnable(true);// 设置让它上拉，FALSE为不让上拉，便不加载更多数据
        mAdapter1 = new SimpleAdapter(getContext(), getData(),
                R.layout.scenic_item_list, new String[] { "name", "img",
                "content","date" }, new int[] { R.id.title, R.id.img,
                R.id.content,R.id.date});
        mListView.setAdapter(mAdapter1);
        mListView.setXListViewListener(this);
        mHandler = new Handler();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                clickItem(position);
                //Toast.makeText(getApplicationContext(), "您点击了"+data[position-1], Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    private void clickItem(int position) {

        switch (position){
            case 1:
               Intent i1= new Intent(getActivity(), NewsdetailActivity.class);
                i1.putExtra("URL","http://www.cnblogs.com/mengdd/archive/2013/03/01/2938295.html");
                startActivity(i1);
                break;
            case 2:
                Intent i2= new Intent(getActivity(), NewsdetailActivity.class);
                i2.putExtra("URL","http://news.cri.cn/20180213/b17691a1-88d3-fc3a-4f63-4345bf22d0dc.html");
                startActivity(i2);
                break;
            case 3:
                Intent i3= new Intent(getActivity(), NewsdetailActivity.class);
                i3.putExtra("URL","http://news.cri.cn/20180212/bc601b98-ac5d-5e1a-9f72-b0c42a1453f9.html");
                startActivity(i3);
                break;
            case 4:
                Intent i4= new Intent(getActivity(), NewsdetailActivity.class);
                i4.putExtra("URL","http://news.cri.cn/20180213/c4f2d2db-782b-c108-6b89-30b1d4cb5a46.html");
                startActivity(i4);
                break;
            case 5:
                Intent i5= new Intent(getActivity(), NewsdetailActivity.class);
                i5.putExtra("URL","https://item.btime.com/34kvn4ct620907asbupmugc5q30?from=gjl");
                startActivity(i5);
                break;
            case 6:
                Intent i6= new Intent(getActivity(), NewsdetailActivity.class);
                i6.putExtra("URL","https://item.btime.com/31t92jo31gp9p2b87u64a9jo67b?from=gjl");
                startActivity(i6);
                break;

        }

    }

    @SuppressLint("HandlerLeak")
    private void imgPlay() {
        //循环播放
        task = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        };
        timer = new Timer();
        timer.schedule(task, 4000, 3000);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    if (mPosition == (list.size() - 1)) {
                        viewPager.setCurrentItem(0, true);
                    } else {
                        Log.i("LHD", "bbbb:   " + mPosition + "当前的进程ID：  " + Thread.currentThread().getId());
                        viewPager.setCurrentItem(mPosition + 1, true);
                        Log.i("LHD", "cccc:   " + mPosition);
                    }
                }
            }
        };
    }
    private void imgStop() {
        handler.removeMessages(1);
        timer.cancel();
        task.cancel();
    }
    private void initdata() {
        list = new ArrayList<View>();
        ImageView iv = new ImageView(ctx);
        ImageView iv2 = new ImageView(ctx);
        ImageView iv3 = new ImageView(ctx);
        ImageView iv4 = new ImageView(ctx);
        ImageView iv5 = new ImageView(ctx);
        iv.setImageResource(R.drawable.image3a);
        iv2.setImageResource(R.drawable.image4a);
        iv3.setImageResource(R.drawable.image5a);
        iv4.setImageResource(R.drawable.image6a);
        iv5.setImageResource(R.drawable.image7a);
        list.add(iv);
        list.add(iv2);
        list.add(iv3);
        list.add(iv4);
        list.add(iv5);
        imgs = new ImageView[list.size()];
    }
    private void initpoint() {
        point_container = (LinearLayout) view.findViewById(R.id.point_container);
        for (int i = 0; i < list.size(); i++) {
            ImageView imageView = new ImageView(ctx);
            imageView.setImageResource(R.drawable.point2);
            //对布局控件添加相对属性
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    20,
                    20);
            //添加规则，示例 靠父控件最右边
            param.setMargins(10, 0, 0, 0);
            Log.i("LHD", "添加小圆点：" + list.size());
            imgs[i] = imageView;//添加到图片数组
            point_container.addView(imageView, param);
            imgs[0].setImageResource(R.drawable.point1);
        }
    }
    private void initTip() {
        tips = new ArrayList<ImageTip>();
        tips.add(new ImageTip("恭贺新禧"));
        tips.add(new ImageTip("狗年大吉"));
        tips.add(new ImageTip("阖家欢乐"));
        tips.add(new ImageTip("万事如意"));
        tips.add(new ImageTip("给您拜年啦"));
        tip1.setText(tips.get(0).getTip1());

    }
    @Override
    public void onPause() {
        super.onPause();
        Log.i("LHD", "onPause");
        mPosition = viewPager.getCurrentItem();
        imgStop();
    }
//        imgPlay();


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


