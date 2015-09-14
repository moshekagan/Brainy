package EEG;

/**
 * Created by kaganmoshe on 5/24/15.
 */
public enum EHeadSetType {
    MindWave, Moker;


    @Override
    public String toString() {
        String res = "";

        switch (this) {
            case MindWave:
                res = "MindWave";
                break;
            case Moker:
                res = "Moker";
                break;
        }

        return res;
    }
}
