package com.example.smartpantry;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.TextView;

public class RecipeDetailActivity extends BaseActivity {
    public static final String EXTRA_RECIPE_ID = "recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareScreen(R.layout.activity_recipe_detail);
        findViewById(R.id.backButton).setOnClickListener(view -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            long recipeId = getIntent().getLongExtra(EXTRA_RECIPE_ID, -1);
            Recipe recipe = database.getRecipe(recipeId);
            if (recipe == null) {
                showMessage(R.string.missing_recipe);
                finish();
                return;
            }
            ((TextView) findViewById(R.id.detailName)).setText(recipe.getName());
            StringBuilder ingredients = new StringBuilder();
            for (Ingredient ingredient : recipe.getIngredients()) {
                if (ingredients.length() > 0) {
                    ingredients.append("\n\n");
                }
                ingredients.append(ingredient.getName()).append(" · ").append(quantityText(ingredient));
            }
            ((TextView) findViewById(R.id.detailIngredients)).setText(ingredients.toString());
            ((TextView) findViewById(R.id.detailMethod)).setText(recipe.getMethod());
            boolean ready = RecipeMatcher.matches(recipe, database.getPantry(false));
            ((TextView) findViewById(R.id.detailStatus)).setText(ready ? R.string.recipe_ready : R.string.recipe_not_ready);
        } catch (SQLiteException exception) {
            showMessage(R.string.load_failed);
            finish();
        }
    }
}
