package com.example.smartpantry;

import android.os.Bundle;
import android.widget.RadioGroup;

public class SettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareScreen(R.layout.activity_settings);
        initNavigation(SettingsActivity.class);
        RadioGroup sortOrder = findViewById(R.id.sortOrder);
        boolean descending = preferences.getBoolean("sort_descending", false);
        sortOrder.check(descending ? R.id.sortDescending : R.id.sortAscending);
        sortOrder.setOnCheckedChangeListener((group, checkedId) ->
                preferences.edit().putBoolean("sort_descending", checkedId == R.id.sortDescending).apply());
    }
}
