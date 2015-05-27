package com.example.first.kaganmoshe.brainy.GuessTheNumber;

import java.util.Random;

/**
 * Created by tamirkash on 5/25/15.
 */
public class GuessTheNumberEngine {
    public enum GuessResult{
        NOT_IN_RANGE, TOO_LOW, TOO_HIGH, GOOD;
    }

    private int maxValue;
    private final Random random;
    private int value;

    public GuessTheNumberEngine(int maxValue){
        random = new Random();
        setNewValue(maxValue);
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

    public GuessResult checkGuess(int guess){
        GuessResult guessResult = null;

        if (guess < 0 || guess > maxValue) {
            guessResult = GuessResult.NOT_IN_RANGE;
        } else if (guess == value) {
            guessResult = GuessResult.GOOD;
        } else if (guess < value) {
            guessResult = GuessResult.TOO_LOW;
        } else {
            guessResult = GuessResult.TOO_HIGH;
        }

        return guessResult;
    }
}
