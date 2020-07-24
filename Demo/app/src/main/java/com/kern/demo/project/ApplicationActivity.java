package com.kern.demo.project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.kern.demo.BaseActivity;
import com.kern.demo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ApplicationActivity extends BaseActivity {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    public static final String[] tabs = {"信息", "备份", "安装"};

    private List<Fragment> fragmentList;

    private ApplicationFragmentAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_application;
    }

    @Override
    protected int getToolbarType() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentList = new ArrayList<>();
        fragmentList.add(new ApplicationInfoFragment());
        fragmentList.add(new ApplicationBackupFragment());
        fragmentList.add(new ApplicationInstallFragment());

        adapter = new ApplicationFragmentAdapter(getSupportFragmentManager(),fragmentList);

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        setToolbarTitle("应用相关操作");
    }
}
