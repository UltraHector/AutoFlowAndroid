package com.griffith.autoflow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.griffith.autoflow.constant.Constant;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoginActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;

    private Button loginButton;
    private String username, userpassword, mUseremail;
    private EditText emailEt, passworEt;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEt = (EditText) findViewById(R.id.activity_login_email_et);
        passworEt = (EditText) findViewById(R.id.activity_login_password_et);

        loginButton = (Button) findViewById(R.id.activity_login_btn_signin);
        loginButton.setOnClickListener(new LoginClickListener());

        // redirect to the usercontent activity if the user already logged in
        SharedPreferences sharedPref = getSharedPreferences(Constant.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        String username = sharedPref.getString(Constant.SHARED_PREFERENCE_USERNAME, "");
        if(username.length() > 0){
            Intent myIntent = new Intent(LoginActivity.this, UserContentActivity.class);
            startActivity(myIntent);
            finish();
        }
    }


    private class LoginClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            username = emailEt.getText().toString();;
            userpassword = passworEt.getText().toString();

            mAuthTask = new UserLoginTask();
            mAuthTask.execute();
        }
    }


    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(LoginActivity.this);
            //mProgressDialog.setTitle("Login");
            mProgressDialog.setMessage("Logging in...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String urlString = "http://13.210.68.199/personal/api/login";
            InputStream in = null;
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                String urlParameters  = "username=" + username;
                urlParameters+= "&password=" + userpassword;
                byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8 );
                int    postDataLength = postData.length;

                conn.setDoOutput( true );
                conn.setInstanceFollowRedirects( false );
                conn.setRequestMethod( "POST" );
                conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty( "charset", "utf-8");
                conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
                conn.setUseCaches( false );
                try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
                    wr.write( postData );
                }

                // get response
                in = new BufferedInputStream(conn.getInputStream());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            try {
                String  responseJsonString = IOUtils.toString(in, "UTF-8");

                JSONObject jsonObject = new JSONObject(responseJsonString);
                if(jsonObject.getBoolean("authenticated")){
                    username = jsonObject.getString("username");
                    mUseremail = jsonObject.getString("useremail");
                    return true;
                }else{
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mProgressDialog.dismiss();

            mAuthTask = null;
            if (success) {
                // save login details here
                saveLoginToSharedPreference();
                // redirect to usercontent activity
                Intent myIntent = new Intent(LoginActivity.this, UserContentActivity.class);
                startActivity(myIntent);
                finish();
            } else {
                passworEt.setError(getString(R.string.error_incorrect_password));
                passworEt.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mProgressDialog.dismiss();
            mAuthTask = null;
            //showProgress(false);
        }
    }

    private void saveLoginToSharedPreference(){
        SharedPreferences sharedPref = getSharedPreferences(Constant.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constant.SHARED_PREFERENCE_USERNAME, username);
        editor.putString(Constant.SHARED_PREFERENCE_USEREMAIL, mUseremail);
        editor.commit();
    }


}
