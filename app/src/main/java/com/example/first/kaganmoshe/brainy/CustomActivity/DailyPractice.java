package com.example.first.kaganmoshe.brainy.CustomActivity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.example.first.kaganmoshe.brainy.CrazyCube.CrazyCubeActivity;
import com.example.first.kaganmoshe.brainy.Feedback.FBActivity;
import com.example.first.kaganmoshe.brainy.Feedback.FeedbackActivity;
import com.example.first.kaganmoshe.brainy.Feedback.FeedbackClass;
import com.example.first.kaganmoshe.brainy.Feedback.ParcelableDataPoint;
import com.example.first.kaganmoshe.brainy.GamesActivity;
import com.example.first.kaganmoshe.brainy.GuessTheNumber.FinishGameDialog;
import com.example.first.kaganmoshe.brainy.GuessTheNumber.GuessTheNumberGameActivity;
import com.example.first.kaganmoshe.brainy.HotAirBallon.HotAirBalloonGameActivity;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by tamirkash on 8/27/15.
 */
public class DailyPractice {
    private static final String DP_OVER_TITLE = "Your practice is over!";
    private boolean dailyPracticeOn = false;
    private int currGameNum = 1;
    private int currConcentrationPointIndex = 0;
    private final ArrayList<ParcelableDataPoint> parcelableConcentrationPointsList = new ArrayList<>();
    private static final Class[] games = {
            GuessTheNumberGameActivity.class,
            HotAirBalloonGameActivity.class,
            CrazyCubeActivity.class
    };
    private final Random random = new Random();

    private static final int TOTAL_GAMES = 15;

    public void startPractice(GamesActivity activity) {
        dailyPracticeOn = true;
        Utils.startNewActivity(activity, games[random.nextInt(games.length)]);
        //make the activity know that its the first game in the daily practice
        //maybe gameActivity will notify the games manager its starting and then the games manager will start a short dialog
        //explaining things about the daily practice
    }

    public boolean isDailyPracticeOn() {
        return dailyPracticeOn;
    }

    public void continueAfterGameFinished(GameActivity gameActivity) {
        int concentration = FeedbackClass.getConcentrationScore(gameActivity.getFeedback().getConcentrationPoints());

        parcelableConcentrationPointsList.add(new ParcelableDataPoint(currConcentrationPointIndex++, concentration));
        Log.d("DAILY PRACTICE", "CONCENTRATION (" + String.valueOf(currGameNum) + "): " + String.valueOf(concentration));
        currGameNum++;

        if (currGameNum > TOTAL_GAMES) {
            Intent intent = new Intent(gameActivity.getApplicationContext(), FBActivity.class);
            intent.putParcelableArrayListExtra(FeedbackActivity.CURR_GAME_CONCENTRATION_POINTS, parcelableConcentrationPointsList);
            Utils.startNewActivity(gameActivity, intent);
            reset();

        } else {

//            Intent intent = prepareIntentForDailyPracticeFeedback();
            Utils.startNewActivity(gameActivity, games[random.nextInt(games.length)]);
        }
    }

    public void reset() {
        dailyPracticeOn = false;
        currConcentrationPointIndex = 0;
        currGameNum = 1;
        parcelableConcentrationPointsList.clear();
    }

    public void showFinishDialog(FragmentManager fm, FinishGameDialog finishGameDialog) {
        finishGameDialog.setLayoutID(R.layout.finish_game_dp_dialog);

        if (currGameNum < TOTAL_GAMES) {
            finishGameDialog.setDPTitle(String.valueOf(currGameNum) + "/" + String.valueOf(TOTAL_GAMES));
        } else {
            finishGameDialog.setDPTitle(DP_OVER_TITLE);
        }

        finishGameDialog.show(fm, "DP_FINISH_DIALOG");
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
