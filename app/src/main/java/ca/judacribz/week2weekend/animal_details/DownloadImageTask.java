package ca.judacribz.week2weekend.animal_details;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

import ca.judacribz.week2weekend.models.Animal;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

    private ImageDownloadedListener imageDownloadedListener;

    interface ImageDownloadedListener {
        void onImageDownloaded(Bitmap bmp);
    }

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
}
