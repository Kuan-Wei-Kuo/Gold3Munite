package com.kuo.gold3munite;

/**
 * Created by User on 2015/4/30.
 */

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

public class MaterialListButton extends View{

    private int textSize = 70;
    private int circleBackgroundColor = getResources().getColor(R.color.BLUE_A400);
    private int textColor = getResources().getColor(R.color.GRAY_900);
    private float circleBackgroundRaduis = 0, circleX = 0, customCircleBackgroundRaduis  = 0;
    private boolean firstRun = false;
    private boolean isFoucs = false;
    private String text = "";
    private Bitmap icon = null;
    private Paint backgroundPaint = new Paint();
    private Paint textPaint = new Paint();
    private onAnimationEnd onAnimationEnd = null;


    public MaterialListButton(Context context) {
        super(context);
    }

    public MaterialListButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaterialListButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(!firstRun){
            firstRun = true;
            initPaint();
        }

        canvas.save();
        canvas.drawCircle(circleX, getCenterVertical(), circleBackgroundRaduis, backgroundPaint);
        canvas.restore();
        Bitmap bitmap = Bitmap.createScaledBitmap(icon, (int)(icon.getWidth() * 0.3), (int)(icon.getHeight() * 0.3), true);
        canvas.drawBitmap(bitmap, circleX - bitmap.getWidth() / 2, getCenterVertical() - bitmap.getHeight() / 2, backgroundPaint);
        canvas.drawText(text, circleX * 2 + 20, getCenterVertical()+(textSize/2)-5, textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                isFoucs = true;
                setCricleBackgroundRaduis(customCircleBackgroundRaduis + 10);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if(isFoucs){
                    startAnimation();
                    isFoucs = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(event.getX() >= getWidth() || event.getY() <= 0 || event.getY() >= getHeight()){
                    if(isFoucs){
                        isFoucs = false;
                        setCricleBackgroundRaduis(customCircleBackgroundRaduis);
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    private void initPaint(){

        if(circleBackgroundRaduis == 0){
            circleBackgroundRaduis = getHeight() * 0.45f;
            customCircleBackgroundRaduis = circleBackgroundRaduis;
        }else{
            customCircleBackgroundRaduis = circleBackgroundRaduis;
        }

        if(circleX == 0){
            circleX = circleBackgroundRaduis + (circleBackgroundRaduis/2) + 20;
        }

        backgroundPaint.setColor(circleBackgroundColor);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        Log.d("", "" + getId());
    }

    public float getCenterVertical(){
        return getHeight()/2;
    }

    public void setCricleBackgroundRaduis(float circleBackgroundRaduis){
        this.circleBackgroundRaduis = circleBackgroundRaduis;
    }

    public void setCircleX(float circleX){
        this.circleX = circleX + customCircleBackgroundRaduis;
    }

    public void setCricleBackgroundColor(int circleBackgroundColor){
        this.circleBackgroundColor = circleBackgroundColor;
    }

    public void setTextColor(int textColor){
        this.textColor = textColor;
    }

    public void setTextSize(int textSize){
        this.textSize = textSize;
    }


    public void setText(String text){
        this.text = text;
    }

    public void setIconResource(int resId){
        this.icon = BitmapFactory.decodeResource(getResources(), resId);
    }

    private void startAnimation(){

        AnimatorSet set = new AnimatorSet();

        set.playTogether(
                ObjectAnimator.ofFloat(this, radiusProperty, customCircleBackgroundRaduis, getWidth())
        );

        set.setDuration(800);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(circleBackgroundRaduis == getWidth()){
                    setCricleBackgroundRaduis(customCircleBackgroundRaduis);
                    if(onAnimationEnd != null){
                        onAnimationEnd.onAnimationEnd(MaterialListButton.this);
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        set.start();
    }

    private Property<MaterialListButton, Float> radiusProperty = new Property<MaterialListButton, Float>(Float.class, "cricleBackgroundRaduis") {
        @Override
        public Float get(MaterialListButton object) {
            return object.circleBackgroundRaduis;
        }

        @Override
        public void set(MaterialListButton object, Float value) {
            object.circleBackgroundRaduis = value;
            invalidate();
        }
    };

    public void setAnimationEndListener(onAnimationEnd onAnimationEnd){
        this.onAnimationEnd = onAnimationEnd;
    }

    public interface onAnimationEnd{
        void onAnimationEnd(MaterialListButton simpleMaterialButton);
    }
}
