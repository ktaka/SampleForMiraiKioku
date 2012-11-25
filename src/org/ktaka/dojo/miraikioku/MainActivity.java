/*
 * 未来へのキオク API アクセスのサンプルコード for 東北 TECH 道場
 * 
 * This code was inspired from "Android Programming Nyumon 2nd edition".
 * http://www.amazon.co.jp/dp/4048860682/
 */

package org.ktaka.dojo.miraikioku;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	private List<KiokuItem> kiokuList;
	private KiokuArrayAdapter adapter;
	// キオク検索 API
	private static final String miraiKiokuUrl = "http://www.miraikioku.com/api/search/kioku";
	private ProgressDialog progressDialog;
	
	LocationManager locationManager;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kiokuList = new ArrayList<KiokuItem>();
        adapter = new KiokuArrayAdapter(getApplicationContext(), 0, kiokuList);
        getListView().setAdapter(adapter);

        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	setupLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(listener);
    }

    private void setupLocation() {
      progressDialog = new ProgressDialog(this);
      progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      progressDialog.setMessage("Getting location...");
      progressDialog.setCancelable(true);
      progressDialog.show();

    	Location gpsLocation = null;
        locationManager.removeUpdates(listener);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, listener);
            gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else {
            Toast.makeText(this, "failed to get location from GPS", Toast.LENGTH_LONG).show();
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, listener);
            gpsLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } else {
            Toast.makeText(this, "failed to get location from NETWORK", Toast.LENGTH_LONG).show();
        }
    	
    }
    
    private final LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            // A new location update is received.  Do something useful with it.  Update the UI with
            // the location update.
            updateLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    
    private void updateLocation(Location location) {
    	Log.i("MiraiKiokuAPILocaion", "location=" + location.toString());
        (new ReverseGeocodingTask(this)).execute(new Location[] {location});
		progressDialog.dismiss();
        getData(location);
    }
    
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	Intent intent = new Intent(this, KiokuView.class);
    	KiokuItem item = (KiokuItem)l.getItemAtPosition(position);
    	intent.putExtra("ImageUrl", item.imageUrl);
    	intent.putExtra("ThumbUrl", item.thumbUrl);
    	intent.putExtra("location", item.location);
    	startActivity(intent);
    	int i = 0;
    	String s;
    	String.valueOf(i);
    }
    
    private void getData(Location location) {
    	// API アクセスのための url を文字列として組み立てます。
    	// ここでは type と event-date のパラメータを指定しています。
    	// http://www.miraikioku.com/docs/api/search_kioku を参照して
    	// いろいろなパラメータを設定して試してみて下さい。
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Getting data from server...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    	String apiUrl = miraiKiokuUrl + "?" + "type=photo" + "&" + "thumb-size=100c" + "&" + "location-radius=40" + "&" +
    			"location=" + String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
    	new AccessAPItask().execute(apiUrl);
    }
    
    private class KiokuItem {
    	String title;
    	String thumbUrl;
    	String imageUrl;
    	String location;
    }
    
    private class KiokuArrayAdapter extends ArrayAdapter<KiokuItem> {
    	private LayoutInflater inflater;
    	
		public KiokuArrayAdapter(Context context, int textViewResourceId,
				List<KiokuItem> objects) {
			super(context, textViewResourceId, objects);
			inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		private class ViewHolder {
			TextView title;
			ImageView thumbnail;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			if(convertView == null) {
				convertView = inflater.inflate(R.layout.row, null, false);
				holder = new ViewHolder();
				holder.title = (TextView)convertView.findViewById(R.id.title);
				holder.thumbnail = (ImageView)convertView.findViewById(R.id.thumbnail);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}
			KiokuItem item = getItem(position);
			holder.title.setText(item.title);
			String thumbUrl = item.thumbUrl;
			holder.thumbnail.setTag(thumbUrl);
			Bitmap b = ImageMap.getImage(thumbUrl);
			if(b != null) {
				holder.thumbnail.setImageBitmap(b);
			} else {
				holder.thumbnail.setImageDrawable(null);
				new SetImageTask(thumbUrl, holder.thumbnail).execute((Void)null);
			}
			return convertView;
		}
    }
    
    private class AccessAPItask extends AsyncTask<String, Void, JSONObject> {
    	private DefaultHttpClient httpClient;
    	
    	public AccessAPItask() {
    		httpClient = new DefaultHttpClient();
    	}

		@Override
		protected JSONObject doInBackground(String... args) {
			execAPI(args[0]);
			return null;
		}
		
		@Override
		protected void onPostExecute(JSONObject result) {
			progressDialog.dismiss();
			adapter.notifyDataSetChanged();
		}
		
		private void execAPI(String url) {
			try {
		    	Log.d("MiraiKiokuAPIsample", "execAPI=" + url);
		    	// 文字列として組み立てた url で http の GET リクエストをサーバーに送ります。
		    	// これが「API を呼び出す」ことになります。
				HttpGet request = new HttpGet(url);
				HttpResponse response = executeRequest(request);
				
				// サーバーからのステータスを取得します。
				int statusCode = response.getStatusLine().getStatusCode();
				StringBuilder buf = new StringBuilder();
				InputStream in = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String l = null;
				
				// サーバーからのレスポンスを行単位に読み込みます。
				while((l = reader.readLine()) != null) {
					buf.append(l);
//					Log.d("MiraiKiokuAPISample", l);
				}
				if(statusCode == 200) {
					// ステータスが成功ならレスポンスのパース（解析）を行います。
					parseResponse(buf.toString());
				}
			} catch(IOException e) {
				Log.e("MiraiKiokuAPISample", "IO error", e);
			} catch(JSONException e) {
				Log.e("MiraiKiokuAPISample", "JSON error", e);
			}
		}
		
		private void parseResponse(String buf) throws JSONException {
			// レスポンスは JSON フォーマットとしてパースします。
			JSONObject rootObj = new JSONObject(buf);
			// アイテムの件数を取得
			int count = rootObj.getInt("count");
			Log.d("MiraiKiokuAPISample", String.valueOf(count));
			
			// アイテムを配列として取得
			JSONArray results = rootObj.getJSONArray("results");
			count = results.length();
			kiokuList.clear();
			for(int i = 0; i < count; i++) {
				JSONObject item = results.getJSONObject(i);
				Log.d("MiraiKiokuAPISample", item.getString("title"));
				Log.d("MiraiKiokuAPISample", item.getString("url"));
				Log.d("MiraiKiokuAPISample", item.getString("thumb-url"));
				
				// ListView に表示するためのアイテムとして登録します。
				KiokuItem kioku = new KiokuItem();
				kioku.title = item.getString("title");
				kioku.thumbUrl = item.getString("thumb-url");
				kioku.imageUrl = item.getString("image-url");
				kioku.location = item.getString("location");
				kiokuList.add(kioku);
			}
		}
		
		private HttpResponse executeRequest(HttpRequestBase base) throws IOException {
			try {
				return httpClient.execute(base);
			} catch(IOException e) {
				base.abort();
				throw e;
			}
		}
    }
    
    //
    private class ReverseGeocodingTask extends AsyncTask<Location, Void, String> {
        Context mContext;
        String addressText = "no location";

        public ReverseGeocodingTask(Context context) {
            super();
            mContext = context;
        }

        @Override
        protected String doInBackground(Location... params) {
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

            Location loc = params[0];
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                // Format the first line of address (if available), city, and country name.
                addressText = String.format("%s, %s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getLocality(),
                        address.getCountryName());
            }
            return addressText;
        }
    	@Override
    	protected void onPostExecute(String result) {
    		Log.d("MiraiKiokuAPI", "Reverse Geo=" + result);
    		updateGeoString(result);
    	}
    }
    
    private void updateGeoString(String geo) {
		TextView loc = (TextView)findViewById(R.id.textView1);
		loc.setText(geo);
    }

}
