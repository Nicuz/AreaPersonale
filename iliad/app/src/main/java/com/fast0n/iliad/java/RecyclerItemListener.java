package com.fast0n.iliad.java;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerItemListener implements RecyclerView.OnItemTouchListener {

    private RecyclerTouchListener listener;
    private GestureDetector gd;

    public interface RecyclerTouchListener {
        public void onClickItem(View v, int position);

        public void onLongClickItem(View v, int position);
    }

    public RecyclerItemListener(Context ctx, final RecyclerView recycler_view, final RecyclerTouchListener listener) {
        this.listener = listener;
        gd = new GestureDetector(ctx, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                // We find the view
                View v = recycler_view.findChildViewUnder(e.getX(), e.getY());
                // Notify the even
                listener.onLongClickItem(v, recycler_view.getChildAdapterPosition(v));
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View v = recycler_view.findChildViewUnder(e.getX(), e.getY());
                // Notify the even
                listener.onClickItem(v, recycler_view.getChildAdapterPosition(v));
                return true;
            }
        });

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        return (child != null && gd.onTouchEvent(e));
    }

    @Override
    public void onTouchEvent(RecyclerView recycler_view, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
