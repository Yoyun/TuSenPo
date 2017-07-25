package com.yoyun.tusenpo.ui.photo;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yoyun.tusenpo.R;
import com.yoyun.tusenpo.data.model.Photo;
import com.yoyun.tusenpo.ui.common.OnItemClickListener;
import com.yoyun.tusenpo.utils.ShareUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Yoyun on 2017/7/18.
 */

public class PhotoFragment extends Fragment implements PhotoMvpView {

    @BindView(R.id.rvPhotos)
    RecyclerView rvPhotos;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;
    private int page = 1;
    private PhotoPresenter photoPresenter;
    private PhotosAdapter photosAdapter;
    private Dialog shareDialog;

    public static PhotoFragment newInstance() {
        return new PhotoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        photoPresenter = new PhotoPresenter();
        photoPresenter.attachView(this);
        photoPresenter.obtainPhotos(page);
        unbinder = ButterKnife.bind(this, view);
        init();
        photoPresenter.obtainPhotos(page);
        return view;
    }

    private void init() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                photoPresenter.obtainPhotos(page);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                photoPresenter.obtainPhotos(page);
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
                                    photoPresenter.genGif(photo.getImgUrl());
                                }
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        photoPresenter.detachView();
        unbinder.unbind();
    }

    @Override
    public void showPhotos(List<Photo> photoList) {
        if (page == 1) {
            photosAdapter.setDatasAndClear(photoList);
            refreshLayout.finishRefresh();
        } else {
            photosAdapter.setDatas(photoList);
            refreshLayout.finishLoadmore();
        }
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
