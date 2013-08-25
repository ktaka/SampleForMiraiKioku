package org.ktaka.dojo.miraikioku;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class KiokuMap extends FragmentActivity {
    private GoogleMap mMap;
    LatLng location;
    String title;
    String date;
    
    private static final LatLng ishinomakiBar = new LatLng(38.43015,141.307096);
    private static final LatLng ginzaBar = new LatLng(35.6674754, 139.7622696);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kioku_map);
        Intent intent = getIntent();
        title = intent.getStringExtra("Title");
        date = intent.getStringExtra("Date");
        String locationStr[] = intent.getStringExtra("location").split(",");
        double lat = Double.parseDouble(locationStr[0]);
        double lon = Double.parseDouble(locationStr[1]);
        location = new LatLng(lat, lon);
        setUpMapIfNeeded();
        setThumbnailOverlay(intent.getStringExtra("ThumbUrl"));    // 追加
    }
	
	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
		        mMap.moveCamera(CameraUpdateFactory.
			           	newCameraPosition(CameraPosition.fromLatLngZoom(
			                location,
			                    (float) 16.0)));
//		        UiSettings settings = mMap.getUiSettings();
//		        settings.setScrollGesturesEnabled(false);
			}
		}
	}
    // ここから
    private void setBmpOverlay(Bitmap bmp) {

    	if(bmp == null) {
    		Log.e("KiokuMap", "Failed to get thumbnail.");
    	} else {
    		BitmapDescriptor desc = BitmapDescriptorFactory.fromBitmap(bmp);
//    	GroundOverlayOptions options = new GroundOverlayOptions();
//    	options.image(desc);
//    	options.anchor(0, 1);
//    	options.position(location, 8600f, 6500f);
//    	 
//    	// マップに貼り付け・アルファを設定
//    	GroundOverlay overlay = mMap.addGroundOverlay(options);
//    	overlay.setTransparency(0.5F);
//
    		MarkerOptions options = new MarkerOptions();
    		// 緯度・経度
    		options.position(location);
    		// タイトル・スニペット
    		options.title(title);
    		options.snippet(date);
    		// アイコン(マップ上に表示されるピン)
    		options.icon(desc);
    	 
    		// マーカーを貼り付け
    		mMap.addMarker(options);//

    		MarkerOptions optionsFukkouBar = new MarkerOptions();
    		// 緯度・経度
    		optionsFukkouBar.position(new LatLng(38.43015,141.307096));
    		// タイトル・スニペット
    		optionsFukkouBar.title("復興バー");
    		//optionsFukkouBar.snippet(date);
    		// アイコン(マップ上に表示されるピン)
//    		options.icon(desc);
    	 
    		// マーカーを貼り付け
    		mMap.addMarker(optionsFukkouBar);//
    		//139.7622696, 35.6674754
    		MarkerOptions optionsGinzaBar = new MarkerOptions();
    		// 緯度・経度
    		optionsGinzaBar.position(new LatLng(35.6674754, 139.7622696));
    		// タイトル・スニペット
    		optionsGinzaBar.title("銀座復興バー");
    		//optionsFukkouBar.snippet(date);
    		// アイコン(マップ上に表示されるピン)
//    		options.icon(desc);
    	 
    		// マーカーを貼り付け
    		mMap.addMarker(optionsGinzaBar);//
    	}
    }
    
    private void setThumbnailOverlay(String url) {
    	Log.i("KiokuMap", "Thumbnail url=" + url);
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
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.menu_kioku:
	    	moveTo(location);
	        return true;
	    case R.id.menu_ishinomaki_bar:
	    	moveTo(ishinomakiBar);
	        return true;
	    case R.id.menu_ginza_bar:
	    	moveTo(ginzaBar);
	        return true;
	    }
		return false;
	}

	private void moveTo(LatLng loc) {
        mMap.animateCamera(CameraUpdateFactory.
	           	newCameraPosition(CameraPosition.fromLatLngZoom(
	                loc, (float) 16.0)), 2000, null);
	}
}
