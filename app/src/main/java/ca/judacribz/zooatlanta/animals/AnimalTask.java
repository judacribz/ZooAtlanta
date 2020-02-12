package ca.judacribz.zooatlanta.animals;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

import ca.judacribz.zooatlanta.models.Animal;

import static ca.judacribz.zooatlanta.animals.Animals.ALL_ANIMALS;

public class AnimalTask extends AsyncTask<String, Void, ArrayList<Animal>> {

    private static final String
            DIET = "Diet ",
            STATUS = " Status In The Wild ",
            RANGE = " Range ",
            READ_MORE = " Read More";

    private AnimalsListener animalsListener;

    AnimalTask(AnimalsListener animalsListener) {
        this.animalsListener = animalsListener;
    }

    @Override
    protected ArrayList<Animal> doInBackground(String... categoryName) {

        ArrayList<Animal> animals = new ArrayList<>();

        try {
            Document document = Jsoup.connect(
                    (ALL_ANIMALS.equals(categoryName[0])) ?
                            "https://zooatlanta.org/animals/" :
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

                diet = backCard.substring(
                        backCard.indexOf(DIET) + DIET.length(),
                        backCard.indexOf(STATUS)
                );

                int beg = backCard.indexOf(STATUS) + STATUS.length();
                int end = backCard.indexOf(RANGE);

                status = (beg >= end) ? "" : backCard.substring(beg, end
                );

                beg = backCard.indexOf(RANGE) + RANGE.length();
                end = backCard.indexOf(READ_MORE);

                range = (beg >= end) ? "" : backCard.substring(beg, end);

                animals.add(new Animal(name, scientificName, diet, status, range, url));
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

    public interface AnimalsListener {
        void onAnimalsReceived(ArrayList<Animal> animals);
    }
}
