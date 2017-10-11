package com.cool.baigu.safeaide.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cool.baigu.safeaide.R;
import com.cool.baigu.safeaide.beans.AppProcessInfo;
import com.cool.baigu.safeaide.beans.MemoryInfoSub;
import com.cool.baigu.safeaide.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baigu on 2017/9/29.
 */

public class ProcessManager extends Activity {

    private MemoryInfoSub menInfoSub;
    private TextView tv_lbcc;
    private TextView tv_wbcc;
    private List<AppProcessInfo> runningProcess;
    private List<AppProcessInfo> runningProcessSys;
    private List<AppProcessInfo> runningProcessUser;
    private ListView lv_all_soft;
    private AppProcessInfoAda appProcessInfoAda;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.process_manager);

        initData();
        initView();
        initEvent();

    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        lv_all_soft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(view.getTag() == null) {
                    return;
                }
                ViewHolder hold = (ViewHolder) view.getTag();
                AppProcessInfo info;
                if(position < runningProcessUser.size() + 1) {
                    info = runningProcessUser.get(position - 1);
                } else {
                    info = runningProcessSys.get(position - runningProcessUser.size() - 2);
                }
                info.setChecked(!info.isChecked());
                hold.getXuanze().setChecked(info.isChecked());
            }
        });
        //按钮事件
        Button b1 = (Button)findViewById(R.id.b1);
        Button b2 = (Button)findViewById(R.id.b2);
        Button b3 = (Button)findViewById(R.id.b3);
        Button b4 = (Button)findViewById(R.id.b4);
        //全选
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (AppProcessInfo t:runningProcessUser
                     ) {
                    t.setChecked(true);
                }
                for (AppProcessInfo t:runningProcessSys
                     ) {
                    t.setChecked(true);
                }
                appProcessInfoAda.notifyDataSetChanged();
            }
        });
        //反选
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (AppProcessInfo t:runningProcessUser
                        ) {
                    t.setChecked(!t.isChecked());
                }
                for (AppProcessInfo t:runningProcessSys
                        ) {
                    t.setChecked(!t.isChecked());
                }
                appProcessInfoAda.notifyDataSetChanged();
            }
        });
        //清理
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                ArrayList<AppProcessInfo> t0 = new ArrayList<>();
                ArrayList<AppProcessInfo> t1 = new ArrayList<>();
                ArrayList<AppProcessInfo> t2 = new ArrayList<>();

                int str1 = 0;
                int str2 = 0;

                for (AppProcessInfo t : runningProcessUser
                        ) {
                    if (t.isChecked() && !t.getPackName().equals(getPackageName())) {
                        ++str1;
                        str2 += t.getSize1();
                        am.killBackgroundProcesses(t.getPackName());
                        continue;
                    }
                    t1.add(t);
                    t0.add(t);
                }
                for (AppProcessInfo t:runningProcessSys
                        ) {
                    if (t.isChecked() && !t.getPackName().equals(getPackageName())) {
                        ++str1;
                        str2 += t.getSize1();
                        am.killBackgroundProcesses(t.getPackName());
                        continue;
                    }
                    t2.add(t);
                    t0.add(t);
                }
                runningProcessUser = t1;
                runningProcessSys = t2;
                runningProcess = t0;
                Toast.makeText(getApplication(), "共结束" + str1 + "个进程，总共释放了" + Formatter.formatFileSize(getApplicationContext(), str2) + "内存", Toast.LENGTH_SHORT).show();
                appProcessInfoAda.notifyDataSetChanged();
            }
        });
        //设置
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /*
    * 初始化view
    * **/
    private void initView() {
        tv_lbcc = (TextView)findViewById(R.id.tv_lbcc);
        tv_wbcc = (TextView)findViewById(R.id.tv_wbcc);
        lv_all_soft = (ListView)findViewById(R.id.lv_all_soft);

        //内存占用总览显示
        tv_lbcc.setText("运行中进程：" + runningProcess.size() + "个");
        tv_wbcc.setText("剩余/总内存：" + menInfoSub.getAvailable() + "/" + menInfoSub.getTotal());
        //listView显示
        appProcessInfoAda = new AppProcessInfoAda();
        lv_all_soft.setAdapter(appProcessInfoAda);

    }

    /**
     * 初始化数据
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initData() {
        menInfoSub = Utils.getMenInfoSub(this);
        runningProcess = Utils.getRunningProcess(this);
        runningProcessSys = new ArrayList<>();
        runningProcessUser = new ArrayList<>();
        for (AppProcessInfo t : runningProcess
             ) {
            if (t.isSysApp()) {
                runningProcessSys.add(t);
                continue;
            }
            runningProcessUser.add(t);
        }
    }

    class ViewHolder {
        ImageView icon;
        TextView tv_app_name;
        TextView tv_app_size;
        CheckBox xuanze;

        public ImageView getIcon() {
            return icon;
        }

        public void setIcon(ImageView icon) {
            this.icon = icon;
        }

        public TextView getTv_app_name() {
            return tv_app_name;
        }

        public void setTv_app_name(TextView tv_app_name) {
            this.tv_app_name = tv_app_name;
        }

        public TextView getTv_app_size() {
            return tv_app_size;
        }

        public void setTv_app_size(TextView tv_app_size) {
            this.tv_app_size = tv_app_size;
        }

        public CheckBox getXuanze() {
            return xuanze;
        }

        public void setXuanze(CheckBox xuanze) {
            this.xuanze = xuanze;
        }
    }

    //
    class AppProcessInfoAda extends BaseAdapter {

        @Override
        public int getCount() {
            return runningProcess.size() + 2;
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
            //第一个title
            if (position == 0) {
                TextView textView = new TextView(ProcessManager.this);
                textView.setText("用户进程：" + runningProcessUser.size() + "个");
                textView.setTag(null);
                textView.setTextSize(16);
                textView.setBackgroundResource(R.color.colorPrimary);
                return textView;
            }
            //第二个个title
            if (position == runningProcessUser.size() + 1) {
                TextView textView = new TextView(ProcessManager.this);
                textView.setText("系统进程：" + runningProcessSys.size() + "个");
                textView.setTag(null);
                textView.setTextSize(16);
                textView.setBackgroundResource(R.color.colorPrimary);
                return textView;
            }
            //item处理
            if (convertView == null || convertView.getTag() == null) {
                convertView = View.inflate(ProcessManager.this, R.layout.app_process_info_item, null);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.setIcon((ImageView) convertView.findViewById(R.id.icon));
                viewHolder.setTv_app_name((TextView) convertView.findViewById(R.id.tv_app_name));
                viewHolder.setTv_app_size((TextView) convertView.findViewById(R.id.tv_app_size));
                CheckBox cb = (CheckBox) convertView.findViewById(R.id.xuanze);
                viewHolder.setXuanze(cb);
                convertView.setTag(viewHolder);
            }
            AppProcessInfo info;
            if(position <= runningProcessUser.size()) {
                info = runningProcessUser.get(position - 1);
            } else {
                info = runningProcessSys.get(position - runningProcessUser.size() - 2);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.getIcon().setImageDrawable(info.getIcon());
            holder.getTv_app_name().setText(info.getName());
            holder.getTv_app_size().setText(info.getSize());
            holder.getXuanze().setChecked(info.isChecked());
            if(getPackageName().equals(info.getPackName())) {
                holder.getXuanze().setVisibility(View.GONE);
            } else {
                holder.getXuanze().setVisibility(View.VISIBLE);
            }
            return convertView;
        }
    }


}
