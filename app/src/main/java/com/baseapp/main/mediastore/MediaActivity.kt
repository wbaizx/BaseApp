package com.baseapp.main.mediastore

import android.content.ContentResolver
import android.content.ContentUris
import android.database.ContentObserver
import android.database.Cursor
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.lifecycleScope
import com.base.common.base.activity.BaseActivity
import com.base.common.helper.safeLaunch
import com.base.common.util.debugLog
import com.baseapp.R
import kotlinx.coroutines.Dispatchers

private const val TAG = "mediaactivity_tag"

class MediaActivity : BaseActivity() {

    private val rootUri = MediaStore.Files.getContentUri("external")
    private val imageRootUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    private val videoRootUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

    private fun Uri.isRootUri() = this == rootUri
    private fun Uri.isImageUri() = path?.contains("image") ?: false
    private fun Uri.isVideoUri() = path?.contains("video") ?: false

    private fun Cursor.getSafeInt(columnIndex: Int) = if (columnIndex != -1) getInt(columnIndex) else -1
    private fun Cursor.getSafeString(columnIndex: Int) = if (columnIndex != -1) getString(columnIndex) ?: "" else ""
    private fun Cursor.getSafeLong(columnIndex: Int) = if (columnIndex != -1) getLong(columnIndex) else 0

    private val videoRetriever by lazy { MediaMetadataRetriever() }

    private val baseArgProjection = listOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.DATA,
        MediaStore.Files.FileColumns.DATE_MODIFIED,
        MediaStore.Files.FileColumns.SIZE,
        MediaStore.Files.FileColumns.MIME_TYPE,
        MediaStore.Files.FileColumns.WIDTH,
        MediaStore.Files.FileColumns.HEIGHT,
        MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
    )

    private val mediaContentObserver = object : ContentObserver(null) {
        //回调就接收这一个方法就行，列表那个方法有些手机系统原因会报错
        override fun onChange(selfChange: Boolean, uri: Uri?, flags: Int) {
            super.onChange(selfChange, uri, flags)
            uri?.let {
                //exclude "content://media/external", use rootUri
                if (uri.pathSegments.size < 2) {
                    query(rootUri)
                    return
                }

                debugLog(TAG, "onChange $selfChange $uri $flags")
//                ContentResolver.NOTIFY_INSERT
//                ContentResolver.NOTIFY_UPDATE
//                ContentResolver.NOTIFY_DELETE
                if (flags == ContentResolver.NOTIFY_INSERT) {
                    query(it)
                }
            }
        }
    }

    override fun getContentView() = R.layout.activity_media

    override fun initView() {
        contentResolver.registerContentObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, true, mediaContentObserver)
        contentResolver.registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, mediaContentObserver)

        query(rootUri)
    }

    private fun query(queryUri: Uri) {
        lifecycleScope.safeLaunch(Dispatchers.IO) {
            getQueryCursor(queryUri)?.use {
                val columnId = it.getColumnIndex(MediaStore.Files.FileColumns._ID)
                val columnData = it.getColumnIndex(MediaStore.Files.FileColumns.DATA)
                val columnMimeType = it.getColumnIndex(MediaStore.Files.FileColumns.MIME_TYPE)
                val columnMediaType = it.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE)
                val columnTrash = it.getColumnIndex(MediaStore.Files.FileColumns.IS_TRASHED)

                while (it.moveToNext()) {
                    val id = it.getSafeLong(columnId)
                    val data = it.getSafeString(columnData)
                    val mimeType = it.getSafeString(columnMimeType)
                    val mediaType = it.getSafeInt(columnMediaType)

                    if (it.getSafeInt(columnTrash) == 1) {
                        debugLog(TAG, "load data is trash")
                        continue
                    }

                    if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                        || mimeType.contains("image")
                        || queryUri.isImageUri()
                    ) {
                        val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                        it.getSafeString(it.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                        fixImageInfo(data)

                    } else if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
                        || mimeType.contains("video")
                        || queryUri.isVideoUri()
                    ) {
                        val uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                        it.getSafeString(it.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                        fixVideoInfo(uri)

                    } else continue

                    debugLog(TAG, "mimeType = $mimeType mediaType = $mediaType")
                }
            }
        }
    }

    private fun getQueryCursor(queryUri: Uri): Cursor? {
        val projections = baseArgProjection.toMutableList()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            projections.add(MediaStore.Files.FileColumns.IS_TRASHED)
        }

        when {
            queryUri.isRootUri() -> {
                projections.add(MediaStore.Files.FileColumns.MEDIA_TYPE)
                projections.add(MediaStore.Files.FileColumns.DURATION)
                projections.add(MediaStore.Files.FileColumns.ORIENTATION)
            }
            queryUri.isImageUri() -> {
                projections.add(MediaStore.Images.Media.ORIENTATION)
            }
            queryUri.isVideoUri() -> {
                projections.add(MediaStore.Video.Media.DURATION)
            }
        }

        //limit 1
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val queryArgs = Bundle()
            queryArgs.putInt(ContentResolver.QUERY_ARG_SORT_DIRECTION, ContentResolver.QUERY_SORT_DIRECTION_DESCENDING)
            queryArgs.putStringArray(ContentResolver.QUERY_ARG_SORT_COLUMNS, arrayOf(MediaStore.Files.FileColumns.DATE_MODIFIED))
            queryArgs.putInt(ContentResolver.QUERY_ARG_OFFSET, 0)
            queryArgs.putInt(ContentResolver.QUERY_ARG_LIMIT, 1)
            contentResolver.query(queryUri, projections.toTypedArray(), queryArgs, null)

        } else {
            contentResolver.query(
                queryUri, projections.toTypedArray(), null, null,
                "${MediaStore.Files.FileColumns.DATE_MODIFIED} DESC limit 1"
            )
        }
    }

    private fun fixImageInfo(path: String) {
        val exifInterface = ExifInterface(path)
        var width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0)
        var height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0)

        if (width <= 0 || height <= 0) {
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeFile(path, options)
            width = options.outWidth
            height = options.outHeight
        }

        val orientation = when (exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }

        if (orientation % 180 != 0) {
            val temp = width
            width = height
            height = temp
        }
    }

    private fun fixVideoInfo(uri: Uri) {
        videoRetriever.setDataSource(this, uri)
        val width = videoRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.toInt()
        if (width == null || width <= 0) throw RuntimeException("video info error")

        val height = videoRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.toInt()
        if (height == null || height <= 0) throw RuntimeException("video info error")

        val orientation = videoRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)?.toInt() ?: 0
    }

    override fun onDestroy() {
        contentResolver.unregisterContentObserver(mediaContentObserver)
        super.onDestroy()
    }
}