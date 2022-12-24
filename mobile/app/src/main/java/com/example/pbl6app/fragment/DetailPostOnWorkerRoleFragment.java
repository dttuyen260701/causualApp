package com.example.pbl6app.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.pbl6app.Adapters.WorkerRequestInPostAdapter;
import com.example.pbl6app.Models.PostOfDemand;
import com.example.pbl6app.Models.Rate;
import com.example.pbl6app.Models.Worker;
import com.example.pbl6app.Models.WorkerDetail;
import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.Utils.FirebaseRepository;
import com.example.pbl6app.Utils.Methods;
import com.example.pbl6app.databinding.FragmentDetailPostOnWorkerRoleBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailPostOnWorkerRoleFragment extends FragmentBase {

    public static String POST_ID = "post_id";
    private FragmentDetailPostOnWorkerRoleBinding binding;
    private String mPodID;
    private Boolean isContainInWorkerList = false;
    private WorkerRequestInPostAdapter mAdapter;
    private ArrayList<Worker> listWorkerRequesting = new ArrayList<>();
    private PostOfDemand mPOD;
    private boolean isFirstLoad = true;
    private ValueEventListener valueEventListener;

    public static DetailPostOnWorkerRoleFragment newInstance(String postID) {
        DetailPostOnWorkerRoleFragment fragment = new DetailPostOnWorkerRoleFragment();
        Bundle args = new Bundle();
        args.putString(POST_ID, postID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetailPostOnWorkerRoleBinding.inflate(inflater);
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
        initRecyclerview();

        mPodID = getArguments().getString(POST_ID);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<Worker> requestList = new ArrayList<>();

                for (DataSnapshot item : snapshot.getChildren()) {
                    requestList.add(item.getValue(Worker.class));
                }

                if(requestList.isEmpty()){
                    binding.tvNoWorker.setVisibility(View.VISIBLE);
                    binding.rv.setVisibility(View.GONE);
                }else {
                    binding.tvNoWorker.setVisibility(View.GONE);
                    binding.rv.setVisibility(View.VISIBLE);
                }

                mAdapter.setData(requestList);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };


        fetchPostOfUser();

    }

    private void initRecyclerview() {
        mAdapter = new WorkerRequestInPostAdapter(listWorkerRequesting);
        binding.rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rv.setHasFixedSize(true);
        binding.rv.setAdapter(mAdapter);
    }

    private void fetchPostOfUser() {

        binding.progressbar.setVisibility(View.VISIBLE);

        ApiService.apiService.getPostOfDemandById(mPodID).enqueue(new Callback<ResponseRetrofit<PostOfDemand>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<PostOfDemand>> call, Response<ResponseRetrofit<PostOfDemand>> response) {

                binding.progressbar.setVisibility(View.GONE);
                if (response.code() == HttpURLConnection.HTTP_OK) {

                    if (response.body().isSuccessed()) {
                        updateUI(response.body().getResultObj());
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<PostOfDemand>> call, Throwable t) {

                binding.progressbar.setVisibility(View.GONE);

                Log.e("TTT", "onFailure: ", t);
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI(PostOfDemand pod) {

        mPOD = pod;

        binding.tvJobName.setText(pod.getJobInfoName());
        binding.tvAddress.setText(pod.getAddress());
        binding.tvTime.setText(Methods.getPastTimeString(pod.getCreationTime()));
        binding.tvTimeClose.setText(pod.getEndDateTimeString());
        binding.tvNote.setText(pod.getNote());
        binding.tvDescription.setText(pod.getDescription());
        binding.tvDetailWithUser.setText("Chi tiết công việc của " + pod.getCustomerName());

        //check if worker exist in worker list
        for (Worker item : pod.getListWorkerRequestInPostOfDemandResponse()) {
            if (Constant.USER.getId().equals(item.getId())) {
                isContainInWorkerList = true;
                break;
            }
        }

        if (pod.getListWorkerRequestInPostOfDemandResponse().isEmpty()) {
            binding.tvNoWorker.setVisibility(View.VISIBLE);
        } else {
            binding.tvNoWorker.setVisibility(View.GONE);
        }

        setAssignButton();

        String imageUrl = Constant.BASE_URL + pod.getCustomerImage();

        if (!imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(binding.ivUserThumb);
        }

        FirebaseRepository.PickPostChild.child(mPodID).setValue(pod.getListWorkerRequestInPostOfDemandResponse());

//        mAdapter.setData(pod.getListWorkerRequestInPostOfDemandResponse());
    }

    private void setAssignButton() {
        if (isContainInWorkerList) {
            binding.btnAssign.setText("Hủy yêu cầu");
            binding.btnAssign.setBackgroundColor(getResources().getColor(R.color.grey));
        } else {
            binding.btnAssign.setText("Ứng tuyển");
            binding.btnAssign.setBackgroundColor(getResources().getColor(R.color.primaryColor));
        }
    }


    @Override
    protected void initListener() {
        binding.btnBack.setOnClickListener(v -> {
            backToPreviousFrag();
        });

        binding.btnAssign.setOnClickListener(v -> {
            if (isContainInWorkerList && mPOD != null) {
                handleUnAssignClick();
            } else {
                handleAssignClick();
            }
        });

        observerChangeFromDatabase();
    }

    private void handleUnAssignClick() {
        ApiService.apiService.deleteWorkerResponsePostOfDemand(Constant.USER.getId(), mPodID).enqueue(new Callback<ResponseRetrofit<Object>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<Object>> call, Response<ResponseRetrofit<Object>> response) {
                if (response.body() != null && response.body().isSuccessed()) {
                    Methods.makeToast("Đã hủy ứng tuyển!");
                    handleDeleteWorkerFromRequestListFirebase();
                    isContainInWorkerList = false;
                    setAssignButton();
                } else {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<Object>> call, Throwable t) {
                Log.e("TTT", "onFailure: ", t);
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void observerChangeFromDatabase() {
        FirebaseRepository.PickPostChild.child(mPodID).addValueEventListener(valueEventListener);
    }

    private void handleDeleteWorkerFromRequestListFirebase(){
        FirebaseRepository.PickPostChild.child(mPodID).get().addOnCompleteListener(task -> {

            ArrayList<Worker> requestList = new ArrayList<>();

            for (DataSnapshot item : task.getResult().getChildren()) {
                Worker w = item.getValue(Worker.class);

                if(!w.getId().equals(Constant.USER.getId())){
                    requestList.add(w);
                }
            }

            FirebaseRepository.PickPostChild.child(mPodID).setValue(requestList);

        });
    }

    private void handleAddWorkerToRequestListFirebase(Worker worker) {

        FirebaseRepository.PickPostChild.child(mPodID).get().addOnCompleteListener(task -> {

            ArrayList<Worker> requestList = new ArrayList<>();
            boolean isContain = false;

            for (DataSnapshot item : task.getResult().getChildren()) {

                Worker w = item.getValue(Worker.class);
                requestList.add(w);

                isContain = w.getId().equals(worker.getId());
            }

            if (!isContain) {

                requestList.add(worker);
                FirebaseRepository.PickPostChild.child(mPodID).setValue(requestList);
            }

        });
    }

    private void handleAssignClick() {
        if (mPOD != null) {
            Map<String, String> body = new HashMap<>();
            body.put("workerId", Constant.USER.getId());
            body.put("postOfDemandId", mPodID);

            ApiService.apiService.requestPostOfDemandForWorker(body).enqueue(new Callback<ResponseRetrofit<Worker>>() {
                @Override
                public void onResponse(Call<ResponseRetrofit<Worker>> call, Response<ResponseRetrofit<Worker>> response) {
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        if (response.body() != null && response.body().isSuccessed()) {
                            Methods.makeToast("Đã ứng tuyển vào công việc!");
                            isContainInWorkerList = true;
                            setAssignButton();
                            handleAddWorkerToRequestListFirebase(response.body().getResultObj());
                        } else {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseRetrofit<Worker>> call, Throwable t) {
                    Log.e("TTT", "onFailure: ", t);
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FirebaseRepository.PickPostChild.child(mPodID).removeEventListener(valueEventListener);
    }
}