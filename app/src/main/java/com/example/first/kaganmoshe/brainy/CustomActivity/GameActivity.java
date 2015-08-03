package com.example.first.kaganmoshe.brainy.CustomActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.first.kaganmoshe.brainy.Feedback.FeedbackActivity;
import com.example.first.kaganmoshe.brainy.Feedback.FeedbackClass;
import com.example.first.kaganmoshe.brainy.GraphFragment;
import com.example.first.kaganmoshe.brainy.GuessTheNumber.GTNFeedback;
import com.example.first.kaganmoshe.brainy.GuessTheNumber.WinnerDialogFragment;
import com.example.first.kaganmoshe.brainy.MenuActivity;
import com.example.first.kaganmoshe.brainy.R;

import EEG.IHeadSetData;

/**
 * Created by tamirkash on 8/3/15.
 */
public abstract class GameActivity extends CustomActivity implements IHeadSetData, WinnerDialogFragment.gameCommunicator {
    protected android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
    protected FeedbackClass feedback;
    protected GraphFragment graphFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setOnBackPressedActivity(MenuActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (graphFragment == null) {
            graphFragment = (GraphFragment) fm.findFragmentById(R.id.fragment);
        }

        graphFragment.resumeRecievingData();
        feedback.resumeRecievingData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        graphFragment.stopRecievingData();
        feedback.stopTimerAndRecievingData();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        graphFragment.stopRecievingData();
//        feedback.stopTimerAndRecievingData();
//    }

    protected void showFinishDialog() {
        feedback.stopTimerAndRecievingData();
        graphFragment.stopRecievingData();
        WinnerDialogFragment winnerDialogFragment = new WinnerDialogFragment();
        winnerDialogFragment.setGameScreen(this);
        winnerDialogFragment.show(fm, "WinnerDialogFragment");
    }

    @Override
    public void continueNextScreen() {
        Intent intent = new Intent(this, FeedbackActivity.class);

        intent.putParcelableArrayListExtra(FeedbackActivity.CURR_GAME_CONCENTRATION_POINTS, feedback.getConcentrationPoints());
        intent.putExtra(FeedbackActivity.CURR_GAME_TIME_SECONDS, feedback.getSessionTimeInSeconds());
        intent.putExtra(FeedbackActivity.CURR_GAME_TIME_MINUTES, feedback.getSessionTimeInMinutes());
        startActivity(intent);
    }

    protected abstract void startFeedbackSession();
}
