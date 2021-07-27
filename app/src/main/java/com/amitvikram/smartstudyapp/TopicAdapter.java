package com.amitvikram.smartstudyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TopicAdapter extends  RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {
    private List<Topic> topicList;
    private Context context;
    private TopicAdapter.OnItemClickListener onItemClickListener;

    public TopicAdapter(List<Topic> topicList, Context context) {
        this.topicList = topicList;
        this.context = context;
    }

    @NonNull
    @Override
    public TopicAdapter.TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemresult, parent, false);

        return new TopicAdapter.TopicViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicAdapter.TopicViewHolder holders, int position) {
        final TopicAdapter.TopicViewHolder holder = holders;
        Topic topic = topicList.get(position);
        holder.textViewCategory.setText(topic.getTopicName());
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    public void setOnItemClickListener(TopicAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewCategory;
        TopicAdapter.OnItemClickListener onItemClickListener;

        public TopicViewHolder(@NonNull View itemView, final TopicAdapter.OnItemClickListener onItemClickListener) {
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
