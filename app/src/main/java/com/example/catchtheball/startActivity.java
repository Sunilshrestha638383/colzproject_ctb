package com.example.catchtheball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class startActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void startGame(View view) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));

    }

    public void Medium(View view) {
        startActivity(new Intent(getApplicationContext(),MediumLevel.class));
    }

    public void hard(View view) {
        startActivity(new Intent(getApplicationContext(),HardLevel.class));
    }

    public void Quit(View view) {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid()) ;
        System.exit(1);
    }
}