package com.amap.monitor.util;

import android.util.Log;



public class LogUtil {
 
	public static void d(String msg){
		if(msg==null){
			return;
		}
			Log.d("yhy", msg);
	}
}
