package com.xzs.shareelectric_second.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.github.shenyuanqing.zxingsimplify.zxing.Activity.CaptureActivity;
import com.xzs.shareelectric_second.R;
import com.xzs.shareelectric_second.application.MyApplication;
import com.xzs.shareelectric_second.entity.UserEntity;
import com.xzs.shareelectric_second.overlay.WalkRouteOverlay;
import com.xzs.shareelectric_second.utils.AMapUtil;
import com.xzs.shareelectric_second.utils.Base64Util;
import com.xzs.shareelectric_second.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity{

    private DrawerLayout main_dl;
    private NavigationView user_view;
    private View headerView;
    private ImageView userheader_circleimageview;
    private TextView user_tv_phone;
    private UserEntity userEntity;

    private List<String> permissionLists=new ArrayList<>();
    private MapView mapView;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;
    private RouteSearch routeSearch;
    private Location myLocation;
    private RouteSearch.WalkRouteQuery walkRouteQuery;
    private RouteSearch.FromAndTo fromAndTo;
    private WalkRouteOverlay walkRouteOverlay;
    private String time;
    private String distance;
    private Marker tempMark;
    private static final String TAG = "MainActivity";

    private FloatingActionButton main_fab_userinfo;
    private FloatingActionButton main_fab_message;
    private FloatingActionButton main_fab_scan;

    private static final int REQUEST_SCAN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        requestPermissions();
        mapView= (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initMap();
        LatLng latLng = new LatLng(29.670000,121.467000);
        Marker marker = aMap.addMarker(new MarkerOptions().position(latLng));
        LatLng latLng1 = new LatLng(29.662391,121.466500);
        Marker marker1 = aMap.addMarker(new MarkerOptions().position(latLng1));
        LatLng latLng2 = new LatLng(29.664000,121.466400);
        Marker marker2 = aMap.addMarker(new MarkerOptions().position(latLng2));
        LatLng latLng3 = new LatLng(29.891200,121.48120 );
        Marker marker3 = aMap.addMarker(new MarkerOptions().position(latLng3));

    }

    private void initMap(){
        if(aMap==null){
            aMap = mapView.getMap();
        }
            myLocationStyle=new MyLocationStyle();
        myLocationStyle.showMyLocation(true);//设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
        myLocationStyle.interval(2000);//设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);//连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
        // myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW) ;//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
         //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
        // myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);//连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
        myLocationStyle.showMyLocation(true);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        //aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(29.8884,121.48205))));//设置地图缩放级别
        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(new MyLocationChangedListener());
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setOnMarkerClickListener(new MyMarkerClickListener());
    }

    private void initView(){
        main_dl=(DrawerLayout)findViewById(R.id.main_dl);
        user_view=(NavigationView)findViewById(R.id.user_view);
        user_view.setNavigationItemSelectedListener(new MyNavigationItemSelectedListener());
        headerView=user_view.getHeaderView(0);
        user_tv_phone=headerView.findViewById(R.id.user_tv_phone);
        userheader_circleimageview=headerView.findViewById(R.id.userheader_circleimageview);
        userEntity= MyApplication.userEntity;
        user_tv_phone.setText(MyApplication.hidePhone(userEntity.getPhone()));
        userheader_circleimageview.setImageBitmap(Base64Util.base64ToBitmap(userEntity.getHeadImage()));
        userheader_circleimageview.setOnClickListener(new MyClickListener());
        main_fab_userinfo=(FloatingActionButton)findViewById(R.id.main_fab_userinfo);
        main_fab_userinfo.setOnClickListener(new MyOnClickListener());

        main_fab_message=(FloatingActionButton)findViewById(R.id.main_fab_message);
        main_fab_message.setOnClickListener(new MyOnClickListener());

        main_fab_scan=(FloatingActionButton)findViewById(R.id.main_fab_scan);
        main_fab_scan.setOnClickListener(new MyOnClickListener());
    }

    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.main_fab_userinfo:
                    main_dl.openDrawer(GravityCompat.START);
                    break;
                case R.id.main_fab_message:
                    startActivity(new Intent(MainActivity.this,MessageActivity.class));
                    break;
                case R.id.main_fab_scan:
                    jumpScanPage();
                    break;
                default:
                    break;
            }
        }
    }

    private void jumpScanPage() {
        startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class),REQUEST_SCAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_SCAN && resultCode == RESULT_OK){
            Toast.makeText(MainActivity.this,data.getStringExtra("barCode"),Toast.LENGTH_LONG).show();
        }
    }

    private class MyMarkerClickListener implements AMap.OnMarkerClickListener{
        @Override
        public boolean onMarkerClick(Marker marker) {
            if(tempMark!=null)
            {
                //tempMark.setIcon(smallIdentificationBitmap);
                walkRouteOverlay.removeFromMap();
                tempMark = null;
            }
            tempMark=marker;
            tempMark.showInfoWindow();
            LatLng latLng=tempMark.getPosition();
            LatLonPoint startPoint=new LatLonPoint(myLocation.getLatitude(),myLocation.getLongitude());
            LatLonPoint endPoint=new LatLonPoint(latLng.latitude,latLng.longitude);

            routeSearch=new RouteSearch(getApplicationContext());
            routeSearch.setRouteSearchListener(new MyRouteSearchListener());
            fromAndTo=new RouteSearch.FromAndTo(startPoint,endPoint);
            walkRouteQuery=new RouteSearch.WalkRouteQuery(fromAndTo,RouteSearch.WALK_DEFAULT);
            routeSearch.calculateWalkRouteAsyn(walkRouteQuery);
            return true;
        }
    }


    private class MyRouteSearchListener implements RouteSearch.OnRouteSearchListener{
        @Override
        public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
            if(i == AMapException.CODE_AMAP_SUCCESS){
                if (walkRouteResult != null && walkRouteResult.getPaths() != null) {
                    if (walkRouteResult.getPaths().size() > 0) {
                        final WalkPath walkPath = walkRouteResult.getPaths()
                                .get(0);
                        walkRouteOverlay = new WalkRouteOverlay(
                                getApplicationContext(), aMap, walkPath,
                                walkRouteResult.getStartPos(),
                                walkRouteResult.getTargetPos());
                        walkRouteOverlay.removeFromMap();
                        walkRouteOverlay.addToMap();
                        walkRouteOverlay.zoomToSpan();
                        int dis = (int) walkPath.getDistance();
                        int dur = (int) walkPath.getDuration();
                        time = AMapUtil.getFriendlyTime(dur);
                        distance = AMapUtil.getFriendlyLength(dis);
                        String des = AMapUtil.getFriendlyTime(dur)+"("+AMapUtil.getFriendlyLength(dis)+")";
                        tempMark.setTitle("步行"+time.toString());
                        tempMark.setSnippet("距离"+distance);
                        tempMark.showInfoWindow();
                        Log.e(TAG,des);
                    } else if (walkRouteResult != null && walkRouteResult.getPaths() == null) {
                        ToastUtil.show(getApplicationContext(), "对不起，没有搜索到相关数据！");
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "对不起，没查询到结果", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }

        @Override
        public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

        }

        @Override
        public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

        }

        @Override
        public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

        }
    }



    private class MyLocationChangedListener implements AMap.OnMyLocationChangeListener{
        @Override
        public void onMyLocationChange(Location location) {
            Log.d("info", "onMyLocationChange: "+location.getLatitude()+" "+location.getLongitude()+" ");
            myLocation=location;
        }
    }


    private void requestPermissions(){
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            permissionLists.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            permissionLists.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            permissionLists.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissionLists.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA)
                !=PackageManager.PERMISSION_GRANTED){
            permissionLists.add(Manifest.permission.CAMERA);
        }
        if(!permissionLists.isEmpty()){
            String[] permissions=permissionLists.toArray(new String[permissionLists.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0){
                    for(int results:grantResults){
                        if(results!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                }else{
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private class MyClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            startActivity(new Intent(MainActivity.this,UserInfoActivity.class));
        }
    }




    private class MyNavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()){
                case R.id.user_menu_record:
                    Toast.makeText(MainActivity.this, "checked user_menu_record", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,UserRecordActivity.class));
                    break;
                case R.id.user_menu_wallet:
                    Toast.makeText(MainActivity.this, "checked user_menu_wallet", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,UserWalletActivity.class));
                    break;
                case R.id.user_menu_invent:
                    Toast.makeText(MainActivity.this, "checked user_menu_invent", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.user_menu_service:
                    Toast.makeText(MainActivity.this, "checked user_menu_service", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }
    }

}
