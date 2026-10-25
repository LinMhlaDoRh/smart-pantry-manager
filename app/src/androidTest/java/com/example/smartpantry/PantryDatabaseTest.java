package com.example.smartpantry;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class PantryDatabaseTest {
    private Context context;
    private PantryDatabase database;
    private final String databaseName = "pantry_test.db";

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context.deleteDatabase(databaseName);
        database = new PantryDatabase(context, databaseName);
    }

    @After
    public void tearDown() {
        database.close();
        context.deleteDatabase(databaseName);
    }

    @Test
    public void createReadUpdateDeletePersistAfterReopening() {
        long id = database.addIngredient(new Ingredient(0, "tomatoes", 2, "pcs"));
        assertTrue(id > 0);
        database.close();
        database = new PantryDatabase(context, databaseName);
        assertEquals(2, database.getIngredient(id).getQuantity(), 0.0001);
        assertTrue(database.updateIngredient(new Ingredient(id, "tomato", 3, "pcs")));
        database.close();
        database = new PantryDatabase(context, databaseName);
        assertEquals(3, database.getIngredient(id).getQuantity(), 0.0001);
        assertTrue(database.deleteIngredient(id));
        assertFalse(database.deleteIngredient(id));
        database.close();
        database = new PantryDatabase(context, databaseName);
        assertNull(database.getIngredient(id));
        assertTrue(database.getPantry(false).isEmpty());
    }

    @Test
    public void twentyRecipesSeedOnceWithCompleteIngredients() {
        ArrayList<Recipe> recipes = database.getRecipes();
        assertEquals(20, recipes.size());
        for (Recipe recipe : recipes) {
            assertFalse(recipe.getIngredients().isEmpty());
            assertFalse(recipe.getMethod().isEmpty());
            assertTrue(RecipeMatcher.matches(recipe, recipe.getIngredients()));
            assertEquals(recipe.getName(), database.getRecipe(recipe.getId()).getName());
        }
        database.close();
        database = new PantryDatabase(context, databaseName);
        assertEquals(20, database.getRecipes().size());
        assertTrue(database.getPantry(false).isEmpty());
    }

    @Test
    public void pantrySortAndMissingRecordHandlingWork() {
        database.addIngredient(new Ingredient(0, "rice", 200, "g"));
        database.addIngredient(new Ingredient(0, "Apple", 1, "pcs"));
        assertEquals("Apple", database.getPantry(false).get(0).getName());
        assertEquals("rice", database.getPantry(true).get(0).getName());
        assertNull(database.getIngredient(-1));
        assertNull(database.getRecipe(-1));
        assertFalse(database.updateIngredient(new Ingredient(999, "rice", 20, "g")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidQuantityIsRejectedBeforeSaving() {
        database.addIngredient(new Ingredient(0, "rice", 0, "g"));
    }

    @Test
    public void databaseChangesUpdateStrictSuggestions() {
        Recipe bananaMilk = null;
        for (Recipe recipe : database.getRecipes()) {
            if (recipe.getName().equals("Banana milk")) {
                bananaMilk = recipe;
            }
        }
        assertNotNull(bananaMilk);
        long bananaId = database.addIngredient(new Ingredient(0, "BANANAS", 1, "pcs"));
        assertFalse(RecipeMatcher.matches(bananaMilk, database.getPantry(false)));
        long milkId = database.addIngredient(new Ingredient(0, "milk", 0.25, "l"));
        assertTrue(RecipeMatcher.matches(bananaMilk, database.getPantry(false)));
        database.updateIngredient(new Ingredient(milkId, "milk", 249, "ml"));
        assertFalse(RecipeMatcher.matches(bananaMilk, database.getPantry(false)));
        database.updateIngredient(new Ingredient(milkId, "milk", 250, "ml"));
        database.deleteIngredient(bananaId);
        assertFalse(RecipeMatcher.matches(bananaMilk, database.getPantry(false)));
    }
}
