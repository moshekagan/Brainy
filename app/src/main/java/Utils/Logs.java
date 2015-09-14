package Utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kaganmoshe on 5/13/15.
 */
public class Logs {
    static private final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
    static public final String SEPARATOR_LINE = "-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-/n";
    static public final int NOT_WRITTEN = -1;
    static private boolean m_WriteLogs = false;
    static public DateFormat m_DateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
    static public Date m_Date = new Date();

    static public String format(String tag, String msg){
        return "||" + "BRAINY" + "|| " + tag + " || " + msg;
    }
    
    public void setWriteLogs(boolean writeLogs) { m_WriteLogs = writeLogs; }

    static public int info(String tag, String msg){
        if (m_WriteLogs)
            return Log.v(tag, format(tag, msg));
        else return NOT_WRITTEN;
    }

    static public int debug(String tag, String msg){
        if (m_WriteLogs)
            return Log.d(tag, format(tag, msg));
        else return NOT_WRITTEN;
    }

    static public int verbose(String tag, String msg){
        if (m_WriteLogs)
            return Log.v(tag, format(tag, msg));
        else return NOT_WRITTEN;
    }

    static public int warn(String tag, String msg){
        if (m_WriteLogs)
            return Log.w(tag, format(tag, msg));
        else return NOT_WRITTEN;
    }

    static public int error(String tag, String msg){
        if (m_WriteLogs)
            return Log.e(tag, format(tag, msg));
        else return NOT_WRITTEN;
    }

    static public int assert_(String tag, String msg){
        if (m_WriteLogs)
            return Log.e(tag, format(tag, msg));
        else return NOT_WRITTEN;
    }
}
