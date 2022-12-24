package com.example.pbl6app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.JobInfo;
import com.example.pbl6app.Models.Rate;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.ItemJobinfoBinding;
import com.example.pbl6app.databinding.ItemRateWorkerBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemRateWorkerAdapter extends RecyclerView.Adapter<ItemRateWorkerAdapter.RateHolder>{
    private ArrayList<Rate> listRate;

    public ItemRateWorkerAdapter(ArrayList<Rate> listRate) {
        this.listRate = listRate;
    }

    @NonNull
    @Override
    public RateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRateWorkerBinding binding = ItemRateWorkerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RateHolder(binding, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull RateHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return listRate.size();
    }

    public class RateHolder extends RecyclerView.ViewHolder {
        private Context context;
        private ItemRateWorkerBinding binding;

        public RateHolder(@NonNull ItemRateWorkerBinding binding, Context context) {
            super(binding.getRoot());
            this.context = context;
            this.binding = binding;
        }

        public void bindView(int position) {
            binding.ratingBar.setRating(listRate.get(position).getRateAverage());
            if(listRate.get(position).getComment()!=null){
                binding.tvComment.setText(String.valueOf(listRate.get(position).getComment()));
            }
            else{
                binding.tvComment.setText("Không có bình luận");
            }
            binding.tvCustomerName.setText(listRate.get(position).getCustomerName());
            binding.tvCreationTime.setText(listRate.get(position).getCreationTime());
            Picasso.get().load(Constant.BASE_URL + listRate.get(position).getCustomerImage()).into(binding.customerAva);
        }
    }


}
