package EEG;


import java.util.LinkedList;
import java.util.List;

/**
 * Created by kaganmoshe on 6/13/15.
 */
public class Moker extends EegHeadSet {
    // Data Members
    final String MOCKER = "Mocker";

    // Data Members
    private boolean m_IsConnected = false;
    private int m_CurrentRawData;

    private int m_PreviousAttentionValue;
    private int m_PreviousMeditationValue;

    private List<Integer> m_AttentionData = new LinkedList<>();

    // C'tor
    public Moker(){
        m_AttentionData.add(0);     m_AttentionData.add(0);     m_AttentionData.add(0);
        addValues();
        m_AttentionData.add(0);     m_AttentionData.add(0);     m_AttentionData.add(0);
    }

    // Methods
    @Override
    public EConnectionState connect() {
        return EConnectionState.DEVICE_CONNECTING;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean IsConnected() {
        return false;
    }

    private void addValues(){
        m_AttentionData.add(10);     m_AttentionData.add(20);     m_AttentionData.add(30);
        m_AttentionData.add(25);     m_AttentionData.add(30);     m_AttentionData.add(40);
        m_AttentionData.add(70);     m_AttentionData.add(66);     m_AttentionData.add(72);
        m_AttentionData.add(80);     m_AttentionData.add(84);     m_AttentionData.add(88);
        m_AttentionData.add(86);     m_AttentionData.add(88);     m_AttentionData.add(88);
        m_AttentionData.add(78);     m_AttentionData.add(70);     m_AttentionData.add(90);
        m_AttentionData.add(85);     m_AttentionData.add(78);     m_AttentionData.add(95);
        m_AttentionData.add(90);     m_AttentionData.add(89);     m_AttentionData.add(100);
        m_AttentionData.add(90);     m_AttentionData.add(95);     m_AttentionData.add(100);
        m_AttentionData.add(100);    m_AttentionData.add(100);    m_AttentionData.add(90);
        m_AttentionData.add(90);     m_AttentionData.add(80);     m_AttentionData.add(70);
        m_AttentionData.add(75);     m_AttentionData.add(69);     m_AttentionData.add(88);
        m_AttentionData.add(80);     m_AttentionData.add(87);     m_AttentionData.add(90);
        m_AttentionData.add(80);     m_AttentionData.add(78);     m_AttentionData.add(60);
    }

}
