package com.greason.smoothinput;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * detect softInput
 */
public class DetectViewChange extends LinearLayout {

    int originW = 0;
    int originH = 0;

    protected OnMeasureBeforeListener onMeasureBeforeListener;
    int oldHeight;

    public DetectViewChange(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DetectViewChange(Context context) {
        super(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //get the max height and width
        if (w > 0 && originW < w) {
            originW = w;
        }
        if (h > 0 && originH < h) {
            originH = h;
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (onMeasureBeforeListener != null) {
            onMeasureBeforeListener.onMeasure(originH, oldHeight, height);
        }
        oldHeight = height;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public interface OnMeasureBeforeListener {
        void onMeasure(int maxHeight, int oldHeight, int nowHeight);
    }

    public void setOnMeasureBeforeListener(OnMeasureBeforeListener onMeasureBeforeListener) {
        this.onMeasureBeforeListener = onMeasureBeforeListener;
    }
}
