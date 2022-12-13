package com.example.pbl6app.Adapters;
/*
 * Created by tuyen.dang on 11/24/2022
 */

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.PostOfDemand;
import com.example.pbl6app.R;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.ItemNewsPostBinding;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsPostAdapter extends RecyclerView.Adapter<NewsPostAdapter.PostHolder> {

    private ItemNewsPostBinding binding;
    private ArrayList<PostOfDemand> listNewPost;
    private OnItemCLickListener<PostOfDemand> listener;

    public NewsPostAdapter(ArrayList<PostOfDemand> listNewPost, OnItemCLickListener<PostOfDemand> listener) {
        this.listNewPost = listNewPost;
        this.listener = listener;
    }

    public void setList_data(ArrayList<PostOfDemand> listNewPost) {
        this.listNewPost = listNewPost;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemNewsPostBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PostHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return listNewPost.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {
        private boolean isShow = true;
        public PostHolder(@NonNull ItemNewsPostBinding binding) {
            super(binding.getRoot());
        }

        @SuppressLint("SetTextI18n")
        public void bindView(int position) {
            if(Constant.USER.getRole().equals("Thợ")){
                Picasso.get().load(Constant.BASE_URL + listNewPost.get(position).getCustomerImage()).into(binding.imgUser);
                binding.tvNameUser.setText(listNewPost.get(position).getCustomerName());
            }
            else{
                Picasso.get().load(Constant.BASE_URL + Constant.USER.getAvatar()).into(binding.imgUser);
                binding.tvNameUser.setText(Constant.USER.getName());
            }


            binding.layout.setOnClickListener(view -> {
                listener.onItemClick(listNewPost.get(position));
            });

            binding.tvContentPost.setText(listNewPost.get(position).getDescription());

            binding.tvTime.setText(listNewPost.get(position).getCreationTime());

            binding.tvJobInfo.setText("Tên công việc: "+listNewPost.get(position).getJobInfoName());
            binding.tvAddress.setText("Địa chỉ"+listNewPost.get(position).getAddress()
                    +" , "+listNewPost.get(position).getWard()
                    +" , "+listNewPost.get(position).getDistrict()
                    +" , "+listNewPost.get(position).getProvince()
            );


//            binding.tvTime.setText(formatter.format(new Date().getTime()));

//            binding.btnShowHide.setOnClickListener(view -> {
//                isShow = !isShow;
//                binding.btnShowHide.setImageResource((isShow) ? R.drawable.ic_up : R.drawable.ic_down);
//                binding.tvContentPost.setVisibility((isShow) ? View.VISIBLE : View.GONE);
//                binding.tvTime.setVisibility((isShow) ? View.VISIBLE : View.GONE);
//                binding.tvTotalLlike.setVisibility((isShow) ? View.VISIBLE : View.GONE);
//                binding.tvLinkTweet.setVisibility((isShow) ? View.VISIBLE : View.GONE);
//                binding.btnFavorite.setVisibility((isShow) ? View.VISIBLE : View.GONE);
//                binding.borderPost.setVisibility((isShow) ? View.VISIBLE : View.GONE);
//            });
        }
    }
}