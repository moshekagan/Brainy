package com.example.first.kaganmoshe.brainy.Games.CrazyCube;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import Utils.AppTimer;

import com.example.first.kaganmoshe.brainy.AppActivities.GameGraph.GameGraph;
import com.example.first.kaganmoshe.brainy.AppActivities.GameGraph.GameGraphActivity;
import com.example.first.kaganmoshe.brainy.AppActivities.MainActivity;
import com.example.first.kaganmoshe.brainy.Feedback.FeedbackActivity;
import com.example.first.kaganmoshe.brainy.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Random;

public class CrazyCubeActivity extends GameGraphActivity implements AppTimer.IAppTimerListener {

    private TableLayout mGameTableLayout;
    private Context mContext;
    private TextView mScoreTextView;
    private TextView mTimeTextView;
    private Button mCurrSpecialCell = null;
    private TextView mBadChoiceLeftTextView;
    private SoundPool mSoundPool;

    private Random mRandom = new Random();
    private int mCurrBoardSize = INIT_BOARD_SIZE - 1;
    private int mCurrColor;
    private int mCurrSpecialCellFactor = MAX_SPECIAL_CELL_FACTOR - 10;
    private int mCurrTime = TIME_FOR_GAME;
    private int mCurrScore = 0;
    private int mBadChoicesLeft = BAD_CHOICES_SIZE;
    private double mLastConcentrationAverage = 0;
    private int mCurrConcentrationListIndex = 0;
    private ArrayList<DataPoint> mConcentrationList = new ArrayList<>();
    private int mGoodSoundId;
    private int mBadSoundId;
    private AppTimer mTimer = new AppTimer(TIME_FOR_GAME, AppTimer.ETimeStringFormat.MINUTES_AND_SECONDS);

    private static final int MIN_SPECIAL_CELL_FACTOR = -5;
    private static final int MAX_SPECIAL_CELL_FACTOR = -20;
    private static final int FACTOR_DELTA_JUMP = 5;
    private static final int INIT_BOARD_SIZE = 2;
    private static final int MAX_BOARD_SIZE = 8;
    private static final int TIME_FOR_GAME = 60;
    private static final int BAD_CHOICES_SIZE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crazy_cube);

        mGameTableLayout = (TableLayout) findViewById(R.id.table);
        mScoreTextView = (TextView) findViewById(R.id.CCubeScoreTextView);
        mTimeTextView = (TextView) findViewById(R.id.CCubeTimeTextView);
        mBadChoiceLeftTextView = (TextView) findViewById(R.id.CCubeBadChoiceLeftTextView);
        mContext = getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        } else {
            createOldSoundPool();
        }

        mGoodSoundId = mSoundPool.load(this, R.raw.bonus_sound, 1);
        mBadSoundId = mSoundPool.load(this, R.raw.wrong_sound2, 1);

        gameGraph = new GameGraph((GraphView) findViewById(R.id.graph), this);
        setScoreView();
        setBadChoicesLeftView();

        mTimer.registerListener(this);
        mTimeTextView.setText(mTimer.toString());
        startFeedbackSession();
        BuildTable(++mCurrBoardSize);
    }

    @Override
    protected String setFinishDialogTitle() {
        return getResources().getString(R.string.crazy_cube_finish_title);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void createOldSoundPool() {
        mSoundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .build();
    }

    private void createNewSoundPool() {
        mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
    }

    private void stopClock() {
        mTimer.stopTimer();
    }

    private void resumeClock() {
        mTimer.resumeTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopClock();
        hideSpecialCell();
    }

    private void setScoreView() {
        mScoreTextView.setText(Integer.toString(mCurrScore));
    }

    public void pickColor() {
        int red = mRandom.nextInt(256);
        int green = mRandom.nextInt(256);
        int blue = mRandom.nextInt(256);

        red = (red + 150) / 2;
        green = (green + 150) / 2;
        blue = (blue + 150) / 2;

        mCurrColor = Color.rgb(red, green, blue);
    }

    private void BuildTable(int n) {

        pickColor();

        for (int i = 0; i < n; i++) {
            TableRow row = new TableRow(mContext);
            row.setWeightSum(n);

            TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0,
                    mGameTableLayout.getWeightSum() / n);
            tableLayoutParams.setMargins(1, 2, 1, 2);
            mGameTableLayout.addView(row, i, tableLayoutParams);

            for (int j = 0; j < n; j++) {
                Button cell = (Button) LayoutInflater.from(mContext).inflate(R.layout.ccube_table_button, null);
                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCellClicked(v);
                    }
                });

                cell.setBackgroundColor(mCurrColor);
                TableRow.LayoutParams tableColLayoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1);
                tableColLayoutParams.setMargins(2, 0, 2, 0);
                row.addView(cell, j, tableColLayoutParams);
            }
        }

        setSpecialCell();
    }

    private void onCellClicked(View cell) {
        ColorDrawable backgroundColor = (ColorDrawable) cell.getBackground();

        if (backgroundColor.getColor() == mCurrColor) {
            wrongCellClicked();
        } else {
            specialCellClicked();
        }
    }

    private void specialCellClicked() {
        playGoodSound();
        ++mCurrScore;
        setScoreView();
        mGameTableLayout.removeAllViews();
        BuildTable((mCurrBoardSize < MAX_BOARD_SIZE) ? ++mCurrBoardSize : MAX_BOARD_SIZE);
    }

    private void playGoodSound() {
        mSoundPool.play(mGoodSoundId, 1, 1, 1, 0, 1);
    }

    private void wrongCellClicked() {
        playBadSound();

        if (mBadChoicesLeft > 0) {
            --mBadChoicesLeft;
            setBadChoicesLeftView();
        } else if (mCurrScore > 0) {
            --mCurrScore;
            setScoreView();
        }
    }

    private void setBadChoicesLeftView() {
        mBadChoiceLeftTextView.setText(Integer.toString(mBadChoicesLeft));
    }

    private void playBadSound() {
        mSoundPool.play(mBadSoundId, 1, 1, 1, 0, 1);
    }

    private void setSpecialCell() {

        int row = mRandom.nextInt(mCurrBoardSize);
        int column = mRandom.nextInt(mCurrBoardSize);

        TableRow tableRow = (TableRow) mGameTableLayout.getChildAt(row);

        if (mCurrSpecialCellFactor < MAX_SPECIAL_CELL_FACTOR)
            mCurrSpecialCellFactor += FACTOR_DELTA_JUMP;
        else
            updateCellFactor();

        mCurrSpecialCell = (Button) tableRow.getChildAt(column);
        mCurrSpecialCell.setBackgroundColor(mCurrColor + mCurrSpecialCellFactor);
    }

    private void hideSpecialCell() {
        mCurrSpecialCell.setBackgroundColor(mCurrColor);
    }

    private void showSpecialCell() {
        mCurrSpecialCell.setBackgroundColor(mCurrColor + mCurrSpecialCellFactor);
    }

    private void updateCellFactor() {
        double average = getAverageConcentration();

        if (average > mLastConcentrationAverage && mCurrSpecialCellFactor > MAX_SPECIAL_CELL_FACTOR)
            mCurrSpecialCellFactor -= FACTOR_DELTA_JUMP;
        else if (average < mLastConcentrationAverage && mCurrSpecialCellFactor < MIN_SPECIAL_CELL_FACTOR)
            mCurrSpecialCellFactor += FACTOR_DELTA_JUMP;

        mLastConcentrationAverage = average;
    }

    private double getAverageConcentration() {
        double sum = 0;
        int size = mConcentrationList.size() - mCurrConcentrationListIndex;

        for (; mCurrConcentrationListIndex < size; mCurrConcentrationListIndex++) {
            sum += mConcentrationList.get(mCurrConcentrationListIndex).getY();
        }

        return (size > 0) ? (sum / size) : mLastConcentrationAverage;
    }

    @Override
    protected void startFeedbackSession() {
        mFeedback = new CCubeFeedback();
//        mFeedback.startTimer();
    }

    @Override
    public void onAttentionReceived(final int attValue) {
        mConcentrationList.add(new DataPoint(mConcentrationList.size(), attValue));
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
        super.onMenuPopupShow();
        stopClock();
    }

    @Override
    public void onGameResumed() {
        super.onGameResumed();
        resumeClock();
        showSpecialCell();
    }

    // If you dont override this, the default is to go to the mFeedback activity
    @Override
    public void onFinishGameContinueClicked() {
        //this is the format to add your own stat
//        addNewStatForFeedback("Bonus", "100");
        //then you call this method
//        continueToNextActivity(FeedbackActivity.class);
        super.onFinishGameContinueClicked();
    }

    @Override
    protected void addTotalTimeSessionFeedbackStat(Intent intent) {
        intent.putExtra(FeedbackActivity.TOTAL_TIME, "01:00");
    }

    //home button icon on the actionbar was clicked
    @Override
    public void homeMenuButtonClicked() {
        super.homeMenuButtonClicked();
        hideSpecialCell();
    }

    @Override
    public void onTimeTick(String timeString) {
        mTimeTextView.setText(timeString);
    }

    @Override
    public void onTimeFinish(String timeString) {
        finishGame();
    }

    private void finishGame() {
        ((CCubeFeedback) mFeedback).calculateFinalScore(mCurrScore, mBadChoicesLeft);
        showFinishDialog();
    }

    @Override
    public String toString() {
        return MainActivity.CRAZY_CUBE_STR;
    }

    @Override
    protected String setContentForHelpDialog() {
        return getResources().getString(R.string.crazy_cube_help_content);
    }
}
