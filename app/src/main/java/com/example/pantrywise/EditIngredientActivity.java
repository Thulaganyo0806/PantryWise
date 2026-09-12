package com.example.pantrywise;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditIngredientActivity extends AppCompatActivity {

    private EditText edtEditName;
    private EditText edtEditQuantity;
    private EditText edtEditUnit;
    private EditText edtEditExpiry;

    private Button btnUpdateIngredient;

    private DatabaseHelper databaseHelper;

    private int ingredientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_ingredient);

        edtEditName = findViewById(R.id.edtEditName);
        edtEditQuantity = findViewById(R.id.edtEditQuantity);
        edtEditUnit = findViewById(R.id.edtEditUnit);
        edtEditExpiry = findViewById(R.id.edtEditExpiry);

        btnUpdateIngredient =
                findViewById(R.id.btnUpdateIngredient);

        databaseHelper = new DatabaseHelper(this);

        ingredientId = getIntent().getIntExtra(
                "ingredient_id",
                -1
        );

        if (ingredientId == -1) {

            Toast.makeText(
                    this,
                    "Ingredient could not be found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        loadIngredient();

        btnUpdateIngredient.setOnClickListener(view -> {
            updateIngredient();
        });
    }

    private void loadIngredient() {

        Ingredient ingredient =
                databaseHelper.getIngredient(ingredientId);

        if (ingredient == null) {

            Toast.makeText(
                    this,
                    "Ingredient could not be found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        edtEditName.setText(ingredient.name);

        edtEditQuantity.setText(
                String.valueOf(ingredient.quantity)
        );

        edtEditUnit.setText(ingredient.unit);

        if (ingredient.expiryDate != null) {
            edtEditExpiry.setText(ingredient.expiryDate);
        }
    }

    private void updateIngredient() {

        String name =
                edtEditName.getText().toString().trim();

        String quantityText =
                edtEditQuantity.getText().toString().trim();

        String unit =
                edtEditUnit.getText().toString().trim();

        String expiryDate =
                edtEditExpiry.getText().toString().trim();

        if (name.isEmpty()) {

            edtEditName.setError(
                    "Enter an ingredient name"
            );

            edtEditName.requestFocus();

            return;
        }

        if (quantityText.isEmpty()) {

            edtEditQuantity.setError(
                    "Enter a quantity"
            );

            edtEditQuantity.requestFocus();

            return;
        }

        if (unit.isEmpty()) {

            edtEditUnit.setError(
                    "Enter a unit"
            );

            edtEditUnit.requestFocus();

            return;
        }

        double quantity;

        try {

            quantity =
                    Double.parseDouble(quantityText);

        } catch (NumberFormatException e) {

            edtEditQuantity.setError(
                    "Enter a valid number"
            );

            edtEditQuantity.requestFocus();

            return;
        }

        if (quantity <= 0) {

            edtEditQuantity.setError(
                    "Quantity must be greater than 0"
            );

            edtEditQuantity.requestFocus();

            return;
        }

        boolean updated =
                databaseHelper.updateIngredient(
                        ingredientId,
                        name,
                        quantity,
                        unit,
                        expiryDate
                );

        if (updated) {

            Toast.makeText(
                    this,
                    name + " updated successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Could not update ingredient",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}