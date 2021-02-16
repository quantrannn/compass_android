package com.example.laban;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class calibrate extends AppCompatActivity {
    TextView tv,tv1,tv2,tv3,tv4;
    SensorManager ssm;
    ConstraintLayout c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate);
        tv = (TextView) findViewById(R.id.textView8);
        tv1 = (TextView) findViewById(R.id.textView9);
        tv2 = (TextView) findViewById(R.id.textView10);
        tv3 = (TextView) findViewById(R.id.textView11);
        c = (ConstraintLayout) findViewById(R.id.con);

        ssm = (SensorManager) getSystemService(SENSOR_SERVICE);

        Sensor ss = ssm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            SensorEventListener sslistener = new SensorEventListener() {
                @Override
                public void onSensorChanged(SensorEvent event) {
                    float x = Math.abs(event.values[0]);
                    float y = Math.abs(event.values[1]);
                    float z = Math.abs(event.values[2]);

                    tv1.setText(""+x);
                    tv2.setText(""+y);
                    tv3.setText(""+z);

                    if ((z <= 10 && z >= 9.8) && (x <= 0.2 && x >= 0) && (y <= 0.2 && y >= 0) ){
                        c.setBackgroundResource(R.color.green);
                        tv.setText("OK");


                    }
                    else {
                        tv.setBackgroundResource(R.color.red);
                        c.setBackgroundResource(R.color.backgr);
                        tv.setText("Lưu ý: Đặt điện thoại nằm cân bằng cho đến khi chuyển qua màu xanh");
                        
                    }
                }
                @Override
                public void onAccuracyChanged (Sensor ss,int accuracy){

                }

            };
            ssm.registerListener(sslistener,ss,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
