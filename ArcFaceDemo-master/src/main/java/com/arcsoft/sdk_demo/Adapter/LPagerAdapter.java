package com.arcsoft.sdk_demo.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.arcsoft.sdk_demo.fragment.LPagerImgClickListener;

import java.util.List;

/**
 * Created by LHD on 2016/6/29.
 */
public class LPagerAdapter extends PagerAdapter {

    private Context ctx;
    private List<View> mlist;
    private LPagerImgClickListener mlistener;

    public LPagerAdapter(Context ctx, List<View> mlist, LPagerImgClickListener listener) {
        this.ctx = ctx;
        this.mlist = mlist;
        this.mlistener = listener;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        container.addView(mlist.get(position));
        mlist.get(position).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ctx, "点击了" + position, Toast.LENGTH_SHORT).show();
                if (mlistener != null) {
                    mlistener.ImgClick(position);
                }
            }
        });
        return mlist.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mlist.get(position));
    }
}
