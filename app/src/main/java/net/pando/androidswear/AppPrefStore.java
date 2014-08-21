package net.pando.androidswear;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by pandora on 14.08.14.
 */
public class AppPrefStore {

    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;

    public AppPrefStore(Context context){
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        edit = this.prefs.edit();
    }

    public boolean putMinTime(int minTime) {
        if(minTime<0 || minTime > 60)
            return false;
        edit.putInt("minTime", minTime);
        return edit.commit();
    }

    public boolean putMaxTime(int maxTime) {
        if(maxTime<1 || maxTime>60)
            return false;
        edit.putInt("maxTime", maxTime);
        return edit.commit();
    }

    public void putNegative(boolean negative){
        edit.putBoolean("negative", negative);
        edit.commit();
    }
    public void putNeutral( boolean neutral ){
        this.prefs.edit().putBoolean("neutral",  neutral);
        edit.commit();
    }
    public void putPositive(boolean positive){
        this.prefs.edit().putBoolean("positive", positive);
        edit.commit();
    }

//    public boolean commit(){ return this.prefs.edit().commit(); }

    public int getMinTime(){ return this.prefs.getInt("minTime", 1); }
    public int getMaxTime(){ return this.prefs.getInt("maxTime", 2); }
    public boolean getNegative(){ return this.prefs.getBoolean("negative", true); }
    public boolean getNeutral(){  return this.prefs.getBoolean("neutral",  false); }
    public boolean getPositive(){ return this.prefs.getBoolean("positive", false); }
}
