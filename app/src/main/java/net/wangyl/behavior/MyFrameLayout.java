package net.wangyl.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

/**
 *
 */
public class MyFrameLayout extends FrameLayout{

    private final int[] offset = new int[2];
    private final int[] consumed = new int[2];
    private float mDownPosX;
    private float mDownPosY;
    private int lastY;

    public MyFrameLayout(Context context) {
        this(context, null);
    }

    public MyFrameLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        final float x = ev.getX();
        final float y = ev.getY();
        Log.e("MyFrameLayout", "dispatchTouchEvent action:" + ev.getAction() + " pixel:" + x + "," + y);
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownPosX = x;
                mDownPosY = y;

                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaX = Math.abs(x - mDownPosX);
                final float deltaY = Math.abs(y - mDownPosY);
                // 这里是够拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                if (deltaX > deltaY) {
                    Log.e("MyFrameLayout", "左右滑动");
                    return super.dispatchTouchEvent(ev);
                } else {
                    Log.e("MyFrameLayout", "上下滑动");
                    return false;
                }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final float x = ev.getX();
        final float y = ev.getY();
        Log.e("MyFrameLayout", "onInterceptTouchEvent action:" + ev.getAction() + " pixel:" + x + "," + y);
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownPosX = x;
                mDownPosY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaX = Math.abs(x - mDownPosX);
                final float deltaY = Math.abs(y - mDownPosY);
                // 这里是够拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                if (deltaX > deltaY) {
                    return false;
                } else {
                    return true;
                }
        }

        return super.onInterceptTouchEvent(ev);
    }
}
