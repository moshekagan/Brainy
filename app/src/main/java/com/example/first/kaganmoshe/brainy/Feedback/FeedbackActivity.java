package com.example.first.kaganmoshe.brainy.Feedback;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.CustomActivity.AppActivity;
import com.example.first.kaganmoshe.brainy.GuessTheNumber.GuessTheNumberConfigActivity;
import com.example.first.kaganmoshe.brainy.GuessTheNumber.GuessTheNumberEngine;
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
    public static final String CURR_GAME_SCORE = "currGameScore";

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
        ArrayList<String> stats = intent.getStringArrayListExtra("EXTRA_STATS");

        for(String statName : stats){
            String statValue = intent.getStringExtra(statName);
            addStat(statName, statValue);
        }

//        game = new GuessTheNumberEngine(Integer.parseInt(intent.getStringExtra(GuessTheNumberConfigActivity.EXTRA_MESSAGE)));
//        guessRequestText.append(" " + Integer.toString(game.getMaxValue()));
    }

    private void addStat(String statName, String statValue) {
//        LayoutInflater inflater = (LayoutInflater)this.getSystemService
//                (Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout newStatLayout = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.layout_feedback_stat, null);
        TextView statNameText = (TextView) newStatLayout.findViewById(R.id.statNameText);
        TextView statValueText = (TextView) newStatLayout.findViewById(R.id.statValueText);
        statNameText.setText(statName);
        statValueText.setText(statValue);

//        LinearLayout mainView = (LinearLayout)this.findViewById(R.id.mainLayout);
        feedbackStatsLayout.addView(newStatLayout);

//        newStatLayout.setMinimumWidth();


//        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
//                LayoutParams.MATCH_PARENT,
//                LayoutParams.MATCH_PARENT, 1.0f);


//        <LinearLayout
//        android:layout_width="@dimen/feedbackStatLayoutWidth"
//        android:layout_height="wrap_content"
//        android:orientation="horizontal">
//
//        <TextView
//                style="@style/feedbackActivityTextView"
//        android:layout_width="wrap_content"
//        android:layout_height="match_parent"
//        android:layout_marginEnd="@dimen/pad_5dp"
//        android:text="@string/feedbackScoreView" />
//
//        <TextView
//        android:id="@+id/feedbackScoreViewText"
//        style="@style/feedbackActivityTextViewText"
//        android:layout_width="wrap_content"
//        android:layout_height="match_parent"
//        android:gravity="bottom"
//        android:text="" />
//
//        </LinearLayout>
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
                startNewActivity(GuessTheNumberConfigActivity.class);
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

        for(ParcelableDataPoint p : concentrationPointsList){
            concentrationPoints.appendData(p, false, Integer.MAX_VALUE);
        }

        initTitle();

        graphView = (GraphView) findViewById(R.id.graph);
        concentrationPoints.setThickness(6);

        GridLabelRenderer gridLabelRenderer = graphView.getGridLabelRenderer();

//        gridLabelRenderer.setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
//        gridLabelRenderer.setHighlightZeroLines(true);

        gridLabelRenderer.setNumHorizontalLabels(2);
        gridLabelRenderer.setNumVerticalLabels(3);
        gridLabelRenderer.setHorizontalLabelsVisible(false);

        Viewport viewport = graphView.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setXAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(100);
//        viewport.setMinX(0);
        viewport.setMaxX(concentrationPointsList.get(concentrationPointsList.size() - 1).getX());
        viewport.setScrollable(false);

        graphView.addSeries(concentrationPoints);
    }

    private void initTitle() {
        feedbackTitle = (TextView) findViewById(R.id.feedbackTitle);
        Utils.changeFont(getAssets(), feedbackTitle);
    }
}
