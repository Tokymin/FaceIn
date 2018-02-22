package com.arcsoft.sdk_demo.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arcsoft.sdk_demo.DemoCache;
import com.arcsoft.sdk_demo.R;
import com.arcsoft.sdk_demo.contact.activity.UserProfileSettingActivity;
import com.arcsoft.sdk_demo.fragment.HomeNewsFragment;
import com.arcsoft.sdk_demo.fragment.TableFragment;
import com.arcsoft.sdk_demo.main.model.Extras;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.List;

public class SlindingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    Handler mHandler;
    private List<String> mDatas;
    private List<View> mViews;
    private FrameLayout frameLayout;
    private RadioGroup radioGroup;
    private Fragment[] mFragments;
    private int mIndex;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private final String TAG = this.getClass().toString();
    private String userID;
    private String account;

    private HeadImageView userHead;

    private TextView nickText;
    private TextView signatureText;

    private NimUserInfo userInfo;
    AbortableFuture<String> uploadAvatarFuture;

    public static void start(Context context, String account) {
        Intent intent = new Intent();
        intent.setClass(context, SlindingActivity.class);
        intent.putExtra(Extras.EXTRA_ACCOUNT, account);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slinding);
//        getUserInfo();
//        ToolBarOptions options = new NimToolBarOptions();
//        options.titleId = R.string.FaceIn;
//        setToolBar(R.id.toolbar, options);

        account = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Log.e("SLIDing","step3"+toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawer.closeDrawers();
                return true;
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        UserProfileSettingActivity.start(SlindingActivity.this, DemoCache.getAccount());
                        drawer.closeDrawers();
            }
        });
        initFragment();
        setRadioGroupListener();
        View layoutHeader = findViewById(R.id.header);

        userHead =headerView.findViewById(R.id.head);
        Log.e("SLIDing","step1"+userHead);
        nickText = headerView.findViewById(R.id.username);
        Log.e("SLIDing","step1"+nickText);
        signatureText =headerView. findViewById(R.id.qianmm);
    }
    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }
    private void findViews() {


    }
    private void getUserInfo() {
        userInfo = (NimUserInfo) NimUIKit.getUserInfoProvider().getUserInfo(account);
        if (userInfo == null) {

            NimUIKit.getUserInfoProvider().getUserInfoAsync(account, new SimpleCallback<NimUserInfo>() {

                @Override
                public void onResult(boolean success, NimUserInfo result, int code) {
                    if (success) {
                        userInfo = result;
                        Log.e("SLIDing","step2"+result);
                        updateUI();
                    } else {
                        Toast.makeText(SlindingActivity.this, "getUserInfoFromRemote failed:" + code, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Log.e("SLIDing","step3");

            updateUI();
        }
    }

    private void updateUI() {
        Log.e("SLIDing","step4"+account);
        userHead.loadBuddyAvatar(account);
        nickText.setText(userInfo.getName());
        if (userInfo.getSignature() != null) {
            signatureText.setText(userInfo.getSignature());
        }
    }


    //几个大的Fragment的切换
    private void initFragment() {
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        frameLayout = (FrameLayout) findViewById(R.id.fl_content);
        HomeNewsFragment homeNewsFragment = new HomeNewsFragment();
        TableFragment tableFragment = new TableFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userID", userID);
        tableFragment.setArguments(bundle);
        //添加到数组
        mFragments = new Fragment[]{homeNewsFragment, tableFragment};
        //开启事务
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //添加首页
        ft.add(R.id.fl_content, homeNewsFragment).commit();
        //默认设置为第1个课程表
        setIndexSelected(0);
    }

    private void setIndexSelected(int index) {
        if (mIndex == index) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //隐藏
        ft.hide(mFragments[mIndex]);
        //判断是否添加
        if (!mFragments[index].isAdded()) {
            ft.add(R.id.fl_content, mFragments[index]).show(mFragments[index]);
        } else {
            ft.show(mFragments[index]);
        }
        ft.commit();
        //再次赋值
        mIndex = index;
    }

    //底部几个单选按钮
    public void setRadioGroupListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                boolean flag = false;
                switch (i) {
                    case R.id.rb_table:
                        toolbar.setVisibility(flag ? View.VISIBLE : View.GONE);
                        setIndexSelected(1);
                        flag = true;
                        break;
                    case R.id.rb_home:
                        flag = true;
                        toolbar.setVisibility(flag ? View.VISIBLE : View.GONE);
                        toolbar.setTitle("江理新闻");
                        setIndexSelected(0);
                        break;
                    case R.id.rb_communi:
                        Intent intent = new Intent(SlindingActivity.this, com.arcsoft.sdk_demo.main.activity.MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.rb_more:
                        startActivity(new Intent(SlindingActivity.this, com.arcsoft.sdk_demo.main.activity.SettingsActivity.class));
                        break;

                    default:
                        flag = true;
                        toolbar.setVisibility(flag ? View.VISIBLE : View.GONE);
                        setIndexSelected(0);
                        break;
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //仅当activity为task根（即首个启动activity）时才生效,这个方法不会改变task中的activity状态，
            // 按下返回键的作用跟按下HOME效果一样；重新点击应用还是回到应用退出前的状态；
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    //返回按钮，我也不知道
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.slinding, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sys) {//要写一个扫一扫界面
            return true;
        }
        if (id == R.id.action_tj) {//添加个啥？？
        }
        return super.onOptionsItemSelected(item);
    }

    //左侧菜单栏的点击事件
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_shoucang) {
            Intent intent = new Intent(SlindingActivity.this, com.arcsoft.sdk_demo.main.activity.MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_biji) {
            Intent intent = new Intent(SlindingActivity.this, NoteActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_cahxun) {
            Intent i = new Intent(getApplicationContext(), NewsdetailActivity.class);
            i.putExtra("URL", "http://jw.jxust.edu.cn");
            startActivity(i);
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(SlindingActivity.this, SettingsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        this.finish();
        return super.onKeyUp(keyCode, event);
    }
}


