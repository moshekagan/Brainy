package com.example.first.kaganmoshe.brainy.CustomActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.PopupWindow;

import com.example.first.kaganmoshe.brainy.Feedback.FeedbackActivity;
import com.example.first.kaganmoshe.brainy.Feedback.FeedbackClass;
import com.example.first.kaganmoshe.brainy.GuessTheNumber.FinishGameDialog;
import com.example.first.kaganmoshe.brainy.MenuActivity;
import com.example.first.kaganmoshe.brainy.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by tamirkash on 8/3/15.
 */
public abstract class GameActivity extends AppActivity implements ResumeGameCountDown.ResumeGameCommunicator,
        QuitGameDialog.QuitGameCommunicator, FinishGameDialog.FinishGameCommunicator {
    // Data Members
    protected FeedbackClass feedback;
//    protected GraphFragment graphFragment;
    protected QuitGameDialog quitGameDialog = new QuitGameDialog();
    protected FinishGameDialog finishGameDialog = new FinishGameDialog();
    protected ResumeGameCountDown resumeGameCountDown = new ResumeGameCountDown();
    protected Class onBackPressedActivityTarget = null;
    private Class targetActivity = null;

    protected GameGraph gameGraph = null;
//    protected GraphView graphView;
//    protected LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
//    protected int lastXGraphAtt = 0;

    // Need To Implements
    protected abstract void startFeedbackSession();

    protected abstract void onMenuPopupShow();

//    protected abstract void onMenuPopupDismiss();

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        onPause();
        quitGameDialog.show(fm, "SHOW QUIT");
    }

    // Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        quitGameDialog.setGameScreen(this);
        finishGameDialog.setGameScreen(this);
        resumeGameCountDown.setGameScreen(this);
        try {
            onBackPressedActivityTarget = Class.forName(getIntent().getStringExtra(Utils.CALLING_CLASS));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

//    @Override
//    public void onGameDialogBackPressed() {
//        Utils.startNewActivity(this, MenuActivity.class);
//    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("RESUME", "onResume");
        targetActivity = null;

//        if (graphFragment == null) {
//            graphFragment = (GraphFragment) fm.findFragmentById(R.id.fragment);
//            graphFragment.stopReceivingData();
//        }

        if (!quitGameDialog.isShowing() && !settingsFragment.isShowing() && !homeButtonPopup.isShowing()
                && !resumeGameCountDown.isShowing()) {
            resumeGameCountDown.show(fm, "SHOW COUNTDOWN");
        }

//        showResumeCountdown();
//                Log.d("GRAPH_LIFE", "RESUME_GRAPH_ON_RESUME");
//                resumeReceivingEEGData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("GRAPH_LIFE", "STOPPING_GRAPH_ON_PAUSE");
        stopReceivingEEGData();
    }

    protected void stopReceivingEEGData() {
        Log.d("GRAPH_LIFE", "STOPPING_GRAPH");

        if (gameGraph != null) {
            gameGraph.stopReceivingData();
        }

        if (feedback != null) {
            feedback.stopTimerAndRecievingData();
        }
    }

    protected void resumeReceivingEEGData() {
        Log.d("GRAPH_LIFE", "RESUME_GRAPH");

        if (gameGraph != null) {
            gameGraph.resumeReceivingData();
        }

        if (feedback != null) {
            feedback.resumeRecievingData();
        }
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        graphFragment.stopReceivingData();
//        feedback.stopTimerAndRecievingData();
//    }

    protected void showFinishDialog() {
        onPause();
//        FinishGameDialog finishGameDialog = new FinishGameDialog();
//        finishGameDialog.setGameScreen(this);
        finishGameDialog.show(fm, "FinishGameDialog");
    }

//    @Override
//    public void onFinishDialogConfirmed() {
//        Intent intent = makeIntentForFeedback();
//
//        intent.putExtra("CALLING_CLASS", this.getClass().getCanonicalName());
//        Utils.startNewActivity(this, intent);
//    }

//    @Override
//    protected void homeMenuButtonClicked() {
//        resumeGameCountDown.show(fm, "Show Resume");
//        super.homeMenuButtonClicked();
//    }

        private Intent makeIntentForFeedback() {
        Intent intent = new Intent(getApplicationContext(), FeedbackActivity.class);

        intent.putParcelableArrayListExtra(FeedbackActivity.CURR_GAME_CONCENTRATION_POINTS, feedback.getConcentrationPoints());
        intent.putExtra(FeedbackActivity.CURR_GAME_TIME_SECONDS, feedback.getSessionTimeInSeconds());
        intent.putExtra(FeedbackActivity.CURR_GAME_TIME_MINUTES, feedback.getSessionTimeInMinutes());
        intent.putExtra(FeedbackActivity.PLAY_AGAIN_ACTIVITY_TARGET, getIntent().getStringExtra(Utils.CALLING_CLASS));

        return intent;
    }

    protected void setNewStatsListAndContinue(LinkedHashMap<String, String> extraStats) {
        ArrayList<String> extraStatKeys = new ArrayList<>();
        Intent intent = makeIntentForFeedback();

        for (String extraStat : extraStats.keySet()) {
            intent.putExtra(extraStat, extraStats.get(extraStat));
            extraStatKeys.add(extraStat);
        }

        intent.putStringArrayListExtra(FeedbackActivity.EXTRA_STATS, extraStatKeys);
        Utils.startNewActivity(this, intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean value = super.onOptionsItemSelected(item);
        stopReceivingEEGData();
        onMenuPopupShow();

        homeButtonPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!quitGameDialog.isShowing() && !settingsFragment.isShowing() && !homeButtonPopup.isShowing()) {
                    onResume();
//            onResume();
//            onMenuPopupDismiss();
                }
//                resumeReceivingEEGData();
//                onMenuPopupDismiss();
//                showResumeCountdown();
            }
        });

        return value;
    }

    @Override
    public void onQuitGameConfirmed() {
        if (targetActivity == null) {
            Utils.startNewActivity(this, onBackPressedActivityTarget);
        } else {
            Utils.startNewActivity(this, targetActivity);
        }
    }

    @Override
    public void onQuitGameCanceled() {
        onResume();
    }

    @Override
    public void onGameResumed() {
        resumeReceivingEEGData();
    }

    @Override
    public void onDialogBackClicked(Class thisClass) {
        Log.d("Back class", thisClass.toString());

        if (thisClass == FinishGameDialog.class) {
            onBackClickedFinishGame();
        } else if (thisClass == QuitGameDialog.class) {
            onBackClickedQuitGame();
        } else if (thisClass == ResumeGameCountDown.class) {
            onBackClickedResumeGame();
        }
    }

    protected void onBackClickedResumeGame() {
//        quitGameDialog.show(fm, "QUIT CONFIRMATION");
    }

    protected void onBackClickedQuitGame() {
        onResume();
    }

    protected void onBackClickedFinishGame() {
        Utils.startNewActivity(this, MenuActivity.class);
    }

    @Override
    public void onDialogShow(Class thisClass) {
        homeButtonPopup.dismiss();

        if (thisClass == FinishGameDialog.class) {
            onFinishGameShow();
        } else if (thisClass == QuitGameDialog.class) {
            onQuitGameShow();
        } else if (thisClass == ResumeGameCountDown.class) {
            onResumeGameShow();
        }
    }

    protected void onResumeGameShow() {

    }

    protected void onQuitGameShow() {

    }

    protected void onFinishGameShow() {

    }

    @Override
    protected void onQuitClicked(){
        quitGameDialog.show(fm, "QUIT CONFIRMATION");
    }

    @Override
    public void onFinishGameContinueClicked() {
        Intent intent = makeIntentForFeedback();

        intent.putExtra("CALLING_CLASS", this.getClass().getCanonicalName());
        Utils.startNewActivity(this, intent);
    }

    @Override
    protected void onPopupMenuOptionSelected() {
        this.targetActivity = MenuActivity.class;
        quitGameDialog.show(fm, "QuitGameDialog");
    }
    @Override
    public void onSettingsShow() {
//        settingsFragment.show(fm, "SETTINGS SHOW");
    }

//    @Override
//    public void onSettingsBackPressed() {
//        onResume();
//        onGameDialogBackPressed();
//    }
//
//    @Override
//    public void onSettingsDonePressed(){
//        onResume();
//    }
}
