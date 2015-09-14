package EEG;

/**
 * Created by kaganmoshe on 5/16/15.
 */
public enum ESignalVolume {
    GOOD_SIGNAL(0),
    HEAD_SET_NOT_COVERED(200),
    POOR_SIGNAL_LOW(1),
    POOR_SIGNAL_HIGH(50);

    private final int value;

    ESignalVolume(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    static public ESignalVolume getSignalVolume(int value) {
        ESignalVolume result;

        if (value == GOOD_SIGNAL.value()) {
            result = GOOD_SIGNAL;
        } else if (value == HEAD_SET_NOT_COVERED.value()) {
            result = HEAD_SET_NOT_COVERED;
        } else if (value < POOR_SIGNAL_HIGH.value()) {
            result = POOR_SIGNAL_LOW;
        } else {
            result = POOR_SIGNAL_HIGH;
        }

        return result;
    }
}
