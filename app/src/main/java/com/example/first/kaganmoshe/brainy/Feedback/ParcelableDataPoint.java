package com.example.first.kaganmoshe.brainy.Feedback;

import android.os.Parcel;
import android.os.Parcelable;

import com.jjoe64.graphview.series.DataPoint;

/**
 * Created by tamirkash on 6/12/15.
 */
public class ParcelableDataPoint extends DataPoint implements Parcelable {
    public ParcelableDataPoint(double x, double y) {
        super(x, y);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(this.getX());
        parcel.writeDouble(this.getY());
    }

    public static final Parcelable.Creator<ParcelableDataPoint> CREATOR
            = new Parcelable.Creator<ParcelableDataPoint>() {
        public ParcelableDataPoint createFromParcel(Parcel in) {
            return new ParcelableDataPoint(in);
        }

        public ParcelableDataPoint[] newArray(int size) {
            return new ParcelableDataPoint[size];
        }
    };

    private ParcelableDataPoint(Parcel in) {
        super(in.readDouble(), in.readDouble());
    }
}
