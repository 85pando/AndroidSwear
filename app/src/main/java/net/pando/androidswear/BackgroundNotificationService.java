package net.pando.androidswear;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import java.util.Random;

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

    @Override
    protected void onHandleIntent(Intent intent) {

        // load values from prefStore
        AppPrefStore prefStore = new AppPrefStore(this);
        Resources res = getResources();

        swearNegative = prefStore.getNegative();
        swearNeutral  = prefStore.getNeutral();
        swearPositive = prefStore.getPositive();

        if(!swearNegative && !swearNeutral && !swearPositive){
            SwearNotification.notify(this, res.getString(R.string.noTypeSelected) + "No notifications will follow!", -1);
            return;
        }

        minTime = prefStore.getMinTime();
        maxTime = prefStore.getMaxTime();

        negative = res.getStringArray(R.array.negative);
        neutral  = res.getStringArray(R.array.neutral);
        positive = res.getStringArray(R.array.positive);

        Random rand = new Random();
        int category;
        while(true) { //break this loop after notification is sent
            category = rand.nextInt(3);
            if(category == 0 && swearNegative) {
                SwearNotification.notify(this, negative[rand.nextInt(negative.length)], category);
                break;
            }
            if(category == 1 && swearNeutral) {
                SwearNotification.notify(this, neutral[rand.nextInt(neutral.length)], category);
                break;
            }
            if(category == 2 && swearPositive) {
                SwearNotification.notify(this, positive[rand.nextInt(positive.length)], category);
                break;
            }
        }
    }
}
