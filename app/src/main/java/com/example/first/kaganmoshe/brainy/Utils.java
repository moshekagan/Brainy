package com.example.first.kaganmoshe.brainy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import android.content.res.AssetManager;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.Size;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import java.security.cert.PolicyNode;

import Utils.Logs;

import EEG.IHeadSetData;

/**
 * Created by tamirkash on 5/28/15.
 */
public class Utils {
    public static final String UTILS = "Utils";
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
        intent.putExtra(CALLING_CLASS, activity.getClass().getCanonicalName());
        activity.startActivity(intent);
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

    // Return point like that:
    // p.x = activity width
    // p.y = activity height
    static public Point getActivityScreenSize(Activity activity){
        int width = activity.getResources().getDisplayMetrics().widthPixels;
        int height = activity.getResources().getDisplayMetrics().heightPixels;

        Point p = new Point(width, height);
        Logs.error(UTILS, "Screen Size - " + "Width: " + p.x + " Height: " + p.y);
        return p;
    }
}