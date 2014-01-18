package org.ktaka.dojo.miraikioku;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

public class KiokuView extends Activity {
	String location;     // この行を追加
	String thumbUrl;     // 追加

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kioku_view);
        Intent intent = getIntent();
        location = intent.getStringExtra("location");     // この行を追加        
        thumbUrl = intent.getStringExtra("ThumbUrl");      // 追加
        location = intent.getStringExtra("location");     // この行を追加
        String imgUrl = intent.getStringExtra("ImageUrl");
        ImageView imgView = (ImageView)findViewById(R.id.imageView1);
        Bitmap b = ImageMap.getImage(imgUrl);
        if(b != null) {
            imgView.setImageBitmap(b);
        } else {
            imgView.setImageDrawable(null);
            new SetImageTask(imgUrl, imgView).execute((Void)null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_kioku_view, menu);
        return true;
    }

	public void showMapView(View v) {
		Intent intent = new Intent(this, KiokuMap.class);
		intent.putExtra("location", location);
		intent.putExtra("ThumbUrl", thumbUrl);     // 追加
		intent.putExtra("location", location);        // この行を追加
		startActivity(intent);
	}
}
