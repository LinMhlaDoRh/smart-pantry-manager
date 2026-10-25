package com.example.smartpantry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    public interface RecipeListener {
        void onSelect(Recipe recipe);
    }

    private final ArrayList<Recipe> recipes = new ArrayList<>();
    private final RecipeListener listener;

    public RecipeAdapter(RecipeListener listener) {
        this.listener = listener;
    }

    public void setRecipes(ArrayList<Recipe> values) {
        recipes.clear();
        recipes.addAll(values);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.name.setText(recipe.getName());
        holder.summary.setText(holder.itemView.getResources().getQuantityString(
                R.plurals.recipe_ingredients_count, recipe.getIngredients().size(), recipe.getIngredients().size()));
        holder.itemView.setOnClickListener(view -> listener.onSelect(recipe));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView summary;

        public RecipeViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.recipeName);
            summary = view.findViewById(R.id.recipeSummary);
        }
    }
}
