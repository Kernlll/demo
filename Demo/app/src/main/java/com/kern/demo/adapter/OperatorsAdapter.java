package com.kern.demo.adapter;

import android.view.View;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kern.demo.R;
import com.kern.demo.mode.OperatorModel;

import java.util.List;

/**
 * @Author: wangke
 */
public abstract class OperatorsAdapter extends BaseQuickAdapter<OperatorModel, BaseViewHolder> {

    public OperatorsAdapter(@Nullable List<OperatorModel> data) {
        super(R.layout.layout_item_operator, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, OperatorModel item) {
        if (item != null) {
            holder.setText(R.id.item_title, item.title)
                    .setText(R.id.item_des, item.des)
                    .getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(holder.getAdapterPosition());
                }
            });
        }
    }

    public abstract void onItemClick(int position);
}

