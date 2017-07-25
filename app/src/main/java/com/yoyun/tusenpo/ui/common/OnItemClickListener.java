package com.yoyun.tusenpo.ui.common;

import android.view.View;

/**
 * Created by Yoyun on 2017/7/20.
 */

public interface OnItemClickListener<V> {
    void onItemClick(View view, int position, V value);
}