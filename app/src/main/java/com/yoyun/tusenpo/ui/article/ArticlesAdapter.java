package com.yoyun.tusenpo.ui.article;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yoyun.tusenpo.R;
import com.yoyun.tusenpo.data.model.Article;
import com.yoyun.tusenpo.ui.common.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yoyun on 2017/7/19.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder> {

    private Context context;
    private RecyclerView recyclerView;
    private List<Article> datas;
    private OnItemClickListener<Article> onItemClickListener;

    public ArticlesAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.context = recyclerView.getContext();
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, final int position) {
        final Article data = this.datas.get(position);

        holder.tvTitle.setText(data.getTitle());
        holder.tvDate.setText(data.getDate());

        if (data.getPhotos() != null) {
            if (data.getPhotos().get(0) != null) {
                Glide.with(context)
                        .load(data.getPhotos().get(0).getImgUrl())
                        .placeholder(R.drawable.ic_cloud_download_black_245dp)
                        .error(R.drawable.ic_cloud_off_black_256dp)
                        .dontAnimate()
                        .into(holder.ivImg1);
            }
            if (data.getPhotos().get(1) != null) {
                Glide.with(context)
                        .load(data.getPhotos().get(1).getImgUrl())
                        .placeholder(R.drawable.ic_cloud_download_black_245dp)
                        .error(R.drawable.ic_cloud_off_black_256dp)
                        .dontAnimate()
                        .into(holder.ivImg2);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position, data);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.datas == null ? 0 : this.datas.size();
    }

    public void clearDatas() {
        if (this.datas != null) {
            this.datas.clear();
            notifyDataSetChanged();
        }
    }

    public ArticlesAdapter setDatas(List<Article> datas) {
        if (this.datas != null) {
            int start = this.datas.size();
            this.datas.addAll(datas);
            notifyItemRangeInserted(start, datas.size());
        } else {
            this.datas = datas;
            notifyItemRangeInserted(0, datas.size());
        }
        return this;
    }

    public ArticlesAdapter setDatasAndClear(List<Article> datas) {
        if (this.datas != null) {
            this.datas.clear();
            this.datas.addAll(datas);
        } else {
            this.datas = datas;
        }
        notifyDataSetChanged();
        return this;
    }

    public ArticlesAdapter setOnItemClickListener(OnItemClickListener<Article> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.ivImg1)
        ImageView ivImg1;
        @BindView(R.id.ivImg2)
        ImageView ivImg2;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
