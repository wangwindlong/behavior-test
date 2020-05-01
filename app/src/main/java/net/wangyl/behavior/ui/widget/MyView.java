package net.wangyl.behavior.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class MyView extends View {
    private static final String TAG = "MyView";

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final int action = ev.getAction() & MotionEvent.ACTION_MASK;
        Log.d(TAG, "MyView dispatchTouchEvent: action=" + action);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xFF558B2F);
    }

}
