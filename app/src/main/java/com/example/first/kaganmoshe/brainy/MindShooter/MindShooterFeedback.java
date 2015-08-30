package com.example.first.kaganmoshe.brainy.MindShooter;

import com.example.first.kaganmoshe.brainy.Feedback.FeedbackClass;

/**
 * Created by kaganmoshe on 8/18/15.
 */
public class MindShooterFeedback extends FeedbackClass {
    private int finalScore;

    public void calculateFinalScore(int score) {
        finalScore =  10 * score;
    }

    @Override
    public int getGameScore() {
        return finalScore;
    }
}
