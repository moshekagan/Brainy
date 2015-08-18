package com.example.first.kaganmoshe.brainy.HotAirBallon;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.first.kaganmoshe.brainy.AppManager;
import com.example.first.kaganmoshe.brainy.CustomActivity.GameActivity;
import com.example.first.kaganmoshe.brainy.Feedback.FeedbackActivity;
import com.example.first.kaganmoshe.brainy.Feedback.FeedbackClass;
import com.example.first.kaganmoshe.brainy.GenericDialogFragment;
import com.example.first.kaganmoshe.brainy.MenuActivity;
import com.example.first.kaganmoshe.brainy.R;

import java.util.LinkedList;
import java.util.List;

import EEG.EConnectionState;
import EEG.EHeadSetType;
import EEG.ESignalVolume;
import EEG.EegHeadSet;
import EEG.IHeadSetData;
import Utils.Logs;
import com.example.first.kaganmoshe.brainy.Utils;



public class HotAirBalloonGameActivity extends GameActivity implements IHeadSetData, GenericDialogFragment.gameCommunicator {
    // Data Members
    private final String HOT_AIR_BALLOON_ACTIVITY = "Hot Ait Balloon Activity";
    private final int distanceFromTopActivity = 15;
    private final int startLocationOnActivity = 1200;
    private float balloonRange = startLocationOnActivity - distanceFromTopActivity;
    private Button btn;
    private ImageView hotAirBalloonImageView;
    private List<Integer> attValues = new LinkedList<>();
    private int i = 1;
    private boolean listenToHeadSet = false;
    private boolean displayMessage = true;
    private MediaPlayer hotAirBalloonSoundAffect;
    private int oldAtt = 0;
    private FeedbackClass feedback;
    private android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
//    StartGameDialogFragment startGameDialogFragment;

    // Data Members For Timer
    private long timeToPlayInMilSec;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private long updatedTime = 0L;
    private long timerValueInMilliseconds = 60000l; //
    private long startTime = 0L;
    private TextView timerValue;
    private Handler customHandler = new Handler();

    // Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_air_balloon);

//        feedback = new HotAirBalloonFeedback();
        hotAirBalloonImageView = (ImageView) findViewById(R.id.balloonImageView);
        timerValue = (TextView) findViewById(R.id.timerValue);
        hotAirBalloonSoundAffect = MediaPlayer.create(this, R.raw.hot_air_balloon_sound_affect);

        setAttentionValues();

        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        setBalloonDefaultLocation();
        // Get HeadSet - ic_mind_wave_mobile and register
        try{
            EegHeadSet headSet = AppManager.getInstance().getHeadSet();
            headSet.registerListener(this);
        } catch (Exception e) {
            // TODO - Not need to go hear never!!!!
        }

        if (savedInstanceState == null) {
            initialize();
        } else { }

        startGame();
//        showStartGameDialogFragment();
    }

//    @Override
//    protected int calculateScore() {
//        return 100;
//    }

    private void initialize() {
        Intent intent = getIntent();
        timeToPlayInMilSec = intent.getLongExtra(HABConfigActivity.TIME_TO_PLAY, HABConfigActivity.ONE_MIN);

    }

    @Override
    protected void startFeedbackSession() {
        feedback = new HotAirBalloonFeedback();
        feedback.startTimer();
    }

    private void setBalloonDefaultLocation() {
        int attVal = 0;
        float attPresent = getAttentionAsFraction(attVal);
        float destination = getDestination(attPresent);

        ObjectAnimator animation = ObjectAnimator.ofFloat(hotAirBalloonImageView,
                "y", destination);
        animation.setDuration(0);
        animation.start();
    }

    private void startGame() {
        if (!listenToHeadSet){

            if(AppManager.getInstance().getAppSettings().getHeadSetType() == EHeadSetType.Moker && displayMessage){
                listenToHeadSet = false;
                displayMessage = false;
                Context context = getApplicationContext();
                CharSequence text = "Unsupported with Moker! click on the button for demo";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                startTimerGame(timeToPlayInMilSec);
            } else if (AppManager.getInstance().getAppSettings().getHeadSetType() == EHeadSetType.MindWave){
                listenToHeadSet = true;
                startTimerGame(timeToPlayInMilSec);
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

//    private void showStartGameDialogFragment() {
//        startGameDialogFragment = new StartGameDialogFragment();
//        startGameDialogFragment.setGameScreen(this);
//        startGameDialogFragment.show(fm, "StartGameDialogFragment");
//        changeActivityDarkerOrLighter(1f);
//    }

//    private void changeActivityDarkerOrLighter(float val){
//        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
//        lp.dimAmount = val;
//    }

    private void startTimerGame(long value) {
        timerValueInMilliseconds = value;
        startTime = SystemClock.uptimeMillis();
//        feedback.startTimer();
        startFeedbackSession();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    private float getAttentionAsFraction(int attVal) {
        return (attVal * 0.01f);
    }

    private void raisedTheAirBalloon(float newDestination){
        ObjectAnimator animation = ObjectAnimator.ofFloat(hotAirBalloonImageView,
                "y", newDestination);
        animation.setDuration(1000);
        animation.start();
    }

    private float getDestination(float attPresent) {
        return (balloonRange * (1 - attPresent)) + distanceFromTopActivity;
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    protected void onMenuPopupShow() {

    }

//    @Override
//    protected void onMenuPopupDismiss() {
//
//    }

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
            updatedTimeForThread(this);
        }
    };

    private void updatedTimeForThread(Runnable runnable) {
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
            customHandler.postDelayed(runnable, 0);
        } else {
            finishTimerGame();
        }
    }

    private void finishTimerGame() {
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
        feedback.stopTimerAndRecievingData();
        showFinishGameDialog();
    }

    private void showFinishGameDialog() {
        GenericDialogFragment finishDialogFragment = new GenericDialogFragment();
        finishDialogFragment.setGameScreen(this);
        finishDialogFragment.setTitleText("End Of Time, Good Work!");
        finishDialogFragment.setContinueButtonText("Show Results");
        finishDialogFragment.show(fm, "FinishDialogFragment");
    }

    @Override
    public void continueNextScreen() {
        Intent intent = new Intent(this, FeedbackActivity.class);

        intent.putParcelableArrayListExtra(FeedbackActivity.CURR_GAME_CONCENTRATION_POINTS, feedback.getConcentrationPoints());
        intent.putExtra(FeedbackActivity.CURR_GAME_TIME_SECONDS, feedback.getSessionTimeInSeconds());
        intent.putExtra(FeedbackActivity.CURR_GAME_TIME_MINUTES, feedback.getSessionTimeInMinutes());
        startActivity(intent);
    }

    @Override
    public void backKeyPressed() {
        Utils.startNewActivity(this, MenuActivity.class);
    }
}
