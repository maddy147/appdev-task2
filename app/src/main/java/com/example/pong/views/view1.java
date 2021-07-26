package com.example.pong.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class view1 extends View{

    /*@SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        if(in_range(event.getX(),0,1400)){
            slider.x = event.getX();
            postInvalidate();
        }
        return false;
    }*/

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if(in_range(event.getX(),0,1400)){
                    slider.x = event.getX();
                    //postInvalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    public static class Ball{
        public float dir_x =  1;
        public float dir_y = -1;
        public float x, y, size;
        public float speed = 5;
        public Paint paint;
        public RectF oval;

        public Ball(int x, int y, int size, int color) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.paint = new Paint();
            this.paint.setColor(color);
        }

        public void move(Canvas canvas) {
            this.x += speed * dir_x;
            this.y += speed * dir_y;
            this.oval = new RectF(x - size / 2, y - size / 2, x + size / 2, y + size / 2);

            Rect bounds = new Rect();
            this.oval.roundOut(bounds);

            if (!canvas.getClipBounds().contains(bounds)) {
                if (this.x - size < 0 || this.x + size > canvas.getWidth()) {
                    dir_x = dir_x * -1;
                    song.start();
                }
                if (this.y - size < 0) {
                    dir_y = dir_y * -1;
                    song.start();
                }//|| this.y+size > canvas.getHeight()
                /*if (this.y + size > canvas.getHeight()) {
                    dir_y = dir_y * -1;
                }*/
            }
        }
        public  void  reset(){
            this.x = 375;
            this.y = 975;
            this.size = 25 ;
        }
    }

    public static class Slider{
        public float x;
        //public int dir = 1;
        //public float speed = 5;
        public Paint pt;
        public RectF rectF;

        public Slider(int x, int color) {
            this.x = x;
            this.pt = new Paint(Paint.ANTI_ALIAS_FLAG);
            this.pt.setColor(color);
            this.rectF = new RectF();
        }

        /*public void move(Canvas canvas) {
            this.x += speed * dir;
            if (x + 250 > canvas.getWidth() || x <= 0) {
                dir *= -1;
            }
        }*/
    }

    public static Ball ball;
    public static Slider slider;
    public static int score = 0 ;
    public static boolean game_ovr = false;
    public static MediaPlayer song;

    public view1(Context context) {
        super(context);
        ini();
    }

    public view1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ini();
    }

    public view1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ini();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public view1(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        ini();
    }

    private void ini() {
        ball = new Ball(375, 975, 25, Color.WHITE);
        slider = new Slider(375, Color.GRAY);
    }



    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.rgb(33, 31, 31));

        slider.rectF.left =  slider.x;
        slider.rectF.top = 1750;
        slider.rectF.right = slider.rectF.left + 300;
        slider.rectF.bottom = slider.rectF.top + 30;

        canvas.drawCircle(ball.x, ball.y, ball.size, ball.paint);
        canvas.drawRect(slider.rectF, slider.pt);


        if(!game_ovr){
            ball.move(canvas);
        }

        if(ball.oval.intersect(slider.rectF)){
            ball.dir_y*=-1;
            score++;
            song.start();
            if(score%5 == 0) {
                ball.speed++;
            }
        }
        if((!game_ovr)&&(!ball.oval.intersect(slider.rectF)) && (ball.y> slider.rectF.bottom)){
            game_ovr = true;
        }
        postInvalidate();
    }
    public boolean in_range(float a, float b,float c) {
        return (a >= b && a <= c);
    }
    /*public static float rand_no(float a, float b){
        return (float) ((Math.random()*(b-a))+a);
    }*/
}
