package com.example.first.kaganmoshe.brainy.GuessTheNumber;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by tamirkash on 5/28/15.
 */
public class Utils {

    public static String GUESS_NUMBER_TITLE_FONT = "fonts/Kidsn.ttf";

    public static void changeFont(AssetManager am, TextView view){
        Typeface tf = Typeface.createFromAsset(am, GUESS_NUMBER_TITLE_FONT);
        view.setTypeface(tf);
    }
}
