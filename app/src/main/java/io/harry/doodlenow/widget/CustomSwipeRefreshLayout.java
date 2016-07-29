package io.harry.doodlenow.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {
    private OnChildScrollUpListener mScrollListenerNeeded;

    public interface OnChildScrollUpListener {
        boolean canChildScrollUp();
    }

    public CustomSwipeRefreshLayout(Context context) {
        super(context);
    }

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnChildScrollUpListener(OnChildScrollUpListener listener) {
        mScrollListenerNeeded = listener;
    }

    @Override
    public boolean canChildScrollUp() {
        return mScrollListenerNeeded != null && mScrollListenerNeeded.canChildScrollUp();
    }
}
