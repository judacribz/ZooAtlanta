package ca.judacribz.week2weekend.animals;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import ca.judacribz.week2weekend.R;
import ca.judacribz.week2weekend.models.Animal;
import ca.judacribz.week2weekend.models.Category;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.ViewHolder> {
    ArrayList<Animal> animals;

    AnimalAdapter(ArrayList<Animal> animals) {
        this.animals = animals;
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

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView
                tvAnimalName,
                tvDiet,
                tvStatus,
                tvRange;

        ImageView ivAnimalImage;
        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvAnimalName = itemView.findViewById(R.id.tvAnimalName);
                    tvDiet = itemView.findViewById(R.id.tvDiet);
                    tvStatus = itemView.findViewById(R.id.tvStatus);
                    tvRange =itemView.findViewById(R.id.tvRange);

             ivAnimalImage = itemView.findViewById(R.id.ivAnimalImage);

        }

        void setAnimalData(Animal animal) {
            tvAnimalName.setText(animal.getName());
            tvDiet.setText(animal.getDiet());
            tvStatus.setText(animal.getStatus());
            tvRange.setText(animal.getRange());

            new DownloadImageTask(ivAnimalImage).execute(animal.getImgUrl());
        }
    }
}
