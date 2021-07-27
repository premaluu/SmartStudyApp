package com.amitvikram.smartstudyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ModelListAdapter extends  RecyclerView.Adapter<ModelListAdapter.ModelViewHolder> {
    private ArrayList<Models> modelList;
    private Context context;
    private ModelListAdapter.OnItemClickListener onItemClickListener;

    public ModelListAdapter(ArrayList<Models> topicList, Context context) {
        this.modelList = topicList;
        this.context = context;
    }

    @NonNull
    @Override
    public ModelListAdapter.ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemmodel, parent, false);

        return new ModelListAdapter.ModelViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ModelListAdapter.ModelViewHolder holders, int position) {
        final ModelListAdapter.ModelViewHolder holder = holders;
        holder.textViewModel.setText(modelList.get(position).getModelName());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public void setOnItemClickListener(ModelListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ModelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewModel;
        ModelListAdapter.OnItemClickListener onItemClickListener;

        public ModelViewHolder(@NonNull View itemView, final ModelListAdapter.OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            textViewModel = itemView.findViewById(R.id.txt_model_name);
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());

        }
    }
}

