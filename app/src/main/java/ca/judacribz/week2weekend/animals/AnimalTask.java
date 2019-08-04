package ca.judacribz.week2weekend.animals;

import android.os.AsyncTask;

import java.util.ArrayList;

import ca.judacribz.week2weekend.models.Category;

public class AnimalTask extends AsyncTask<Void, Void, ArrayList<Category>> {

    AnimalsListener animalsListener;

    public interface AnimalsListener {
        void onAnimalsReceived(ArrayList<Category> animals);
    }

    public void setAnimalsListener(AnimalsListener animalsListener) {
        this.animalsListener = animalsListener;
    }

    @Override
    protected ArrayList<Category> doInBackground(Void... voids) {
        ArrayList<Category> animals = new ArrayList<>();
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
    protected void onPostExecute(ArrayList<Category> categories) {
        if (!categories.isEmpty()) {
            animalsListener.onAnimalsReceived(categories);
        }
    }
}
