package com.griffith.autoflow;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;

    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button)findViewById(R.id.activity_login_btn_signin);
        loginButton.setOnClickListener(new LoginClickListener());
    }



    private class LoginClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            mAuthTask = new UserLoginTask();
            mAuthTask.execute();
        }
    }


    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
                Intent myIntent = new Intent(LoginActivity.this, UserContentActivity.class);
                LoginActivity.this.startActivity(myIntent);

            } else {
                //mPasswordView.setError(getString(R.string.error_incorrect_password));
                //mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
        }
    }
}
