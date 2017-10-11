package com.cool.baigu.safeaide.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cool.baigu.safeaide.Constant;
import com.cool.baigu.safeaide.R;
import com.cool.baigu.safeaide.recriver.DevReceiver;
import com.cool.baigu.safeaide.utils.SPUtils;
import com.cool.baigu.safeaide.utils.Utils;

/**
 * Created by baigu on 2017/8/22.
 */

public class HomeActive extends Activity {

    public static final String funcName[] = {"手机防盗", "通讯卫士", "软件管家", "进程管理", "流量统计", "病毒查杀", "缓存清理", "高级工具"};
    public static final String funcDesc[] = {"手机丢失好找", "反骚扰监听", "方便管理软件", "保持手机通畅", "注意流量超标", "手机安全保障", "手机快步如飞", "特性处理更好"};
    public static final int funcIcon[] = {
            R.drawable.a, R.drawable.b,
            R.drawable.c, R.drawable.d,
            R.drawable.e, R.drawable.f,
            R.drawable.j, R.drawable.h};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_home);

        initAnimation();

        GridView gridView = (GridView) findViewById(R.id.gv_home_items);
        gridView.setAdapter(new HomeAdater());

        initEvent();

        //没有权限申请权限
        DevicePolicyManager apm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        ComponentName componentName = new ComponentName(HomeActive.this, DevReceiver.class);
        if(!apm.isAdminActive(componentName)) {
            showBase();
        }
    }

    private void showBase() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActive.this);
        builder.setTitle("申请权限");
        builder.setMessage("要完全使用本程序需要给予设备管理权限");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                ComponentName name = new ComponentName(HomeActive.this, DevReceiver.class);
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, name);
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                        "激活权限");
                startActivityForResult(intent, 0);
            }
        });
        builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 0) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initEvent() {
        //设置按钮点击事件
        ImageView tv_home_setting = (ImageView) findViewById(R.id.tv_home_setting);
        tv_home_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(HomeActive.this, SetActive.class);
//                startActivity(intent);

                Intent intent = new Intent(getApplicationContext(), SetActive.class);
                startActivity(intent);

            }
        });
        //设置功能点击事件
        GridView gv_home_items = (GridView) findViewById(R.id.gv_home_items);
        gv_home_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    //手机防盗
                    case 0:
                        if (Utils.isEmpty(SPUtils.getString(HomeActive.this, Constant.SAFEPASSWORD))) {    //没设置密码 && 引导设置密码
                            showSetPassword();
                        } else {    //已经设置了安全密码 && 校验密码 && 引导进入安全设置
                            showCheckPassword();

                        }
                        break;
                    //黑名单
                    case 1:
                        Intent intent = new Intent(HomeActive.this, BlackListActivity.class);
                        startActivity(intent);
                        break;
                    //软件管家
                    case 2:
                        startActivity(new Intent(HomeActive.this, SoftManager.class));
                        break;
                    //进程管理
                    case 3:
                        startActivity(new Intent(HomeActive.this, ProcessManager.class));
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    //高级工具
                    case 7:
                        Intent intent7 = new Intent(HomeActive.this, Tools.class);
                        startActivity(intent7);
                        break;
                }
            }
        });

    }

    private void showCheckPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActive.this);
        View view = View.inflate(HomeActive.this, R.layout.set_password, null);
        builder.setView(view);
        final AlertDialog dialog = builder.show();

        final TextView s = (TextView) view.findViewById(R.id.s);
        final TextView pass = (TextView) view.findViewById(R.id.pass);
        final TextView pass1 = (TextView) view.findViewById(R.id.pass1);
        //
        s.setText("请输入密码");
        pass.setVisibility(View.GONE);
        pass1.setHint("安全密码");


        Button ok = (Button) view.findViewById(R.id.set_p_ok);
        Button cancel = (Button) view.findViewById(R.id.set_p_cancel);



        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //校验密码输入
                if(Utils.isEmpty(pass1.getText().toString())
                        ) {
                    Toast.makeText(HomeActive.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //校验密码
                if(!SPUtils.getString(HomeActive.this, Constant.SAFEPASSWORD).equals(Utils.md5(pass1.getText().toString()))) {
                    Toast.makeText(HomeActive.this, "密码不正确！", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                //显示安全导航
                showSaft();
            }
        });

    }

    /**
     * 显示安全导航
     */
    private void showSaft() {
        if(SPUtils.getBoolean(getApplicationContext(), Constant.FUN1FINISH)) {
            Intent intent = new Intent(HomeActive.this, Func5Main.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(HomeActive.this, Func1Main.class);
            startActivity(intent);
        }
    }

    private void showSetPassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActive.this);
        View view = View.inflate(HomeActive.this, R.layout.set_password, null);
        builder.setView(view);
        final AlertDialog dialog = builder.show();

        final TextView pass = (TextView) view.findViewById(R.id.pass);
        final TextView pass1 = (TextView) view.findViewById(R.id.pass1);
        Button ok = (Button) view.findViewById(R.id.set_p_ok);
        Button cancel = (Button) view.findViewById(R.id.set_p_cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //校验密码输入
                if(Utils.isEmpty(pass.getText().toString()) || Utils.isEmpty(pass1.getText().toString())
                        || !(pass.getText().toString().equals(pass1.getText().toString()))
                        ) {
                    Toast.makeText(HomeActive.this, "密码不一致或密码为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //保存密码
                updatePW(Utils.md5(pass1.getText().toString()));
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    /**
     * 保存手机防盗密码
     */
    private void updatePW(String pw) {
        SPUtils.setString(HomeActive.this, Constant.SAFEPASSWORD, pw);
    }

    private void initAnimation() {
        ImageView home_logo = (ImageView) findViewById(R.id.home_logo);
        ObjectAnimator animator = ObjectAnimator.ofFloat(home_logo, "rotationY", 0, 60, 120, 180, 240, 360);
        animator.setDuration(2000);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.start();


    }

    class HomeAdater extends BaseAdapter {

        @Override
        public int getCount() {
            return funcName.length;
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
            View inflate = View.inflate(getApplicationContext(), R.layout.item_hone, null);
            ImageView iv_phone_icon = (ImageView) inflate.findViewById(R.id.iv_phone_icon);
            TextView tv_phone_funcName = (TextView) inflate.findViewById(R.id.tv_phone_funcName);
            TextView tv_phone_funcDesc = (TextView) inflate.findViewById(R.id.tv_phone_funcDesc);
            iv_phone_icon.setImageResource(funcIcon[position]);
            tv_phone_funcName.setText(funcName[position]);
            tv_phone_funcDesc.setText(funcDesc[position]);
            return inflate;
        }
    }

}
