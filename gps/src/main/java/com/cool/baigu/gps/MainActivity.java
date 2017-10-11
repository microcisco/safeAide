package com.cool.baigu.gps;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.InputStream;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取位置管理
        LocationManager s1 = (LocationManager) getSystemService(LOCATION_SERVICE);
        //获取所有支持的定位方式
        List<String> allProviders = s1.getAllProviders();

        //获取最好的定位方式
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        String bestProvider = s1.getBestProvider(criteria, true);

        //获取定位
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        s1.requestLocationUpdates(bestProvider, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {  //位置变化是发生回掉
                //经度
                double longitude = location.getLongitude();
                //纬度
                double latitude = location.getLatitude();
                Log.d("kaka", "onLocationChanged: longitude=" + longitude + "latitude" + latitude);
                Log.d("kaka", "=========================================");

                try {
                    InputStream inputStream = getApplicationContext().getAssets().open("axisoffset.dat");
                    ModifyOffset instance = ModifyOffset.getInstance(inputStream);
                    PointDouble pointDouble = new PointDouble(longitude, latitude);
                    PointDouble pointDouble1 = instance.s2c(pointDouble);
                    Log.d("kaka", "onLocationChanged: longitude=" + pointDouble1.x + "latitude" + pointDouble1.y);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });



    }
}
