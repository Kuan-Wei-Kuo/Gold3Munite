package com.kuo.common;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Property;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.kuo.gold3munite.R;

/**
 * Created by User on 2015/4/21.
 */
public class MaterialLinearLayout extends LinearLayout {

    private Paint backgroundPaint = new Paint();
    private float radius, paintX, paintY;
    private OnAnimationListener onAnimationListener;
    private int position;

    public MaterialLinearLayout(Context context) {
        super(context);
    }

    public MaterialLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaterialLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawCircle(paintX, paintY, radius, backgroundPaint);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isClickable()){
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                paintX = event.getX();
                paintY = event.getY();
            }
            return super.onTouchEvent(event);
        }else{
            return false;
        }
    }

    private Property<MaterialLinearLayout, Float> radiusProperty = new Property<MaterialLinearLayout, Float>(Float.class, "radius") {
        @Override
        public Float get(MaterialLinearLayout materialLinearLayout) {
            return materialLinearLayout.radius;
        }

        @Override
        public void set(MaterialLinearLayout object, Float value) {
            object.radius = value;
            invalidate();
        }
    };


    private Property<MaterialLinearLayout, Integer> backgroundColorProperty = new Property<MaterialLinearLayout, Integer>(Integer.class, "backgroundColor") {

        @Override
        public Integer get(MaterialLinearLayout materialLinearLayout) {
            return materialLinearLayout.backgroundPaint.getColor();
        }

        @Override
        public void set(MaterialLinearLayout object, Integer value) {
            object.backgroundPaint.setColor(value);
        }
    };

    public void startAnimator() {
        int start, end;

        if (getHeight() < getWidth()) {
            start = getHeight();
            end = getWidth();
        } else {
            start = getWidth();
            end = getHeight();
        }

        float startRadius = (start / 2 > paintY ? start - paintY : paintY) * 1.15f;
        float endRadius = (end / 2 > paintX ? end - paintX : paintX) * 0.85f;

        ObjectAnimator objectAnimatorRadius = new ObjectAnimator();
        ObjectAnimator objectAnimatorBackGround = new ObjectAnimator();

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                objectAnimatorRadius.ofFloat(this, radiusProperty, startRadius, endRadius),
                objectAnimatorBackGround.ofObject(this, backgroundColorProperty, new ArgbEvaluator(), getResources().getColor(R.color.black_2), Color.TRANSPARENT)
        );

        //set.setDuration((long) (1200 / end * endRadius));
        set.setDuration(300);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                onAnimationListener.onAnimationStart(position);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                onAnimationListener.onAnimationEnd(position);
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

    public interface OnAnimationListener{
        void onAnimationStart(int position);
        void onAnimationEnd(int position);
    }

    public void setOnAnimationListener(OnAnimationListener onAnimationListener, int position){
        this.onAnimationListener = onAnimationListener;
        this.position = position;
    }
}
