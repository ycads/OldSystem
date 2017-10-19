package com.liu.oldsystem.location;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteLine;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.liu.oldsystem.MainActivity;
import com.liu.oldsystem.R;

public  class NavigationActivity extends AppCompatActivity implements View.OnClickListener, OnGetGeoCoderResultListener,OnGetRoutePlanResultListener{
    TextView mTvinfo;
    MapView mMapView = null;
    private BaiduMap mBaiduMap;

    private Marker mMark;

    ////loc//////
    private MyLocationListener mLocationListener;
    private LocationClient mLocationClient;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    //geo
    private GeoCoder mGeoSearch;

    //poi
    private TextView mTxtStartPos;
    private TextView mTxtEndPos;
    private String mCity;
    //    private MapPosition mStartPosition;
//    private MapPosition mEndPosition;
    BDLocation startLocation;
    BDLocation endLocation;

    //RoutePlan
    private Button mSearchButton;
    RouteLine mroute = null;
    OverlayManager mrouteOverlay = null;
    private TextView popupText = null;//泡泡view
    RoutePlanSearch mSearch = null;
    LatLng start;
    LatLng end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_navigation);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(17).build()));
        initview();
        intloc();
        initGEO();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("路线导航");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Intent intent =new Intent(NavigationActivity.this,MainActivity.class);
//                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initview() {
        mTvinfo =(TextView)findViewById(R.id.tv_info);
        mTxtStartPos = (TextView) findViewById(R.id.tv_start_pos);
        mTxtEndPos = (TextView) findViewById(R.id.tv_end_pos);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.bt_search:{
//                if(mStartPosition.getAddress().isEmpty() ||mEndPosition.getAddress().isEmpty()){
//                    Toast.makeText(this, "请先选择起始位置", Toast.LENGTH_SHORT).show();
//                }else{
                LatLng from = new LatLng(116.30142, 40.05087);
                LatLng dest = new LatLng(116.39750, 39.90882);

                //设置起终点信息，对于tranist search 来说，城市名无意义
                PlanNode stNode = PlanNode.withLocation(from);
                PlanNode enNode = PlanNode.withLocation(dest);
                // 实际使用中请对起点终点城市进行正确的设定
                mSearch.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(enNode));
//                }
            }
        }
    }

    //    public void search_route(View v) {
////        LatLng from = start;
////        LatLng dest = end;
//        LatLng from = new LatLng(116.30142, 40.05087);
//        LatLng dest = new LatLng(116.39750, 39.90882);
//
//
//        //设置起终点信息，对于tranist search 来说，城市名无意义
//        PlanNode stNode = PlanNode.withLocation(from);
//        PlanNode enNode = PlanNode.withLocation(dest);
//        // 实际使用中请对起点终点城市进行正确的设定
//        mSearch.drivingSearch((new DrivingRoutePlanOption())
//                .from(stNode)
//                .to(enNode));
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mLocationClient.stop();
        mGeoSearch.destroy();
        mSearch.destroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        mLocationClient.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
        mLocationClient.stop();
    }

    //////////////////////////////////////////////////////////////////////////////////
    //定位
    private void intloc() {
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(this);
        mLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mLocationListener);
        initLocation();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            mBaiduMap.setMyLocationEnabled(true);
            mCity =location.getCity();
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
            //mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.navi_map_gps_locked);
            MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, null);  //第三个参数是位置图片没有就默认
            mBaiduMap.setMyLocationConfigeration(config);
            //以我的位置为中心
            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
            start = new LatLng(location.getLatitude(), location.getLongitude());
            mTxtStartPos.setText(location.getCity() + location.getDistrict() + location.getStreet());
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(latlng));
        }
    }


    /**
     * 定位初始化
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 10000;
        // option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }


    ///geo
    private void initGEO() {
        //Geo
        mGeoSearch = GeoCoder.newInstance();
        mGeoSearch.setOnGetGeoCodeResultListener(this);
        //////////////////////////////////////////////////////////////////////////////////
     /*地图监听GEO转换*/
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                final LatLng lat = latLng;
                mGeoSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(lat));
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }

        });
        //////////////////////////////BaiduMap.OnMapClickListener///////////////////////////////////
    }
    ///////////////OnGetGeoCoderResultListener////////////////////////////////////////////
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (TextUtils.isEmpty(reverseGeoCodeResult.getAddress())) {
            Toast.makeText(NavigationActivity.this, "地点解析失败，请重新选择", Toast.LENGTH_SHORT).show();
        } else {
            if (null != mMark) {
                mMark.remove();
            }
            mTvinfo.setText(reverseGeoCodeResult.getAddressDetail().district + reverseGeoCodeResult.getAddressDetail().street + reverseGeoCodeResult.getAddressDetail().streetNumber);
            mTxtEndPos.setText(reverseGeoCodeResult.getAddressDetail().city+reverseGeoCodeResult.getAddressDetail().district + reverseGeoCodeResult.getAddressDetail().street + reverseGeoCodeResult.getAddressDetail().streetNumber);

            /////show pos
            LatLng from = new LatLng(reverseGeoCodeResult.getLocation().latitude,
                    reverseGeoCodeResult.getLocation().longitude);
            end = new LatLng(reverseGeoCodeResult.getLocation().latitude,
                    reverseGeoCodeResult.getLocation().longitude);
            BitmapDescriptor bdB = BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_en);
            OverlayOptions ooP = new MarkerOptions().position(from).icon(bdB);
            mMark = (Marker) (mBaiduMap.addOverlay(ooP));
            MapStatus mMapStatus = new MapStatus.Builder().target(from)
                    .build();
            /////show pos
        }
    }


    ///////////////OnGetGeoCoderResultListener////////////////////////////////////////////


    //////////////////////////////////route plan
    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult massRouteResult) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult massRouteResult) {

    }


    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            mBaiduMap.clear();
            // mroute = result.getRouteLines().get(0);
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
            mrouteOverlay = overlay;
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
    }
    //定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
        }
    }
    //////////////////////////////////route plan
}
