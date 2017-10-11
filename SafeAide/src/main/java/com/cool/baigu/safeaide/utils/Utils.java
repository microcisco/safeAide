package com.cool.baigu.safeaide.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.text.format.Formatter;

import com.cool.baigu.safeaide.beans.AppProcessInfo;
import com.cool.baigu.safeaide.beans.MemoryInfoSub;
import com.cool.baigu.safeaide.dao.Impl.TeleAttribution;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by baigu on 2017/8/24.
 */

public class Utils {
    public static boolean isEmpty(String s) {
        if (s == null || s.isEmpty()) {
            return true;
        }
        return false;
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static <T> int indexOf(T[] a, T ele) {
        int index = -1;
        for (int i = 0; i < a.length; ++i) {
            if(a[i].equals(ele)) {
                index = i;
            }
        }
        return index;
    }

    /**
     *  判断服务是否在开启状态
     *  @param context
     *  @param clzz
     *  @return 存活状态
     */
    public static boolean isLife(Context context, String clzz) {
        ActivityManager activityService = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = activityService.getRunningServices(100);

        for (ActivityManager.RunningServiceInfo serviceInfo : runningServices
                ) {
            if(clzz.equals(serviceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    /**
     * 获取号码归属地
     *
     * */
    public static String getAddress(Context c, String phone) {
        TeleAttribution attribution = new TeleAttribution(c.getFilesDir().getAbsolutePath() + "/address.db");
        String address = null;
        //判断号码类型
        NumberUtil.Number number = NumberUtil.checkNumber(phone);
        switch (number.getType()) {
            //手机
            case CELLPHONE:
                address = attribution.getAddress4Phone(number.getCode());
                break;
            //座机
            case FIXEDPHONE:
                address = attribution.getAddress4Landline(number.getCode());
                break;
            //无效号码
            case INVALIDPHONE:
                address = "未知归属地";
                break;
        }
        return address;
    }

    /**
     *  获取内存摘要信息
     *  @param c
     * @return
     */
    public static MemoryInfoSub getMenInfoSub(Context c) {
        MemoryInfoSub memoryInfoSub = new MemoryInfoSub();
        ActivityManager am = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);
        memoryInfoSub.setAvailable(Formatter.formatFileSize(c, memoryInfo.availMem));    //获取可用内存
        memoryInfoSub.setTotal(Formatter.formatFileSize(c, memoryInfo.totalMem));        //获取总内存
        return  memoryInfoSub;
    }

    /**
     *  总运行进程数量
     *  @param c
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static List<AppProcessInfo> getRunningProcess(Context c) {
        ActivityManager am = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = c.getPackageManager();
        ArrayList<AppProcessInfo> res = new ArrayList<>();
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo t:runningAppProcesses
             ) {
            AppProcessInfo info = new AppProcessInfo();
            res.add(info);
            info.setPackName(t.processName);    //设置包名（进程名）
            int size = am.getProcessMemoryInfo(new int[]{t.pid})[0].getTotalPrivateDirty() * 1024;    //获取进程占用的空间
            info.setSize(Formatter.formatFileSize(c, size));     //设置占用的空间
            info.setSize1(size);     //设置占用的空间
            try {
                PackageInfo packageInfo = pm.getPackageInfo(t.processName, 0);
                info.setName(packageInfo.applicationInfo.loadLabel(pm).toString());    //设置APP名
                info.setIcon(packageInfo.applicationInfo.loadIcon(pm));                //设置icon
                info.setSysApp((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1);   //设置是否是系统进程
            } catch (PackageManager.NameNotFoundException e) {
                info.setName(t.processName);
                e.printStackTrace();
            }
        }
        return res;
    }



}

