package com.amap.monitor.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.amap.monitor.R;

/**
 * Created by Administrator on 2016/5/4.
 */
public class BaseActivity extends AppCompatActivity {



    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initView();
        initConfig();
        initData();
        initEvent();
    }
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initView();
        initConfig();
        initData();
        initEvent();
    }

    protected void initView(){

    }

    //初始化适配相关
    protected void initConfig(){

    }
    protected void initData(){

    }

    protected void initEvent(){

    }
    /** 短暂显示Toast提示(来自res) **/
    protected void showShortToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

    /** 短暂显示Toast提示(来自String) **/
    protected void showShortToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /** 长时间显示Toast提示(来自res) **/
    protected void showLongToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();
    }

    /** 长时间显示Toast提示(来自String) **/
    protected void showLongToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

//    /** 显示自定义Toast提示(来自res) **/
//    protected void showCustomToast(int resId) {
//        View toastRoot = LayoutInflater.from(BaseActivity.this).inflate(
//                R.layout.common_toast, null);
//        ((HandyTextView) toastRoot.findViewById(R.id.toast_text))
//                .setText(getString(resId));
//        Toast toast = new Toast(BaseActivity.this);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setView(toastRoot);
//        toast.show();
//    }
//
//    /** 显示自定义Toast提示(来自String) **/
//    protected void showCustomToast(String text) {
//        View toastRoot = LayoutInflater.from(BaseActivity.this).inflate(
//                R.layout.common_toast, null);
//        ((HandyTextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
//        Toast toast = new Toast(BaseActivity.this);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.setView(toastRoot);
//        toast.show();
//    }

}
