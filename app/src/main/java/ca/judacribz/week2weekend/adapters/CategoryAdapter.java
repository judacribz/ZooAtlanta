package ca.judacribz.week2weekend.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ca.judacribz.week2weekend.R;
import ca.judacribz.week2weekend.models.Category;

public class CategoryAdapter extends ArrayAdapter<Category> {

    private Category category;

    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        super(context, 0, categories);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_category,
                    parent,
                    false
            );
        }

        if ((category = getItem(position)) != null) {
            setText(convertView, R.id.tvCategoryName, category.getName());
            setText(convertView, R.id.tvNumSpecies, String.valueOf(category.getNumSpecies()));
            setText(convertView, R.id.tvDescription, category.getDescription());
        }

        return convertView;
    }

    private void setText(View convertView, int tvId, String name) {
        ((TextView) convertView.findViewById(tvId)).setText(name);
    }
}
