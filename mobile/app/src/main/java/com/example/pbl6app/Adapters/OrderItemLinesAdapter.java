package com.example.pbl6app.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.Order;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.ItemOrderLineBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderItemLinesAdapter extends RecyclerView.Adapter<OrderItemLinesAdapter.OrderHolder> {
    private ItemOrderLineBinding binding;
    private ArrayList<Order> listData;
    private OnItemCLickListener<Order> listener;

    public OrderItemLinesAdapter(ArrayList<Order> listData, OnItemCLickListener<Order> listener) {
        this.listData = listData;
        this.listener = listener;
    }

    public ArrayList<Order> getListData() {
        return listData;
    }

    public void setListData(ArrayList<Order> listData) {
        this.listData = listData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderItemLinesAdapter.OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemOrderLineBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderItemLinesAdapter.OrderHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemLinesAdapter.OrderHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class OrderHolder extends RecyclerView.ViewHolder {
        private ItemOrderLineBinding binding;

        public OrderHolder(ItemOrderLineBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        private void bindView(int position) {
//            Picasso.get().load(Constant.BASE_URL + listData.get(position).getLinkIMG()).into(binding.imgWorkerLineItem); //todo: image of job info
            binding.tvItem.setText(listData.get(position).getJobInfoName());
            binding.tvWorker.setText(listData.get(position).getWorkerName());
            binding.tvTime.setText(listData.get(position).getCreationTime());
            binding.layoutOrderItemLine.setOnClickListener(view -> {
                listener.onItemClick(listData.get(position));
            });
            switch (listData.get(position).getStatus()){
                case "In Progress":
                    binding.tvStatus.setText("Đang thực hiện");
                    break;
                case "Done":
                    binding.tvStatus.setText("Đã xong");
                    break;
                case "Waiting":
                    binding.tvStatus.setText("Chờ phản hồi");
                    break;

            }
        }
    }

}
