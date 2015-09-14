package com.example.first.kaganmoshe.brainy.AppManagement;

import android.content.Intent;
import android.support.v4.app.FragmentManager;

import com.example.first.kaganmoshe.brainy.AppActivities.MainActivity;
import com.example.first.kaganmoshe.brainy.DailyPractice.DailyPractice;
import com.example.first.kaganmoshe.brainy.AppActivities.GameActivity;
import com.example.first.kaganmoshe.brainy.Games.Dialogs.QuitGameDialog;
import com.example.first.kaganmoshe.brainy.Utils;

/**
 * Created by tamirkash on 8/27/15.
 */
public class GamesManager {
    private DailyPractice mDailyPractice = new DailyPractice();

    public void showFinishDialog(FragmentManager fm, GameActivity gameActivity) {
        if (mDailyPractice.isDailyPracticeOn()) {
            mDailyPractice.showFinishDialog(fm, gameActivity);
        } else {
            gameActivity.getFinishGameDialog().show(fm, "FinishGameDialog");
        }
    }

    public void continueToActivityAfterGameFinished(GameActivity gamesActivity) {
//        if(mNoHeadsetMode){
//
//        }
        if (mDailyPractice.isDailyPracticeOn()) {
            mDailyPractice.continueAfterGameFinished(gamesActivity);
        } else {
            Intent intent = gamesActivity.prepareIntentForFeedback();
            Utils.startNewActivity(gamesActivity, intent);
        }
    }

    public void startDailyPractice(MainActivity activity){
        mDailyPractice.startPractice(activity);
    }

    public void continueAfterQuitConfirmed(GameActivity gameActivity) {
        if(mDailyPractice.isDailyPracticeOn()){
            mDailyPractice.reset();
        }

        Utils.startNewActivity(gameActivity, MainActivity.class);
    }

    public void showQuitDialog(FragmentManager fm, QuitGameDialog quitGameDialog) {
        quitGameDialog.show(fm, "QUIT CONFIRMATION");
    }

    public boolean isDailyPracticeModeOn(){
        return mDailyPractice.isDailyPracticeOn();
    }
}
