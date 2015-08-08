package com.example.first.kaganmoshe.brainy.MindShooter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.first.kaganmoshe.brainy.CustomActivity.GameActivity;
import com.example.first.kaganmoshe.brainy.R;

import EEG.EConnectionState;
import EEG.ESignalVolume;

public class MindShooterGameActivity extends GameActivity {

    // Data Members

    // Methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maind_shooter_game);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maind_shooter_game, menu);
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

    // GameActivity Override Methods
    @Override
    protected void startFeedbackSession() {

    }

    @Override
    protected void onMenuPopupShow() {

    }

    @Override
    protected void onMenuPopupDismiss() {

    }

    @Override
    public void onAttentionReceived(int attValue) {

    }

    @Override
    public void onMeditationReceived(int medValue) {

    }

    @Override
    public void onHeadSetChangedState(String headSetName, EConnectionState connectionState) {

    }

    @Override
    public void onPoorSignalReceived(ESignalVolume signalVolume) {

    }
}
