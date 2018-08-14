package com.fan.mycode.adapter;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.fan.imageutils.EditImageDialog;
import com.fan.mycode.CityActivity;
import com.fan.mycode.MainActivity;
import com.fan.mycode.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FanWenLong on 2018/8/13.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    List<DataInfo> data;
    Context context;

    @NonNull
    @Override

    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_main, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.btn_item_main.setText(data.get(position).name);
        holder.btn_item_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 3:
                        EditImageDialog mdf = new EditImageDialog();
                        FragmentTransaction ft = ((Activity) context).getFragmentManager().beginTransaction();
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        mdf.show(ft, "df");
                        break;
                    default:
                        Intent it = new Intent(context, data.get(position).toActivity);
                        context.startActivity(it);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public MainAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
        data.add(new DataInfo("我的2048", com.fan.my2048.MainActivity.class));
        data.add(new DataInfo("2048原著", com.yuan2048.MainActivity.class));
        data.add(new DataInfo("城市选择", CityActivity.class));
        data.add(new DataInfo("画板", null));

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public Button btn_item_main;

        public MyViewHolder(View itemView) {
            super(itemView);
            btn_item_main = itemView.findViewById(R.id.btn_item_main);
        }
    }

    class DataInfo {

        public DataInfo(String name, Class toActivity) {
            this.name = name;
            this.toActivity = toActivity;
        }

        public String name;
        public Class toActivity;
    }
}
