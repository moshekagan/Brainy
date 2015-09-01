package Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.R;

/**
 * Created by tamirkash on 9/1/15.
 */
public class CustomFontHelper {
    public static final String APP_FONT = "fonts/Kidsn.ttf";

    /**
     * Sets a font on a textview based on the custom com.my.package:font attribute
     * If the custom font attribute isn't found in the attributes nothing happens
     */
    public static void setCustomFont(TextView textview, Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomFont);
        String font = a.getString(R.styleable.CustomFont_font);
        setCustomFont(textview, font, context);
        a.recycle();
    }

    /**
     * Sets a font on a textview
     */
    public static void setCustomFont(TextView textview, String font, Context context) {
        if(font == null) {
            return;
        }
        Typeface tf = FontCache.get(font, context);
        if(tf != null) {
            textview.setTypeface(tf);
        }
    }

    public static void setAppFont(TextView textView, Context context){
        Typeface tf = FontCache.get(APP_FONT, context);
        if(tf != null) {
            textView.setTypeface(tf);
        }
    }



}
