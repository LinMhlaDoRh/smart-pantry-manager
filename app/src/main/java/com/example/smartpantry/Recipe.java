package com.example.smartpantry;

import java.util.ArrayList;

public class Recipe {
    private final long id;
    private final String name;
    private final String method;
    private final ArrayList<Ingredient> ingredients;

    public Recipe(long id, String name, String method, ArrayList<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.method = method;
        this.ingredients = ingredients;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMethod() {
        return method;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }
}
