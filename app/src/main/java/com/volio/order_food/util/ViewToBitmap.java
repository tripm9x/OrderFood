package com.volio.order_food.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ViewToBitmap {

    private static final String TAG = ViewToBitmap.class.getSimpleName();
    private static final String EXTENSION_PNG = ".png";
    private static final String EXTENSION_JPG = ".jpg";
    private static final int JPG_MAX_QUALITY = 100;
    private String fileName, fileExtension;
    private OnSaveResultListener onSaveResultListener;
    private int jpgQuality;
    private Handler handler;
    private View view;

    private Bitmap bitmap;

    private ViewToBitmap(@NonNull View view) {
        this.view = view;
    }

    private ViewToBitmap(@NonNull Bitmap bm) {
        this.bitmap = bm;
    }

    public static ViewToBitmap of(@NonNull View view) {
        return new ViewToBitmap(view);
    }

    public static ViewToBitmap of(@NonNull Bitmap bm) {
        return new ViewToBitmap(bm);
    }

    public ViewToBitmap toJPG() {
        jpgQuality = JPG_MAX_QUALITY;
        setFileExtension(EXTENSION_JPG);
        return this;
    }

    public ViewToBitmap toJPG(int jpgQuality) {
        this.jpgQuality = (jpgQuality == 0) ? JPG_MAX_QUALITY : jpgQuality;
        setFileExtension(EXTENSION_JPG);
        return this;
    }

    public ViewToBitmap toPNG() {
        setFileExtension(EXTENSION_PNG);
        return this;
    }


    public ViewToBitmap setOnSaveResultListener(OnSaveResultListener onSaveResultListener) {
        this.onSaveResultListener = onSaveResultListener;
        this.handler = new Handler(Looper.myLooper());
        return this;
    }

    public void save(Context context) {
        Bitmap bitmap = getBitmap();
        if (bitmap != null) {
            AsyncSaveImage asyncSaveBitmap = new AsyncSaveImage(context, bitmap);
            asyncSaveBitmap.execute();
        } else {
            notifyListener(false, "");
        }
    }

    public void saveBitmap(Context context) {
        if (bitmap != null) {
            AsyncSaveBitmap asyncSaveBitmap = new AsyncSaveBitmap(context, bitmap);
            asyncSaveBitmap.execute();
        } else {
            notifyListener(false, "");
        }
    }


    private Context getAppContext() {
        if (view == null) {
            throw new NullPointerException("Null cannot passed to ViewToBitmap.of()");
        } else {
            return view.getContext().getApplicationContext();
        }
    }

    private String getFileExtension() {
        return fileExtension;
    }

    private void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    private String getFilename() {
        if (fileName == null || fileName.isEmpty()) {
            return "picture_" + System.currentTimeMillis() / 1000 + getFileExtension();
        } else {
            return fileName + getFileExtension();
        }
    }


    private Bitmap getBitmap() {
        Bitmap bitmap;
        //reduce quality image
        if (view == null || view.getWidth() <= 0 || view.getHeight() <= 0) return null;
        bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        canvas.setBitmap(null);
        return bitmap;
    }

    private Bitmap getBitmapByCache() {
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        // copy this bitmap otherwise distroying the cache will destroy
        // the bitmap for the referencing drawable and you'll not
        // get the captured view
        Bitmap b = b1.copy(Bitmap.Config.ARGB_8888, false);
        view.destroyDrawingCache();
        return b;
    }

    private void notifyListener(final boolean isSaved, final String path) {

        if (onSaveResultListener != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onSaveResultListener.onSaveResult(isSaved, path);
                }
            });
        }
    }

    public interface OnSaveResultListener {
        void onSaveResult(boolean isSaved, String path);
    }

    private class AsyncSaveImage extends AsyncTask<Void, Void, Boolean>
            implements MediaScannerConnection.OnScanCompletedListener {
        private Context context;
        private Bitmap bitmap;
        //private CustomProgressDialog dialog;

        private AsyncSaveImage(Context context, Bitmap bitmap) {
            this.context = context;
            this.bitmap = bitmap;
            //dialog = new CustomProgressDialog(context);
        }

        protected void onPreExecute() {
            //this.dialog.show();
        }
private String FOLDER_PHOTO_LAYOUT = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/PhotoLayout/";
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(Void... params) {
            File myDir = new File( FOLDER_PHOTO_LAYOUT);
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            File imageFile = new File(myDir, getFilename());
            if (fileExtension == null) {
                throw new IllegalStateException(
                        "A file format must be chosen to ViewToBitmap before calling save()");
            } else {
                try (
                        OutputStream out = new BufferedOutputStream(new FileOutputStream(imageFile))) {
                    switch (fileExtension) {
                        case EXTENSION_JPG:
                            bitmap.compress(Bitmap.CompressFormat.JPEG, jpgQuality, out);
                            break;
                        case EXTENSION_PNG:
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                            break;
                        default:
                            bitmap.compress(Bitmap.CompressFormat.JPEG, jpgQuality, out);
                            break;
                    }
//                    notifyListener(true, imageFile.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                    bitmap = null;
                    notifyListener(false, null);
                    return false;
                }
            }
            bitmap = null;
            if (imageFile.getAbsolutePath() != null && context != null) {
                MediaScannerConnection.scanFile(context, new String[]{imageFile.getAbsolutePath()}, null, this);
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {


        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            if (uri != null && path != null) {
                notifyListener(true, path);
            } else {
                notifyListener(false, null);
            }
        }
    }


    private  class AsyncSaveBitmap extends AsyncTask<Void, Void, Boolean>
            implements MediaScannerConnection.OnScanCompletedListener {
        private Context context;
        private Bitmap bitmap;
        //private CustomProgressDialog dialog;

        private AsyncSaveBitmap(Context context, Bitmap bitmap) {
            this.context = context;
            this.bitmap = bitmap;
            //dialog = new CustomProgressDialog(context);
        }

        protected void onPreExecute() {
            //this.dialog.show();
        }

        private String FOLDER_PHOTO_LAYOUT = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/PhotoLayout/";
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected Boolean doInBackground(Void... params) {
            File myDir = new File(FOLDER_PHOTO_LAYOUT);
            if (!myDir.exists()) {
                myDir.mkdirs();
            }
            File imageFile = new File(myDir, getFilename());
            if (fileExtension == null) {
                throw new IllegalStateException(
                        "A file format must be chosen to ViewToBitmap before calling save()");
            } else {
                try (OutputStream out = new BufferedOutputStream(new FileOutputStream(imageFile))) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                    notifyListener(true, imageFile.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                    bitmap = null;
                    notifyListener(false, null);
                    return false;
                }
            }
            bitmap = null;
            if (imageFile.getAbsolutePath() != null && context != null) {
                MediaScannerConnection.scanFile(context, new String[]{imageFile.getAbsolutePath()}, null, this);
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {


        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            if (uri != null && path != null) {
                notifyListener(true, path);
            } else {
                notifyListener(false, null);
            }
        }
    }

}

