package com.example.first.kaganmoshe.brainy.Games.MindShooter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import Utils.AppTimer;
import com.example.first.kaganmoshe.brainy.AppActivities.GameActivity;
import com.example.first.kaganmoshe.brainy.AppActivities.MainActivity;
import com.example.first.kaganmoshe.brainy.Feedback.FeedbackActivity;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Utils;

import java.util.Random;

import EEG.EConnectionState;
import EEG.ESignalVolume;
import Utils.Logs;

public class MindShooterGameActivity extends GameActivity implements IMindShooter, AppTimer.IAppTimerListener {

    // Data Members
    public static final String MIND_SHOOTER_GAME_ACTIVITY = "MindShooterGameActivity";
    private static final long TIME_FOR_GAME = 60000l;

    private MindShooterLogic m_MindShooterLogic;
    private Point m_IntentPointSize = new Point();
    private Point m_IntentPointLocation = new Point();
    private Point m_BalloonPointSize = new Point();
    private Point m_BalloonPointLocation = new Point();
    private Point m_ScreenSize;
    private ImageView m_IntentImageView;
    private ImageView m_BalloonImageView;
    private TextView m_ScoreTextView;
    private Button m_ShootBtn;
    private boolean timerOn = true;
    private SoundPool m_SoundEffect;
    private int m_BalloonPoppingAffectId;
    private int m_SingleShotAffectId;
    private boolean m_IsPlaying = false;

    // Timer Members
    private AppTimer m_Timer = new AppTimer(TIME_FOR_GAME, AppTimer.ETimeStringFormat.MINUTES_AND_SECONDS);
    private TextView m_TimeTextView;
//    private Handler m_CustomHandler = new Handler();
//    private long m_StartTime = 0;
//    private long m_TimeInMilliseconds;
//    private long m_TimerValueInMilliseconds = TIME_FOR_GAME;
//    private long m_UpdatedTime;
//    private long m_TimeSwapBuff;
//    private Runnable m_UpdateTimerThread = new Runnable() {
//        public void run() {
//            updatedTimeForThread(this);
//        }
//    };

    // Methods
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maind_shooter_game);

        m_ScreenSize = Utils.getActivityScreenSize(this);
        m_BalloonImageView = (ImageView) findViewById(R.id.balloon);
        m_IntentImageView = (ImageView) findViewById(R.id.intent);
        m_ShootBtn = (Button) findViewById(R.id.shootBtn);
        m_ScoreTextView = (TextView) findViewById(R.id.MindShooterScoreTextView);
        m_TimeTextView = (TextView) findViewById(R.id.MindShooterTimeTextView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        }else{
            createOldSoundPool();
        }

        m_BalloonPoppingAffectId = m_SoundEffect.load(this, R.raw.balloon_popping_affect, 1);
        m_SingleShotAffectId = m_SoundEffect.load(this, R.raw.single_shot_affect, 1);

        try {
            m_MindShooterLogic = new MindShooterLogic(m_ScreenSize.x, m_ScreenSize.y, this);
        } catch (Exception ex) {}

        m_IntentImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                calcViewSizeAbdLocation(m_IntentImageView, m_IntentPointLocation, m_IntentPointSize);
                m_IntentImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        m_BalloonImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                calcViewSizeAbdLocation(m_BalloonImageView, m_BalloonPointLocation, m_BalloonPointSize);
                m_BalloonImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        m_ShootBtn.bringToFront();
        m_IntentImageView.bringToFront();
        m_BalloonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_MindShooterLogic.test();
            }
        }); // TODO - Remove it
        m_ShootBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoot();

            }
        });

        setScore(0);
        startGame();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createOldSoundPool() {
        m_SoundEffect = new SoundPool.Builder()
                .setMaxStreams(10)
                .build();
    }

    private void createNewSoundPool() {
        m_SoundEffect = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
    }

    private void shoot() {
        if (m_IsPlaying) {
            m_MindShooterLogic.shoot();
//        m_SingleShotAffect_.start();
            m_SoundEffect.play(m_SingleShotAffectId, 1, 1, 1, 0, 1);
        }
    }

    private void startGame() {
        m_MindShooterLogic.startGame();
//        startFeedbackSession();
        startTimerGame(TIME_FOR_GAME);
    }

//    private void updatedTimeForThread(Runnable runnable) {
//        m_TimeInMilliseconds = SystemClock.uptimeMillis() - m_StartTime;
//        m_TimeInMilliseconds = m_TimerValueInMilliseconds - m_TimeInMilliseconds;
//
//        if (m_TimeInMilliseconds > 0L) {
//            m_UpdatedTime = m_TimeSwapBuff + m_TimeInMilliseconds;
//
//
//            int secs = (int) (m_UpdatedTime / 1000);
//            int mins = secs / 60;
//            secs = secs % 60;
////                int milliseconds = (int) (m_UpdatedTime % 1000);
//            m_TimeTextView.setText("" + mins + ":"
//                    + String.format("%02d", secs)/*+ String.format("%03d", milliseconds)*/);
//            m_CustomHandler.postDelayed(runnable, 0);
//        } else {
//            finishTimerGame();
//        }
//    }

    private void startTimerGame(long value) {
        m_Timer.registerListener(this);
        m_TimeTextView.setText(m_Timer.toString());
        m_IsPlaying = true;
        startFeedbackSession();

//        m_TimerValueInMilliseconds = value;
//        m_StartTime = SystemClock.uptimeMillis();
//        mFeedback.startTimer();
//        m_CustomHandler.postDelayed(m_UpdateTimerThread, 0);

    }

    private void stopTimer(){
        m_Timer.stopTimer();
        m_IsPlaying = false;
    }

    private void resumeTimer() {
        m_Timer.resumeTimer();
        m_IsPlaying = true;
    }

    private void finishTimerGame() {
//        m_TimeSwapBuff += m_TimeInMilliseconds;
//        m_CustomHandler.removeCallbacks(m_UpdateTimerThread);
//        mFeedback.stopTimerAndRecievingData();

        ((MindShooterFeedback) mFeedback).calculateFinalScore(m_MindShooterLogic.getScore());
        showFinishDialog();
    }

    private void calcViewSizeAbdLocation(ImageView image, Point pointLocation, Point pointSize) {
        pointSize.set(image.getHeight(), image.getWidth());
        pointLocation.set(image.getLeft(), image.getTop());

        Logs.error(MIND_SHOOTER_GAME_ACTIVITY, "Image location: " + pointLocation.toString());
        Logs.error(MIND_SHOOTER_GAME_ACTIVITY, "Image size: " + pointSize.toString());
    }

    private void moveImageViewTo(ImageView image,Point newDestination, int duration){
//        ObjectAnimator animation = ObjectAnimator.ofFloat(m_IntentImageView,
//                "y", newDestination);

//        ObjectAnimator animation = ObjectAnimator.ofFloat(m_IntentImageView,"x", "y",new Path());
        ObjectAnimator animX = ObjectAnimator.ofFloat(image, "x", newDestination.x);
        ObjectAnimator animY = ObjectAnimator.ofFloat(image, "y", newDestination.y);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animX, animY);
        animSetXY.setDuration(duration);
        animSetXY.start();
//        ObjectAnimator animation = ObjectAnimator.ofFloat(m_IntentImageView,"x", "y",new Path());
//        animation.setDuration(1000);
//        animation.start();
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

    @Override
    protected void addTotalTimeSessionFeedbackStat(Intent intent) {
        intent.putExtra(FeedbackActivity.TOTAL_TIME, "01:00");
    }

    @Override
    protected int calculateScore() {
        return 100;
    }

    // GameActivity Override Methods
    @Override
    protected void startFeedbackSession() {
        mFeedback = new MindShooterFeedback();
//        mFeedback.startTimer();
    }

//    @Override
//    protected void onMenuPopupDismiss() {
//
//    }

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

    // implements IMindShooter
    @Override
    public void setIntentLocation(Point intentLocation) {
        moveImageViewTo(m_IntentImageView, intentLocation, 1000);
    }

    @Override
    public void setBalloonLocation(Point balloonLocation, boolean withAnimation) {
        // Choose random color for the balloon
        int[] balloonIdArray = new int[4];
        balloonIdArray[0] = R.drawable.balloon_blue;
        balloonIdArray[1] = R.drawable.balloon_red;
        balloonIdArray[2] = R.drawable.balloon_green;
        balloonIdArray[3] = R.drawable.balloon_gold;

        Random random = new Random();
        int index = random.nextInt(4);

        m_BalloonImageView.setImageResource(balloonIdArray[index]);
        int animationDuration = withAnimation ? 1000 : 0;
        moveImageViewTo(m_BalloonImageView, balloonLocation, animationDuration);
    }

    @Override
    public void animateIntentForLocation(Point intentLocation, int duration) {
        if (m_IsPlaying)
            moveImageViewTo(m_IntentImageView, intentLocation, duration);
    }

    @Override
    public void theBalloonExploded(Point currentBalloonLocation, int i) {
//        m_BalloonPoppingAffect_.start();
        m_SoundEffect.play(m_BalloonPoppingAffectId, 1, 1, 1, 0, 1);

        setBalloonLocation(currentBalloonLocation, false);
        setScore(i);
    }

    @Override
    public void setScore(int currentScore) {
        m_ScoreTextView.setText(Integer.toString(currentScore));
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDialogShow(Class thisClass) {
        stopTimer();
        super.onDialogShow(thisClass);
    }

    @Override
    protected void onMenuPopupShow() {
        super.onMenuPopupShow();
        stopTimer();
    }

    @Override
    public void onGameResumed() {
        super.onGameResumed();
        resumeTimer();
    }

    @Override
    public void onTimeTick(String timeString) { m_TimeTextView.setText(m_Timer.toString()); }

    @Override
    public void onTimeFinish(String timeString) {
        finishTimerGame();
    }

    @Override
    public String toString(){
        return MainActivity.MIND_SHOOTER_STR;
    }
}
