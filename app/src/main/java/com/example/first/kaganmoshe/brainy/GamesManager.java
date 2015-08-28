package com.example.first.kaganmoshe.brainy;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;

import com.example.first.kaganmoshe.brainy.CustomActivity.ActionBarAppActivity;
import com.example.first.kaganmoshe.brainy.CustomActivity.DailyPractice;
import com.example.first.kaganmoshe.brainy.CustomActivity.GameActivity;
import com.example.first.kaganmoshe.brainy.CustomActivity.QuitGameDialog;
import com.example.first.kaganmoshe.brainy.GuessTheNumber.FinishGameDialog;

/**
 * Created by tamirkash on 8/27/15.
 */
public class GamesManager {
    private DailyPractice dailyPractice = new DailyPractice();

    public void showFinishDialog(FragmentManager fm, FinishGameDialog finishGameDialog) {
        if (dailyPractice.isDailyPracticeOn()) {
            dailyPractice.showFinishDialog(fm, finishGameDialog);
        } else {
            finishGameDialog.show(fm, "FinishGameDialog");
        }
    }

    public void showQuitDialog(GameActivity gamesActivity, FragmentManager fm,
                               QuitGameDialog quitGameDialog) {

    }

//    public void showResumeGameDialog(GameActivity gamesActivity, FragmentManager fm,
//                                      ResumeGameCountDown resumeGameCountDown) {
//
//    }

    public void continueToActivityAfterGameFinished(GameActivity gamesActivity) {
        if (dailyPractice.isDailyPracticeOn()) {
            dailyPractice.continueAfterGameFinished(gamesActivity);
        } else {
            Intent intent = gamesActivity.prepareIntentForFeedback();
            Utils.startNewActivity(gamesActivity, intent);
        }
    }

    public void startDailyPractice(GamesActivity activity){
        dailyPractice.startPractice(activity);
    }

    public void continueAfterQuitConfirmed(GameActivity gameActivity) {
        if(dailyPractice.isDailyPracticeOn()){
            dailyPractice.reset();
        }

        Utils.startNewActivity(gameActivity, GamesActivity.class);
    }

    public void showQuitDialog(FragmentManager fm, QuitGameDialog quitGameDialog) {
        quitGameDialog.show(fm, "QUIT CONFIRMATION");
    }

//    private void initConcentrationPoints() {
//        parcelableConcentrationPointsList = getIntent().getParcelableArrayListExtra(CURR_GAME_CONCENTRATION_POINTS);
//
////        prepareConcentrationPoints();
//        for (ParcelableDataPoint p : parcelableConcentrationPointsList) {
//            graphConcentrationPoints.appendData(p, false, Integer.MAX_VALUE);
//        }
//    }
}
