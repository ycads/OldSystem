package com.liu.oldsystem.location;

import android.graphics.Color;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteLine.DrivingStep;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DrivingRouteOverlay extends OverlayManager {
    private DrivingRouteLine c = null;

    public DrivingRouteOverlay(BaiduMap var1) {
        super(var1);
    }

    public final List<OverlayOptions> getOverlayOptions() {
        if(this.c == null) {
            return null;
        } else {
            ArrayList var1 = new ArrayList();
            if(this.c.getAllStep() != null && this.c.getAllStep().size() > 0) {
                new ArrayList();
                Iterator var3 = this.c.getAllStep().iterator();

                while(var3.hasNext()) {
                    DrivingStep var4 = (DrivingStep)var3.next();
                    Bundle var5 = new Bundle();
                    var5.putInt("index", this.c.getAllStep().indexOf(var4));
//                    if(var4.getEntrace() != null) {
//                        var1.add((new MarkerOptions()).position(var4.getEntrace().getLocation()).anchor(0.5F, 0.5F).zIndex(10).rotate((float)(360 - var4.getDirection())).extraInfo(var5).icon(BitmapDescriptorFactory.fromAssetWithDpi("Icon_line_node.png")));
//                    }

                    if(this.c.getAllStep().indexOf(var4) == this.c.getAllStep().size() - 1 && var4.getExit() != null) {
                        var1.add((new MarkerOptions()).position(var4.getExit().getLocation()).anchor(0.5F, 0.5F).zIndex(10).icon(BitmapDescriptorFactory.fromAssetWithDpi("Icon_line_node.png")));
                    }
                }
            }

            if(this.c.getStarting() != null) {
                var1.add((new MarkerOptions()).position(this.c.getStarting().getLocation()).icon(this.getStartMarker() != null?this.getStartMarker(): BitmapDescriptorFactory.fromAssetWithDpi("Icon_start.png")).zIndex(10));
            }

            if(this.c.getTerminal() != null) {
                var1.add((new MarkerOptions()).position(this.c.getTerminal().getLocation()).icon(this.getTerminalMarker() != null?this.getTerminalMarker(): BitmapDescriptorFactory.fromAssetWithDpi("Icon_end.png")).zIndex(10));
            }

            if(this.c.getAllStep() != null && this.c.getAllStep().size() > 0) {
                LatLng var2 = null;
                List var9 = this.c.getAllStep();
                int var10 = var9.size();

                for(int var11 = 0; var11 < var10; ++var11) {
                    DrivingStep var6 = (DrivingStep)var9.get(var11);
                    if(var6.getWayPoints() != null && var6.getWayPoints().size() > 0) {
                        ArrayList var7 = new ArrayList();
                        if(var2 != null) {
                            var7.add(var2);
                        }

                        List var8 = var6.getWayPoints();
                        var7.addAll(var8);
                        var1.add((new PolylineOptions()).points(var7).width(10).color(Color.argb(178, 0, 78, 255)).zIndex(0));
                        var2 = (LatLng)var8.get(var8.size() - 1);
                    }
                }
            }

            return var1;
        }
    }

    public void setData(DrivingRouteLine var1) {
        this.c = var1;
    }

    public BitmapDescriptor getStartMarker() {
        return null;
    }

    public BitmapDescriptor getTerminalMarker() {
        return null;
    }

    public boolean onRouteNodeClick(int var1) {
        if(this.c.getAllStep() != null && this.c.getAllStep().get(var1) != null) {
//            Toast.makeText(a.a().e(), ((DrivingStep)this.c.getAllStep().get(var1)).getInstructions(), 1).show();
        }

        return false;
    }

    public final boolean onMarkerClick(Marker var1) {
        if(var1.getExtraInfo() != null) {
            this.onRouteNodeClick(var1.getExtraInfo().getInt("index"));
        }

        return true;
    }


}
