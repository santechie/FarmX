package com.ascentya.AsgriV2.Utils;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {

    private int mSizeGridSpacingPx;
    private int mGridSize;
    private boolean mNeedLeftSpacing = false;

    /**
     * @param gridSpacingPx
     * @param gridSize
     */
    GridDividerItemDecoration(Context context, int gridSpacingPx, int gridSize) {
        mSizeGridSpacingPx = (int) ConvertUtils.dpToPx(gridSpacingPx, context);
        mGridSize = gridSize;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int frameWidth = (int) ((parent.getWidth() - (float) mSizeGridSpacingPx * (mGridSize - 1)) / mGridSize);
        int padding = parent.getWidth() / mGridSize - frameWidth;
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
        int itemCount = parent.getAdapter().getItemCount() - mGridSize;


   /*     if (itemPosition < mGridSize) {
            outRect.top = mSizeGridSpacingPx;
        } else {
                   outRect.top = mSizeGridSpacingPx;
        }*/
        outRect.top = mSizeGridSpacingPx;
        if (itemPosition % mGridSize == 0) {
            outRect.left = mSizeGridSpacingPx;
            outRect.right = padding;
            mNeedLeftSpacing = true;
        } else if ((itemPosition + 1) % mGridSize == 0) {
            mNeedLeftSpacing = false;
            outRect.right = mSizeGridSpacingPx;
            outRect.left = padding;
        } else if (mNeedLeftSpacing) {
            mNeedLeftSpacing = false;
            outRect.left = mSizeGridSpacingPx - padding;
            if ((itemPosition + 2) % mGridSize == 0) {
                outRect.right = mSizeGridSpacingPx - padding;
            } else {
                outRect.right = mSizeGridSpacingPx / 2;
            }
        } else if ((itemPosition + 2) % mGridSize == 0) {
            mNeedLeftSpacing = false;
            outRect.left = mSizeGridSpacingPx / 2;
            outRect.right = mSizeGridSpacingPx - padding;
        } else {
            mNeedLeftSpacing = false;
            outRect.left = mSizeGridSpacingPx / 2;
            outRect.right = mSizeGridSpacingPx / 2;
        }
        if (itemPosition > itemCount) {
            outRect.bottom = mSizeGridSpacingPx;
        } else {
            outRect.bottom = 0;
        }

    }

}
