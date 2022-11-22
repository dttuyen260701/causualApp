package com.example.pbl6app.fragment;/*
 * Created by tuyen.dang on 10/20/2022
 */

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.AddressTemp;
import com.example.pbl6app.Models.User;
import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.FragmentProfileBinding;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends FragmentBase {

    private FragmentProfileBinding binding;
    int REQUEST_CODE_CAMERA = 123, REQUEST_CODE_FOLDER = 456;
    private Bitmap bitmap;
    private static String idProvinceChosen = "-1", idDistrictChosen = "-1", idWardChosen = "-1", idGenderChonse = "-1";
    private ChoiceFragment choiceFragment;

    public static String getIdProvinceChosen() {
        return idProvinceChosen;
    }

    public static String getIdDistrictChosen() {
        return idDistrictChosen;
    }

    public static String getIdWardChosen() {
        return idWardChosen;
    }

    public static String getIdGenderChonse() {
        return idGenderChonse;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initListener();
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initView() {

        idDistrictChosen = (Constant.USER.getDistrictId() == null) ? "" : Constant.USER.getDistrictId();
        idProvinceChosen = (Constant.USER.getProvinceId() == null) ? "" : Constant.USER.getProvinceId();
        idWardChosen = (Constant.USER.getWardId() == null) ? "" : Constant.USER.getWardId();
        idGenderChonse = Constant.USER.getGender() + "";

        binding.imvAddAva.setEnabled(false);
        binding.tvUsername.setText(Constant.USER.getUserName());
        binding.edtEmail.setText(Constant.USER.getEmail());
        binding.edtName.setText(Constant.USER.getName());
        binding.edtAge.setText(Constant.USER.getDateOfBirth());
        binding.edtPhone.setText(Constant.USER.getPhoneNumber());

        String gender = (Constant.USER.getGender() == 0) ? "Nam" : ((Constant.USER.getGender() == 1) ? "Nữ" : "Không xác định");
        binding.edtGender.setText(gender);
        binding.tvProvince.setText(Constant.USER.getProvinceName());
        binding.tvDistrict.setText(Constant.USER.getDistrictName());
        binding.tvWard.setText(Constant.USER.getWardName());
        binding.edtAddress.setText(Constant.USER.getAddress());

        binding.progressBar2.setVisibility(View.VISIBLE);
        Picasso.get().load(Constant.BASE_URL + Constant.USER.getAvatar()).networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(binding.imvUser, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        binding.progressBar2.setVisibility(View.GONE);
                        Picasso.get().load(Constant.BASE_URL + Constant.USER.getAvatar()).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                ProfileFragment.this.bitmap = bitmap;
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        binding.progressBar2.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    protected void initListener() {

        binding.imvBack.setOnClickListener(view -> {
            backToPreviousFrag();
        });

        binding.edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.tvWarningPhone.setVisibility((s.length() == 10) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.tvWarningName.setVisibility((s.length() > 0) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.tvWarningAdress.setVisibility((s.length() > 0) ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.edtAge.setOnClickListener(view -> {
            showDatePickerDialog();
        });

        binding.imvEdit.setOnClickListener(view -> {
            binding.imvEdit.setVisibility(View.GONE);
            binding.imvSave.setVisibility(View.VISIBLE);
            binding.edtAddress.setEnabled(true);
            binding.edtAge.setEnabled(true);
            binding.edtName.setEnabled(true);
            binding.edtPhone.setEnabled(true);
            binding.imvAddAva.setEnabled(true);
            binding.edtGender.setEnabled(true);
            binding.tvProvince.setEnabled(true);
            binding.tvDistrict.setEnabled(true);
            binding.edtAddress.setEnabled(true);
            binding.tvWard.setEnabled(true);
        });

        binding.imvSave.setOnClickListener(view -> {

            if (binding.tvWarningAdress.getVisibility() == View.VISIBLE
                    || binding.tvWarningName.getVisibility() == View.VISIBLE
                    || binding.tvWarningPhone.getVisibility() == View.VISIBLE
                    || idDistrictChosen.equals("")
                    || idWardChosen.equals("")) {

                Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();

            } else {

                onSubmitData();

                binding.imvEdit.setVisibility(View.VISIBLE);
                binding.imvSave.setVisibility(View.GONE);
                binding.edtAddress.setEnabled(false);
                binding.edtAge.setEnabled(false);
                binding.edtName.setEnabled(false);
                binding.edtPhone.setEnabled(false);
                binding.imvAddAva.setEnabled(false);
                binding.edtGender.setEnabled(false);
                binding.tvProvince.setEnabled(false);
                binding.tvDistrict.setEnabled(false);
                binding.tvWard.setEnabled(false);
                binding.edtAddress.setEnabled(false);
                binding.tvWard.setEnabled(false);

            }
        });

        binding.imvAddAva.setOnClickListener(view -> {
            showDialog();
        });

        binding.edtGender.setOnClickListener(v -> {
            onChoiceShow(Constant.GENDER_DATA,
                    "Giới tính",
                    item -> {
                        binding.edtGender.setText(item.getName());
                        idGenderChonse = item.getId();
                        choiceFragment.dismiss();
                    }, "");
        });

        binding.tvProvince.setOnClickListener(view -> {
            onChoiceShow(Constant.PROVINCE_DATA,
                    "Tỉnh/ Thành Phố",
                    item -> {
                        binding.tvDistrict.setEnabled(true);
                        binding.tvProvince.setText(item.getName());
                        idProvinceChosen = item.getId();
                        idDistrictChosen = "";
                        idWardChosen = "";
                        choiceFragment.dismiss();
                        binding.tvDistrict.setText("Vui lòng chọn quận/huyện");
                        binding.tvWard.setText("Vui lòng chọn phường/xã");
                    }, "");
        });

        binding.tvDistrict.setOnClickListener(view -> {
            onChoiceShow(Constant.DISTRICT_DATA,
                    "Quận/ Huyện",
                    item -> {
                        binding.tvWard.setEnabled(true);
                        binding.tvDistrict.setText(item.getName());
                        idDistrictChosen = item.getId();
                        idWardChosen = "";
                        choiceFragment.dismiss();
                        binding.tvWard.setText("Vui lòng chọn phường/xã");
                    }, idProvinceChosen);
        });

        binding.tvWard.setOnClickListener(view -> {
            onChoiceShow(Constant.WARD_DATA,
                    "Phường/ Xã",
                    item -> {
                        binding.tvWard.setText(item.getName());
                        idWardChosen = item.getId();
                        choiceFragment.dismiss();
                    }, idDistrictChosen);
        });

    }

    public void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment(result -> binding.edtAge.setText(result));
        if (getActivity() != null) {
            newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
        }
    }

    private void onChoiceShow(
            int keyData,
            String title,
            OnItemCLickListener<AddressTemp> onItemCLickListener,
            String IdLoad) {
        choiceFragment = new ChoiceFragment(keyData, title, onItemCLickListener, IdLoad);
        choiceFragment.show(getActivity().getSupportFragmentManager(), choiceFragment.getTag());
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

        TextView txtTitleDialog = dialog.findViewById(R.id.txtTitleDialog);
        txtTitleDialog.setText("");

        TextView txtContentDialog = dialog.findViewById(R.id.txtContentDialog);
        txtContentDialog.setText("Chọn ảnh đại diện của bạn.");
        Button btnCancelDialog = dialog.findViewById(R.id.btnCancelDialog);
        btnCancelDialog.setText("Máy ảnh");
        btnCancelDialog.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getActivity().startActivityFromFragment(ProfileFragment.this
                    , intent, REQUEST_CODE_CAMERA);
            dialog.dismiss();
        });

        Button btnChangeDialog = dialog.findViewById(R.id.btnChangeDialog);
        btnChangeDialog.setText("Thư viện");
        btnChangeDialog.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            getActivity().startActivityFromFragment(ProfileFragment.this
                    , intent, REQUEST_CODE_FOLDER);
            dialog.dismiss();
        });
        dialog.show();
    }

    private void onSubmitData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);

        File file = new File(getContext().getCacheDir(), "Test");
        try {
            file.createNewFile();

            //Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ApiService.apiService.updateCustomer(
                Constant.USER.getId(),
                RequestBody.create(MediaType.parse("multipart/form-data"), binding.edtName.getText().toString().trim()),
                RequestBody.create(MediaType.parse("multipart/form-data"), binding.edtPhone.getText().toString().trim()),
                RequestBody.create(MediaType.parse("multipart/form-data"), Constant.USER.getId()),
                RequestBody.create(MediaType.parse("multipart/form-data"), idGenderChonse),
                RequestBody.create(MediaType.parse("multipart/form-data"), binding.edtAddress.getText().toString().trim()),
                RequestBody.create(MediaType.parse("multipart/form-data"), "125- 220"),
                RequestBody.create(MediaType.parse("multipart/form-data"), binding.edtAge.getText().toString().trim()),
                RequestBody.create(MediaType.parse("multipart/form-data"), idProvinceChosen),
                RequestBody.create(MediaType.parse("multipart/form-data"), binding.tvProvince.getText().toString()),
                RequestBody.create(MediaType.parse("multipart/form-data"), idDistrictChosen),
                RequestBody.create(MediaType.parse("multipart/form-data"), binding.tvDistrict.getText().toString()),
                RequestBody.create(MediaType.parse("multipart/form-data"), idWardChosen),
                RequestBody.create(MediaType.parse("multipart/form-data"), binding.tvWard.getText().toString()),
                MultipartBody.Part.createFormData("avatar", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
        ).enqueue(new Callback<ResponseRetrofit<User>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<User>> call, Response<ResponseRetrofit<User>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (response.body().isSuccessed()) {
                        String id = Constant.USER.getId();
                        Constant.USER = response.body().getResultObj();
                        Constant.USER.setId(id);
                        initView();
                        Toast.makeText(getContext(), "Cập nhật thành công!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        if(getContext() != null) {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if(getContext() != null) {
                        Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<User>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                Log.e("TTT", "onFailure: ", t);
                if(getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");//Key mặc định data
            ProfileFragment.this.bitmap = bitmap;
            binding.imvUser.setImageBitmap(bitmap);
        }
        //chọn hình trong file;
        if (requestCode == REQUEST_CODE_FOLDER && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ProfileFragment.this.bitmap = bitmap;
                binding.imvUser.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


}
