package edu.orangecoastcollege.cs273.nhoang53.occlibrary2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/** Shake Detector for shake event
 * Created by ltruong58 on 10/27/2016.
 */

public class ShakeDetector implements SensorEventListener {
    // Constant to represent a shake threshold
    private static final float SHAKE_THRESHOLD = 25f;

    //Constant to represent how long bw shakes
    private static final int SHAKE_TIME_LAPSE = 2000;

    // What was the last time the event occurred
    private long timeOfLastShake;

    //Define a listener to register onSHake events
    private OnShakeListener shakeListener;

    // Constructor to create a new SHakeDetector
    public ShakeDetector(OnShakeListener listener)
    {
        shakeListener = listener;
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            // Get the x, y , z values when this event occurs
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            // Compare all 3 values against gravity
            float gForceX = x - SensorManager.GRAVITY_EARTH;
            float gForceY = y - SensorManager.GRAVITY_EARTH;
            float gForceZ = z - SensorManager.GRAVITY_EARTH;

            // Compute sum of squares
            double vector = Math.pow(gForceX, 2.0) + Math.pow(gForceY, 2.0) + Math.pow(gForceZ, 2.0);

            //
            float gForce = (float) Math.sqrt(vector);

            if(gForce > SHAKE_THRESHOLD)
            {
                // Get the current time:
                long now = System.currentTimeMillis();

                // Compare to see if the current
                if(now - timeOfLastShake >= SHAKE_TIME_LAPSE)
                {
                    timeOfLastShake = now;
                    // Register a shake event
                    shakeListener.onShake();


                }


            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    // Define our own interface (method for other classes to implement called onShake()
    //It's the responsibility of MagicAnswerAct to implement
    public interface  OnShakeListener{
        void onShake();

    }
}
