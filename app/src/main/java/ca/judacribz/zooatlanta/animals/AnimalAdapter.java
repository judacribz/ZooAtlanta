package ca.judacribz.zooatlanta.animals;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ca.judacribz.zooatlanta.R;
import ca.judacribz.zooatlanta.models.Animal;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.ViewHolder> {
    private ArrayList<Animal> animals;
    private Map<String, Bitmap> animalBmps;

    AnimalAdapter(ArrayList<Animal> animals) {
        this.animals = animals;
        animalBmps = new HashMap<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_animal,
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setAnimalData(animals.get(position));
    }

    @Override
    public int getItemCount() {
        return animals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public static final String EXTRA_ANIMAL =
                "ca.judacribz.week2weekend.animals.EXTRA_ANIMAL";

        TextView
                tvAnimalName,
                tvScientificName,
                tvDiet,
                tvStatus,
                tvRange;

        ImageView ivAnimalImage;

        Animal animal;
        Bitmap bmp;
        String aniName;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);

            tvAnimalName = itemView.findViewById(R.id.tvAnimalName);
            tvScientificName = itemView.findViewById(R.id.tvScientificName);
            tvDiet = itemView.findViewById(R.id.tvDiet);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvRange = itemView.findViewById(R.id.tvRange);

            ivAnimalImage = itemView.findViewById(R.id.ivAnimalImage);

            itemView.setOnClickListener(v -> {
                android.content.Intent intent = new android.content.Intent(v.getContext(), ca.judacribz.zooatlanta.animal_details.AnimalDetails.class);
                android.os.Bundle bundle = new android.os.Bundle();

                bundle.putParcelable(EXTRA_ANIMAL, animal);
                intent.putExtras(bundle);

                v.getContext().startActivity(intent);
            });
        }


        void setAnimalData(final Animal animal) {
            this.animal = animal;
            tvAnimalName.setText(aniName = animal.getName());
            tvScientificName.setText(animal.getScientificName());
            tvDiet.setText(animal.getDiet());
            tvStatus.setText(animal.getStatus());
            tvRange.setText(animal.getRange());

            if (animalBmps.containsKey(aniName)) {
                setAnimalImage(animalBmps.get(aniName));
            } else {
                setAnimalImage(animal.getImgUrl());
            }
        }

        void setAnimalImage(final String url) {
            new Thread(() -> {
                try {
                    animalBmps.put(
                            aniName,
                            bmp = android.graphics.BitmapFactory.decodeStream(new java.net.URL(url).openStream())
                    );
                    setAnimalImage(bmp);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        void setAnimalImage(Bitmap bmp) {
            ivAnimalImage.setImageBitmap(bmp);
        }
    }
}
