package com.example.smartpantry;

import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RecipeMatcherTest {
    private Ingredient ingredient(String name, double quantity, String unit) {
        return new Ingredient(0, name, quantity, unit);
    }

    private Recipe recipe(Ingredient... ingredients) {
        return new Recipe(1, "Test recipe", "Prepare the ingredients.",
                new ArrayList<>(Arrays.asList(ingredients)));
    }

    @Test
    public void missingIngredientExcludesRecipe() {
        Recipe recipe = recipe(ingredient("tomato", 2, "pcs"), ingredient("onion", 1, "pcs"));
        assertFalse(RecipeMatcher.matches(recipe, Collections.singletonList(ingredient("tomato", 2, "pcs"))));
    }

    @Test
    public void everyIngredientAndExactQuantitiesMatch() {
        Recipe recipe = recipe(ingredient("tomato", 2, "pcs"), ingredient("onion", 1, "pcs"));
        assertTrue(RecipeMatcher.matches(recipe, Arrays.asList(ingredient("tomato", 2, "pcs"), ingredient("onion", 1, "pcs"))));
    }

    @Test
    public void insufficientQuantityExcludesRecipe() {
        assertFalse(RecipeMatcher.matches(recipe(ingredient("rice", 200, "g")), Collections.singletonList(ingredient("rice", 199, "g"))));
    }

    @Test
    public void pluralCaseAndWhitespaceMatch() {
        assertTrue(RecipeMatcher.matches(recipe(ingredient("tomato", 2, "pcs")), Collections.singletonList(ingredient("  TOMATOES  ", 2, "pieces"))));
    }

    @Test
    public void kilogramsConvertToGrams() {
        assertTrue(RecipeMatcher.matches(recipe(ingredient("rice", 500, "g")), Collections.singletonList(ingredient("rice", 0.5, "kg"))));
    }

    @Test
    public void litresConvertToMillilitres() {
        assertTrue(RecipeMatcher.matches(recipe(ingredient("milk", 250, "ml")), Collections.singletonList(ingredient("milk", 0.25, "l"))));
    }

    @Test
    public void incompatibleUnitsNeverMatch() {
        assertFalse(RecipeMatcher.matches(recipe(ingredient("tomato", 2, "pcs")), Collections.singletonList(ingredient("tomato", 500, "g"))));
    }

    @Test
    public void separatePantryRowsAreAddedTogether() {
        assertTrue(RecipeMatcher.matches(recipe(ingredient("rice", 300, "g")), Arrays.asList(ingredient("rice", 100, "g"), ingredient("rice", 0.2, "kg"))));
    }

    @Test
    public void repeatedRequirementsCannotReuseTheSameQuantity() {
        assertFalse(RecipeMatcher.matches(recipe(ingredient("rice", 200, "g"), ingredient("rice", 200, "g")), Collections.singletonList(ingredient("rice", 300, "g"))));
    }

    @Test
    public void emptyPantryAndEmptyRecipeDoNotMatch() {
        assertFalse(RecipeMatcher.matches(recipe(ingredient("rice", 200, "g")), Collections.emptyList()));
        assertFalse(RecipeMatcher.matches(recipe(), Collections.emptyList()));
    }

    @Test
    public void invalidQuantitiesAndUnknownUnitsDoNotMatch() {
        assertFalse(RecipeMatcher.matches(recipe(ingredient("rice", 200, "g")), Collections.singletonList(ingredient("rice", Double.NaN, "g"))));
        assertFalse(RecipeMatcher.matches(recipe(ingredient("rice", 200, "g")), Collections.singletonList(ingredient("rice", -5, "kg"))));
        assertFalse(RecipeMatcher.matches(recipe(ingredient("rice", 200, "cup")), Collections.singletonList(ingredient("rice", 200, "cup"))));
    }

    @Test
    public void unrelatedIngredientsDoNotBlockAMatch() {
        assertTrue(RecipeMatcher.matches(recipe(ingredient("egg", 2, "pcs")), Arrays.asList(ingredient("egg", 3, "pcs"), ingredient("rice", 100, "g"))));
    }
}
