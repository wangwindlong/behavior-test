package net.wangyl.behavior.behaviors;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import net.wangyl.behavior.R;

import org.jetbrains.annotations.NotNull;

/**
 * Title Behavior
 */
public class MainTitleBehavior extends CoordinatorLayout.Behavior<View> {

    private Context mContext;

    public MainTitleBehavior() {

    }

    public MainTitleBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public boolean layoutDependsOn(@NotNull CoordinatorLayout parent, @NotNull View child, @NotNull View dependency) {
        return isDependOn(dependency);
    }

    @Override
    public boolean onDependentViewChanged(@NotNull CoordinatorLayout parent, View child, View dependency) {
        float y = -(1 - dependency.getTranslationY() / getHeaderOffset()) * getTitleHeight();
        child.setY(y);
        return false;
    }

    private int getHeaderOffset() {
        return mContext.getResources().getDimensionPixelOffset(R.dimen.header_offset);
    }

    private int getTitleHeight() {
        return mContext.getResources().getDimensionPixelOffset(R.dimen.title_height);
    }

    private boolean isDependOn(View dependency) {
        return dependency != null && dependency.getId() == R.id.header;
    }
}
