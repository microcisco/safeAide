package com.cool.baigu.yijian;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DevicePolicyManager apm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        ComponentName name = new ComponentName(MainActivity.this, DevReceiver.class);

        if(apm.isAdminActive(name)) {
            apm.lockNow();
            finish();
        } else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, name);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    "激活");
            startActivityForResult(intent, 0);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 0) {
            finish();
            return;
        }
        DevicePolicyManager apm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        apm.lockNow();
        finish();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
