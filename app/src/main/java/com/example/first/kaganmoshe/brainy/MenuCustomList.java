package com.example.first.kaganmoshe.brainy;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.CustomActivity.AppActivity;

/**
 * Created by tamirkash on 7/27/15.
 */
public class MenuCustomList extends ArrayAdapter<String>{
    //TODO - make this generic !!

    private final Activity context;
    private final String[] titles;
    private final String[] reviews;
    private final Integer[] imageId;
    private final int layoutResource;

    public MenuCustomList(Activity context,
                          String[] titles, Integer[] imageId, String[] reviews, int layoutResource) {
        super(context, layoutResource, titles);
        this.reviews = reviews;
        this.context = context;
        this.titles = titles;
        this.imageId = imageId;
        this.layoutResource = layoutResource;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(layoutResource, null, true);

            ImageView imageView = (ImageView) view.findViewById(R.id.menu_listrow_thumbnail);
            imageView.setImageResource(imageId[position]);
            TextView review = (TextView) view.findViewById(R.id.menu_listrow_review);
            TextView txtTitle = (TextView) view.findViewById(R.id.menu_listrow_title);

            review.setText(reviews[position]);
            txtTitle.setText(titles[position]);
//            Utils.changeFont(context.getAssets(), txtTitle);
//            int alpha = 128; //50% transparency
//            int color = Color.WHITE; //Your color value
//            int bgColor = Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
//
//            view.setBackgroundColor(bgColor);
//            Log.d("COLOR", Integer.toString(bgColor));
//            view.setOnTouchListener(AppActivity.TOUCH);
        } else {
        /* Fetch data already in the row layout,
         *    primarily you only use this to get a copy of the ViewHolder */
        }

        return view;
    }
}
