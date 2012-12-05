/*
 * This code was derived from "Android Programming Nyumon 2nd edition".
 * http://www.amazon.co.jp/dp/4048860682/
 */

package org.ktaka.dojo.miraikioku;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.AsyncTask;
import android.widget.ImageView;

public class SetImageTask extends AsyncTask<Void, Void, Bitmap> {
	   // ここから
		public interface SetImageTaskListener {
			public void onSetImage(Bitmap bmp);
		}
	   // ここまで追加
		
	protected String mUrl;
    protected ImageView mImageView;
    protected SetImageTaskListener postExecTask = null;   // 追加
    public SetImageTask(String url, ImageView iv) {
        mUrl = url;
        mImageView = iv;
        mImageView.setTag(mUrl);
    }

    // ここから
    public SetImageTask(String url, SetImageTaskListener listener) {
        mUrl = url;
        postExecTask = listener;
    }
    // ここまで追加
    
    /* (non-Javadoc)
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected Bitmap doInBackground(Void... params) {
        String cacheName = mUrl;
        Bitmap bmp = ImageMap.getImage(cacheName);
        if (bmp == null) {
            try{
                URL url = new URL(mUrl);
                Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Config.RGB_565;
                bmp = BitmapFactory.decodeStream(url.openStream(), null, options);
            } catch (MalformedURLException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
            ImageMap.setImage(cacheName, bmp);
        }
        return bmp;
    }

    /* (non-Javadoc)
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(Bitmap bmp) {
        // ここから
     	if(postExecTask != null) {
     		postExecTask.onSetImage(bmp);   // 
     	} else
        // ここまで追加
     	if(mImageView != null && mImageView.getTag() != null && mImageView.getTag().equals(mUrl)) {
            mImageView.setImageBitmap(bmp);
        }
    }
}