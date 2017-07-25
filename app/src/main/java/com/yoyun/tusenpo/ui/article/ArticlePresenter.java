package com.yoyun.tusenpo.ui.article;

import com.orhanobut.logger.Logger;
import com.yoyun.tusenpo.data.model.Article;
import com.yoyun.tusenpo.data.model.Photo;
import com.yoyun.tusenpo.data.remote.Api;
import com.yoyun.tusenpo.ui.base.BasePresenter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Yoyun on 2017/7/18.
 */

public class ArticlePresenter extends BasePresenter<ArticleMvpView> {

    public void obtainArticles(int page) {
        Api.getInstance().obtainArticles(page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Article>>() {
                    @Override
                    public void accept(@NonNull List<Article> articles) throws Exception {
                        Logger.d(articles);
                        getMvpView().showArticles(articles);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Logger.e(throwable, throwable.getMessage());
                    }
                });
    }

    public void obtainArticleDetail(String pageUrl) {
        Api.getInstance().obtainArticleDetail(pageUrl)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Article>() {
                    @Override
                    public void accept(@NonNull Article article) throws Exception {
                        Logger.d(article);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Logger.e(throwable, throwable.getMessage());
                    }
                });
    }

    public void searchArticles(String keyword, int page) {
        Api.getInstance().searchArticles(keyword, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Article>>() {
                    @Override
                    public void accept(@NonNull List<Article> articleList) throws Exception {
                        Logger.d(articleList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Logger.e(throwable, throwable.getMessage());
                    }
                });
    }

    public void searchPhotos(String keyword, int page) {
        Api.getInstance().searchPhotos(keyword, page)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Photo>>() {
                    @Override
                    public void accept(@NonNull List<Photo> photoList) throws Exception {
                        Logger.d(photoList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Logger.e(throwable, throwable.getMessage());
                    }
                });
    }
}
