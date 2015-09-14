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
        initGraph();
        mGraphView.getViewport().setMaxX(mGraphConcentrationPoints.getHighestValueX());
        initConcentrationPoints();
        initButtons();
    }

    protected void initButtons() {
        mContinueButton = (Button) findViewById(R.id.continueButton);

        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(MainActivity.class);
            }
        });

    }

    protected void initConcentrationPoints() {
        mParcelableConcentrationPointsList = getIntent().getParcelableArrayListExtra(CURR_GAME_CONCENTRATION_POINTS);

        mGraphConcentrationPoints.appendData(new DataPoint(0, 0), false, Integer.MAX_VALUE);

        for (ParcelableDataPoint p : mParcelableConcentrationPointsList) {
            mGraphConcentrationPoints.appendData(p, false, Integer.MAX_VALUE);
        }

        int concentrationScore = FeedbackClass.getConcentrationScore(mParcelableConcentrationPointsList);

        addConcentrationStat(concentrationScore);
        addSeries();
    }
}
