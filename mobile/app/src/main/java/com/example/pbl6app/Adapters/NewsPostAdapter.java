package com.example.pbl6app.Adapters;
/*
 * Created by tuyen.dang on 11/24/2022
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.R;
import com.example.pbl6app.databinding.ItemNewsPostBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsPostAdapter extends RecyclerView.Adapter<NewsPostAdapter.PostHolder> {

    private ItemNewsPostBinding binding;
    private ArrayList<String> listNewPost;
    private OnItemCLickListener<String> listener;

    public NewsPostAdapter(ArrayList<String> listNewPost, OnItemCLickListener<String> listener) {
        this.listNewPost = listNewPost;
        this.listener = listener;
    }

    public void setList_data(ArrayList<String> listNewPost) {
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

        public void bindView(int position) {
            binding.layout.setOnClickListener(view -> {
                listener.onItemClick(listNewPost.get(position));
            });

            binding.tvNameUser.setText("Name " + (position + 1));

            binding.tvContentPost.setText("Content " + (position + 1));
            Picasso.get().load(listNewPost.get(position)).into(binding.imgUser);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            binding.tvTime.setText(formatter.format(new Date().getTime()));

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