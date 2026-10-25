package com.example.smartpantry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class PantryDatabase extends SQLiteOpenHelper {
    private final Context context;

    public PantryDatabase(Context context) {
        this(context, "pantry.db");
    }

    public PantryDatabase(Context context, String name) {
        super(context, name, null, 1);
        this.context = context.getApplicationContext();
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
        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new SQLiteException("A database migration is required.");
    }

    private void seedRecipes(SQLiteDatabase db) {
        try (InputStream input = context.getAssets().open("recipes.json");
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int count;
            while ((count = input.read(buffer)) != -1) {
                output.write(buffer, 0, count);
            }
            JSONArray recipes = new JSONArray(output.toString(StandardCharsets.UTF_8.name()));
            for (int index = 0; index < recipes.length(); index++) {
                JSONObject recipe = recipes.getJSONObject(index);
                ContentValues values = new ContentValues();
                values.put("name", recipe.getString("name"));
                values.put("method", recipe.getString("method"));
                long recipeId = db.insertOrThrow("recipes", null, values);
                JSONArray ingredients = recipe.getJSONArray("ingredients");
                for (int position = 0; position < ingredients.length(); position++) {
                    JSONObject ingredient = ingredients.getJSONObject(position);
                    ContentValues required = new ContentValues();
                    required.put("recipe_id", recipeId);
                    required.put("name", ingredient.getString("name"));
                    required.put("quantity", ingredient.getDouble("quantity"));
                    required.put("unit", ingredient.getString("unit"));
                    db.insertOrThrow("recipe_ingredients", null, required);
                }
            }
        } catch (Exception exception) {
            throw new SQLiteException("Could not load the recipe collection.", exception);
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
