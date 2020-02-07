package ca.judacribz.zooatlanta.animals;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import ca.judacribz.zooatlanta.R;
import ca.judacribz.zooatlanta.models.Animal;

import static ca.judacribz.zooatlanta.categories.Categories.EXTRA_CATEGORY_NAME;

public class Animals extends AppCompatActivity implements AnimalTask.AnimalsListener {

    public static final String KEY_CATEGORY = "ca.judacribz.week2weekend.animals.KEY_CATEGORY";
    public static final String ALL_ANIMALS = "All Animals";
    RecyclerView rvAnimals;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals);


        category = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);
        if (category == null) {
            category = ALL_ANIMALS;
        }

        new AnimalTask(this).execute(category);
        Objects.requireNonNull(getSupportActionBar()).setTitle(category.toUpperCase());

        rvAnimals = findViewById(R.id.rvAnimals);
        rvAnimals.setLayoutManager(new LinearLayoutManager(this));


        SharedPreferences.Editor editor = getSharedPreferences(
                getResources().getString(R.string.category_file),
                Context.MODE_PRIVATE
        ).edit();
        editor.putString(KEY_CATEGORY, category);
        editor.apply();
    }

    @Override
    public void onAnimalsReceived(ArrayList<Animal> animals) {
        AnimalAdapter animalAdapter = new AnimalAdapter(animals);
        rvAnimals.setAdapter(animalAdapter);
    }
}
