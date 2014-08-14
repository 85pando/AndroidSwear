package net.pando.androidswear;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by pandora on 14.08.14.
 */
public class AppPrefStore {

    private SharedPreferences prefs;

    public AppPrefStore(Context context){
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean putMinTime(int minTime) {
        if(minTime<0 || minTime > 60)
            return false;
        this.prefs.edit().putInt("minTime", minTime);
        return true;
    }

    public boolean putMaxTime(int maxTime) {
        if(maxTime<0 || maxTime>60)
            return false;
        this.prefs.edit().putInt("maxTime", maxTime);
        return true;
    }

    public void putNegative(boolean negative){ this.prefs.edit().putBoolean("negative",negative); }
    public void putNeutral(boolean neutral){ this.prefs.edit().putBoolean("neutral",neutral); }
    public void putPositive(boolean positive){ this.prefs.edit().putBoolean("positive",positive); }

    public boolean commit(){ return this.prefs.edit().commit(); }

    public int getMinTime(){ return this.prefs.getInt("minTime",10); }
    public int getMaxTime(){ return this.prefs.getInt("maxTime", 45); }
    public boolean getNegative(){ return this.prefs.getBoolean("negative", true); }
    public boolean getNeutral(){ return this.prefs.getBoolean("neutral", false); }
    public boolean getPositive(){ return this.prefs.getBoolean("positive", false); }
}
