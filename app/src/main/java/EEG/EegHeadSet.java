package EEG;

import java.util.LinkedList;
import java.util.List;

import Utils.Logs;

/**
 * Created by kaganmoshe on 5/9/15.
 */
public abstract class EegHeadSet {
    // Final Members

    static public final String EEGHEADSET_STR = "EegHeadSet";
    static public final String MEDITATION_STR = "Meditation";
    static public final String ATTENTION_STR = "Attention";
    static public final String POOR_SIGNAL_STR = "Poor Signal";

    // Data Members
    protected List<IHeadSetData> m_HeadSetData = new LinkedList<>();
    private int m_CurrentPoorSignal = ESignalVolume.HEAD_SET_NOT_COVERED.value();

    // Methods
    public abstract EConnectionState connect();

    public abstract void close();

    public abstract boolean IsConnected();

    public void registerListener(IHeadSetData headSetDate){
        m_HeadSetData.add(headSetDate);
    }

    public void unregisterListener(IHeadSetData headSetDate){
        if (m_HeadSetData.contains(headSetDate))
            m_HeadSetData.remove(headSetDate);
    }


    public void removeListener(IHeadSetData headSetData){
        m_HeadSetData.remove(headSetData);
    }

    public void raiseOnAttention(int attValue){
        Logs.info(EEGHEADSET_STR, ATTENTION_STR + ": " + attValue);

        for (IHeadSetData headSetData : m_HeadSetData){
            if (headSetData != null){
                headSetData.onAttentionReceived(attValue);
            }
        }
    }

    public void raiseOnMeditation(int medValue){

        Logs.info(EEGHEADSET_STR, MEDITATION_STR + ": " + medValue);

        for (IHeadSetData headSetData : m_HeadSetData){
            if (headSetData != null){
                headSetData.onMeditationReceived(medValue);
            }
        }
    }

    public void raiseOnHeadSetChangedState(String headSetName, EConnectionState connectionState){
        Logs.info(EEGHEADSET_STR, headSetName + " connecetd!");

        for (IHeadSetData headSetData : m_HeadSetData){
            if (headSetData != null){
                headSetData.onHeadSetChangedState(headSetName, connectionState);
            }
        }
    }

    public void raiseOnPoorSignal(int poorSignalValue){
        Logs.info(EEGHEADSET_STR, POOR_SIGNAL_STR + ": " + poorSignalValue);

        ESignalVolume signalVolume = ESignalVolume.getSignalVolume(poorSignalValue);

        if (signalVolume != ESignalVolume.GOOD_SIGNAL || m_CurrentPoorSignal != poorSignalValue) {
            for (IHeadSetData headSetData : m_HeadSetData) {
                if (headSetData != null) {
                    headSetData.onPoorSignalReceived(signalVolume);
                }
            }
        }

        m_CurrentPoorSignal = poorSignalValue;
    }

}
