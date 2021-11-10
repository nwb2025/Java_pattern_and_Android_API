package com.example.android.java;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.android.java.utilities.ActivityHelper;
import com.example.android.java.utilities.AudioHelper;

import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements SensorEventListener,
        TextToSpeech.OnInitListener {

    private ScrollView mScroll;
    private EditText mLog;
    private Button mButtonClick, mButtonPlay, mButtonStop;

    private SensorManager sensor;
    private Sensor accelerometer;

    private long lastUpdate = 0;
    private float lastX,lastY,lastZ;
    private static final int SHAKE_THRESHOLD = 510;

    private TextToSpeech tts;
    private boolean ttsInitialized;

    // playing audio
    private AudioHelper mAudioHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      Initialize the logging components
        mScroll = (ScrollView) findViewById(R.id.scrollLog);
        mLog = (EditText) findViewById(R.id.tvLog);
        mLog.setText("");

        // sensor = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // accelerometer = sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        tts = new TextToSpeech(this, this);

        findViewById(R.id.btnRun).setOnClickListener((View view) -> onRunBtnClick());

        mButtonPlay = findViewById(R.id.btnPlay);
        mButtonStop = findViewById(R.id.btnStop);

        mButtonPlay.setOnClickListener(view -> onPlayBtnClick());

        mButtonStop.setOnClickListener(view -> onStopBtnClick());

    }

    public void onRunBtnClick() {
        String text = mLog.getText().toString();
        if (text.length() == 0){
            Toast.makeText(MainActivity.this, "What do you want to say?",
                    Toast.LENGTH_SHORT).show();
        } else {
            saySomething(text);
            displayMessage(text);
        }
    }

    // methods for playing audio
    public void onPlayBtnClick() {
        if (mAudioHelper != null){
            mAudioHelper.stop();
        }
        mAudioHelper = new AudioHelper(this, "music.mp3");
        mAudioHelper.prepareAndPlay();
        displayMessage("Music is playing !");
    }

    public void onStopBtnClick() {
        if (mAudioHelper != null){
            mAudioHelper.stop();
            displayMessage("Stopping !");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
       // sensor.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
       //  sensor.unregisterListener(this);
    }

    public void onClearBtnClick(View v) {
        mLog.setText("");
        mScroll.scrollTo(0, mScroll.getBottom());
    }


    public void displayMessage(String message) {
        ActivityHelper.log(this,mLog, message,false);
        mScroll.scrollTo(0, mScroll.getBottom());
    }



    // Methods for accelerometer
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long currentTime = System.currentTimeMillis();

            if ((currentTime - lastUpdate) > 300) {
                long diffTime = (currentTime - lastUpdate);
                lastUpdate = currentTime;

                float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD) {
                    displayMessage("Stop shaking me!");
                }

                lastX = x;
                lastY = y;
                lastZ = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    // TextToSpeech
    @Override
    public void onInit(int status) {
        if ( status == TextToSpeech.SUCCESS){
            int result = tts.setLanguage(Locale.CANADA);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                displayMessage("This language isn't supported");
            }else {
                ttsInitialized = true;
            }
        }else{
            displayMessage("tts initialization failed");
        }
    }

    private void saySomething(String speech){
        if(!ttsInitialized){
            displayMessage("TextToSpeech wasn't initialized!");
            return;
        }else {
            tts.speak(speech, TextToSpeech.QUEUE_FLUSH,null, "speech");
        }
    }
}