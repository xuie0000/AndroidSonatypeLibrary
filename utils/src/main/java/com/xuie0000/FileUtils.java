package com.xuie0000;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.sort;

/**
 * @author Dimorinny
 * @date 24.10.15
 */
public class FileUtils {
    public static List<File> getFileListByDirPath(String path) {
        File directory = new File(path);
        List<File> resultFiles = new ArrayList<>();

        // TODO: filter here
        File[] files = directory.listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (f.isHidden()) {
                    continue;
                }
                resultFiles.add(f);
            }

            sort(resultFiles, new FileComparator());
        }
        return resultFiles;
    }

    public static String cutLastSegmentOfPath(String path) {
        return path.substring(0, path.lastIndexOf("/"));
    }

    public static String getReadableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private static final int DEFAULT_COMPRESS_QUALITY = 100;
    private static final String PATH_DOCUMENT = "document";
    private static final String PATH_TREE = "tree";


    private static String getFilePathFromUri(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * get SDPath, if no sdcard, return null
     */
    public static String getSDPath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        } else {
            Log.e("SDCard", "no sdcard found!");
            return null;
        }
    }

    public static String getBasePath(Context context) {
        if (null == getSDPath()) {
            return context.getCacheDir().getAbsolutePath();
        }

        File file = new File(getSDPath() + File.separator + context.getPackageName());
        if (!file.exists()) {
            file.mkdir();
        }

        return file.getAbsolutePath();
    }


    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * loadFromAssets
     */
    public static String loadFromAssets(Context context, String fileName) {
        BufferedReader reader = null;
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            reader = new BufferedReader(new InputStreamReader(in));

            char[] buf = new char[1024];
            int count;
            StringBuilder sb = new StringBuilder(in.available());
            while ((count = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, count);
                sb.append(readData);
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(reader);
        }

        return "";
    }

    /**
     * 删除文件
     *
     * @param filePath 路径
     */
    public static void deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteFiles(ArrayList<String> filePaths) {
        for (int i = 0; i < filePaths.size(); i++) {
            deleteFile(filePaths.get(i));
        }
    }
}
