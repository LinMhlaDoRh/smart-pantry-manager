package com.example.smartpantry;

import java.util.List;
import java.util.Locale;

public class RecipeMatcher {
    private static String normalizeName(String value) {
        String name = value.trim().toLowerCase(Locale.ROOT).replaceAll("\\s+", " ");
        // Keep aliases explicit so names such as oats are not shortened incorrectly.
        switch (name) {
            case "tomatoes": return "tomato";
            case "potatoes": return "potato";
            case "eggs": return "egg";
            case "onions": return "onion";
            case "carrots": return "carrot";
            case "mushrooms": return "mushroom";
            case "peppers": return "pepper";
            case "beans": return "bean";
            case "cooked beans": return "cooked bean";
            case "bananas": return "banana";
            case "apples": return "apple";
            case "peas": return "pea";
            default: return name;
        }
    }

    public static String normalizeUnit(String value) {
        String unit = value.trim().toLowerCase(Locale.ROOT);
        switch (unit) {
            case "gram": case "grams": return "g";
            case "kilogram": case "kilograms": return "kg";
            case "millilitre": case "millilitres": case "milliliter": case "milliliters": return "ml";
            case "litre": case "litres": case "liter": case "liters": return "l";
            case "piece": case "pieces": case "unit": case "units": return "pcs";
            default: return unit;
        }
    }

    public static String unitGroup(String value) {
        switch (normalizeUnit(value)) {
            case "g": case "kg": return "mass";
            case "ml": case "l": return "volume";
            case "pcs": return "count";
            default: return "";
        }
    }

    private static double baseQuantity(Ingredient ingredient) {
        // Compare mass in grams and volume in millilitres.
        String unit = normalizeUnit(ingredient.getUnit());
        if (unit.equals("kg") || unit.equals("l")) {
            return ingredient.getQuantity() * 1000;
        }
        return ingredient.getQuantity();
    }

    private static boolean sameIngredient(Ingredient first, Ingredient second) {
        String group = unitGroup(first.getUnit());
        return !group.isEmpty()
                && group.equals(unitGroup(second.getUnit()))
                && normalizeName(first.getName()).equals(normalizeName(second.getName()));
    }

    private static boolean valid(Ingredient ingredient) {
        return ingredient.getName() != null && !ingredient.getName().trim().isEmpty()
                && !Double.isNaN(ingredient.getQuantity()) && !Double.isInfinite(ingredient.getQuantity()) && ingredient.getQuantity() > 0
                && ingredient.getUnit() != null && !unitGroup(ingredient.getUnit()).isEmpty();
    }

    public static boolean matches(Recipe recipe, List<Ingredient> pantry) {
        if (recipe.getIngredients().isEmpty()) {
            return false;
        }
        for (Ingredient required : recipe.getIngredients()) {
            double needed = 0;
            double available = 0;
            // Combine repeated requirements before comparing the available total.
            for (Ingredient ingredient : recipe.getIngredients()) {
                if (!valid(ingredient)) {
                    return false;
                }
                if (sameIngredient(required, ingredient)) {
                    needed += baseQuantity(ingredient);
                }
            }
            for (Ingredient ingredient : pantry) {
                if (valid(ingredient) && sameIngredient(required, ingredient)) {
                    available += baseQuantity(ingredient);
                }
            }
            if (available < needed) {
                // One shortage excludes the entire recipe.
                return false;
            }
        }
        return true;
    }
}
