package ca.judacribz.zooatlanta.animal_details;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private ImageDownloadedListener imageDownloadedListener;

    public DownloadImageTask(ImageDownloadedListener imageDownloadedListener) {
        this.imageDownloadedListener = imageDownloadedListener;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new URL(urls[0]).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bmp) {
        imageDownloadedListener.onImageDownloaded(bmp);
    }

    interface ImageDownloadedListener {
        void onImageDownloaded(Bitmap bmp);
    }
}
