package com.example.pantrywise;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EditRecipeActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private LinearLayout ingredientContainer;
    private Button btnSaveRecipe;

    private int recipeId;

    private ArrayList<EditText> ingredientNames = new ArrayList<>();
    private ArrayList<EditText> ingredientQuantities = new ArrayList<>();
    private ArrayList<EditText> ingredientUnits = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_recipe);

        databaseHelper = new DatabaseHelper(this);

        ingredientContainer =
                findViewById(R.id.ingredientContainer);

        btnSaveRecipe =
                findViewById(R.id.btnSaveRecipe);

        recipeId =
                getIntent().getIntExtra(
                        "recipeId",
                        -1
                );

        if (recipeId == -1) {

            Toast.makeText(
                    this,
                    "Recipe could not be found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        loadIngredients();

        btnSaveRecipe.setOnClickListener(view -> {
            saveIngredients();
        });
    }

    private void loadIngredients() {

        ArrayList<RecipeIngredient> ingredients =
                databaseHelper.getRecipeIngredients(recipeId);

        if (ingredients.isEmpty()) {

            Toast.makeText(
                    this,
                    "No ingredients found",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        for (RecipeIngredient ingredient : ingredients) {

            LinearLayout row =
                    new LinearLayout(this);

            row.setOrientation(
                    LinearLayout.VERTICAL
            );

            row.setPadding(
                    0,
                    10,
                    0,
                    10
            );

            EditText name =
                    new EditText(this);

            name.setHint("Ingredient name");
            name.setText(ingredient.getName());

            EditText quantity =
                    new EditText(this);

            quantity.setHint("Quantity");
            quantity.setText(
                    String.valueOf(
                            ingredient.getQuantity()
                    )
            );

            quantity.setInputType(
                    android.text.InputType.TYPE_CLASS_NUMBER |
                            android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            );

            EditText unit =
                    new EditText(this);

            unit.setHint("Unit");
            unit.setText(ingredient.getUnit());

            row.addView(name);
            row.addView(quantity);
            row.addView(unit);

            ingredientContainer.addView(row);

            ingredientNames.add(name);
            ingredientQuantities.add(quantity);
            ingredientUnits.add(unit);
        }
    }

    private void saveIngredients() {

        ArrayList<String> names =
                new ArrayList<>();

        ArrayList<Double> quantities =
                new ArrayList<>();

        ArrayList<String> units =
                new ArrayList<>();

        for (int i = 0; i < ingredientNames.size(); i++) {

            String name =
                    ingredientNames
                            .get(i)
                            .getText()
                            .toString()
                            .trim();

            String quantityText =
                    ingredientQuantities
                            .get(i)
                            .getText()
                            .toString()
                            .trim();

            String unit =
                    ingredientUnits
                            .get(i)
                            .getText()
                            .toString()
                            .trim();

            if (name.isEmpty()) {

                ingredientNames
                        .get(i)
                        .setError(
                                "Enter an ingredient"
                        );

                ingredientNames
                        .get(i)
                        .requestFocus();

                return;
            }

            if (quantityText.isEmpty()) {

                ingredientQuantities
                        .get(i)
                        .setError(
                                "Enter a quantity"
                        );

                ingredientQuantities
                        .get(i)
                        .requestFocus();

                return;
            }

            if (unit.isEmpty()) {

                ingredientUnits
                        .get(i)
                        .setError(
                                "Enter a unit"
                        );

                ingredientUnits
                        .get(i)
                        .requestFocus();

                return;
            }

            double quantity;

            try {

                quantity =
                        Double.parseDouble(
                                quantityText
                        );

            } catch (NumberFormatException e) {

                ingredientQuantities
                        .get(i)
                        .setError(
                                "Enter a valid number"
                        );

                ingredientQuantities
                        .get(i)
                        .requestFocus();

                return;
            }

            if (quantity <= 0) {

                ingredientQuantities
                        .get(i)
                        .setError(
                                "Quantity must be greater than 0"
                        );

                ingredientQuantities
                        .get(i)
                        .requestFocus();

                return;
            }

            names.add(name);
            quantities.add(quantity);
            units.add(unit);
        }

        boolean updated =
                databaseHelper.updateRecipeIngredients(
                        recipeId,
                        names,
                        quantities,
                        units
                );

        if (updated) {

            Toast.makeText(
                    this,
                    "Recipe ingredients updated",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Could not update ingredients",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}