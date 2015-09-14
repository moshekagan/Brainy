package com.example.first.kaganmoshe.brainy.DailyPractice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.example.first.kaganmoshe.brainy.AppActivities.GameActivity;
import com.example.first.kaganmoshe.brainy.AppManagement.AppManager;
import com.example.first.kaganmoshe.brainy.Games.CrazyCube.CrazyCubeActivity;
import com.example.first.kaganmoshe.brainy.Feedback.DPFeedbackActivity;
import com.example.first.kaganmoshe.brainy.Feedback.FeedbackActivity;
import com.example.first.kaganmoshe.brainy.Feedback.FeedbackClass;
import com.example.first.kaganmoshe.brainy.Feedback.ParcelableDataPoint;
import com.example.first.kaganmoshe.brainy.AppActivities.MainActivity;
import com.example.first.kaganmoshe.brainy.Games.Dialogs.FinishGameDialog;
import com.example.first.kaganmoshe.brainy.Games.GuessTheNumber.GuessTheNumberGameActivity;
import com.example.first.kaganmoshe.brainy.Games.HotAirBallon.HotAirBalloonGameActivity;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by tamirkash on 8/27/15.
 */
public class DailyPractice {
    private static final String DP_OVER_TITLE = "Your practice is over!";
    private boolean mDailyPracticeOn = false;
    private int mCurrGameNum = 1;
    private int mCurrConcentrationPointIndex = 0;
    private final ArrayList<ParcelableDataPoint> mParcelableConcentrationPointsList = new ArrayList<>();
    private static final Class[] mGames = {
            GuessTheNumberGameActivity.class,
            HotAirBalloonGameActivity.class,
            CrazyCubeActivity.class
    };
    private final Random mRandom = new Random();

    private static final int TOTAL_GAMES = 15;

    public void startPractice(MainActivity activity) {
        mDailyPracticeOn = true;
        Utils.startNewActivity(activity, mGames[mRandom.nextInt(mGames.length)]);
    }

    public boolean isDailyPracticeOn() {
        return mDailyPracticeOn;
    }

    public void continueAfterGameFinished(GameActivity gameActivity) {
        int concentration = FeedbackClass.getConcentrationScore(gameActivity.getFeedback().getConcentrationPoints());

        mParcelableConcentrationPointsList.add(new ParcelableDataPoint(mCurrConcentrationPointIndex++, concentration));
        Log.d("DAILY PRACTICE", "CONCENTRATION (" + String.valueOf(mCurrGameNum) + "): " + String.valueOf(concentration));
        mCurrGameNum++;

        if (mCurrGameNum > TOTAL_GAMES) {
            Intent intent = new Intent(gameActivity.getApplicationContext(), DPFeedbackActivity.class);
            intent.putParcelableArrayListExtra(FeedbackActivity.CURR_GAME_CONCENTRATION_POINTS, mParcelableConcentrationPointsList);

            AppManager.getHistoryDBInstance(gameActivity.getApplicationContext()).insertRecord("Daily Practices",
                    FeedbackClass.getConcentrationScore(mParcelableConcentrationPointsList));
            Utils.startNewActivity(gameActivity, intent);
            reset();

        } else {
            Utils.startNewActivity(gameActivity, mGames[mRandom.nextInt(mGames.length)]);
        }
    }

    public void reset() {
        mDailyPracticeOn = false;
        mCurrConcentrationPointIndex = 0;
        mCurrGameNum = 1;
        mParcelableConcentrationPointsList.clear();
    }

    public void showFinishDialog(FragmentManager fm, GameActivity gameActivity) {
        FinishGameDialog finishGameDialog = gameActivity.getFinishGameDialog();

        finishGameDialog.setLayoutID(R.layout.finish_game_dp_dialog);

        initFinishGameDialog(gameActivity);

        if (mCurrGameNum < TOTAL_GAMES) {
            finishGameDialog.setDPTitle(String.valueOf(mCurrGameNum) + "/" + String.valueOf(TOTAL_GAMES));
        } else {
            finishGameDialog.setDPTitle(DP_OVER_TITLE);
        }

        finishGameDialog.show(fm, "DP_FINISH_DIALOG");
    }

    private void initFinishGameDialog(GameActivity gameActivity) {
        int gameScore = gameActivity.getFeedback().getGameScore();
        int generalDistraction = gameActivity.getFeedback().getDistractionScore();
        int concentrationScore = FeedbackClass.getConcentrationScore(gameActivity.getFeedback().getConcentrationPoints());

        gameActivity.getFinishGameDialog().setConcentrationScoreStat(concentrationScore);

        initBestScore(gameScore + generalDistraction + concentrationScore, gameActivity);
    }

    private void initBestScore(int finalScore, GameActivity gameActivity){
        SharedPreferences sharedPref = gameActivity.getApplicationContext().getSharedPreferences("bestScore", Context.MODE_PRIVATE);
        int defaultValue = gameActivity.getResources().getInteger(R.integer.default_high_score);
        long highScore = sharedPref.getInt(gameActivity.getClass().getCanonicalName(), defaultValue);

        Log.d("DP", "defaultValue=" +defaultValue+ " highScore=" + highScore);

        if(highScore < finalScore){
            FeedbackActivity.setBestScore(gameActivity, gameActivity.getClass().getCanonicalName(), finalScore);
        }

        gameActivity.getFinishGameDialog().setScoreStat(finalScore);
        gameActivity.getFinishGameDialog().setBestScoreStat(sharedPref.getInt(gameActivity.getClass().getCanonicalName(), defaultValue));
    }
}
