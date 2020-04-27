package net.wangyl.behavior.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;

public class MyViewPager extends ViewPager implements NestedScrollingChild {
    public static final String TAG = "MyViewPager";

    private NestedScrollingChildHelper mScrollingChildHelper;
    private int lastY;
    private int lastX;
    private int downY;
    private final int[] offset = new int[2];
    private final int[] consumed = new int[2];

    public static final int STATE_UNKOWN = 0;
    public static final int STATE_X = 1;
    public static final int STATE_Y = 2;
    private int mState = STATE_UNKOWN;
    private int mTouchSlop;


    public MyViewPager(@NonNull Context context) {
        this(context, null);
    }

    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setNestedScrollingEnabled(true);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledPagingTouchSlop();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        int x = (int) event.getX();
        Log.d(TAG, "onTouchEvent action:" + event.getAction() + " pixel:" + x + "," + y + ",mTouchSlop=" + mTouchSlop);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                downY = lastX = x;
                mState = STATE_UNKOWN;
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            case MotionEvent.ACTION_MOVE:
                //dy < 0 下滑， dy>0 上滑
                int dx = lastX - x;
                int dy = lastY - y;
                final float deltaX = Math.abs(x - lastX);
                final float deltaY = Math.abs(y - lastY);
                if (deltaX > mTouchSlop && deltaX * 0.5f > deltaY) {
                    if (mState == STATE_UNKOWN) mState = STATE_X;
                } else if (deltaY > mTouchSlop || Math.abs(y - downY) > mTouchSlop) {
                    if (mState == STATE_UNKOWN) mState = STATE_Y;
                }
                Log.d(TAG, "ACTION_MOVE mState:" + mState + " deltaX:" + deltaX + ",deltaY:" + deltaY);
                if (mState == STATE_Y) dispatchNestedPreScroll(0, dy, consumed, offset);
                lastX = x;
                lastY = y;
                break;
        }
        super.onTouchEvent(event);
        return true;
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        getScrollingChildHelper().setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return getScrollingChildHelper().isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        Log.i(TAG, "startNestedScroll axes=" + axes);
//        if (axes == SCROLL_AXIS_VERTICAL) {
//            return true;
//        }
        return getScrollingChildHelper().startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        getScrollingChildHelper().stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return getScrollingChildHelper().hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed,
                                        int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return getScrollingChildHelper().dispatchNestedScroll(dxConsumed,
                dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed,
                                           int[] offsetInWindow) {
        Log.i(TAG, "dispatchNestedPreScroll dx=" + dx + ",dy=" + dy);
        return getScrollingChildHelper().dispatchNestedPreScroll(dx, dy,
                consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY,
                                       boolean consumed) {
        return getScrollingChildHelper().dispatchNestedFling(velocityX,
                velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return getScrollingChildHelper().dispatchNestedPreFling(velocityX,
                velocityY);
    }

    private NestedScrollingChildHelper getScrollingChildHelper() {
        if (mScrollingChildHelper == null) {
            mScrollingChildHelper = new NestedScrollingChildHelper(this);
        }
        return mScrollingChildHelper;
    }
}
