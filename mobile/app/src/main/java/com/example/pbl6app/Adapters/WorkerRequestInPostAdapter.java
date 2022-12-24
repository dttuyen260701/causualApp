package com.example.pbl6app.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.Worker;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.ItemWorkerRequestInPostBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WorkerRequestInPostAdapter extends RecyclerView.Adapter<WorkerRequestInPostAdapter.WorkerViewHolder> {

    private ArrayList<Worker> list;
    private OnItemCLickListener<Worker> listenerWorker;
    private OnItemCLickListener<Worker> listenerAccept;

    public WorkerRequestInPostAdapter(ArrayList<Worker> list, OnItemCLickListener<Worker> listenerWorker, OnItemCLickListener<Worker> listenerAccept) {
        this.list = list;
        this.listenerWorker = listenerWorker;
        this.listenerAccept = listenerAccept;
    }

    @NonNull
    @Override
    public WorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemWorkerRequestInPostBinding binding = ItemWorkerRequestInPostBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WorkerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(ArrayList<Worker> newList){
        this.list.clear();
        this.list.addAll(newList);
    }

    public class WorkerViewHolder extends RecyclerView.ViewHolder {

        ItemWorkerRequestInPostBinding binding;
        public WorkerViewHolder(ItemWorkerRequestInPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(int position){
            Worker worker = list.get(position);
            binding.tvWorkerName.setText(worker.getName());
            binding.tvRate.setText(""+worker.getRate().getRateAverage());
            binding.tvReviewNumber.setText(worker.getTotalReviews() + " reviews");
            binding.tvJobList.setText("#"+worker.getListJobInfoString());

            String imageUrl = Constant.BASE_URL + worker.getLinkIMG();

            if(!imageUrl.isEmpty()){
                Picasso.get().load(imageUrl).into(binding.ivUseThumb);
            }

            binding.layoutIem.setOnClickListener(view -> {
                listenerWorker.onItemClick(worker);
            });

            binding.btnAcceptWorker.setOnClickListener(view -> {
                listenerAccept.onItemClick(worker);
            });
        }
    }
}
