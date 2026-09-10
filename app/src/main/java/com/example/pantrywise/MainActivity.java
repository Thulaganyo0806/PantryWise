package com.example.pantrywise;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private LinearLayout cardShop;
    private LinearLayout cardAddIngredient;
    private LinearLayout cardMultiple;
    private LinearLayout cardRecipes;
    private LinearLayout cardSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        cardShop = findViewById(R.id.cardShop);
        cardAddIngredient = findViewById(R.id.cardAddIngredient);
        cardMultiple = findViewById(R.id.cardMultiple);
        cardRecipes = findViewById(R.id.cardRecipes);
        cardSettings = findViewById(R.id.cardSettings);

        setCardBackground(cardShop, "#F9D5E7");
        setCardBackground(cardAddIngredient, "#E8DCF6");
        setCardBackground(cardMultiple, "#F9D5E7");
        setCardBackground(cardRecipes, "#E8DCF6");
        setCardBackground(cardSettings, "#F9D5E7");

        cardShop.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    ShopActivity.class
            );
            startActivity(intent);
        });

        cardAddIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AddIngredientActivity.class
            );
            startActivity(intent);
        });

        cardMultiple.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AddMultipleIngredientsActivity.class
            );
            startActivity(intent);
        });

        cardRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    SuggestedRecipesActivity.class
            );
            startActivity(intent);
        });

        cardSettings.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    SettingsActivity.class
            );
            startActivity(intent);
        });
    }

    private void setCardBackground(View view, String colour) {

        GradientDrawable background =
                new GradientDrawable();

        background.setColor(
                Color.parseColor(colour)
        );

        background.setCornerRadius(38);

        background.setStroke(
                2,
                Color.parseColor("#E6A8CA")
        );

        view.setBackground(background);
    }
}