package com.kuo.common;

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
import android.widget.Button;

import com.kuo.gold3munite.R;

/**
 * Created by User on 2015/4/21.
 */
public class MaterialButton extends Button {

    private Paint backgroundPaint = new Paint();
    private float radius, paintX, paintY;

    public MaterialButton(Context context) {
        super(context);
    }

    public MaterialButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaterialButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.save();
        canvas.drawCircle(paintX, paintY, radius, backgroundPaint);
        canvas.restore();

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

    private Property<MaterialButton, Float> radiusProperty = new Property<MaterialButton, Float>(Float.class, "radius") {

        @Override
        public Float get(MaterialButton materialButton) {
            return materialButton.radius;
        }

        @Override
        public void set(MaterialButton object, Float value) {
            object.radius = value;
            invalidate();
        }
    };

    private Property<MaterialButton, Integer> backgroundColorProperty = new Property<MaterialButton, Integer>(Integer.class, "bg_color") {

        @Override
        public Integer get(MaterialButton materialButton) {
            return materialButton.backgroundPaint.getColor();
        }

        @Override
        public void set(MaterialButton object, Integer value) {
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
                ObjectAnimator.ofObject(this, backgroundColorProperty, new ArgbEvaluator(), getResources().getColor(R.color.GRAY_500), Color.TRANSPARENT)
        );
        set.setDuration((long) (1200 / end * endRadius));
        set.setInterpolator(new DecelerateInterpolator());
        set.start();
    }
}
