package com.example.smartpantry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class PantryDatabase extends SQLiteOpenHelper {
    public PantryDatabase(Context context) {
        super(context, "pantry.db", null, 1);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, quantity REAL NOT NULL CHECK(quantity > 0), unit TEXT NOT NULL)");
        db.execSQL("CREATE TABLE recipes (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, method TEXT NOT NULL)");
        db.execSQL("CREATE TABLE recipe_ingredients (_id INTEGER PRIMARY KEY AUTOINCREMENT, recipe_id INTEGER NOT NULL REFERENCES recipes(_id) ON DELETE CASCADE, name TEXT NOT NULL, quantity REAL NOT NULL CHECK(quantity > 0), unit TEXT NOT NULL)");
        // Seed only when the database is first created.
        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new SQLiteException("A database migration is required.");
    }

    private void seedRecipes(SQLiteDatabase db) {
        seedRecipe(db, "Scrambled eggs",
                "1. Beat the eggs with the milk.\n\n2. Melt the butter in a pan over low heat.\n\n3. Add the eggs and gently stir until fully set. Serve immediately.",
                new String[]{"egg", "milk", "butter"},
                new double[]{2, 30, 10},
                new String[]{"pcs", "ml", "g"});
        seedRecipe(db, "Tomato omelette",
                "1. Chop the tomato and beat the eggs.\n\n2. Heat the oil in a pan and soften the tomato.\n\n3. Add the eggs and cook over low heat until fully set. Fold and serve.",
                new String[]{"egg", "tomato", "oil"},
                new double[]{2, 1, 5},
                new String[]{"pcs", "pcs", "ml"});
        seedRecipe(db, "Cheese toast",
                "1. Spread the butter on the bread and place the cheese on top.\n\n2. Cook under a grill until the cheese melts and the bread is toasted. Serve warm.",
                new String[]{"bread", "cheese", "butter"},
                new double[]{2, 40, 5},
                new String[]{"pcs", "g", "g"});
        seedRecipe(db, "Mashed potato",
                "1. Peel and cut the potatoes. Place in a covered microwave-safe bowl.\n\n2. Microwave until tender, stirring and checking regularly.\n\n3. Mash with the milk and butter. Heat through before serving.",
                new String[]{"potato", "milk", "butter"},
                new double[]{3, 100, 15},
                new String[]{"pcs", "ml", "g"});
        seedRecipe(db, "Tomato pasta",
                "1. Chop the onion and tomatoes. Cook the onion in the oil, add tomatoes and simmer into a sauce.\n\n2. Bring the measured water to the boil in a separate pot. Add pasta and cook until tender, stirring regularly.\n\n3. Drain remaining water and mix the pasta with the sauce.",
                new String[]{"pasta", "tomato", "onion", "oil", "water"},
                new double[]{200, 3, 1, 10, 1000},
                new String[]{"g", "pcs", "pcs", "ml", "ml"});
        seedRecipe(db, "Egg fried rice",
                "1. Simmer the rice in the measured water until tender and the water is absorbed.\n\n2. Heat oil in a pan, add peas and cook through.\n\n3. Add beaten eggs and stir until fully set. Add rice and stir until piping hot. Serve immediately.",
                new String[]{"rice", "water", "egg", "pea", "oil"},
                new double[]{150, 300, 2, 50, 10},
                new String[]{"g", "ml", "pcs", "g", "ml"});
        seedRecipe(db, "Carrot soup",
                "1. Chop the carrots and onion.\n\n2. Heat the oil in a pot and soften the onion. Add the carrots and water.\n\n3. Simmer until the carrots are tender. Mash or blend carefully and serve hot.",
                new String[]{"carrot", "onion", "water", "oil"},
                new double[]{3, 1, 500, 5},
                new String[]{"pcs", "pcs", "ml", "ml"});
        seedRecipe(db, "Banana oats",
                "1. Combine oats and milk in a saucepan.\n\n2. Simmer gently, stirring until thick and cooked.\n\n3. Slice the banana over the oats and serve.",
                new String[]{"oats", "milk", "banana"},
                new double[]{60, 250, 1},
                new String[]{"g", "ml", "pcs"});
        seedRecipe(db, "Apple porridge",
                "1. Chop the apple into small pieces.\n\n2. Combine apple, oats and milk in a saucepan.\n\n3. Simmer gently, stirring until the oats are cooked and the apple softens.",
                new String[]{"oats", "milk", "apple"},
                new double[]{60, 250, 1},
                new String[]{"g", "ml", "pcs"});
        seedRecipe(db, "Rice pudding",
                "1. Place rice, milk and sugar in a saucepan.\n\n2. Simmer over low heat, stirring frequently until the rice is tender and the pudding thickens.\n\n3. Serve warm.",
                new String[]{"rice", "milk", "sugar"},
                new double[]{100, 500, 20},
                new String[]{"g", "ml", "g"});
        seedRecipe(db, "Potato hash",
                "1. Cut the potatoes into small cubes and finely chop the onion.\n\n2. Heat oil in a pan. Add potatoes, cover and cook over medium-low heat, turning regularly until tender.\n\n3. Add onion and continue cooking until softened and the potatoes are golden.",
                new String[]{"potato", "onion", "oil"},
                new double[]{2, 1, 15},
                new String[]{"pcs", "pcs", "ml"});
        seedRecipe(db, "Mushroom eggs",
                "1. Slice the mushrooms and beat the eggs.\n\n2. Melt butter in a pan and cook the mushrooms until soft.\n\n3. Add the eggs and stir until fully set. Serve warm.",
                new String[]{"mushroom", "egg", "butter"},
                new double[]{100, 2, 10},
                new String[]{"g", "pcs", "g"});
        seedRecipe(db, "Tomato sandwich",
                "1. Slice the tomato.\n\n2. Spread butter over the bread.\n\n3. Add the tomato slices, close the sandwich and serve.",
                new String[]{"bread", "tomato", "butter"},
                new double[]{2, 1, 5},
                new String[]{"pcs", "pcs", "g"});
        seedRecipe(db, "Pea rice",
                "1. Bring rice and water to a gentle simmer in a covered saucepan.\n\n2. When the rice is almost tender, add peas and butter.\n\n3. Continue cooking until the rice is tender and the peas are cooked through.",
                new String[]{"rice", "water", "pea", "butter"},
                new double[]{150, 300, 80, 10},
                new String[]{"g", "ml", "g", "g"});
        seedRecipe(db, "Cheese omelette",
                "1. Beat the eggs and grate the cheese.\n\n2. Melt butter in a pan, add eggs and cook over low heat.\n\n3. Add cheese, fold and continue cooking until the eggs are fully set.",
                new String[]{"egg", "cheese", "butter"},
                new double[]{2, 30, 10},
                new String[]{"pcs", "g", "g"});
        seedRecipe(db, "Carrot and potato soup",
                "1. Chop the carrots, potatoes and onion into small pieces.\n\n2. Place all vegetables in a pot with the measured water.\n\n3. Simmer until tender, then mash or blend carefully and serve hot.",
                new String[]{"carrot", "potato", "onion", "water"},
                new double[]{2, 2, 1, 700},
                new String[]{"pcs", "pcs", "pcs", "ml"});
        seedRecipe(db, "Banana milk",
                "1. Peel and slice the banana.\n\n2. Blend with the milk until smooth. Serve immediately.",
                new String[]{"banana", "milk"},
                new double[]{1, 250},
                new String[]{"pcs", "ml"});
        seedRecipe(db, "Buttered pasta",
                "1. Bring the measured water to the boil and add pasta.\n\n2. Cook until tender, stirring regularly, then drain.\n\n3. Stir through butter until melted and serve.",
                new String[]{"pasta", "water", "butter"},
                new double[]{200, 1000, 20},
                new String[]{"g", "ml", "g"});
        seedRecipe(db, "Warm tomato beans",
                "1. Chop the onion and tomatoes. Drain the cooked beans if needed.\n\n2. Heat oil in a pan and soften the onion. Add tomatoes and simmer into a sauce.\n\n3. Add the cooked beans and heat through. Use cooked or canned beans, not raw dried beans.",
                new String[]{"cooked bean", "tomato", "onion", "oil"},
                new double[]{250, 2, 1, 10},
                new String[]{"g", "pcs", "pcs", "ml"});
        seedRecipe(db, "Pepper and onion eggs",
                "1. Chop the pepper and onion and beat the eggs.\n\n2. Heat oil in a pan and cook the vegetables until soft.\n\n3. Add the eggs and stir until fully set. Serve warm.",
                new String[]{"pepper", "onion", "egg", "oil"},
                new double[]{1, 1, 2, 10},
                new String[]{"pcs", "pcs", "pcs", "ml"});
    }

    private void seedRecipe(SQLiteDatabase db, String name, String method,
                            String[] names, double[] quantities, String[] units) {
        ContentValues recipe = new ContentValues();
        recipe.put("name", name);
        recipe.put("method", method);
        long recipeId = db.insertOrThrow("recipes", null, recipe);
        for (int index = 0; index < names.length; index++) {
            ContentValues ingredient = new ContentValues();
            ingredient.put("recipe_id", recipeId);
            ingredient.put("name", names[index]);
            ingredient.put("quantity", quantities[index]);
            ingredient.put("unit", units[index]);
            db.insertOrThrow("recipe_ingredients", null, ingredient);
        }
    }

    private ContentValues valuesFor(Ingredient ingredient) {
        if (ingredient.getName() == null || ingredient.getName().trim().isEmpty()
                || ingredient.getName().trim().length() > 60
                || Double.isNaN(ingredient.getQuantity()) || Double.isInfinite(ingredient.getQuantity()) || ingredient.getQuantity() <= 0
                || ingredient.getQuantity() > 1000000000
                || ingredient.getUnit() == null || RecipeMatcher.unitGroup(ingredient.getUnit()).isEmpty()) {
            throw new IllegalArgumentException("Enter a name, positive quantity and supported unit.");
        }
        ContentValues values = new ContentValues();
        values.put("name", ingredient.getName().trim());
        values.put("quantity", ingredient.getQuantity());
        values.put("unit", RecipeMatcher.normalizeUnit(ingredient.getUnit()));
        return values;
    }

    public long addIngredient(Ingredient ingredient) {
        return getWritableDatabase().insertOrThrow("pantry", null, valuesFor(ingredient));
    }

    public boolean updateIngredient(Ingredient ingredient) {
        // Use the row ID so editing one item cannot change another.
        return getWritableDatabase().update("pantry", valuesFor(ingredient), "_id = ?",
                new String[]{String.valueOf(ingredient.getId())}) == 1;
    }

    public boolean deleteIngredient(long id) {
        return getWritableDatabase().delete("pantry", "_id = ?", new String[]{String.valueOf(id)}) == 1;
    }

    private Ingredient readIngredient(Cursor cursor) {
        return new Ingredient(cursor.getLong(cursor.getColumnIndexOrThrow("_id")),
                cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("quantity")),
                cursor.getString(cursor.getColumnIndexOrThrow("unit")));
    }

    public ArrayList<Ingredient> getPantry(boolean descending) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        String order = descending ? "DESC" : "ASC";
        try (Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM pantry ORDER BY name COLLATE NOCASE " + order + ", _id ASC", null)) {
            while (cursor.moveToNext()) {
                ingredients.add(readIngredient(cursor));
            }
        }
        return ingredients;
    }

    public Ingredient getIngredient(long id) {
        try (Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM pantry WHERE _id = ?",
                new String[]{String.valueOf(id)})) {
            return cursor.moveToFirst() ? readIngredient(cursor) : null;
        }
    }

    private ArrayList<Ingredient> getRecipeIngredients(long recipeId) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM recipe_ingredients WHERE recipe_id = ? ORDER BY _id",
                new String[]{String.valueOf(recipeId)})) {
            while (cursor.moveToNext()) {
                ingredients.add(readIngredient(cursor));
            }
        }
        return ingredients;
    }

    private Recipe readRecipe(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));
        return new Recipe(id, cursor.getString(cursor.getColumnIndexOrThrow("name")),
                cursor.getString(cursor.getColumnIndexOrThrow("method")), getRecipeIngredients(id));
    }

    public ArrayList<Recipe> getRecipes() {
        ArrayList<Recipe> recipes = new ArrayList<>();
        try (Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM recipes ORDER BY name", null)) {
            while (cursor.moveToNext()) {
                recipes.add(readRecipe(cursor));
            }
        }
        return recipes;
    }

    public Recipe getRecipe(long id) {
        try (Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM recipes WHERE _id = ?",
                new String[]{String.valueOf(id)})) {
            return cursor.moveToFirst() ? readRecipe(cursor) : null;
        }
    }
}
