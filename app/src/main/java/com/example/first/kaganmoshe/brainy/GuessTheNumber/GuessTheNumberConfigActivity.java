package com.example.first.kaganmoshe.brainy.GuessTheNumber;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.R;


public class GuessTheNumberConfigActivity extends Activity {
    private static final String CURRENT_RANGE_VALUE = "CURRENT_RANGE_VALUE";
    public final static String EXTRA_MESSAGE = "MESSAGE";
    private int currentRangeValue;
    private TextView rangeValueText;
    private SeekBar rangeSeekBar;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess_the_number_config);

        if(savedInstanceState == null){
            currentRangeValue = 100;
        } else{
            currentRangeValue = savedInstanceState.getInt(CURRENT_RANGE_VALUE);
        }

        rangeValueText = (TextView) findViewById(R.id.rangeValueText);
        rangeSeekBar = (SeekBar) findViewById(R.id.rangeSeekBar);
        startButton = (Button) findViewById(R.id.startButton);

        rangeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = (rangeSeekBar.getProgress() + 1) * 100;

                rangeValueText.setText(Integer.toString(value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        startButton.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                onStartGameClick();
            }
        });
    }

    private void onStartGameClick(){
        Intent intent = new Intent(this, GuessTheNumberGameActivity.class);
        String rangeValue = rangeValueText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, rangeValue);
        startActivity(intent);
    }

    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_RANGE_VALUE, currentRangeValue);
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
}