package ca.judacribz.week2weekend.homepage.model.remote.asynctask

import android.os.AsyncTask
import org.jsoup.Jsoup
import org.jsoup.nodes.TextNode
import java.io.IOException

class ScheduleTask(private val listener: Listener) :
    AsyncTask<Void, Void, MutableList<TextNode>>() {

    interface Listener {
        fun onScheduleReceived(schedule: List<TextNode?>?)
    }

    override fun doInBackground(vararg voids: Void?): MutableList<TextNode>? {
        try {
            val document = Jsoup.connect("https://zooatlanta.org/").get()
            return document
                .getElementById("today")
                .getElementById("hours-today")
                .textNodes()
                .subList(0, 2)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    override fun onPostExecute(schedule: MutableList<TextNode>?) =
        this.listener.onScheduleReceived(schedule)
}