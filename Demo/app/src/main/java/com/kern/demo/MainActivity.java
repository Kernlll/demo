package com.kern.demo;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Author: wangke
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.bnv_tab)
    BottomNavigationView bnvTab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getToolbarType() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final List<Fragment> list = new ArrayList<>();
        for (int i = 0;i<5; i++) {
            list.add(new MainFragment(i));
        }

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);  //得到Fragment
            }

            @Override
            public int getCount() {
                return list.size();  //得到数量
            }
        };
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bnvTab.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bnvTab.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.java:
                        viewpager.setCurrentItem(0);
                        break;
                    case R.id.kotlin:
                        viewpager.setCurrentItem(1);
                        break;
                    case R.id.android:
                        viewpager.setCurrentItem(2);
                        break;
                    case R.id.frame:
                        viewpager.setCurrentItem(3);
                        break;
                    case R.id.project:
                        viewpager.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });
        viewpager.setOffscreenPageLimit(4);
        viewpager.setCurrentItem(3);
    }
}
