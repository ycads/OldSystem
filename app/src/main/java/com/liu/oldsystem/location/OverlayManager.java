package com.liu.oldsystem.location;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLngBounds.Builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class OverlayManager implements OnMarkerClickListener {
    BaiduMap a = null;
    private List<OverlayOptions> c = null;
    List<Overlay> b = null;

    public OverlayManager(BaiduMap var1) {
        this.a = var1;
        if(this.c == null) {
            this.c = new ArrayList();
        }

        if(this.b == null) {
            this.b = new ArrayList();
        }

    }

    public abstract List<OverlayOptions> getOverlayOptions();

    public final void addToMap() {
        if(this.a != null) {
            this.removeFromMap();
            List var1 = this.getOverlayOptions();
            if(var1 != null) {
                this.c.addAll(this.getOverlayOptions());
            }

            Iterator var2 = this.c.iterator();

            while(var2.hasNext()) {
                OverlayOptions var3 = (OverlayOptions)var2.next();
                this.b.add(this.a.addOverlay(var3));
            }

        }
    }

    public final void removeFromMap() {
        if(this.a != null) {
            Iterator var1 = this.b.iterator();

            while(var1.hasNext()) {
                Overlay var2 = (Overlay)var1.next();
                var2.remove();
            }

            this.c.clear();
            this.b.clear();
        }
    }

    public void zoomToSpan() {
        if(this.a != null) {
            if(this.b.size() > 0) {
                Builder var1 = new Builder();
                Iterator var2 = this.b.iterator();

                while(var2.hasNext()) {
                    Overlay var3 = (Overlay)var2.next();
                    if(var3 instanceof Marker) {
                        var1.include(((Marker)var3).getPosition());
                    }
                }

                this.a.setMapStatus(MapStatusUpdateFactory.newLatLngBounds(var1.build()));
            }

        }
    }
}
