package com.griffith.autoflow.fragment;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

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
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private ProgressDialog mProgressDialog;
    private Activity activity;
    private PieChart usagePieChart;

    private static EditText startDateEt, endDateEt;

    private List<Float> usageVolumes;

    private LoadUsageDataTask mLoadUsageDataTask = null;


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


        startDateEt = (EditText) view.findViewById(R.id.fragment_home_start_date_et);
        startDateEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment startDateFragment = new StartDatePickerFragment();
                    startDateFragment.show(getFragmentManager(), "datePicker");
                }
            }
        });

        endDateEt = (EditText) view.findViewById(R.id.fragment_home_end_date_et);
        endDateEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    DialogFragment endDateFragment = new EndDatePickerFragment();
                    endDateFragment.show(getFragmentManager(), "datePicker");
                }
            }
        });

        mLoadUsageDataTask = new LoadUsageDataTask();
        mLoadUsageDataTask.execute();

        return view;
    }

    private class LoadUsageDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(activity);
            //mProgressDialog.setTitle("Login");
            mProgressDialog.setMessage("Fetching Data...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            String urlString = Constant.SERVER_IP + "/personal/api/usage?username=";
            urlString += mUsername;
            InputStream in = null;
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // get response
                in = new BufferedInputStream(conn.getInputStream());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
                String responseJsonString = IOUtils.toString(in, "UTF-8");
                return responseJsonString;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(final String responseJsonString) {
            mProgressDialog.dismiss();
            mLoadUsageDataTask = null;

            try {
                JSONObject jsonObject = new JSONObject(responseJsonString);
                JSONArray volArray = jsonObject.getJSONArray("vol");
                usageVolumes = new ArrayList<Float>();
                for (int i = 0; i < volArray.length(); i++) {
                    double vol = volArray.getDouble(i);
                    usageVolumes.add((float)vol);
                }
                drawUsageChart();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }


        @Override
        protected void onCancelled() {
            mProgressDialog.dismiss();
            mLoadUsageDataTask = null;
            //showProgress(false);
        }
    }

    private void drawUsageChart() {
        float total = usageVolumes.get(0)
                + usageVolumes.get(1)
                + usageVolumes.get(2)
                + usageVolumes.get(3)
                + usageVolumes.get(4)
                + usageVolumes.get(5)
                + usageVolumes.get(6)
                + usageVolumes.get(7)
                + usageVolumes.get(8)
                + usageVolumes.get(9);


        ArrayList<PieEntry> usageEntries = new ArrayList<>();
        usageEntries.add(new PieEntry(usageVolumes.get(0) * 100/total, "Shower", 0));
        usageEntries.add(new PieEntry(usageVolumes.get(1) * 100/total, "Tap", 1));
        usageEntries.add(new PieEntry(usageVolumes.get(2)* 100/total, "Clothes washer", 2));
        usageEntries.add(new PieEntry(usageVolumes.get(3) * 100/total, "Dishwasher", 3));
        usageEntries.add(new PieEntry(usageVolumes.get(4) * 100/total, "Toilet", 4));
        usageEntries.add(new PieEntry(usageVolumes.get(5)* 100/total, "Bathtub", 5));
        usageEntries.add(new PieEntry(usageVolumes.get(6) * 100/total, "Irrigation", 6));
        usageEntries.add(new PieEntry(usageVolumes.get(7) * 100/total, "Evap cooler", 7));
        usageEntries.add(new PieEntry(usageVolumes.get(8)* 100/total, "Leak", 8));
        usageEntries.add(new PieEntry(usageVolumes.get(9)* 100/total, "Leak", 9));

        PieDataSet usageDataset = new PieDataSet(usageEntries, "# of Calls");

        PieData usageData = new PieData();
        usageData.setDataSet(usageDataset);
        usageDataset.setColors(ColorTemplate.COLORFUL_COLORS); //

        usagePieChart.setCenterText("usage Level");
        usagePieChart.setData(usageData);
        usagePieChart.getDescription().setEnabled(false);
        usagePieChart.setDrawSliceText(false);
        usagePieChart.animateY(2000);

        usagePieChart.saveToGallery("/sd/mychart.jpg", 85); // 85 is the quality of the image

    }



    /**
     * implement the date picker
     */
    public static class StartDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            startDateEt.setText(view.getDayOfMonth() + "/" + view.getMonth() + "/" + view.getYear());
        }
    }
    public static class EndDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            endDateEt.setText(view.getDayOfMonth() + "/" + view.getMonth() + "/" + view.getYear());
        }
    }

}

