package com.example.first.kaganmoshe.brainy.GuessTheNumber;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.first.kaganmoshe.brainy.CustomActivity.CustomActivity;
import com.example.first.kaganmoshe.brainy.Feedback.FeedbackActivity;
import com.example.first.kaganmoshe.brainy.Feedback.FeedbackClass;
import com.example.first.kaganmoshe.brainy.GraphFragment;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Utils;

import EEG.EConnectionState;
import EEG.ESignalVolume;
import EEG.EegHeadSet;
import EEG.IHeadSetData;
import Utils.Logs;


public class GuessTheNumberGameActivity extends CustomActivity implements IHeadSetData, WinnerDialogFragment.gameCommunicator {

    private static final int MAX_INPUT_DIGITS = 3;
    private final String GUESS_THE_NUMBER_GAME_ACTIVITY = "GuessTheNumberGameActivity";
    private TextSwitcher outputText;
    private TextView inputText;
    private TextView headLineText;
    private TextView guessRequestText;
    private Button approveGuessButton;
    private Button backspaceButton;
    private GuessTheNumberEngine game;
    private MediaPlayer buttonClickSound;
    private MediaPlayer wrongAnswerSound;
    private android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
    private FeedbackClass feedback;


    // Activity components
//    private ImageView m_ConnectivityIconImageV;
    private TextView m_AttentionTextV;
    private TextView m_MeditationTextV;
//    private Button m_ConnectBtn;
    private GraphFragment graphFragment;

    // Data Members
    private EegHeadSet m_HeadSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_the_number_game);

        guessRequestText = (TextView) findViewById(R.id.guessNumberRequest);
        outputText = (TextSwitcher) findViewById(R.id.outputText);
        headLineText = (TextView) findViewById(R.id.guessNumberTitle);
        approveGuessButton = (Button) findViewById(R.id.approveGuessButton);
        backspaceButton = (Button) findViewById(R.id.backspaceButton);
        inputText = (TextView) findViewById(R.id.guessInput);

        buttonClickSound = MediaPlayer.create(this, R.raw.button_click_sound);
        wrongAnswerSound = MediaPlayer.create(this, R.raw.wrong_sound2);

        feedback = new GTNFeedback();
        feedback.startTimer();

        graphFragment = (GraphFragment) fm.findFragmentById(R.id.fragment);

        initTextLines();

        if (savedInstanceState == null) {
            initialize();
        } else {

        }

        initializationActivity();
    }

    private void initTextLines() {
        outputText.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(GuessTheNumberGameActivity.this);
                textView.setTextAppearance(GuessTheNumberGameActivity.this, R.style.gameOutputText);
                Utils.changeFont(getAssets(), textView);
                return textView;
            }
        });

        outputText.setInAnimation(this, android.R.anim.fade_in);
        outputText.setOutAnimation(this, android.R.anim.fade_out);

        //changing title font
        Utils.changeFont(getAssets(), headLineText);
    }

    private void initializationActivity() {
//        m_ConnectivityIconImageV = (ImageView) findViewById(R.id.connectivityImageView);
//        m_ConnectivityIconImageV.setImageResource(R.drawable.bad);
//
//        m_AttentionTextV = (TextView) findViewById(R.id.attentionTextView);
//        m_MeditationTextV = (TextView) findViewById(R.id.meditationTextView);
//
//        m_ConnectBtn = (Button) findViewById(R.id.connectBtn);
//        m_ConnectBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (!m_HeadSet.IsConnected()) {
//                    m_HeadSet.connect();
//                }
//            }
//        });

        // Get HeadSet - ic_mind_wave_mobile
//        try{
//            m_HeadSet = AppManager.getInstance().getHeadSet();
//            m_HeadSet.registerListener(this);
//            Logs.info(GUESS_THE_NUMBER_GAME_ACTIVITY, Logs.SEPARATOR_LINE + "Just created MindWave HeadSet" + Logs.SEPARATOR_LINE);
//        } catch (Exception e){
//            // TODO - Not need to go hear never!!!!
//        }
    }

    private void initialize() {
        Intent intent = getIntent();
        game = new GuessTheNumberEngine(Integer.parseInt(intent.getStringExtra(GuessTheNumberConfigActivity.EXTRA_MESSAGE)));
        guessRequestText.append(" " + Integer.toString(game.getMaxValue()));
    }

    private void checkGuess() {
        try {
            GuessTheNumberEngine.GuessResult result = game.checkGuess(Integer.parseInt(inputText.getText().toString()));

            switch (result) {
                case GOOD:
                    feedback.stopTimer();
                    graphFragment.stopRecievingData();
                    showWinnerDialog();
                    break;
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

            if (result != GuessTheNumberEngine.GuessResult.GOOD) {
                wrongAnswerSound.start();
                inputText.setText("");
            }
        } catch (NumberFormatException ex) {
            outputText.setText("Invalid input");
            wrongAnswerSound.start();
        }
    }

    private void showWinnerDialog() {
        WinnerDialogFragment winnerDialogFragment = new WinnerDialogFragment();
        winnerDialogFragment.setGameScreen(this);
        winnerDialogFragment.show(fm, "WinnerDialogFragment");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
    public void onAttentionReceived(int attValue) {
        Logs.info(GUESS_THE_NUMBER_GAME_ACTIVITY, "Got Attention! " + EegHeadSet.ATTENTION_STR + ": " + attValue);

        if (attValue != Integer.parseInt(m_AttentionTextV.getText().toString())) {
            final String newAttValue = Integer.toString(attValue);
            final int att = attValue;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    m_AttentionTextV.setText(newAttValue);
                }
            });
        }
    }

    @Override
    public void onMeditationReceived(int medValue) {
        Logs.info(GUESS_THE_NUMBER_GAME_ACTIVITY, "Got Meditation!" + EegHeadSet.MEDITATION_STR + ": " + medValue);

        // TODO - To see if we need it
//        if (medValue != Integer.parseInt(m_AttentionTextV.getText().toString())){
//            final String newMedValue = Integer.toString(medValue);
//
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    m_MeditationTextV.setText(newMedValue);
//                }
//            });
//        }
    }

    @Override
    public void onHeadSetChangedState(String headSetName, EConnectionState connectionState) {
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
    public void continueNextScreen() {
        Intent intent = new Intent(this, FeedbackActivity.class);

        intent.putParcelableArrayListExtra(FeedbackActivity.CURR_GAME_CONCENTRATION_POINTS, feedback.getConcentrationPoints());
        intent.putExtra(FeedbackActivity.CURR_GAME_TIME_SECONDS, feedback.getSessionTimeInSeconds());
        intent.putExtra(FeedbackActivity.CURR_GAME_TIME_MINUTES, feedback.getSessionTimeInMinutes());
        startActivity(intent);
    }

    @Override
    public void backKeyPressed() {
        Utils.startNewActivity(this, GuessTheNumberConfigActivity.class);
    }
}
