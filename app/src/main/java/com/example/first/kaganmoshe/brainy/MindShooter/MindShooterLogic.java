package com.example.first.kaganmoshe.brainy.MindShooter;

import android.graphics.Point;

import com.example.first.kaganmoshe.brainy.AppManager;

import EEG.EConnectionState;
import EEG.EHeadSetType;
import EEG.ESignalVolume;
import EEG.IHeadSetData;
import Utils.Logs;

/**
 * Created by kaganmoshe on 8/13/15.
 */
public class MindShooterLogic implements IHeadSetData{

    // Data Members
    public static final String MIND_SHOOTER_LOGIC = "MindShooterLogic";
    private final int screenWidth;
    private final int screenHeight;

    private IMindShooter m_MindShooter;
    private Point m_CurrentBalloonLocation = new Point();
    private Point m_CurrentIntentLocation = new Point();
    private Point m_BalloonSize = new Point(250, 250);
    private Point m_IntentSize = new Point(135, 135);
    private boolean m_ListenToHeadSet= false;
    private int m_SpaceVal = 20;
    private int m_CurrentScore = 0;

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
        if ( XmiddelIntentLocation >= m_CurrentBalloonLocation.x+ m_SpaceVal &&
                XmiddelIntentLocation <= m_CurrentBalloonLocation.x+ m_BalloonSize.x- m_SpaceVal &&
                YmiddelIntentLocation >= m_CurrentBalloonLocation.y+ m_SpaceVal &&
                YmiddelIntentLocation <= m_CurrentBalloonLocation.y+ m_BalloonSize.y- m_SpaceVal)
        { // Good Shoot
            // TODO - Make sound of the balloon bamp
            calculateNewLocationForBalloon();
            m_MindShooter.theBalloonExploded(m_CurrentBalloonLocation, ++m_CurrentScore);
        }
    }

    int count1 =0 ;
    private void calculateNewLocationForBalloon() {
        count1++;
        if (count1%2 == 0)
            m_CurrentBalloonLocation.set(m_CurrentBalloonLocation.x*2, m_CurrentBalloonLocation.y*2);
        else m_CurrentBalloonLocation.set(m_CurrentBalloonLocation.x/2, m_CurrentBalloonLocation.y/2);
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
            } else if (attValue >= 50) { // Bring the intent close but not enough
                goCloseToTarget(attFranction);
            } else if (attValue >= 25) { // Taking the intent far from the target
                goFarFromTarget(attFranction);
            } else { // Taking the intent far from
                goFarAwayFromTarget(attFranction);
            }
        }
    }

//    private Point calcNewPosition(float attFranction) {
//        return new Point(m_CurrentBalloonLocation.x + m_SpaceVal + m_BalloonSize.x/2,
//                m_CurrentBalloonLocation.y + m_SpaceVal + m_BalloonSize.y/2);
//    }

    private void goFarAwayFromTarget(float attFranction) {
        count++;
        int intentImageSize = 180;
        int disBetweenTheEdges = 80;
        if(count%2 == 0)
                m_CurrentIntentLocation.set(disBetweenTheEdges, disBetweenTheEdges);
        else if (count%3 == 0)
                m_CurrentIntentLocation.set(screenWidth-disBetweenTheEdges-intentImageSize, disBetweenTheEdges);
        else if (count%4 == 0)
                m_CurrentIntentLocation.set(disBetweenTheEdges, screenHeight-disBetweenTheEdges-intentImageSize);
        else
                m_CurrentIntentLocation.set(screenWidth-disBetweenTheEdges-intentImageSize, screenHeight-disBetweenTheEdges-intentImageSize);

        m_MindShooter.animateIntentForLocation(m_CurrentIntentLocation, 1000);
    }

    private void goFarFromTarget(float attFranction) {
        m_CurrentIntentLocation.set(m_CurrentBalloonLocation.x - m_CurrentBalloonLocation.x/2 + m_SpaceVal + m_BalloonSize.x/2,
                m_CurrentBalloonLocation.y - m_CurrentBalloonLocation.y/2 + m_SpaceVal + m_BalloonSize.y/2);
        m_MindShooter.animateIntentForLocation(m_CurrentIntentLocation, 1000);
    }

    private void goCloseToTarget(float attFranction) {
        m_CurrentIntentLocation.set(m_CurrentBalloonLocation.x - m_CurrentBalloonLocation.x/3 + m_SpaceVal + m_BalloonSize.x/2,
                m_CurrentBalloonLocation.y - m_CurrentBalloonLocation.y/3 + m_SpaceVal + m_BalloonSize.y/2);
        m_MindShooter.animateIntentForLocation(m_CurrentIntentLocation, 1000);
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
}
