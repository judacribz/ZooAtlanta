package ca.judacribz.zooatlanta.categories;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

import ca.judacribz.zooatlanta.models.Category;

public class CategoryTask extends AsyncTask<Void, Void, ArrayList<Category>> {

    CategoriesListener categoriesListener;

    public void setCategoriesListener(CategoriesListener categoriesListener) {
        this.categoriesListener = categoriesListener;
    }

    @Override
    protected ArrayList<Category> doInBackground(Void... voids) {
        ArrayList<Category> categories = new ArrayList<>();

        try {
            String name;
            int numSpecies;

            Document document = Jsoup.connect("https://zooatlanta.org/animals").get();

            for (Element el : document.getElementsByClass("popular-category")) {

                name = el.getElementsByTag("label").get(0).getElementsByTag("input").get(0).val();

                document = Jsoup.connect(
                        "https://zooatlanta.org/animals/?wpvtypeofanimal%5B%5D=" +
                                name
                ).get();
                numSpecies = document.getElementsByClass("animal card").size();

                document = Jsoup.connect(
                        "https://www.vocabulary.com/dictionary/" +
                                name
                ).get();

//                for (Element aniEl : document.getElementsByClass("animal card")) {
//                    Log.d("YOOOOOO", "doInBackground: " + aniEl
//                            .getElementsByClass("flipper").get(0)
//                            .getElementsByClass("back").get(0)
//                            .getElementsByClass("container").get(0)
//                            .text());
//                }
                categories.add(new Category(
                        name,
                        document.getElementsByClass("short").get(0).text(),
                        numSpecies
                ));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return categories;
    }

    @Override
    protected void onPostExecute(ArrayList<Category> categories) {
        if (!categories.isEmpty()) {
            categoriesListener.onCategoriesReceived(categories);
        }
    }

    public interface CategoriesListener {
        void onCategoriesReceived(ArrayList<Category> categories);
    }
}
