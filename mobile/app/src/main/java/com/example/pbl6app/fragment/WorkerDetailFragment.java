package com.example.pbl6app.fragment;
/*
 * Created by tuyen.dang on 11/9/2022
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pbl6app.Adapters.JobInfoAdapter;
import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.JobInfo;
import com.example.pbl6app.Models.WorkerDetail;
import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.FragmentWorkerDetailBinding;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkerDetailFragment extends FragmentBase {

    private static final int MY_PERMISSION_REQUEST_CODE_CALL_PHONE = 112;
    private FragmentWorkerDetailBinding binding;
    private WorkerDetail worker;
    private JobInfoAdapter adapter;

    public WorkerDetailFragment(WorkerDetail worker) {
        this.worker = worker;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkerDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
        loadData();
    }

    @Override
    protected void initView() {
        Picasso.get().load(Constant.BASE_URL + worker.getLinkIMG()).into(binding.imvAva);
        binding.tvUsername.setText(worker.getName());
        binding.tvAdress.setText(worker.getAddress());
        binding.tvPhone.setText(worker.getPhone());
        binding.tvWorkingTime.setText(worker.getWorkingTime());
        binding.ratingBarReviewFirstRow.setRating(worker.getRate().getRateAverage());
        binding.totalReviews.setText("(" + worker.getTotalReviews() + " đánh giá)");

        binding.statusPoint.setSelected(worker.isWorkerStatus() == Constant.BUSY_STATUS);

        adapter = new JobInfoAdapter(worker.getListJobList(), new OnItemCLickListener<JobInfo>() {
            @Override
            public void onItemClick(JobInfo item) {

            }
        });

        binding.rclJobInfo.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.VERTICAL, false));
        binding.rclJobInfo.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        binding.btnBack.setOnClickListener(view -> {
            backToPreviousFrag();
        });

        binding.btnOrder.setOnClickListener(view -> {
            addFragment(new BookingOrderScreenFragment(worker), R.id.ctFragmentUser);
        });

        binding.totalReviews.setOnClickListener(view->{
            addFragment(new ListRateWorkerFragment(worker), R.id.ctFragmentUser);

        });

        binding.imvAva.setOnClickListener(v -> {
//            addFragment(new RateWorkerFragment(worker), R.id.ctFragmentUser);
        });

        binding.btnCallWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermissionAndCall(worker.getPhone());
            }
        });

    }

    private void loadData() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);

        ApiService.apiService.getWorkerDetail(worker.getId()).enqueue(new Callback<ResponseRetrofit<WorkerDetail>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<WorkerDetail>> call, Response<ResponseRetrofit<WorkerDetail>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (response.body().isSuccessed()) {
                        if(response.body().getResultObj() != null) {
                            worker = response.body().getResultObj();
                            initView();
                        }
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
            public void onFailure(Call<ResponseRetrofit<WorkerDetail>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                Log.e("TTT", "onFailure: ",t );
                if(getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void askPermissionAndCall(String phoneNB) {

        // With Android Level >= 23, you have to ask the user
        // for permission to Call.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // 23

            // Check if we have Call permission
            int sendSmsPermisson = ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.CALL_PHONE);

            if (sendSmsPermisson != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSION_REQUEST_CODE_CALL_PHONE
                );
                return;
            }
        }
        callNow(phoneNB);
    }

    @SuppressLint("MissingPermission")
    private void callNow(String phoneNB) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNB));
        startActivity(callIntent);
    }
}
