package com.example.pbl6app.fragment;/*
 * Created by tuyen.dang on 10/20/2022
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pbl6app.databinding.FragmentProfileBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        enableEdit();
        saveData();
        setHintDate();
    }

    void enableEdit(){
        binding.imvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.imvEdit.setVisibility(View.GONE);
                binding.imvSave.setVisibility(View.VISIBLE);
                binding.edtAddress.setEnabled(true);
                binding.edtAge.setEnabled(true);
                binding.edtName.setEnabled(true);
                binding.edtPhone.setEnabled(true);
            }
        });
    }

    void saveData(){
        binding.imvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.imvEdit.setVisibility(View.VISIBLE);
                binding.imvSave.setVisibility(View.GONE);
                binding.edtAddress.setEnabled(false);
                binding.edtAge.setEnabled(false);
                binding.edtName.setEnabled(false);
                binding.edtPhone.setEnabled(false);
            }
        });
    }

    void setHintDate(){
        String timeStamp = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        binding.edtAge.setHint(timeStamp);
    }
}
