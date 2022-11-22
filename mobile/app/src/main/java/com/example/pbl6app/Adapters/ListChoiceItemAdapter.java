package com.example.pbl6app.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.AddressTemp;
import com.example.pbl6app.R;

import java.util.ArrayList;

public class ListChoiceItemAdapter extends RecyclerView.Adapter<ListChoiceItemAdapter.ListChoiceHolder> {

    private ArrayList<AddressTemp> list_data;
    private OnItemCLickListener<AddressTemp> itemChoiceListener;

    public ListChoiceItemAdapter(ArrayList<AddressTemp> list_data, OnItemCLickListener<AddressTemp> itemChoiceListener) {
        this.list_data = list_data;
        this.itemChoiceListener = itemChoiceListener;
    }

    public void setList_data(ArrayList<AddressTemp> list_data) {
        this.list_data = list_data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListChoiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.feature_list_item, parent, false);
        return new ListChoiceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListChoiceHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    class ListChoiceHolder extends RecyclerView.ViewHolder {
        private TextView txt_name_item;
        private CheckBox cb_item;
        private ConstraintLayout layout_item_choice;

        public ListChoiceHolder(@NonNull View itemView) {
            super(itemView);
            txt_name_item = (TextView) itemView.findViewById(R.id.txt_name_item);
            cb_item = (CheckBox) itemView.findViewById(R.id.cb_item);
            layout_item_choice = (ConstraintLayout) itemView.findViewById(R.id.layout_item_choice);
        }

        public void bindView(int position) {
            txt_name_item.setText(list_data.get(position).getName());
            cb_item.setChecked(list_data.get(position).isCheck());

            layout_item_choice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemChoiceListener.onItemClick(list_data.get(position));
                    cb_item.setChecked(list_data.get(position).isCheck());
                }
            });

            cb_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemChoiceListener.onItemClick(list_data.get(position));
                }
            });
        }
    }
}
