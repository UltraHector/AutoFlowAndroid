package com.griffith.autoflow.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.griffith.autoflow.R;

public class LeakAlertFragment extends Fragment {

    private Button button;


    private class ItemSelectListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }



    public static LeakAlertFragment newInstance() {
        LeakAlertFragment usageFrament = new LeakAlertFragment();
        return usageFrament;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_summary, container, false);


        return view;
    }


    private class ViewOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch(view.getId()){

            }
        }
    }

}
