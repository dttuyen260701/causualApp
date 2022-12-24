package com.example.pbl6app.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.JobInfo;
import com.example.pbl6app.Models.Order;
import com.example.pbl6app.databinding.ItemJobInfoOrderDetailBinding;
import com.example.pbl6app.databinding.ItemOrderLineBinding;

import java.util.ArrayList;

public class JobInfoOrderDetailAdapter extends RecyclerView.Adapter<JobInfoOrderDetailAdapter.JobInfoHolder>{
    private ArrayList<JobInfo> listData;

    public JobInfoOrderDetailAdapter(ArrayList<JobInfo> listData) {
        this.listData = listData;
    }

    public ArrayList<JobInfo> getListData() {
        return listData;
    }

    public void setListData(ArrayList<JobInfo> listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public JobInfoOrderDetailAdapter.JobInfoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemJobInfoOrderDetailBinding binding = ItemJobInfoOrderDetailBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new JobInfoOrderDetailAdapter.JobInfoHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull JobInfoOrderDetailAdapter.JobInfoHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class JobInfoHolder extends RecyclerView.ViewHolder {
        private ItemJobInfoOrderDetailBinding binding;

        public JobInfoHolder(ItemJobInfoOrderDetailBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        private void bindView(int position) {
            binding.tvName.setText(listData.get(position).getName());
            binding.tvPrice.setText(listData.get(position).getPrice());
        }
    }
}
