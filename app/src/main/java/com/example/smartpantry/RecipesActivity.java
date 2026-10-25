package com.example.smartpantry;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecipesActivity extends BaseActivity {
    private RecipeAdapter adapter;
    private RecyclerView list;
    private TextView empty;
    private TextView count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareScreen(R.layout.activity_recipes);
        initNavigation(RecipesActivity.class);
        list = findViewById(R.id.recipeList);
        empty = findViewById(R.id.emptyRecipes);
        count = findViewById(R.id.recipeCount);
        adapter = new RecipeAdapter(recipe -> {
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
            startActivity(intent);
        });
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            ArrayList<Ingredient> pantry = database.getPantry(false);
            ArrayList<Recipe> suggestions = new ArrayList<>();
            for (Recipe recipe : database.getRecipes()) {
                if (RecipeMatcher.matches(recipe, pantry)) {
                    suggestions.add(recipe);
                }
            }
            adapter.setRecipes(suggestions);
            count.setText(getString(R.string.recipes_count, suggestions.size()));
            list.setVisibility(suggestions.isEmpty() ? View.GONE : View.VISIBLE);
            empty.setText(R.string.recipes_empty);
            empty.setVisibility(suggestions.isEmpty() ? View.VISIBLE : View.GONE);
        } catch (SQLiteException exception) {
            list.setVisibility(View.GONE);
            empty.setText(R.string.load_failed);
            empty.setVisibility(View.VISIBLE);
        }
    }
}
