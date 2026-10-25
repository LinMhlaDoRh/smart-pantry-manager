package com.example.smartpantry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import java.text.DecimalFormat;

public abstract class BaseActivity extends AppCompatActivity {
    protected PantryDatabase database;
    protected SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new PantryDatabase(this);
        preferences = getSharedPreferences("PantrySettings", MODE_PRIVATE);
    }

    protected void prepareScreen(int layout) {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(layout);
        View screen = findViewById(R.id.screen);
        int left = screen.getPaddingLeft();
        int top = screen.getPaddingTop();
        int right = screen.getPaddingRight();
        int bottom = screen.getPaddingBottom();
        ViewCompat.setOnApplyWindowInsetsListener(screen, (view, windowInsets) -> {
            Insets bars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets keyboard = windowInsets.getInsets(WindowInsetsCompat.Type.ime());
            view.setPadding(left + bars.left, top + bars.top, right + bars.right,
                    bottom + Math.max(bars.bottom, keyboard.bottom));
            return windowInsets;
        });
        ViewCompat.requestApplyInsets(screen);
    }

    protected void initNavigation(Class<?> current) {
        initNavigationButton(R.id.navPantry, PantryActivity.class, current);
        initNavigationButton(R.id.navRecipes, RecipesActivity.class, current);
        initNavigationButton(R.id.navSettings, SettingsActivity.class, current);
    }

    private void initNavigationButton(int id, Class<?> destination, Class<?> current) {
        Button button = findViewById(id);
        boolean selected = destination == current;
        button.setSelected(selected);
        button.setBackgroundResource(selected ? R.drawable.navigation_selected : android.R.color.transparent);
        button.setOnClickListener(view -> {
            if (!selected) {
                Intent intent = new Intent(this, destination);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                if (!(this instanceof PantryActivity)) {
                    finish();
                }
            }
        });
    }

    protected void showMessage(int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public static String quantityText(Ingredient ingredient) {
        return new DecimalFormat("0.##########").format(ingredient.getQuantity()) + " " + ingredient.getUnit();
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
