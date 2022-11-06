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
import com.example.pbl6app.databinding.ItemMostUredServiceBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UredServiceAdapter extends RecyclerView.Adapter<UredServiceAdapter.UredHolder> {

    private ItemMostUredServiceBinding binding;
    private ArrayList<TypeOfJob> listData;
    private OnItemCLickListener<TypeOfJob> listener;

    public UredServiceAdapter(ArrayList<TypeOfJob> listData, OnItemCLickListener<TypeOfJob> listener) {
        this.listData = listData;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UredServiceAdapter.UredHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemMostUredServiceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UredHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UredServiceAdapter.UredHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class UredHolder extends RecyclerView.ViewHolder {
        private ItemMostUredServiceBinding binding;

        public UredHolder(ItemMostUredServiceBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindView(int position) {
            binding.tvNameUredService.setText(listData.get(position).getName());

            Picasso.get().load(listData.get(position).getLinkIMG()).into(binding.imgNameUredService);

            binding.layoutUredServiecItem.setOnClickListener(view -> {
                listener.onItemClick(listData.get(position));
            });
        }
    }
}
