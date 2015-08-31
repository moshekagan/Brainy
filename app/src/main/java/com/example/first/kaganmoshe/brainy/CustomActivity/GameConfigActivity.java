package com.example.first.kaganmoshe.brainy.CustomActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.AppActivities.ActionBarActivity.ActionBarAppActivity;
import com.example.first.kaganmoshe.brainy.AppActivities.MainActivity;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Utils;


/**
 * Created by tamirkash on 8/5/15.
 */
public abstract class GameConfigActivity extends ActionBarAppActivity {

    protected TextView configTitle;
    protected SeekBar sessionsRangeSeekBar;
    protected TextView sessionsRangeValueTextView;
    protected Button m_StartGameButton;

    @Override
    public final void onBackPressed() {
        if (!mHomeButtonPopup.isShowing()) {
            Utils.startNewActivity(this, MainActivity.class);
        } else {
            mHomeButtonPopup.dismiss();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        sessionsRangeSeekBar = (SeekBar) findViewById(R.id.sessionsRangeSeekBar);
        sessionsRangeValueTextView = (TextView) findViewById(R.id.numberOfSessionsChoice);
        m_StartGameButton = (Button) findViewById(R.id.startGameButton);
        configTitle = (TextView) findViewById(R.id.configTitle);

        setTouchNClick(R.id.startGameButton);

        sessionsRangeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int value = sessionsRangeSeekBar.getProgress() + 1;

                sessionsRangeValueTextView.setText(Integer.toString(value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        m_StartGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartGameClick();
            }
        });
    }

    abstract protected void onStartGameClick();

    protected void setTitle(String title) {
        configTitle.append(title);
//        Utils.changeFont(getAssets(), configTitle);
    }

}
