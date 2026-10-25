package com.example.smartpantry;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class IngredientActivity extends BaseActivity {
    public static final String EXTRA_INGREDIENT_ID = "ingredient_id";
    private long ingredientId;
    private EditText name;
    private EditText quantity;
    private Spinner unit;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareScreen(R.layout.activity_ingredient);
        name = findViewById(R.id.inputName);
        quantity = findViewById(R.id.inputQuantity);
        unit = findViewById(R.id.inputUnit);
        save = findViewById(R.id.saveIngredient);
        ingredientId = getIntent().getLongExtra(EXTRA_INGREDIENT_ID, -1);
        findViewById(R.id.backButton).setOnClickListener(view -> finish());
        save.setOnClickListener(view -> saveIngredient());
        if (ingredientId != -1) {
            ((TextView) findViewById(R.id.formTitle)).setText(R.string.edit_ingredient);
            if (savedInstanceState == null) {
                loadIngredient();
            }
        }
    }

    private void loadIngredient() {
        try {
            Ingredient ingredient = database.getIngredient(ingredientId);
            if (ingredient == null) {
                showMessage(R.string.missing_ingredient);
                finish();
                return;
            }
            name.setText(ingredient.getName());
            quantity.setText(String.valueOf(ingredient.getQuantity()));
            String[] units = getResources().getStringArray(R.array.units);
            for (int index = 0; index < units.length; index++) {
                if (units[index].equals(ingredient.getUnit())) {
                    unit.setSelection(index);
                }
            }
        } catch (SQLiteException exception) {
            showMessage(R.string.load_failed);
            finish();
        }
    }

    private void saveIngredient() {
        String ingredientName = name.getText().toString().trim();
        if (ingredientName.isEmpty()) {
            name.setError(getString(R.string.name_error));
            name.requestFocus();
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(quantity.getText().toString().trim());
            if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0 || amount > 1000000000) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException exception) {
            quantity.setError(getString(R.string.quantity_error));
            quantity.requestFocus();
            return;
        }
        Ingredient ingredient = new Ingredient(ingredientId, ingredientName, amount, unit.getSelectedItem().toString());
        save.setEnabled(false);
        try {
            boolean saved;
            if (ingredientId == -1) {
                saved = database.addIngredient(ingredient) > 0;
            } else {
                saved = database.updateIngredient(ingredient);
            }
            if (saved) {
                showMessage(R.string.saved);
                finish();
            } else {
                save.setEnabled(true);
                showMessage(R.string.save_failed);
            }
        } catch (SQLiteException | IllegalArgumentException exception) {
            save.setEnabled(true);
            showMessage(R.string.save_failed);
        }
    }
}
