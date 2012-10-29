package org.ktaka.dojo.miraikioku;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	private static final String miraiKiokuUrl = "http://www.miraikioku.com/api/search/kioku";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private void getData() {
    	//android.net.Uri.Builder を使うべきか
    	String apiUrl = miraiKiokuUrl + "?" + "event-date=" + "20080805";
    	new AccessAPItask().execute(apiUrl);
    }
    
    private class AccessAPItask extends AsyncTask<String, Void, Void> {
    	private DefaultHttpClient httpClient;
    	
    	public AccessAPItask() {
    		httpClient = new DefaultHttpClient();
    	}

		@Override
		protected Void doInBackground(String... args) {
			// TODO Auto-generated method stub
			execAPI(args[0]);
			return null;
		}
		
		private void execAPI(String url) {
			try {
		    	Log.d("MiraiKiokuAPIsample", "execAPI=" + url);
				HttpGet request = new HttpGet(url);
				HttpResponse response = executeRequest(request);
				int statusCode = response.getStatusLine().getStatusCode();
				StringBuilder buf = new StringBuilder();
				InputStream in = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String l = null;
				while((l = reader.readLine()) != null) {
					buf.append(l);
					Log.d("MiraiKiokuAPISample", l);
				}
				if(statusCode == 200) {
					//
				}
			} catch(IOException e) {
				
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
}
