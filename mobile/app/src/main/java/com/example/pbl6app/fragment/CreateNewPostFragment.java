package com.example.pbl6app.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pbl6app.Adapters.JobInfoAdapter;
import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.AddressTemp;
import com.example.pbl6app.Models.PostOfDemand;
import com.example.pbl6app.Models.WorkerDetail;
import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.activities.LoginActivity;
import com.example.pbl6app.activities.SignupActivity;
import com.example.pbl6app.databinding.FragmentCreateNewPostBinding;
import com.example.pbl6app.databinding.FragmentNewfeedBinding;
import com.example.pbl6app.databinding.FragmentWorkerDetailBinding;

import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNewPostFragment extends FragmentBase {

    private FragmentCreateNewPostBinding binding;
    private ChoiceFragment choiceFragment;
    private static String idTypeOfJobChosen = "-1", idNumberHourChosen="-1", idJobInfoChosen="-1";
    private String userAddress = "", userPoint = "";

    public static String getIdTypeOfJobChosen() {
        return idTypeOfJobChosen;
    }
    public static String getIdNumberHourChosen() {
        return idNumberHourChosen;
    }
    public static String getIdJobInfoChosen() {
        return idJobInfoChosen;
    }


    public CreateNewPostFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateNewPostBinding.inflate(inflater, container, false);
        return binding.getRoot();    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
    }

    @Override
    protected void initView() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        binding.tvDate.setText(dateFormat.format(date));
        binding.tvDate.setEnabled(false);
        binding.tvJobInfo.setEnabled(false);
        userAddress = Constant.USER.getAddress();
        userPoint = Constant.USER.getAddressPoint();
        binding.tvAdressCreatePost.setText(userAddress);
    }

    @Override
    protected void initListener() {
        binding.btnBack.setOnClickListener(view -> {
            backToPreviousFrag();
        });

        binding.btnSubmit.setOnClickListener(view->{
            if(binding.tvRequired1.getVisibility() == View.VISIBLE
                    || idJobInfoChosen.equals("")
                    || idTypeOfJobChosen.equals("")
                    || idNumberHourChosen.equals("")
            ){
                Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            }
            else{
                onSubmitData();
            }
        });

        binding.edtDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                binding.tvRequired1.setVisibility(
                        binding.edtDescription.getText().toString().isEmpty()
                                ? View.VISIBLE
                                : View.GONE
                );
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.btnPickAdressPost.setOnClickListener(view -> {
            addFragment(new MapFragment_Parent((address, point) -> {
                binding.tvAdressCreatePost.setText(address);
                userAddress = address;
                userPoint = point;
                backToPreviousFrag();
            }), R.id.ctFragmentUser);
        });

        binding.tvTypeOfJob.setOnClickListener(view -> {
            onChoiceShow(
                    Constant.TYPE_OF_JOB_DATA,
                    "Loại công việc",
                    item->{
                        binding.tvJobInfo.setEnabled(true);
                        binding.tvTypeOfJob.setText(item.getName());
                        idTypeOfJobChosen = item.getId();
                        choiceFragment.dismiss();
                    }
            ,"");
        });

        binding.tvJobInfo.setOnClickListener(view->{
            onChoiceShow(
                    Constant.JOB_INFO_DATA,
                    "Tên công việc",
                    item->{
                        binding.tvJobInfo.setText(item.getName());
                        idJobInfoChosen = item.getId();
                        choiceFragment.dismiss();
                    }
                    ,idTypeOfJobChosen);
        });
    }

    private void onSubmitData(){
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);

        Map<String , String> options = new HashMap<>();
        options.put("userId", Constant.USER.getId());
        options.put("jobInfoId", idJobInfoChosen);
        options.put("description", binding.edtDescription.getText().toString());
        options.put("note", "null");
        options.put("endTime", binding.tvDate.getText().toString());
        options.put("address", userAddress);
        options.put("addressPoint", userPoint);

        ApiService.apiService.createNewPost(options, Constant.USER.getId()
        ).enqueue(new Callback<ResponseRetrofit<PostOfDemand>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<PostOfDemand>> call, Response<ResponseRetrofit<PostOfDemand>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                if(response.code() == HttpURLConnection.HTTP_OK) {
                    if(response.body().isSuccessed()) {
                        showSuccessDialog();
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<PostOfDemand>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                Log.e("NEW_POST", "onFailure: ", t);
                Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onChoiceShow(
            int keyData,
            String title,
            OnItemCLickListener<AddressTemp> onItemCLickListener,
            String IdLoad) {
        choiceFragment = new ChoiceFragment(keyData, title, onItemCLickListener, IdLoad);
        choiceFragment.show(getActivity().getSupportFragmentManager(), choiceFragment.getTag());
    }

    private void showSuccessDialog() {
        binding.edtDescription.setText("");
        binding.tvJobInfo.setText("");
        binding.tvTypeOfJob.setText("");

        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_dialog_arrive);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        dialog.setCancelable(true);

        TextView txtTitleDialog = dialog.findViewById(R.id.txtTitleDialog);
        txtTitleDialog.setText("Tạo bài viết mới thành công");

        Button btnOK = dialog.findViewById(R.id.btnOk);
        btnOK.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.show();
    }

}