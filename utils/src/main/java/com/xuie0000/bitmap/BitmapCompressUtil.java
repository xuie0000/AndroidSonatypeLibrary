package com.xuie0000.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.xuie0000.bitmap.config.FileConfig;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author Jie Xu
 */
public class BitmapCompressUtil {

    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
    private int compressQuality = 50;
    private String mFileOutputPath;
    private Context context;


    /**
     * 压缩
     *
     * @param fileInputPath          需要压缩图片SD卡路径
     * @param bitmapCompressCallback 压缩回调
     */
    public void bitmapCompress(String fileInputPath, BitmapCompressCallback bitmapCompressCallback) {
        new BitmapCropTask(bitmapCompressCallback).execute(fileInputPath);
    }

    class BitmapCropTask extends AsyncTask<String, Void, Exception> {
        BitmapCompressCallback bitmapCompressCallback;

        BitmapCropTask(BitmapCompressCallback bitmapCompressCallback) {
            this.bitmapCompressCallback = bitmapCompressCallback;
        }

        @Override
        protected Exception doInBackground(String... params) {
            Bitmap bitmap = decodeFile(params[0]);
            return compress(bitmap);
        }

        private Bitmap decodeFile(String param) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            int bmpWidth = options.outWidth;
            int bmpHeght = options.outHeight;

            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(metrics);
            int screenWidth = metrics.widthPixels;
            int screenHeight = metrics.heightPixels;

            options.inSampleSize = 1;
            if (bmpWidth > bmpHeght) {
                if (bmpWidth > screenWidth)
                    options.inSampleSize = bmpWidth / screenWidth;
            } else {
                if (bmpHeght > screenHeight)
                    options.inSampleSize = bmpHeght / screenHeight;
            }
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(param, options);
        }

        private Exception compress(Bitmap bitmap) {
//            File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

            File outDir = new File(FileConfig.COMPRESS_IMG_PATH);
            if (!outDir.exists()) {
                outDir.mkdirs();
            }
            File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
            mFileOutputPath = outFile.getAbsolutePath();
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(outFile);
                bitmap.compress(compressFormat, compressQuality, fileOutputStream);
                bitmap.recycle();
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                return e;
            }
            return null;
        }


        @Override
        protected void onPostExecute(Exception e) {
            if (e == null) {
                bitmapCompressCallback.onCompressSuccess(mFileOutputPath);
            } else {
                bitmapCompressCallback.onCompressFailure(e.getMessage());
            }
            super.onPostExecute(e);
        }
    }

    public interface BitmapCompressCallback {

        void onCompressSuccess(String fileOutputPath);

        void onCompressFailure(String t);

    }

    public void setCompressFormat(Bitmap.CompressFormat compressFormat) {
        this.compressFormat = compressFormat;
    }

    /**
     * 设置压缩图片质量
     *
     * @param compressQuality 取值0-100
     */
    public void setCompressQuality(int compressQuality) {
        this.compressQuality = compressQuality;
    }

    public BitmapCompressUtil(Context context) {
        this.context = context;
    }
}
