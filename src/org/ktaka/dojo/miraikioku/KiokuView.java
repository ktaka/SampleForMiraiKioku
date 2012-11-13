package org.ktaka.dojo.miraikioku;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.widget.ImageView;

public class KiokuView extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kioku_view);
        Intent intent = getIntent();
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
}
