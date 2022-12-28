package com.example.pbl6app.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pbl6app.Adapters.WorkerRequestInPostAdapter;
import com.example.pbl6app.Listeners.ListenerDialog;
import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.Order;
import com.example.pbl6app.Models.PostOfDemand;
import com.example.pbl6app.Models.Worker;
import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.Utils.FirebaseRepository;
import com.example.pbl6app.Utils.Methods;
import com.example.pbl6app.activities.MainActivityUser;
import com.example.pbl6app.databinding.FragmentDetailPostOnWorkerRoleBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    private final OnItemCLickListener<Object> listenerEnd;
    private static boolean isRunning = false;

    public static boolean isIsRunning() {
        return isRunning;
    }

    public static void setIsRunning(boolean isRunning) {
        DetailPostOnWorkerRoleFragment.isRunning = isRunning;
    }

    public DetailPostOnWorkerRoleFragment(OnItemCLickListener<Object> listenerEnd) {
        this.listenerEnd = listenerEnd;
    }

    public static DetailPostOnWorkerRoleFragment newInstance(String postID, OnItemCLickListener<Object> listenerEnd) {
        DetailPostOnWorkerRoleFragment fragment = new DetailPostOnWorkerRoleFragment(listenerEnd);
        Bundle args = new Bundle();
        args.putString(POST_ID, postID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDetailPostOnWorkerRoleBinding.inflate(inflater, container, false);
        isRunning = true;
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

                if (requestList.isEmpty()) {
                    binding.tvNoWorker.setVisibility(View.VISIBLE);
                    binding.rv.setVisibility(View.GONE);
                } else {
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
        mAdapter = new WorkerRequestInPostAdapter(
                listWorkerRequesting,
                item -> addFragment(new ListRateWorkerFragment(item), R.id.ctFragmentUser),
                item -> {
                    if (Constant.USER.getRole() == Constant.ROLE_CUSTOMER) {
                        Methods.showDialog(
                                R.drawable.smile_dialog,
                                "Xác nhận",
                                "Chọn thợ " + item.getName() + " để thực hiện công việc " + mPOD.getJobInfoName(),
                                "Không",
                                "Xác nhận",
                                new ListenerDialog() {
                                    @Override
                                    public void onDismiss() {

                                    }

                                    @Override
                                    public void onNoClick(Dialog dialog) {
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onYesClick(Dialog dialog) {
                                        createOrder(item);
                                        dialog.dismiss();
                                    }
                                }
                        );
                    }
                }
        );
        binding.rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rv.setHasFixedSize(true);
        binding.rv.setAdapter(mAdapter);
    }

    private void fetchPostOfUser() {

        binding.progressbar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);

        ApiService.apiService.getPostOfDemandById(mPodID).enqueue(new Callback<ResponseRetrofit<PostOfDemand>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<PostOfDemand>> call, Response<ResponseRetrofit<PostOfDemand>> response) {

                binding.progressbar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);

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
                binding.viewBg.setVisibility(View.GONE);


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
        if (getContext() != null) {
            if (Constant.USER.getRole() == Constant.ROLE_WORKER) {
                if (isContainInWorkerList) {
                    binding.btnAssign.setText("Hủy yêu cầu");
                    binding.btnAssign.setBackgroundColor(getResources().getColor(R.color.grey));
                } else {
                    binding.btnAssign.setText("Ứng tuyển");
                    binding.btnAssign.setBackgroundColor(getResources().getColor(R.color.primaryColor));
                }
            } else {
                binding.btnAssign.setText("Ngừng tìm");
                binding.btnAssign.setBackgroundColor(getResources().getColor(R.color.primaryColor));
            }
        }
    }


    @Override
    protected void initListener() {
        binding.btnBack.setOnClickListener(v -> {
            backToPreviousFrag();
        });

        binding.btnAssign.setOnClickListener(v -> {
            if (Constant.USER.getRole() == Constant.ROLE_WORKER) {
                if (Constant.USER.getLastModificationTime() == null) {
                    Methods.showDialog(
                            R.drawable.smile_dialog,
                            "Thông báo",
                            "Bạn vui lòng cập nhật thông tin để sử dụng dịch vụ của chúng tôi",
                            "Để sau",
                            "Cập nhật",
                            new ListenerDialog() {
                                @Override
                                public void onDismiss() {

                                }

                                @Override
                                public void onNoClick(Dialog dialog) {
                                    dialog.dismiss();
                                }

                                @Override
                                public void onYesClick(Dialog dialog) {
                                    addFragment(new ProfileFragment(), R.id.ctFragmentUser);
                                    dialog.dismiss();
                                }
                            }
                    );
                } else {
                    if (isContainInWorkerList && mPOD != null) {
                        handleUnAssignClick();
                    } else {
                        handleAssignClick();
                    }
                }
            } else {
                Methods.showDialog(
                        R.drawable.sad_dialog,
                        "Ngừng tìm thợ",
                        "Bạn có chắc muốn ngừng tìm thợ cho công việc này",
                        "Không",
                        "Có",
                        new ListenerDialog() {
                            @Override
                            public void onDismiss() {

                            }

                            @Override
                            public void onNoClick(Dialog dialog) {
                                dialog.dismiss();
                            }

                            @Override
                            public void onYesClick(Dialog dialog) {
                                updatePODToFalse();
                                dialog.dismiss();
                            }
                        }
                );
            }
        });

        observerChangeFromDatabase();
    }

    private void handleUnAssignClick() {
        binding.viewBg.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);
        ApiService.apiService.deleteWorkerResponsePostOfDemand(Constant.USER.getId(), mPodID).enqueue(new Callback<ResponseRetrofit<Object>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<Object>> call, Response<ResponseRetrofit<Object>> response) {
                binding.viewBg.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
                if (response.body() != null && response.body().isSuccessed()) {
                    FirebaseRepository
                            .ResponsePost
                            .child(mPOD.getCustomerId())
                            .child(mPOD.getId())
                            .setValue(mPOD);
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
                binding.viewBg.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
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

    private void handleDeleteWorkerFromRequestListFirebase() {
        FirebaseRepository.PickPostChild.child(mPodID).get().addOnCompleteListener(task -> {

            ArrayList<Worker> requestList = new ArrayList<>();

            for (DataSnapshot item : task.getResult().getChildren()) {
                Worker w = item.getValue(Worker.class);

                if (!w.getId().equals(Constant.USER.getId())) {
                    requestList.add(w);
                }
            }

            FirebaseRepository.PickPostChild.child(mPodID).setValue(requestList);

        });
    }

    private void handleAddWorkerToRequestListFirebase(Worker worker) {

        binding.viewBg.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);
        FirebaseRepository.PickPostChild.child(mPodID).get().addOnCompleteListener(task -> {

            binding.viewBg.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.GONE);
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
            binding.viewBg.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);

            ApiService.apiService.requestPostOfDemandForWorker(body).enqueue(new Callback<ResponseRetrofit<Worker>>() {
                @Override
                public void onResponse(Call<ResponseRetrofit<Worker>> call, Response<ResponseRetrofit<Worker>> response) {
                    binding.viewBg.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.GONE);
                    if (response.code() == HttpURLConnection.HTTP_OK) {
                        if (response.body() != null && response.body().isSuccessed()) {
                            FirebaseRepository
                                    .ResponsePost
                                    .child(mPOD.getCustomerId())
                                    .child(mPOD.getId())
                                    .setValue(mPOD);
                            Methods.makeToast("Đã ứng tuyển vào công việc");
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
                    binding.viewBg.setVisibility(View.GONE);
                    binding.progressBar.setVisibility(View.GONE);
                    Log.e("TTT", "onFailure: ", t);
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void createOrder(Worker worker) {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);
        Map<String, String> options = new HashMap<>();
        options.put("workerId", worker.getId());
        options.put("jobInfoId", mPOD.getJobInfoId());
        options.put("note", mPOD.getNote());
        options.put("addressPoint", mPOD.getAddressPoint());
        options.put("address", mPOD.getAddress());
        options.put("status", String.valueOf(Constant.ACCEPT_STATUS));

        ApiService.apiService.createOrder(Constant.USER.getId(), options).enqueue(new Callback<ResponseRetrofit<Order>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<Order>> call, Response<ResponseRetrofit<Order>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (response.body().isSuccessed()) {
                        FirebaseRepository.
                                ResponsePostWorker.
                                child(worker.getId()).
                                child(response.body().getResultObj().getId()).
                                setValue(response.body().getResultObj());

                        mPOD.getListWorkerRequestInPostOfDemandResponse().remove(worker);

                        updatePODToFalse();

                        Methods.showDialog(
                                R.drawable.smile_dialog,
                                "Tuyệt vời",
                                "Yêu cầu công việc của bạn đã được gửi đến thợ",
                                "Trở về",
                                "Xem đơn",
                                new ListenerDialog() {
                                    @Override
                                    public void onDismiss() {

                                    }

                                    @Override
                                    public void onNoClick(Dialog dialog) {
                                        dialog.dismiss();
                                        backToPreviousFrag();
                                    }

                                    @Override
                                    public void onYesClick(Dialog dialog) {
                                        StatusFragment.setOrderId(response.body().getResultObj().getId());
                                        MainActivityUser.setIdNavigate(R.id.menu_status);
                                        dialog.dismiss();
                                    }
                                });
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<Order>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                Log.e("CHANGE_PASS", "onFailure: ", t);
                Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updatePODToFalse() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);

        ApiService.apiService.unActivePOD(mPOD.getId()).enqueue(new Callback<ResponseRetrofit<Object>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<Object>> call, Response<ResponseRetrofit<Object>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (response.body().isSuccessed()) {
                        for (Worker i : mPOD.getListWorkerRequestInPostOfDemandResponse()) {
                            Order order = new Order(Constant.REJECT_STATUS);
                            FirebaseRepository.
                                    ResponsePostWorker.
                                    child(i.getId()).
                                    child(order.getId()).
                                    setValue(order);
                        }
                        listenerEnd.onItemClick(new Object());
                        backToPreviousFrag();
                        Methods.makeToast("Đã hủy tìm kiếm thợ.");

                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<Object>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                Log.e("CHANGE_PASS", "onFailure: ", t);
                Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        super.onDestroy();
        FirebaseRepository.PickPostChild.child(mPodID).removeEventListener(valueEventListener);
    }
}