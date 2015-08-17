package com.example.first.kaganmoshe.brainy.CrazyCube;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.example.first.kaganmoshe.brainy.CustomActivity.GameActivity;
import com.example.first.kaganmoshe.brainy.CustomActivity.GameGraph;
import com.example.first.kaganmoshe.brainy.CustomActivity.GameGraphActivity;
import com.example.first.kaganmoshe.brainy.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

public class CrazyCubeActivity extends GameGraphActivity {

    private TableLayout gameTable;
    private Context context;
    private Random random = new Random();
    private int currBoardSize = INIT_BOARD_SIZE - 1;
    private int currColor;
    private int currSpecialCellFactor = MAX_SPECIAL_CELL_FACTOR - 10;
    private TextView scoreTextView;
    private TextView timeTextView;
    private Handler handler = new Handler();
    private int currTime = TIME_FOR_GAME;
    private int currScore = 0;
    private Runnable timer;
    private boolean timerOn = true;
    private double lastConcentrationAverage = 0;
    private int currConcentrationListIndex = 0;
    private ArrayList<DataPoint> concentrationList = new ArrayList<>();
    private Button currSpecialCell = null;

//    private GraphView graphView;
//    private LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
//    private int lastX = 0;

    private static final int MIN_SPECIAL_CELL_FACTOR = -5;
    private static final int MAX_SPECIAL_CELL_FACTOR = -40;
    private static final int FACTOR_DELTA_JUMP = 5;
    private static final int INIT_BOARD_SIZE = 2;
    private static final int MAX_BOARD_SIZE = 8;
    //    private static final int TIME_FOR_GAME = 60;
    private static final int TIME_FOR_GAME = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crazy_cube);

        gameTable = (TableLayout) findViewById(R.id.table);
        scoreTextView = (TextView) findViewById(R.id.CCubeScoreTextView);
        timeTextView = (TextView) findViewById(R.id.CCubeTimeTextView);
        context = getApplicationContext();

        gameGraph = new GameGraph((GraphView)findViewById(R.id.graph), this);
        setScore(currScore);

        timer = new Runnable() {
            @Override
            public void run() {
                updateTimeTextView();
            }
        };

        startFeedbackSession();
        BuildTable(++currBoardSize);

//        ResumeGameCountDown rgc = new ResumeGameCountDown();
//        rgc.show(fm, "Countdown");
    }

    private void stopClock() {
        handler.removeCallbacks(timer);
        timerOn = false;
    }

    private void resumeClock() {
        timerOn = true;
        handler.postDelayed(timer, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopClock();
        hideSpecialCell();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (graphView == null) {
//            graphFragment = (GraphFragment) fm.findFragmentById(R.id.fragment);
////            graphFragment.stopReceivingData();
//      }
//        resumeClock();
    }

    private void updateTimeTextView() {
        if (timerOn) {
            timeTextView.setText(Integer.toString(currTime--));

            if (currTime != 0)
                handler.postDelayed(timer, 1000);
            else
                showFinishDialog();
        }
    }

    private void setScore(int score) {
        scoreTextView.setText(Integer.toString(score));
    }

    public void pickColor() {
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);

        red = (red + 150) / 2;
        green = (green + 150) / 2;
        blue = (blue + 150) / 2;

        currColor = Color.rgb(red, green, blue);
    }

    private void BuildTable(int n) {

        pickColor();

        for (int i = 0; i < n; i++) {
            TableRow row = new TableRow(context);
            row.setWeightSum(n);

            TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0,
                    gameTable.getWeightSum() / n);
            tableLayoutParams.setMargins(1, 2, 1, 2);
            gameTable.addView(row, i, tableLayoutParams);

            for (int j = 0; j < n; j++) {
                Button button = (Button) LayoutInflater.from(context).inflate(R.layout.ccube_table_button, null);
                button.setBackgroundColor(currColor);
                TableRow.LayoutParams tableColLayoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1);
                tableColLayoutParams.setMargins(2, 0, 2, 0);
                row.addView(button, j, tableColLayoutParams);
            }
        }

        setSpecialCell();
    }

    private void setSpecialCell() {

        int row = random.nextInt(currBoardSize);
        int column = random.nextInt(currBoardSize);

        TableRow tableRow = (TableRow) gameTable.getChildAt(row);

        if (currSpecialCellFactor < MAX_SPECIAL_CELL_FACTOR)
            currSpecialCellFactor += FACTOR_DELTA_JUMP;
        else
            updateCellFactor();

        currSpecialCell = (Button) tableRow.getChildAt(column);
        currSpecialCell.setBackgroundColor(currColor + currSpecialCellFactor);
        currSpecialCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScore(++currScore);
                gameTable.removeAllViews();
                BuildTable((currBoardSize < MAX_BOARD_SIZE) ? ++currBoardSize : MAX_BOARD_SIZE);
            }
        });
    }

    private void hideSpecialCell(){
        currSpecialCell.setBackgroundColor(currColor);
    }

    private void showSpecialCell(){
        currSpecialCell.setBackgroundColor(currColor + currSpecialCellFactor);
    }

    private void updateCellFactor() {
        double average = getAverageConcentration();

        if (average > lastConcentrationAverage && currSpecialCellFactor > MAX_SPECIAL_CELL_FACTOR)
            currSpecialCellFactor -= FACTOR_DELTA_JUMP;
        else if (average < lastConcentrationAverage && currSpecialCellFactor < MIN_SPECIAL_CELL_FACTOR)
            currSpecialCellFactor += FACTOR_DELTA_JUMP;

        lastConcentrationAverage = average;

        Log.d("CCUBE_ACTIVITY", Double.toString(average));
        Log.d("CCUBE_ACTIVITY", Integer.toString(currSpecialCellFactor));
    }

    private double getAverageConcentration() {
        double sum = 0;
        int size = concentrationList.size() - currConcentrationListIndex;

        for (; currConcentrationListIndex < size; currConcentrationListIndex++) {
            sum += concentrationList.get(currConcentrationListIndex).getY();
        }

        return (size > 0) ? (sum / size) : lastConcentrationAverage;
    }

    @Override
    protected void startFeedbackSession() {
        feedback = new CCubeFeedback();
        feedback.startTimer();
    }

    @Override
    public void onAttentionReceived(final int attValue) {
        concentrationList.add(new DataPoint(concentrationList.size(), attValue));
    }

    @Override
    protected void onResumeGameShow() {
        hideSpecialCell();
    }

    @Override
    public void onDialogShow(Class thisClass) {
        stopClock();
        super.onDialogShow(thisClass);
    }

    @Override
    protected void onMenuPopupShow() {
        stopClock();
    }

    @Override
    public void onGameResumed() {
        super.onGameResumed();
        resumeClock();
        showSpecialCell();
    }

    @Override
    public void onFinishGameContinueClicked() {
        LinkedHashMap<String, String> extraStats = new LinkedHashMap<>();

//        extraStats.put("Test", "Success");
//        extraStats.put("Test2", "Success");
//        extraStats.put("Test3", "3:21");

        setNewStatsListAndContinue(extraStats);
    }

    @Override
    protected int calculateScore() {
        return 100;
    }

    @Override
    public void homeMenuButtonClicked(){
        super.homeMenuButtonClicked();

        hideSpecialCell();
    }
}
