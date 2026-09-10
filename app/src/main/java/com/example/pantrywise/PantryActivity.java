package com.example.pantrywise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PantryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewIngredients;
    private Button btnAddIngredient;
    private DatabaseHelper databaseHelper;
    private ArrayList<Ingredient> ingredients;
    private IngredientAdapter ingredientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pantry);

        recyclerViewIngredients =
                findViewById(R.id.recyclerViewIngredients);

        btnAddIngredient =
                findViewById(R.id.btnAddIngredient);

        databaseHelper =
                new DatabaseHelper(this);

        recyclerViewIngredients.setLayoutManager(
                new LinearLayoutManager(this)
        );

        btnAddIngredient.setOnClickListener(view -> {

            Intent intent = new Intent(
                    PantryActivity.this,
                    AddIngredientActivity.class
            );

            startActivity(intent);
        });

        loadIngredients();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (databaseHelper != null) {
            loadIngredients();
        }
    }

    private void loadIngredients() {

        ingredients =
                databaseHelper.getAllIngredients();

        ingredientAdapter =
                new IngredientAdapter(
                        this,
                        ingredients,
                        databaseHelper
                );

        recyclerViewIngredients.setAdapter(
                ingredientAdapter
        );
    }
}