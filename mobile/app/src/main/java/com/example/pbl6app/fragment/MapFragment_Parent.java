package com.example.pbl6app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.pbl6app.Listeners.Listener_for_PickAddress;
import com.example.pbl6app.R;

import java.util.ArrayList;

public class MapFragment_Parent extends Fragment {
    private ImageView img_Back_Map_Frag;
    private ConstraintLayout layout_MapFragment;
    private static Button btnSave_Location;
    private SearchView searchView_Map_Frag;
    private Listener_for_PickAddress listener_for_pickAddress;
    private ListView list_result_Map_Frag;
    private ArrayList<String> list_result;

    public MapFragment_Parent(Listener_for_PickAddress listener_for_pickAddress) {
        this.listener_for_pickAddress = listener_for_pickAddress;
    }

    public void setVisible() {
        btnSave_Location.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SetUp(view);
        return view;
    }

    private void SetUp(View view) {
        MapFragment mapFragment = new MapFragment(this, "");

        img_Back_Map_Frag = (ImageView) view.findViewById(R.id.img_Back_Map_Frag);
        img_Back_Map_Frag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        layout_MapFragment = (ConstraintLayout) view.findViewById(R.id.layout_MapFragment);
        btnSave_Location = (Button) view.findViewById(R.id.btnSave_Location);
        btnSave_Location.setVisibility(View.GONE);

        list_result = new ArrayList<>();

        list_result_Map_Frag = (ListView) view.findViewById(R.id.list_result_Map_Frag);

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, list_result);

        list_result_Map_Frag.setAdapter(arrayAdapter);

        list_result_Map_Frag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mapFragment.searchAddress(list_result.get(i));
            }
        });

        searchView_Map_Frag = (SearchView) view.findViewById(R.id.searchView_Map_Frag);

        searchView_Map_Frag.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //sự kiện khi submit
            @Override
            public boolean onQueryTextSubmit(String query) {
                list_result.clear();
                mapFragment.search_result(query, list_result);
                arrayAdapter.notifyDataSetChanged();
                list_result_Map_Frag.setBackgroundColor(getResources().getColor(R.color.white));
                return false;
            }

            //sự kiện khi thay đổi
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    list_result.clear();
                    arrayAdapter.notifyDataSetChanged();
                    list_result_Map_Frag.setBackgroundColor(0);
                }
                return false;
            }
        });

        btnSave_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener_for_pickAddress.onClick_pick(mapFragment.getAddress_line(),
                        (float) mapFragment.CalculationByDistance());
            }
        });

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        //add để trạng thái trước được lưu
        transaction.add(R.id.layout_MapFragment, mapFragment);
        transaction.commit();
    }
}
