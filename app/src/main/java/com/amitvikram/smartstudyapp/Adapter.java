package com.amitvikram.smartstudyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<Result> resultList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public Adapter(List<Result> resultList, Context context) {
        this.resultList = resultList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemresult, parent, false);

        return new MyViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holders, int position) {
        final MyViewHolder holder = holders;
        Result result = resultList.get(position);
        holder.textViewCategory.setText(result.getCategory());
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewCategory;
        OnItemClickListener onItemClickListener;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            textViewCategory = itemView.findViewById(R.id.txt_category);
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());

        }
    }
}
