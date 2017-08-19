package com.griffith.autoflow;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;

    private Button loginButton;
    private String username, useremail;
    private EditText emailEt, passworEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEt = (EditText) findViewById(R.id.activity_login_email_et);
        passworEt = (EditText) findViewById(R.id.activity_login_password_et);

        loginButton = (Button) findViewById(R.id.activity_login_btn_signin);
        loginButton.setOnClickListener(new LoginClickListener());
    }


    private class LoginClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mAuthTask = new UserLoginTask();
            mAuthTask.execute();
        }
    }


    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            String urlString = "http://10.0.0.9:8000/personal/api/login";
            InputStream in = null;
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                in = new BufferedInputStream(urlConnection.getInputStream());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
                String responseJsonString = IOUtils.toString(in, "UTF-8");
                JSONObject jsonObject = new JSONObject(responseJsonString);
                if(jsonObject.getBoolean("authenticated")){
                    username = jsonObject.getString("username");
                    useremail = jsonObject.getString("useremail");
                    return true;
                }else{
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
                Intent myIntent = new Intent(LoginActivity.this, UserContentActivity.class);
                myIntent.putExtra("username", username);
                myIntent.putExtra("useremail", useremail);
                startActivity(myIntent);
                finish();
            } else {
                passworEt.setError(getString(R.string.error_incorrect_password));
                passworEt.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
        }
    }
}
