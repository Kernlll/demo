package com.kern.demo.frame.rxjava;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.jaeger.library.StatusBarUtil;
import com.kern.demo.R;
import com.kern.demo.frame.RxJavaBaseActivity;

import butterknife.BindView;

/**
 * @Author: wangke
 */
public abstract class ToolbarBaseActivity extends RxJavaBaseActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title_text)
    TextView mTitleName;


    /**
     * 设置标题文本
     */
    protected abstract String getSubTitle();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this,getResources().getColor(R.color.blue)); //
        if (getContentViewLayoutId() != 0) {
            initToolbar();
        }
    }

    private void initToolbar() {
        if (mToolbar != null){
            mToolbar.setTitle("");
            mTitleName.setText(getSubTitle());
            if (isShowBack()){
                showBack();
            }
        }
    }

    /**
     * 版本号小于21的后退按钮图片
     */
    private void showBack(){
        //setNavigationIcon必须在setSupportActionBar(toolbar);方法后面加入
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.mipmap.icon_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    protected boolean isShowBack(){
        return true;
    }
}
