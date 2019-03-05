package com.amap.monitor.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.monitor.activity.DrawTraceActivity;

public class Common {
    public static final String SERVER_URL = "http://www.cqycg.com";
    public static final String LOCATION_DATA_URL = SERVER_URL+"/monitor/location.php?act=login";

    //子线程执行代码
    public static void runInChildThread(Runnable runnable){
        new Thread(runnable).start();
    }

    //AMapLocation转成字符串
    public static String AMapLocationToString(AMapLocation aMapLocation){
        StringBuffer var1 = new StringBuffer();
        var1.append("latitude=" + aMapLocation.getLatitude()+"#");
        var1.append("longitude=" + aMapLocation.getLongitude()+"#");
        var1.append("province=" + aMapLocation.getProvince() + "#");
        var1.append("city=" + aMapLocation.getCity() + "#");
        var1.append("district=" + aMapLocation.getDistrict() + "#");
        var1.append("cityCode=" + aMapLocation.getCityCode() + "#");
        var1.append("adCode=" + aMapLocation.getAdCode() + "#");
        var1.append("address=" + aMapLocation.getAddress() + "#");
        var1.append("country=" + aMapLocation.getCountry() + "#");
        var1.append("road=" + aMapLocation.getRoad() + "#");
        var1.append("poiName=" + aMapLocation.getPoiName() + "#");
        var1.append("street=" + aMapLocation.getStreet() + "#");
        var1.append("streetNum=" + aMapLocation.getStreetNum() + "#");
        var1.append("aoiName=" + aMapLocation.getAoiName() + "#");
        var1.append("errorCode=" + aMapLocation.getErrorCode() + "#");
        var1.append("errorInfo=" + aMapLocation.getErrorInfo() + "#");
        var1.append("locationDetail=" + aMapLocation.getLocationDetail() + "#");
        var1.append("locationType=" + aMapLocation.getLocationType());
        return var1.toString();
    }

    //获取登陆用户名
    public static String getUsername(Context mContext){
        SharedPreferences sp = mContext.getSharedPreferences("userinfo", mContext.MODE_PRIVATE); //
        return sp.getString("USERNAME", ""); //获取sp里面存储的数据
    }

    //获取登陆密码
    public static String getPassword(Context mContext){
        SharedPreferences sp = mContext.getSharedPreferences("userinfo", mContext.MODE_PRIVATE); //
        return sp.getString("PASSWORD", ""); //获取sp里面存储的数据
    }

    //获取用户ID
    public static String getEmpId(Context mContext){
        SharedPreferences sp = mContext.getSharedPreferences("userinfo", mContext.MODE_PRIVATE); //
        return sp.getString("empid", ""); //获取sp里面存储的数据
    }


    /**
     * 设置Android6.0的权限申请
     */
    private void setPermissions(Activity mContext) {
        final String[] PERMISSION = new String[]{
                Manifest.permission.READ_CONTACTS,// 写入权限
                Manifest.permission.READ_EXTERNAL_STORAGE,  //读取权限
                Manifest.permission.ACCESS_COARSE_LOCATION,        //读取设备信息
                Manifest.permission.ACCESS_FINE_LOCATION        //读取设备信息
        };
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //Android 6.0申请权限
            ActivityCompat.requestPermissions(mContext,PERMISSION,1);
        }else{
            Log.i("申请","权限申请ok");
        }
    }
}
