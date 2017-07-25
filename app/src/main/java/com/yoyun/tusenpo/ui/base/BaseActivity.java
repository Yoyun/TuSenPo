package com.yoyun.tusenpo.ui.base;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Yoyun on 2017/6/19.
 */

public class BaseActivity extends AppCompatActivity {

    public Context getContext() {
        return this;
    }

    public void showToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
