package com.kuo.gold3munite;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by User on 2015/4/28.
 */
public class WidgetsMaterialButton extends Button {

    private Paint backgroundPaint = new Paint();
    private float left, right, top, bottom;

    public WidgetsMaterialButton(Context context) {
        super(context);
    }

    public WidgetsMaterialButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WidgetsMaterialButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.save();
        canvas.drawRect(left, top, right, bottom, backgroundPaint);
        canvas.restore();

        super.onDraw(canvas);
    }
}
