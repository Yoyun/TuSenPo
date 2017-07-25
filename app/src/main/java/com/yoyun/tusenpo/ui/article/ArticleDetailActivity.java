package com.yoyun.tusenpo.ui.article;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yoyun.tusenpo.R;
import com.yoyun.tusenpo.data.model.Article;
import com.yoyun.tusenpo.data.model.Photo;
import com.yoyun.tusenpo.ui.base.BaseActivity;
import com.yoyun.tusenpo.ui.common.OnItemClickListener;
import com.yoyun.tusenpo.ui.photo.PhotosAdapter;
import com.yoyun.tusenpo.utils.ShareUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleDetailActivity extends BaseActivity implements ArticleDetailMvpView {

    private static final String EXTRA_ARTICLE = "extra_article";

    public static Intent startAction(Context context, Article article) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(EXTRA_ARTICLE, (Parcelable) article);
        return intent;
    }

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.rvPhotos)
    RecyclerView rvPhotos;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private ArticleDetailPresenter articleDetailPresenter;
    private Article article;
    private PhotosAdapter photosAdapter;
    private Dialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        articleDetailPresenter = new ArticleDetailPresenter();
        articleDetailPresenter.attachView(this);
        ButterKnife.bind(this);

        initExtras(getIntent());
        initView();

        articleDetailPresenter.obtainDetail(article.getPageUrl());
    }

    private void initView() {
        toolbar.setTitle(article.getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        photosAdapter = new PhotosAdapter(rvPhotos);
        rvPhotos.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
        rvPhotos.setAdapter(photosAdapter);
        photosAdapter.setOnItemClickListener(new OnItemClickListener<Photo>() {
            @Override
            public void onItemClick(View view, int position, final Photo photo) {
                new MaterialDialog.Builder(getContext())
                        .title("操作")
                        .items(new String[]{
                                "分享",
                                "保存"
                        })
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                if (position == 0) {
                                    articleDetailPresenter.genGif(photo.getImgUrl());
                                }
                            }
                        })
                        .show();
            }
        });

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                articleDetailPresenter.obtainDetail(article.getPageUrl());
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        articleDetailPresenter.detachView();
    }

    private void initExtras(Intent intent) {
        article = intent.getParcelableExtra(EXTRA_ARTICLE);
    }

    @Override
    public void showPhotos(List<Photo> photoList) {
        refreshLayout.finishRefresh();
        photosAdapter.setDatasAndClear(photoList);
    }

    @Override
    public void showShareDialog() {
        if (shareDialog == null) {
            shareDialog = new MaterialDialog.Builder(getContext())
                    .content("分享中...")
                    .progress(true, 0)
                    .build();
        }
        shareDialog.show();
    }

    @Override
    public void hideShareDialog() {
        if (shareDialog != null) {
            shareDialog.dismiss();
        }
    }

    @Override
    public void shareImage(String imagePath) {
        ShareUtil.shareSingleImage(getContext(), imagePath);
    }
}
