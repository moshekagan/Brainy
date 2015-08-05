package com.example.first.kaganmoshe.brainy.CustomActivity;

import com.example.first.kaganmoshe.brainy.MenuActivity;
import com.example.first.kaganmoshe.brainy.Utils;

/**
 * Created by tamirkash on 8/5/15.
 */
public abstract class GameConfigActivity extends AppActivity {


    @Override
    public final void onBackPressed(){
        Utils.startNewActivity(this, MenuActivity.class);
    }
}
