package com.example.timecapsule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timecapsule.R;
import com.example.timecapsule.bean.CapsuleBean;
import com.example.timecapsule.utils.DateUtils;

import java.util.List;

public class MyCapsuleRecyclerAdapter extends RecyclerView.Adapter<MyCapsuleRecyclerAdapter.ItemView>{
    private List<CapsuleBean> list;
    Context context;
    private MyCapsuleRecyclerAdapter.OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(MyCapsuleRecyclerAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public MyCapsuleRecyclerAdapter(List<CapsuleBean> list, Context context) {
        this.list=list;
        this.context=context;
    }

    public void updateData(List<CapsuleBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ItemView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_title, viewGroup, false);
        return new ItemView(view);
    }

    @Override
    public void onBindViewHolder(final ItemView itemView, int position) {

        CapsuleBean bean = list.get(position);
        itemView.tvTitle.setText(bean.getTitle());
        itemView.tvTime.setText("Open Time: "+ DateUtils.getCurrentTime(bean.getOpenTime()));
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
        private TextView tvTime;
        private CardView bg;

        public ItemView(View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);
            bg = itemView.findViewById(R.id.bg);
        }


    }

}