package com.ascentya.AsgriV2.e_market.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class DynamicViewPager extends ViewPager {
    public DynamicViewPager(@NonNull Context context) {
        super(context);
    }

    public DynamicViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        int childWidthSpec = MeasureSpec.makeMeasureSpec(
                Math.max(0, MeasureSpec.getSize(widthMeasureSpec) -
                        getPaddingLeft() - getPaddingRight()),
                MeasureSpec.getMode(widthMeasureSpec)
        );
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(childWidthSpec, MeasureSpec.UNSPECIFIED);
            int h = child.getMeasuredHeight();
            if (h > height) height = h;
        }

        if (height != 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
