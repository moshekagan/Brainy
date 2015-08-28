package com.example.first.kaganmoshe.brainy;

import android.os.Bundle;
import android.view.View;

import com.example.first.kaganmoshe.brainy.CustomActivity.AppActivity;

import Utils.Logs;


public class LoginActivity extends AppActivity {
    private static final String LOGIN_ACTIVITY = "LoginActivity";
//    private TextView loginTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViewActivity();
    }

    private void initViewActivity() {
        setTouchNClick(R.id.RegisterButton);
        setTouchNClick(R.id.loginButton);

//        loginTitle = (TextView) findViewById(R.id.loginTitle);
//        Utils.changeFont(getAssets(), loginTitle);

        Logs.info(LOGIN_ACTIVITY, " geting the first instace of AppManager");
        AppManager.getInstance(); // Create the appManager
        AppManager.getInstance().setBackgroundMusic(getApplicationContext());
        AppManager.getInstance().playBackgroundMusic();
    }

    @Override
    public void onClick(View v){
        super.onClick(v);
        if (v.getId() == R.id.loginButton)
        {
            Utils.startNewActivity(this, ConnectionActivity.class);
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }

}
