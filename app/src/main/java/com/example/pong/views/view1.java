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

    float touched_x;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {

                touched_x = event.getX();
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final float x = event.getX();
                final float dx = x - touched_x;
                //slider.move(dx);
                if (slider.x >= 0 && slider.x <= canvas_width-300){
                    slider.x+=dx;
                }
                if(slider.x <=0  && dx>0){
                    slider.x+=dx;
                }
                if(slider.x >= canvas_width-300 && dx<0){
                    slider.x+=dx;
                }
                touched_x = x;

                invalidate();
                break;
            }
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
                }
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
        public Paint pt;
        public RectF rectF;

        public Slider(int x, int color) {
            this.x = x;
            this.pt = new Paint(Paint.ANTI_ALIAS_FLAG);
            this.pt.setColor(color);
            this.rectF = new RectF();
        }
        public void move(float dx) {
            if (slider.x >= 0 && slider.x <= canvas_width){
                slider.x+=dx;
            }
        }
        public float getWidth(Canvas canvas){
            return canvas.getWidth();
        }

    }

    public static Ball ball;
    public static Slider slider;
    public static int score = 0 ;
    public static boolean game_ovr = false;
    public static MediaPlayer song;
    public static float canvas_width;

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

        canvas_width = getWidth();

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
    public boolean in_range(float a, float b,float c){
        return (a>=b && a<=c);
    }
}
