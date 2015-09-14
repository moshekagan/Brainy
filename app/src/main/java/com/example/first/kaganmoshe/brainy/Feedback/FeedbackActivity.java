package com.example.first.kaganmoshe.brainy.Feedback;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.AppActivities.MainActivity;
import com.example.first.kaganmoshe.brainy.Utils;
import com.example.first.kaganmoshe.brainy.R;

import java.util.ArrayList;

public class FeedbackActivity extends FBActivity {
    public static final String EXTRA_STATS = "EXTRA_STATS";
    public static final String SCORE_STAT = "Score";
    public static final String BEST_SCORE_STAT = "Best score";
    public static final String DISTRACTION_STAT = "DISTRACTION_STAT";
    public static final String PLAY_AGAIN_ACTIVITY_TARGET = "PLAY_AGAIN_ACTIVITY_TARGET";
    public static final String TOTAL_TIME = "Session time";
    private int mFinalScore;

    protected Button mBackButton;
    protected Button mPlayAgainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mFeedbackStatsLayout = (LinearLayout) findViewById(R.id.feedbackStatsLayout);

        initGraph();
        initGameTime();
        initConcentrationPoints();
        initScoreStat();
        initExtraStats();
        initButtons();
    }

    private int getBestScore() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("bestScore", Context.MODE_PRIVATE);
        int defaultValue = getResources().getInteger(R.integer.default_high_score);
        long highScore = sharedPref.getInt(getIntent().getStringExtra(Utils.CALLING_CLASS), defaultValue);

        return (int) highScore;
    }

    private void initScoreStat() {
        int gameScore = Integer.valueOf(getIntent().getStringExtra(SCORE_STAT));
        int generalDistraction = Integer.valueOf(getIntent().getStringExtra(DISTRACTION_STAT));
        int bestScore = getBestScore();
        int concentrationScore = FeedbackClass.getConcentrationScore(mParcelableConcentrationPointsList);

        addConcentrationStat(concentrationScore);

        mFinalScore = gameScore + generalDistraction + concentrationScore;
        LinearLayout scoreStat = prepareStat(SCORE_STAT, String.valueOf(mFinalScore), mStatsTextSize, getApplicationContext());

        if (mFinalScore > bestScore) {
            bestScore = mFinalScore;
            setBestScore(this, getIntent().getStringExtra(Utils.CALLING_CLASS), bestScore);
            ((TextView) scoreStat.getChildAt(1)).setTextColor(getResources().getColor(R.color.feedback_best_score_text));
            ((TextView) scoreStat.getChildAt(1)).append(" (New record!)");
        }

        LinearLayout bestScoreStat = prepareStat(BEST_SCORE_STAT, String.valueOf(bestScore), mStatsTextSize, getApplicationContext());
        ((TextView) bestScoreStat.getChildAt(1)).setTextColor(getResources().getColor(R.color.feedback_best_score_text));

        addStat(scoreStat);
        addStat(bestScoreStat);
    }

    private void initExtraStats() {
        Intent intent = getIntent();
        ArrayList<String> stats = intent.getStringArrayListExtra(EXTRA_STATS);

        if (stats != null) {
            for (String statName : stats) {
                String statValue = intent.getStringExtra(statName);
                addStat(prepareStat(statName, statValue, mStatsTextSize, getApplicationContext()));
            }
        }
    }

    private void initGameTime() {
        addStat(prepareStat(TOTAL_TIME, getIntent().getStringExtra(TOTAL_TIME), mStatsTextSize, getApplicationContext()));
    }

    private void initButtons() {
        mBackButton = (Button) findViewById(R.id.backFeedbackButton);
        mPlayAgainButton = (Button) findViewById(R.id.playAgainFeedbackButton);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(MainActivity.class);
            }
        });

        mPlayAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getStringExtra(PLAY_AGAIN_ACTIVITY_TARGET) != null) {
                    try {
                        startNewActivity(Class.forName(getIntent().getStringExtra(PLAY_AGAIN_ACTIVITY_TARGET)));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    startNewActivity(MainActivity.class);
                }
            }
        });

    }

    private void initConcentrationPoints() {
        mParcelableConcentrationPointsList = getIntent().getParcelableArrayListExtra(CURR_GAME_CONCENTRATION_POINTS);

        for (ParcelableDataPoint p : mParcelableConcentrationPointsList) {
            mGraphConcentrationPoints.appendData(p, false, Integer.MAX_VALUE);
        }

        addSeries();
    }
}
