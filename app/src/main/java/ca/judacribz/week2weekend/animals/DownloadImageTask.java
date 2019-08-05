package ca.judacribz.week2weekend.animals;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.net.URL;

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView iv;

    public DownloadImageTask(ImageView iv) {
        this.iv = iv;
    }

    protected Bitmap doInBackground(String... urls) {
        Bitmap bmp = null;

        try {
            bmp = BitmapFactory.decodeStream(new URL(urls[0]).openStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bmp;
    }

    protected void onPostExecute(Bitmap bmp) {
        iv.setImageBitmap(bmp);
    }
}
