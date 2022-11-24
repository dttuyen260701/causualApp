package com.example.pbl6app.Adapters;
/*
 * Created by tuyen.dang on 11/24/2022
 */

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.JobInfo;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.ItemJobinfoBinding;
import com.example.pbl6app.databinding.ItemJobinfoLineBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class JobInfoLinesAdapter extends RecyclerView.Adapter<JobInfoLinesAdapter.JobHolder> {

    private ItemJobinfoLineBinding binding;
    private ArrayList<JobInfo> listJobInfo;
    private OnItemCLickListener<JobInfo> listener;

    public JobInfoLinesAdapter(ArrayList<JobInfo> listJobInfo, OnItemCLickListener<JobInfo> listener) {
        this.listJobInfo = listJobInfo;
        this.listener = listener;
    }

    public void setList_data(ArrayList<JobInfo> list_data) {
        this.listJobInfo = list_data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public JobHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemJobinfoLineBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new JobHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull JobHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return listJobInfo.size();
    }

    public class JobHolder extends RecyclerView.ViewHolder {
        public JobHolder(@NonNull ItemJobinfoLineBinding binding) {
            super(binding.getRoot());
        }

        public void bindView(int position) {
            binding.layout.setOnClickListener(view -> {
                listener.onItemClick(listJobInfo.get(position));
            });
            binding.tvItem.setText(listJobInfo.get(position).getName());
        }
    }
}
