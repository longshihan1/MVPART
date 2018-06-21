package com.longshihan.module_gank.mvp.MainModule.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longshihan.module_gank.R;


/**
 * Created by longshihan on 2017/9/10.
 */

public class WelfareFragment extends Fragment {


    public WelfareFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welfare_gank, container, false);
    }

}
