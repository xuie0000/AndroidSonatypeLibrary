package com.xuie0000;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Jie Xu
 * @date 16-1-5
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    static final String TAG = CrashHandler.class.getSimpleName();
    static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/Android/data/";
    static final String FILE_NAME = "crash";
    static final String FILE_NAME_SUFFIX = ".trace";

    Thread.UncaughtExceptionHandler defaultCrashHandler;
    Context context;

    public CrashHandler(Context context) {
        defaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.context = context.getApplicationContext();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            saveEx2Sdcard(ex);
            uploadExceptionToServer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (defaultCrashHandler != null) {
            defaultCrashHandler.uncaughtException(thread, ex);
        } else {
            Process.killProcess(Process.myPid());
        }
    }

    private void saveEx2Sdcard(Throwable ex) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }

        File dir = new File(PATH);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.e(TAG, "create " + PATH + " fail");
                return;
            }
        }

        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.CHINA).format(new Date(current));
        File file = new File(PATH + FILE_NAME + time + FILE_NAME_SUFFIX);

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            dumpPhoneInfo(pw);
            pw.println(time);
            pw.print("wise exception:");
            pw.println();
            pw.println();
            ex.printStackTrace(pw);

            pw.close();
        } catch (Exception e) {
            Log.e(TAG, "dump crash info failed");
        }
    }

    private void uploadExceptionToServer() {
        //TODO Upload Exception Message To Your Web Server
    }

    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        //应用的版本名称和版本号
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);

        //android版本号
        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);

        //手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);

        //手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);

        //cpu架构
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
    }
}
