package com.example.first.kaganmoshe.brainy.CrazyCube;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.CustomActivity.CustomActivity;
import com.example.first.kaganmoshe.brainy.Feedback.FeedbackActivity;
import com.example.first.kaganmoshe.brainy.Feedback.FeedbackClass;
import com.example.first.kaganmoshe.brainy.Feedback.ParcelableDataPoint;
import com.example.first.kaganmoshe.brainy.GuessTheNumber.GTNFeedback;
import com.example.first.kaganmoshe.brainy.GuessTheNumber.WinnerDialogFragment;
import com.example.first.kaganmoshe.brainy.MenuActivity;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Utils;

import java.util.List;
import java.util.Random;

public class CrazyCubeActivity extends CustomActivity implements WinnerDialogFragment.gameCommunicator {

    private TableLayout gameTable;
    private Context context;
    private Random random = new Random();
    private int currBoardSize = INIT_BOARD_SIZE - 1;
    private int currColor;
    private int currSpecialCellFactor = MAX_SPECIAL_CELL_FACTOR - 10;
//    private TextView headlineText;
    private TextView scoreTextView;
    private TextView timeTextView;
    private Handler handler = new Handler();
    private int currTime = TIME_FOR_GAME;
    private int currScore = 0;
    private Runnable timer = new Runnable() {
        @Override
        public void run() {
            updateTimeTextView();
        }
    };
    private android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
    private FeedbackClass feedback;
    private double lastConcentrationAverage = 0;
    private int currConcentrationPointsListIndex = 0;

    private static final int MIN_SPECIAL_CELL_FACTOR = -5;
    private static final int MAX_SPECIAL_CELL_FACTOR = -40;
    private static final int FACTOR_DELTA_JUMP = 5;
    private static final int INIT_BOARD_SIZE = 2;
    private static final int MAX_BOARD_SIZE = 8;
    private static final int TIME_FOR_GAME = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crazy_cube);
        gameTable = (TableLayout) findViewById(R.id.table);
//        headlineText = (TextView) findViewById(R.id.CrazyCubeHeadline);
        scoreTextView = (TextView) findViewById(R.id.CCubeScoreTextView);
        timeTextView = (TextView) findViewById(R.id.CCubeTimeTextView);
        context = getApplicationContext();
        feedback = new CCubeFeedback();

        //Utils.changeFont(getAssets(), headlineText);
        setScore(currScore);
        timer.run();
        feedback.startTimer();

        BuildTable(++currBoardSize);
    }

    private void updateTimeTextView() {
        timeTextView.setText(Integer.toString(currTime--));
        if(currTime != 0)
            handler.postDelayed(timer, 1000);
        else
            finishGame();
    }

    private void finishGame() {
        feedback.stopTimer();
        WinnerDialogFragment winnerDialogFragment = new WinnerDialogFragment();
        winnerDialogFragment.setGameScreen(this);
        winnerDialogFragment.show(fm, "WinnerDialogFragment");
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
                Button button = (Button) LayoutInflater.from(this).inflate(R.layout.ccube_table_button, null);
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

        if(currSpecialCellFactor < MAX_SPECIAL_CELL_FACTOR)
            currSpecialCellFactor += FACTOR_DELTA_JUMP;
        else
            updateCellFactor();

//        currSpecialCellFactor = -10;

        (tableRow.getChildAt(column)).setBackgroundColor(currColor + currSpecialCellFactor);
        (tableRow.getChildAt(column)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setScore(++currScore);
                gameTable.removeAllViews();
                BuildTable((currBoardSize < MAX_BOARD_SIZE) ? ++currBoardSize : MAX_BOARD_SIZE);
            }
        });

    }

    private void updateCellFactor() {
        List<ParcelableDataPoint> concentrationPointsList = feedback.getConcentrationPoints(currConcentrationPointsListIndex);
        double average = getAverageConcentration(concentrationPointsList);

        if(average > lastConcentrationAverage && currSpecialCellFactor > MAX_SPECIAL_CELL_FACTOR)
            currSpecialCellFactor -= FACTOR_DELTA_JUMP;
        else if(average < lastConcentrationAverage && currSpecialCellFactor < MIN_SPECIAL_CELL_FACTOR)
            currSpecialCellFactor += FACTOR_DELTA_JUMP;

        currConcentrationPointsListIndex += concentrationPointsList.size();
        lastConcentrationAverage = average;

        Log.d("CCUBE_ACTIVITY", Double.toString(average));
        Log.d("CCUBE_ACTIVITY", Integer.toString(currSpecialCellFactor));
    }

    private double getAverageConcentration(List<ParcelableDataPoint> concentrationPointsList) {
        //TODO fix 0 bug
        double average = 0;

        for(ParcelableDataPoint point : concentrationPointsList){
            average += point.getY();
        }

        average = average / concentrationPointsList.size();

        return average;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crazy_cube, menu);
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

    @Override
    public void continueNextScreen() {
        Intent intent = new Intent(this, FeedbackActivity.class);

        intent.putParcelableArrayListExtra(FeedbackActivity.CURR_GAME_CONCENTRATION_POINTS, feedback.getConcentrationPoints());
        intent.putExtra(FeedbackActivity.CURR_GAME_TIME_SECONDS, feedback.getSessionTimeInSeconds());
        intent.putExtra(FeedbackActivity.CURR_GAME_TIME_MINUTES, feedback.getSessionTimeInMinutes());
        startActivity(intent);
    }

    @Override
    public void backKeyPressed() {
        Utils.startNewActivity(this, MenuActivity.class);
    }
}
