package net.wangyl.behavior.behaviors;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.OverScroller;


import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.wangyl.behavior.ui.widget.NestedLinearLayout;
import net.wangyl.behavior.R;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class MainHeaderBehavior extends ViewOffsetBehavior<View> {
    private static final int STATE_OPENED = 0;
    private static final int STATE_CLOSED = 1;
    private static final int DURATION_SHORT = 300;
    private static final int DURATION_LONG = 600;
    public static final String TAG = "MainHeaderBehavior";

    private int mCurState = STATE_OPENED;
    private OnHeaderStateListener mHeaderStateListener;

    private OverScroller mOverScroller;

    private WeakReference<CoordinatorLayout> mParent;//CoordinatorLayout
    private WeakReference<View> mChild;//CoordinatorLayout的子View，即header

    //界面整体向上滑动，达到列表可滑动的临界点
    private boolean upReach;
    //列表向上滑动后，再向下滑动，达到界面整体可滑动的临界点
    private boolean downReach;
    //列表上一个全部可见的item位置
    private int lastPosition = -1;

    private FlingRunnable mFlingRunnable;

    private Context mContext;

    //tab上移结束后是否悬浮在固定位置
    private boolean tabSuspension = false;

    private float mDownPosX;
    private float mDownPosY;

    public MainHeaderBehavior() {
        init();
    }

    public MainHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        mOverScroller = new OverScroller(mContext);
    }

    @Override
    protected void layoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        super.layoutChild(parent, child, layoutDirection);
        mParent = new WeakReference<>(parent);
        mChild = new WeakReference<>(child);
    }

    @Override
    public boolean onStartNestedScroll(@NotNull CoordinatorLayout coordinatorLayout,
                                       @NotNull View child, @NotNull View directTargetChild,
                                       @NotNull View target, int nestedScrollAxes, @ViewCompat.NestedScrollType int type) {
        boolean result = (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        Log.d(TAG, "onStartNestedScroll =" + result);
        if (tabSuspension) {
            return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0 && !isClosed();
        }
        return result;
    }


    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, float velocityX, float velocityY) {
        lastPosition = -1;
        return !isClosed();
    }

    @Override
    public boolean onInterceptTouchEvent(@NotNull CoordinatorLayout parent, @NotNull final View child, MotionEvent ev) {
        Log.d(TAG, "onInterceptTouchEvent =" + ev);
        final float x = ev.getX();
        final float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downReach = false;
                upReach = false;
                mDownPosX = x;
                mDownPosY = y;
                break;
//            case MotionEvent.ACTION_MOVE:
//                final float deltaX = Math.abs(x - mDownPosX);
//                final float deltaY = Math.abs(y - mDownPosY);
//                if (deltaX > deltaY) {
//                    Log.d(TAG, "左右滑动");
//                    return super.onInterceptTouchEvent(parent, child, ev);
//                } else {
//                    Log.d(TAG, "上下滑动");
//                    return true;
//                }
            case MotionEvent.ACTION_UP:
                handleActionUp(child);
                break;
        }
//        if (child instanceof ViewGroup) {
//            return ((ViewGroup)child).onInterceptTouchEvent(ev);
//        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }


    /**
     * @param coordinatorLayout
     * @param child             代表header
     * @param target            代表RecyclerView
     * @param dx
     * @param dy                上滑 dy>0， 下滑dy<0
     * @param consumed
     */
    @Override
    public void onNestedPreScroll(@NotNull CoordinatorLayout coordinatorLayout, @NotNull View child,
                                  @NotNull View target, int dx, int dy,
                                  @NotNull int[] consumed, @ViewCompat.NestedScrollType int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        Log.d(TAG, "onNestedPreScroll target=" + target.getId() + ",nestsc=" + R.id.viewpager2_nsv + ",type=" + type+"'dy="+dy);
        //制造滑动视察，使header的移动比手指滑动慢
        float scrollY = dy / 4.0f;

//        if (target instanceof NestedScrollView && target.getId() == R.id.viewpager2_nsv) {//处理header滑动
        if (target instanceof NestedLinearLayout) {//处理header滑动
            Log.d(TAG, "scrollY=" + child + ",target.getTranslationY()=" + child.getTranslationY() +
                    ",dx=" + dx + ",dy=" + dy);
            float finalY = child.getTranslationY() - scrollY;
            if (finalY < getHeaderOffset()) {
                finalY = getHeaderOffset();
            } else if (finalY > 0) {
                finalY = 0;
            }
            child.setTranslationY(finalY);
            consumed[1] = dy;
        } else if (target instanceof RecyclerView) {//处理列表滑动
            RecyclerView list = (RecyclerView) target;
            int pos = ((LinearLayoutManager) Objects.requireNonNull(list.getLayoutManager())).findFirstCompletelyVisibleItemPosition();

            //header closed状态下，列表上滑再下滑到第一个item全部显示，此时不让CoordinatorLayout整体下滑，
            //手指重新抬起再下滑才可以整体滑动
            if (pos == 0 && pos < lastPosition) {
                downReach = true;
            }

            if (pos == 0 && canScroll(child, scrollY)) {
                //如果列表第一个item全部可见、或者header已展开，则让CoordinatorLayout消费掉事件
                float finalY = child.getTranslationY() - scrollY;
                //header已经closed，整体不能继续上滑，手指抬起重新上滑列表开始滚动
                if (finalY < getHeaderOffset()) {
                    finalY = getHeaderOffset();
                    upReach = true;
                } else if (finalY > 0) {//header已经opened，整体不能继续下滑
                    finalY = 0;
                }
                child.setTranslationY(finalY);
                consumed[1] = dy;//让CoordinatorLayout消费掉事件，实现整体滑动
            }
            lastPosition = pos;
        }
        else if (target instanceof NestedScrollView) {//处理header滑动
            float finalY = child.getTranslationY() - scrollY;
            if (finalY < getHeaderOffset()) {
                finalY = getHeaderOffset();
            } else if (finalY > 0) {
                finalY = 0;
            }
            child.setTranslationY(finalY);
            consumed[1] = dy;
        }
//        else if (target instanceof NestedScrollView) {//处理NestedScrollview滑动
//            float scroly = target.getScrollY();
//            float childheight = child.getMeasuredHeight();
//            Log.e(TAG, "child=" + child + ",childheight=" + childheight + ",child.getTranslationY()=" +
//                    child.getTranslationY() + ",scrollY=" + scrollY);
//            if (scroly == 0) {
//                consumed[1] = dy;
//                float finalY = child.getTranslationY() + dy;
//                if (finalY < 0) {
//                    finalY = 0;
//                } else if (finalY > childheight) {
//                    finalY = childheight;
//                }
//                Log.e(TAG, "finalY=" + finalY + ",getHeaderOffset()=" + getHeaderOffset());
//                child.setTranslationY(finalY);
//            }
//        }
    }

    /**
     * 是否可以整体滑动
     *
     * @return
     */
    private boolean canScroll(View child, float scrollY) {
        if (scrollY > 0 && child.getTranslationY() > getHeaderOffset()) {
            return true;
        }

        if (child.getTranslationY() == getHeaderOffset() && upReach) {
            return true;
        }

        if (scrollY < 0 && !downReach) {
            return true;
        }

        return false;
    }

    private int getHeaderOffset() {
        return mContext.getResources().getDimensionPixelOffset(R.dimen.header_offset);
    }

    private void handleActionUp(View child) {
        if (mFlingRunnable != null) {
            child.removeCallbacks(mFlingRunnable);
            mFlingRunnable = null;
        }
        //手指抬起时，header上滑距离超过总距离三分之一，则整体自动上滑到关闭状态
        if (child.getTranslationY() < getHeaderOffset() / 3.0f) {
            scrollToClose(DURATION_SHORT);
        } else {
            scrollToOpen(DURATION_SHORT);
        }
    }

    private void onFlingFinished(View layout) {
        changeState(isClosed(layout) ? STATE_CLOSED : STATE_OPENED);
    }

    /**
     * 直接展开
     */
    public void openHeader() {
        openHeader(DURATION_LONG);
    }

    private void openHeader(int duration) {
        if (isClosed() && mChild.get() != null) {
            if (mFlingRunnable != null) {
                mChild.get().removeCallbacks(mFlingRunnable);
                mFlingRunnable = null;
            }
            scrollToOpen(duration);
        }
    }

    public void closeHeader() {
        closeHeader(DURATION_LONG);
    }

    private void closeHeader(int duration) {
        if (!isClosed() && mChild.get() != null) {
            if (mFlingRunnable != null) {
                mChild.get().removeCallbacks(mFlingRunnable);
                mFlingRunnable = null;
            }
            scrollToClose(duration);
        }
    }

    private boolean isClosed(View child) {
        return child.getTranslationY() == getHeaderOffset();
    }

    public boolean isClosed() {
        return mCurState == STATE_CLOSED;
    }

    private void changeState(int newState) {

        if (mCurState != newState) {
            mCurState = newState;

            if (mHeaderStateListener == null) {
                return;
            }

            if (mCurState == STATE_OPENED) {
                mHeaderStateListener.onHeaderOpened();
            } else {
                mHeaderStateListener.onHeaderClosed();
            }
        }
    }

    private void scrollToClose(int duration) {
        int curTranslationY = (int) mChild.get().getTranslationY();
        int dy = getHeaderOffset() - curTranslationY;
        mOverScroller.startScroll(0, curTranslationY, 0, dy, duration);
        start();
    }

    private void scrollToOpen(int duration) {
        float curTranslationY = mChild.get().getTranslationY();
        mOverScroller.startScroll(0, (int) curTranslationY, 0, (int) -curTranslationY, duration);
        start();
    }

    private void start() {
        if (mOverScroller.computeScrollOffset()) {
            mFlingRunnable = new FlingRunnable(mParent.get(), mChild.get());
            ViewCompat.postOnAnimation(mChild.get(), mFlingRunnable);
        } else {
            onFlingFinished(mChild.get());
        }
    }

    private class FlingRunnable implements Runnable {
        private final CoordinatorLayout mParent;
        private final View mLayout;

        FlingRunnable(CoordinatorLayout parent, View layout) {
            mParent = parent;
            mLayout = layout;
        }

        @Override
        public void run() {
            if (mLayout != null && mOverScroller != null) {
                if (mOverScroller.computeScrollOffset()) {
                    mLayout.setTranslationY(mOverScroller.getCurrY());
                    ViewCompat.postOnAnimation(mLayout, this);
                } else {
                    onFlingFinished(mLayout);
                }
            }
        }
    }

    public void setTabSuspension(boolean tabSuspension) {
        this.tabSuspension = tabSuspension;
    }

    public void setHeaderStateListener(OnHeaderStateListener headerStateListener) {
        mHeaderStateListener = headerStateListener;
    }

    public interface OnHeaderStateListener {
        void onHeaderClosed();

        void onHeaderOpened();
    }

}