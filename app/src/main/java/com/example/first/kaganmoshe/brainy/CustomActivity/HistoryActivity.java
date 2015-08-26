package com.example.first.kaganmoshe.brainy.CustomActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.first.kaganmoshe.brainy.AppManager;
import com.example.first.kaganmoshe.brainy.ConnectionActivity;
import com.example.first.kaganmoshe.brainy.GamesActivity;
import com.example.first.kaganmoshe.brainy.HistoryDBAdapter;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Utils;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import java.util.ArrayList;
import java.util.Date;

public class HistoryActivity extends ActionBarAppActivity {

    public class HistoryDataPoint extends com.jjoe64.graphview.series.DataPoint {
        private final HistoryRecordData data;

        public HistoryDataPoint(double x, double y, HistoryRecordData data) {
            super(x, y);
            this.data = data;
        }

        public HistoryDataPoint(Date x, double y, HistoryRecordData data) {
            super((double) x.getTime(), y);
            this.data = data;
        }

        public HistoryRecordData getData() {
            return this.data;
        }
    }

    private BetterSpinner itemsSpinner;
    private BetterSpinner timeRangeSpinner;
    private HistoryDBAdapter.ETimeRange currTimeRangeSelection = HistoryDBAdapter.ETimeRange.LAST_WEEK;
    private String currGameSelection = "Guess The Number";
    private GraphView graphView;
    private LineGraphSeries<DataPoint> concentrationSeries;
    private LineGraphSeries<DataPoint> scoreSeries;
    private TextView nameTextView;
    private TextView scoreTextView;
    private TextView dateTextView;
    private TextView concentrationTextView;

    private static final int SCORE_COLOR = Color.RED;
    private static final int CONCENTRATION_COLOR = Color.BLUE;

    private static final String[] ITEMS = new String[]{
            "Guess The Number",
            "Crazy Cube",
            "MindShooter",
            "Hot Air Balloon",
            "Daily Practices"
    };

    private static final ArrayList<String> TIME_RANGES = new ArrayList<>();

    static {
        for (HistoryDBAdapter.ETimeRange timeRange : HistoryDBAdapter.ETimeRange.values()) {
            TIME_RANGES.add(timeRange.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        String[] timeRangesStringArray = new String[TIME_RANGES.size()];

        for (int i = 0; i < TIME_RANGES.size(); i++) {
            timeRangesStringArray[i] = TIME_RANGES.get(i);
        }

        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(this, R.layout.app_spinner_row, ITEMS);
        ArrayAdapter<String> timeRangeAdapter = new ArrayAdapter<>(this, R.layout.app_spinner_row, timeRangesStringArray);

        itemsSpinner = (BetterSpinner) findViewById(R.id.showListSpinner);
        timeRangeSpinner = (BetterSpinner) findViewById(R.id.timeRangeSpinner);
        graphView = (GraphView) findViewById(R.id.graph);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        scoreTextView = (TextView) findViewById(R.id.scoreTextView);
        concentrationTextView = (TextView) findViewById(R.id.concentrationTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);

        timeRangeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HistoryDBAdapter.ETimeRange selection = HistoryDBAdapter.ETimeRange.enumValueOf(parent.getItemAtPosition(position).toString());

                resetTextViews();

                if (currTimeRangeSelection != selection) {
                    currTimeRangeSelection = HistoryDBAdapter.ETimeRange.enumValueOf(parent.getItemAtPosition(position).toString());
                    addRecords();

                    timeRangeSpinner.onItemClick(parent, view, position, id);
                }
            }
        });
        timeRangeSpinner.setAdapter(timeRangeAdapter);

        itemsSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = parent.getItemAtPosition(position).toString();

                resetTextViews();

                if (!currGameSelection.equals(selection)) {
                    currGameSelection = parent.getItemAtPosition(position).toString();
                    addRecords();

                    itemsSpinner.onItemClick(parent, view, position, id);
                }
            }
        });
        itemsSpinner.setAdapter(itemsAdapter);

        setTouchNClick(R.id.showListSpinner);
        setTouchNClick(R.id.timeRangeSpinner);

        initSeries();
        initGraph();
        addRecords();
    }

    private void addRecords() {
        ArrayList<HistoryRecordData> records = AppManager.getHistoryDBInstance(getApplicationContext()).getRecords(currGameSelection,
                currTimeRangeSelection);

        makeGraph(records);
//        for(String record : records){
//            addRecord(record);
//        }
    }

//    private void addRecord(String record) {
//        TextView recordTextView = new TextView(getApplicationContext());
//
//        recordTextView.setText(record);
//
//        itemsLayout.addView(recordTextView);
//    }

    @Override
    public void onBackPressed() {
        Utils.startNewActivity(this, GamesActivity.class);
    }

    private void makeGraph(ArrayList<HistoryRecordData> records) {
        int index = 0;

        graphView.removeAllSeries();
        graphView.getSecondScale().getSeries().clear();
        initSeries();
//        graphView.getViewport().setMinX(Doublerecords.get(0).date);

//        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
//        graphView.getGridLabelRenderer().setNumHorizontalLabels(3);

//        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
//        String[] dates = new String[records.size()];
//
//        for (int i = 0; i < records.size(); i++) {
//            dates[i] = records.get(i).date;
//        }
//
//        staticLabelsFormatter.setHorizontalLabels(dates);
//        staticLabelsFormatter.set
//        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        for (HistoryRecordData record : records) {
//            String dateString = record.date;
//            Log.d("HistoryActivity", "Date: " + record.date);
//
//            Date date = null;
//            try {
//                date = dateFormat.parse(dateString);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            Log.d("HistoryActivity", String.valueOf(date.getTime()));
//            String formattedDate = record.date.substring(8, 10) + "." + record.date.substring(5, 7)
//                    + "." + record.date.substring(0, 4) + " - " + record.date.substring(11, 16);
//            Log.d("HistoryActivity", "Date: " + formattedDate);
//            Double d = Double.parseDouble(formattedDate);
//            Log.d("HistoryActivity", "Dateformatted valueof: " + String.valueOf(d));
//            Log.d("HistoryActivity", "Dateformatted: " + d);

            concentrationSeries.appendData(new HistoryDataPoint(index, record.concentration, record),
                    false, Integer.MAX_VALUE);
            scoreSeries.appendData(new HistoryDataPoint(index++, record.score, record),
                    false, Integer.MAX_VALUE);
        }

        scoreSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                showData(((HistoryDataPoint) dataPoint).getData());
            }
        });

        concentrationSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                showData(((HistoryDataPoint) dataPoint).getData());
            }
        });

        if (records.size() > 0) {
            graphView.getSecondScale().addSeries(scoreSeries);
            graphView.addSeries(concentrationSeries);
        }
    }

    private void showData(HistoryRecordData data) {
        if(!data.date.equals(dateTextView.getText())) {
            resetTextViews();

            nameTextView.setText(data.name);
            scoreTextView.setText(String.valueOf(data.score));
            dateTextView.setText(data.date);
            concentrationTextView.setText(String.valueOf(data.concentration));
        }
    }

    private void resetTextViews(){
        nameTextView.setText("");
        scoreTextView.setText("");
        concentrationTextView.setText("");
        dateTextView.setText("");
    }

    private void initGraph() {
        GridLabelRenderer gridLabelRenderer = graphView.getGridLabelRenderer();

//        NumberFormat nf = NumberFormat.getInstance();
//        nf.setMinimumFractionDigits(0);
//        nf.setMinimumIntegerDigits(0);
//        nf.setMaximumIntegerDigits(5);
//        nf.setMaximumFractionDigits(5);
//        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(nf, nf));

        graphView.getSecondScale().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    return super.formatLabel(value, isValueX);
                }

                return String.valueOf((int) value) + " \u00A0";
            }
        });

//        gridLabelRenderer.setHighlightZeroLines(false);


//        gridLabelRenderer.setTextSize(1);

//        gridLabelRenderer.setLabelsSpace(6);
        Viewport viewport = graphView.getViewport();
        viewport.setXAxisBoundsManual(true);
        viewport.setYAxisBoundsManual(true);
        viewport.setMaxX(20);
        viewport.setMinY(0);
        viewport.setMaxY(100);
        viewport.setScrollable(true);
        graphView.getSecondScale().setMaxY(1000);

        gridLabelRenderer.setVerticalLabelsSecondScaleColor(SCORE_COLOR);
        gridLabelRenderer.setVerticalLabelsColor(CONCENTRATION_COLOR);
    }

    private void initSeries() {
        concentrationSeries = new LineGraphSeries<>();
        scoreSeries = new LineGraphSeries<>();

        graphView.getLegendRenderer().setVisible(true);
        concentrationSeries.setTitle("Concentration");
        scoreSeries.setTitle("Score");
        graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graphView.getLegendRenderer().setBackgroundColor(android.R.color.transparent);
        concentrationSeries.setColor(CONCENTRATION_COLOR);
        scoreSeries.setColor(SCORE_COLOR);
    }
}
