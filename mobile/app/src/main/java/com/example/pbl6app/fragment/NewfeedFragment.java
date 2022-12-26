package com.example.pbl6app.fragment;

/*
 * Created by tuyen.dang on 10/20/2022
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.pbl6app.Adapters.NewsPostAdapter;
import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.PostOfDemand;
import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ItemPaging;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.Utils.Methods;
import com.example.pbl6app.databinding.FragmentNewfeedBinding;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewfeedFragment extends FragmentBase {

    private FragmentNewfeedBinding binding;
    private ArrayList<PostOfDemand> listData;
    private NewsPostAdapter adapter;
    private static int pageIndex = 1;
    private int pageSize = Constant.DEFAULT_PAGE_SIZE;
    private boolean isLoading = false;
    private static ArrayList<PostOfDemand> listNew = new ArrayList<>();

    public static void setListNew(ArrayList<PostOfDemand> listNew) {
        NewfeedFragment.listNew.clear();
        NewfeedFragment.listNew.addAll(listNew);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNewfeedBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initListener();
//        NewsPostAdapter.setListNew(new ArrayList<>());
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    void initWorker() {
        binding.layoutHeaderFragUserHome.setVisibility(View.VISIBLE);
        binding.appbar.setVisibility(View.GONE);
        Picasso.get().load(Constant.BASE_URL + Constant.USER.getAvatar()).networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(binding.imageView2, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });

        binding.tvNameFragUserHome.setText("Welcome");
        binding.tvDescripFragUserHome.setText(Constant.USER.getName());

    }

    void initCustomer() {
        binding.layoutHeaderFragUserHome.setVisibility(View.GONE);
        binding.appbar.setVisibility(View.VISIBLE);
        binding.btnOpenCreatePostScreen.setVisibility(View.VISIBLE);
        binding.btnOpenCreatePostScreen.setOnClickListener(v -> {
            addFragment(new CreateNewPostFragment(item -> {
                loadDataCustomer();
                binding.btnOpenCreatePostScreen.setVisibility(View.VISIBLE);
            }), R.id.ctFragmentUser);
            binding.btnOpenCreatePostScreen.setVisibility(View.GONE);
        });
    }

    @Override
    protected void initView() {
        listData = new ArrayList<>();
        pageIndex = 1;
        if (Constant.USER.getRole() == Constant.ROLE_WORKER) {
            loadDataWorker();
            initWorker();
        } else {
            loadDataCustomer();
            initCustomer();
        }
        adapter = new NewsPostAdapter(listData, new OnItemCLickListener<PostOfDemand>() {
            @Override
            public void onItemClick(PostOfDemand item) {
                addFragment(
                        DetailPostOnWorkerRoleFragment.newInstance(item.getId(), object -> {
                            if (Constant.USER.getRole() == Constant.ROLE_WORKER) {
                                loadDataWorker();
                            } else {
                                loadDataCustomer();
                            }
                        }),
                        R.id.ctFragmentUser
                );
            }
        }, listNew);

        binding.rclViewData.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rclViewData.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        binding.refreshLayout.setOnRefreshListener(() -> {
            pageIndex = 1;
            if (Constant.USER.getRole() == Constant.ROLE_WORKER) {
                loadDataWorker();
            } else {
                loadDataCustomer();
            }
        });

        binding.rclViewData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!binding.scrollView.canScrollVertically(1)) {
                        if (!isLoading) {
                            handlePagingAction(false);
                            ++pageIndex;
                            if (Constant.USER.getRole() == Constant.ROLE_WORKER) {
                                loadDataWorker();
                            } else {
                                loadDataCustomer();
                            }
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


    private void loadDataWorker() {
        if(pageIndex==1){
            binding.viewBg.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
            isLoading = true;
            binding.progressRV.setVisibility(View.GONE);
        }
        binding.refreshLayout.setEnabled(false);
        ApiService.apiService.getListPostOfDemand(Constant.USER.getId(), pageIndex, pageSize).enqueue(new Callback<ResponseRetrofit<ItemPaging<ArrayList<PostOfDemand>>>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<ItemPaging<ArrayList<PostOfDemand>>>> call, Response<ResponseRetrofit<ItemPaging<ArrayList<PostOfDemand>>>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                binding.refreshLayout.setEnabled(true);
                binding.refreshLayout.setRefreshing(false);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (response.body().isSuccessed()) {
                        ItemPaging<ArrayList<PostOfDemand>> resultObj = response.body().getResultObj();
                        if(pageIndex==1){
                            listData.clear();
                        }
                        if(listData.size()<resultObj.getTotalRecords()) {
                            listData.addAll(resultObj.getItems());
                            adapter.notifyDataSetChanged();
                        }
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
                if(pageIndex==1){
                    isLoading = false;
                }
                else{
                    handlePagingAction(true);
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<ItemPaging<ArrayList<PostOfDemand>>>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                Log.e("TTT", "onFailure: ", t);
                if (getContext() != null) {
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
    private void loadDataCustomer() {
        if(pageIndex==1){
            binding.viewBg.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
            isLoading = true;
            binding.progressRV.setVisibility(View.GONE);
        }
        binding.refreshLayout.setEnabled(false);
        ApiService.apiService.getListPostOfDemandCustomer(Constant.USER.getId(), pageIndex, pageSize).enqueue(new Callback<ResponseRetrofit<ItemPaging<ArrayList<PostOfDemand>>>>() {
            @Override
            public void onResponse(Call<ResponseRetrofit<ItemPaging<ArrayList<PostOfDemand>>>> call, Response<ResponseRetrofit<ItemPaging<ArrayList<PostOfDemand>>>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                binding.refreshLayout.setEnabled(true);
                binding.refreshLayout.setRefreshing(false);
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    if (response.body().isSuccessed()) {
                        ItemPaging<ArrayList<PostOfDemand>> resultObj = response.body().getResultObj();
                        if(pageIndex==1){
                            listData.clear();
                        }
                        if(listData.size()<resultObj.getTotalRecords()) {
                            listData.addAll(resultObj.getItems());
                            adapter.notifyDataSetChanged();
                        }
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
                if(pageIndex==1){
                    isLoading = false;
                }
                else{
                    handlePagingAction(true);
                }
            }

            @Override
            public void onFailure(Call<ResponseRetrofit<ItemPaging<ArrayList<PostOfDemand>>>> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.viewBg.setVisibility(View.GONE);
                Log.e("TTT", "onFailure: ", t);
                if (getContext() != null) {
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
