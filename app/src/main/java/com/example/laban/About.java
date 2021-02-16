package com.example.laban;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;

public class About extends AppCompatActivity {
TextView t5,t6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        t5=findViewById(R.id.textView5);
        t6=findViewById(R.id.textView6);

        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/hongquannn1/"));
                startActivity(i);
            }

        });
        t6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num="tel: "+t6.getText().toString();
                Intent i= new Intent(Intent.ACTION_DIAL,Uri.parse(num));
                startActivity(i);
            }

        });
    }

}