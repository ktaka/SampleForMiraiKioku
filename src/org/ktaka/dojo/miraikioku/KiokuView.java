package org.ktaka.dojo.miraikioku;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class KiokuView extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kioku_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_kioku_view, menu);
        return true;
    }
}
