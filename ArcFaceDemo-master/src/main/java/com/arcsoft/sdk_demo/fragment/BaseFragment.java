package com.arcsoft.sdk_demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Toky on 2018/1/13.
 */

public abstract class BaseFragment extends Fragment {

    protected Context mContext;

    public BaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
    }

}
