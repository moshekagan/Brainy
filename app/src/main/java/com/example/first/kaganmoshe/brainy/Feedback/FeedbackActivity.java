package com.example.first.kaganmoshe.brainy.Feedback;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
    //    public static final int BEST_CONCENTRATION_SCORE = 85;
    public static final String TOTAL_TIME = "Session time";
    private int mFinalScore;

    protected Button mBackButton;
    protected Button mPlayAgainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        mFeedbackStatsLayout = (LinearLayout) findViewById(R.id.feedbackStatsLayout);
//        bestScoreTextView = (TextView) findViewById(R.id.feedbackBestScoreTextView);
//        scoreTextView = (TextView) findViewById(R.id.feedbackScoreTextView);

        initGameTime();
        initConcentrationPoints();
        initScoreStat();
//        initBestScore();
        initExtraStats();
        initGraph();
        initButtons();
    }

//    private void initBestScore() {
//        checkBestScore();
//    }

    private int getBestScore() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("bestScore", Context.MODE_PRIVATE);
        int defaultValue = getResources().getInteger(R.integer.default_high_score);
        long highScore = sharedPref.getInt(getIntent().getStringExtra(Utils.CALLING_CLASS), defaultValue);

        Log.d("DP", "defaultValue=" + defaultValue + " highScore=" + highScore);

//        bestScoreTextView.append(String.valueOf(sharedPref.getInt(getIntent().getStringExtra(Utils.CALLING_CLASS), defaultValue)));
        return (int) highScore;
    }

    private void initScoreStat() {
        int gameScore = Integer.valueOf(getIntent().getStringExtra(SCORE_STAT));
        int generalDistraction = Integer.valueOf(getIntent().getStringExtra(DISTRACTION_STAT));
        int bestScore = getBestScore();
        int concentrationScore = FeedbackClass.getConcentrationScore(mParcelableConcentrationPointsList);

        addConcentrationStat(concentrationScore);

//        if(concentrationScore > BEST_CONCENTRATION_SCORE) {
//            concentrationScore = 100;
//        }

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

//    private void addConcentrationStat(int score){
//        addStat(CONCENTRATION_AVERAGE, Integer.toString(score) + " (0-100)");
//    }

    private void initExtraStats() {
        Intent intent = getIntent();
        ArrayList<String> stats = intent.getStringArrayListExtra(EXTRA_STATS);

        if (stats != null) {
            for (String statName : stats) {
                String statValue = intent.getStringExtra(statName);
                addStat(prepareStat(statName, statValue, mStatsTextSize, getApplicationContext()));
//                addStat(statName, statValue);
            }
        }
    }

//    private void addStat(String statName, String statValue) {
//        LinearLayout newStatLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_feedback_stat, null);
//        TextView statKeyText = (TextView) newStatLayout.findViewById(R.id.statNameText);
//        TextView statValueText = (TextView) newStatLayout.findViewById(R.id.statValueText);
//
//        statKeyText.setText(formatStatKey(statName));
//        statValueText.setText(formatStatValue(statValue));
//
//        mFeedbackStatsLayout.addView(newStatLayout);
//    }

//    private String formatStatValue(String statValue) {
//        return statValue.substring(0, 1).toUpperCase() + statValue.substring(1, statValue.length()).toLowerCase();
//    }
//
//    private String formatStatKey(String statName) {
//        return statName.substring(0, 1).toUpperCase() + statName.substring(1, statName.length()).toLowerCase() + ":";
//    }

    private void initGameTime() {
        addStat(prepareStat(TOTAL_TIME, getIntent().getStringExtra(TOTAL_TIME), mStatsTextSize, getApplicationContext()));
//        addStat(TOTAL_TIME, getIntent().getStringExtra(TOTAL_TIME));
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
                Log.d("PLAY_AGAIN_TARGET", getIntent().getStringExtra(PLAY_AGAIN_ACTIVITY_TARGET));
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

//    private void startNewActivity(Class toActivity) {
//        Utils.startNewActivity(this, toActivity);
//    }

//    @Override
//    public void onBackPressed() {
//        Utils.startNewActivity(this, MainActivity.class);
//    }

    private void initConcentrationPoints() {
        mParcelableConcentrationPointsList = getIntent().getParcelableArrayListExtra(CURR_GAME_CONCENTRATION_POINTS);

//        prepareConcentrationPoints();
        for (ParcelableDataPoint p : mParcelableConcentrationPointsList) {
            mGraphConcentrationPoints.appendData(p, false, Integer.MAX_VALUE);
        }
    }

//    private void initGraph() {
//        mGraphView = (GraphView) findViewById(R.id.graph);
//        mGraphConcentrationPoints.setThickness(6);
//
//        GridLabelRenderer gridLabelRenderer = mGraphView.getGridLabelRenderer();
//        gridLabelRenderer.setNumHorizontalLabels(2);
//        gridLabelRenderer.setNumVerticalLabels(3);
//        gridLabelRenderer.setHorizontalLabelsVisible(false);
//
//        Viewport viewport = mGraphView.getViewport();
//        viewport.setYAxisBoundsManual(true);
//        viewport.setXAxisBoundsManual(true);
//        viewport.setMinY(0);
//        viewport.setMaxY(100);
//        viewport.setMaxX(mGraphConcentrationPoints.getHighestValueX());
//        viewport.setScrollable(false);
//
//        mGraphView.addSeries(mGraphConcentrationPoints);
//    }

    private void prepareConcentrationPoints() {
//        ArrayList<ParcelableDataPoint> mParcelableConcentrationPointsList = getIntent().getParcelableArrayListExtra(CURR_GAME_CONCENTRATION_POINTS);
//        double size = mParcelableConcentrationPointsList.size() / FACTOR;
//        int currX = 0;
//        int currY = 0;
//        int currConcentrationListIndex = 0;
//
//        for(int i = 0; i < size; i++){
//            for(int y = 0; y < FACTOR; y++){
//                currY += mParcelableConcentrationPointsList.get(currX++).getY();
//            }
//
//            mGraphConcentrationPoints.appendData(new DataPoint(currConcentrationListIndex++, currY / FACTOR), false, Integer.MAX_VALUE);
//            currY = 0;
//        }
    }

//    private void initTitle() {
//        feedbackTitle = (TextView) findViewById(R.id.feedbackTitle);
//        Utils.changeFont(getAssets(), feedbackTitle);
//    }
}
