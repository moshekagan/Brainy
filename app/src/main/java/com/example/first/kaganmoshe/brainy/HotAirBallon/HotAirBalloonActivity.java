package com.example.first.kaganmoshe.brainy.HotAirBallon;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.first.kaganmoshe.brainy.AppManager;
import com.example.first.kaganmoshe.brainy.R;

import java.util.LinkedList;
import java.util.List;

import EEG.EConnectionState;
import EEG.EHeadSetType;
import EEG.ESignalVolume;
import EEG.EegHeadSet;
import EEG.IHeadSetData;
import Utils.Logs;

public class HotAirBalloonActivity extends Activity implements IHeadSetData {

    // Data Members
    private final String HOT_AIR_BALLOON_ACTIVITY = "Hot Ait Balloon Activity";
    private final int distanceFromTopActivity = 15;
    private final int startLocationOnActivity = 1200;
    private float balloonRange = startLocationOnActivity - distanceFromTopActivity;
    private Button btn;
    private ImageView imageView;
    private List<Integer> attValues = new LinkedList<>();
    private int i = 1;
    private boolean listenToHeadSet = false;
    private boolean displayMessage = true;
    private MediaPlayer hotAirBalloonSoundAffect;
    private int oldAtt = 0;

    // Data Members For Timer
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private long timerValueInMilliseconds = 60000l; //
    private long startTime = 0L;
    private TextView timerValue;
    private Handler customHandler = new Handler();

    // Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_air_balloon);

        imageView = (ImageView) findViewById(R.id.balloonImageView);
        timerValue = (TextView) findViewById(R.id.timerValue);
        hotAirBalloonSoundAffect = MediaPlayer.create(this, R.raw.hot_air_balloon_sound_affect);

        setAttentionValues();

        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!listenToHeadSet){

                    if(AppManager.getInstance().getAppSettings().getHeadSetType() == EHeadSetType.Moker && displayMessage){
                        listenToHeadSet = false;
                        displayMessage = false;
                        Context context = getApplicationContext();
                        CharSequence text = "Unsupported with Moker! click on the button for demo";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        startTimerGame(60000L);
                    } else if (AppManager.getInstance().getAppSettings().getHeadSetType() == EHeadSetType.MindWave){
                        listenToHeadSet = true;
                        startTimerGame(90000L);
                    }
                }
                int attVal = attValues.get(i);
                float attPresent = getAttentionAsFraction(attVal);
                i++;
                if (i == attValues.size()){
                    i = 0;
                }

                float destination = getDestination(attPresent);
                if (oldAtt < attValues.get(i))
                    hotAirBalloonSoundAffect.start();
//                else hotAirBalloonSoundAffect.stop();
                oldAtt = attValues.get(i);
//                Log.e(HOT_AIR_BALLOON_ACTIVITY, "# ATT = " + attVal);
//                Log.e(HOT_AIR_BALLOON_ACTIVITY, "++++++ destination = " + destination);

                raisedTheAirBalloon(destination);
            }
        });

        // Get HeadSet - ic_mind_wave_mobile and register
        try{
            EegHeadSet headSet = AppManager.getInstance().getHeadSet();
            headSet.registerListener(this);
        } catch (Exception e) {
            // TODO - Not need to go hear never!!!!
        }
    }

    private void startTimerGame(long value) {
        timerValueInMilliseconds = value;
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    private float getAttentionAsFraction(int attVal) {
        return (attVal * 0.01f);
    }

    private void raisedTheAirBalloon(float newDestination){
        ObjectAnimator animation = ObjectAnimator.ofFloat(imageView,
                "y", newDestination);
        animation.setDuration(1000);
        animation.start();
    }

    private float getDestination(float attPrecent) {
        return (balloonRange * (1 - attPrecent)) + distanceFromTopActivity;
    }

    private void setAttentionValues() {
        attValues.add(0);
        attValues.add(0);
        attValues.add(25);
        attValues.add(50);
        attValues.add(75);
        attValues.add(100);
        attValues.add(0);
        attValues.add(70);
        attValues.add(60);
        attValues.add(25);
        attValues.add(90);
        attValues.add(80);
        attValues.add(100);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onAttentionReceived(int attValue) {
        if (listenToHeadSet) {
            // Raised the balloon
            Logs.warn(HOT_AIR_BALLOON_ACTIVITY, "Got Attention: " + attValue);
            float attPresent = getAttentionAsFraction(attValue);
            float destination = getDestination(attPresent);
            Logs.warn(HOT_AIR_BALLOON_ACTIVITY, "New Destination: " + destination);
            raisedTheAirBalloon(destination);
            if (oldAtt < attValue)
                hotAirBalloonSoundAffect.start();
//            else hotAirBalloonSoundAffect.stop();
            oldAtt = attValue;
        }
    }
    @Override
    public void onMeditationReceived(int medValue) {
        // Do Nothing
    }

    @Override
    public void onHeadSetChangedState(String headSetName, EConnectionState connectionState) {
        // Do Nothing
    }

    @Override
    public void onPoorSignalReceived(ESignalVolume signalVolume) {
        // Do Nothing
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            timeInMilliseconds = timerValueInMilliseconds - timeInMilliseconds;

            if (timeInMilliseconds > 0L) {
                updatedTime = timeSwapBuff + timeInMilliseconds;


                int secs = (int) (updatedTime / 1000);
                int mins = secs / 60;
                secs = secs % 60;
//                int milliseconds = (int) (updatedTime % 1000);
                timerValue.setText("" + mins + ":"
                        + String.format("%02d", secs)/*+ String.format("%03d", milliseconds)*/);
                customHandler.postDelayed(this, 0);
            } else {
                finishTimerGame();
            }
        }
    };

    private void finishTimerGame() {
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
    }

}
