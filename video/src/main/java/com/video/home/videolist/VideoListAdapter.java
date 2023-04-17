package com.video.home.videolist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.base.common.base.adapter.BaseHolder;
import com.base.common.util.imageload.LoadImageKt;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.video.R;

import java.io.File;
import java.util.Iterator;

public class VideoListAdapter extends BaseQuickAdapter<VideoListAdapter.FileBean, BaseHolder> {
    private static final String TAG = "VideoListAdapter";

    private boolean selectMode = false;
    private boolean allSelect = false;
    private int selectNum = 0;

    public boolean isSelectMode() {
        return selectMode;
    }

    public void setSelectMode(boolean selectMode) {
        this.selectMode = selectMode;
    }

    @NonNull
    @Override
    protected BaseHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup parent, int position) {
        return new BaseHolder(parent, R.layout.video_list_item);
    }

    @Override
    protected void onBindViewHolder(@NonNull BaseHolder holder, int position, @Nullable FileBean fileBean) {
        if (getItem(position).isSelect()) {
            holder.itemView.findViewById(R.id.videoItemSelect).setVisibility(View.VISIBLE);
        } else {
            holder.itemView.findViewById(R.id.videoItemSelect).setVisibility(View.GONE);
        }
        TextView videoItemSelect = holder.itemView.findViewById(R.id.videoItemTitle);
        videoItemSelect.setText(getItem(position).getFile().getName());

        LoadImageKt.loadImg(holder.itemView.findViewById(R.id.videoItemImg), getItem(position).getFile());
    }


    public void select(int position) {
        FileBean bean = getItem(position);
        bean.setSelect(!bean.isSelect());
        if (bean.isSelect()) {
            selectNum++;
        } else {
            selectNum--;
        }
        checkIsAllSelect();
        notifyItemChanged(position);
    }

    public void selectAll() {
        allSelect = !allSelect;
        for (VideoListAdapter.FileBean bean : getItems()) {
            bean.setSelect(allSelect);
        }
        if (allSelect) {
            selectNum = getItems().size();
        } else {
            selectNum = 0;
        }
        notifyDataSetChanged();
    }

    private void checkIsAllSelect() {
        allSelect = selectNum == getItems().size();
    }

    public void deleteFile() {
        Iterator<FileBean> iterator = getItems().iterator();
        while (iterator.hasNext()) {
            FileBean bean = iterator.next();
            if (bean.isSelect()) {
                bean.file.delete();
                iterator.remove();
            }
        }
        notifyDataSetChanged();
    }

    public static class FileBean {
        private boolean isSelect = false;

        private File file;

        public File getFile() {
            return file;
        }

        public FileBean(File file) {
            this.file = file;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }
    }
}
