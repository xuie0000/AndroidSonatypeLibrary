package com.xuie0000;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * 服务是否运行检测帮助类
 *
 * @author Jie Xu
 */
public class ServiceUtils {

    /**
     * 判断服务是否后台运行
     *
     * @param context   Context
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRun(Context context, String className) {
        boolean isRun = false;
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(40);
        int size = serviceList.size();
        for (int i = 0; i < size; i++) {
            if (serviceList.get(i).service.getClassName().equals(className)) {
                isRun = true;
                break;
            }
        }
        return isRun;
    }

}
