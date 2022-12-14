package com.example.pbl6app.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.pbl6app.Adapters.ListChoiceItemAdapter;
import com.example.pbl6app.Listeners.OnItemCLickListener;
import com.example.pbl6app.Models.AddressTemp;
import com.example.pbl6app.Models.JobInfo;
import com.example.pbl6app.Models.TypeOfJob;
import com.example.pbl6app.R;
import com.example.pbl6app.Retrofit.ApiService;
import com.example.pbl6app.Retrofit.ResponseRetrofit;
import com.example.pbl6app.Utils.Constant;
import com.example.pbl6app.databinding.LayoutListChoiceBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoiceFragment extends BottomSheetDialogFragment {
    private BottomSheetBehavior mBehavior;
    private LayoutListChoiceBinding binding;
    private ArrayList<AddressTemp> list_data;
    private ArrayList<TypeOfJob> list_type_of_job;
    private OnItemCLickListener<AddressTemp> listener;
    private ListChoiceItemAdapter adapter;
    private String title;
    private int KEY_DATA = -1;
    private String ID_Load = "";

    public ChoiceFragment(int keyData, String title, OnItemCLickListener<AddressTemp> listener, String ID_Load) {
        this.KEY_DATA = keyData;
        this.listener = listener;
        this.title = title;
        this.list_data = new ArrayList<>();
        this.ID_Load = ID_Load;
        this.list_type_of_job = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LayoutListChoiceBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        setUp();
        loadData();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = View.inflate(getContext(), R.layout.layout_list_choice, null);
        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        dialog.setCancelable(false);
        return dialog;
    }

    private void setUp() {
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                getActivity().getResources().getDisplayMetrics().widthPixels,
                getActivity().getResources().getDisplayMetrics().heightPixels * 7 / 10
        );
        binding.layoutMainChoice.setLayoutParams(layoutParams);
        binding.txtTileOption.setText(title);
        binding.btnCancelChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        adapter = new ListChoiceItemAdapter(list_data, listener);
        binding.rclChoice.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rclChoice.setAdapter(adapter);

        binding.searchViewChoice.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                search(s);
                if (s.length() == 0) {
                    adapter.setList_data(list_data);
                }
                return false;
            }
        });
    }

    private void search(String text) {
        ArrayList<AddressTemp> list_search = new ArrayList<>();
        for (AddressTemp i : list_data) {
            if (i.getName().toLowerCase().contains(text.toLowerCase()))
                list_search.add(i);
        }
        if (list_search.isEmpty()) {
            if (text.length() > 0)
                Toast.makeText(getActivity(), "Không tìm thấy dữ liệu", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setList_data(list_search);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void loadData() {
        binding.viewBg.setVisibility(View.VISIBLE);
        binding.progressBar.setVisibility(View.VISIBLE);
        switch (KEY_DATA) {
            case Constant.PROVINCE_DATA:
                ApiService.apiService.getProvince().enqueue(new Callback<ArrayList<AddressTemp>>() {
                    @Override
                    public void onResponse(Call<ArrayList<AddressTemp>> call, Response<ArrayList<AddressTemp>> response) {
                        binding.viewBg.setVisibility(View.GONE);
                        binding.progressBar.setVisibility(View.GONE);
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            list_data.addAll(response.body());

                            for (AddressTemp i : list_data) {
                                i.setCheck(i.getId().equals(ProfileFragment.getIdProvinceChosen()));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            if (getContext() != null) {
                                Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<AddressTemp>> call, Throwable t) {
                        binding.viewBg.setVisibility(View.GONE);
                        binding.progressBar.setVisibility(View.GONE);
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case Constant.DISTRICT_DATA:
                ApiService.apiService.getDistrict(ID_Load).enqueue(new Callback<ArrayList<AddressTemp>>() {
                    @Override
                    public void onResponse(Call<ArrayList<AddressTemp>> call, Response<ArrayList<AddressTemp>> response) {
                        binding.viewBg.setVisibility(View.GONE);
                        binding.progressBar.setVisibility(View.GONE);
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            list_data.addAll(response.body());

                            for (AddressTemp i : list_data) {
                                i.setCheck(i.getId().equals(ProfileFragment.getIdDistrictChosen()));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            if (getContext() != null) {
                                Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<AddressTemp>> call, Throwable t) {
                        binding.viewBg.setVisibility(View.GONE);
                        binding.progressBar.setVisibility(View.GONE);
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case Constant.WARD_DATA:
                ApiService.apiService.getWard(ID_Load).enqueue(new Callback<ArrayList<AddressTemp>>() {
                    @Override
                    public void onResponse(Call<ArrayList<AddressTemp>> call, Response<ArrayList<AddressTemp>> response) {
                        binding.viewBg.setVisibility(View.GONE);
                        binding.progressBar.setVisibility(View.GONE);
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            list_data.addAll(response.body());

                            for (AddressTemp i : list_data) {
                                i.setCheck(i.getId().equals(ProfileFragment.getIdWardChosen()));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            if (getContext() != null) {
                                Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<AddressTemp>> call, Throwable t) {
                        binding.viewBg.setVisibility(View.GONE);
                        binding.progressBar.setVisibility(View.GONE);
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case Constant.JOB_INFO_DATA_DETAIL:
                binding.viewBg.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
                AddressTemp temp;
                for (JobInfo i : BookingOrderScreenFragment.getListJobInfo()) {
                    temp = new AddressTemp(i.getId(), i.getName());
                    temp.setCheck(i.getId().equals(BookingOrderScreenFragment.getIdJobInfo().getId()));
                    list_data.add(temp);
                }
                adapter.notifyDataSetChanged();
                break;
            case Constant.GENDER_DATA:
                binding.viewBg.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
                list_data.add(new AddressTemp("0", "Nam"));
                list_data.add(new AddressTemp("1", "Nữ"));
                list_data.add(new AddressTemp("2", "Không xác định"));
                for (AddressTemp i : list_data) {
                    i.setCheck(i.getId().equals(ProfileFragment.getIdGenderChonse()));
                }
                adapter.notifyDataSetChanged();
                break;
            case Constant.TYPE_OF_JOB_DATA:
                ApiService.apiService.getAllTypeOfJob().enqueue(new Callback<ResponseRetrofit<ArrayList<TypeOfJob>>>() {
                    @Override
                    public void onResponse(Call<ResponseRetrofit<ArrayList<TypeOfJob>>> call, Response<ResponseRetrofit<ArrayList<TypeOfJob>>> response) {
                        binding.viewBg.setVisibility(View.GONE);
                        binding.progressBar.setVisibility(View.GONE);
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            ArrayList<TypeOfJob> typeOfJobList = response.body().getResultObj();
                            for (TypeOfJob type : typeOfJobList) {
                                list_data.add(new AddressTemp(type.getId(), type.getName()));
                            }

                            for (AddressTemp i : list_data) {
                                i.setCheck(i.getId().equals(CreateNewPostFragment.getIdTypeOfJobChosen()));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            if (getContext() != null) {
                                Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseRetrofit<ArrayList<TypeOfJob>>> call, Throwable t) {
                        binding.viewBg.setVisibility(View.GONE);
                        binding.progressBar.setVisibility(View.GONE);
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case Constant.JOB_INFO_DATA:
                ApiService.apiService.getListJobInfo(ID_Load).enqueue(new Callback<ResponseRetrofit<ArrayList<JobInfo>>>() {
                    @Override
                    public void onResponse(Call<ResponseRetrofit<ArrayList<JobInfo>>> call, Response<ResponseRetrofit<ArrayList<JobInfo>>> response) {
                        binding.viewBg.setVisibility(View.GONE);
                        binding.progressBar.setVisibility(View.GONE);
                        if (response.code() == HttpURLConnection.HTTP_OK) {
                            ArrayList<JobInfo> listJob = response.body().getResultObj();
                            for (JobInfo job : listJob) {
                                list_data.add(new AddressTemp(job.getId(), job.getName()));
                            }

                            for (AddressTemp i : list_data) {
                                i.setCheck(i.getId().equals(CreateNewPostFragment.getIdJobInfoChosen()));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            if (getContext() != null) {
                                Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseRetrofit<ArrayList<JobInfo>>> call, Throwable t) {
                        binding.viewBg.setVisibility(View.GONE);
                        binding.progressBar.setVisibility(View.GONE);
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Lỗi khi thực hiện thao tác", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case Constant.NUMBER_HOUR_DATA:
                binding.viewBg.setVisibility(View.GONE);
                binding.progressBar.setVisibility(View.GONE);
                list_data.add(new AddressTemp("0", "1"));
                list_data.add(new AddressTemp("1", "2"));
                list_data.add(new AddressTemp("2", "3"));
                list_data.add(new AddressTemp("3", "4"));
                list_data.add(new AddressTemp("4", "5"));
                list_data.add(new AddressTemp("5", "6"));
                list_data.add(new AddressTemp("6", "7"));
                list_data.add(new AddressTemp("7", "8"));

                for (AddressTemp i : list_data) {
                    i.setCheck(i.getId().equals(CreateNewPostFragment.getIdNumberHourChosen()));
                }
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }
}
