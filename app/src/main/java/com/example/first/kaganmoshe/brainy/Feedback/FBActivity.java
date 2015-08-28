package com.example.first.kaganmoshe.brainy.Feedback;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.CustomActivity.ActionBarAppActivity;
import com.example.first.kaganmoshe.brainy.GamesActivity;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Utils;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class FBActivity extends ActionBarAppActivity {
    public static final String CURR_GAME_CONCENTRATION_POINTS = "currGameConcentrationPoints";
    public static final String CONCENTRATION_AVERAGE = "Concentration";

    protected GraphView graphView;
    protected LineGraphSeries<DataPoint> graphConcentrationPoints = new LineGraphSeries<>();
    protected ArrayList<ParcelableDataPoint> parcelableConcentrationPointsList;
    protected Button backButton;
    protected LinearLayout feedbackStatsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedbackStatsLayout = (LinearLayout) findViewById(R.id.feedbackStatsLayout);

        initConcentrationPoints();
//        initScoreStat();
//        initBestScore();
        initGraph();
        initButtons();
    }

//    private void initBestScore() {
//        checkBestScore();
//    }
//
//    private void checkBestScore(){
//        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
//        int defaultValue = getResources().getInteger(R.integer.default_high_score);
//        long highScore = sharedPref.getInt(getIntent().getStringExtra(Utils.CALLING_CLASS), defaultValue);
//
//        if(highScore < finalScore){
//            setBestScore();
//            scoreTextView.setTextColor(getResources().getColor(R.color.feedback_best_score_text));
////            addStat("New high score", String.valueOf(finalScore));
//        }
//
//        bestScoreTextView.append(String.valueOf(sharedPref.getInt(getIntent().getStringExtra(Utils.CALLING_CLASS), defaultValue)));
//    }
//
//    private void setBestScore(){
//        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putInt(getIntent().getStringExtra(Utils.CALLING_CLASS), finalScore);
//        editor.commit();
//    }
//
//    private void initScoreStat() {
//        int gameScore = Integer.valueOf(getIntent().getStringExtra(SCORE_STAT));
//        int generalDistraction = Integer.valueOf(getIntent().getStringExtra(DISTRACTION_STAT));
//
//        int concentrationScore = FeedbackClass.getConcentrationScore(parcelableConcentrationPointsList);
//
//        addConcentrationStat(concentrationScore);
//
//        if(concentrationScore > BEST_CONCENTRATION_SCORE) {
//            concentrationScore = 100;
//        }
//
//        finalScore = gameScore + generalDistraction + concentrationScore;
//
//        scoreTextView.setText(Integer.toString(finalScore));
//    }

    protected void addConcentrationStat(int score){
        addStat(CONCENTRATION_AVERAGE, Integer.toString(score) + " (0-100)");
    }

    protected void addStat(String statName, String statValue) {
        LinearLayout newStatLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_feedback_stat, null);
        TextView statKeyText = (TextView) newStatLayout.findViewById(R.id.statNameText);
        TextView statValueText = (TextView) newStatLayout.findViewById(R.id.statValueText);

        statKeyText.setText(formatStatKey(statName));
        statValueText.setText(formatStatValue(statValue));

        feedbackStatsLayout.addView(newStatLayout);
    }

    protected String formatStatValue(String statValue) {
        return statValue.substring(0, 1).toUpperCase() + statValue.substring(1, statValue.length()).toLowerCase();
    }

    protected String formatStatKey(String statName) {
        return statName.substring(0, 1).toUpperCase() + statName.substring(1, statName.length()).toLowerCase() + ":";
    }

    protected void initButtons() {
        backButton = (Button) findViewById(R.id.backFeedbackButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(GamesActivity.class);
            }
        });
    }

    protected void startNewActivity(Class toActivity) {
        Utils.startNewActivity(this, toActivity);
    }

    @Override
    public void onBackPressed() {
        Utils.startNewActivity(this, GamesActivity.class);
    }

    protected void initConcentrationPoints() {
        parcelableConcentrationPointsList = getIntent().getParcelableArrayListExtra(CURR_GAME_CONCENTRATION_POINTS);

        for (ParcelableDataPoint p : parcelableConcentrationPointsList) {
            graphConcentrationPoints.appendData(p, false, Integer.MAX_VALUE);
        }

        int concentrationScore = FeedbackClass.getConcentrationScore(parcelableConcentrationPointsList);

        addConcentrationStat(concentrationScore);
    }

    protected void initGraph() {
//        ArrayList<ParcelableDataPoint> parcelableConcentrationPointsList = getIntent().getParcelableArrayListExtra(CURR_GAME_CONCENTRATION_POINTS);
//
////        prepareConcentrationPoints();
//        for(ParcelableDataPoint p : parcelableConcentrationPointsList){
//            graphConcentrationPoints.appendData(p, false, Integer.MAX_VALUE);
//        }

//        initTitle();

        graphView = (GraphView) findViewById(R.id.graph);
        graphConcentrationPoints.setThickness(6);
        graphConcentrationPoints.setDataPointsRadius(7f);

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

}
