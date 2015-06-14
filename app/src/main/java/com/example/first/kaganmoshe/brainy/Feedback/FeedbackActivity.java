package com.example.first.kaganmoshe.brainy.Feedback;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.first.kaganmoshe.brainy.GuessTheNumber.Utils;
import com.example.first.kaganmoshe.brainy.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class FeedbackActivity extends Activity {
    public static final String CURR_GAME_CONCENTRATION_POINTS = "currGameConcentrationPoints";
    private GraphView graphView;
    private LineGraphSeries<DataPoint> concentrationPoints = new LineGraphSeries<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initGraph();
    }

    @Override
    public void onBackPressed() {
        Utils.onBackKeyPressed(this);
    }

    private void initGraph(){
        ArrayList<ParcelableDataPoint> concentrationPointsList = getIntent().getParcelableArrayListExtra(CURR_GAME_CONCENTRATION_POINTS);

        for(ParcelableDataPoint p : concentrationPointsList){
            concentrationPoints.appendData(p, false, Integer.MAX_VALUE);
        }

        graphView = (GraphView) findViewById(R.id.graph);
        graphView.addSeries(concentrationPoints);

        Viewport viewport = graphView.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(100);
        viewport.setMinX(10);
        viewport.setScrollable(true);
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
