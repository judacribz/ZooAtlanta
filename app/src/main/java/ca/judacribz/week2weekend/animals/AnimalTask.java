package ca.judacribz.week2weekend.animals;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

import ca.judacribz.week2weekend.models.Animal;

public class AnimalTask extends AsyncTask<Void, Void, ArrayList<Animal>> {

    AnimalsListener animalsListener;

    public interface AnimalsListener {
        void onAnimalsReceived(ArrayList<Animal> animals);
    }

    public void setAnimalsListener(AnimalsListener animalsListener) {
        this.animalsListener = animalsListener;
    }

    @Override
    protected ArrayList<Animal> doInBackground(Void... voids) {
        ArrayList<Animal> animals = new ArrayList<>();
//
//        try {
//
//            Document document = Jsoup.connect(
//                    "https://zooatlanta.org/animals/?wpvtypeofanimal%5B%5D=" +
//                            name
//            ).get();
//
//
//                for (Element aniEl : document.getElementsByClass("animal card")) {
//                    Log.d("YOOOOOO", "doInBackground: " + aniEl
//                            .getElementsByClass("flipper").get(0)
//                            .getElementsByClass("back").get(0)
//                            .getElementsByClass("container").get(0)
//                            .text());
//                }
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return animals;
    }

    @Override
    protected void onPostExecute(ArrayList<Animal> categories) {
        if (!categories.isEmpty()) {
            animalsListener.onAnimalsReceived(categories);
        }
    }
}
