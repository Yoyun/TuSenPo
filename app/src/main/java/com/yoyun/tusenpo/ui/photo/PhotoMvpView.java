package com.yoyun.tusenpo.ui.photo;

import com.yoyun.tusenpo.data.model.Photo;
import com.yoyun.tusenpo.ui.base.MvpView;

import java.util.List;

/**
 * Created by Yoyun on 2017/7/19.
 */

public interface PhotoMvpView extends MvpView {

    void showPhotos(List<Photo> photoList);

    void showShareDialog();

    void hideShareDialog();

    void shareImage(String imagePath);
}
