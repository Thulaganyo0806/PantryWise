package com.example.pantrywise;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddIngredientActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editQuantity;
    private Spinner spinnerUnit;
    private EditText editExpiry;

    private Button buttonSave;
    private Button buttonMultiple;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        databaseHelper = new DatabaseHelper(this);

        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        editExpiry = findViewById(R.id.editExpiry);

        buttonSave = findViewById(R.id.buttonSave);
        buttonMultiple = findViewById(R.id.buttonMultiple);

        String[] units = getResources().getStringArray(
                R.array.unit_options
        );

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        units
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerUnit.setAdapter(adapter);

        buttonSave.setOnClickListener(v -> saveIngredient());

        buttonMultiple.setOnClickListener(v -> {
            Intent intent = new Intent(
                    AddIngredientActivity.this,
                    AddMultipleIngredientsActivity.class
            );

            startActivity(intent);
        });
    }

    private void saveIngredient() {

        String name =
                editName.getText().toString().trim();

        String quantityText =
                editQuantity.getText().toString().trim();

        String unit =
                spinnerUnit.getSelectedItem().toString();

        String expiryDate =
                editExpiry.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {

            editName.setError(
                    "Please enter an ingredient name"
            );

            editName.requestFocus();

            return;
        }

        if (TextUtils.isEmpty(quantityText)) {

            editQuantity.setError(
                    "Please enter a quantity"
            );

            editQuantity.requestFocus();

            return;
        }

        double quantity;

        try {

            quantity =
                    Double.parseDouble(quantityText);

        } catch (NumberFormatException e) {

            editQuantity.setError(
                    "Please enter a valid number"
            );

            editQuantity.requestFocus();

            return;
        }

        if (quantity <= 0) {

            editQuantity.setError(
                    "Quantity must be greater than 0"
            );

            editQuantity.requestFocus();

            return;
        }

        long added =
                databaseHelper.addIngredient(
                        name,
                        quantity,
                        unit,
                        expiryDate
                );

        if (added != -1) {

            Toast.makeText(
                    this,
                    name + " added successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();

        } else {

            Toast.makeText(
                    this,
                    "Could not add ingredient",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}