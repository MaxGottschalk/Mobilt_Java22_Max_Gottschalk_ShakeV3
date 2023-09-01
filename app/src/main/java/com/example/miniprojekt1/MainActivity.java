package com.example.miniprojekt1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private FrameLayout frameLayout;
    private Sensor accelerometerSensor;
    FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the SensorManager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        fm = getSupportFragmentManager();
        frameLayout = findViewById(R.id.frameLayout2);

        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gyroscopeSensor == null) {
            Log.d("Gyro", "NULL ERROR");
        }

        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor == null) {
            Log.d("Acc", "NULL ERROR");
        }

    }

    private final SensorEventListener gyroscopeListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            float xRotationRate = event.values[0];
            float yRotationRate = event.values[1];
            float zRotationRate = event.values[2];

            // Log the gyro data
            Log.d("Gyroscope", "X: " + xRotationRate + ", Y: " + yRotationRate + ", Z: " + zRotationRate);

            TextView textY = findViewById(R.id.textView);
            textY.setText(String.format(Locale.ROOT, "Y: %.1f", yRotationRate));
            TextView textX = findViewById(R.id.textView3);
            textX.setText("X: " + xRotationRate);
            TextView textZ = findViewById(R.id.textView4);
            textZ.setText("Z: " + zRotationRate);


            float oldRotX = frameLayout.getRotationX();
            float oldRotY = frameLayout.getRotationY();

            if(xRotationRate > 0 || yRotationRate > 0){
                fm.beginTransaction().add(R.id.frameLayout2, BlankFragment.class, null).commit();

                frameLayout.setRotationX(oldRotX + xRotationRate * 4);
                frameLayout.setRotationY(oldRotY + yRotationRate * 4);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private final SensorEventListener accelerometerListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            float xAcceleration = event.values[0];
            float yAcceleration = event.values[1];
            float zAcceleration = event.values[2];

            // Log acc data
            Log.d("Accelerometer", "X: " + xAcceleration + ", Y: " + yAcceleration + ", Z: " + zAcceleration);

           if (xAcceleration > 10 || yAcceleration > 15) {
                fm.beginTransaction().add(R.id.frameLayout, BlankFragment.class, null).commit();
                findViewById(R.id.frameLayout).setVisibility(View.VISIBLE);
            }else{
               findViewById(R.id.frameLayout).setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // Register the gyroscope listener when the activity is in the foreground
        sensorManager.registerListener(gyroscopeListener, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
        // Register the accelerometer listener when the activity is in the foreground
        sensorManager.registerListener(accelerometerListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the gyroscope listener when the activity is in the background
        sensorManager.unregisterListener(gyroscopeListener);
        // Unregister the accelerometer listener when the activity is in the background
        sensorManager.unregisterListener(accelerometerListener);
    }
}