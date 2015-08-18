package com.example.first.kaganmoshe.brainy.GuessTheNumber;

import java.util.Random;

/**
 * Created by tamirkash on 5/25/15.
 */
public class GuessTheNumberLogic {
    public enum GuessResult{
        NOT_IN_RANGE, TOO_LOW, TOO_HIGH, GOOD;
    }

    private int maxValue;
    private final Random random;
    private int value;
    private GTNFeedback feedback;

    public static final int NUM_OF_RANGES = 5;
    public static final int RANGE_DIFFERENCE = 100;

    public GuessTheNumberLogic(int maxValue){
        random = new Random();
        setNewValue(maxValue);
        feedback = new GTNFeedback(value, maxValue);
    }

    public void setFeedback(GTNFeedback feedback) {
        this.feedback = feedback;
    }

    public GTNFeedback getFeedback() {
        return feedback;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getValue() {
        return value;
    }

    public void setNewValue(int maxValue){
        this.maxValue = maxValue;
        value = random.nextInt(maxValue + 1);
    }

    public GuessResult checkGuess(int input){
        GuessResult guessResult;

        feedback.checkNewInput(input);

        if (input < 0 || input > maxValue) {
            guessResult = GuessResult.NOT_IN_RANGE;
        } else if (input == value) {
            guessResult = GuessResult.GOOD;
        } else if (input < value) {
            guessResult = GuessResult.TOO_LOW;
        } else {
            guessResult = GuessResult.TOO_HIGH;
        }

        return guessResult;
    }
}
