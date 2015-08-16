package com.example.first.kaganmoshe.brainy.CustomActivity;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by tamirkash on 8/16/15.
 */
public class AppTextView extends TextView {
    public static final String APP_FONT = "fonts/Kidsn.ttf";
    private static Typeface tf;

    public AppTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if(tf == null){
            tf = Typeface.createFromAsset(context.getAssets(), APP_FONT);
        }

        init();
    }

    public AppTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(tf == null){
            tf = Typeface.createFromAsset(context.getAssets(), APP_FONT);
        }

        init();
    }

    public AppTextView(Context context) {
        super(context);

        if(tf == null){
            tf = Typeface.createFromAsset(context.getAssets(), APP_FONT);
        }

        init();
    }

    private void init() {
        if (!isInEditMode()) {
//            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), APP_FONT);
            setTypeface(tf);
        }
    }

    public static Typeface getAppFontTypeface(){
        return tf;
    }
}
