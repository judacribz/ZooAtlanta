package ca.judacribz.zooatlanta.animal_details;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import ca.judacribz.zooatlanta.R;
import ca.judacribz.zooatlanta.models.Animal;

import static ca.judacribz.zooatlanta.animals.AnimalAdapter.ViewHolder.EXTRA_ANIMAL;
import static ca.judacribz.zooatlanta.animals.Animals.KEY_CATEGORY;

public class AnimalDetails extends AppCompatActivity implements
        AnimalDetailsTask.AnimalDetailsListener,
        DownloadImageTask.ImageDownloadedListener {

    private static final String BIRDS = "birds";

    private TextView
            tvAnimalName,
            tvScientificName,
            tvDiet,
            tvStatus,
            tvRange,
            tvHabitat,
            tvViewingHints,
            tvDescription;
    private ImageView ivAnimalImage;
    private Animal animal;

    private MediaPlayer animalSound = null;

    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_details);
        Objects.requireNonNull(getSupportActionBar()).hide();

        tvAnimalName = findViewById(R.id.tvAnimalName);
        tvScientificName = findViewById(R.id.tvScientificName);
        tvDiet = findViewById(R.id.tvDiet);
        tvStatus = findViewById(R.id.tvStatus);
        tvRange = findViewById(R.id.tvRange);
        tvHabitat = findViewById(R.id.tvHabitat);
        tvViewingHints = findViewById(R.id.tvViewingHints);
        tvDescription = findViewById(R.id.tvHomepageAnimalDescription);

        ivAnimalImage = findViewById(R.id.ivHomepageAnimalImage);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            setAnimalData(animal = bundle.getParcelable(EXTRA_ANIMAL));
            new DownloadImageTask(this).execute(animal.getImgUrl());
            new AnimalDetailsTask(this).execute(animal);
        }

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.category_file), Context.MODE_PRIVATE);
        category = sharedPref.getString(KEY_CATEGORY, "");
    }

    void setAnimalData(Animal animal) {
        tvAnimalName.setText(animal.getName());
        tvScientificName.setText(animal.getScientificName());
        tvDiet.setText(animal.getDiet());
        tvStatus.setText(animal.getStatus());
        tvRange.setText(animal.getRange());
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseSound();
    }

    public void makeAnimalSound(View view) {
        releaseSound();

        String name = animal.getName().toLowerCase();
        animalSound = MediaPlayer.create(this, R.raw.king);

        if (BIRDS.equals(category)) {
            animalSound = MediaPlayer.create(this, R.raw.bird);
        } else if (name.contains("lion")) {
            animalSound = MediaPlayer.create(this, R.raw.lion);
        } else if (name.contains("elephant")) {
            animalSound = MediaPlayer.create(this, R.raw.elephant);
        }

        animalSound.start();
    }

    private void releaseSound() {
        if (animalSound != null) {
            animalSound.stop();
            animalSound.release();
        }
    }

    @Override
    public void onImageDownloaded(Bitmap bmp) {
        ivAnimalImage.setImageBitmap(bmp);
    }

    @Override
    public void onAnimalDetailsReceived(Animal animal) {
        tvDescription.setText(animal.getDescription());
        tvHabitat.setText(animal.getHabitat());
        tvViewingHints.setText(animal.getViewingHints());
    }
}
