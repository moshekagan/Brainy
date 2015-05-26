package com.example.first.kaganmoshe.brainy;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class LoginActivity extends ActionBarActivity {

    private Button m_ConnectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        m_ConnectBtn = (Button) findViewById(R.id.connectBtn);
        m_ConnectBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onConnectClick();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onConnectClick(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Message","");
        startActivity(intent);
    }
}
