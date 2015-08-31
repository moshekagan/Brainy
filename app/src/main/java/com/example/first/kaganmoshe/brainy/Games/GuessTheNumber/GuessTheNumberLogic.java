package com.example.first.kaganmoshe.brainy.Games.GuessTheNumber;

import java.util.Random;

/**
 * Created by tamirkash on 5/25/15.
 */
public class GuessTheNumberLogic {
    public enum GuessResult{
        NOT_IN_RANGE, TOO_LOW, TOO_HIGH, GOOD;
    }

    private int mMaxValue;
    private final Random mRandom;
    private int mRightValue;
    private GTNFeedback mFeedback;

    public static final int NUM_OF_RANGES = 1;
    public static final int RANGE_DIFFERENCE = 100;

    public GuessTheNumberLogic(int maxValue){
        mRandom = new Random();
        setNewValue(maxValue);
        mFeedback = new GTNFeedback(mRightValue, maxValue);
    }

    public void setFeedback(GTNFeedback feedback) {
        this.mFeedback = feedback;
    }

    public GTNFeedback getFeedback() {
        return mFeedback;
    }

    public int getMaxValue() {
        return mMaxValue;
    }

    public int getValue() {
        return mRightValue;
    }

    public void setNewValue(int maxValue){
        this.mMaxValue = maxValue;
        mRightValue = mRandom.nextInt(maxValue + 1);
    }

    public GuessResult checkGuess(int input){
        GuessResult guessResult;

        mFeedback.checkNewInput(input);

        if (input < 0 || input > mMaxValue) {
            guessResult = GuessResult.NOT_IN_RANGE;
        } else if (input == mRightValue) {
            guessResult = GuessResult.GOOD;
        } else if (input < mRightValue) {
            guessResult = GuessResult.TOO_LOW;
        } else {
            guessResult = GuessResult.TOO_HIGH;
        }

        return guessResult;
    }
}
