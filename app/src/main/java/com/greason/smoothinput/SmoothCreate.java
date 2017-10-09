package com.greason.smoothinput;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by greason on 2017/9/25.
 */
public class SmoothCreate {

    public static void create(ViewGroup scroll) {
        new SmoothControl().register(scroll);
    }

    public static View create(ViewGroup scroll, View content) {
        return new SmoothControl().register(scroll, content);
    }
}
