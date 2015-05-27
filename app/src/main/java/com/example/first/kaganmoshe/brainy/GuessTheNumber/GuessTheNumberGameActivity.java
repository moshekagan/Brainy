package com.example.first.kaganmoshe.brainy.GuessTheNumber;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.first.kaganmoshe.brainy.R;

import EEG.EConnectionState;
import EEG.EHeadSetType;
import EEG.ESignalVolume;
import EEG.EegHeadSet;
import EEG.IHeadSetData;
import EEG.MindWave;
import Utils.Logs;


public class GuessTheNumberGameActivity extends FragmentActivity implements IHeadSetData {

    private static final int MAX_INPUT_DIGITS = 3;
    private final String GUESS_THE_NUMBER_GAME_ACTIVITY = "GuessTheNumberGameActivity";
    private TextView outputText;
    private TextView inputText;
    private TextView headLineText;
    private TextView guessRequestText;
    private Button approveGuessButton;
    private Button backspaceButton;
    private GuessTheNumberEngine game;
    private android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

    // Activity components
//    private ImageView m_ConnectivityIconImageV;
    private TextView m_AttentionTextV;
    private TextView m_MeditationTextV;
//    private Button m_ConnectBtn;

    // Data Members
    private EegHeadSet m_HeadSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_the_number_game);

        guessRequestText = (TextView) findViewById(R.id.guessNumberRequest);
        outputText = (TextView) findViewById(R.id.outputText);
        headLineText = (TextView) findViewById(R.id.guessNumberTitle);
        approveGuessButton = (Button) findViewById(R.id.approveGuessButton);
        backspaceButton = (Button) findViewById(R.id.backspaceButton);
        inputText = (TextView) findViewById(R.id.guessInput);

        //changing title font
        String fontPath = "fonts/Kidsn.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        headLineText.setTypeface(tf);

        if (savedInstanceState == null) {
            initialize();
        } else {

        }

        initializationActivity();
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
//            m_HeadSet = MindWave.getInstance(EHeadSetType.MindWave);
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

            switch(result){
                case GOOD:
                    showWinnerDialog();
                    break;
                case TOO_HIGH:
                    outputText.setText("Try a lower number..");
                    break;
                case NOT_IN_RANGE:
                    outputText.setText("Your number is not in range..");
                    break;
                case TOO_LOW:
                    outputText.setText("Try a higher number..");
                    break;
            }

            if(result != GuessTheNumberEngine.GuessResult.GOOD){
                inputText.setText("");
            }
        } catch (NumberFormatException ex) {
            outputText.setText("Invalid input");
        }
    }

    private void showWinnerDialog() {
        WinnerDialogFragment winnerDialogFragment = new WinnerDialogFragment();
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
        } else{
            if (view.getId() == backspaceButton.getId()) {
                removeLastLetterFromInput();
            } else {
                addLetterToInput(((Button)view).getText().toString());
            }

            outputText.setText("");
        }
    }

    private void removeLastLetterFromInput() {
        //int inputLength = inputText.length();

        if(inputText.length() > 0){
            CharSequence newText = inputText.getText().subSequence(0, inputText.length() - 1);
            inputText.setText(newText);
        }
    }

    private void addLetterToInput(String text) {
        if(inputText.length() < MAX_INPUT_DIGITS){
            inputText.append(text);
        }
    }

    @Override
    public void onAttentionReceived(int attValue) {
        Logs.info(GUESS_THE_NUMBER_GAME_ACTIVITY, "Got Attention! " + EegHeadSet.ATTENTION_STR + ": " + attValue);

        if (attValue != Integer.parseInt(m_AttentionTextV.getText().toString())){
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

        switch (connectionState){
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
}
