package com.example.first.kaganmoshe.brainy.CustomActivity;

import android.widget.Button;

import com.example.first.kaganmoshe.brainy.MenuActivity;
import com.example.first.kaganmoshe.brainy.Utils;

/**
 * Created by tamirkash on 8/5/15.
 */
public abstract class GameConfigActivity extends AppActivity {

    protected Button m_StartGameButton;
    @Override
    public final void onBackPressed(){
        Utils.startNewActivity(this, MenuActivity.class);
    }
}
