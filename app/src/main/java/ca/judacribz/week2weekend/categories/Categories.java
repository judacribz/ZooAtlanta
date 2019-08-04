package ca.judacribz.week2weekend.categories;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ca.judacribz.week2weekend.R;
import ca.judacribz.week2weekend.models.Category;

public class Categories extends AppCompatActivity implements AnimalCategoryTask.CategoriesListener{

    ListView lvCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        lvCategories = findViewById(R.id.lvCategories);

        AnimalCategoryTask animalCategoryTask = new AnimalCategoryTask();
        animalCategoryTask.setCategoriesListener(this);
        animalCategoryTask.execute();
    }

    @Override
    public void onCategoriesReceived(ArrayList<Category> categories) {
        Toast.makeText(this, "" + categories.size(), Toast.LENGTH_SHORT).show();
        lvCategories.setAdapter(new CategoryAdapter(this, categories));
    }
}
