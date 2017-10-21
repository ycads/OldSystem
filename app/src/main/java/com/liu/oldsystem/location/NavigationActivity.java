package com.liu.oldsystem.location;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URISyntaxException;
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
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.liu.oldsystem.MainActivity;
import com.liu.oldsystem.R;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.baidu.mapapi.utils.DistanceUtil.getDistance;

public  class NavigationActivity extends AppCompatActivity implements  OnGetGeoCoderResultListener,OnGetRoutePlanResultListener{
    TextView mTvinfo;
    MapView mMapView = null;
    private BaiduMap mBaiduMap;

    private String mSdcardPath=null;
    public static final String ROUTE_PLAN_NODE = "routePlanNode";
    private static final String APP_FOLDER_NAME="mikyouPath";
    private String authinfo = null;
    RouteLine route = null;
//    private WalkingRouteOverlay mRouteOverlay;
    private float mTotalDistance;
    private ArrayList<LatLng> myAllStep = new ArrayList();


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
        getSupportActionBar().setTitle("路线规划");
    }

    public void search_route(View view) {
        if (start == null || end == null) {
            Toast.makeText(this, "请先选择终点位置", Toast.LENGTH_SHORT).show();
        } else {
            //设置起终点信息，对于tranist search 来说，城市名无意义
            PlanNode stNode = PlanNode.withLocation(start);
            PlanNode enNode = PlanNode.withLocation(end);
            // 实际使用中请对起点终点城市进行正确的设定
            mSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(stNode)
                    .to(enNode));
        }
//        setUpBaiduAPPByMine();
    }

    /**
     * 我的位置到终点通过百度地图
     */
    void setUpBaiduAPPByMine() {

        try {
            Intent intent = Intent.getIntent("intent://map/direction?origin=我的位置&destination="+mTxtEndPos.getText()+"&mode=driving&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            if (isInstallByread("com.baidu.BaiduMap")) {
                startActivity(intent);
//                Log.e(TAG, "百度地图客户端已经安装");
            } else {
//                Log.e(TAG, "没有安装百度地图客户端");
                Toast.makeText(this,"没有安装百度地图客户端,请提前安装百度地图哦", Toast.LENGTH_SHORT).show();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    private void initBaiduMap(){
        //初始化地图
        mMapView.showZoomControls(false);

        mBaiduMap = mMapView.getMap();

        //地图点击事件处理
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {

            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onMapClick(LatLng arg0) {
                // TODO Auto-generated method stub
                mBaiduMap.hideInfoWindow();
            }
        });
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        //mSearch.setOnGetRoutePlanResultListener(this);
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {
                // TODO Auto-generated method stub
                //地图加载完成
                SearchButtonProcess();//调用路径规划
            }
        });
    }

    public void SearchButtonProcess() {
        //重置浏览节点的路线数据
        //route = null;
        mBaiduMap.clear();
        ArrayList<PlanNode> arg0 =new ArrayList<PlanNode>();
        //设置起终点、途经点信息，对于tranist search 来说，城市名无意义
        PlanNode stNode = PlanNode.withLocation(start);
        PlanNode enNode = PlanNode.withLocation(end);
        // 实际使用中请对起点终点城市进行正确的设定
        mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode)//起点
                .passBy(arg0)//途经点
                .to(enNode));//终点
    }

    private void initNaviPath() {//初始化导航路线的导航引擎
        BNOuterTTSPlayerCallback ttsCallback = null;
        BaiduNaviManager.getInstance().init(NavigationActivity.this, mSdcardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {

            @Override
            public void onAuthResult(int status, String msg) {
                if (status==0) {
                    authinfo = "key校验成功!";
                }else{
                    authinfo = "key校验失败!"+msg;
                }
                NavigationActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(NavigationActivity.this, authinfo, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void initSuccess() {
                Toast.makeText(NavigationActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_LONG).show();
            }

            @Override
            public void initStart() {
                Toast.makeText(NavigationActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_LONG).show();
            }

            @Override
            public void initFailed() {
                Toast.makeText(NavigationActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_LONG).show();
            }
        }, ttsCallback);
    }

    private void initBNRoutePlan(LatLng mySInfo,LatLng myEndInfo) {
        BNRoutePlanNode startNode=new BNRoutePlanNode(mySInfo.longitude, mySInfo.latitude, null, null, BNRoutePlanNode.CoordinateType.BD09LL);//根据得到的起点的信息创建起点节点
        BNRoutePlanNode endNode=new BNRoutePlanNode(myEndInfo.longitude, myEndInfo.latitude, null,null, BNRoutePlanNode.CoordinateType.BD09LL);//根据得到的终点的信息创建终点节点
        if (startNode!=null&&endNode!=null) {
            ArrayList<BNRoutePlanNode> list=new ArrayList<BNRoutePlanNode>();
            list.add(startNode);//将起点和终点加入节点集合中
            list.add(endNode);
            BaiduNaviManager.getInstance().launchNavigator(NavigationActivity.this, list, 1, true, new MyRoutePlanListener(list) );
        }
    }
    class MyRoutePlanListener implements BaiduNaviManager.RoutePlanListener {//路线规划监听器接口类
        private ArrayList<BNRoutePlanNode>mList=null;

        public MyRoutePlanListener(ArrayList<BNRoutePlanNode> list) {
            mList = list;
        }

        @Override
        public void onJumpToNavigator() {
            Intent intent=new Intent(NavigationActivity.this, PathGuideActivity.class);
            intent.putExtra(ROUTE_PLAN_NODE, mList);//将得到所有的节点集合传入到导航的Activity中去
            startActivity(intent);
        }

        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub

        }

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
        // 初始化路线规划模块，注册事件监听;
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
    }

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
            mTvinfo.setText(reverseGeoCodeResult.getAddressDetail().province+reverseGeoCodeResult.getAddressDetail().district + reverseGeoCodeResult.getAddressDetail().street + reverseGeoCodeResult.getAddressDetail().streetNumber);
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


//    //////////////////////////////////route plan
//    @Override
//    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
//
//    }

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

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult result) {
        // TODO Auto-generated method stub
//        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            Toast.makeText(NavigationActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
//                    .show();
//        }
//        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
//            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
//            // result.getSuggestAddrInfo()
//            return;
//        }
//        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//            mBaiduMap.clear();
//            route = result.getRouteLines().get(0);
//            mRouteOverlay = new MyWalkingRouteOverlay(mBaiduMap);
//            mBaiduMap.setOnMarkerClickListener(mRouteOverlay);
//            // routeOverlay = overlay;
//            mRouteOverlay.setData(result.getRouteLines().get(0));
//            mRouteOverlay.addToMap();
//            mRouteOverlay.zoomToSpan();
//            mTotalDistance = route.getDistance();
//            LatLng currentLanLang = null;
//            myAllStep.clear();
//
//            for (int i = 0; i < route.getAllStep().size(); i++) {
//
//                // Iterator iter = ((WalkingStep) route.getAllStep().get(i))
//                // .getWayPoints().iterator();
//
//                List<LatLng> LatLngList = ((WalkingRouteLine.WalkingStep) route.getAllStep()
//                        .get(i)).getWayPoints();
//                for (LatLng latlng1 : LatLngList) {
//
//                    if (currentLanLang != null) {
//                        double d1 = getDistance(latlng1, currentLanLang);
//                        if (d1 > 9.000000000000001E-005D) {
//                            int q = (int) (d1 / 9.000000000000001E-005D);
//                            double d2latitude = (latlng1.latitude - currentLanLang.latitude)
//                                    / q;
//                            double d3longitude = (latlng1.longitude - currentLanLang.longitude)
//                                    / q;
//                            for (int k = 1; k < q; k++) {
//                                LatLng LatLng2 = new LatLng(
//                                        currentLanLang.latitude + d2latitude
//                                                * k, currentLanLang.longitude
//                                        + d3longitude * k);
//                                myAllStep.add(LatLng2);
//                            }
//                        } else {
//                            myAllStep.add(latlng1);
//                        }
//                        currentLanLang = latlng1;
//                    } else {
//                        myAllStep.add(latlng1);
//                        currentLanLang = latlng1;
//                    }
//                }
//            }
//
//        }

    }

//    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {
//
//        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
//            super(baiduMap);
//        }
//
//        @Override
//        public BitmapDescriptor getStartMarker() {
//            // if (useDefaultIcon) {
//            return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
//            // }
//            // return null;
//        }
//
//        @Override
//        public BitmapDescriptor getTerminalMarker() {
//            // if (useDefaultIcon) {
//            return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
//            // }
//            // return null;
//        }
//    }

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
