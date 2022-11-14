package com.example.pbl6app.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pbl6app.Adapters.ListChoiceItemAdapter;
import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.AddressTemp;
import com.example.pbl6app.R;
import com.example.pbl6app.databinding.LayoutListChoiceBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class ChoiceFragment extends BottomSheetDialogFragment {
    private BottomSheetBehavior mBehavior;
    private LayoutListChoiceBinding binding;
    private ArrayList<AddressTemp> list_data;
    private OnItemCLickListener<AddressTemp> listener;
    private ListChoiceItemAdapter adapter;
    private String title;

    public ChoiceFragment(ArrayList<AddressTemp> list_data, String title, OnItemCLickListener<AddressTemp> listener) {
        this.listener = listener;
        this.list_data = list_data;
        this.title = title;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LayoutListChoiceBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setUp();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.layout_list_choice, null);
        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        return dialog;
    }

    private void setUp() {
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                getActivity().getResources().getDisplayMetrics().widthPixels,
                getActivity().getResources().getDisplayMetrics().heightPixels * 7 / 10
        );
        binding.layoutMainChoice.setLayoutParams(layoutParams);
        binding.txtTileOption.setText(title);
        binding.btnCancelChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        adapter = new ListChoiceItemAdapter(list_data, listener);
        binding.rclChoice.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rclChoice.setAdapter(adapter);

        binding.searchViewChoice.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search(s);
                if (s.length() == 0) {
                    adapter.setList_data(list_data);
                }
                return false;
            }
        });
    }

    private void search(String text) {
        ArrayList<AddressTemp> list_search = new ArrayList<>();
        for (AddressTemp i : list_data) {
            if (i.getName().toLowerCase().contains(text.toLowerCase()))
                list_search.add(i);
        }
        if (list_search.isEmpty()) {
            if (text.length() > 0)
                Toast.makeText(getActivity(), "Không tìm thấy dữ liệu!!!", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setList_data(list_search);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}
