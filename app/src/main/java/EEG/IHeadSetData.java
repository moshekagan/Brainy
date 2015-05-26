package EEG;

/**
 * Created by kaganmoshe on 5/14/15.
 */
public interface IHeadSetData {
    public void onAttentionReceived(int attValue);
    public void onMeditationReceived(int medValue);
    public void onHeadSetChangedState(String headSetName, EConnectionState connectionState);
    public void onPoorSignalReceived(ESignalVolume signalVolume);
}
