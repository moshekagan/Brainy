package Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.example.first.kaganmoshe.brainy.R;

/**
 * Created by tamirkash on 9/4/15.
 */
public class AppCheckBox extends CheckBox {

    public AppCheckBox(Context context) {
        super(context);
        CustomFontHelper.setAppFont(this, context);
    }

    public AppCheckBox(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        CustomFontHelper.setAppFont(this, arg0);
    }

    public AppCheckBox(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
        CustomFontHelper.setAppFont(this, arg0);
    }

    public int getDayOfWeek(){
        String sunday = getResources().getString(R.string.Sunday);
        String monday = getResources().getString(R.string.Monday);
        String tuesday = getResources().getString(R.string.Tuesday);
        String wednesday = getResources().getString(R.string.Wednesday);
        String thursday = getResources().getString(R.string.Thursday);
        String friday = getResources().getString(R.string.Friday);
        String thisDay = getText().toString();

        if(thisDay.equals(sunday)){
            return 1;
        } else if(thisDay.equals(monday)){
            return 2;
        } else if(thisDay.equals(tuesday)){
            return 3;
        } else if(thisDay.equals(wednesday)){
            return 4;
        } else if(thisDay.equals(thursday)){
            return 5;
        } else if(thisDay.equals(friday)){
            return 6;
        } else{
            return 7;
        }
    }
}
