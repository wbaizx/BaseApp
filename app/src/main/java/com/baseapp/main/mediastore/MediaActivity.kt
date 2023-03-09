package com.baseapp.main.mediastore

import android.content.ContentResolver
import android.content.ContentUris
import android.database.ContentObserver
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.lifecycle.lifecycleScope
import com.base.common.base.activity.BaseActivity
import com.base.common.util.debugLog
import com.baseapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "MediaActivity_tag"

class MediaActivity : BaseActivity() {

    private val mediaContentObserver = object : ContentObserver(null) {
        override fun onChange(selfChange: Boolean, uris: MutableCollection<Uri>, flags: Int) {
            super.onChange(selfChange, uris, flags)
            uris.forEach {
                debugLog(TAG, "onChange $selfChange $it $flags")
//                ContentResolver.NOTIFY_INSERT
//                ContentResolver.NOTIFY_UPDATE
//                ContentResolver.NOTIFY_DELETE
//                if (flags == ContentResolver.NOTIFY_INSERT) {
                query(it)
//                }
            }
        }
    }

    override fun getContentView() = R.layout.activity_media

    override fun initView() {
        contentResolver.registerContentObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, true, mediaContentObserver)
        contentResolver.registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, mediaContentObserver)

        query()
    }

    /**
     * MediaStore.Images.Media.EXTERNAL_CONTENT_URI
     *
     * MediaStore.Files相关，可以作为同时查询图片视频的部分信息的基类引用
     * MediaStore.Files.getContentUri("external")
     * MediaStore.Files.FileColumns.DATA
     *
     * MediaStore.Files.FileColumns.MEDIA_TYPE 判断媒体类型
     */
    private fun query(queryUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI) {
        lifecycleScope.launch(Dispatchers.IO) {

            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.IS_TRASHED,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val queryArgs = Bundle()
                queryArgs.putInt(ContentResolver.QUERY_ARG_SORT_DIRECTION, ContentResolver.QUERY_SORT_DIRECTION_DESCENDING)
                queryArgs.putStringArray(ContentResolver.QUERY_ARG_SORT_COLUMNS, arrayOf(MediaStore.Images.Media.ORIENTATION))
                queryArgs.putInt(ContentResolver.QUERY_ARG_OFFSET, 0)
                queryArgs.putInt(ContentResolver.QUERY_ARG_LIMIT, 1)
                contentResolver.query(queryUri, projection, queryArgs, null)

            } else {
                contentResolver.query(
                    queryUri, projection, null, null,
                    "${MediaStore.Images.Media.ORIENTATION} DESC limit 1"
                )
            }?.use {
                val columnId = it.getColumnIndex(MediaStore.Images.Media._ID)
                val columnMime = it.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)
                val columnData = it.getColumnIndex(MediaStore.Images.Media.DATA)
                val columnBucket = it.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
                val columnSize = it.getColumnIndex(MediaStore.Images.Media.SIZE)
                val columnWidth = it.getColumnIndex(MediaStore.Images.Media.WIDTH)
                val columnHeight = it.getColumnIndex(MediaStore.Images.Media.HEIGHT)
                val columnOrientation = it.getColumnIndex(MediaStore.Images.Media.ORIENTATION)

                while (it.moveToNext()) {
                    val id = it.getLong(columnId)
                    val mime = it.getString(columnMime)
                    val bucket = it.getString(columnBucket)
                    val data = it.getString(columnData)
                    val size = it.getLong(columnSize)
                    val width = it.getInt(columnWidth)
                    val height = it.getInt(columnHeight)
                    val orientation = it.getInt(columnOrientation)
                    val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    debugLog(TAG, "mime = $mime bucket = $bucket orientation = $orientation width = $width height = $height size = $size data = $data uri = $uri")
                }
            }
        }
    }

    override fun onDestroy() {
        contentResolver.unregisterContentObserver(mediaContentObserver)
        super.onDestroy()
    }
}