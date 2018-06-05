package com.example.davea.accelerometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //UI:
    public Button BtnStart, BtnClear;
    public TextView TVData;
    public TextView TVAverage;

    //variables:
    public float xAv;
    public float yAv;
    public float zAv;
    public float x, y, z;
    public long lastUpdateTime = 0;
    public int i = 0;
    public boolean on = true;

    //sensors:
    public Sensor accelerometer;
    public SensorManager sensorManager;
    public SensorManager RVSensorManager;
    public Sensor RVSensor;

    //constants:
    final public int INTERVAL = 200;
    final public int NUMBER_OF_POINTS_TO_AVERAGE = 5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setup();    //assign and setup everything


        BtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                on = !on;
                i = 0;
            }
        });

        BtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                on = false;
                i = 0;
                TVData.setText("");
                TVAverage.setText("");
            }
        });
    }

    public void setup() {
        //set up accelerometer:
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        assert sensorManager != null;   //ensures next line does not return null pointer exception
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, sensorManager.SENSOR_DELAY_NORMAL);

        //set up rotation vector sensor:
        RVSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert RVSensorManager != null;
        RVSensor = RVSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        RVSensorManager.registerListener(this, RVSensor, sensorManager.SENSOR_DELAY_NORMAL);

        BtnStart = findViewById(R.id.Start);
        BtnClear = findViewById(R.id.Clear);

        TVData = findViewById(R.id.data);
        TVAverage = findViewById(R.id.Averages);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not used, but must be included for this to work
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (System.currentTimeMillis() - lastUpdateTime > INTERVAL && on) {
            lastUpdateTime = System.currentTimeMillis();

            x = event.values[0];
            y = event.values[1];
            z = event.values[2];

            //compute collective/cumulative average instead of accumulating values and later getting average
            xAv = (xAv * i + x) / (i + 1);
            yAv = (yAv * i + y) / (i + 1);
            zAv = (zAv * i + z) / (i + 1);

            if (i >= NUMBER_OF_POINTS_TO_AVERAGE - 1) {
                TVAverage.setText("X: " + xAv + "\n" + "Y: " + yAv + "\n" + "Z: " + zAv);
                i = 0;
                xAv = yAv = zAv = 0;
            } else i++;

            TVData.setText("X: " + x + "\n" + "Y: " + y + "\n" + "Z: " + z + "\n" + "i: " + i);

        }

    }

    private double pythagorean(double a, double b) {
        return (Math.sqrt((a * a) + (b * b)));
    }//not used yet

}

