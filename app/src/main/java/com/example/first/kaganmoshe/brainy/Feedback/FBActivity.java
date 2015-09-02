package com.example.first.kaganmoshe.brainy.Feedback;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.AppActivities.ActionBarActivity.ActionBarAppActivity;
import com.example.first.kaganmoshe.brainy.AppActivities.AppActivity;
import com.example.first.kaganmoshe.brainy.AppActivities.MainActivity;
import com.example.first.kaganmoshe.brainy.AppManagement.AppManager;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Utils;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public abstract class FBActivity extends ActionBarAppActivity {
    public static final String CURR_GAME_CONCENTRATION_POINTS = "currGameConcentrationPoints";
    public static final String CONCENTRATION_AVERAGE = "Concentration";

    protected float mStatsTextSize = 18f;
    protected GraphView mGraphView;
    protected LineGraphSeries<DataPoint> mGraphConcentrationPoints = new LineGraphSeries<>();
    protected ArrayList<ParcelableDataPoint> mParcelableConcentrationPointsList;
    protected LinearLayout mFeedbackStatsLayout;
//    protected Button continueButton;

    @Override
    protected void onResume(){
        super.onResume();
//        AppManager.playBackgroundMusic();
    }

    @Override
    protected void onPause(){
        super.onPause();
//        AppManager.pauseBackgroundMusic();
    }

    protected void addConcentrationStat(int score){
        addStat(prepareStat(CONCENTRATION_AVERAGE, Integer.toString(score) + " (0-100)", mStatsTextSize, getApplicationContext()));
//        addStat(CONCENTRATION_AVERAGE, Integer.toString(score) + " (0-100)");
    }

//    protected void addStat(String statName, String statValue) {
//        LinearLayout newStatLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_feedback_stat, null);
//        TextView statKeyText = (TextView) newStatLayout.findViewById(R.id.statNameText);
//        TextView statValueText = (TextView) newStatLayout.findViewById(R.id.statValueText);
//
//        statKeyText.setText(formatStatKey(statName));
//        statValueText.setText(formatStatValue(statValue));
//        statKeyText.setTextSize(TypedValue.COMPLEX_UNIT_SP, mStatsTextSize);
//        statValueText.setTextSize(TypedValue.COMPLEX_UNIT_SP, mStatsTextSize);
//
//        mFeedbackStatsLayout.addView(newStatLayout);
//    }

    protected static LinearLayout prepareStat(String statName, String statValue, float textSize, Context context){
        LinearLayout newStatLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_feedback_stat, null);
        TextView statKeyText = (TextView) newStatLayout.findViewById(R.id.statNameText);
        TextView statValueText = (TextView) newStatLayout.findViewById(R.id.statValueText);

        statKeyText.setText(formatStatKey(statName));
        statValueText.setText(formatStatValue(statValue));
        statKeyText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        statValueText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        return newStatLayout;
    }

    protected void addStat(LinearLayout statLayout){
        mFeedbackStatsLayout.addView(statLayout);
    }

    protected static String formatStatValue(String statValue) {
        return statValue.substring(0, 1).toUpperCase() + statValue.substring(1, statValue.length()).toLowerCase();
    }

    protected static String formatStatKey(String statName) {
        return statName.substring(0, 1).toUpperCase() + statName.substring(1, statName.length()).toLowerCase() + ":";
    }

    protected void startNewActivity(Class toActivity) {
        Utils.startNewActivity(this, toActivity);
    }

    @Override
    public void onBackPressed() {
        Utils.startNewActivity(this, MainActivity.class);
    }

    protected void initGraph() {
        mGraphView = (GraphView) findViewById(R.id.graph);
        mGraphConcentrationPoints.setDrawDataPoints(true);
        mGraphConcentrationPoints.setThickness(6);
        mGraphConcentrationPoints.setDataPointsRadius(7f);

        GridLabelRenderer gridLabelRenderer = mGraphView.getGridLabelRenderer();
        gridLabelRenderer.setNumHorizontalLabels(2);
        gridLabelRenderer.setNumVerticalLabels(3);
        gridLabelRenderer.setHorizontalLabelsVisible(false);

        Viewport viewport = mGraphView.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setXAxisBoundsManual(true);
        viewport.setMaxX(20);
        viewport.setMinY(0);
        viewport.setMaxY(100);
        viewport.setScrollable(true);
//        viewport.setMaxX(mGraphConcentrationPoints.getHighestValueX());




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
//        viewport.setMaxX(10);
//        viewport.setMinY(0);
//        viewport.setMaxY(100);
//        viewport.setScrollable(true);
//        viewport.setMaxX(mGraphConcentrationPoints.getHighestValueX());

//        mGraphView.addSeries(mGraphConcentrationPoints);
    }

    protected void addSeries(){
        mGraphView.addSeries(mGraphConcentrationPoints);
    }

    public static void setBestScore(AppActivity appActivity, String key, int score){
        SharedPreferences sharedPref = appActivity.getApplicationContext().getSharedPreferences("bestScore", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, score);
        Log.d("DP", "setBestScore: key=" + key + " score=" + score);
        editor.commit();
    }
}
