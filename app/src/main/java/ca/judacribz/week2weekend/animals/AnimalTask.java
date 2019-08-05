package ca.judacribz.week2weekend.animals;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.judacribz.week2weekend.models.Animal;
import ca.judacribz.week2weekend.models.Category;

public class AnimalTask extends AsyncTask<String, Void, ArrayList<Animal>> {

    private AnimalsListener animalsListener;
    private String categoryName;

    public interface AnimalsListener {
        void onAnimalsReceived(ArrayList<Animal> animals);
    }

    public AnimalTask(AnimalsListener animalsListener) {
        this.animalsListener = animalsListener;
    }

    public static final String DIET = "Diet ";
    public static final String STATUS = " Status In The Wild ";
    public static final String RANGE = " Range ";
    public static final String READ_MORE = " Read More";

    @Override
    protected ArrayList<Animal> doInBackground(String... categoryName) {
        ArrayList<Animal> animals = new ArrayList<>();

        try {

            Document document = Jsoup.connect(
                    "https://zooatlanta.org/animals/?wpvtypeofanimal%5B%5D=" +
                            categoryName[0]
            ).get();

            String styleStr;
            String url;
            String backCard;
            String name, scientificName;
            String diet, status, range;
            for (Element aniEl : document.getElementsByClass("animal card")) {
                styleStr = aniEl.getElementsByClass("featured-image").get(0).attr("style");
                url = styleStr.substring(styleStr.indexOf('(') + 1, styleStr.indexOf(')'));
                name = aniEl.getElementsByTag("h3").get(0).text();
                scientificName = aniEl.getElementsByTag("em").get(0).text();


                backCard = aniEl
                        .getElementsByClass("flipper").get(0)
                        .getElementsByClass("back").get(0)
                        .getElementsByClass("container").get(0)
                        .text();
                diet = backCard.substring(backCard.indexOf(DIET) + DIET.length(), backCard.indexOf(STATUS));
                status = backCard.substring(backCard.indexOf(STATUS) + STATUS.length(), backCard.indexOf(RANGE));
                range = backCard.substring(backCard.indexOf(RANGE) + RANGE.length(), backCard.indexOf(READ_MORE));

                animals.add(new Animal(name, scientificName, diet, status, range, url));

                Log.d("YOOOOOO", "doInBackground: " + url);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return animals;
    }

    @Override
    protected void onPostExecute(ArrayList<Animal> animals) {
        if (!animals.isEmpty()) {
            animalsListener.onAnimalsReceived(animals);
        }
    }
}
