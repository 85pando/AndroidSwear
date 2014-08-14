package net.pando.androidswear;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * Created by pandora on 14.08.14.
 */
public class SwearNotification {

    /**
     * Unique identifier for this type of notification
     */
    private static final String NOTIFICATION_TAG = "Swear";

    /**
     *
     * @param context
     * @param swear The text to send in notification.
     * @param type Is the notification a swear (0), neutral (1) or positive (1)? Notification pool empty (-1).
     */
    public static void notify(final Context context, final String swear, final int type) {
        final Resources res = context.getResources();

        // This image is used as the notification's large icon (thumbnail).
       final Bitmap picture = BitmapFactory.decodeResource(res, R.drawable.logo);

        final String intro = res.getString(R.string.notificationIntro);

        String text = intro + swear + ". ";
        String summary;
        switch(type) {
            case 0: text += res.getString(R.string.negativeText);
                summary = res.getString(R.string.negativeSummary);
                break;
            case 1: text += res.getString(R.string.neutralText);
                summary = res.getString(R.string.neutralSummary);
                break;
            case 2: text += res.getString(R.string.positiveText);
                summary = res.getString(R.string.positiveSummary);
                break;
            case -1: text = res.getString(R.string.emptyPoolText) + swear;
                summary = res.getString(R.string.emptyPoolSummary);
                break;
            default: text += "This text should never be readable." + swear;
                summary = "This text should never be readable.";
                break;
        }

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
            //set appropriate defaults for the notification light, sound and vibration
            .setDefaults(Notification.DEFAULT_ALL)
            // Set required fields, including small icon, notification title and text
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(swear)
            .setContentText(text)
            // Everything below is optional
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // TODO set good picture
            .setLargeIcon(picture)
            // set ticker text (preview)
            .setTicker(swear)
            // set number (for stacking notifications)
            //.setNumber(number)
            // set notification timestamp
            // .setWhen(...)
            // pending intent (what to do when clicking notification
            /*.setContentIntent(
                    PendingIntent.getActivity(context,0,new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com")),PendingIntent.FLAG_UPDATE_CURRENT)
            )*/
            // show expanded text for 4.1 or later
            .setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(text)
                    .setBigContentTitle(swear)
                    .setSummaryText(summary)
            )
            //automatically dismiss notification when touched
            .setAutoCancel(true);

        notify(context, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR)
            nm.notify(NOTIFICATION_TAG, 0, notification);
        else
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR)
            nm.cancel(NOTIFICATION_TAG, 0);
        else
            nm.cancel((NOTIFICATION_TAG.hashCode()));
    }
}
