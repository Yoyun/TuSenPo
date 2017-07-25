package com.yoyun.tusenpo.ui.article;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yoyun.tusenpo.R;
import com.yoyun.tusenpo.data.model.Article;
import com.yoyun.tusenpo.ui.base.BaseFragment;
import com.yoyun.tusenpo.ui.common.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Yoyun on 2017/7/18.
 */

public class ArticleFragment extends BaseFragment implements ArticleMvpView {

    ArticlePresenter articlePresenter;
    Unbinder unbinder;
    @BindView(R.id.rvArticles)
    RecyclerView rvArticles;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private ArticlesAdapter articlesAdapter;
    private int page = 1;

    public static ArticleFragment newInstance() {
        return new ArticleFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        articlePresenter = new ArticlePresenter();
        articlePresenter.attachView(this);
        unbinder = ButterKnife.bind(this, view);
        init();

        articlePresenter.obtainArticles(page);
        return view;
    }

    private void init() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                articlePresenter.obtainArticles(page);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                articlePresenter.obtainArticles(page);
            }
        });

        articlesAdapter = new ArticlesAdapter(rvArticles);
        rvArticles.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvArticles.setAdapter(articlesAdapter);
        articlesAdapter.setOnItemClickListener(new OnItemClickListener<Article>() {
            @Override
            public void onItemClick(View view, int position, Article article) {
                startActivity(ArticleDetailActivity.startAction(getContext(), article));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        articlePresenter.detachView();
        unbinder.unbind();
    }

    @Override
    public void showArticles(List<Article> articleList) {
        if (page == 1) {
            articlesAdapter.setDatasAndClear(articleList);
            refreshLayout.finishRefresh();
        } else {
            articlesAdapter.setDatas(articleList);
            refreshLayout.finishLoadmore();
        }
    }
}
