package com.example.first.kaganmoshe.brainy.CustomActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.PopupWindow;

import com.example.first.kaganmoshe.brainy.Feedback.FeedbackActivity;
import com.example.first.kaganmoshe.brainy.Feedback.FeedbackClass;
import com.example.first.kaganmoshe.brainy.GraphFragment;
import com.example.first.kaganmoshe.brainy.GuessTheNumber.FinishGameDialog;
import com.example.first.kaganmoshe.brainy.MenuActivity;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Utils;

import EEG.IHeadSetData;

/**
 * Created by tamirkash on 8/3/15.
 */
public abstract class GameActivity extends CustomActivity implements IHeadSetData, GameDialog.GameDialogCommunicator {
    protected android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
    protected FeedbackClass feedback;
    protected GraphFragment graphFragment;
    protected QuitGameDialog quitGameDialog = new QuitGameDialog();
    protected FinishGameDialog finishGameDialog = new FinishGameDialog();

    private Class targetActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        quitGameDialog.setGameScreen(this);
        finishGameDialog.setGameScreen(this);
        this.setOnBackPressedActivity(MenuActivity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (graphFragment == null) {
            graphFragment = (GraphFragment) fm.findFragmentById(R.id.fragment);
        } else {
            Log.d("GRAPH_LIFE", "RESUME_GRAPH_ON_RESUME");
            resumeFeedbackAndGraph();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("GRAPH_LIFE", "STOPPING_GRAPH_ON_PAUSE");
        stopFeedbackAndGraph();
    }

    protected void stopFeedbackAndGraph() {
        Log.d("GRAPH_LIFE", "STOPPING_GRAPH");
        graphFragment.stopRecievingData();
        feedback.stopTimerAndRecievingData();
    }

    protected void resumeFeedbackAndGraph() {
        Log.d("GRAPH_LIFE", "RESUME_GRAPH");
        graphFragment.resumeRecievingData();
        feedback.resumeRecievingData();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        graphFragment.stopRecievingData();
//        feedback.stopTimerAndRecievingData();
//    }

    protected void showFinishDialog() {
//        onPause();
//        FinishGameDialog finishGameDialog = new FinishGameDialog();
//        finishGameDialog.setGameScreen(this);
        finishGameDialog.show(fm, "FinishGameDialog");
    }

    @Override
    public void onFinishDialogConfirmed() {
        Intent intent = new Intent(this, FeedbackActivity.class);

        intent.putParcelableArrayListExtra(FeedbackActivity.CURR_GAME_CONCENTRATION_POINTS, feedback.getConcentrationPoints());
        intent.putExtra(FeedbackActivity.CURR_GAME_TIME_SECONDS, feedback.getSessionTimeInSeconds());
        intent.putExtra(FeedbackActivity.CURR_GAME_TIME_MINUTES, feedback.getSessionTimeInMinutes());
        Utils.startNewActivity(this, intent);
    }

    protected abstract void startFeedbackSession();

//    @Override
//    public void onPopupDialogCanceled() {
//        quitGameDialog.dismiss();
//        resumeFeedbackAndGraph();
//    }

    @Override
    public void onPopupDialogLeave() {
        Utils.startNewActivity(this, targetActivity);
    }

    @Override
    public void onBackPressed() {
        if (quitGameDialog.isVisible()) {

        } else {
            quitGameDialog.show(fm, "QuitGameDialog");
        }
    }

    @Override
    public void onPopupDialogCanceled() {
        resumeFeedbackAndGraph();
    }

    @Override
    public void onDialogShow() {
        Log.d("GRAPH_LIFE", "STOPPING_GRAPH_ON_SHOW");
        stopFeedbackAndGraph();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean value = super.onOptionsItemSelected(item);
        stopFeedbackAndGraph();
        onMenuPopupShow();

        homeButtonPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                resumeFeedbackAndGraph();
                onMenuPopupDismiss();
            }
        });

        return value;
    }

    protected abstract void onMenuPopupShow();

    protected abstract void onMenuPopupDismiss();

    @Override
    protected void onPopupMenuOptionSelected(Class targetActivity){
        this.targetActivity = targetActivity;
        quitGameDialog.show(fm, "QuitGameDialog");
    }
}
