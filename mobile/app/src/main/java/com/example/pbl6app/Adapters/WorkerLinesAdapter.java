package com.example.pbl6app.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.Worker;
import com.example.pbl6app.databinding.ItemWorkerLineBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WorkerLinesAdapter extends RecyclerView.Adapter<WorkerLinesAdapter.WorkerHolder> {
    private ItemWorkerLineBinding binding;
    private ArrayList<Worker> listData;
    private OnItemCLickListener<Worker> listener;

    public WorkerLinesAdapter(ArrayList<Worker> listData, OnItemCLickListener<Worker> listener) {
        this.listData = listData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkerLinesAdapter.WorkerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemWorkerLineBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WorkerHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerLinesAdapter.WorkerHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class WorkerHolder extends RecyclerView.ViewHolder {
        private ItemWorkerLineBinding binding;

        public WorkerHolder(ItemWorkerLineBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        private void bindView(int position) {
            Picasso.get().load(listData.get(position).getLinkIMG()).into(binding.imgWorkerLineItem);
            binding.tvNameWorker.setText(listData.get(position).getName());
            binding.tvTypeOfJobWorker.setText(listData.get(position).getNameTypeOfJob());
            binding.tvCountOfRate.setText(listData.get(position).getRate() + " (" + listData.get(position).getTotalReviews() + " Reviews)");

            binding.imgFavoWorkerLines.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setSelected(!v.isSelected());
                }
            });

            binding.layoutItemWorkerLines.setOnClickListener(view -> {
                listener.onItemClick(listData.get(position));
            });
        }
    }
}