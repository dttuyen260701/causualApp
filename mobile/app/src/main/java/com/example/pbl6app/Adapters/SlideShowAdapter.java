package com.example.pbl6app.Adapters;
/*
 * Created by tuyen.dang on 11/6/2022
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbl6app.Models.SlideItem;
import com.example.pbl6app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SlideShowAdapter extends RecyclerView.Adapter<SlideShowAdapter.PhotoViewHolder> {
    private ArrayList<SlideItem> arrayList;
    private static int selected_index = 0;

    public SlideShowAdapter(ArrayList<SlideItem> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_slide_show, parent, false);
        return new PhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideShowAdapter.PhotoViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_item;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            img_item = (ImageView) itemView.findViewById(R.id.img_item);
        }

        public void bindView(int position) {
            Picasso.get().load(arrayList.get(position).getLinkImg()).into(img_item);
        }
    }

    public static void setSelected_index(int selected_index) {
        SlideShowAdapter.selected_index = selected_index;
    }
}
