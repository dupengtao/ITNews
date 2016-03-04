package com.dpt.itnews.ui.widget;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.dpt.itnews.ui.widget.adapter.SmoothScrollerListener;
import com.dpt.itnews.util.LogHelper;

public class ScrollingLinearLayoutManager extends LinearLayoutManager {
    private final int duration;
    private SmoothScrollerListener mSmoothScrollerListener;
    private SmoothScroller smoothScroller;

    public ScrollingLinearLayoutManager(Context context, int orientation, boolean reverseLayout, int duration) {
        super(context, orientation, reverseLayout);
        this.duration = duration;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
                                       int position) {
        View firstVisibleChild = recyclerView.getChildAt(0);
        int itemHeight = firstVisibleChild.getHeight();
        int currentPosition = recyclerView.getChildAdapterPosition(firstVisibleChild);
        int distanceInPixels = Math.abs((currentPosition - position) * itemHeight);
        if (distanceInPixels == 0) {
            distanceInPixels = (int) Math.abs(firstVisibleChild.getY());
        }
        smoothScroller = new SmoothScroller(recyclerView.getContext(), distanceInPixels, duration);
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }

    public SmoothScrollerListener getSmoothScrollerListener() {
        return mSmoothScrollerListener;
    }

    public void setSmoothScrollerListener(SmoothScrollerListener mSmoothScrollerListener) {
        this.mSmoothScrollerListener = mSmoothScrollerListener;
    }

    public SmoothScroller getSmoothScroller() {
        return smoothScroller;
    }

    public class SmoothScroller extends LinearSmoothScroller {
        private final float distanceInPixels;
        private final float duration;
        private int times;

        public SmoothScroller(Context context, int distanceInPixels, int duration) {
            super(context);
            this.distanceInPixels = distanceInPixels;
            this.duration = duration;
        }

        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            PointF pointF = ScrollingLinearLayoutManager.this.computeScrollVectorForPosition(targetPosition);
            return pointF;
        }

        @Override
        protected int calculateTimeForScrolling(int dx) {
            float proportion = (float) dx / distanceInPixels;
            times++;
            LogHelper.e("times"," times == "+times);
            return 100;
        }

        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_START;
        }

        @Override
        protected void onStart() {
            super.onStart();
            if(ScrollingLinearLayoutManager.this.getSmoothScrollerListener()!=null){
                ScrollingLinearLayoutManager.this.getSmoothScrollerListener().onStart();
            }
        }

        @Override
        protected void onStop() {
            super.onStop();
            if(ScrollingLinearLayoutManager.this.getSmoothScrollerListener()!=null){
                ScrollingLinearLayoutManager.this.getSmoothScrollerListener().onStop();
            }
        }
    }
}

