package com.example.timecapsule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.timecapsule.R;
import com.example.timecapsule.bean.NewsList;

import java.util.List;


public class MyNewsRecyclerAdapter extends RecyclerView.Adapter<MyNewsRecyclerAdapter.ItemView>{
    private List<NewsList> list;
    Context context;
    private OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public MyNewsRecyclerAdapter(List<NewsList> list, Context context) {
        this.list=list;
        this.context=context;
    }

    public void updateData(List<NewsList> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ItemView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_news_list, viewGroup, false);
        return new ItemView(view);
    }

    @Override
    public void onBindViewHolder(final ItemView itemView, int position) {

        NewsList bean = list.get(position);
        itemView.tvTitle.setText(bean.getTitle());
        itemView.tvInfo.setText(bean.getDate().substring(5,16)+"      "+bean.getAuthor_name());
        Glide.with(context)
                .load(bean.getThumbnail_pic_s())
                .into(itemView.imageView);
        itemView.bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener != null) {
                    int pos = itemView.getLayoutPosition();
                    onItemClickListener.onItemClick(itemView.itemView, pos);
                }
            }
        });
        itemView.bg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(onItemClickListener != null) {
                    int pos = itemView.getLayoutPosition();
                    onItemClickListener.onItemLongClick(itemView.itemView, pos);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ItemView extends  RecyclerView.ViewHolder{


        private TextView tvTitle;
        private TextView tvInfo;
        private ImageView imageView;
        private LinearLayout bg;
        public ItemView(View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvInfo = itemView.findViewById(R.id.tv_info);
            imageView = itemView.findViewById(R.id.iv_picture);
            bg = itemView.findViewById(R.id.bg);
        }


    }

}