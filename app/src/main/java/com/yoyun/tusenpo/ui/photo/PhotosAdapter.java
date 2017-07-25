package com.yoyun.tusenpo.ui.photo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yoyun.tusenpo.R;
import com.yoyun.tusenpo.data.model.Photo;
import com.yoyun.tusenpo.ui.common.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Yoyun on 2017/7/19.
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {

    private Context context;
    private RecyclerView recyclerView;
    private List<Photo> datas;
    private OnItemClickListener<Photo> onItemClickListener;

    public PhotosAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.context = this.recyclerView.getContext();
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, final int position) {
        final Photo data = this.datas.get(position);

        Glide.with(context)
                .load(data.getImgUrl())
                .placeholder(R.drawable.ic_cloud_download_black_245dp)
                .error(R.drawable.ic_cloud_off_black_256dp)
                .dontAnimate()
                .into(holder.ivImg);

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

    public PhotosAdapter setDatas(List<Photo> datas) {
        if (datas == null || datas.size() == 0) {
            return this;
        }
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

    public PhotosAdapter setDatasAndClear(List<Photo> datas) {
        if (this.datas != null) {
            this.datas.clear();
            this.datas.addAll(datas);
        } else {
            this.datas = datas;
        }
        notifyDataSetChanged();
        return this;
    }

    public PhotosAdapter setOnItemClickListener(OnItemClickListener<Photo> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivImg)
        ImageView ivImg;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
