package com.example.first.kaganmoshe.brainy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.CustomActivity.CustomActivity;

import Utils.Logs;


public class LoginActivity extends CustomActivity {
//    private static final String LOGIN_TOOLBAR_TEXT = "Login";
    private static final String LOGIN_ACTIVITY = "LoginActivity";
//    private Button m_loginButton;
    private TextView loginTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViewActivity();
    }

    private void initViewActivity() {
//        toolbarText = (TextView) findViewById(R.id.toolbarText);
//        toolbarText.setText(LOGIN_TOOLBAR_TEXT);
        setTouchNClick(R.id.RegisterButton);
        setTouchNClick(R.id.loginButton);

        loginTitle = (TextView) findViewById(R.id.loginTitle);
        Utils.changeFont(getAssets(), loginTitle);

        Logs.info(LOGIN_ACTIVITY, " geting the first instace of AppManager");
        AppManager.getInstance(); // Create the appManager
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        //getMenuInflater().inflate(R.menu.menu_login, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    private void onConnectClick(){
//        Intent intent = new Intent(this, SettingsActivity.class);
//        intent.putExtra("Message", "");
//        startActivity(intent);
//    }

    @Override
    public void onClick(View v){
        super.onClick(v);
        if (v.getId() == R.id.loginButton)
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("Message", "");
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
