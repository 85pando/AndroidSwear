package net.pando.androidswear;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;

import java.util.Random;

public class Settings extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener {

    private AppPrefStore prefStore;
    private Intent backgroundIntent;

    private SeekBar seekBarMinTime;
    private EditText editTextMinTime;
    private int minTime;

    private SeekBar seekBarMaxTime;
    private EditText editTextMaxTime;
    private int maxTime;

    private CheckBox checkBoxSwearNegative;
    private CheckBox checkBoxSwearNeutral;
    private CheckBox checkBoxSwearPositive;
    private Boolean swearNegative;
    private Boolean swearNeutral;
    private Boolean swearPositive;

    private Button buttonStopService;
    private Button buttonStartService;

    private String[] negative;
    private String[] neutral;
    private String[] positive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //persistent storage of variables
        prefStore = new AppPrefStore(this);

        //get default/current values for persisted values
        minTime = prefStore.getMinTime();
        maxTime = prefStore.getMaxTime();
        swearNegative = prefStore.getNegative();
        swearNeutral = prefStore.getNeutral();
        swearPositive = prefStore.getPositive();

        editTextMinTime = (EditText) findViewById(R.id.editMinTime);
        editTextMinTime.setText(Integer.toString(minTime));
        seekBarMinTime = (SeekBar) findViewById(R.id.seekBarMinTime);
        seekBarMinTime.setProgress(minTime);
        seekBarMinTime.setOnSeekBarChangeListener(this);

        editTextMaxTime = (EditText) findViewById(R.id.editMaxTime);
        editTextMaxTime.setText(Integer.toString(maxTime));
        seekBarMaxTime = (SeekBar) findViewById(R.id.seekBarMaxTime);
        seekBarMaxTime.setProgress(maxTime);
        seekBarMaxTime.setOnSeekBarChangeListener(this);

        checkBoxSwearNegative = (CheckBox) findViewById(R.id.checkBoxSwearNegative);
        checkBoxSwearNeutral  = (CheckBox) findViewById(R.id.checkBoxSwearNeutral);
        checkBoxSwearPositive = (CheckBox) findViewById(R.id.checkBoxSwearPositive);
        checkBoxSwearNegative.setChecked(swearNegative);
        checkBoxSwearNeutral.setChecked(swearNeutral);
        checkBoxSwearPositive.setChecked(swearPositive);

        buttonStopService  = (Button) findViewById(R.id.buttonStopService);
        buttonStartService = (Button) findViewById(R.id.buttonStartService);

        Resources res = getResources();
        negative = res.getStringArray(R.array.negative);
        neutral  = res.getStringArray(R.array.neutral);
        positive = res.getStringArray(R.array.positive);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // save values to persistent storage
        saveValues();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // reload values from persisted storage
        minTime = prefStore.getMinTime();
        maxTime = prefStore.getMaxTime();
        swearNegative = prefStore.getNegative();
        swearNeutral = prefStore.getNeutral();
        swearPositive = prefStore.getPositive();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // don't create menu
        // getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(seekBar.equals(seekBarMinTime)) {
            //Minimum Bar has changed
            if(progress > maxTime)
                maxTime = progress;
            minTime = progress;
        }
        if(seekBar.equals(seekBarMaxTime)) {
            // Maximum Bar has changed
            if(progress < minTime)
                minTime = progress;
            maxTime = progress;
        }
        editTextMinTime.setText(Integer.toString(minTime));
        editTextMaxTime.setText(Integer.toString(maxTime));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        saveValues();
        seekBarMinTime.setProgress(minTime);
        seekBarMaxTime.setProgress(maxTime);
    }

    public void saveValues() {
        prefStore.putMinTime(minTime);
        prefStore.putMaxTime(maxTime);
        prefStore.putNegative(swearNegative);
        prefStore.putNeutral(swearNeutral);
        prefStore.putPositive(swearPositive);
//        prefStore.commit();
    }

    public void onCheckBoxPressed(View view){
        if(view.equals(checkBoxSwearNegative))
            swearNegative = checkBoxSwearNegative.isChecked();
        if(view.equals(checkBoxSwearNeutral))
            swearNeutral = checkBoxSwearNeutral.isChecked();
        if(view.equals(checkBoxSwearPositive))
            swearPositive = checkBoxSwearPositive.isChecked();
    }

    public void sendSwear(View view) {
        if(!swearNegative && !swearNeutral && !swearPositive) {
            Resources res = getResources();
            SwearNotification.notify(this, res.getString(R.string.noTypeSelected), -1);
            return;
        }

        Random rand = new Random();
        int category;
        while(true) { //loop is broken once a category is found
            category = rand.nextInt(3);
            if(category == 0 && swearNegative){
                SwearNotification.notify(this,negative[rand.nextInt(negative.length)], category);
                break;
            }
            if(category == 1 && swearNeutral){
                SwearNotification.notify(this,neutral[rand.nextInt(neutral.length)], category);
                break;
            }
            if(category == 2 && swearPositive){
                SwearNotification.notify(this,positive[rand.nextInt(positive.length)], category);
                break;
            }
        }
    }

    public void startService(View view){
        // start background service to send notifications again and again
        saveValues();
        if (backgroundIntent != null){
            getApplicationContext().stopService(backgroundIntent);
        }
        backgroundIntent = new Intent(getApplicationContext(), BackgroundNotificationService.class);
        getApplicationContext().startService(backgroundIntent);
        buttonStopService.setEnabled(true);
        buttonStartService.setEnabled(false);
    }

    public void stopService(View view){
        if(backgroundIntent != null){
            getApplicationContext().stopService(backgroundIntent);
        }
        buttonStopService.setEnabled(false);
        buttonStartService.setEnabled(true);
    }
}
