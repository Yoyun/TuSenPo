package com.yoyun.tusenpo.ui.article;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.squareup.gifencoder.GifEncoder;
import com.squareup.gifencoder.ImageOptions;
import com.yoyun.tusenpo.data.model.Article;
import com.yoyun.tusenpo.data.remote.Api;
import com.yoyun.tusenpo.ui.base.BasePresenter;
import com.yoyun.tusenpo.utils.DownloadUtil;

import java.io.File;
import java.io.FileOutputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Yoyun on 2017/7/20.
 */

public class ArticleDetailPresenter extends BasePresenter<ArticleDetailMvpView> {

    public void obtainDetail(String pageUrl) {
        Api.getInstance().obtainArticleDetail(pageUrl)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Article>() {
                    @Override
                    public void accept(@NonNull Article article) throws Exception {
                        if (article.getPhotos() != null) {
                            getMvpView().showPhotos(article.getPhotos());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Logger.e(throwable, throwable.getMessage());
                    }
                });
    }

    public void genGif(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }

        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());
        final File file = new File(Environment.getExternalStorageDirectory(), "Pictures" + File.separator + fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        String tmp = file.getAbsolutePath();
        if (!fileName.contains(".gif")) {
            tmp = tmp.substring(0, tmp.lastIndexOf(".")) + ".gif";
        }
        if (new File(tmp).exists()) {
            getMvpView().shareImage(tmp);
            Logger.d("已经存在啦！");
            return;
        }

        getMvpView().showShareDialog();

        DownloadUtil
                .downloadFile(imageUrl, file.getAbsolutePath())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) throws Exception {

                        String fileName = s;
                        if (!fileName.contains(".gif")) {
                            fileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".gif";

                            File tmpFile = new File(fileName);
                            if (tmpFile.exists()) {

                            } else {

                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inPreferredConfig = Bitmap.Config.ARGB_4444;
                                Bitmap bitmap = BitmapFactory.decodeFile(s, options);

                                if (bitmap.getWidth() > 300 || bitmap.getHeight() > 300) {
                                    options.inSampleSize = 2;
                                    bitmap = BitmapFactory.decodeFile(s, options);
                                }

                                int w = bitmap.getWidth();
                                int h = bitmap.getHeight();
                                int[][] rgbs = new int[h][w];

                                for (int i = 0; i < w; i++) {
                                    for (int j = 0; j < h; j++) {
                                        rgbs[j][i] = bitmap.getPixel(i, j);
                                    }
                                }

                                FileOutputStream fos = new FileOutputStream(fileName);
                                new GifEncoder(fos, w, h, 0)
                                        .addImage(rgbs, new ImageOptions())
                                        .finishEncoding();
                                fos.close();

                                File tf = new File(s);
                                tf.delete();
                            }

                        }

                        return fileName;
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        getMvpView().hideShareDialog();
                        getMvpView().shareImage(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        getMvpView().hideShareDialog();
                        Logger.e(throwable, throwable.getMessage());
                    }
                });
    }
}
