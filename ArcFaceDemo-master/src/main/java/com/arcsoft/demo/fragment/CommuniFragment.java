package com.arcsoft.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arcsoft.demo.R;

/**
 * Created by Toky on 2018/1/13.
 */

public class CommuniFragment extends BaseFragment {

    public CommuniFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_commu,container,false);
        return view;
    }
}