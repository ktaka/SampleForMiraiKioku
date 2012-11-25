package org.ktaka.dojo.miraikioku;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class KiokuView extends Activity implements OnClickListener {
	String location;
	String thumbUrl;
	ImageView imgView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kioku_view);
        Intent intent = getIntent();
        location = intent.getStringExtra("location");
        thumbUrl = intent.getStringExtra("ThumbUrl");
        String imgUrl = intent.getStringExtra("ImageUrl");
        imgView = (ImageView)findViewById(R.id.imageView1);
        Bitmap b = ImageMap.getImage(imgUrl);
        if(b != null) {
            imgView.setImageBitmap(b);
        } else {
            imgView.setImageDrawable(null);
            new SetImageTask(imgUrl, imgView).execute((Void)null);
        }
        
        Button mapButton = (Button)findViewById(R.id.button1);
        mapButton.setOnClickListener(this);
    }

	public void onClick(View v) {
		Intent intent = new Intent(this, KiokuMap.class);
		intent.putExtra("location", location);
		intent.putExtra("ThumbUrl", thumbUrl);
		startActivity(intent);
	}
    	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_kioku_view, menu);
        return true;
    }
}
