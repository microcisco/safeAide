package com.cool.baigu.safeaide.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cool.baigu.safeaide.Constant;
import com.cool.baigu.safeaide.R;
import com.cool.baigu.safeaide.beans.VersionInfo;
import com.cool.baigu.safeaide.utils.BufferToString;
import com.cool.baigu.safeaide.utils.SPUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

public class LogoActivity extends Activity {

    private RelativeLayout rl_logo_root;
    private VersionInfo checkVersionInfo;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);    //设置无标题
        setContentView(R.layout.activity_main);

//        SPUtils.setBoolean(LogoActivity.this, Constant.CHECKUPDATE, false);

        initView();
        initAnimation();

        //复制数据库
        initDB();
    }

    /**
     * 复制归属地号码数据库到手机
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initDB() {
        File dataDir = getFilesDir();
        final File dbFile = new File(dataDir.getAbsoluteFile() + "/address.db");
        //不存在就复制进去
        if(!dbFile.exists()) {
            new Thread(){
                @Override
                public void run() {
                    try {
                        InputStream inputStream = getAssets().open("address.db");
                        FileOutputStream fileOutputStream = new FileOutputStream(dbFile);
                        int len;
                        byte[] buffer = new byte[1024];
                        while ((len = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, len);
                        }
                        fileOutputStream.close();
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

        //常用号码数据库
        File file = new File(getFilesDir() + "/commonnum.db");
        if(!file.exists()) {
            try {
                InputStream inputStream = getAssets().open("commonnum.db");
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                FileOutputStream out = new FileOutputStream(file);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out);
                byte[] bytes = new byte[1024];
                int len;
                while ((len = bufferedInputStream.read(bytes)) != -1) {
                    bufferedOutputStream.write(bytes, 0, len);
                }
                bufferedInputStream.close();
                bufferedOutputStream.close();
                inputStream.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        rl_logo_root = (RelativeLayout) findViewById(R.id.rl_logo_root);
        TextView tv_logo_version_name = (TextView) findViewById(R.id.tv_logo_version_name);
        TextView tv_logo_version_code = (TextView) findViewById(R.id.tv_logo_version_code);

        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo info = packageManager.getPackageInfo(getPackageName(), 0);
            tv_logo_version_name.setText(info.versionName);                   //获取版本名
            tv_logo_version_code.setText(String.valueOf(info.versionCode));   //获取版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void initAnimation() {
        //旋转动画
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setFillAfter(true);
        //缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(.0f, 1.0f, .0f, 1.0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
        scaleAnimation.setDuration(2000);
        scaleAnimation.setFillAfter(true);
        //透明度动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(.0f, 1.0f);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);
        //组合所有动画
        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(rotateAnimation);
        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                boolean needCheckUpdate = SPUtils.getBoolean(LogoActivity.this, Constant.CHECKUPDATE);
                if(needCheckUpdate) {
                    checkUpdate();
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boolean needCheckUpdate = SPUtils.getBoolean(LogoActivity.this, Constant.CHECKUPDATE);
                if(needCheckUpdate) {
                    try {
                        if(checkVersionInfo == null) {
                            Toast.makeText(LogoActivity.this, "连接更新服务器超时或版本", Toast.LENGTH_SHORT).show();
                            showMain();
                            return;
                        }
                        if(checkVersionInfo.version_code != getPackageManager().getPackageInfo(getPackageName(), 0).versionCode) {
                            showUpdateDialog();
                        } else {
                            showMain();
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    showMain();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        animationSet.setAnimationListener(animationListener);
        rl_logo_root.startAnimation(animationSet);
    }

    /**
     * 显示是否升级对话框
     */
    private void showUpdateDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LogoActivity.this);
        builder.setTitle("检测到新版本");
        builder.setMessage(checkVersionInfo.version_des);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showMain();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                HttpUtils httpUtils = new HttpUtils();

                final File file = new File(Environment.getExternalStorageDirectory(), "xxx.apk");
                httpUtils.download(checkVersionInfo.down_url, file.getAbsolutePath(), false, new RequestCallBack<File>() {
                    @Override
                    public void onSuccess(ResponseInfo<File> responseInfo) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file),
                                "application/vnd.android.package-archive");

                        LogoActivity.this.startActivityForResult(intent, 0);

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Log.d(TAG, "onSuccess: 下载失败");
                    }
                });


            }
        });
        builder.show();
    }

    /**
     * 显示主界面
     */
    private void showMain() {

//        Toast.makeText(LogoActivity.this, "进入主界面了",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(LogoActivity.this, HomeActive.class);
        startActivity(intent);
        finish();

    }

    /**
     * 检查应用更新
     */
    private void checkUpdate() {

        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(Constant.URL.CHECKUPDATEURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(2000);
                    connection.setRequestMethod("GET");

                    int code = connection.getResponseCode();
                    if(code == 200) {
                        InputStream inputStream = connection.getInputStream();
                        String string = BufferToString.Stream2String(inputStream);
                        JSONObject jsonObject = new JSONObject(string);
                        checkVersionInfo = new VersionInfo(jsonObject.getInt("version_code"),
                                jsonObject.getString("version_name"),
                                jsonObject.getString("version_des"),
                                jsonObject.getString("down_url")
                        );
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LogoActivity.this, "连接更新服务器失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showMain();
    }
}
