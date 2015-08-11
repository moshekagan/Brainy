package com.example.first.kaganmoshe.brainy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;
import android.widget.Toast;

import EEG.IHeadSetData;

/**
 * Created by tamirkash on 5/28/15.
 */
public class Utils {

    public static final String CALLING_CLASS = "CALLING_CLASS";

    public static final String GUESS_NUMBER_TITLE_FONT = "fonts/Kidsn.ttf";
    //TODO - make this generic

    public static void changeFont(AssetManager am, TextView view){
        Typeface tf = Typeface.createFromAsset(am, GUESS_NUMBER_TITLE_FONT);
        view.setTypeface(tf);
    }

    public static void startNewActivity(Activity activity, Class cls){
        Intent intent = new Intent(activity.getApplicationContext(), cls);
        startNewActivity(activity, intent);
    }

    public static void startNewActivity(Activity activity, Intent intent){
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("CALLING_CLASS", activity.getClass().getCanonicalName());
//        intent.setClass(activity, activity.getClass());
        activity.startActivity(intent);
//        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
        activity.finish();
    }

    // Need to call by application like that (For example):
    // Utils.showToastMessage(getApplicationContext(), "Your Message");
    public static void showToastMessage(Context context, String msg){
//        Context context = getApplicationContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
