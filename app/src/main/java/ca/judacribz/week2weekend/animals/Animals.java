package ca.judacribz.week2weekend.animals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

import ca.judacribz.week2weekend.R;
import ca.judacribz.week2weekend.models.Category;

import static ca.judacribz.week2weekend.categories.Categories.EXTRA_CATEGORY_NAME;

public class Animals extends AppCompatActivity implements AnimalTask.AnimalsListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals);
        Toast.makeText(this, "" + getIntent().getStringExtra(EXTRA_CATEGORY_NAME), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAnimalsReceived(ArrayList<Category> animals) {

    }
}
