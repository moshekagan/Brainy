package com.example.first.kaganmoshe.brainy.GuessTheNumber;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.CustomActivity.GameConfigActivity;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Utils;


public class GuessTheNumberConfigActivity extends GameConfigActivity {
    private static final String CURRENT_RANGE_VALUE = "CURRENT_RANGE_VALUE";
    public final static String EXTRA_MESSAGE = "MESSAGE";
    private int currentRangeValue;
    private TextView rangeValueText;
    private SeekBar rangeSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_the_number_config);

        if (savedInstanceState == null) {
            currentRangeValue = GuessTheNumberLogic.RANGE_DIFFERENCE;
        } else {
            currentRangeValue = savedInstanceState.getInt(CURRENT_RANGE_VALUE);
        }

        rangeValueText = (TextView) findViewById(R.id.rangeValueText);
        rangeSeekBar = (SeekBar) findViewById(R.id.rangeSeekBar);
        rangeSeekBar.setMax(GuessTheNumberLogic.NUM_OF_RANGES - 1);
//        startButton = (Button) findViewById(R.id.startButton);
//        setTouchNClick(R.id.startButton);
//        this.setOnBackPressedActivity(MenuActivity.class);

        rangeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = (rangeSeekBar.getProgress() + 1) * GuessTheNumberLogic.RANGE_DIFFERENCE;

                rangeValueText.setText(Integer.toString(value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

//        startButton.setOnClickListener(new Button.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                onStartGameClick();
//            }
//        });
    }

    //    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.i("START", "START");
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.i("RESTART", "RESTART");
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.i("RESUME", "RESUME");
//    }
    @Override
    protected void onStartGameClick() {
        Intent intent = new Intent(getApplicationContext(), GuessTheNumberGameActivity.class);
        String rangeValue = rangeValueText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, rangeValue);
        Utils.startNewActivity(this, intent);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_RANGE_VALUE, currentRangeValue);
    }

//    @Override
//    protected void onStart(){
//        super.onStart();
//        setTitle("Guess the Number");
//    }
//    @Override
//    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.i("Pause","Pause");
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.i("STOP", "STOP");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.i("DESTROY", "DESTROY");
//    }
}
