package com.example.ilhamrofiqi.uangkas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalshscreen);

        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(2000); // 1000 = 1 detik
                } catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    startActivity(new Intent(Splashscreen.this, MainActivity.class));
                    finish(); // menutup aplikasi
                }
            }
        };

        thread.start();
    }
}
