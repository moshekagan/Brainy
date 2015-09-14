package Utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.R;

/**
 * Created by tamirkash on 8/26/15.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] items;
    private final int layoutResource;

    public SpinnerAdapter(Activity context, String[] items, int layoutResource) {
        super(context, layoutResource);
        this.context = context;
        this.items = items;
        this.layoutResource = layoutResource;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(layoutResource, null, true);
        } else {
        /* Fetch data already in the row layout,
         *    primarily you only use this to get a copy of the ViewHolder */
        }

        return view;
    }
}
