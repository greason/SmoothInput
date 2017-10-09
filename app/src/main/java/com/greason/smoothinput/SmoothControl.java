package com.greason.smoothinput;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Created by greason on 2017/9/23.
 */
public class SmoothControl {

    private ViewGroup scrollable;
    private int scrollPadding = 30; //滑动距离上偏移
    private Rect rect = new Rect();

    private int screenHeight = 0;
    private DetectViewChange detectViewChange;
    private View empty;

    public DetectViewChange register(ViewGroup scroll) {
        return register(scroll, null);
    }

    public DetectViewChange register(ViewGroup scroll, View content) {
        this.scrollable = scroll;

        if (scroll == null) {
            return null;
        }
        DisplayMetrics dm = scrollable.getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels;
        scrollable.getWindowVisibleDisplayFrame(rect);
        return initDetectView(content);
    }

    private DetectViewChange initDetectView(View content) {
        if (content instanceof DetectViewChange) {
            detectViewChange = (DetectViewChange) content;
        } else {
            detectViewChange = new DetectViewChange(scrollable.getContext());
            detectViewChange.setOrientation(LinearLayout.VERTICAL);
        }

        if (scrollable instanceof ScrollView) {
            detectViewChange.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            View middle = scrollable.getChildAt(0);
            scrollable.removeAllViews();
            scrollable.addView(detectViewChange);
            detectViewChange.addView(middle);
        } else if (scrollable instanceof ListView) {
            if (!(content instanceof DetectViewChange)) {
                detectViewChange.addView(content);
            }
        }

        detectViewChange.setOnMeasureBeforeListener(new DetectViewChange.OnMeasureBeforeListener() {
            @Override
            public void onMeasure(int maxHeight, int oldHeight, int nowHeight) {
                Activity activity = (Activity) scrollable.getContext();
                View focus = activity.getCurrentFocus();
                if (oldHeight > nowHeight && focus instanceof EditText) {
                    EditText editText = (EditText) focus;
                    Rect editRect = new Rect();
                    editText.getWindowVisibleDisplayFrame(editRect);

                    Rect rect1 = new Rect();
                    scrollable.getGlobalVisibleRect(rect1);

                    //输入框需要上滑的高度
                    int range = (maxHeight - nowHeight + (screenHeight - rect.bottom) + rect1.top) - (screenHeight - editRect.bottom);
                    if (range > 0) {
                        //减去距离底部所有view的高度
                        int leftViewHeight = 0;
                        for (int i = detectViewChange.indexOfChild(editText) + 1; i < detectViewChange.getChildCount(); i++) {
                            if (detectViewChange.getChildAt(i) != null) {
                                leftViewHeight += detectViewChange.getChildAt(i).getHeight();
                            }
                        }
                        if (range + scrollPadding < leftViewHeight) {
                            scrollBy(scrollPadding);
                        } else {
                            addBottomEmptyView(detectViewChange);
                            if (empty != null) {
                                ViewGroup.LayoutParams params = empty.getLayoutParams();
                                params.height = scrollPadding;
                                scrollBy(scrollPadding);
                            }
                        }

                    } else {
                        if (empty != null) {
                            ViewGroup.LayoutParams params = empty.getLayoutParams();
                            params.height = 0;
                            empty.setLayoutParams(params);
                        }
                    }
                } else if (oldHeight < nowHeight) {
                    removeBottomEmptyView(detectViewChange);
                }
            }
        });

        return detectViewChange;
    }

    private void addBottomEmptyView(LinearLayout view) {
        if (empty == null) {
            empty = new View(view.getContext());
            empty.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.addView(empty);
        }
    }

    private void removeBottomEmptyView(LinearLayout view) {
        if (empty != null) {
            view.removeView(empty);
            empty = null;
        }
    }

    private void scrollBy(final int y) {
        detectViewChange.post(new Runnable() {
            @Override
            public void run() {
                scrollable.scrollBy(0, y);
            }
        });
    }
}
