package org.ktaka.dojo.miraikioku;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class KiokuMap extends MapActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kioku_map);
        Intent intent = getIntent();
        String location[] = intent.getStringExtra("location").split(",");
        float lat = Float.parseFloat(location[0]);
        float lon = Float.parseFloat(location[1]);
        GeoPoint point = new GeoPoint( (int)(lat * 1e6), (int)(lon * 1e6));
        MapView mapView = (MapView) findViewById(R.id.mapview);
        MapController mapCtrl = mapView.getController();
        mapCtrl.setCenter(point);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_kioku_map, menu);
        return true;
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
