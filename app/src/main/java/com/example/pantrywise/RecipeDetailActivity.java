package com.example.pantrywise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private TextView txtRecipeName;
    private TextView txtIngredients;
    private TextView txtMethod;

    private Button btnEditRecipe;
    private Button btnDeleteRecipe;

    private int recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);

        txtRecipeName = findViewById(R.id.txtRecipeName);
        txtIngredients = findViewById(R.id.txtIngredients);
        txtMethod = findViewById(R.id.txtMethod);

        btnEditRecipe = findViewById(R.id.btnEditRecipe);
        btnDeleteRecipe = findViewById(R.id.btnDeleteRecipe);

        databaseHelper = new DatabaseHelper(this);

        recipeId = getIntent().getIntExtra("recipeId", -1);

        if (recipeId == -1) {
            recipeId = getIntent().getIntExtra("recipe_id", -1);
        }

        if (recipeId == -1) {
            finish();
            return;
        }

        btnEditRecipe.setOnClickListener(view -> {
            Intent intent = new Intent(
                    RecipeDetailActivity.this,
                    EditRecipeActivity.class
            );

            intent.putExtra("recipeId", recipeId);
            startActivity(intent);
        });

        btnDeleteRecipe.setOnClickListener(view -> {
            confirmDelete();
        });

        loadRecipe(recipeId);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null && recipeId != -1) {
            loadRecipe(recipeId);
        }
    }

    private void loadRecipe(int recipeId) {

        ArrayList<Recipe> recipes =
                databaseHelper.getAllRecipes();

        Recipe selectedRecipe = null;

        for (Recipe recipe : recipes) {

            if (recipe.getId() == recipeId) {
                selectedRecipe = recipe;
                break;
            }
        }

        if (selectedRecipe == null) {
            finish();
            return;
        }

        txtRecipeName.setText(selectedRecipe.getName());

        txtMethod.setText(selectedRecipe.getMethod());

        ArrayList<RecipeIngredient> ingredients =
                databaseHelper.getRecipeIngredients(recipeId);

        StringBuilder ingredientText =
                new StringBuilder();

        for (RecipeIngredient ingredient : ingredients) {

            ingredientText
                    .append("• ")
                    .append(ingredient.getName())
                    .append(" - ")
                    .append(ingredient.getQuantity())
                    .append(" ")
                    .append(ingredient.getUnit())
                    .append("\n");
        }

        txtIngredients.setText(
                ingredientText.toString()
        );
    }

    private void confirmDelete() {

        new AlertDialog.Builder(this)
                .setTitle("Delete Recipe")
                .setMessage(
                        "Are you sure you want to delete this recipe?"
                )
                .setPositiveButton("DELETE", (dialog, which) -> {

                    boolean deleted =
                            databaseHelper.deleteRecipe(recipeId);

                    if (deleted) {

                        Toast.makeText(
                                this,
                                "Recipe deleted successfully",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();

                    } else {

                        Toast.makeText(
                                this,
                                "Could not delete recipe",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }
}