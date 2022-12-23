package com.example.pbl6app.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.Order;
import com.example.pbl6app.R;
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
        private final ItemOrderLineBinding binding;

        public OrderHolder(ItemOrderLineBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        private void bindView(int position) {
            Order order = listData.get(position);
            String status;
            switch (order.getStatus()) {
                case Constant.ACCEPT_STATUS:
                    status = "Đã nhận";
                    break;
                case Constant.REJECT_STATUS:
                    status = "Từ chối";
                    break;
                case Constant.WAITING_STATUS:
                    status = "Đang chờ phản hồi";
                    break;
                case Constant.CANCEL_STATUS:
                    status = "Đã hủy";
                    break;
                case Constant.COMPLETED_STATUS:
                    status = "Đã hoàn thành";
                    break;
                default:
                    status = "Đang thực hiện";
                    break;
            }
            if(!order.isRead()) {
                binding.layoutOrderItemLine.setBackgroundColor(Color.parseColor("#98bb55"));
            }
            Picasso.get().load(Constant.BASE_URL + ((Constant.USER.getRole() == Constant.ROLE_WORKER) ? order.getCustomerImage() : order.getWorkerImage())).into(binding.imgItem);
            binding.tvItem.setText(listData.get(position).getJobInfoName());
            binding.tvWorker.setText((Constant.USER.getRole() == Constant.ROLE_WORKER) ? order.getCustomerName() : order.getWorkerName());
            binding.tvTime.setText(listData.get(position).getCreationTime());
            binding.tvStatus.setText(status);
            binding.layoutOrderItemLine.setOnClickListener((View view) -> {
                listener.onItemClick(listData.get(position));
            });
        }
    }

}
