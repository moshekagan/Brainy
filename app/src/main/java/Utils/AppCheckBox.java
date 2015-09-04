package Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

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
}
