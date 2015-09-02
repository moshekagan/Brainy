package com.example.first.kaganmoshe.brainy.Games.HotAirBallon;

/**
 * Created by tamirkash on 8/5/15.
 */
public class HotAirBalloonFeedback extends com.example.first.kaganmoshe.brainy.Feedback.FeedbackClass {
    @Override
    public int getGameScore() {
        return 100;
    }

    public void calculateFinalScore(int score) {
        finalScore =  10 * score;
    }
}
