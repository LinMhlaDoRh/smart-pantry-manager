package com.example.smartpantry;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class PantryActivity extends BaseActivity implements IngredientAdapter.IngredientListener {
    private IngredientAdapter adapter;
    private RecyclerView list;
    private TextView empty;
    private TextView count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareScreen(R.layout.activity_pantry);
        initNavigation(PantryActivity.class);
        list = findViewById(R.id.pantryList);
        empty = findViewById(R.id.emptyPantry);
        count = findViewById(R.id.pantryCount);
        adapter = new IngredientAdapter(this);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        findViewById(R.id.addIngredient).setOnClickListener(view ->
                startActivity(new Intent(this, IngredientActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Apply ingredient changes and sorting when returning to this screen.
        loadPantry();
    }

    private void loadPantry() {
        try {
            ArrayList<Ingredient> ingredients = database.getPantry(preferences.getBoolean("sort_descending", false));
            adapter.setIngredients(ingredients);
            empty.setText(R.string.pantry_empty);
            count.setText(getResources().getQuantityString(R.plurals.pantry_count, ingredients.size(), ingredients.size()));
            list.setVisibility(ingredients.isEmpty() ? View.GONE : View.VISIBLE);
            empty.setVisibility(ingredients.isEmpty() ? View.VISIBLE : View.GONE);
        } catch (SQLiteException exception) {
            list.setVisibility(View.GONE);
            empty.setText(R.string.load_failed);
            empty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEdit(Ingredient ingredient) {
        Intent intent = new Intent(this, IngredientActivity.class);
        intent.putExtra(IngredientActivity.EXTRA_INGREDIENT_ID, ingredient.getId());
        startActivity(intent);
    }

    @Override
    public void onDelete(Ingredient ingredient) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_title)
                .setMessage(getString(R.string.delete_message, ingredient.getName()))
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    try {
                        if (database.deleteIngredient(ingredient.getId())) {
                            showMessage(R.string.deleted);
                            loadPantry();
                        } else {
                            showMessage(R.string.delete_failed);
                        }
                    } catch (SQLiteException exception) {
                        showMessage(R.string.delete_failed);
                    }
                })
                .show();
    }
}
