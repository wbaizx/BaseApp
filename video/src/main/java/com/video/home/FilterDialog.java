package com.video.home;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.base.common.base.adapter.BaseHolder;
import com.base.common.util.imageload.LoadImageKt;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.video.R;
import com.video.home.gl.renderer.filter.FilterType;

public class FilterDialog {
    private BottomSheetDialog bottomSheetDialog;
    private DialogInterface.OnDismissListener onDismissListener;
    private OnItemClickListener onItemClickListener;

    public void init(Context context) {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new BottomSheetDialog(context);
            RecyclerView recyclerView = (RecyclerView) LayoutInflater.from(context).inflate(R.layout.bottom_filter_view, null);
            bottomSheetDialog.setContentView(recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

            BaseQuickAdapter<FilterType, BaseHolder> adapter = new BaseQuickAdapter<FilterType, BaseHolder>() {
                @NonNull
                @Override
                protected BaseHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int position) {
                    return new BaseHolder(viewGroup, R.layout.bottom_filter_item);
                }

                @Override
                protected void onBindViewHolder(@NonNull BaseHolder baseHolder, int position, @Nullable FilterType type) {
                    LoadImageKt.loadImg(baseHolder.itemView.findViewById(R.id.filterItemImg), type.getPng());
                    TextView name = baseHolder.itemView.findViewById(R.id.name);
                    name.setText(type.getName());
                }
            };
            adapter.submitList(FilterType.getList());
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener<FilterType>() {
                @Override
                public void onClick(@NonNull BaseQuickAdapter<FilterType, ?> baseQuickAdapter, @NonNull View view, int position) {
                    onItemClickListener.onItemClick(adapter.getItem(position));
                }
            });
            recyclerView.setAdapter(adapter);

            bottomSheetDialog.setOnDismissListener(onDismissListener);
        }

        Window window = bottomSheetDialog.getWindow();
        if (window != null) {
            window.setDimAmount(0f);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public void show() {
        bottomSheetDialog.show();
    }

    public interface OnItemClickListener {
        void onItemClick(FilterType type);
    }
}
