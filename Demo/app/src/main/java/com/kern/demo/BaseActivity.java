package com.kern.demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Author: wangke
 */
public abstract class BaseActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    protected abstract int getLayoutId();

    protected abstract int getToolbarType();

    LinearLayout mParent;

    private int toolbarType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        ButterKnife.bind(this);
        ivBack.setOnClickListener(view->finish());
    }

    private void setContentView() {
        toolbarType = getToolbarType();
        int toolbarLayout = R.layout.toolbar_normal;
        switch (toolbarType) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                toolbarLayout = R.layout.toolbar_normal;
                break;
        }
        ViewGroup content = findViewById(android.R.id.content);
        content.removeAllViews();
        mParent = new LinearLayout(this);
        mParent.setOrientation(LinearLayout.VERTICAL);
        content.addView(mParent);
        LayoutInflater.from(this).inflate(toolbarLayout, mParent, true);
        LayoutInflater.from(this).inflate(getLayoutId(), mParent, true);
        setSupportActionBar(toolbar);
    }

    protected void setToolbarTitle(String title) {
        tvTitle.setText(title);
    }

    protected void setToolbarTitle(int title) {
        tvTitle.setText(title);
    }
}
