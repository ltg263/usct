package com.frico.usct.ui.activity.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frico.usct.R;
import com.frico.usct.ui.activity.response.JsonCityBean;
import com.frico.usct.utils.LogUtils;

import java.util.List;

public class PopRecycleAdapter extends RecyclerView.Adapter<PopRecycleAdapter.ViewHolder> {
    private Context context;
    private List<JsonCityBean> data;


    public PopRecycleAdapter(Context context, List<JsonCityBean> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pop_recycleview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.city.setText(data.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e("这里是点击每一行item的响应事件",""+position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView city;

        public ViewHolder(View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.city);

        }
    }

}
