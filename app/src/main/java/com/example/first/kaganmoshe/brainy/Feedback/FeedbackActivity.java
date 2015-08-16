package com.example.first.kaganmoshe.brainy.Feedback;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.first.kaganmoshe.brainy.CustomActivity.AppActivity;
import com.example.first.kaganmoshe.brainy.Utils;
import com.example.first.kaganmoshe.brainy.MenuActivity;
import com.example.first.kaganmoshe.brainy.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class FeedbackActivity extends AppActivity {
    //TODO - take care of the onFinishDialogConfirmed method of the games
    //TODO - constants for the extra stats
    public static final String CURR_GAME_CONCENTRATION_POINTS = "currGameConcentrationPoints";
    public static final String CURR_GAME_TIME_MINUTES = "currGameTimeMinutes";
    public static final String CURR_GAME_TIME_SECONDS = "currGameTimeSeconds";
    public static final String EXTRA_STATS = "EXTRA_STATS";
    public static final String PLAY_AGAIN_ACTIVITY_TARGET = "PLAY_AGAIN_ACTIVITY_TARGET";
    private static final int FACTOR = 20;

    protected GraphView graphView;
    protected LineGraphSeries<DataPoint> concentrationPoints = new LineGraphSeries<>();
    protected TextView feedbackTitle;
    protected TextView timeView;
    protected Button backButton;
    protected Button playAgainButton;
    protected LinearLayout feedbackStatsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedbackStatsLayout = (LinearLayout)findViewById(R.id.feedbackStatsLayout);

        initExtraStats();
        initGraph();
        initButtons();
        initInfo();
    }

    private void initExtraStats() {
        Intent intent = getIntent();
        ArrayList<String> stats = intent.getStringArrayListExtra(EXTRA_STATS);

        if(stats != null){
            for(String statName : stats){
                String statValue = intent.getStringExtra(statName);
                addStat(statName, statValue);
            }
        }
    }

    private void addStat(String statName, String statValue) {
        LinearLayout newStatLayout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.layout_feedback_stat, null);
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
        long sessionTimeMin = getIntent().getLongExtra(CURR_GAME_TIME_MINUTES, 0);
        long sessionTimeSec = getIntent().getLongExtra(CURR_GAME_TIME_SECONDS, 0);
        timeView = (TextView) findViewById(R.id.feedbackTimeViewText);

        timeView.append(" " + Long.toString(sessionTimeMin) + ":" + Long.toString(sessionTimeSec));
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

    private void initGraph(){
        ArrayList<ParcelableDataPoint> concentrationPointsList = getIntent().getParcelableArrayListExtra(CURR_GAME_CONCENTRATION_POINTS);

//        prepareConcentrationPoints();
        for(ParcelableDataPoint p : concentrationPointsList){
            concentrationPoints.appendData(p, false, Integer.MAX_VALUE);
        }

//        initTitle();

        graphView = (GraphView) findViewById(R.id.graph);
        concentrationPoints.setThickness(6);

        GridLabelRenderer gridLabelRenderer = graphView.getGridLabelRenderer();
        gridLabelRenderer.setNumHorizontalLabels(2);
        gridLabelRenderer.setNumVerticalLabels(3);
        gridLabelRenderer.setHorizontalLabelsVisible(false);

        Viewport viewport = graphView.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setXAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(100);
        viewport.setMaxX(concentrationPoints.getHighestValueX());
        viewport.setScrollable(false);

        graphView.addSeries(concentrationPoints);
    }

    private void prepareConcentrationPoints() {
//        ArrayList<ParcelableDataPoint> concentrationPointsList = getIntent().getParcelableArrayListExtra(CURR_GAME_CONCENTRATION_POINTS);
//        double size = concentrationPointsList.size() / FACTOR;
//        int currX = 0;
//        int currY = 0;
//        int currConcentrationListIndex = 0;
//
//        for(int i = 0; i < size; i++){
//            for(int y = 0; y < FACTOR; y++){
//                currY += concentrationPointsList.get(currX++).getY();
//            }
//
//            concentrationPoints.appendData(new DataPoint(currConcentrationListIndex++, currY / FACTOR), false, Integer.MAX_VALUE);
//            currY = 0;
//        }
    }

//    private void initTitle() {
//        feedbackTitle = (TextView) findViewById(R.id.feedbackTitle);
//        Utils.changeFont(getAssets(), feedbackTitle);
//    }
}
