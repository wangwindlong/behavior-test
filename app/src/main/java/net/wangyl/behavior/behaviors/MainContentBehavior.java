package net.wangyl.behavior.behaviors;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import net.wangyl.behavior.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 列表Behavior
 */
public class MainContentBehavior extends HeaderScrollingViewBehavior {
    private Context mContext;
    public static final String TAG = "MainContentBehavior";

    public MainContentBehavior() {
    }

    public MainContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public boolean layoutDependsOn(@NotNull CoordinatorLayout parent, @NotNull View child, @NotNull View dependency) {
        return isDependOn(dependency);
    }

    @Override
    public boolean onDependentViewChanged(@NotNull CoordinatorLayout parent, View child, View dependency) {
        float contentScrollY = dependency.getTranslationY() / getHeaderOffset() * (dependency.getHeight() - getFinalHeight());
        float y = dependency.getHeight() - contentScrollY;
        Log.d(TAG, "onDependentViewChanged y=" + y + ",dependency=" + dependency + ",child=" + child);
        child.setY(y);
        return true;
    }

    private void offsetChildAsNeeded(CoordinatorLayout parent, View child, View dependency) {
        child.setTranslationY((-dependency.getTranslationY() / getHeaderOffset() * getScrollRange(dependency)));

    }


    @Override
    protected View findFirstDependency(List<View> views) {
        for (int i = 0, z = views.size(); i < z; i++) {
            View view = views.get(i);
            if (isDependOn(view))
                return view;
        }
        return null;
    }

    /**
     * 计算content的最大向上偏移距离
     *
     * @param v
     * @return
     */
    @Override
    protected int getScrollRange(View v) {
        if (isDependOn(v)) {
            return Math.max(0, v.getMeasuredHeight() - getFinalHeight());
        } else {
            return super.getScrollRange(v);
        }
    }

    private int getHeaderOffset() {
        return mContext.getResources().getDimensionPixelOffset(R.dimen.header_offset);
    }

    private int getFinalHeight() {
        return mContext.getResources().getDimensionPixelOffset(R.dimen.tab_height)
                + mContext.getResources().getDimensionPixelOffset(R.dimen.title_height);
    }


    private boolean isDependOn(View dependency) {
        boolean isDependOn = dependency != null && dependency.getId() == R.id.header;
        Log.d(TAG, "isDependOn=" + isDependOn);
        return isDependOn;
    }


}
