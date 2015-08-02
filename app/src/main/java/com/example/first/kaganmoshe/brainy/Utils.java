package com.example.first.kaganmoshe.brainy;

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
    //TODO - make this generic

    public static void changeFont(AssetManager am, TextView view){
        Typeface tf = Typeface.createFromAsset(am, GUESS_NUMBER_TITLE_FONT);
        view.setTypeface(tf);
    }

    public static void startNewActivity(Activity activity, Class cls){
        Intent intent = new Intent(activity, cls);
        startNewActivity(activity, intent);
    }

    public static void startNewActivity(Activity activity, Intent intent){
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
        activity.finish();
    }
}
