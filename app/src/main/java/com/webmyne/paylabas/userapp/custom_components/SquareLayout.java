package com.webmyne.paylabas.userapp.custom_components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Android on 03-12-2014.
 */
public class SquareLayout extends LinearLayout{


    public SquareLayout(Context context) {
        super(context);
    }

    public SquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);

        super.onMeasure(widthMeasureSpec,width);
    }
}
