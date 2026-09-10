package com.example.pantrywise;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddMultipleIngredientsActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    private EditText name1, quantity1, expiry1;
    private EditText name2, quantity2, expiry2;
    private EditText name3, quantity3, expiry3;
    private EditText name4, quantity4, expiry4;
    private EditText name5, quantity5, expiry5;
    private EditText name6, quantity6, expiry6;

    private Spinner unit1, unit2, unit3, unit4, unit5, unit6;

    private Button addAllButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_multiple_ingredients);

        databaseHelper = new DatabaseHelper(this);

        name1 = findViewById(R.id.name1);
        quantity1 = findViewById(R.id.quantity1);
        unit1 = findViewById(R.id.unit1);
        expiry1 = findViewById(R.id.expiry1);

        name2 = findViewById(R.id.name2);
        quantity2 = findViewById(R.id.quantity2);
        unit2 = findViewById(R.id.unit2);
        expiry2 = findViewById(R.id.expiry2);

        name3 = findViewById(R.id.name3);
        quantity3 = findViewById(R.id.quantity3);
        unit3 = findViewById(R.id.unit3);
        expiry3 = findViewById(R.id.expiry3);

        name4 = findViewById(R.id.name4);
        quantity4 = findViewById(R.id.quantity4);
        unit4 = findViewById(R.id.unit4);
        expiry4 = findViewById(R.id.expiry4);

        name5 = findViewById(R.id.name5);
        quantity5 = findViewById(R.id.quantity5);
        unit5 = findViewById(R.id.unit5);
        expiry5 = findViewById(R.id.expiry5);

        name6 = findViewById(R.id.name6);
        quantity6 = findViewById(R.id.quantity6);
        unit6 = findViewById(R.id.unit6);
        expiry6 = findViewById(R.id.expiry6);

        addAllButton = findViewById(R.id.addAllButton);

        String[] units = getResources().getStringArray(R.array.unit_options);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        unit1.setAdapter(adapter);
        unit2.setAdapter(adapter);
        unit3.setAdapter(adapter);
        unit4.setAdapter(adapter);
        unit5.setAdapter(adapter);
        unit6.setAdapter(adapter);

        addAllButton.setOnClickListener(v -> addAllIngredients());
    }

    private void addAllIngredients() {

        int addedCount = 0;

        int result;

        result = saveIngredient(name1, quantity1, unit1, expiry1);
        if (result == -1) {
            return;
        }
        addedCount += result;

        result = saveIngredient(name2, quantity2, unit2, expiry2);
        if (result == -1) {
            return;
        }
        addedCount += result;

        result = saveIngredient(name3, quantity3, unit3, expiry3);
        if (result == -1) {
            return;
        }
        addedCount += result;

        result = saveIngredient(name4, quantity4, unit4, expiry4);
        if (result == -1) {
            return;
        }
        addedCount += result;

        result = saveIngredient(name5, quantity5, unit5, expiry5);
        if (result == -1) {
            return;
        }
        addedCount += result;

        result = saveIngredient(name6, quantity6, unit6, expiry6);
        if (result == -1) {
            return;
        }
        addedCount += result;

        if (addedCount > 0) {
            Toast.makeText(
                    this,
                    addedCount + " ingredient(s) added successfully",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        } else {
            Toast.makeText(
                    this,
                    "Please enter at least one ingredient",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private int saveIngredient(
            EditText name,
            EditText quantity,
            Spinner unit,
            EditText expiry
    ) {

        String ingredientName =
                name.getText().toString().trim();

        String quantityText =
                quantity.getText().toString().trim();

        String selectedUnit =
                unit.getSelectedItem().toString();

        String expiryDate =
                expiry.getText().toString().trim();

        if (TextUtils.isEmpty(ingredientName)
                && TextUtils.isEmpty(quantityText)
                && TextUtils.isEmpty(expiryDate)) {
            return 0;
        }

        if (TextUtils.isEmpty(ingredientName)) {
            name.setError("Enter ingredient name");
            name.requestFocus();
            return -1;
        }

        if (TextUtils.isEmpty(quantityText)) {
            quantity.setError("Enter quantity");
            quantity.requestFocus();
            return -1;
        }

        double quantityValue;

        try {
            quantityValue = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            quantity.setError("Enter a valid number");
            quantity.requestFocus();
            return -1;
        }

        if (quantityValue <= 0) {
            quantity.setError("Quantity must be greater than 0");
            quantity.requestFocus();
            return -1;
        }

        long added = databaseHelper.addIngredient(
                ingredientName,
                quantityValue,
                selectedUnit,
                expiryDate
        );

        if (added == -1) {
            return -1;
        }

        return 1;
    }
}