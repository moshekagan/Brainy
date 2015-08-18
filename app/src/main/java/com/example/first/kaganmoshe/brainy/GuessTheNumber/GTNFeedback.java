package com.example.first.kaganmoshe.brainy.GuessTheNumber;

import android.util.Log;

import com.example.first.kaganmoshe.brainy.Feedback.FeedbackClass;

/**
 * Created by tamirkash on 6/11/15.
 */
public class GTNFeedback extends FeedbackClass {

    private int numOfOffBaseInputs = 0;
    private int minAttemptsToAnswer = 0;
    private int numOfAttempts = 0;
    private int lastInputEntered = NONE;
    private int rightValue;

    private static final int PERFECT_SCORE = 500;
    private static final int MIN_SCORE = 100;
    private static final int[][] rangeArrays = new int[GuessTheNumberLogic.NUM_OF_RANGES][];
    private static final int NONE = -1;

    static{
        Log.d("GTN", "STARTING INIT RANGES ARRAY");
        for(int i = 0; i < GuessTheNumberLogic.NUM_OF_RANGES; i++){
            rangeArrays[i] = new int[((i + 1) * GuessTheNumberLogic.RANGE_DIFFERENCE) + 1];
            initRangeArray(rangeArrays[i], rangeArrays[i].length);
        }
        Log.d("GTN", "FINISH INIT RANGES ARRAY");
    }

    private static void initRangeArray(int[] rangeArray, int size){
        for(int i = 0; i < size; i++){
            rangeArray[i] = i;
        }
    }

    public GTNFeedback(int rightValue, int range){
        super();
        this.rightValue = rightValue;
        Log.d("GTN", "RANGE: " + range + " VALUE: " + rightValue);
        minAttemptsToAnswer = initMinAttemptsToAnswer(range, rightValue);
        Log.d("GTN", "MIN ATTEMPTS: " + Integer.valueOf(minAttemptsToAnswer));
    }

    private static int initMinAttemptsToAnswer(int range, int value) {
        int[] rangeArray = rangeArrays[range/ GuessTheNumberLogic.RANGE_DIFFERENCE - 1];
        int left = 0;
        int right = rangeArray.length - 1;
        int mid;
        int result = 0;

        Log.d("GTN", "RANGE ARRAY SIZE: " + Integer.valueOf(right));

        while(left <= right){
            result++;
            mid = (right + left) / 2;

            if(rangeArray[mid] < value){
                left = mid + 1;
            } else if (rangeArray[mid] > value){
                right = mid - 1;
            } else{
                break;
            }
        }

        return result;
    }

    public void checkNewInput(int input){
        numOfAttempts++;

        if(lastInputEntered != NONE && ((input < rightValue && lastInputEntered >= input) ||
                (input > rightValue && lastInputEntered <= input))){
            Log.d("GTN", "OFF BASE");
            numOfOffBaseInputs++;
        }

        lastInputEntered = input;
    }

    @Override
    public int getGameScore() {
        int finalScore = PERFECT_SCORE - (10 * (numOfOffBaseInputs)) - (5 * (numOfAttempts - minAttemptsToAnswer));
        Log.d("GTN", "FINAL SCORE: " + finalScore);
        Log.d("GTN", "OFF BASE INPUTS: " + numOfOffBaseInputs);
        Log.d("GTN", "NUM OF ATTEMPTS: " + numOfAttempts);

        return finalScore > MIN_SCORE ? finalScore : MIN_SCORE;
    }
}
