package com.example.first.kaganmoshe.brainy;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.first.kaganmoshe.brainy.CrazyCube.CrazyCubeActivity;
import com.example.first.kaganmoshe.brainy.GuessTheNumber.GuessTheNumberConfigActivity;


public class MenuActivity extends Activity {

    private Button m_CrazyCubeBtn;
    private Button m_GuessTheNumberBtn;
    private Button m_SettingsBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initViewActivity();
    }

    private void initViewActivity() {
        m_CrazyCubeBtn = (Button) findViewById(R.id.CrazyCubeBtn);
        m_CrazyCubeBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                onCrazyCubeClick();
            }
        });

        m_GuessTheNumberBtn = (Button) findViewById(R.id.guessTheNumberBtn);
        m_GuessTheNumberBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onGuessTheNumberClick();
            }
        });

        m_SettingsBtn = (Button) findViewById(R.id.settingsBtn);
        m_SettingsBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSettingsClick();
            }
        });
    }

    private void onCrazyCubeClick() {
        //TODO - make code reusable for every click
        Intent intent = new Intent(this, CrazyCubeActivity.class);
        intent.putExtra("Message", "");
        startActivity(intent);
    }

    private void onSettingsClick() {
        //TODO - Update settings
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("Message", "");
        startActivity(intent);
    }

    private void onGuessTheNumberClick() {
        //TODO - Update settings
        Intent intent = new Intent(this, GuessTheNumberConfigActivity.class);
        intent.putExtra("Message", "");
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
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
}
