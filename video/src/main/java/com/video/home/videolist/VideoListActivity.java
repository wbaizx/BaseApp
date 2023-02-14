package com.video.home.videolist;

import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.base.common.base.activity.BaseBindContentActivity;
import com.base.common.util.FileUtilKt;
import com.base.common.util.RouterUtilKt;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.video.R;
import com.video.databinding.ActivityVideoListBinding;
import com.video.home.play.PlayActivity;

import java.io.File;
import java.util.ArrayList;

public class VideoListActivity extends BaseBindContentActivity<ActivityVideoListBinding> {

    private VideoListAdapter videoListAdapter = new VideoListAdapter();

    @Override
    protected int getContentView() {
        return R.layout.activity_video_list;
    }

    @Override
    protected void viewBind(@NonNull ActivityVideoListBinding binding) {
    }

    @Override
    protected void initView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(videoListAdapter);

        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoListAdapter.deleteFile();
            }
        });

        binding.allSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoListAdapter.selectAll();
            }
        });

        videoListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (videoListAdapter.isSelectMode()) {
                    videoListAdapter.select(position);
                } else {
                    Intent intent = new Intent(VideoListActivity.this, PlayActivity.class);
                    intent.putExtra("path", videoListAdapter.getData().get(position).getFile().getAbsolutePath());
                    RouterUtilKt.launchActivity(VideoListActivity.this, intent);
                }
            }
        });

        videoListAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                videoListAdapter.setSelectMode(true);
                binding.allSelect.setVisibility(View.VISIBLE);
                binding.delete.setVisibility(View.VISIBLE);

                videoListAdapter.select(position);
                return true;
            }
        });

        initData();
    }

    private void initData() {
        File file = new File(FileUtilKt.getDiskFilePath("VIDEO"));
        File[] files = file.listFiles();
        if (files != null) {
            ArrayList<VideoListAdapter.FileBean> list = new ArrayList<>();
            for (File f : files) {
                list.add(new VideoListAdapter.FileBean(f));
            }
            videoListAdapter.setList(list);
        }
    }
}
