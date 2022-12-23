package com.example.pbl6app.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbl6app.Models.Worker;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.ItemWorkerRequestInPostBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WorkerRequestInPostAdapter extends RecyclerView.Adapter<WorkerRequestInPostAdapter.WorkerViewHolder> {

    private ArrayList<Worker> list;
    private ItemWorkerRequestInPostBinding binding;

    public WorkerRequestInPostAdapter(ArrayList<Worker> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public WorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemWorkerRequestInPostBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WorkerViewHolder(binding, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerViewHolder holder, int position) {
        holder.bindView(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(ArrayList<Worker> newList){
        String str = "";
        String str1 = "";
        this.list.clear();
        this.list.addAll(newList);

        for(Worker w : newList){
            str += w.getName() + " - ";
        }

        for(Worker w : list){
            str1 += w.getName() + " - ";
        }

        Log.e("tuan", str);
        Log.e("tuan1", str1);

        notifyDataSetChanged();
    }

    public class WorkerViewHolder extends RecyclerView.ViewHolder {


        public WorkerViewHolder(ItemWorkerRequestInPostBinding binding, Context context) {
            super(binding.getRoot());
        }

        public void bindView(Worker worker){
            binding.tvWorkerName.setText(worker.getName());
            binding.tvRate.setText(""+worker.getRate().getRateAverage());
            binding.tvReviewNumber.setText(worker.getTotalReviews() + " reviews");
            binding.tvJobList.setText("#"+worker.getListJobInfoString());

            String imageUrl = Constant.BASE_URL + worker.getLinkIMG();

            if(!imageUrl.isEmpty()){
                Picasso.get().load(imageUrl).into(binding.ivUseThumb);
            }
        }
    }
}
