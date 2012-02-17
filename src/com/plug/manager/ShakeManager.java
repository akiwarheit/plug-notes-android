package com.plug.manager;

import android.content.Context;
import android.hardware.*;

import java.util.ArrayList;

public final class ShakeManager implements SensorEventListener {

    private static final int FORCE_THRESHOLD = 1000;
    private static final int TIME_THRESHOLD = 100;
    private static final int SHAKE_TIMEOUT = 500;
    private static final int SHAKE_DURATION = 1000;
    private static final int SHAKE_COUNT = 3;

    private Context mContext;

    private SensorManager sensorMgr;
    private long lastUpdate = -1;
    private float last_x, last_y, last_z;


    private int shakeCount = 0;
    private long lastShake;
    private long lastForce;

    private ArrayList<ShakeEventListener> listeners;

    public ShakeManager(Context context) {
        mContext = context;
        listeners = new ArrayList<ShakeEventListener>();
        initSensorManager();
    }

    private void initSensorManager(){

        sensorMgr = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);

        boolean accelerometerSupported =
                sensorMgr.registerListener(this,
      			sensorMgr.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER),
                   		                    SensorManager.SENSOR_DELAY_GAME);

        if (!accelerometerSupported) {
            // Non accelerometer on this device.
            sensorMgr.unregisterListener(this);
        }
    }

    public void addListener(ShakeEventListener listener){
        listeners.add(listener);
    }

    public void onPause() {
        if (sensorMgr != null) {
            sensorMgr.unregisterListener(this,
                    sensorMgr.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER));
            sensorMgr = null;
        }
    }

    public void onResume(){
        initSensorManager();
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor mySensor = event.sensor;

        if (mySensor.getType() == SensorManager.SENSOR_ACCELEROMETER) {

            long curTime = System.currentTimeMillis();

            if ((curTime - lastForce) > SHAKE_TIMEOUT) {
              shakeCount = 0;
            }

            // Only allow one update every 100ms.
       	    if ((curTime - lastUpdate) > TIME_THRESHOLD) {
   	    		long diffTime = (curTime - lastUpdate);
   	    		lastUpdate = curTime;

               float x = event.values[SensorManager.DATA_X];
               float y = event.values[SensorManager.DATA_Y];
               float z = event.values[SensorManager.DATA_Z];

  	    		float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                if (speed > FORCE_THRESHOLD) {

                    if ((++shakeCount >= SHAKE_COUNT) && (curTime - lastShake > SHAKE_DURATION)) {
                      lastShake = curTime;
                      shakeCount = 0;
                      if (sensorMgr != null) {
                          for(ShakeEventListener listener : listeners) {
                            listener.onShakeEvent();
                          }
                      }
                    }

                    lastForce = curTime;
  	    		}

                lastForce = curTime;
                last_x = x;
                last_y = y;
                last_z = z;
      	    }
      	}
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public interface ShakeEventListener {
        public void onShakeEvent();
    }
}
