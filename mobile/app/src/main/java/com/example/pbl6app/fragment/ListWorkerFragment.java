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
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.pbl6app.Adapters.WorkerLinesAdapter;
import com.example.pbl6app.Models.JobInfo;
import com.example.pbl6app.Models.Order;
import com.example.pbl6app.Models.WorkerDetail;
import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ItemPaging;
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
    private JobInfo jobInfo;
    private ArrayList<WorkerDetail> listWorker;
    private WorkerLinesAdapter workerLinesAdapter;
    private static int pageIndex = 1;
    private int pageSize = Constant.DEFAULT_PAGE_SIZE;
    private boolean isLoading = false;

    public ListWorkerFragment(JobInfo jobInfo) {
        this.jobInfo = jobInfo;
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
        pageIndex = 1;
        binding.txtTittleListWorkerFragment.setText(getResources().getString(R.string.Workers, jobInfo.getName()));
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
                pageIndex = 1;
                loadData();
            }
        });

        binding.recyclerWorker.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    if(!binding.scrollView.canScrollVertically(1)){
                        if(!isLoading){
                            handlePagingAction(false);
                            ++pageIndex;
                            loadData();
                        }
                    }
                }
            }
        });
    }


    private void handlePagingAction(boolean isDoneLoading) {

        if (isDoneLoading) {
            isLoading = false;
            binding.progressRV.setVisibility(View.GONE);
        } else {
            isLoading = true;
            binding.progressRV.setVisibility(View.VISIBLE);
        }

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
        if(pageIndex==1){
            binding.viewBg.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
            isLoading = true;
            binding.progressRV.setVisibility(View.GONE);
        }

        ApiService.apiService.getListWorkerByIDTypeOfJob(Constant.USER.getId(), jobInfo.getId(), pageIndex, pageSize).enqueue(new Callback<ResponseRetrofit<ItemPaging<ArrayList<WorkerDetail>>>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<ItemPaging<ArrayList<WorkerDetail>>>> call, Response<ResponseRetrofit<ItemPaging<ArrayList<WorkerDetail>>>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                binding.swipeRefreshWorkerFrag.setEnabled(true);
                binding.swipeRefreshWorkerFrag.setRefreshing(false);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (response.body().isSuccessed()) {
                        ItemPaging<ArrayList<WorkerDetail>> resultObj = response.body().getResultObj();
                        if(pageIndex==1){
                            listWorker.clear();
                        }
                        if(listWorker.size()<resultObj.getTotalRecords()) {
                            listWorker.addAll(resultObj.getItems());
                            workerLinesAdapter.notifyDataSetChanged();
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
                if(pageIndex==1){
                    isLoading = false;
                }
                else{
                    handlePagingAction(true);
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<ItemPaging<ArrayList<WorkerDetail>>>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                binding.swipeRefreshWorkerFrag.setEnabled(true);
                binding.swipeRefreshWorkerFrag.setRefreshing(false);
                Log.e("TTT", "onFailure: ",t );
                if(getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                }
                if(pageIndex==1){
                    isLoading = false;
                }
                else{
                    handlePagingAction(true);
                }
            }
        });
    }
}
