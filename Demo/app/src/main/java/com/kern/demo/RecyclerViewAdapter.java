package com.kern.demo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kern.demo.frame.RxJavaActivity;
import com.kern.demo.project.ApplicationActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Author: wangke
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<String> datas;
    int index;

    public RecyclerViewAdapter(Context context, List<String> datas, int index) {
        this.context = context;
        this.datas = datas;
        this.index = index;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_main_item, parent, false);
        Log.i("wangke", "onCreateViewHolder");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(datas.get(position));
        Log.i("wangke", "onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.cardview)
        CardView cardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            title.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String text = title.getText().toString();
            Intent intent = null;
            switch (text) {
                case "应用相关操作":
                    intent = new Intent(context, ApplicationActivity.class);
                    break;
                case "Rxjava":
                    intent = new Intent(context, RxJavaActivity.class);
                    break;
            }
            context.startActivity(intent);
        }


    }
}
