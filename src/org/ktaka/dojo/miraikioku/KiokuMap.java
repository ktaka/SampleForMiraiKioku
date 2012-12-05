package org.ktaka.dojo.miraikioku;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Menu;

public class KiokuMap extends MapActivity {
	MapView mapView;    // この行を追加
	GeoPoint point;     // この行を追加
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kioku_map);
        // ここから
        Intent intent = getIntent();
        String location[] = intent.getStringExtra("location").split(",");
        float lat = Float.parseFloat(location[0]);
        float lon = Float.parseFloat(location[1]);
        point = new GeoPoint( (int)(lat * 1e6), (int)(lon * 1e6));
        mapView = (MapView) findViewById(R.id.mapview);
        MapController mapCtrl = mapView.getController();
        mapCtrl.setCenter(point);
        mapCtrl.setZoom(12);
        // ここまでを追加
        setThumbnailOverlay(intent.getStringExtra("ThumbUrl"), point);    // 追加
    }

    // ここから
    private void setBmpOverlay(Bitmap bmp) {
		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = new BitmapDrawable(getResources(), bmp);
		ThumbnailOverlay thumbOverlay = new ThumbnailOverlay(drawable, this);
		OverlayItem item = new OverlayItem(point, "Mirai", "Kioku");
		thumbOverlay.addOverlay(item);
		mapOverlays.add(thumbOverlay);
    }
    
    private void setThumbnailOverlay(String url, GeoPoint point) {
		Bitmap b = ImageMap.getImage(url);
		if(b != null) {
			setBmpOverlay(b);
		} else {
	    	new SetImageTask(url, new SetImageTask.SetImageTaskListener() {
				public void onSetImage(Bitmap bmp) {
					setBmpOverlay(bmp);
				}
			}).execute((Void)null);
		}
    }
    // ここまで追加
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_kioku_map, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
    // ここから
	class ThumbnailOverlay extends ItemizedOverlay<OverlayItem> {

		private List<OverlayItem> overlays = new ArrayList<OverlayItem>();
		Context mContext;

		public ThumbnailOverlay(Drawable defaultMarker) {
			  super(boundCenterBottom(defaultMarker));
		}

		public ThumbnailOverlay(Drawable defaultMarker, Context context) {
			  super(boundCenterBottom(defaultMarker));
			  mContext = context;
		}

		public void addOverlay(OverlayItem overlay) {
			overlays.add(overlay);
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return overlays.get(i);
		}

		@Override
		public int size() {
			return overlays.size();
		}

	}
    // ここまで追加
}
