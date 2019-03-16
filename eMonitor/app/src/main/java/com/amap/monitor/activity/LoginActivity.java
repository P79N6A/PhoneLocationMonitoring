package com.amap.monitor.activity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.monitor.MainActivity;
import com.amap.monitor.R;
import com.amap.monitor.bean.User;
import com.amap.monitor.util.Common;
import com.amap.monitor.util.FileUtil;
import com.amap.monitor.util.HttpUtil;
import com.amap.monitor.util.VerifyUtils;
import com.amap.monitor.util.VersionUtil;
import com.yanzhenjie.permission.AndPermission;

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


        try {
            //初始化界面，分为管理员界面和员工界面
            initMainActivity();

            mContext = this;
            initView();
            mAccount.setText(Common.getUsername(this));
            mPassword.setText(Common.getPassword(this));
            sp = mContext.getSharedPreferences("userinfo", mContext.MODE_PRIVATE);
            Common.setPermissions(LoginActivity.this);
        }catch (Exception e){
            String a = e.getMessage();
        }



    }

    private void initMainActivity(){
        if(Common.getIsAdmin(LoginActivity.this)){
            Intent intent = new Intent(LoginActivity.this, DrawTraceActivity.class);
            startActivity(intent);
        }else if(Common.getEmpId(LoginActivity.this)>0){
            Intent intent = new Intent(LoginActivity.this, TextLocationActivity.class);
            startActivity(intent);
        }
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
                        String res = result.getString("data");
                        String[] datas = res.split("#");
                        String empids = datas[0].replace("\"","");
                        if(Integer.valueOf(empids) > 0){
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("USERNAME", account);
                            editor.putString("PASSWORD",password);
                            editor.putString("empid",datas[0]);
                            editor.putString("isadmin",datas[1]);
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
                        Intent intent = new Intent(mContext, TextLocationActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        showLongToast("账号或密码错误");
                    }
                }
            }.execute();

        }
    };
//----------------------------------权限回调处理----------------------------------//

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /**
         * 转给AndPermission分析结果。
         *
         * @param object     要接受结果的Activity、Fragment。
         * @param requestCode  请求码。
         * @param permissions  权限数组，一个或者多个。
         * @param grantResults 请求结果。
         */
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
    private static final int REQUEST_CODE_PERMISSION_SD = 101;
    private static final int REQUEST_CODE_SETTING = 300;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SETTING: {
                Toast.makeText(this, R.string.message_setting_back, Toast.LENGTH_LONG).show();
                //设置成功，再次请求更新

                //VersionUtil versionUtil = new VersionUtil(LoginActivity.this);
                //versionUtil.getVersion(versionUtil.getVersion(mContext));

                break;
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

            Common.runInChildThread(new Runnable() {
                @Override
                public void run() {
                    try{
                        com.alibaba.fastjson.JSONObject result = HttpUtil.doGet(Common.VersionUrl,null);
                        String res = result.getString("data");
                        Message message = new Message();
                        message.obj = res;
                        mHandler .sendMessage(message);

                    }catch (Exception ex){
                        String ss = ex.getMessage();
                    }
                }
            });



    }

    Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                VersionUtil versionUtil = new VersionUtil(LoginActivity.this);
                int vision = versionUtil.getVersion(LoginActivity.this);
                String[] datas = msg.obj.toString().split("#");
                versionUtil.getVersion(vision,datas[0],datas[1]);
            }catch (Exception ex){
                String ss =ex.getMessage();
            }
        }
    };

}
