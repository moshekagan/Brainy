package com.example.first.kaganmoshe.brainy.MindShooter;

import android.graphics.Point;

/**
 * Created by kaganmoshe on 8/13/15.
 */
public interface IMindShooter {
    void setIntentLocation(Point intentLocation);
    void setBalloonLocation(Point balloonLocation, boolean withAnimation);
    void animateIntentForLocation(Point intentLocation, int duration);
    void theBalloonExploded(Point m_currentBalloonLocation, int i);
    void setScore(int m_currentScore);
}
