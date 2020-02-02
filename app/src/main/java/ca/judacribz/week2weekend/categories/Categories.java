package ca.judacribz.week2weekend.categories;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

import ca.judacribz.week2weekend.R;
import ca.judacribz.week2weekend.animals.Animals;
import ca.judacribz.week2weekend.models.Category;

public class Categories extends AppCompatActivity implements
        CategoryTask.CategoriesListener,
        AdapterView.OnItemClickListener {

    public static final String EXTRA_CATEGORY_NAME =
            "ca.judacribz.week2weekend.categories.EXTRA_CATEGORY_NAME";
    ListView lvCategories;
    CategoryAdapter categoryAdapter;
    ArrayList<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.animal_classifications);

        lvCategories = findViewById(R.id.lvCategories);
        lvCategories.setOnItemClickListener(this);

        CategoryTask categoryTask = new CategoryTask();
        categoryTask.setCategoriesListener(this);
        categoryTask.execute();
    }

    @Override
    public void onCategoriesReceived(ArrayList<Category> categories) {
        categoryAdapter = new CategoryAdapter(this, this.categories = categories);
        lvCategories.setAdapter(categoryAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, Animals.class);
        intent.putExtra(EXTRA_CATEGORY_NAME, categories.get(position).getName());
        startActivity(intent);
    }
}
