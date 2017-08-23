package com.griffith.autoflow.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.griffith.autoflow.R;
import com.griffith.autoflow.constant.Constant;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ProgressDialog mProgressDialog;
    private Activity activity;
    private PieChart usagePieChart;

    private List<Float> usageVolumes;

    //private LoadUsageDataTask mLoadUsageDataTask = null;


    private String mUsername;

    public static HomeFragment newInstance() {
        HomeFragment usageFrament = new HomeFragment();
        return usageFrament;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        /**
         * Draw the usage Level chart
         */
        usagePieChart = (PieChart) view.findViewById(R.id.fragment_home_result_usage_chart);


        activity = getActivity();

        SharedPreferences sharedPref = activity.getSharedPreferences(Constant.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        mUsername = sharedPref.getString(Constant.SHARED_PREFERENCE_USERNAME, "");

        /*mLoadUsageDataTask = new LoadUsageDataTask();
        mLoadUsageDataTask.execute();*/

        return view;
    }

}
