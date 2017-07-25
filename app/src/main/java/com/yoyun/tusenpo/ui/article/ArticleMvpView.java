package com.yoyun.tusenpo.ui.article;

import com.yoyun.tusenpo.data.model.Article;
import com.yoyun.tusenpo.ui.base.MvpView;

import java.util.List;

/**
 * Created by Yoyun on 2017/7/18.
 */

public interface ArticleMvpView extends MvpView {

    void showArticles(List<Article> articleList);
}
