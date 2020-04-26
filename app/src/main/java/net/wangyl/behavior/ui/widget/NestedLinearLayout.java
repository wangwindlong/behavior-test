package net.wangyl.behavior.ui.widget;

import android.annotation.SuppressLint;
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
 * 实现header跟随手指触发整体滑动
 */
public class NestedLinearLayout extends FrameLayout implements NestedScrollingChild {
    public static final String TAG = "NestedLinearLayout";
    private final int[] offset = new int[2];
    private final int[] consumed = new int[2];

    private NestedScrollingChildHelper mScrollingChildHelper;
    private int lastY;
    private int lastX;
    private float mDownPosX;
    private float mDownPosY;

    public NestedLinearLayout(Context context) {
        this(context, null);
    }

    public NestedLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setNestedScrollingEnabled(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        int x = (int) event.getX();
        Log.d(TAG, "onTouchEvent action:" + event.getAction() + " pixel:" + x + "," + y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                lastX = x;
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            case MotionEvent.ACTION_MOVE:
                //dy < 0 下滑， dy>0 上滑
                int dx = lastX - x;
                int dy = lastY - y;
                lastY = y;
                dispatchNestedPreScroll(0, dy, consumed, offset);
                break;
        }
        return true;
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        final float x = ev.getX();
//        final float y = ev.getY();
//        Log.d(TAG, "dispatchTouchEvent action:" + ev.getAction() + " pixel:" + x + "," + y);
//        final int action = ev.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                mDownPosX = x;
//                mDownPosY = y;
//                // 保证子View能够接收到Action_move事件
//                getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                final float deltaX = Math.abs(x - mDownPosX);
//                final float deltaY = Math.abs(y - mDownPosY);
//                if (deltaX > deltaY) {
//                    Log.d(TAG, "左右滑动");
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                } else {
//                    Log.d(TAG, "上下滑动");
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                }
//                break;
//            case MotionEvent.ACTION_CANCEL:
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//        }
//        return super.dispatchTouchEvent(ev);
////        return true;
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        final float x = ev.getX();
//        final float y = ev.getY();
//        Log.e(TAG, "onInterceptTouchEvent action:" + ev.getAction() + " pixel:" + x + "," + y);
//        final int action = ev.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                mDownPosX = x;
//                mDownPosY = y;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                final float deltaX = Math.abs(x - mDownPosX);
//                final float deltaY = Math.abs(y - mDownPosY);
//                Log.e(TAG, "onInterceptTouchEvent action:2 return:" + (deltaX <= deltaY));
//                if (deltaX > deltaY) {
//                    return false;
//                } else {
//                    return true;
//                }
//        }
//
////        return super.onInterceptTouchEvent(ev);
//        return false;
//    }


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
