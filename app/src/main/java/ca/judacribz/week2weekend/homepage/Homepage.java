package ca.judacribz.week2weekend.homepage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.nodes.TextNode;

import java.util.List;
import java.util.Objects;

import ca.judacribz.week2weekend.R;

public class Homepage extends AppCompatActivity implements ScheduleTask.ScheduleListener {

    public static final int DURATION_IMAGE_CHANGE = 9000;
    private static final int[] IMG_ID_ANIMALS = new int[]{
            R.drawable.elephant,
            R.drawable.gorilla,
            R.drawable.panda,
            R.drawable.zebra
    };
    private static final int[] STR_ID_HEAD_ANIMALS = new int[]{
            R.string.elephant,
            R.string.gorilla,
            R.string.panda,
            R.string.zebra
    };
    private static final int[] STR_ID_DESC_ANIMALS = new int[]{
            R.string.elephant_desc,
            R.string.gorilla_desc,
            R.string.panda_desc,
            R.string.zebra_desc
    };

    private static final int[] BTN_IDS = new int[]{
            R.id.btnCategories,
            R.id.btnTickets,
            R.id.btnAnimals
    };

    private ImageView ivAnimalImages;
    private TextView
            tvAnimalHeadline,
            tvAnimalDescription,
            tvSchedule,
            tvLastAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Objects.requireNonNull(getSupportActionBar()).hide();

        ScheduleTask scheduleScrape = new ScheduleTask();
        scheduleScrape.setScheduleListener(this);
        scheduleScrape.execute();

        setButtonWidthEqually(getResources().getDisplayMetrics().widthPixels / 3);

        ivAnimalImages = findViewById(R.id.ivAnimalImages);
        tvAnimalHeadline = findViewById(R.id.tvAnimalHeadline);
        tvAnimalDescription = findViewById(R.id.tvAnimalDescription);
        tvSchedule = findViewById(R.id.tvSchedule);
        tvLastAdmin = findViewById(R.id.tvLastAdmin);
    }

    private void setButtonWidthEqually(int btnWidth) {
        for (int btnId : BTN_IDS) {
            findViewById(btnId).getLayoutParams().width = btnWidth;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ivAnimalImages.post(
                new Runnable() {
                    int i = 0;
                    int numImages = IMG_ID_ANIMALS.length;

                    public void run() {
                        ivAnimalImages.setImageResource(IMG_ID_ANIMALS[i]);
                        tvAnimalHeadline.setText(getResources().getString(STR_ID_HEAD_ANIMALS[i]));
                        tvAnimalDescription.setText(getResources().getString(STR_ID_DESC_ANIMALS[i]));
                        i = (i + 1) % numImages;
                        ivAnimalImages.postDelayed(this, DURATION_IMAGE_CHANGE);
                    }
                }
        );
    }

    @Override
    public void onScheduleReceived(List<TextNode> schedule) {
        tvSchedule.setText(schedule.get(0).toString().trim());
        tvLastAdmin.setText(schedule.get(1).toString());
    }
}
