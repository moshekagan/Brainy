package com.example.first.kaganmoshe.brainy.AppActivities;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.first.kaganmoshe.brainy.R;

/**
 * Created by tamirkash on 7/27/15.
 */
public class MenuCustomList extends ArrayAdapter<String>{
    //TODO - make this generic !!

    private final Activity mContext;
    private final String[] mTitles;
    private final String[] mReviews;
    private final Integer[] mImagesId;
    private final int mLayoutResource;

    public MenuCustomList(Activity context,
                          String[] titles, Integer[] imageId, String[] reviews, int layoutResource) {
        super(context, layoutResource, titles);
        this.mReviews = reviews;
        this.mContext = context;
        this.mTitles = titles;
        this.mImagesId = imageId;
        this.mLayoutResource = layoutResource;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            view = inflater.inflate(mLayoutResource, null, true);

            ImageView imageView = (ImageView) view.findViewById(R.id.menu_listrow_thumbnail);
            imageView.setImageResource(mImagesId[position]);
            TextView review = (TextView) view.findViewById(R.id.menu_listrow_review);
            TextView txtTitle = (TextView) view.findViewById(R.id.menu_listrow_title);

            review.setText(mReviews[position]);
            txtTitle.setText(mTitles[position]);
        } else {
        /* Fetch data already in the row layout,
         *    primarily you only use this to get a copy of the ViewHolder */
        }

        return view;
    }
}
