package com.example.first.kaganmoshe.brainy.Feedback;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.GuessTheNumber.GuessTheNumberConfigActivity;
import com.example.first.kaganmoshe.brainy.Utils;
import com.example.first.kaganmoshe.brainy.MenuActivity;
import com.example.first.kaganmoshe.brainy.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class FeedbackActivity extends Activity {
    public static final String CURR_GAME_CONCENTRATION_POINTS = "currGameConcentrationPoints";
    public static final String CURR_GAME_TIME_MINUTES = "currGameTimeMinutes";
    public static final String CURR_GAME_TIME_SECONDS = "currGameTimeSeconds";
    public static final String CURR_GAME_SCORE = "currGameScore";
    private GraphView graphView;
    private LineGraphSeries<DataPoint> concentrationPoints = new LineGraphSeries<>();
    private TextView feedbackTitle;
    private TextView timeView;
    private Button backButton;
    private Button playAgainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initGraph();
        initButtons();
        initInfo();
    }

    private void initInfo() {
        long sessionTimeMin = getIntent().getLongExtra(CURR_GAME_TIME_MINUTES, 0);
        long sessionTimeSec = getIntent().getLongExtra(CURR_GAME_TIME_SECONDS, 0);
        timeView = (TextView) findViewById(R.id.feedbackTimeView);

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
        graphView.addSeries(concentrationPoints);

        Viewport viewport = graphView.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setXAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(100);
        viewport.setMinX(0);
        viewport.setMaxX(concentrationPointsList.get(concentrationPointsList.size() - 1).getX());
        viewport.setScrollable(true);
    }

    private void initTitle() {
        feedbackTitle = (TextView) findViewById(R.id.feedbackTitle);
        Utils.changeFont(getAssets(), feedbackTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
