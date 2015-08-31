package com.example.first.kaganmoshe.brainy.Games.CrazyCube;

import com.example.first.kaganmoshe.brainy.Feedback.FeedbackClass;

/**
 * Created by tamirkash on 7/26/15.
 */
public class CCubeFeedback extends FeedbackClass {
    private int finalScore;

    public void calculateFinalScore(int score, int badChoicesLeft) {
        finalScore = ((5 * badChoicesLeft) + (10 * score));
    }

    @Override
    public int getGameScore() {
        return finalScore;
    }
}
