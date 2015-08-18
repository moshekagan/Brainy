package com.example.first.kaganmoshe.brainy.GuessTheNumber;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.first.kaganmoshe.brainy.CustomActivity.AppTextView;
import com.example.first.kaganmoshe.brainy.CustomActivity.GameGraph;
import com.example.first.kaganmoshe.brainy.CustomActivity.GameGraphActivity;
import com.example.first.kaganmoshe.brainy.R;
import com.jjoe64.graphview.GraphView;

import EEG.EConnectionState;
import EEG.ESignalVolume;
import EEG.EegHeadSet;
import Utils.Logs;


public class GuessTheNumberGameActivity extends GameGraphActivity {

    private static final int MAX_INPUT_DIGITS = 3;
    private final String GUESS_THE_NUMBER_GAME_ACTIVITY = "GuessTheNumberGameActivity";
    private TextSwitcher outputText;
    private TextView inputText;
    private TextView guessRequestText;
    private Button approveGuessButton;
    private Button backspaceButton;
    private GuessTheNumberLogic game;
    private MediaPlayer buttonClickSound;
    private MediaPlayer wrongAnswerSound;
//    private boolean isButtonClickSoundReady = false;
//    private boolean isWrongAnswerSoundReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_the_number_game);

        guessRequestText = (TextView) findViewById(R.id.guessNumberRequest);
        outputText = (TextSwitcher) findViewById(R.id.outputText);
        approveGuessButton = (Button) findViewById(R.id.approveGuessButton);
        backspaceButton = (Button) findViewById(R.id.backspaceButton);
        inputText = (TextView) findViewById(R.id.guessInput);

        if (buttonClickSound == null && wrongAnswerSound == null) {
            buttonClickSound = MediaPlayer.create(this, R.raw.button_click_sound);
            wrongAnswerSound = MediaPlayer.create(this, R.raw.wrong_sound2);
        }

        gameGraph = new GameGraph((GraphView) findViewById(R.id.graph), this);

        initTextLines();

        if (savedInstanceState == null) {
            initialize();
        } else {

        }

        startFeedbackSession();
    }

//    @Override
//    protected int calculateScore() {
//        int minAttemptsToWin ;
//        return 100;
//    }

    private void initTextLines() {
        outputText.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(GuessTheNumberGameActivity.this);
                textView.setTextAppearance(GuessTheNumberGameActivity.this, R.style.gameOutputText);
                textView.setTypeface(AppTextView.getAppFontTypeface());
                return textView;
            }
        });

        outputText.setInAnimation(this, android.R.anim.fade_in);
        outputText.setOutAnimation(this, android.R.anim.fade_out);
    }

    private void initialize() {
        Intent intent = getIntent();
        Log.d("GTN", "CREATE_LOGIC");
        game = new GuessTheNumberLogic(Integer.parseInt(intent.getStringExtra(GuessTheNumberConfigActivity.EXTRA_MESSAGE)));
        Log.d("GTN", "FINISH CREATE LOGIC");
        guessRequestText.append(" " + Integer.toString(game.getMaxValue()));
    }

    private void checkGuess() {
        try {
            GuessTheNumberLogic.GuessResult result = game.checkGuess(Integer.parseInt(inputText.getText().toString()));

            switch (result) {
                case GOOD:
                    showFinishDialog();
                    return;
                case TOO_HIGH:
                    outputText.setText("Try a lower number..");
                    break;
                case NOT_IN_RANGE:
                    outputText.setText("Number not in range..");
                    break;
                case TOO_LOW:
                    outputText.setText("Try a higher number..");
                    break;
            }

            wrongAnswerSound.start();
            inputText.setText("");
        } catch (NumberFormatException ex) {
            outputText.setText("Invalid input");
            wrongAnswerSound.start();
        }
    }

    public void gameButtonClicked(View view) {
        if (view.getId() == approveGuessButton.getId()) {
            checkGuess();
        } else {
            buttonClickSound.start();

            if (view.getId() == backspaceButton.getId()) {
                removeLastLetterFromInput();
            } else {
                addLetterToInput(((Button) view).getText().toString());
            }

            outputText.setText("");
        }
    }

    private void removeLastLetterFromInput() {
        if (inputText.length() > 0) {
            CharSequence newText = inputText.getText().subSequence(0, inputText.length() - 1);
            inputText.setText(newText);
        }
    }

    private void addLetterToInput(String text) {
        if (inputText.length() < MAX_INPUT_DIGITS) {
            inputText.append(text);
        }
    }

    @Override
    public void onMeditationReceived(int medValue) {
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
        Log.d("GTN", "GETTING FEEDBACK");
        feedback = game.getFeedback();
        Log.d("GTN", "FINISH SET FEEDBACK");
        Log.d("GTN", "STARTING FEEDBACK TIMER");
        feedback.startTimer();
        Log.d("GTN", "STARTED FEEDBACK TIMER");
    }

    @Override
    protected void onMenuPopupShow() {
        super.onMenuPopupShow();
    }
}
