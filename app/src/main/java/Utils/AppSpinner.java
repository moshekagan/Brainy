package Utils;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by tamirkash on 9/1/15.
 */
public class AppSpinner extends com.weiwangcn.betterspinner.library.BetterSpinner {
    public AppSpinner(Context context) {
        super(context);
        CustomFontHelper.setAppFont(this, context);
    }

    public AppSpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        CustomFontHelper.setAppFont(this, arg0);
    }

    public AppSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
        CustomFontHelper.setAppFont(this, arg0);
    }
}
