package com.example.first.kaganmoshe.brainy.Games.GuessTheNumber;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import Utils.AppStopWatch;
import Utils.AppTextView;
import Utils.AppTime;
import com.example.first.kaganmoshe.brainy.AppActivities.GameGraph.GameGraph;
import com.example.first.kaganmoshe.brainy.AppActivities.GameGraph.GameGraphActivity;
import com.example.first.kaganmoshe.brainy.AppActivities.MainActivity;
import com.example.first.kaganmoshe.brainy.Feedback.FeedbackActivity;
import com.example.first.kaganmoshe.brainy.R;
import com.jjoe64.graphview.GraphView;

import EEG.EConnectionState;
import EEG.ESignalVolume;
import EEG.EegHeadSet;
import Utils.CustomFontHelper;
import Utils.Logs;


public class GuessTheNumberGameActivity extends GameGraphActivity {

    private static final int MAX_INPUT_DIGITS = 3;

    private TextSwitcher mOutputTextView;
    private TextView mInputTextView;
    private TextView mGuessRequestTextView;
    private Button mApproveGuessButton;
    private Button mBackspaceButton;
    private GuessTheNumberLogic mLogic;
    private SoundPool mSoundPool;
    private int mButtonClickSoundId;
    private int mWrongAnswerSoundId;
    private ImageSwitcher mArrowImage;
    protected AppStopWatch mStopWatch = new AppStopWatch(AppTime.ETimeStringFormat.MINUTES_AND_SECONDS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_the_number_game);

        mGuessRequestTextView = (TextView) findViewById(R.id.guessNumberRequest);
        mOutputTextView = (TextSwitcher) findViewById(R.id.outputText);
        mApproveGuessButton = (Button) findViewById(R.id.approveGuessButton);
        mBackspaceButton = (Button) findViewById(R.id.backspaceButton);
        mInputTextView = (TextView) findViewById(R.id.guessInput);
        mArrowImage = (ImageSwitcher) findViewById(R.id.arrowImageView);

//        if (mSoundPool == null && wrongAnswerSound == null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        }else{
            createOldSoundPool();
        }
        mButtonClickSoundId = mSoundPool.load(this, R.raw.button_click_sound, 1);
        mWrongAnswerSoundId = mSoundPool.load(this, R.raw.wrong_sound2, 1);
//            mSoundPool = MediaPlayer.create(this, R.raw.button_click_sound);
//            wrongAnswerSound = MediaPlayer.create(this, R.raw.wrong_sound2);
//        }

        gameGraph = new GameGraph((GraphView) findViewById(R.id.graph), this);

        initTextLines();

        if (savedInstanceState == null) {
            initialize();
        } else {

        }

        startFeedbackSession();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void createNewSoundPool(){
        mSoundPool = new SoundPool.Builder()
                .setMaxStreams(10)
                .build();
    }
    @SuppressWarnings("deprecation")
    protected void createOldSoundPool(){
        mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mStopWatch.stopTimer();
    }

    @Override
    public void onGameResumed() {
        super.onGameResumed();
        mStopWatch.resumeTimer();
    }

    @Override
    public void onDialogShow(Class thisClass) {
        super.onDialogShow(thisClass);
        mStopWatch.stopTimer();
    }

    @Override
    protected void addTotalTimeSessionFeedbackStat(Intent intent) {
        intent.putExtra(FeedbackActivity.TOTAL_TIME, mStopWatch.toString());
    }

//    @Override
//    protected String getGameName() {
//        return "Guess The Number";
//    }

//    @Override
//    protected int calculateScore() {
//        int minAttemptsToWin ;
//        return 100;
//    }

    private void initTextLines() {
        mOutputTextView.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(GuessTheNumberGameActivity.this);
                textView.setTextAppearance(GuessTheNumberGameActivity.this, R.style.gameOutputText);
//                textView.setTypeface(AppTextView.getAppFontTypeface());
                CustomFontHelper.setAppFont(textView, getApplicationContext());
                return textView;
            }
        });

        mOutputTextView.setInAnimation(getApplicationContext(), android.R.anim.fade_in);
        mOutputTextView.setOutAnimation(getApplicationContext(), android.R.anim.fade_out);

        mArrowImage.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());

                myView.setScaleType(ImageView.ScaleType.FIT_XY);

                FrameLayout.LayoutParams params = new ImageSwitcher.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                myView.setLayoutParams(params);

                return myView;
            }
        });

        mArrowImage.setInAnimation(getApplicationContext(), android.R.anim.fade_in);
        mArrowImage.setOutAnimation(getApplicationContext(), android.R.anim.fade_out);
    }

    private void initialize() {
//        Intent intent = getIntent();
        Log.d("GTN", "CREATE_LOGIC");
//        mLogic = new GuessTheNumberLogic(Integer.parseInt(intent.getStringExtra(GuessTheNumberConfigActivity.EXTRA_MESSAGE)));
        mLogic = new GuessTheNumberLogic(100);
        Log.d("GTN", "FINISH CREATE LOGIC");
        mGuessRequestTextView.append(" " + Integer.toString(mLogic.getMaxValue()));
    }

    private void checkGuess() {
        try {
            GuessTheNumberLogic.GuessResult result = mLogic.checkGuess(Integer.parseInt(mInputTextView.getText().toString()));

            switch (result) {
                case TOO_LOW:
                    mArrowImage.setImageResource(R.drawable.arrow_up2);
                    mOutputTextView.setText("Try a higher number..");
                    break;
                case TOO_HIGH:
                    mArrowImage.setImageResource(R.drawable.arrow_down2);
                    mOutputTextView.setText("Try a lower number..");
                    break;
                case NOT_IN_RANGE:
                    mArrowImage.setImageResource(android.R.color.transparent);
                    mOutputTextView.setText("Number not in range..");
                    break;
                case GOOD:
                    mArrowImage.setImageResource(android.R.color.transparent);
                    showFinishDialog();
                    return;
            }

//            wrongAnswerSound.start();
            mSoundPool.play(mWrongAnswerSoundId, 1, 1, 1, 0, 1);
            mInputTextView.setText("");
        } catch (NumberFormatException ex) {
            mOutputTextView.setText("Invalid input");
//            wrongAnswerSound.start();
            mSoundPool.play(mWrongAnswerSoundId, 1, 1, 1, 0, 1);
        }
    }

    public void gameButtonClicked(View view) {
        if (view.getId() == mApproveGuessButton.getId()) {
            checkGuess();
        } else {
//            mSoundPool.start();
            mSoundPool.play(mButtonClickSoundId, 1, 1, 1, 0, 1);

            if (view.getId() == mBackspaceButton.getId()) {
                removeLastLetterFromInput();
            } else {
                addLetterToInput(((Button) view).getText().toString());
            }

            mOutputTextView.setText("");
            mArrowImage.setImageResource(android.R.color.transparent);
//            mArrowImage.setImageDrawable(null);
        }
    }

    private void removeLastLetterFromInput() {
        if (mInputTextView.length() > 0) {
            CharSequence newText = mInputTextView.getText().subSequence(0, mInputTextView.length() - 1);
            mInputTextView.setText(newText);
        }
    }

    private void addLetterToInput(String text) {
        if (mInputTextView.length() < MAX_INPUT_DIGITS) {
            mInputTextView.append(text);
        }
    }

    @Override
    public void onMeditationReceived(int medValue) {
        String GUESS_THE_NUMBER_GAME_ACTIVITY = "GuessTheNumberGameActivity";
        Logs.info(GUESS_THE_NUMBER_GAME_ACTIVITY, "Got Meditation!" + EegHeadSet.MEDITATION_STR + ": " + medValue);
    }

    @Override
    public void onHeadSetChangedState(String headSetName, EConnectionState connectionState) {
        super.onHeadSetChangedState(headSetName, connectionState);

        String message = "";

        switch (connectionState) {
            case DEVICE_CONNECTED:
                message = "Connection established :)";
                break;
            case DEVICE_CONNECTING:
                message = "Connecting...";
                break;
            case DEVICE_NOT_CONNECTED:
                message = "Device disconnected! :(";
                break;
            case DEVICE_NOT_FOUND:
                message = "Device not found :(";
                break;
            case BLUETOOTH_NOT_AVAILABLE:
                message = "Bluetooth not available. Turn on bluetooth or check paring.";
                break;
        }

        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void onPoorSignalReceived(ESignalVolume signalVolume) {
        final ESignalVolume newSignalVolume = signalVolume;

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                switch (newSignalVolume){
//                    case HEAD_SET_NOT_COVERED:
//                        m_ConnectivityIconImageV.setImageResource(R.drawable.bad);
//                        break;
//                    case POOR_SIGNAL_HIGH:
//                    case POOR_SIGNAL_LOW:
//                        m_ConnectivityIconImageV.setImageResource(R.drawable.medium);
//                        break;
//                    case GOOD_SIGNAL:
//                        m_ConnectivityIconImageV.setImageResource(R.drawable.good);
//                        break;
//                }
//            }
//        });
//
//        if (newSignalVolume == ESignalVolume.HEAD_SET_NOT_COVERED){
//            Context context = getApplicationContext();
//            CharSequence text = "The Head set should be on the head... da!";
//            int duration = Toast.LENGTH_SHORT;
//
//            Toast toast = Toast.makeText(context, text, duration);
//            toast.show();
//        }
    }

    @Override
    protected void startFeedbackSession() {
        mFeedback = mLogic.getFeedback();
    }

    @Override
    protected void onMenuPopupShow() {
        mStopWatch.stopTimer();
        super.onMenuPopupShow();
    }

    @Override
    protected String setContentForHelpDialog() {
        return getResources().getString(R.string.guess_number_help_content);
    }

    @Override
    protected String setFinishDialogTitle() {
        return getResources().getString(R.string.guess_number_finish_title);
    }

    @Override
    public String toString(){
        return MainActivity.GUESS_THE_NUMBER_STR;
    }
}
