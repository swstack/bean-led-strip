package com.bean.swstack.beanledstrip;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private BeanLEDStrip beanLedStrip;

    ToggleButton powerButton;
    SeekBar intensitySeekBar;
    SeekBar colorSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        powerButton = (ToggleButton) findViewById(R.id.powerButton);
        intensitySeekBar = (SeekBar) findViewById(R.id.intensitySeekBar);
        colorSeekBar = (SeekBar) findViewById(R.id.colorSeekBar);

        powerButton.setOnCheckedChangeListener(powerChangeListener);
        intensitySeekBar.setOnSeekBarChangeListener(intensityChangeListener);
        colorSeekBar.setOnSeekBarChangeListener(colorChangeListener);

        beanLedStrip = new BeanLEDStrip(this, (TextView) findViewById(R.id.connectStatusText));
        beanLedStrip.connect();

    }

    private void reset() {
        powerButton.setChecked(false);
    }

    private CompoundButton.OnCheckedChangeListener powerChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.d(TAG, "Power button changed to " + isChecked);
        }
    };

    private SeekBar.OnSeekBarChangeListener colorChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Log.d(TAG, "Color seek bar changed to " + progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private SeekBar.OnSeekBarChangeListener intensityChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Log.d(TAG, "Intensity seek bar changed to " + progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.refresh_beans_setting) {
            this.beanLedStrip.reset();
            this.reset();
        }

        return super.onOptionsItemSelected(item);
    }
}
