package com.example.laban;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ImageView compass_img;
    TextView txt_compass;
    int mAzimuth;
    private SensorManager mSensorManager;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;
    boolean haveSensor = false, haveSensor2 = false;
    float[] rMat = new float[9];
    float[] orientation = new float[3];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        compass_img =findViewById(R.id.img_compass);
        txt_compass =findViewById(R.id.txt_azimuth);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toast.makeText(this, "Để la bàn hoạt động chính xác hãy để đt nằm trên mặt bàn và để ở chế độ PORTRAIT", Toast.LENGTH_SHORT).show();
        start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Intent i = new Intent(MainActivity.this, instruction.class);
                startActivity(i);
                return true;
            case R.id.item2:
                Intent ii = new Intent(MainActivity.this,About.class);
                startActivity(ii);
                return true;
            case R.id.item3:
                Intent iii = new Intent(MainActivity.this,calibrate.class);
                startActivity(iii);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    SensorEventListener ssl=new SensorEventListener() {
        @Override//source/destination
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                SensorManager.getRotationMatrixFromVector(rMat, event.values);
                mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
            }
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
                mLastAccelerometerSet = true;
            }
            else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
                mLastMagnetometerSet = true;
            }
            if (mLastAccelerometerSet && mLastMagnetometerSet) {
                SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
                SensorManager.getOrientation(rMat, orientation);
                mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
            }

            mAzimuth = Math.round(mAzimuth);
            compass_img.setRotation(-mAzimuth);

            String where = "Hướng Bắc";

            if (mAzimuth >= 350 || mAzimuth <= 10)
                where = "Hướng Bắc";
            if (mAzimuth < 350 && mAzimuth > 280)
                where = "Hướng Tây Bắc";
            if (mAzimuth <= 280 && mAzimuth > 260)
                where = "Hướng Tây";
            if (mAzimuth <= 260 && mAzimuth > 190)
                where = "Hướng Tây Nam";
            if (mAzimuth <= 190 && mAzimuth > 170)
                where = "Hướng Nam";
            if (mAzimuth <= 170 && mAzimuth > 100)
                where = "Hướng Đông Nam";
            if (mAzimuth <= 100 && mAzimuth > 80)
                where = "Hướng Đông";
            if (mAzimuth <= 80 && mAzimuth > 10)
                where = "Hướng Đông Bắc";

            txt_compass.setText(mAzimuth + "° " + where);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    public void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
            if ((mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) || (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null)) {
                Toast.makeText(this, "no sensor", Toast.LENGTH_SHORT).show();
            }
            else {
                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                haveSensor = mSensorManager.registerListener(ssl, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                haveSensor2 = mSensorManager.registerListener(ssl, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
            }
        }
        else{
            mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = mSensorManager.registerListener(ssl, mRotationV, SensorManager.SENSOR_DELAY_UI);
//            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//            mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//            haveSensor = mSensorManager.registerListener(ssl, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
//            haveSensor2 = mSensorManager.registerListener(ssl, mMagnetometer, SensorManager.SENSOR_DELAY_UI);

        }
    }

    public void stop() {
        if (haveSensor) {
            mSensorManager.unregisterListener(ssl, mRotationV);
        }
        else {
            mSensorManager.unregisterListener(ssl, mAccelerometer);
            mSensorManager.unregisterListener(ssl, mMagnetometer);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
    }

}