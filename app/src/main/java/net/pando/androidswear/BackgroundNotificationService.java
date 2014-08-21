package net.pando.androidswear;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pandora on 14.08.14.
 */
public class BackgroundNotificationService extends IntentService {

    public BackgroundNotificationService() { super("BackgroundNotificationService"); };

    public BackgroundNotificationService(String name) { super(name); }

    boolean swearNegative;
    boolean swearNeutral;
    boolean swearPositive;

    int minTime;
    int maxTime;

    String[] negative;
    String[] neutral;
    String[] positive;

    Context context = this;

    Random rand;
    Timer swearTimer;

    @Override
    protected void onHandleIntent(Intent intent) {

        // load values from prefStore
        retrieveValues();
        Resources res = getResources();

        if(!swearNegative && !swearNeutral && !swearPositive){
            SwearNotification.notify(this, res.getString(R.string.noTypeSelected) + "No swears will follow!", -1);
            return;
        }

        negative = res.getStringArray(R.array.negative);
        neutral  = res.getStringArray(R.array.neutral);
        positive = res.getStringArray(R.array.positive);

        rand = new Random();

        swearTimer = new Timer("swearTimer", true);
        swearTimer.schedule(new swearTask(), getDelay());
//        SwearNotification.notify(context,"minTime:"+Integer.toString(minTime) + ", maxTime:"+Integer.toString(maxTime)+",delay:"+Long.toString(getDelay()),-1);

    }

    private void retrieveValues() {
        AppPrefStore prefStore = new AppPrefStore(this);

        minTime = prefStore.getMinTime();
        maxTime = prefStore.getMaxTime();

        swearNegative = prefStore.getNegative();
        swearNeutral  = prefStore.getNeutral();
        swearPositive = prefStore.getPositive();
    }

    private long getDelay() {
        AppPrefStore prefStore= new AppPrefStore(this);
        minTime = prefStore.getMinTime();
        maxTime = prefStore.getMaxTime();

        final long delay;

        if (minTime == maxTime){
            delay = maxTime*1000*60;
        } else {
            final long upperBound = (long) maxTime-minTime;
//            delay = (long) minTime + rand.nextLong() % upperBound;
            delay = (long) minTime + (long) (rand.nextDouble()*upperBound*1000*60);
//            delay = (long) (rand.nextDouble()*10000);
        }
        return delay;
    }

    private class swearTask extends TimerTask {

        public void run() {
            if (rand == null) {
                rand = new Random();
            }
            int category;
            while(true) { //break this loop after notification is sent
                category = rand.nextInt(3);
                if(category == 0 && swearNegative) {
                    SwearNotification.notify(context, negative[rand.nextInt(negative.length)], category);
                    break;
                }
                if(category == 1 && swearNeutral) {
                    SwearNotification.notify(context, neutral[rand.nextInt(neutral.length)], category);
                    break;
                }
                if(category == 2 && swearPositive) {
                    SwearNotification.notify(context, positive[rand.nextInt(positive.length)], category);
                    break;
                }
            }
            swearTimer.schedule(new swearTask(), getDelay());
        }
    }

}
