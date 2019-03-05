package com.amap.monitor.activity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.amap.monitor.MainActivity;
import com.amap.monitor.R;
import com.amap.monitor.bean.User;
import com.amap.monitor.util.Common;
import com.amap.monitor.util.FileUtil;
import com.amap.monitor.util.HttpUtil;
import com.amap.monitor.util.VerifyUtils;

import org.json.JSONObject;

public class LoginActivity extends BaseActivity {

    private Context mContext;
    private RelativeLayout rl_user;
    private Button mLoginButton;
    private EditText mAccount;
    private EditText mPassword;
    public static final String SERVER_URL = "http://www.cqycg.com";
    public static final String LOCATION_DATA_URL = SERVER_URL+"/monitor/location.php?act=login";
    private SharedPreferences sp=null;
    public LoginActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        initView();
        mAccount.setText(Common.getUsername(this));
        mPassword.setText(Common.getPassword(this));
        sp = mContext.getSharedPreferences("userinfo", mContext.MODE_PRIVATE);
    }

    @Override
    protected void initView() {
        // TODO Auto-generated method stub
        rl_user = (RelativeLayout) findViewById(R.id.rl_user);
        mLoginButton = (Button) findViewById(R.id.login);
        mAccount = (EditText) findViewById(R.id.account);
        mPassword = (EditText) findViewById(R.id.password);
    }

    @Override
    protected void initEvent() {
        // TODO Auto-generated method stub
//        Animation anim = AnimationUtils.loadAnimation(mContext,
//                R.anim.login_anim);
//        anim.setFillAfter(true);
//        rl_user.startAnimation(anim);
        mLoginButton.setOnClickListener(loginOnClickListener);
    }

    private View.OnClickListener loginOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String account = mAccount.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            if (account.equals("")) {
                showLongToast("请填写账号");
                mAccount.requestFocus();
            } else if (password.equals("")) {
                showLongToast("请填写密码");
            }  else {
                tryLogin(account, password);
            }
        }
        @SuppressLint("StaticFieldLeak")
        private void tryLogin(final String account, final String password) {
            new AsyncTask<Void, Void, Integer>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected Integer doInBackground(Void... params) {
                    try {
                        String account = mAccount.getText().toString().trim();
                        String password = mPassword.getText().toString().trim();
                        String url = LOCATION_DATA_URL+"&account="+account+"&password="+password;
                        com.alibaba.fastjson.JSONObject result = HttpUtil.doGet(url,null);
                        if(result.getInteger("data") > 0){
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("USERNAME", account);
                            editor.putString("PASSWORD",password);
                            editor.putString("empid",result.getInteger("data").toString());
                            editor.commit();
                            //新建一个Editor对象来存储键值对用户名和密码
                            return 1;
                        }else {
                            return 0;
                        }
                    } catch (Exception e) {
                        Log.d("network", "IO异常");
                    }
                    return 0;

                }

                @Override
                protected void onPostExecute(Integer result) {
                    super.onPostExecute(result);
                    if (result == 1) {

                        Intent intent = new Intent(mContext, DrawTraceActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        showLongToast("服务器异常");
                    }
                }
            }.execute();

        }
    };

}
