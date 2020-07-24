package com.kern.demo.frame;

import android.os.Build;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.kern.demo.R;
import com.kern.demo.adapter.BaseViewPagerAdapter;
import com.kern.demo.config.GlobalConfig;
import com.kern.demo.frame.fragment.OperatorsFragment;
import com.kern.demo.frame.fragment.UseCasesFragment;
import com.kern.demo.util.ScreenUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: wangke
 */
public class RxJavaActivity extends RxJavaBaseActivity {
    @BindView(R.id.rxjava_toolbar)
    Toolbar rxjavaToolbar;
    @BindView(R.id.rxjava_tabLayout)
    TabLayout rxjavaTabLayout;
    @BindView(R.id.rxjava_appbar)
    AppBarLayout rxjavaAppbar;
    @BindView(R.id.rxjava_viewPager)
    ViewPager rxjavaViewPager;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_rxjava;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 4.4 以上版本
            // 设置 Toolbar 高度为 80dp，适配状态栏
            ViewGroup.LayoutParams layoutParams = toolbarTitle.getLayoutParams();
//            layoutParams.height = ScreenUtil.dip2px(this,ScreenUtil.getStatusBarHeight(this));
            layoutParams.height = ScreenUtil.dip2px(this, 80);
            toolbarTitle.setLayoutParams(layoutParams);
        }
        initToolBar(rxjavaToolbar, false, "");

        String[] titles = {
                GlobalConfig.CATEGORY_NAME_OPERATORS,
                GlobalConfig.CATEGORY_NAME_EXAMPLES
        };

        BaseViewPagerAdapter pagerAdapter = new BaseViewPagerAdapter(getSupportFragmentManager(), titles);
        pagerAdapter.addFragment(new OperatorsFragment());
        pagerAdapter.addFragment(new UseCasesFragment());

        rxjavaViewPager.setAdapter(pagerAdapter);
        rxjavaTabLayout.setupWithViewPager(rxjavaViewPager);
    }

    /**
     * 初始化 Toolbar
     */
    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

    @OnClick(R.id.fab)
    public void onViewClicked() {
        //WebViewActivity.start(this,"https://github.com/nanchen2251","我的GitHub,欢迎Star");
    }
}
