package com.example.pbl6app.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.JobInfo;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.ItemJobinfoBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class JobInfoAdapter extends RecyclerView.Adapter<JobInfoAdapter.JobHolder> {

    private ItemJobinfoBinding binding;
    private ArrayList<JobInfo> listJobInfo;
    private OnItemCLickListener<JobInfo> listener;

    public JobInfoAdapter(ArrayList<JobInfo> listJobInfo, OnItemCLickListener<JobInfo> listener) {
        this.listJobInfo = listJobInfo;
        this.listener = listener;
    }

    @NonNull
    @Override
    public JobHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemJobinfoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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
        public JobHolder(@NonNull ItemJobinfoBinding binding) {
            super(binding.getRoot());
        }

        public void bindView(int position) {
            binding.layout.setOnClickListener(view -> {
                listener.onItemClick(listJobInfo.get(position));
            });
            Picasso.get().load(Constant.BASE_URL + listJobInfo.get(position).getLinkIMG()).into(binding.imgItem);
            binding.tvItem.setText(listJobInfo.get(position).getName());
        }
    }
}
