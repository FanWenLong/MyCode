package com.fan.imageutils;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fan.color.ColorData;

import java.io.File;

/**
 * 仿微信图片编辑
 */
public class EditImageDialog extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen);
//        <style name="Dialog.FullScreen" parent="Theme.AppCompat.Dialog">
//        <item name="android:windowNoTitle">true</item>
//        <item name="android:windowBackground">@color/transparent</item>
//        <item name="android:windowIsFloating">false</item>
//        </style>
    }

    private RelativeLayout rl_top_dialog, rl_bottom_dialog;
    private EditImageView eiv;
    private RecyclerView recyclerView_dialog;
    private Button btn_cancel_dialog;
    private Button btn_color_dialog, btn_width_dialog;
    private Button btn_undo_dialog, btn_redo_dialog;
    private TextView tv_width_dialog;
    private MyAdapter colorAdapter;
    private SeekBar seek_width_dialog;
    private boolean show = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_image, container, false);
        eiv = view.findViewById(R.id.eiv);
        rl_top_dialog = view.findViewById(R.id.rl_top_dialog);
        rl_bottom_dialog = view.findViewById(R.id.rl_bottom_dialog);
        btn_cancel_dialog = view.findViewById(R.id.btn_cancel_dialog);
        btn_color_dialog = view.findViewById(R.id.btn_color_dialog);
        btn_width_dialog = view.findViewById(R.id.btn_width_dialog);
        recyclerView_dialog = view.findViewById(R.id.recyclerView_dialog);
        seek_width_dialog = view.findViewById(R.id.seek_width_dialog);
        tv_width_dialog = view.findViewById(R.id.tv_width_dialog);

        btn_undo_dialog = view.findViewById(R.id.btn_undo_dialog);
        btn_redo_dialog = view.findViewById(R.id.btn_redo_dialog);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x80000000));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        btn_cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        eiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(getClass().toString(), "click");
                if (show) {
                    show = false;
                    rl_top_dialog.setVisibility(View.GONE);
                    rl_bottom_dialog.setVisibility(View.GONE);
                } else {
                    show = true;
                    rl_top_dialog.setVisibility(View.VISIBLE);
                    rl_bottom_dialog.setVisibility(View.VISIBLE);
                }
            }
        });
        //颜色
        btn_color_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView_dialog.getVisibility() == View.GONE) {
                    recyclerView_dialog.setVisibility(View.VISIBLE);
                } else {
                    recyclerView_dialog.setVisibility(View.GONE);
                }
                if (colorAdapter == null) {
                    colorAdapter = new MyAdapter();
                    recyclerView_dialog.setLayoutManager(new GridLayoutManager((Context) getActivity(), 14));
                    recyclerView_dialog.setAdapter(colorAdapter);
                }
            }
        });
        //宽度
        btn_width_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seek_width_dialog.getVisibility() == View.GONE) {
                    seek_width_dialog.setVisibility(View.VISIBLE);
                } else {
                    seek_width_dialog.setVisibility(View.GONE);
                }
            }
        });
        //撤销
        btn_undo_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eiv.undo();
            }
        });
        //还原
        btn_redo_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eiv.redo();
            }
        });
        //调节宽度
        seek_width_dialog.setProgress((int) EditImageView.INIT_WIDTH);
        seek_width_dialog.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_width_dialog.setText(seekBar.getProgress() + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                tv_width_dialog.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                eiv.setPaintWidth(seekBar.getProgress() * 1.0f);
                tv_width_dialog.setVisibility(View.GONE);
                seek_width_dialog.setVisibility(View.GONE);
            }
        });
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private int w = eiv.getWidth() / 14;

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_dialog, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.tv_item_dialog.getLayoutParams();
            params.width = w;
            params.height = w;
            holder.tv_item_dialog.setLayoutParams(params);
            holder.tv_item_dialog.setText(ColorData.allColor[position]);
            holder.tv_item_dialog.setTextColor(Color.parseColor(ColorData.allColor[position]));
            holder.tv_item_dialog.setBackgroundColor(Color.parseColor(ColorData.allColor[position]));
            holder.tv_item_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eiv.setPaintColor(Color.parseColor(ColorData.allColor[position]));
                    recyclerView_dialog.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public int getItemCount() {
            for (int i = 0; i < ColorData.color.length; i++) {
                for (int j = 0; j < ColorData.color[i].length; j++) {
                    ColorData.allColor[i * ColorData.color[i].length + j] = ColorData.color[i][j];
                }
            }
            return ColorData.allColor.length;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_item_dialog;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_item_dialog = itemView.findViewById(R.id.tv_item_dialog);
        }
    }
}
