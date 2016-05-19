package timmycheng.countdown;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Pref extends PreferenceActivity {
    public static final String PREF = "COUNTDOWN_PREF";
    public static final String PREF_MIN = "PREF_MIN";
    public static final String PREF_SEC = "PREF_SEC";
    public static final String PREF_REMAIN = "PREF_REMAIN";
    //load minute
    public static String getMin(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(PREF_MIN,"");
    }
    //load second
    public static String getSec(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(PREF_SEC,"");
    }
    //load timeremain
    public static String getRemain(Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(PREF_REMAIN,"");
    }
    //store minute
    public static void setMin(Context context, String minute) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PREF_MIN, minute);
        editor.commit();
    }
    //store second
    public static void setSec(Context context, String second) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PREF_SEC, second);
        editor.commit();
    }
    //store timeremain
    public static void setRemain(Context context, String remain) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PREF_REMAIN, remain);
        editor.commit();
    }
}

