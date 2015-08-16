package com.example.first.kaganmoshe.brainy.HotAirBallon;

import android.content.Intent;
import android.os.Bundle;

import com.example.first.kaganmoshe.brainy.CustomActivity.GameConfigActivity;
import com.example.first.kaganmoshe.brainy.R;
import com.example.first.kaganmoshe.brainy.Utils;

public class HABConfigActivity extends GameConfigActivity {

    // Const Members
    public static final String TIME_TO_PLAY = "Time To Play";
    public static final long ONE_MIN = 60000L;

    // Data Members
//    private Button m_StartGameBtn;
    private long timeToPlayInMilSec = ONE_MIN;

    // Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habconfig);

//        m_StartGameBtn = (Button) findViewById(R.id.startGameButton);

//        setTouchNClick(R.id.startGameButton);
//        m_StartGameBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onStartGameClick();
//            }
//        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        setTitle("Hot Air Balloon");
    }

    @Override
    protected void onStartGameClick() {
        Intent intent = new Intent(getApplicationContext(), HotAirBalloonGameActivity.class);
        intent.putExtra(TIME_TO_PLAY, timeToPlayInMilSec);
        Utils.startNewActivity(this, intent);
    }
}
