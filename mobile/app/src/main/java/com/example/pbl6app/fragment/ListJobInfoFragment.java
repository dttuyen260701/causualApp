package com.example.pbl6app.fragment;
/*
 * Created by tuyen.dang on 11/24/2022
 */

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

import com.example.pbl6app.Adapters.JobInfoLinesAdapter;
import com.example.pbl6app.Models.JobInfo;
import com.example.pbl6app.Models.TypeOfJob;
import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.databinding.FragmentListJobinfoBinding;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListJobInfoFragment extends FragmentBase {

    private TypeOfJob typeOfJob;
    private FragmentListJobinfoBinding binding;
    private ArrayList<JobInfo> listJobInfo;
    private JobInfoLinesAdapter adapter;

    public ListJobInfoFragment(TypeOfJob typeOfJob) {
        this.typeOfJob = typeOfJob;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListJobinfoBinding.inflate(inflater, container, false);
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
        binding.txtTittleListWorkerFragment.setText(getResources().getString(R.string.JobInfo, typeOfJob.getName()));

        listJobInfo = new ArrayList<>();
        loadData();
        adapter = new JobInfoLinesAdapter(listJobInfo, item -> addFragment(new ListWorkerFragment(item), R.id.ctFragmentUser));

        binding.recyclerWorker.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerWorker.setAdapter(adapter);
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
                    adapter.setList_data(listJobInfo);
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
        ArrayList<JobInfo> list_search = new ArrayList<>();
        for (JobInfo i : listJobInfo) {
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

    private void loadData() {

        binding.swipeRefreshWorkerFrag.setEnabled(false);
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.viewBg.setVisibility(View.VISIBLE);

        ApiService.apiService.getListJobInfo(typeOfJob.getId()).enqueue(new Callback<ResponseRetrofit<ArrayList<JobInfo>>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<ArrayList<JobInfo>>> call, Response<ResponseRetrofit<ArrayList<JobInfo>>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                binding.swipeRefreshWorkerFrag.setEnabled(true);
                binding.swipeRefreshWorkerFrag.setRefreshing(false);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (response.body().isSuccessed()) {
                        listJobInfo.clear();
                        listJobInfo.addAll(response.body().getResultObj());
                        adapter.notifyDataSetChanged();
                    } else {
                        if (getContext() != null) {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<ArrayList<JobInfo>>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                binding.swipeRefreshWorkerFrag.setEnabled(true);
                binding.swipeRefreshWorkerFrag.setRefreshing(false);
                Log.e("TTT", "onFailure: ", t);
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
