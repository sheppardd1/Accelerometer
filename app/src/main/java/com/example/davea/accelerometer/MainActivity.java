package com.example.davea.accelerometer;

import android.graphics.SumPathEffect;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    public Button BtnStart, BtnClear;
    public TextView TVData, TVAverage;
    public float xAv;
    public float yAv;
    public float zAv;  //accelerometer values with no decimal place
    public Sensor accelerometer;
    public SensorManager sensorManager;
    public boolean on = true;
    long begin_time_ms, begin_time_ms2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setup();    //assign and setup everything

        BtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                on = !on;
            }
        });

        BtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                on = false;
                TVData.setText("");
                TVAverage.setText("");
            }
        });
    }

    public void setup(){
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //register sensor listener
        sensorManager.registerListener(this, accelerometer, sensorManager.SENSOR_DELAY_NORMAL);

        BtnStart = findViewById(R.id.Start);
        BtnClear = findViewById(R.id.Clear);
        TVData = findViewById(R.id.data);
        TVAverage = findViewById(R.id.Averages);
    }


    private double pythagorean(double a, double b){
        return (Math.sqrt((a * a) + (b * b)));
    }//not used yet

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not used, but must be included for this to work
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {

            xAv = yAv = zAv = 0;    //set averages to 0

            final Thread getAccelerometerData = new Thread() {
                public void run() {
                    int i = 0;
                    float x, y, z;
                    while(on){

                        x = event.values[0];
                        y = event.values[1];
                        z = event.values[2];
                        //compute collective/cumulative average instead of accumulating values and later getting average
                        xAv = (xAv * i + x) / (i + 1);
                        yAv = (yAv * i + y) / (i + 1);
                        zAv = (zAv * i + z) / (i + 1);

                        TVData.setText("X: " + x + "\n" + "Y: " + y + "\n" + "Z: " + z);

                        if (i == 3) {   //averaging 4 values at a time
                            TVAverage.setText("X: " + xAv + "\n" + "Y: " + yAv + "\n" + "Z: " + zAv);
                            i = 0;
                        }
                        else i++;

                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }   //end while

                }//end run()
            };//end thread

            if(on) getAccelerometerData.start();

    }//end OnSensorChanged()


    //OLD Project:
/*    @Override
    public void onSensorChanged(SensorEvent event) {
        if(on) {
            begin_time_ms = System.currentTimeMillis();  //for do-while loop
            begin_time_ms2 = System.currentTimeMillis();    //for if statement
            int i = 0;
            xAv = yAv = zAv = 0;    //set averages to 0
            float x, y, z;
            int count = 0;

            do {
                if (System.currentTimeMillis() - begin_time_ms2 > 1000) { //getting data every __ ms
                    begin_time_ms2 = System.currentTimeMillis();    //reset begin_time_ms2

                    x = event.values[0];
                    y = event.values[1];
                    z = event.values[2];
                    //compute collective average instead of accumulating values and later getting average
                    xAv = (xAv * i + x) / (i + 1);
                    yAv = (yAv * i + y) / (i + 1);
                    zAv = (zAv * i + z) / (i + 1);

                    i++;
                    TVData.setText("X: " + x + "\n" + "Y: " + y + "\n" + "Z: " + z);
                    count++;
                }

            }while (count < 5);//System.currentTimeMillis() - begin_time_ms <= 10000);    //averaging data over __ ms

            if(i != 0){
                updateScreenAverage(i);
            }

        }
    }*/
}
