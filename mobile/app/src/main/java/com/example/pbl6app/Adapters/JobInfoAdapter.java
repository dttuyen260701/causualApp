package com.example.pbl6app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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
        return new JobHolder(binding, parent.getContext());
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
        private Context context;

        public JobHolder(@NonNull ItemJobinfoBinding binding, Context context) {
            super(binding.getRoot());
            this.context = context;
        }

        public void bindView(int position) {
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    context.getResources().getDisplayMetrics().widthPixels * 4 / 10,
                    context.getResources().getDisplayMetrics().widthPixels * 4 / 10 / 6 * 3
                );

            binding.layout.setLayoutParams(layoutParams);
            binding.layout.setOnClickListener(view -> {
                listener.onItemClick(listJobInfo.get(position));
            });
            Picasso.get().load(Constant.BASE_URL + listJobInfo.get(position).getLinkIMG()).into(binding.imgItem);
            binding.tvItem.setText(listJobInfo.get(position).getName());
        }
    }
}
