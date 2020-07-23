package com.kern.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Author: wangke
 */
public class MainFragment extends Fragment {
    private static final String TAG = MainFragment.class.getSimpleName();
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    private int mIndex;
    private List<String> datas = new ArrayList<>();
    RecyclerViewAdapter adapter;
    public MainFragment(int index) {
        mIndex = index;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        Log.i("wangke","recyclerview:"+recyclerview);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        //LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(manager);
        initData();
        adapter = new RecyclerViewAdapter(getActivity(), datas, mIndex);
        recyclerview.setAdapter(adapter);
        return view;
    }

/*    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG,"onViewCreate");
        ButterKnife.bind(this, view);
    }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG,"onActivityCreated");
    }

    @Override
    public void onResume() {
        super.onResume();
        switch (mIndex) {
            case 0:
                ((MainActivity)getActivity()).setToolbarTitle("Java");
                break;
            case 1:
                ((MainActivity)getActivity()).setToolbarTitle("Kotlin");
                break;
            case 2:
                ((MainActivity)getActivity()).setToolbarTitle("Android");
                break;
            case 3:
                ((MainActivity)getActivity()).setToolbarTitle("Java");
                break;
            case 4:
                ((MainActivity)getActivity()).setToolbarTitle("Project");
                break;
        }

    }

    private void initData(){
        switch (mIndex) {
            case 0:
                initJava();
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                initProject();
                break;
        }
    }

    private void initJava() {
        datas.add("多线程");
        datas.add("基本类型");
        datas.add("IO");
        datas.add("NIO");
        datas.add("流");
        datas.add("集合");
    }

    private void initProject() {
        datas.add("应用相关操作");
    }
}
