package com.example.pantrywise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PantryWise.db";
    private static final int DATABASE_VERSION = 8;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createPantryTable(db);
        createRecipeTables(db);
        addRecipes(db);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion
    ) {
        createPantryTable(db);

        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        db.execSQL("DROP TABLE IF EXISTS recipes");

        createRecipeTables(db);
        addRecipes(db);
    }

    private void createPantryTable(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS pantry (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL," +
                        "quantity REAL NOT NULL," +
                        "unit TEXT NOT NULL," +
                        "expiryDate TEXT)"
        );
    }

    private void createRecipeTables(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS recipes (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL," +
                        "method TEXT NOT NULL)"
        );

        db.execSQL(
                "CREATE TABLE IF NOT EXISTS recipe_ingredients (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "recipeId INTEGER NOT NULL," +
                        "ingredientName TEXT NOT NULL," +
                        "quantity REAL NOT NULL," +
                        "unit TEXT NOT NULL)"
        );
    }

    public long addIngredient(
            String name,
            double quantity,
            String unit,
            String expiryDate
    ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("expiryDate", expiryDate);

        return db.insert(
                "pantry",
                null,
                values
        );
    }

    public ArrayList<Ingredient> getAllIngredients() {

        ArrayList<Ingredient> ingredients =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM pantry ORDER BY id DESC",
                null
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow("name")
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow("quantity")
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow("unit")
                );

                String expiryDate = cursor.getString(
                        cursor.getColumnIndexOrThrow("expiryDate")
                );

                ingredients.add(
                        new Ingredient(
                                id,
                                name,
                                quantity,
                                unit,
                                expiryDate
                        )
                );

            } while (cursor.moveToNext());
        }

        cursor.close();

        return ingredients;
    }

    public Ingredient getIngredient(int id) {

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM pantry WHERE id = ?",
                new String[]{
                        String.valueOf(id)
                }
        );

        Ingredient ingredient = null;

        if (cursor.moveToFirst()) {

            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow("name")
            );

            double quantity = cursor.getDouble(
                    cursor.getColumnIndexOrThrow("quantity")
            );

            String unit = cursor.getString(
                    cursor.getColumnIndexOrThrow("unit")
            );

            String expiryDate = cursor.getString(
                    cursor.getColumnIndexOrThrow("expiryDate")
            );

            ingredient = new Ingredient(
                    id,
                    name,
                    quantity,
                    unit,
                    expiryDate
            );
        }

        cursor.close();

        return ingredient;
    }

    public boolean updateIngredient(
            int id,
            String name,
            double quantity,
            String unit,
            String expiryDate
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("expiryDate", expiryDate);

        int result = db.update(
                "pantry",
                values,
                "id = ?",
                new String[]{
                        String.valueOf(id)
                }
        );

        return result > 0;
    }

    public boolean deleteIngredient(int id) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        int result = db.delete(
                "pantry",
                "id = ?",
                new String[]{
                        String.valueOf(id)
                }
        );

        return result > 0;
    }

    private void addRecipes(SQLiteDatabase db) {

        addRecipe(
                db,
                "Tuna Pasta",
                "Cook the pasta. Fry the onion and tomato. Add tuna and cheese. Mix with the pasta and serve.",
                new String[]{
                        "pasta",
                        "tuna",
                        "onion",
                        "tomato",
                        "cheese"
                },
                new double[]{
                        200,
                        1,
                        1,
                        2,
                        50
                },
                new String[]{
                        "grams",
                        "can",
                        "piece",
                        "pieces",
                        "grams"
                }
        );

        addRecipe(
                db,
                "Chicken Fried Rice",
                "Cook the rice. Fry the chicken, onion and carrot. Add the egg and cooked rice. Stir well and serve.",
                new String[]{
                        "rice",
                        "chicken",
                        "onion",
                        "carrot",
                        "egg"
                },
                new double[]{
                        300,
                        200,
                        1,
                        1,
                        2
                },
                new String[]{
                        "grams",
                        "grams",
                        "piece",
                        "piece",
                        "pieces"
                }
        );

        addRecipe(
                db,
                "Cheesy Potato Bake",
                "Boil the potatoes. Place potatoes, onion, cheese and milk in a baking dish. Bake until golden.",
                new String[]{
                        "potato",
                        "cheese",
                        "onion",
                        "milk"
                },
                new double[]{
                        3,
                        100,
                        1,
                        100
                },
                new String[]{
                        "pieces",
                        "grams",
                        "piece",
                        "ml"
                }
        );

        addRecipe(
                db,
                "Tomato Egg Toast",
                "Toast the bread. Cook the eggs and tomato. Place them on the bread and add cheese.",
                new String[]{
                        "bread",
                        "tomato",
                        "egg",
                        "cheese"
                },
                new double[]{
                        2,
                        1,
                        2,
                        50
                },
                new String[]{
                        "slices",
                        "piece",
                        "pieces",
                        "grams"
                }
        );

        addRecipe(
                db,
                "Chicken Wrap",
                "Cook the chicken. Add chicken, lettuce, tomato and cheese to the bread. Roll and serve.",
                new String[]{
                        "chicken",
                        "lettuce",
                        "tomato",
                        "cheese",
                        "bread"
                },
                new double[]{
                        150,
                        2,
                        1,
                        50,
                        1
                },
                new String[]{
                        "grams",
                        "leaves",
                        "piece",
                        "grams",
                        "piece"
                }
        );

        addRecipe(
                db,
                "Vegetable Stir-Fry",
                "Cook the rice. Stir-fry the carrot, onion, pepper and cabbage. Serve with rice.",
                new String[]{
                        "carrot",
                        "onion",
                        "pepper",
                        "cabbage",
                        "rice"
                },
                new double[]{
                        2,
                        1,
                        1,
                        100,
                        300
                },
                new String[]{
                        "pieces",
                        "piece",
                        "piece",
                        "grams",
                        "grams"
                }
        );

        addRecipe(
                db,
                "Creamy Garlic Pasta",
                "Cook the pasta. Fry garlic and onion. Add milk and cheese. Mix with the pasta and serve.",
                new String[]{
                        "pasta",
                        "milk",
                        "cheese",
                        "garlic",
                        "onion"
                },
                new double[]{
                        200,
                        100,
                        50,
                        2,
                        1
                },
                new String[]{
                        "grams",
                        "ml",
                        "grams",
                        "cloves",
                        "piece"
                }
        );

        addRecipe(
                db,
                "Chicken Tomato Pasta",
                "Cook the pasta. Cook the chicken, tomato and onion. Add cheese and mix with the pasta.",
                new String[]{
                        "chicken",
                        "pasta",
                        "tomato",
                        "onion",
                        "cheese"
                },
                new double[]{
                        200,
                        200,
                        2,
                        1,
                        50
                },
                new String[]{
                        "grams",
                        "grams",
                        "pieces",
                        "piece",
                        "grams"
                }
        );

        addRecipe(
                db,
                "Potato and Egg Hash",
                "Cook the potatoes and onion. Add tomato and eggs. Cook until the eggs are done.",
                new String[]{
                        "potato",
                        "egg",
                        "onion",
                        "tomato"
                },
                new double[]{
                        3,
                        2,
                        1,
                        1
                },
                new String[]{
                        "pieces",
                        "pieces",
                        "piece",
                        "piece"
                }
        );

        addRecipe(
                db,
                "Cheese and Vegetable Omelette",
                "Beat the eggs. Add cheese, onion, tomato and pepper. Cook until set.",
                new String[]{
                        "egg",
                        "cheese",
                        "onion",
                        "tomato",
                        "pepper"
                },
                new double[]{
                        2,
                        50,
                        1,
                        1,
                        1
                },
                new String[]{
                        "pieces",
                        "grams",
                        "piece",
                        "piece",
                        "piece"
                }
        );

        addRecipe(
                db,
                "Chicken and Potato Curry",
                "Cook the chicken, potatoes, onion and tomato. Add curry powder and simmer until cooked.",
                new String[]{
                        "chicken",
                        "potato",
                        "onion",
                        "tomato",
                        "curry powder"
                },
                new double[]{
                        200,
                        3,
                        1,
                        1,
                        10
                },
                new String[]{
                        "grams",
                        "pieces",
                        "piece",
                        "piece",
                        "grams"
                }
        );

        addRecipe(
                db,
                "Vegetable Rice Bowl",
                "Cook the rice. Cook the carrot, peas and onion. Add egg and serve everything together.",
                new String[]{
                        "rice",
                        "carrot",
                        "peas",
                        "onion",
                        "egg"
                },
                new double[]{
                        300,
                        2,
                        100,
                        1,
                        1
                },
                new String[]{
                        "grams",
                        "pieces",
                        "grams",
                        "piece",
                        "piece"
                }
        );

        addRecipe(
                db,
                "Tuna Sandwich",
                "Mix tuna with onion and tomato. Place the mixture on bread and add cheese.",
                new String[]{
                        "bread",
                        "tuna",
                        "onion",
                        "tomato",
                        "cheese"
                },
                new double[]{
                        2,
                        1,
                        1,
                        1,
                        50
                },
                new String[]{
                        "slices",
                        "can",
                        "piece",
                        "piece",
                        "grams"
                }
        );

        addRecipe(
                db,
                "Chicken Soup",
                "Cook the chicken, potatoes, carrots, onion and tomato in water until soft.",
                new String[]{
                        "chicken",
                        "potato",
                        "carrot",
                        "onion",
                        "tomato"
                },
                new double[]{
                        200,
                        2,
                        2,
                        1,
                        1
                },
                new String[]{
                        "grams",
                        "pieces",
                        "pieces",
                        "piece",
                        "piece"
                }
        );

        addRecipe(
                db,
                "Creamy Chicken Pasta",
                "Cook the pasta and chicken. Add onion, milk and cheese. Mix together and serve.",
                new String[]{
                        "chicken",
                        "pasta",
                        "milk",
                        "cheese",
                        "onion"
                },
                new double[]{
                        200,
                        200,
                        100,
                        50,
                        1
                },
                new String[]{
                        "grams",
                        "grams",
                        "ml",
                        "grams",
                        "piece"
                }
        );

        addRecipe(
                db,
                "French Toast",
                "Dip the bread in beaten eggs and milk. Fry until golden and add sugar.",
                new String[]{
                        "bread",
                        "egg",
                        "milk",
                        "sugar"
                },
                new double[]{
                        2,
                        2,
                        50,
                        10
                },
                new String[]{
                        "slices",
                        "pieces",
                        "ml",
                        "grams"
                }
        );

        addRecipe(
                db,
                "Chips",
                "Cut the potatoes into chips. Fry in oil and add salt.",
                new String[]{
                        "potato",
                        "oil",
                        "salt"
                },
                new double[]{
                        4,
                        100,
                        5
                },
                new String[]{
                        "pieces",
                        "ml",
                        "grams"
                }
        );

        addRecipe(
                db,
                "Burger",
                "Cook the burger patty. Place it on bread with cheese, tomato, onion and lettuce.",
                new String[]{
                        "burger patty",
                        "bread",
                        "cheese",
                        "tomato",
                        "onion",
                        "lettuce"
                },
                new double[]{
                        1,
                        1,
                        30,
                        1,
                        1,
                        1
                },
                new String[]{
                        "piece",
                        "piece",
                        "grams",
                        "piece",
                        "piece",
                        "leaf"
                }
        );

        addRecipe(
                db,
                "Chicken and Vegetable Stir-Fry",
                "Cook the chicken. Stir-fry the carrot, pepper and onion. Serve with rice.",
                new String[]{
                        "chicken",
                        "carrot",
                        "pepper",
                        "onion",
                        "rice"
                },
                new double[]{
                        200,
                        2,
                        1,
                        1,
                        300
                },
                new String[]{
                        "grams",
                        "pieces",
                        "piece",
                        "piece",
                        "grams"
                }
        );

        addRecipe(
                db,
                "Cheesy Scrambled Eggs",
                "Beat the eggs with milk. Cook with onion and cheese until scrambled and creamy.",
                new String[]{
                        "egg",
                        "cheese",
                        "milk",
                        "onion"
                },
                new double[]{
                        2,
                        50,
                        30,
                        1
                },
                new String[]{
                        "pieces",
                        "grams",
                        "ml",
                        "piece"
                }
        );
    }

    private void addRecipe(
            SQLiteDatabase db,
            String name,
            String method,
            String[] ingredients,
            double[] quantities,
            String[] units
    ) {

        ContentValues recipeValues =
                new ContentValues();

        recipeValues.put(
                "name",
                name
        );

        recipeValues.put(
                "method",
                method
        );

        long recipeId =
                db.insert(
                        "recipes",
                        null,
                        recipeValues
                );

        for (int i = 0;
             i < ingredients.length;
             i++) {

            ContentValues ingredientValues =
                    new ContentValues();

            ingredientValues.put(
                    "recipeId",
                    recipeId
            );

            ingredientValues.put(
                    "ingredientName",
                    ingredients[i]
            );

            ingredientValues.put(
                    "quantity",
                    quantities[i]
            );

            ingredientValues.put(
                    "unit",
                    units[i]
            );

            db.insert(
                    "recipe_ingredients",
                    null,
                    ingredientValues
            );
        }
    }

    public ArrayList<Recipe> getAllRecipes() {

        ArrayList<Recipe> recipes =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM recipes ORDER BY id",
                null
        );

        if (cursor.moveToFirst()) {

            do {

                int id = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id")
                );

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow("name")
                );

                String method = cursor.getString(
                        cursor.getColumnIndexOrThrow("method")
                );

                recipes.add(
                        new Recipe(
                                id,
                                name,
                                method
                        )
                );

            } while (cursor.moveToNext());
        }

        cursor.close();

        return recipes;
    }

    public ArrayList<RecipeIngredient> getRecipeIngredients(
            int recipeId
    ) {

        ArrayList<RecipeIngredient> ingredients =
                new ArrayList<>();

        SQLiteDatabase db =
                this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT ingredientName, quantity, unit " +
                        "FROM recipe_ingredients " +
                        "WHERE recipeId = ?",
                new String[]{
                        String.valueOf(recipeId)
                }
        );

        if (cursor.moveToFirst()) {

            do {

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                "ingredientName"
                        )
                );

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(
                                "quantity"
                        )
                );

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                "unit"
                        )
                );

                ingredients.add(
                        new RecipeIngredient(
                                name,
                                quantity,
                                unit
                        )
                );

            } while (cursor.moveToNext());
        }

        cursor.close();

        return ingredients;
    }

    public boolean updateRecipe(
            int recipeId,
            String name,
            String method
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put(
                "name",
                name
        );

        values.put(
                "method",
                method
        );

        int result = db.update(
                "recipes",
                values,
                "id = ?",
                new String[]{
                        String.valueOf(recipeId)
                }
        );

        return result > 0;
    }

    public boolean deleteRecipe(int recipeId) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        db.delete(
                "recipe_ingredients",
                "recipeId = ?",
                new String[]{
                        String.valueOf(recipeId)
                }
        );

        int result = db.delete(
                "recipes",
                "id = ?",
                new String[]{
                        String.valueOf(recipeId)
                }
        );

        return result > 0;
    }

    public boolean updateRecipeIngredients(
            int recipeId,
            ArrayList<String> names,
            ArrayList<Double> quantities,
            ArrayList<String> units
    ) {

        SQLiteDatabase db =
                this.getWritableDatabase();

        db.delete(
                "recipe_ingredients",
                "recipeId = ?",
                new String[]{
                        String.valueOf(recipeId)
                }
        );

        for (int i = 0; i < names.size(); i++) {

            ContentValues values =
                    new ContentValues();

            values.put(
                    "recipeId",
                    recipeId
            );

            values.put(
                    "ingredientName",
                    names.get(i)
            );

            values.put(
                    "quantity",
                    quantities.get(i)
            );

            values.put(
                    "unit",
                    units.get(i)
            );

            db.insert(
                    "recipe_ingredients",
                    null,
                    values
            );
        }

        return true;
    }
}