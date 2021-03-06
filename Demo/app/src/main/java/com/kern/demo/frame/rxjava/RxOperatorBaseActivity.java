package com.kern.demo.frame.rxjava;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kern.demo.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: wangke
 */
public abstract class RxOperatorBaseActivity extends ToolbarBaseActivity {
    @BindView(R.id.rx_operators_btn)
    protected Button mRxOperatorsBtn;
    @BindView(R.id.rx_operators_text)
    protected TextView mRxOperatorsText;
    @BindView(R.id.rx_clear_btn)
    protected Button mRxClearBtn;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_rx_operator_base;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Override
    protected abstract String getSubTitle();

    protected abstract void doSomething();

    @OnClick({R.id.rx_operators_btn, R.id.rx_clear_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rx_operators_btn:
                mRxOperatorsText.append("\n");
                doSomething();
                break;
            case R.id.rx_clear_btn:
                mRxOperatorsText.setText("\n");
                break;
        }
    }
}
