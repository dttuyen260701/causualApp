package com.example.pbl6app.Adapters;
/*
 * Created by tuyen.dang on 11/6/2022
 */

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.TypeOfJob;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.ItemMostUsedServiceBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsedServiceAdapter extends RecyclerView.Adapter<UsedServiceAdapter.UsedHolder> {

    private ItemMostUsedServiceBinding binding;
    private ArrayList<TypeOfJob> listData;
    private OnItemCLickListener<TypeOfJob> listener;

    public UsedServiceAdapter(ArrayList<TypeOfJob> listData, OnItemCLickListener<TypeOfJob> listener) {
        this.listData = listData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UsedServiceAdapter.UsedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemMostUsedServiceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UsedHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UsedServiceAdapter.UsedHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class UsedHolder extends RecyclerView.ViewHolder {
        private ItemMostUsedServiceBinding binding;

        public UsedHolder(ItemMostUsedServiceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(int position) {
            binding.tvNameUsedService.setText(listData.get(position).getName());

            Picasso.get().load(Constant.BASE_URL + listData.get(position).getLinkIMG()).into(binding.imgNameUsedService);

            binding.layoutUsedServiecItem.setOnClickListener(view -> {
                listener.onItemClick(listData.get(position));
            });
        }
    }
}
