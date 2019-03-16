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
import com.autonavi.amap.mapcore.Convert;

public class Common {
    public static final String SERVER_URL = "http://www.cqycg.com";
    public static final String LOCATION_DATA_URL = SERVER_URL+"/monitor/location.php?act=login";
    public static final String VersionUrl = "http://www.cqycg.com//monitor/location.php?act=version";

    public static final String apkurl = "http://www.cqycg.com/app/cqycglocation.apk";

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
    public static Integer getEmpId(Context mContext){
        int empid = 0;
        try {
            SharedPreferences sp = mContext.getSharedPreferences("userinfo", mContext.MODE_PRIVATE); //
            empid = Integer.valueOf(sp.getString("empid", "")); //获取sp里面存储的数据
        }catch (Exception e){

        }
        return empid;
    }

    //获取是否为管理员
    public static boolean getIsAdmin(Context mContext){
        SharedPreferences sp = mContext.getSharedPreferences("userinfo", mContext.MODE_PRIVATE); //
        String isadmin = sp.getString("isadmin", ""); //获取sp里面存储的数据
        if(!isadmin.equals("") && Integer.valueOf(isadmin) == 1){
            return true;
        }else {
            return false;
        }
    }
    //获取是否为管理员
    public static boolean clearEmployInfo(Context mContext){
        SharedPreferences sp = mContext.getSharedPreferences("userinfo", mContext.MODE_PRIVATE); //
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USERNAME", "");
        editor.putString("PASSWORD","");
        editor.putString("empid","0");
        editor.putString("isadmin","0");
        editor.commit();

        return true;
    }
    /**
     * 设置Android6.0的权限申请
     */
    public static void setPermissions(Activity mContext) {
        final String[] PERMISSION = new String[]{
                Manifest.permission.READ_CONTACTS,          // 读取联系人
                Manifest.permission.ACCESS_COARSE_LOCATION, //用于进行网络定位
                Manifest.permission.ACCESS_FINE_LOCATION,   //用于访问GPS定位
                Manifest.permission.WRITE_EXTERNAL_STORAGE, //写入外部存储
                Manifest.permission.READ_EXTERNAL_STORAGE,  //读取外部存储
                Manifest.permission.READ_PHONE_STATE        //读取电话状态
        };
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
          ||ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
          ||ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED      ) {
            //Android 6.0申请权限
            ActivityCompat.requestPermissions(mContext,PERMISSION,1);
        }else{
            Log.i("申请","权限申请ok");
        }
    }
}
