package com.yoyun.tusenpo.ui.base;

/**
 * Created by Yoyun on 2017/6/19.
 */

public interface Presenter<V extends MvpView> {

    void attachView(V mvpView);

    void detachView();
}
