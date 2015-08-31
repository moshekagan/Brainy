package com.example.first.kaganmoshe.brainy.Feedback;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.first.kaganmoshe.brainy.AppActivities.MainActivity;
import com.example.first.kaganmoshe.brainy.R;
import com.jjoe64.graphview.series.DataPoint;

public class DPFeedbackActivity extends FBActivity {
    protected Button mContinueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dpfeedback);

        mFeedbackStatsLayout = (LinearLayout) findViewById(R.id.feedbackStatsLayout);

        this.mStatsTextSize = 19f;
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
//        int concentrationScore = FeedbackClass.getConcentrationScore(mParcelableConcentrationPointsList);
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

//    protected void addConcentrationStat(int score){
//        addStat(CONCENTRATION_AVERAGE, Integer.toString(score) + " (0-100)");
//    }
//
//    protected void addStat(String statName, String statValue) {
//        LinearLayout newStatLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_feedback_stat, null);
//        TextView statKeyText = (TextView) newStatLayout.findViewById(R.id.statNameText);
//        TextView statValueText = (TextView) newStatLayout.findViewById(R.id.statValueText);
//
//        statKeyText.setText(formatStatKey(statName));
//        statValueText.setText(formatStatValue(statValue));
//        statKeyText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f);
//        statValueText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f);
//
//        mFeedbackStatsLayout.addView(newStatLayout);
//    }

//    protected String formatStatValue(String statValue) {
//        return statValue.substring(0, 1).toUpperCase() + statValue.substring(1, statValue.length()).toLowerCase();
//    }
//
//    protected String formatStatKey(String statName) {
//        return statName.substring(0, 1).toUpperCase() + statName.substring(1, statName.length()).toLowerCase() + ":";
//    }

    protected void initButtons() {
        mContinueButton = (Button) findViewById(R.id.continueButton);

        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(MainActivity.class);
            }
        });
    }

//    protected void startNewActivity(Class toActivity) {
//        Utils.startNewActivity(this, toActivity);
//    }

//    @Override
//    public void onBackPressed() {
//        Utils.startNewActivity(this, MainActivity.class);
//    }

    protected void initConcentrationPoints() {
        mParcelableConcentrationPointsList = getIntent().getParcelableArrayListExtra(CURR_GAME_CONCENTRATION_POINTS);

        mGraphConcentrationPoints.appendData(new DataPoint(0, 0), false, Integer.MAX_VALUE);

        for (ParcelableDataPoint p : mParcelableConcentrationPointsList) {
            mGraphConcentrationPoints.appendData(p, false, Integer.MAX_VALUE);
        }

        int concentrationScore = FeedbackClass.getConcentrationScore(mParcelableConcentrationPointsList);

        addConcentrationStat(concentrationScore);
    }

//    protected void initGraph() {
//
//
//        mGraphView = (GraphView) findViewById(R.id.graph);
//        mGraphConcentrationPoints.setThickness(6);
//        mGraphConcentrationPoints.setDataPointsRadius(7f);
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
}
