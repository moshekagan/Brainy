package com.example.first.kaganmoshe.brainy.AppActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.PopupWindow;

import com.example.first.kaganmoshe.brainy.AppActivities.ActionBarActivity.ActionBarAppActivity;
import com.example.first.kaganmoshe.brainy.AppManagement.AppManager;
import com.example.first.kaganmoshe.brainy.Feedback.FeedbackActivity;
import com.example.first.kaganmoshe.brainy.Feedback.FeedbackClass;
import com.example.first.kaganmoshe.brainy.Games.Dialogs.GameHelpDialog;
import com.example.first.kaganmoshe.brainy.Games.Dialogs.QuitGameDialog;
import com.example.first.kaganmoshe.brainy.Games.Dialogs.ResumeGameCountDown;
import com.example.first.kaganmoshe.brainy.Games.Dialogs.FinishGameDialog;
import com.example.first.kaganmoshe.brainy.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by tamirkash on 8/3/15.
 */
public abstract class GameActivity extends ActionBarAppActivity implements ResumeGameCountDown.ResumeGameCommunicator,
        QuitGameDialog.QuitGameCommunicator, FinishGameDialog.FinishGameCommunicator, GameHelpDialog.GameHelpCommunicator {
    // Data Members
    protected FeedbackClass mFeedback;
    protected QuitGameDialog mQuitGameDialog = new QuitGameDialog();
    protected FinishGameDialog mFinishGameDialog = new FinishGameDialog();
    protected ResumeGameCountDown mResumeGameCountDown = new ResumeGameCountDown();
    protected GameHelpDialog mGameHelpDialog = new GameHelpDialog();
    protected Class mBackPressedActivityTarget = null;
    protected boolean mWasGameStarted = false;
    protected LinkedHashMap<String, String> mExtraStats = new LinkedHashMap<>();

    // Need To Implements
    protected abstract void startFeedbackSession();

    protected void onMenuPopupShow() {
        Log.d("FEEDBACK_USER_PAUSE", "PAUSED");
    }

    protected abstract String setContentForHelpDialog();

    protected int calculateScore(){
        return 100;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        onPause();
        mQuitGameDialog.show(mFragmentManager, "SHOW QUIT");
    }

    // Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mQuitGameDialog.setListener(this);
        mFinishGameDialog.setListener(this);
        mResumeGameCountDown.setListener(this);
        mGameHelpDialog.setListener(this);

        mGameHelpDialog.setHelpContentText(this.setContentForHelpDialog());
        mFinishGameDialog.setTitle(this.setFinishDialogTitle());

        initGameHelpDialog();

        mBackPressedActivityTarget = MainActivity.class;
    }

    protected abstract String setFinishDialogTitle();

    private void initGameHelpDialog() {
        String gameTitle = MainActivity.EGameItem.getGameNameByClass(this.getClass());

        mGameHelpDialog.setHelpTitleText(gameTitle);
    }

    @Override
    protected void onStart(){
        super.onStart();

        if(!mWasGameStarted && !AppManager.getInstance().getGamesManager().isDailyPracticeModeOn()){
            mWasGameStarted = true;
            mGameHelpDialog.show(mFragmentManager, "helpDialog");
        }
    }

    @Override
    protected boolean playMusicInActivity(){
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("RESUME", "onResume");

        if (!mQuitGameDialog.isShowing() && !mSettingsFragment.isShowing() && !mHomeButtonPopup.isShowing()
                && !mResumeGameCountDown.isShowing() && !mGameHelpDialog.isShowing()) {
            mResumeGameCountDown.show(mFragmentManager, "SHOW COUNTDOWN");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("GRAPH_LIFE", "STOPPING_GRAPH_ON_PAUSE");
        mFeedback.incNumOfUserPauses();
        stopReceivingEEGData();
    }

    protected void stopReceivingEEGData() {
        Log.d("GRAPH_LIFE", "STOPPING_GRAPH");

        if (mFeedback != null) {
            mFeedback.stopTimerAndRecievingData();
        }
    }

    protected void resumeReceivingEEGData() {
        Log.d("GRAPH_LIFE", "RESUME_GRAPH");


        if (mFeedback != null) {
            mFeedback.resumeTimerAndReceivingData();
        }
    }

    protected void showFinishDialog() {
        onPause();
        AppManager.getInstance().getGamesManager().showFinishDialog(mFragmentManager, this);
    }

    private Intent makeIntentForFinishedGame(Class targetActivity){
        Intent intent = new Intent(getApplicationContext(), targetActivity);

        intent.putExtra(FeedbackActivity.DISTRACTION_STAT, Integer.toString(mFeedback.getDistractionScore()));
        intent.putExtra(FeedbackActivity.SCORE_STAT, Integer.toString(mFeedback.getGameScore()));
        intent.putParcelableArrayListExtra(FeedbackActivity.CURR_GAME_CONCENTRATION_POINTS, mFeedback.getConcentrationPoints());
        intent.putExtra(FeedbackActivity.PLAY_AGAIN_ACTIVITY_TARGET, this.getClass().getCanonicalName());
        addTotalTimeSessionFeedbackStat(intent);

        return intent;
    }


    protected void loadExtraStatsToIntent(Intent intent){
        ArrayList<String> extraStatKeys = new ArrayList<>();

        for (String extraStat : mExtraStats.keySet()) {
            intent.putExtra(extraStat, mExtraStats.get(extraStat));
            extraStatKeys.add(extraStat);
        }

        intent.putStringArrayListExtra(FeedbackActivity.EXTRA_STATS, extraStatKeys);
    }

    protected void addNewStatForFeedback(String name, String value){
        mExtraStats.put(name, value);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean value = super.onOptionsItemSelected(item);
        stopReceivingEEGData();
        onMenuPopupShow();

        mHomeButtonPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (!mQuitGameDialog.isShowing() && !mSettingsFragment.isShowing() && !mHomeButtonPopup.isShowing()) {
                    onResume();
                }
            }
        });

        return value;
    }

    @Override
    public void onQuitGameConfirmed() {
        AppManager.getInstance().getGamesManager().continueAfterQuitConfirmed(this);
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
        //TODO - not good !!
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
//        mQuitGameDialog.show(mFragmentManager, "QUIT CONFIRMATION");
    }

    protected void onBackClickedQuitGame() {
        onResume();
    }

    protected void onBackClickedFinishGame() {
        Utils.startNewActivity(this, MainActivity.class);
    }

    @Override
    public void onDialogShow(Class thisClass) {
        mHomeButtonPopup.dismiss();
        mFeedback.stopTimerAndRecievingData();

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
        mFeedback.insertRecordToHistoryDB(getApplicationContext(), this.toString());
        Log.d("DAILY PRACTICE", "INSERT RECORD " + this.toString());
    }

    @Override
    protected void onQuitClicked() {
        AppManager.getInstance().getGamesManager().showQuitDialog(mFragmentManager, mQuitGameDialog);
    }

    @Override
    public void onFinishGameContinueClicked() {
        AppManager.getInstance().getGamesManager().continueToActivityAfterGameFinished(this);
    }

    @Override
    protected void onPopupMenuOptionSelected() {
        mQuitGameDialog.show(mFragmentManager, "QuitGameDialog");
    }

    @Override
    public void onSettingsShow() {
    }

    protected abstract void addTotalTimeSessionFeedbackStat(Intent intent);

    public Intent prepareIntentForFeedback() {
        Intent intent = makeIntentForFinishedGame(FeedbackActivity.class);

        loadExtraStatsToIntent(intent);

        return intent;
    }

    public FeedbackClass getFeedback() {
        return mFeedback;
    }

    public FinishGameDialog getFinishGameDialog(){
        return mFinishGameDialog;
    }

    @Override
    public void onStartClicked() {
        mGameHelpDialog.dismiss();
        onResume();
    }
}
