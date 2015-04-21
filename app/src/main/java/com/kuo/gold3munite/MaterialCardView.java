package com.kuo.gold3munite;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Property;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by User on 2015/4/21.
 */
public class MaterialCardView extends CardView {

    private Paint backgroundPaint = new Paint();
    private float radius, paintX, paintY;

    public MaterialCardView(Context context) {
        super(context);
    }

    public MaterialCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaterialCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawCircle(paintX, paintY, radius, backgroundPaint);
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            paintX = event.getX();
            paintY = event.getY();
            startAnimator();
        }

        return super.onTouchEvent(event);
    }

    private Property<MaterialCardView, Float> radiusProperty = new Property<MaterialCardView, Float>(Float.class, "radius") {
        @Override
        public Float get(MaterialCardView materialCardView) {
            return materialCardView.radius;
        }

        @Override
        public void set(MaterialCardView object, Float value) {
            object.radius = value;
            invalidate();
        }
    };


    private Property<MaterialCardView, Integer> backgroundColorProperty = new Property<MaterialCardView, Integer>(Integer.class, "backgroundColor") {

        @Override
        public Integer get(MaterialCardView materialCardView) {
            return materialCardView.backgroundPaint.getColor();
        }

        @Override
        public void set(MaterialCardView object, Integer value) {
            object.backgroundPaint.setColor(value);
        }
    };

    private void startAnimator() {
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

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(this, radiusProperty, startRadius, endRadius),
                ObjectAnimator.ofObject(this, backgroundColorProperty, new ArgbEvaluator(), getResources().getColor(R.color.black_2), Color.TRANSPARENT)
        );
        set.setDuration((long) (1200 / end * endRadius));
        set.setInterpolator(new DecelerateInterpolator());
        set.start();
    }
}
