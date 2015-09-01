package Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by tamirkash on 9/1/15.
 */
public class AppButton extends Button {

    public AppButton(Context context) {
        super(context);
    }

    public AppButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setAppFont(this, context);
    }

    public AppButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        CustomFontHelper.setAppFont(this, context);
    }
}
