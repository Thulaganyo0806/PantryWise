package com.example.pantrywise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RecipeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Recipe> recipes;

    public RecipeAdapter(
            Context context,
            ArrayList<Recipe> recipes
    ) {
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public Object getItem(int position) {
        return recipes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return recipes.get(position).getId();
    }

    @Override
    public View getView(
            int position,
            View convertView,
            ViewGroup parent
    ) {

        if (convertView == null) {

            convertView =
                    LayoutInflater.from(context).inflate(
                            R.layout.item_recipe,
                            parent,
                            false
                    );
        }

        TextView recipeName =
                convertView.findViewById(
                        R.id.recipeName
                );

        TextView recipeDescription =
                convertView.findViewById(
                        R.id.recipeDescription
                );

        Recipe recipe =
                recipes.get(position);

        recipeName.setText(
                recipe.getName()
        );

        recipeDescription.setText(
                "You have the ingredients needed"
        );

        return convertView;
    }
}