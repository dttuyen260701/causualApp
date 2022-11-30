package com.example.pbl6app.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.pbl6app.Models.User;
import com.example.pbl6app.R;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.activities.LoginActivity;
import com.example.pbl6app.databinding.FragmentSettingsBinding;

public class SettingsFragment extends FragmentBase{

    private FragmentSettingsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
    }

    @Override
    protected void initView() {
        binding.btnProfile.setText(Constant.USER.getName());
    }

    @Override
    protected void initListener() {
        binding.btnProfile.setOnClickListener(view -> {
            addFragment(new ProfileFragment(), R.id.ctFragmentUser);
        });

        binding.btnLogOut.setOnClickListener(view -> {
            showDialog();
        });

        binding.btnHistory.setOnClickListener(view -> {
            addFragment(new HistoryFragment(), R.id.ctFragmentUser);
        });
    }

    private void showDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_dialog);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        dialog.setCancelable(true);

        ImageView imageView = dialog.findViewById(R.id.imageDialog);
        imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.sad_dialog));

        TextView txtContentDialog = dialog.findViewById(R.id.txtContentDialog);
        txtContentDialog.setText("Bạn có chắc chắn muốn đăng xuất?");

        TextView txtTitleDialog = dialog.findViewById(R.id.txtTitleDialog);
        txtTitleDialog.setText("Đăng Xuất");

        Button btnCancelDialog = dialog.findViewById(R.id.btnCancelDialog);
        btnCancelDialog.setOnClickListener(view -> dialog.dismiss());

        Button btnChangeDialog = dialog.findViewById(R.id.btnChangeDialog);
        btnChangeDialog.setOnClickListener(view -> {
            Constant.USER = new User();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });

        dialog.show();
    }

}


