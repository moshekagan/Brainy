package com.example.first.kaganmoshe.brainy.MindShooter;

import android.graphics.Point;

import com.example.first.kaganmoshe.brainy.AppManager;

import java.util.Random;

import EEG.EConnectionState;
import EEG.EHeadSetType;
import EEG.ESignalVolume;
import EEG.IHeadSetData;
import Utils.Logs;

/**
 * Created by kaganmoshe on 8/13/15.
 */
public class MindShooterLogic implements IHeadSetData{
    enum EEdges {
        LEFT (1),
        TOP (2),
        RIGHT(3),
        BOTTOM (4);

        private int val;
        EEdges(int val){ this.val = val; }
        public int getValue() { return val; }
        public static EEdges getEgdeByValue(int val){ 
            EEdges res = LEFT;
            switch (val){
                case 2: res = TOP; break;
                case 3: res = RIGHT; break;
                case 4: res = BOTTOM; break;
            }
            return res;
        }
    }

    // Data Members
    public static final String MIND_SHOOTER_LOGIC = "MindShooterLogic";
    private final int screenWidth;
    private final int screenHeight;
    private final int X_MinRangeScreen;
    private final int X_MaxRangeScreen;
    private final int Y_MinRangeScreen;
    private final int Y_MaxRangeScreen;

    private IMindShooter m_MindShooter;
    private Point m_CurrentBalloonLocation = new Point();
    private Point m_CurrentIntentLocation = new Point();
    private Point m_BalloonSize = new Point(250, 250);
    private Point m_IntentSize = new Point(180, 180);
    private boolean m_ListenToHeadSet= false;
    private int m_SpaceVal = 20;
    private int m_CurrentScore = 0;
    private Random rand = new Random();
    private EEdges m_CurrentEdge = EEdges.BOTTOM; 

//    private int balloonSize = 250;
//    private int intentSize = 135;
    // Methods
    public MindShooterLogic(int screenWidth, int screenHeight, IMindShooter mindShooter) throws Exception {
        if (mindShooter == null)
            throw new Exception("Can't be a NULL listener! (IMindShooter is NULL)");

        if (AppManager.getInstance().getAppSettings().getHeadSetType() != EHeadSetType.Moker){
            AppManager.getInstance().getHeadSet().registerListener(this); // TODO - unregister
            m_ListenToHeadSet = true;
        }
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.m_MindShooter = mindShooter;
        m_CurrentBalloonLocation.set(screenWidth/2, screenHeight/4);
        m_CurrentIntentLocation.set(m_CurrentBalloonLocation.x/2, m_CurrentBalloonLocation.y*3);
        X_MaxRangeScreen = getX_MaxRangeForBalloon();
        X_MinRangeScreen = getX_MinRange();
        Y_MaxRangeScreen = getY_MaxRangeForBalloon();
        Y_MinRangeScreen = getY_MinRange();
    }

    private Point calculatePointTarget(){
        Point target = new Point();

        return target;
    }

    public void startGame(){
        // TODO - Init: timer,
        m_MindShooter.setIntentLocation(m_CurrentIntentLocation);
        m_MindShooter.setBalloonLocation(m_CurrentBalloonLocation, true);
        m_MindShooter.setScore(m_CurrentScore);

//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                m_MindShooter.animateIntentForLocation(
//                        new Point(m_CurrentBalloonLocation.x + m_CurrentBalloonLocation.x/2,
//                                m_CurrentBalloonLocation.y + m_CurrentBalloonLocation.y/2 ),
//                        1500);
//            }
//        }, 0, 1000);
    }

    public void shoot(){
        Logs.error(MIND_SHOOTER_LOGIC, "Enter to MindShooterLogic.Shoot()");
        int XmiddelIntentLocation = m_CurrentIntentLocation.x + (m_IntentSize.x/2);
        int YmiddelIntentLocation = m_CurrentIntentLocation.y + (m_IntentSize.y/2);

        // TODO - Make sound of shoot
        if ( XmiddelIntentLocation >= m_CurrentBalloonLocation.x + m_SpaceVal &&
                XmiddelIntentLocation <= m_CurrentBalloonLocation.x + m_BalloonSize.x - m_SpaceVal &&
                YmiddelIntentLocation >= m_CurrentBalloonLocation.y + m_SpaceVal &&
                YmiddelIntentLocation <= m_CurrentBalloonLocation.y + m_BalloonSize.y - m_SpaceVal)
        { // Good Shoot
            // TODO - Make sound of the balloon bamp
            calculateNewLocationForBalloon();
            m_MindShooter.theBalloonExploded(m_CurrentBalloonLocation, ++m_CurrentScore);
        }
    }

    int count1 = 0;
    private void calculateNewLocationForBalloon() {
        int newX_Position;
        int newY_Position;

        do {
            newX_Position = rand.nextInt(X_MaxRangeScreen) + X_MinRangeScreen;
            newY_Position = rand.nextInt(Y_MaxRangeScreen) + Y_MinRangeScreen;
        } while (newY_Position >= Y_MaxRangeScreen - 120 &&
                newX_Position >= screenWidth / 2 - 180 &&
                newX_Position <= screenWidth / 2);

        m_CurrentBalloonLocation.set(newX_Position, newY_Position);
//        int val = m_SpaceVal*3;
//        switch (count1){
//            case 0:
//                m_CurrentBalloonLocation.set(val,val); // Top Left
//                break;
//            case 1:
//                m_CurrentBalloonLocation.set(screenWidth - m_BalloonSize.x - val, val); // Top Right
//                break;
//            case 2:
//                m_CurrentBalloonLocation.set(val, screenHeight - m_BalloonSize.y -val - val - val); // Bottom Left
//                break;
//            case 3:
//                m_CurrentBalloonLocation.set(screenWidth - m_BalloonSize.x - val,
//                        screenHeight - m_BalloonSize.y -val - val - val); //Bottom Right
//                count1 = 0;
//                break;
//        }
//        count1++;

//        if (count1%2 == 0)
//            m_CurrentBalloonLocation.set(m_CurrentBalloonLocation.x*2, m_CurrentBalloonLocation.y*2);
//        else m_CurrentBalloonLocation.set(m_CurrentBalloonLocation.x/2, m_CurrentBalloonLocation.y/2);

//        else if (count%3 == 0) m_CurrentBalloonLocation.set(m_CurrentBalloonLocation.x/2, m_CurrentBalloonLocation.y + m_CurrentBalloonLocation.y/8);
//        else if (count%4 == 0) m_CurrentBalloonLocation.set(m_CurrentBalloonLocation.x + m_CurrentBalloonLocation.x/8, m_CurrentBalloonLocation.y/2);
//        else m_CurrentBalloonLocation.set(m_CurrentBalloonLocation.x + m_CurrentBalloonLocation.x/10, m_CurrentBalloonLocation.y + m_CurrentBalloonLocation.y/12);
    }

    int count =0;
    // TODO - Need to remove this method...
    public void test(){
//        count++;
//        int intentImageSize = 180;
//        int disBetweenTheEdges = 80;
//        switch (count){
//            case 1:
//                m_CurrentIntentLocation.set(disBetweenTheEdges, disBetweenTheEdges);
//                break;
//            case 2:
//                m_CurrentIntentLocation.set(screenWidth-disBetweenTheEdges-intentImageSize, disBetweenTheEdges);
//                break;
//            case 3:
//                m_CurrentIntentLocation.set(disBetweenTheEdges, screenHeight-disBetweenTheEdges-intentImageSize);
//                break;
//            case 4:
//                m_CurrentIntentLocation.set(screenWidth-disBetweenTheEdges-intentImageSize, screenHeight-disBetweenTheEdges-intentImageSize);
//                int x = m_CurrentBalloonLocation.x - intentImageSize <= screenWidth-disBetweenTheEdges-intentImageSize ?
//                        m_CurrentBalloonLocation.x - intentImageSize : m_CurrentBalloonLocation.x + intentImageSize;
//                int y = m_CurrentBalloonLocation.y - disBetweenTheEdges <= screenHeight-disBetweenTheEdges-intentImageSize?
//                        m_CurrentBalloonLocation.y - disBetweenTheEdges : m_CurrentBalloonLocation.y + disBetweenTheEdges;
//                m_CurrentBalloonLocation.set(x,y);
//                m_MindShooter.setBalloonLocation(m_CurrentBalloonLocation);
//                count = 0;
//                break;
//        }
        m_CurrentIntentLocation.set(m_CurrentBalloonLocation.x+ m_SpaceVal, m_CurrentBalloonLocation.y+ m_SpaceVal);
        m_MindShooter.animateIntentForLocation(m_CurrentIntentLocation, 1000);
    }

    private float getAttentionAsFraction(int attVal) {
        return (attVal * 0.01f);
    }

    @Override
    public void onAttentionReceived(int attValue) {
        if (m_ListenToHeadSet) {
            Logs.error(MIND_SHOOTER_LOGIC, "Att Rec =>" + attValue);
            float attFranction = getAttentionAsFraction(attValue);
//            goToTheTarget(calcNewPosition(attFranction));

            if (attValue >= 75) { // Case that bring the intent right to the target
                goToTheTarget(attFranction);
            } else if(attValue >= 65){
                goCloseToTarget(attFranction, 1.25f);
            } else if (attValue >= 50) { // Bring the intent close but not enough
                goCloseToTarget(attFranction, 1.5f);
            } else if (attValue >= 25) { // Taking the intent far from the target
                goCloseToTarget(attFranction, 1.75f);
//                goFarFromTarget(attFranction);
            } else { // Taking the intent far from
                goFarAwayFromTarget(attFranction);
            }
        }
    }

//    private Point calcNewPosition(float attFranction) {
//        return new Point(m_CurrentBalloonLocation.x + m_SpaceVal + m_BalloonSize.x/2,
//                m_CurrentBalloonLocation.y + m_SpaceVal + m_BalloonSize.y/2);
//    }
    
    private EEdges getAndSetNextRandomEdge(){
        int newEdgeVal;
        newEdgeVal = rand.nextInt(4) + 1;
        Logs.debug(MIND_SHOOTER_LOGIC, "Rand the number: " + newEdgeVal);

        switch (m_CurrentEdge){
            case LEFT:
                if (newEdgeVal == 1) m_CurrentEdge = EEdges.TOP;
                else if (newEdgeVal == 2) m_CurrentEdge = EEdges.BOTTOM;
                break;
            case TOP:
                if (newEdgeVal == 1) m_CurrentEdge = EEdges.RIGHT;
                else if (newEdgeVal == 2) m_CurrentEdge = EEdges.LEFT;
                break;
            case RIGHT:
                if (newEdgeVal == 1) m_CurrentEdge = EEdges.BOTTOM;
                else if (newEdgeVal == 2) m_CurrentEdge = EEdges.TOP;
                break;
            case BOTTOM:
                if (newEdgeVal == 1) m_CurrentEdge = EEdges.LEFT;
                else if (newEdgeVal == 2) m_CurrentEdge = EEdges.RIGHT;
                break;
        }

        return m_CurrentEdge;
    }

    private void goFarAwayFromTarget(float attFranction) {
//        int randPosition = rand.nextInt(4) + 1; // random new position (1-8)
        int newXPosition = getX_MinRange();
        int newYPosition = getY_MinRange();

        switch (getAndSetNextRandomEdge()){
            case LEFT:
                newYPosition = rand.nextInt(getY_MaxRangeForIntent()) + getY_MinRange();
                break;
            case TOP:
                newXPosition = rand.nextInt(getX_MaxRangeForIntent()) + getX_MinRange();
                break;
            case RIGHT:
                newYPosition = rand.nextInt(getY_MaxRangeForIntent()) + getY_MinRange();
                newXPosition = getX_MaxRangeForIntent();
                break;
            case BOTTOM:
                newXPosition = rand.nextInt(getX_MaxRangeForIntent()) + getX_MinRange();
                newYPosition = getY_MaxRangeForIntent();
                break;
        }

//        switch (randPosition){
//            case 1:
//                break;
//            case 2:
//                newXPosition = getX_MaxRangeForIntent() / 2;
//                break;
//            case 3:
//                newXPosition = getX_MaxRangeForIntent();
//                break;
//            case 4:
//                newXPosition = getX_MaxRangeForIntent();
//                newYPosition = getY_MaxRangeForIntent() / 2;
//                break;
//            case 5:
//                newXPosition = getX_MaxRangeForIntent();
//                newYPosition = getY_MaxRangeForIntent();
//                break;
//            case 6:
//                newXPosition = getX_MaxRangeForIntent() / 2;
//                newYPosition = getY_MaxRangeForIntent();
//                break;
//            case 7:
//                newYPosition = getY_MaxRangeForIntent();
//                break;
//            case 8:
//                newYPosition = getY_MaxRangeForIntent() / 2;
//                break;
//        }

//        count++;
//        int intentImageSize = 180;
//        int disBetweenTheEdges = 80;
//        if(count%2 == 0)
//                m_CurrentIntentLocation.set(disBetweenTheEdges, disBetweenTheEdges);
//        else if (count%3 == 0)
//                m_CurrentIntentLocation.set(screenWidth-disBetweenTheEdges-intentImageSize, disBetweenTheEdges);
//        else if (count%5 == 0)
//                m_CurrentIntentLocation.set(disBetweenTheEdges, screenHeight-disBetweenTheEdges-intentImageSize);
//        else
//                m_CurrentIntentLocation.set(screenWidth-disBetweenTheEdges-intentImageSize, screenHeight-disBetweenTheEdges-intentImageSize);
        m_CurrentIntentLocation.set(newXPosition, newYPosition);
        m_MindShooter.animateIntentForLocation(m_CurrentIntentLocation, 700);
    }

    private void goFarFromTarget(float attFranction) {
        m_CurrentIntentLocation.set(m_CurrentBalloonLocation.x - m_CurrentBalloonLocation.x/2 + m_SpaceVal + m_BalloonSize.x/2,
                m_CurrentBalloonLocation.y - m_CurrentBalloonLocation.y/2 + m_SpaceVal + m_BalloonSize.y/2);
        m_MindShooter.animateIntentForLocation(m_CurrentIntentLocation, 1000);
    }

    private void goCloseToTarget(float attFranction, float level) {
        int newXIntentPosition;
        int newYIntentPosition;

        int X_SpaceVal = m_SpaceVal + m_IntentSize.x;
        int Y_SpaceVal = m_SpaceVal + m_IntentSize.y;

        int rangeHeight = (int)((m_CurrentBalloonLocation.y + m_BalloonSize.y) * level -
                (m_CurrentBalloonLocation.y / level));
        int rangeWidth = (int)((m_CurrentBalloonLocation.x + m_BalloonSize.x) * level -
                (m_CurrentBalloonLocation.x / level));

        switch (getAndSetNextRandomEdge()){
            case LEFT:
                newYIntentPosition = rand.nextInt(rangeHeight) + (int)(m_CurrentBalloonLocation.y / level);
                newXIntentPosition = (int)(m_CurrentBalloonLocation.x / level);
                break;
            case TOP:
                newXIntentPosition = rand.nextInt(rangeWidth) + (int)(m_CurrentBalloonLocation.x / level);
                newYIntentPosition = (int)(m_CurrentBalloonLocation.y / level);
                break;
            case RIGHT:
                newYIntentPosition = rand.nextInt(rangeHeight) + (int)(m_CurrentBalloonLocation.y / level);
                newXIntentPosition = (int)((m_CurrentBalloonLocation.x + m_BalloonSize.x) / level);
                break;
            case BOTTOM:
            default:
                newXIntentPosition = rand.nextInt(rangeWidth) + (int)(m_CurrentBalloonLocation.x / level);
                newYIntentPosition = (int)((m_CurrentBalloonLocation.y + m_BalloonSize.y) / level);
                break;
        }

//        newXIntentPosition += X_SpaceVal;
//        newYIntentPosition += Y_SpaceVal;

        newYIntentPosition = fix_Y_Location(newYIntentPosition);
        newXIntentPosition = fix_X_Location(newXIntentPosition);

        m_CurrentIntentLocation.set(newXIntentPosition, newYIntentPosition);
//        m_CurrentIntentLocation.set(m_CurrentBalloonLocation.x - m_CurrentBalloonLocation.x/3 + m_SpaceVal + m_BalloonSize.x/2,
//                m_CurrentBalloonLocation.y - m_CurrentBalloonLocation.y/3 + m_SpaceVal + m_BalloonSize.y/2);
        m_MindShooter.animateIntentForLocation(m_CurrentIntentLocation, 1000);
    }

    private int fix_Y_Location(int y){
        // TODO:
        return y;
    }

    private int fix_X_Location(int x){
        // TODO:
        return x;
    }

    private void goToTheTarget(float attFranction) {
        m_CurrentIntentLocation.set(m_CurrentBalloonLocation.x + m_SpaceVal + m_BalloonSize.x/2,
                m_CurrentBalloonLocation.y + m_SpaceVal + m_BalloonSize.y/2);
        m_MindShooter.animateIntentForLocation(m_CurrentIntentLocation, 1000);
    }

//    private void getNewYPos(float attFranction) {
//
//        return
//    }

//    private float getNewXPos(float attPresent) {
//        final int distanceFromTopActivity = 15;
//        final int startLocationOnActivity = 1200;
//        float balloonRange = startLocationOnActivity - distanceFromTopActivity;
//
//        return (balloonRange * (1 - attPresent)) + distanceFromTopActivity;
//    }

    public void setBalloonSize(Point p){ this.m_BalloonSize.set(p.x, p.y); }

    public void setIntentSize(Point p){ this.m_IntentSize.set(p.x, p.y); }

    @Override
    public void onMeditationReceived(int medValue) {

    }

    @Override
    public void onHeadSetChangedState(String headSetName, EConnectionState connectionState) {

    }

    @Override
    public void onPoorSignalReceived(ESignalVolume signalVolume) {

    }

    public int getX_MaxRangeForBalloon() { return screenWidth - m_BalloonSize.x - 2*getX_MinRange(); }

    public int getY_MaxRangeForBalloon() { return screenHeight - m_BalloonSize.y - 2*getY_MinRange(); }

    public int getX_MaxRangeForIntent() { return screenWidth - m_IntentSize.x - 2*getX_MinRange(); }

    public int getY_MaxRangeForIntent() { return screenHeight - m_IntentSize.y - 2*getY_MinRange(); }

    public int getX_MinRange() {
        return 80;
    }

    public int getY_MinRange() {
        return 80;
    }
}
