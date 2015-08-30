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

    static public DateFormat m_DateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);;
    static public Date m_Date = new Date();

    static public String format(String tag, String msg){
        return "||" /*+ m_DateFormat.format(m_Date)*/ + "||" + "BRAINY" + "|| " + "|| " + tag + " || " + msg;
    }

    static public int info(String tag, String msg){
        return Log.v(tag, format(tag, msg));
    }

    static public int debug(String tag, String msg){
        return Log.d(tag, format(tag, msg));
    }

    static public int verbose(String tag, String msg){
        return Log.v(tag, format(tag, msg));
    }

    static public int warn(String tag, String msg){
        return Log.w(tag, format(tag, msg));
    }

     static public int error(String tag, String msg){
        return Log.e(tag, format(tag, msg));
    }

    static public int assert_(String tag, String msg){
        return Log.e(tag, format(tag, msg));
    }
}
