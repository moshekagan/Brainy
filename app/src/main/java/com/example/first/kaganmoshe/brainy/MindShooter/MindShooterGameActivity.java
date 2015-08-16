package com.example.first.kaganmoshe.brainy.MindShooter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Property;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;

import com.example.first.kaganmoshe.brainy.CustomActivity.GameActivity;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Utils;

import java.util.Random;

import EEG.EConnectionState;
import EEG.ESignalVolume;
import Utils.Logs;

public class MindShooterGameActivity extends GameActivity implements IMindShooter {

    // Data Members
    public static final String MIND_SHOOTER_GAME_ACTIVITY = "MindShooterGameActivity";

    private MindShooterLogic m_MindShooterLogic;
    private Point m_IntentPointSize = new Point();
    private Point m_IntentPointLocation = new Point();
    private Point m_BalloonPointSize = new Point();
    private Point m_BalloonPointLocation = new Point();
    private Point m_ScreenSize;
    private ImageView m_IntentImageView;
    private ImageView m_BalloonImageView;
    private Button m_ShootBtn;

    // Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maind_shooter_game);

        m_ScreenSize = Utils.getActivityScreenSize(this);
        m_BalloonImageView = (ImageView) findViewById(R.id.balloon);
        m_IntentImageView = (ImageView) findViewById(R.id.intent);
        m_ShootBtn = (Button) findViewById(R.id.shootBtn);

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

        m_IntentImageView.bringToFront();
        m_BalloonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { m_MindShooterLogic.test(); }
        }); // TODO - Remove it
        m_ShootBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               m_MindShooterLogic.shoot();
            }});
        m_MindShooterLogic.startGame();
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

    // GameActivity Override Methods
    @Override
    protected void startFeedbackSession() {

    }

    @Override
    protected void onMenuPopupShow() {

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
        moveImageViewTo(m_IntentImageView, intentLocation, duration);
    }
}
