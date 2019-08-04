package ca.judacribz.week2weekend.animals;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import ca.judacribz.week2weekend.R;
import ca.judacribz.week2weekend.models.Category;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.ViewHolder> {
    ArrayList<Category> animals;

    public AnimalAdapter(ArrayList<Category> animals) {
        this.animals = animals;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_category,
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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setAnimalData(Category animal) {
        }


        public Drawable urlImageToDrawable(String url, String srcName) {
            Drawable drawable = null;

            try {
                drawable = Drawable.createFromStream(
                        (InputStream) new URL(url).getContent(),
                        srcName
                );
            } catch (Exception e) {
                e.printStackTrace();
            }

            return drawable;
        }
    }
}
