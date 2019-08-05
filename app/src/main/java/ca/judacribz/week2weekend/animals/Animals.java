package ca.judacribz.week2weekend.animals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

import ca.judacribz.week2weekend.R;
import ca.judacribz.week2weekend.models.Animal;
import ca.judacribz.week2weekend.models.Category;

import static ca.judacribz.week2weekend.categories.Categories.EXTRA_CATEGORY_NAME;

public class Animals extends AppCompatActivity implements AnimalTask.AnimalsListener {

    RecyclerView rvAnimals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals);
        rvAnimals = findViewById(R.id.rvAnimals);
        rvAnimals.setLayoutManager(new LinearLayoutManager(this));
        new AnimalTask(this).execute(getIntent().getStringExtra(EXTRA_CATEGORY_NAME));
    }

    @Override
    public void onAnimalsReceived(ArrayList<Animal> animals) {
        AnimalAdapter animalAdapter = new AnimalAdapter(this, animals);
        rvAnimals.setAdapter(animalAdapter);
    }
}
