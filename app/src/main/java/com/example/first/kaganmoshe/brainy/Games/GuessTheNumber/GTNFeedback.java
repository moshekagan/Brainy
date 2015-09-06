package com.example.first.kaganmoshe.brainy.Games.GuessTheNumber;

import com.example.first.kaganmoshe.brainy.Feedback.FeedbackClass;

/**
 * Created by tamirkash on 6/11/15.
 */
public class GTNFeedback extends FeedbackClass {

    private int mNumOfOffBaseInputs = 0;
    private int mMinAttemptsToAnswer = 0;
    private int mNumOfAttempts = 0;
    private int mLastInputEntered = NONE;
    private int mRightValue;

    private static final int[][] mRangeArrays = new int[GuessTheNumberLogic.NUM_OF_RANGES][];
    private static final int PERFECT_SCORE = 500;
    private static final int MIN_SCORE = 100;
    private static final int NONE = -1;

    static{
        for(int i = 0; i < GuessTheNumberLogic.NUM_OF_RANGES; i++){
            mRangeArrays[i] = new int[((i + 1) * GuessTheNumberLogic.RANGE_DIFFERENCE) + 1];
            initRangeArray(mRangeArrays[i], mRangeArrays[i].length);
        }
    }

    private static void initRangeArray(int[] rangeArray, int size){
        for(int i = 0; i < size; i++){
            rangeArray[i] = i;
        }
    }

    public GTNFeedback(int rightValue, int range){
        super();
        this.mRightValue = rightValue;
        mMinAttemptsToAnswer = initMinAttemptsToAnswer(range, rightValue);
    }

    private static int initMinAttemptsToAnswer(int range, int value) {
        int[] rangeArray = mRangeArrays[range/ GuessTheNumberLogic.RANGE_DIFFERENCE - 1];
        int left = 0;
        int right = rangeArray.length - 1;
        int mid;
        int result = 0;

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
        mNumOfAttempts++;

        if(mLastInputEntered != NONE && ((input < mRightValue && mLastInputEntered >= input) ||
                (input > mRightValue && mLastInputEntered <= input))){
            mNumOfOffBaseInputs++;
        }

        mLastInputEntered = input;
    }

    @Override
    public int getGameScore() {
        int finalScore = PERFECT_SCORE - (10 * (mNumOfOffBaseInputs));

        if(mNumOfAttempts > mMinAttemptsToAnswer){
            finalScore -= (5 * (mNumOfAttempts - mMinAttemptsToAnswer));
        }

        return finalScore > MIN_SCORE ? finalScore : MIN_SCORE;
    }
}
