package com.example.catchtheball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class HardLevel extends AppCompatActivity {

    //Elements
    private TextView scoreLabel, startLabel;
    private ImageView dog, orange, pink,black;

    //size
    private int screenHeight, screenWidth;
    private int frameHeight;
    private int dogSize;


    //position
    private float dogY;
    private float orangeX, orangeY;
    private float pinkX, pinkY;
    private float blackX, blackY;

    //score
    private int score;

    //speed
    private int dogSpeed, orangeSpeed, pinkSpeed,blackSpeed;

    //timer
    private Timer timer= new Timer();
    private Handler handler=new Handler();

    //status
    private boolean actoin_flg= false;
    private boolean start_flg=false;

    //soundplayer
    private soundPlayer soundPlayer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundPlayer = new soundPlayer(this);
        scoreLabel=findViewById(R.id.Scorelabel);
        startLabel=findViewById(R.id.Startlabel);
        dog=findViewById(R.id.dog);
        orange=findViewById(R.id.orange);
        pink=findViewById(R.id.pink);
        black=findViewById(R.id.black);

        //screen size
        WindowManager windowManager=getWindowManager();
        Display display=windowManager.getDefaultDisplay();
        Point size= new Point();
        display.getSize(size);

        screenWidth=size.x;
        screenHeight=size.y;

        //Nexus 4 width:768 height:1184
        //speed dog:20 pink:12 orange:20 black:16
        dogSpeed = Math.round(screenHeight / 60.0f); //1184/60 = 19.733.... =>20
        orangeSpeed = Math.round(screenWidth / 20.0f); // 768/60 =12.8..... => 13
        pinkSpeed = Math.round(screenWidth / 10.0f);  //768/36/ = 21.333 => 21
        blackSpeed = Math.round(screenWidth / 15.0f); //768/45 = 17.06..... => 17

//        Log.v("SPEED_DOG", dogSpeed + "");
//        Log.v("SPEED_ORANGE", orangeSpeed + "");
//        Log.v("SPEED_PINK", pinkSpeed + "");
//        Log.v("SPEED_BLACK", blackSpeed + "");


        //initial position
        orange.setX(-80.0f);
        orange.setY(-80.0f);
        pink.setX(-80.0f);
        pink.setY(-80.0f);
        black.setX(-80.0f);
        black.setY(-80.0f);

        scoreLabel.setText("score: "+ score);


    }
    public void changePos(){

        //hitcheck
        hitCheck();

        //orange
        orangeX -= orangeSpeed;
        if(orangeX<0){
            orangeX = screenWidth+20;
            orangeY=(float)Math.floor(Math.random() * (frameHeight-orange.getHeight()));
        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        //black
        blackX -= blackSpeed;
        if(blackX<0){
            blackX = screenWidth+10;
            blackY = (float)Math.floor(Math.random() * (frameHeight-orange.getHeight()));
        }
        black.setX(blackX);
        black.setY(blackY);

        //pink
        pinkX -= pinkSpeed;
        if(pinkX<0){
            pinkX =screenWidth + 5000;
            pinkY= (float)Math.floor(Math.random() * (frameHeight-orange.getHeight()));
        }
        pink.setX(pinkX);
        pink.setY(pinkY);




        if (actoin_flg) {
            //touching
            dogY -= dogSpeed;
        }else
        {
            //Releasing
            dogY += dogSpeed;
        }
        if(dogY <0)dogY=0;
        if(dogY > frameHeight-dogSize) dogY=frameHeight-dogSize;
        dog.setY(dogY);

        scoreLabel.setText("score: "+ score);

    }

    public void hitCheck(){

        //orange
        float orangeCenterX = orangeX+orange.getWidth()/2.0f;
        float orangeCenterY = orangeY+orange.getHeight()/2.0f;

        if(0<=orangeCenterX && orangeCenterX <= dogSize &&
                dogY <=orangeCenterY && orangeCenterY <= dogY+ dogSize){
            orangeX= -100.0f;
            score += 10;
            soundPlayer.playHitSound();
        }

        //pink
        float pinkCenterX= pinkX+pink.getWidth()/2.0f;
        float pinkCenterY = pinkY+pink.getHeight()/2.0f;
        if(0<=pinkCenterX && pinkCenterX <= dogSize &&
                dogY <=pinkCenterY && pinkCenterY <= dogY+ dogSize){

            pinkX= -100.0f;
            score += 30;
            soundPlayer.playHitSound();


        }

        //black
        float blackCenterX = blackX + black.getWidth() /2.0f;
        float blackCenterY = blackY + black.getHeight()/2.0f;
        if(0<=blackCenterX && blackCenterX <= dogSize &&
                dogY <=blackCenterY && blackCenterY <= dogY+ dogSize){

            soundPlayer.playOverSound();
            //game over
            if(timer != null){
                timer.cancel();
                timer = null;
            }

            //show resultActivity
            Intent intent = new Intent(getApplicationContext(),ResultActivity.class);
            intent.putExtra("score", score);
            startActivity(intent);

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!start_flg) {
            start_flg=true;

            //Frameheight
            FrameLayout frameLayout= findViewById(R.id.frame);
            frameHeight=frameLayout.getHeight();
            //dog
            dogY=dog.getY();
            dogSize=dog.getHeight();



            startLabel.setVisibility(View.GONE);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            },0,20);

        }else{
            if(event.getAction()==MotionEvent.ACTION_DOWN){
                actoin_flg=true;
            }else if (event.getAction()==MotionEvent.ACTION_UP){
                actoin_flg=false;
            }

        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),startActivity.class));
    }
}
