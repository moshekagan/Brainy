package com.example.first.kaganmoshe.brainy;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.first.kaganmoshe.brainy.CustomActivity.*;

/**
 * Created by tamirkash on 7/27/15.
 */
public class MenuCustomList extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] titles;
    private final String[] reviews;
    private final Integer[] imageId;

    public MenuCustomList(Activity context,
                          String[] titles, Integer[] imageId, String[] reviews) {
        super(context, R.layout.menu_list_row, titles);
        this.reviews = reviews;
        this.context = context;
        this.titles = titles;
        this.imageId = imageId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.menu_list_row, null, true);

//            int alpha = 128; //50% transparency
//            int color = Color.WHITE; //Your color value
//            int bgColor = Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
//
//            view.setBackgroundColor(bgColor);
//            Log.d("COLOR", Integer.toString(bgColor));
//            view.setOnTouchListener(CustomActivity.TOUCH);
        } else {
        /* Fetch data already in the row layout,
         *    primarily you only use this to get a copy of the ViewHolder */
        }

        TextView txtTitle = (TextView) view.findViewById(R.id.menu_listrow_title);
        ImageView imageView = (ImageView) view.findViewById(R.id.menu_listrow_thumbnail);
        txtTitle.setText(titles[position]);
        imageView.setImageResource(imageId[position]);

        return view;
    }
}
