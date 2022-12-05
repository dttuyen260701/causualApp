package com.example.pbl6app.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.Order;
import com.example.pbl6app.Models.Worker;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.ItemOrderLineBinding;
import com.example.pbl6app.databinding.ItemWorkerOfPostBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WorkerItemOfPostAdapter extends RecyclerView.Adapter<WorkerItemOfPostAdapter.WorkerHolder>{
    private ItemWorkerOfPostBinding binding;
    private ArrayList<Worker> listData;
    private OnItemCLickListener<Worker> listener;

    public WorkerItemOfPostAdapter(ArrayList<Worker> listData, OnItemCLickListener<Worker> listener) {
        this.listData = listData;
        this.listener = listener;
    }

    public ArrayList<Worker> getListData() {
        return listData;
    }

    public void setListData(ArrayList<Worker> listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WorkerItemOfPostAdapter.WorkerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemWorkerOfPostBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WorkerItemOfPostAdapter.WorkerHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerItemOfPostAdapter.WorkerHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class WorkerHolder extends RecyclerView.ViewHolder {
        private ItemWorkerOfPostBinding binding;

        public WorkerHolder(ItemWorkerOfPostBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        private void bindView(int position) {
            //Picasso.get().load(Constant.BASE_URL + listData.get(position).getLinkIMG()).into(binding.avaWorker); //todo: image of worker
            binding.tvWorkerName.setText(listData.get(position).getName());
            binding.tvWorkerRate.setText(listData.get(position).getName());
            //binding.tvPrice.setText(listData.get(position).getCreationTime()); //todo: tv price
            binding.layoutWorkerPost.setOnClickListener(view -> {
                listener.onItemClick(listData.get(position));
            });

        }
    }
}
