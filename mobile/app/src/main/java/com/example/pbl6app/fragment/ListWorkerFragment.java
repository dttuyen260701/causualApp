package com.example.pbl6app.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pbl6app.Adapters.WorkerLinesAdapter;
import com.example.pbl6app.Models.JobInfo;
import com.example.pbl6app.Models.Rate;
import com.example.pbl6app.Models.TypeOfJob;
import com.example.pbl6app.Models.Worker;
import com.example.pbl6app.Models.WorkerDetail;
import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.FragmentListWorkerBinding;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListWorkerFragment extends FragmentBase{

    private FragmentListWorkerBinding binding;
    private TypeOfJob typeOfJob;
    private ArrayList<WorkerDetail> listWorker;
    private WorkerLinesAdapter workerLinesAdapter;

    public ListWorkerFragment(TypeOfJob typeOfJob) {
        this.typeOfJob = typeOfJob;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListWorkerBinding.inflate(inflater, container, false);
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
        binding.txtTittleListWorkerFragment.setText(getResources().getString(R.string.Workers, typeOfJob.getName()));

        listWorker = new ArrayList<>();
        loadData();
        workerLinesAdapter = new WorkerLinesAdapter(listWorker, item -> addFragment(new WorkerDetailFragment(item), R.id.ctFragmentUser));

        binding.recyclerWorker.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerWorker.setAdapter(workerLinesAdapter);
    }

    @Override
    protected void initListener() {
        binding.btnBack.setOnClickListener(view -> {
            backToPreviousFrag();
        });

        binding.searchWorkerView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                if (newText.length() == 0) {
                    workerLinesAdapter.setListData(listWorker);
                }
                return false;
            }
        });

        binding.swipeRefreshWorkerFrag.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
    }

    private void search(String text) {
        ArrayList<WorkerDetail> list_search = new ArrayList<>();
        for (WorkerDetail i : listWorker) {
            if (i.getName().toLowerCase().contains(text.toLowerCase()))
                list_search.add(i);
        }
        if (list_search.isEmpty()) {
            if (text.length() > 0)
                Toast.makeText(getActivity(), "Không tìm thấy dữ liệu!!!", Toast.LENGTH_SHORT).show();
        } else {
            workerLinesAdapter.setListData(list_search);
        }
    }

    private void loadData() {

        binding.swipeRefreshWorkerFrag.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);

        ApiService.apiService.getListUserByIDTypeOfJob(Constant.USER.getId(), typeOfJob.getId()).enqueue(new Callback<ResponseRetrofit<ArrayList<WorkerDetail>>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<ArrayList<WorkerDetail>>> call, Response<ResponseRetrofit<ArrayList<WorkerDetail>>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                binding.swipeRefreshWorkerFrag.setEnabled(true);
                binding.swipeRefreshWorkerFrag.setRefreshing(false);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (response.body().isSuccessed()) {
                        listWorker.clear();
                        listWorker.addAll(response.body().getResultObj());
                        workerLinesAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<ResponseRetrofit<ArrayList<WorkerDetail>>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                binding.swipeRefreshWorkerFrag.setEnabled(true);
                binding.swipeRefreshWorkerFrag.setRefreshing(false);
                Log.e("TTT", "onFailure: ",t );
                if(getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
