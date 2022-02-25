package com.example.timecapsule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.timecapsule.R;
import com.example.timecapsule.bean.CommentBean;
import com.example.timecapsule.utils.DateUtils;

import java.util.List;

public class MyCommentRecyclerAdapter extends RecyclerView.Adapter<MyCommentRecyclerAdapter.ItemView>{
    private List<CommentBean> list;
    Context context;
    private MyCommentRecyclerAdapter.OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(MyCommentRecyclerAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public MyCommentRecyclerAdapter(List<CommentBean> list, Context context) {
        this.list=list;
        this.context=context;
    }

    public void updateData(List<CommentBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ItemView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment, viewGroup, false);
        return new ItemView(view);
    }

    @Override
    public void onBindViewHolder(final ItemView itemView, int position) {

        CommentBean bean = list.get(position);
        itemView.tvName.setText(bean.getAccount());
        itemView.tvTime.setText(DateUtils.getCurrentTime(bean.getTime()));
        itemView.tvComment.setText(bean.getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ItemView extends  RecyclerView.ViewHolder{


        private TextView tvName;
        private TextView tvTime;
        private TextView tvComment;

        public ItemView(View itemView){
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvComment = itemView.findViewById(R.id.tv_comment);
        }


    }

}