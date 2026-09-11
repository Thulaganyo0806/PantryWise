package com.example.pantrywise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private LinearLayout recipeContainer;
    private TextView emptyMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        databaseHelper = new DatabaseHelper(this);

        recipeContainer = findViewById(R.id.recipeContainer);
        emptyMessage = findViewById(R.id.emptyMessage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showSuggestedRecipes();
    }

    private void showSuggestedRecipes() {

        recipeContainer.removeAllViews();

        ArrayList<Ingredient> pantryIngredients =
                databaseHelper.getAllIngredients();

        ArrayList<Recipe> recipes =
                databaseHelper.getAllRecipes();

        HashSet<String> pantryNames = new HashSet<>();

        for (Ingredient ingredient : pantryIngredients) {

            String name = ingredient.getName()
                    .trim()
                    .toLowerCase(Locale.ROOT);

            pantryNames.add(name);
        }

        boolean foundRecipe = false;

        for (Recipe recipe : recipes) {

            ArrayList<RecipeIngredient> requiredIngredients =
                    databaseHelper.getRecipeIngredients(
                            (int) recipe.getId()
                    );

            boolean canMakeRecipe = true;

            for (RecipeIngredient required : requiredIngredients) {

                String requiredName = required.getName()
                        .trim()
                        .toLowerCase(Locale.ROOT);

                if (!pantryNames.contains(requiredName)) {

                    canMakeRecipe = false;
                    break;
                }
            }

            if (canMakeRecipe) {

                foundRecipe = true;

                View recipeCard =
                        getLayoutInflater().inflate(
                                R.layout.item_recipe,
                                recipeContainer,
                                false
                        );

                TextView recipeName =
                        recipeCard.findViewById(
                                R.id.recipeName
                        );

                TextView recipeDescription =
                        recipeCard.findViewById(
                                R.id.recipeDescription
                        );

                View viewRecipeButton =
                        recipeCard.findViewById(
                                R.id.viewRecipeButton
                        );

                recipeName.setText(
                        recipe.getName()
                );

                recipeDescription.setText(
                        "You have all the ingredients needed"
                );

                viewRecipeButton.setOnClickListener(v -> {

                    Intent intent =
                            new Intent(
                                    SuggestedRecipesActivity.this,
                                    RecipeDetailActivity.class
                            );

                    intent.putExtra(
                            "recipeId",
                            (int) recipe.getId()
                    );

                    startActivity(intent);
                });

                recipeContainer.addView(recipeCard);
            }
        }

        if (foundRecipe) {

            emptyMessage.setVisibility(
                    View.GONE
            );

        } else {

            emptyMessage.setVisibility(
                    View.VISIBLE
            );

            emptyMessage.setText(
                    "No recipes available yet. Add more ingredients to discover recipes!"
            );
        }
    }
}