package com.example.first.kaganmoshe.brainy.Feedback;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.CustomActivity.ActionBarAppActivity;
import com.example.first.kaganmoshe.brainy.Utils;
import com.example.first.kaganmoshe.brainy.MenuActivity;
import com.example.first.kaganmoshe.brainy.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class FeedbackActivity extends ActionBarAppActivity {
    //TODO - take care of the onFinishDialogConfirmed method of the games
    //TODO - constants for the extra stats
    public static final String CURR_GAME_CONCENTRATION_POINTS = "currGameConcentrationPoints";
    public static final String CURR_GAME_TIME_MINUTES = "currGameTimeMinutes";
    public static final String CURR_GAME_TIME_SECONDS = "currGameTimeSeconds";
    public static final String EXTRA_STATS = "EXTRA_STATS";
    public static final String SCORE_STAT = "SCORE_STAT";
    public static final String DISTRACTION_STAT = "DISTRACTION_STAT";
    //    public static final String GAME_DISTRACTION_STAT = "GAME_DISTRACTION_STAT";
    public static final String SCORE_LABEL = "Score";
    public static final String PLAY_AGAIN_ACTIVITY_TARGET = "PLAY_AGAIN_ACTIVITY_TARGET";
    private static final int FACTOR = 20;
//    private static final float GAME_SCORE_WEIGHT = 0.5f;
//    private static final float GENERAL_DISTRACTION_WEIGHT = 0.2f;
//    private static final float CONCENTRATION_WEIGHT = 0.3f;
    public static final int BEST_CONCENTRATION_SCORE = 85;
    public static final String CONCENTRATION_AVERAGE = "Concentration average";

    protected GraphView graphView;
    protected LineGraphSeries<DataPoint> graphConcentrationPoints = new LineGraphSeries<>();
    protected ArrayList<ParcelableDataPoint> parcelableConcentrationPointsList;
    protected TextView timeView;
    protected Button backButton;
    protected Button playAgainButton;
    protected LinearLayout feedbackStatsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedbackStatsLayout = (LinearLayout) findViewById(R.id.feedbackStatsLayout);

        initConcentrationPoints();
        initStats();
        initExtraStats();
        initGraph();
        initButtons();
        initInfo();
    }

    private void initStats() {
        int gameScore = Integer.valueOf(getIntent().getStringExtra(SCORE_STAT));
        int generalDistraction = Integer.valueOf(getIntent().getStringExtra(DISTRACTION_STAT));
//        int gameDistraction = Integer.valueOf(getIntent().getStringExtra(GAME_DISTRACTION_STAT));

//        int finalScore = (int) (gameScore * GAME_SCORE_WEIGHT + generalDistraction * GENERAL_DISTRACTION_WEIGHT
//                + concentrationScore() * CONCENTRATION_WEIGHT);
        int finalScore = gameScore + generalDistraction + concentrationScore();

        ((TextView)findViewById(R.id.feedbackScoreTextView)).setText(Integer.toString(finalScore));
    }

    private int concentrationScore() {
        int concentrationSum = 0;
        int concentrationAvg;
        int finalScore;

        for (ParcelableDataPoint dp : parcelableConcentrationPointsList) {
            concentrationSum += dp.getY();
        }

        concentrationAvg = concentrationSum / parcelableConcentrationPointsList.size();
        addStat(CONCENTRATION_AVERAGE, Integer.toString(concentrationAvg));

        if(concentrationAvg > BEST_CONCENTRATION_SCORE){
            finalScore = 100;
        } else {
            finalScore = concentrationAvg;
        }

        return finalScore;
    }

    private void initExtraStats() {
        Intent intent = getIntent();
        ArrayList<String> stats = intent.getStringArrayListExtra(EXTRA_STATS);

        if (stats != null) {
            for (String statName : stats) {
                String statValue = intent.getStringExtra(statName);
                addStat(statName, statValue);
            }
        }
    }

    private void addStat(String statName, String statValue) {
        LinearLayout newStatLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_feedback_stat, null);
        TextView statKeyText = (TextView) newStatLayout.findViewById(R.id.statNameText);
        TextView statValueText = (TextView) newStatLayout.findViewById(R.id.statValueText);

        statKeyText.setText(formatStatKey(statName));
        statValueText.setText(formatStatValue(statValue));

        feedbackStatsLayout.addView(newStatLayout);
    }

    private String formatStatValue(String statValue) {
        return statValue.substring(0, 1).toUpperCase() + statValue.substring(1, statValue.length()).toLowerCase();
    }

    private String formatStatKey(String statName) {
        return statName.substring(0, 1).toUpperCase() + statName.substring(1, statName.length()).toLowerCase() + ":";
    }

    private void initInfo() {
        int sessionTimeMin = (int) getIntent().getLongExtra(CURR_GAME_TIME_MINUTES, 0);
        int sessionTimeSec = (int) getIntent().getLongExtra(CURR_GAME_TIME_SECONDS, 0);
        timeView = (TextView) findViewById(R.id.feedbackTimeViewText);

        timeView.append(" " + Integer.toString(sessionTimeMin) + ":" + Integer.toString(sessionTimeSec));
    }

    private void initButtons() {
        backButton = (Button) findViewById(R.id.backFeedbackButton);
        playAgainButton = (Button) findViewById(R.id.playAgainFeedbackButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(MenuActivity.class);
            }
        });

        playAgainButton.setOnClickListener(new View.OnClickListener() {
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
                    startNewActivity(MenuActivity.class);
                }
            }
        });
    }

    private void startNewActivity(Class toActivity) {
        Utils.startNewActivity(this, toActivity);
    }

    @Override
    public void onBackPressed() {
        Utils.startNewActivity(this, MenuActivity.class);
    }

    private void initConcentrationPoints() {
        parcelableConcentrationPointsList = getIntent().getParcelableArrayListExtra(CURR_GAME_CONCENTRATION_POINTS);

//        prepareConcentrationPoints();
        for (ParcelableDataPoint p : parcelableConcentrationPointsList) {
            graphConcentrationPoints.appendData(p, false, Integer.MAX_VALUE);
        }
    }

    private void initGraph() {
//        ArrayList<ParcelableDataPoint> parcelableConcentrationPointsList = getIntent().getParcelableArrayListExtra(CURR_GAME_CONCENTRATION_POINTS);
//
////        prepareConcentrationPoints();
//        for(ParcelableDataPoint p : parcelableConcentrationPointsList){
//            graphConcentrationPoints.appendData(p, false, Integer.MAX_VALUE);
//        }

//        initTitle();

        graphView = (GraphView) findViewById(R.id.graph);
        graphConcentrationPoints.setThickness(6);

        GridLabelRenderer gridLabelRenderer = graphView.getGridLabelRenderer();
        gridLabelRenderer.setNumHorizontalLabels(2);
        gridLabelRenderer.setNumVerticalLabels(3);
        gridLabelRenderer.setHorizontalLabelsVisible(false);

        Viewport viewport = graphView.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setXAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(100);
        viewport.setMaxX(graphConcentrationPoints.getHighestValueX());
        viewport.setScrollable(false);

        graphView.addSeries(graphConcentrationPoints);
    }

    private void prepareConcentrationPoints() {
//        ArrayList<ParcelableDataPoint> parcelableConcentrationPointsList = getIntent().getParcelableArrayListExtra(CURR_GAME_CONCENTRATION_POINTS);
//        double size = parcelableConcentrationPointsList.size() / FACTOR;
//        int currX = 0;
//        int currY = 0;
//        int currConcentrationListIndex = 0;
//
//        for(int i = 0; i < size; i++){
//            for(int y = 0; y < FACTOR; y++){
//                currY += parcelableConcentrationPointsList.get(currX++).getY();
//            }
//
//            graphConcentrationPoints.appendData(new DataPoint(currConcentrationListIndex++, currY / FACTOR), false, Integer.MAX_VALUE);
//            currY = 0;
//        }
    }

//    private void initTitle() {
//        feedbackTitle = (TextView) findViewById(R.id.feedbackTitle);
//        Utils.changeFont(getAssets(), feedbackTitle);
//    }
}
