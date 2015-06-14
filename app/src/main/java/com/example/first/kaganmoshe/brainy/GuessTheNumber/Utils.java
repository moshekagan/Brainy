package com.example.first.kaganmoshe.brainy.GuessTheNumber;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.MenuActivity;

/**
 * Created by tamirkash on 5/28/15.
 */
public class Utils {

    public static String GUESS_NUMBER_TITLE_FONT = "fonts/Kidsn.ttf";

    public static void changeFont(AssetManager am, TextView view){
        Typeface tf = Typeface.createFromAsset(am, GUESS_NUMBER_TITLE_FONT);
        view.setTypeface(tf);
    }

    public static void onBackKeyPressed(Activity activity){
        Intent intent = new Intent(activity, MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }
}
