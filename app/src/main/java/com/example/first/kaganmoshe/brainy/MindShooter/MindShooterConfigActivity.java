package com.example.first.kaganmoshe.brainy.MindShooter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.first.kaganmoshe.brainy.CustomActivity.GameConfigActivity;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Utils;

public class MindShooterConfigActivity extends GameConfigActivity {

    // Const Members
    public static final String TIME_TO_PLAY = "Time To Play";
    public static final long ONE_MIN = 60000L;

    // Data Members
    private Button m_StartGameBtn; ///////tamir
    private long timeToPlayInMilSec = ONE_MIN;


    ////////tamir start////////

    // Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mind_shooter_config);
//
//        m_StartGameBtn = (Button) findViewById(R.id.startGameButton);
//
//        setTouchNClick(R.id.startGameButton);
//        m_StartGameBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onStarGameClick();
//            }
//        });
    }
    @Override
    protected void onStart(){
        super.onStart();
    }

    //////tamir end
    @Override
    protected void onStartGameClick() {
        Intent intent = new Intent(getApplicationContext(), MindShooterGameActivity.class);
        intent.putExtra(TIME_TO_PLAY, timeToPlayInMilSec);
        Utils.startNewActivity(this, intent);
    }

//    @Override             ///////TAMIR///////
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_habconfig, menu);
//        return true;
//    }
//
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
}
