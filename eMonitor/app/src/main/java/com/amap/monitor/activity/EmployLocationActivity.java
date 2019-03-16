package com.amap.monitor.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.monitor.R;
import com.amap.monitor.common.LocationStatusManager;
import com.amap.monitor.services.LocationService;
import com.amap.monitor.util.Common;
import com.amap.monitor.util.Utils;

import java.util.HashMap;

public class EmployLocationActivity extends BaseActivity implements LocationSource {
    private Button buttonStartService;
    private TextView tvResult;
    private MapView mapView;
    Bundle mSavedInstanceState;

    private Button nologin = null;//退出登录
    private AMap aMap;
    private UiSettings mUiSettings;


    public static final String RECEIVER_ACTION = "location_in_background";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStartService = (Button) findViewById(R.id.button_start_service);
        tvResult = (TextView) findViewById(R.id.tv_result);


        //mapView.onCreate(savedInstanceState);// 此方法必须重写
        mSavedInstanceState = savedInstanceState;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVER_ACTION);
        registerReceiver(locationChangeBroadcastReceiver, intentFilter);

        //获取权限
        Common.setPermissions(EmployLocationActivity.this);



    }

    private Marker mMarkMyLocation;
    private void setMyStopLoca(final LatLng latlng) {
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17f));

        if(mMarkMyLocation!=null){
            mMarkMyLocation.destroy();
            mMarkMyLocation = null;
        }


        if (mMarkMyLocation == null) {
            final MarkerOptions markerOptions = new MarkerOptions();
            //markerOptions.snippet(dogId);
            // 设置Marker点击之后显示的标题
            markerOptions.setFlat(false);
            markerOptions.anchor(0.5f, 0.7f);
            markerOptions.zIndex(25);
            markerOptions.zIndex(90);
            ImageView iv = new ImageView(this);
            FrameLayout.LayoutParams fmIv = new FrameLayout.LayoutParams(100, 100);
            iv.setImageResource(R.mipmap.location);
            iv.setLayoutParams(fmIv);
            BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromView(iv);
            markerOptions.icon(markerIcon);
            markerOptions.position(latlng);
            mMarkMyLocation = aMap.addMarker(markerOptions);

        }else {
            mMarkMyLocation.setPosition(latlng);
        }
    }
    @Override
    protected void initView() {
        super.initView();
        initMap();
    }

    private void initMap(){
        mapView = (MapView) findViewById(R.id.map);

        //退到登录界面
        nologin = (Button)findViewById(R.id.nologin);
        nologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(EmployLocationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    Common.clearEmployInfo(EmployLocationActivity.this);
                }catch (Exception ex){
                    String e = ex.getMessage();
                }

            }
        });

        mapView.onCreate(mSavedInstanceState);// 此方法必须重写
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {
                    aMap.setMapType(AMap.MAP_TYPE_NAVI);
                    setMyLocationStyleIcon();
//                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(laQuick, loQuick), 17));
                }
            });

        }
        mUiSettings = aMap.getUiSettings();
        mUiSettings.setLogoPosition(2);//设置高德地图logo位置
        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setTiltGesturesEnabled(false);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.setMyLocationEnabled(true);
    }
    private void setMyLocationStyleIcon() {
//		 自定义系统定位小蓝点

        MyLocationStyle myLocationStyle = new MyLocationStyle();
        ImageView iv = new ImageView(this);
        FrameLayout.LayoutParams fmIv = new FrameLayout.LayoutParams(1, 1);
        iv.setImageResource(R.mipmap.location);
        iv.setLayoutParams(fmIv);
        BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromView(iv);
        myLocationStyle.myLocationIcon(markerIcon);// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(0f);// 设置圆形的边框粗细
//				myLocationStyle.
        myLocationStyle.anchor(0.5f, 0.9f);
        aMap.setMyLocationStyle(myLocationStyle);

//        aMap.setMyLocationEnabled(true);
////				// 设置定位的类型为 跟随模式
//        aMap.setMyLocationType(AMap.MAP_TYPE_NORMAL);

    }
    private BroadcastReceiver locationChangeBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            try{
                String action = intent.getAction();
                String locationResult = null;
                if (action.equals(RECEIVER_ACTION)) {
                    locationResult = intent.getStringExtra("result");
                    String[] locations = locationResult.split("#");
                    HashMap locals = new HashMap();
                    for (String location : locations) {
                        String[] values = location.split("=");
                        if(values!=null && values.length>1){
                            locals.put(values[0],values[1]);
                        }
                    }
                    String errorCode = locals.get("errorCode").toString();
                    if(errorCode.equals("0")){
                        Double mLocatinLat = Double.valueOf(locals.get("latitude").toString());
                        Double mLocationLon = Double.valueOf(locals.get("longitude").toString());

//
//                        setMyStopLoca(new LatLng(mLocatinLat, mLocationLon));
//                        TextView username = (TextView)findViewById(R.id.username);
//                        username.setText("您的名字:"+Common.getUsername(EmployLocationActivity.this));
//                        TextView address = (TextView)findViewById(R.id.address);
//                        address.setText("当前位置:"+locals.get("address"));
                    }
                }
            }catch (Exception e){

            }
        }
    };

    /**
     * 启动或者关闭定位服务
     *
     */
    public void startService(View v) {
        if (buttonStartService.getText().toString().equals(getResources().getString(R.string.startLocation))) {
            startLocationService();

            buttonStartService.setText(R.string.stopLocation);
            tvResult.setText("正在定位...");
        } else {
            stopLocationService();

            buttonStartService.setText(R.string.startLocation);
            tvResult.setText("");
        }
        LocationStatusManager.getInstance().resetToInit(getApplicationContext());
    }
    /**
     * 开始定位服务
     */
    private void startLocationService(){
        getApplicationContext().startService(new Intent(this, LocationService.class));
    }
    /**
     * 关闭服务
     * 先关闭守护进程，再关闭定位服务
     */
    private void stopLocationService(){
        sendBroadcast(Utils.getCloseBrodecastIntent());
    }


    @Override
    protected void onResume() {
        super.onResume();
        //stopLocationService();
        //startLocationService();
    }



    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }
}
