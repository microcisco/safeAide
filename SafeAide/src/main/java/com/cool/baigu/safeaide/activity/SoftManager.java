package com.cool.baigu.safeaide.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cool.baigu.safeaide.R;
import com.cool.baigu.safeaide.beans.AppBaeInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by baigu on 2017/9/21.
 */

public class SoftManager extends Activity {

    private long freeSpace;
    private long externalStorageDirectoryFreeSpace;
    TextView tv_lbcc;
    TextView tv_wbcc;
    TextView title;
    List<AppBaeInfo> appsInfo = new ArrayList<>();
    ListView lv_all_soft;

    List<AppBaeInfo> appsInfoUser = new ArrayList<>();
    List<AppBaeInfo> appsInfoSys = new ArrayList<>();
    private PopupWindow popupWindow;
    private UninstallBR uninstallBR;
    private AppinfoAdapter appinfoAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.soft_manager);

        initData();
        initView();
        initEvent();
        //注册卸载
        uninstallBR = new UninstallBR();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        registerReceiver(uninstallBR, intentFilter);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        //设置滚动监听事件
        lv_all_soft.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                if (firstVisibleItem > appsInfoUser.size()) {
                    title.setText("系统软件");
                } else {
                    title.setText("用户软件");
                }
            }
        });
        //设置item点击事件
        lv_all_soft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                final AppInfoConvert viewTag = (AppInfoConvert) view.getTag();
                if (!viewTag.isValid()) {
                    return;
                }
                //如果是有效的item
                View inflate = View.inflate(getApplicationContext(), R.layout.app_xuanxian, null);
                //设置事件监听
                TextView uninstall = (TextView) inflate.findViewById(R.id.i1);
                TextView start = (TextView) inflate.findViewById(R.id.i2);
                TextView share = (TextView) inflate.findViewById(R.id.i3);
                TextView info = (TextView) inflate.findViewById(R.id.i4);
                uninstall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                        Intent intent = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + viewTag.getPackageName()));
                        startActivity(intent);
                    }
                });
                start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                        Intent intent = getPackageManager().getLaunchIntentForPackage(viewTag.getPackageName());
                        startActivity(intent);
                    }
                });
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, "这个软件牛逼");
                        startActivity(intent);
                    }
                });
                info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (popupWindow != null) {
                            popupWindow.dismiss();
                        }
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setData( Uri.parse("package:" + viewTag.getPackageName()));
                        startActivity(intent);
                    }
                });


                popupWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.showAsDropDown(view, 180, -100);
            }
        });
    }

    /**
     * 初始化view
     */
    private void initView() {
        tv_lbcc = (TextView) findViewById(R.id.tv_lbcc);
        tv_wbcc = (TextView) findViewById(R.id.tv_wbcc);
        title = (TextView) findViewById(R.id.title);
        tv_lbcc.setText("内存可用：" + Formatter.formatFileSize(getApplicationContext(), freeSpace));
        tv_wbcc.setText("SD卡可用：" + android.text.format.Formatter.formatFileSize(getApplicationContext(), externalStorageDirectoryFreeSpace));  //格式化成XXGB的形式

        lv_all_soft = (ListView) findViewById(R.id.lv_all_soft);
        appinfoAdapter = new AppinfoAdapter();
        lv_all_soft.setAdapter(appinfoAdapter);


    }

    /**
     * 初始化数据
     */
    private void initData() {
        appsInfoUser = new ArrayList<>();
        appsInfoSys = new ArrayList<>();
        File dataDirectory = Environment.getDataDirectory();    //手机内存
        freeSpace = dataDirectory.getFreeSpace();
        File externalStorageDirectory = Environment.getExternalStorageDirectory();  //手机外存（SD卡）
        externalStorageDirectoryFreeSpace = externalStorageDirectory.getFreeSpace();
        //获取所有包信息
        PackageManager packageManager = getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        for (PackageInfo pi :
                installedPackages
                ) {
            AppBaeInfo appBaeInfo = new AppBaeInfo();
            appBaeInfo.setPackName(pi.packageName);
            appBaeInfo.setIcon(pi.applicationInfo.loadIcon(packageManager));    //获取icon
            appBaeInfo.setName(pi.applicationInfo.loadLabel(packageManager).toString());  //获取软件名
            appBaeInfo.setVersion(pi.versionName);    //版本
            appBaeInfo.setDataApp(((pi.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0));  //是否安装在内存
            appBaeInfo.setSize(Formatter.formatFileSize(getApplicationContext(), (new File(pi.applicationInfo.sourceDir)).length()));   //大小
            appBaeInfo.setSysApp(((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1));  //是否是系统
            appsInfo.add(appBaeInfo);
            if (appBaeInfo.isSysApp()) {
                appsInfoSys.add(appBaeInfo);
            } else {
                appsInfoUser.add(appBaeInfo);
            }
        }

        List<AppBaeInfo> t = new ArrayList<>();
        for (AppBaeInfo i : appsInfoUser
                ) {
            t.add(i);
        }
        for (AppBaeInfo i : appsInfoSys
                ) {
            t.add(i);
        }
        appsInfo = t;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        unregisterReceiver(uninstallBR);
    }

    class AppinfoAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return appsInfo.size() + 2;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position == 0) {
                TextView textView = new TextView(SoftManager.this);
                textView.setText("用户软件");
                textView.setTextSize(16);
                textView.setBackgroundResource(R.color.colorPrimary);
                AppInfoConvert appInfoConvert = new AppInfoConvert();
                textView.setTag(appInfoConvert);
                return textView;
            }
            if (position == appsInfoUser.size() + 1) {
                TextView textView = new TextView(SoftManager.this);
                textView.setText("系统软件");
                textView.setTextSize(16);
                textView.setBackgroundResource(R.color.colorPrimary);
                AppInfoConvert appInfoConvert = new AppInfoConvert();
                textView.setTag(appInfoConvert);
                return textView;
            }

            if (position <= appsInfoUser.size()) {
                position = position - 1;
            }

            if (position > appsInfoUser.size()) {
                position = position - 2;
            }

            AppInfoConvert convertViewTag = new AppInfoConvert();
            if (convertView != null) {
                convertViewTag = (AppInfoConvert) convertView.getTag();
            }

            if (convertView == null || !convertViewTag.isValid()) {
                convertView = View.inflate(getApplicationContext(), R.layout.app_info_item, null);
                AppInfoConvert appInfoConvert = new AppInfoConvert();
                appInfoConvert.setIcon((ImageView) convertView.findViewById(R.id.icon));
                appInfoConvert.setName((TextView) convertView.findViewById(R.id.tv_app_name));
                appInfoConvert.setPosition((TextView) convertView.findViewById(R.id.tv_app_position));
                appInfoConvert.setSize((TextView) convertView.findViewById(R.id.tv_app_size));
                appInfoConvert.setType((TextView) convertView.findViewById(R.id.tv_app_type));
                appInfoConvert.setValid(true);
                convertView.setTag(appInfoConvert);
            }
            AppInfoConvert appc = (AppInfoConvert) convertView.getTag();
            AppBaeInfo info = appsInfo.get(position);
            appc.getIcon().setImageDrawable(info.getIcon());
            appc.getName().setText(info.getName());
            appc.getSize().setText(info.getSize());
            appc.setPackageName(info.getPackName());

            appc.getPosition().setText(info.isDataApp() ? "位置：内存" : "位置：SD卡");
            appc.getType().setText(info.isSysApp() ? "类型：系统应用" : "类型：用户应用");

            return convertView;
        }
    }

    class AppInfoConvert {
        ImageView icon;
        TextView name;
        TextView type;
        TextView position;
        TextView size;
        boolean valid;
        String packageName;

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public ImageView getIcon() {
            return icon;
        }

        public void setIcon(ImageView icon) {
            this.icon = icon;
        }

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        public TextView getType() {
            return type;
        }

        public void setType(TextView type) {
            this.type = type;
        }

        public TextView getPosition() {
            return position;
        }

        public void setPosition(TextView position) {
            this.position = position;
        }

        public TextView getSize() {
            return size;
        }

        public void setSize(TextView size) {
            this.size = size;
        }
    }

    class UninstallBR extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            initData();
            appinfoAdapter.notifyDataSetChanged();

        }
    }

}
