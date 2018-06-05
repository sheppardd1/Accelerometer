package com.example.davea.accelerometer;

//import android.app.Activity;
//import android.graphics.SumPathEffect;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
//import android.media.MediaCas;
//import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import java.util.zip.DeflaterOutputStream;

public class MainActivity extends AppCompatActivity implements SensorEventListener {



    public Button BtnStart, BtnClear;
    public TextView TVData;
    public TextView TVAverage;
    public float xAv;
    public float yAv;
    public float zAv;  //accelerometer values with no decimal place
    public Sensor accelerometer;
    public SensorManager sensorManager;
    public boolean on = true;
    //    long begin_time_ms, begin_time_ms2;
    public boolean finishedThread = true;
    Bundle dataBundle = new Bundle();
    public float x, y, z;
    public long lastUpdateTime = 0;
    final public int INTERVAL = 1000;
    public int i = 0;

/*    Handler dataHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            TextView TVData;
            TVData= findViewById(R.id.data);
            Bundle bundle = msg.getData();
            String string = bundle.getString("key1");
            Log.d("QQQQ", string);
            TVData.setText(string);
            return false;
        }
    });

    Handler averageHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            TextView TVAverage;
            TVAverage= findViewById(R.id.Averages);
            Bundle bundle = msg.getData();
            String string = bundle.getString("key2");
            TVAverage.setText(string);
            return false;
        }
    });*/

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

        //getAccelerometerData();

    }


    public void setup() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        assert sensorManager != null;   //ensures next line does not return null pointer exception
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //register sensor listener
        sensorManager.registerListener(this, accelerometer, sensorManager.SENSOR_DELAY_NORMAL);

        BtnStart = findViewById(R.id.Start);
        BtnClear = findViewById(R.id.Clear);

        TVData  = findViewById(R.id.data);
        TVAverage  = findViewById(R.id.Averages);
    }

    public void registerUnregister() {
        sensorManager.registerListener(this, accelerometer, sensorManager.SENSOR_DELAY_NORMAL);
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 100) {
            if (System.currentTimeMillis() - startTime >= 100) {
                sensorManager.unregisterListener(this);
            }
        }
    }


    private double pythagorean(double a, double b) {
        return (Math.sqrt((a * a) + (b * b)));
    }//not used yet

 /*   @Override
    public void onSensorChanged(SensorEvent event) {
        float float1 = 0;   //placeholders. will come back to this
        float float2 = 0;
        //new GetAccelrData().execute(event, event, event);   //????
    }*/

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not used, but must be included for this to work
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(System.currentTimeMillis() - lastUpdateTime > INTERVAL && on) {
            lastUpdateTime = System.currentTimeMillis();

            x = event.values[0];
            y = event.values[1];
            z = event.values[2];

            //compute collective/cumulative average instead of accumulating values and later getting average
            xAv = (xAv * i + x) / (i + 1);
            yAv = (yAv * i + y) / (i + 1);
            zAv = (zAv * i + z) / (i + 1);

            if(i == 4){
            }
                TVAverage.setText("X: " + xAv + "\n" + "Y: " + yAv + "\n" + "Z: " + zAv);
                i = 0;
                xAv = yAv = zAv = 0;
            }
            else i++;

            TVData.setText("X: " + x + "\n" + "Y: " + y + "\n" + "Z: " + z + "\n" + "i: " + i);



        }

    }


/*    public void getAccelerometerData(){
        //sensorManager.registerListener(this, accelerometer, sensorManager.SENSOR_DELAY_NORMAL);
        //sensorManager.unregisterListener(this);

                int i = 0;
                while(on){
                    long startTime = System.currentTimeMillis();

                    //if(System.currentTimeMillis() - startTime >= 250){
                    registerUnregister();

                      //compute collective/cumulative average instead of accumulating values and later getting average
                      xAv = (xAv * i + x) / (i + 1);
                      yAv = (yAv * i + y) / (i + 1);
                      zAv = (zAv * i + z) / (i + 1);

                      if (i == 3) {   //averaging 4 values at a time

                          TVData.setText("X: " + xAv + "\n" + "Y: " + yAv + "\n" + "Z: " + zAv);
                          TVAverage.setText("X: " + xAv + "\n" + "Y: " + yAv + "\n" + "Z: " + zAv);

                          i = 0;
                      }
                        else i++;

                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //}   //end if time


                }//end while
        }

}*/

/*    public void getAccelerometerData(){
        //sensorManager.registerListener(this, accelerometer, sensorManager.SENSOR_DELAY_NORMAL);
        //sensorManager.unregisterListener(this);

        Runnable runnable = new Runnable() {
            public void run() {
                Message dataMessage = dataHandler.obtainMessage();
                Message averageMessage = averageHandler.obtainMessage();
                Bundle dataBundle = new Bundle();
                Bundle averageBundle = new Bundle();
                int i = 0;
                while(on){

                    registerUnregister();

                    //compute collective/cumulative average instead of accumulating values and later getting average
                    xAv = (xAv * i + x) / (i + 1);
                    yAv = (yAv * i + y) / (i + 1);
                    zAv = (zAv * i + z) / (i + 1);

                    //TVData.setText("X: " + x + "\n" + "Y: " + y + "\n" + "Z: " + z);//maybe use handler for this???
                    dataBundle.putString("key1", "X: " + x + "\n" + "Y: " + y + "\n" + "Z: " + z);
                    dataMessage.setData(dataBundle);
                    Bundle bundle = dataMessage.getData();
                    String string = bundle.getString("key1");
                    dataHandler.sendMessage(dataMessage);
                    //Log.d("HandlerIssue3", string);
                    //Log.d("QQQQQ", String.valueOf(Looper.myQueue()));
                    Log.d("HandlerIssue1", String.valueOf(dataBundle));
                    Log.d("HandlerIssue2", String.valueOf(dataMessage));


                    if (i == 3) {   //averaging 4 values at a time
                        //TVAverage.setText("X: " + xAv + "\n" + "Y: " + yAv + "\n" + "Z: " + zAv);

                        averageBundle.putString("key2", "X: " + xAv + "\n" + "Y: " + yAv + "\n" + "Z: " + zAv);
                        averageMessage.setData(averageBundle);
                        averageHandler.sendMessage(averageMessage);

                        i = 0;
                    }
                    else i++;

                    try {
                        Thread.sleep(150);  //thread "sleeps" for total of 1/4 second (150 here + 100 in registerUnregister
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }   //end while
                finishedThread = true;  //ensures that only one instance of the thread is running at once

            }//end run()
        };//end thread

        while(on && finishedThread) {
            finishedThread = false;
            xAv = yAv = zAv = 0;    //set averages to 0
            Thread getAccelerometerDataThread = new Thread(runnable);
            getAccelerometerDataThread.start();
        }
    }
}*/


/*    @Override
    public void onSensorChanged(final SensorEvent event) {



        Runnable runnable = new Runnable() {
            public void run() {
                Message dataMessage = dataHandler.obtainMessage();
                Message averageMessage = averageHandler.obtainMessage();
                Bundle dataBundle = new Bundle();
                Bundle averageBundle = new Bundle();
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

                    //TVData.setText("X: " + x + "\n" + "Y: " + y + "\n" + "Z: " + z);//maybe use handler for this???

                    dataBundle.putString("X: " + x + "\n" + "Y: " + y + "\n" + "Z: " + z, "key1");
                    dataMessage.setData(dataBundle);
                    dataHandler.sendMessage(dataMessage);

                    if (i == 3) {   //averaging 4 values at a time
                        //TVAverage.setText("X: " + xAv + "\n" + "Y: " + yAv + "\n" + "Z: " + zAv);

                        averageBundle.putString("X: " + xAv + "\n" + "Y: " + yAv + "\n" + "Z: " + zAv, "key2");
                        averageMessage.setData(averageBundle);
                        averageHandler.sendMessage(averageMessage);

                        i = 0;
                    }
                    else i++;

                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }   //end while
                finishedThread = true;

            }//end run()
        };//end thread

        if(on && finishedThread) {
            finishedThread = false;
            xAv = yAv = zAv = 0;    //set averages to 0
            Thread getAccelerometerDataThread = new Thread(runnable);
            getAccelerometerDataThread.start();
        }



    }//end OnSensorChanged()

}*/



 /*   public void getAverage() {
        float x, y, z;
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];

        //compute collective/cumulative average instead of accumulating values and later getting average
        xAv = (xAv * i + x) / (i + 1);
        yAv = (yAv * i + y) / (i + 1);
        zAv = (zAv * i + z) / (i + 1);

        TVData.setText("X: " + x + "\n" + "Y: " + y + "\n" + "Z: " + z);
    }*/



//maybe keep using thread, but put handler inside of thread to deal with .setText()

/*
    private class GetAccelrData extends AsyncTask <SensorEvent, Float, Float> {


        @Override
        protected Float doInBackground(SensorEvent... event) {
            int i = 0;
            float x, y, z;
            float xAv = yAv = zAv = 0;

            while(on) {
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
                } else i++;

                try {
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Float... progress){

        }

        @Override
        protected void onPostExecute(Float result){

        }
    }*/




//will look at this more:
// https://stackoverflow.com/questions/26260838/how-to-print-accelerometer-values-every-x-seconds-in-android
/*    @Override
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

                        TVData.setText("X: " + x + "\n" + "Y: " + y + "\n" + "Z: " + z);//maybe use handler for this???

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

    }//end OnSensorChanged()*/


    //OLD Project:*/
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


