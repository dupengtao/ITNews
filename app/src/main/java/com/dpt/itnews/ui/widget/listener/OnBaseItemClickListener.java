package com.dpt.itnews.ui.widget.listener;

import android.view.View;

/**
 * Created by dupengtao on 14-12-6.
 */
public interface OnBaseItemClickListener {

    /**
     * Called when a item has been clicked.
     * @param view     item view was clicked.
     * @param itemView
     * @param position the child's index
     * @param count item size
     */
    void onItemClick(View view, View itemView, int position, int count);

}
