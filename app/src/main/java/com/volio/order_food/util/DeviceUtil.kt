    package com.volio.order_food.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.volio.order_food.R
import com.volio.order_food.model.FolderGalleryObj
import com.volio.order_food.model.ImageGalleryObj
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.set

object DeviceUtil {
    var arrImage: ArrayList<ImageGalleryObj> = ArrayList()


    fun getBitmapFromVectorDrawable(context: Context?, drawableId: Int): Bitmap {
        var drawable = ContextCompat.getDrawable(context!!, drawableId)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable!!).mutate()
        }
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }


    private fun getCount(
        context: Context,
        contentUri: Uri,
        bucketId: String
    ): Int {
        context.contentResolver.query(
            contentUri,
            null,
            " media_type = 1 AND " + MediaStore.Images.Media.BUCKET_ID + "=?",
            arrayOf(bucketId),
            null
        )
            .use { cursor -> return if (cursor == null || !cursor.moveToFirst()) 0 else cursor.count }
    }

    private fun getCountAll(
        context: Context,
        contentUri: Uri
    ): Int {
        context.contentResolver.query(
            contentUri,
            null,
            "media_type = 1 ",
            null,
            null
        )
            .use { cursor -> return if (cursor == null || !cursor.moveToFirst()) 0 else cursor.count }
    }

    fun getFolderImage(context: Context): List<FolderGalleryObj> {

        val selection = " media_type = 1 "
        val arrFolder = ArrayList<FolderGalleryObj>()
        val folders = HashMap<Long, String>()
        val projection = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE,
            MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
            MediaStore.Files.FileColumns.BUCKET_ID,
            MediaStore.Files.FileColumns.PARENT,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns._ID
        )
//        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val uri = MediaStore.Files.getContentUri("external")
        val cursor =
            context.contentResolver.query(
                uri,
                projection,
                selection,
                null,
                null
            )
        if (cursor != null && cursor.count > 0) {
            val folderIdIndex: Int =
                cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_ID)
            val folderNameIndex: Int =
                cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME)
            val trackIdIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
            val dateAddedIndex: Int =
                cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.PARENT)

            arrFolder.add(
                FolderGalleryObj(
                    0,
                    context.getString(R.string.all_photo),
                    "",
                    getCountAll(context, uri)
                )
            )
            while (cursor.moveToNext()) {
                try {
                    val folderId: Long = cursor.getLong(folderIdIndex)
                    if (!folders.containsKey(folderId)) {
                        val folderName: String = cursor.getString(folderNameIndex)
                        val folderPath: String = cursor.getString(trackIdIndex)
                        val folderParentId: Int = cursor.getInt(dateAddedIndex)
                        val folder = FolderGalleryObj(
                            folderId,
                            folderName,
                            folderPath,
                            getCount(context, uri, folderId.toString())
                        )
                        folders[folderId] = folderName
                        arrFolder.add(folder)
                    }
                } catch (ex: NullPointerException) {
                }
            }
            // Close cursor
            cursor.close()
            folders.clear() //clear the hashmap becuase it's no more useful

        }
        return arrFolder
    }

    fun getAllImage(context: Context): ArrayList<ImageGalleryObj>? {
        val arrImage = ArrayList<ImageGalleryObj>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME
        )
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val orderBy = MediaStore.Images.Media.DATE_ADDED
        val cursor = context.contentResolver.query(uri, projection, null, null, " $orderBy DESC ")
        if (cursor != null && cursor.count > 0) {
            val columnIndexId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val columnIndexPath = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val columnIndexName = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)


            while (cursor.moveToNext()) {
                try {
                    val id = cursor.getLong(columnIndexId)
                    val pathFile = cursor.getString(columnIndexPath)
                    val name = cursor.getString(columnIndexName)
                    val file = File(pathFile)
                    if (file.exists()) {
                        val image = ImageGalleryObj(id, pathFile)

                        arrImage.add(image)


                    }
                } catch (ex: NullPointerException) {
                    //ex.printStackTrace();
                }
            }
            // Close cursor
            cursor.close()
        }
        return arrImage
    }

    fun getImageFromFolder(context: Context, idFolder: Long): ArrayList<ImageGalleryObj>? {
        val selection = " media_type = 1 AND " + MediaStore.Images.Media.BUCKET_ID + "=?"
        val arrImage = ArrayList<ImageGalleryObj>()
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME

        )
        val uri = MediaStore.Files.getContentUri("external")
        val cursor =
            context.contentResolver.query(
                uri,
                projection,
                selection,
                arrayOf(idFolder.toString()),
                null
            )
        if (cursor != null && cursor.count > 0) {
            val columnIndexId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val columnIndexPath = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val columnIndexName = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

            while (cursor.moveToNext()) {
                try {
                    val id = cursor.getLong(columnIndexId)
                    val pathFile = cursor.getString(columnIndexPath)
                    val name = cursor.getString(columnIndexName)
                    val file = File(pathFile)
                    if (file.exists()) {

                        val image =
                            ImageGalleryObj(id, pathFile)
                        arrImage.add(image)

                    }
                } catch (ex: NullPointerException) {
                    //ex.printStackTrace();
                }
            }
            // Close cursor
            cursor.close()
        }
        return arrImage
    }


}