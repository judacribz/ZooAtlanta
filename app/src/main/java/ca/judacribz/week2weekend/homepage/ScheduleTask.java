package ca.judacribz.week2weekend.homepage;

import android.os.AsyncTask;

import androidx.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;

import java.io.IOException;
import java.util.List;

public class ScheduleTask extends AsyncTask<Void, Void, List<TextNode>> {

    ScheduleListener scheduleListener;

    public interface ScheduleListener {
        void onScheduleReceived(List<TextNode> schedule);
    }

    public void setScheduleListener(ScheduleListener scheduleListener) {
        this.scheduleListener = scheduleListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<TextNode> doInBackground(Void... voids) {
        try {
            Document document = Jsoup.connect("https://zooatlanta.org/").get();

            return document
                    .getElementById("today")
                    .getElementById("hours-today")
                    .textNodes()
                    .subList(0, 2);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(@Nullable List schedule) {
        if (schedule != null) {
            scheduleListener.onScheduleReceived(schedule);
        }
    }
}
