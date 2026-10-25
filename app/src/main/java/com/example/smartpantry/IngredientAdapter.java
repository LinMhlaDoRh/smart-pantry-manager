package com.example.smartpantry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    public interface IngredientListener {
        void onEdit(Ingredient ingredient);
        void onDelete(Ingredient ingredient);
    }

    private final ArrayList<Ingredient> ingredients = new ArrayList<>();
    private final IngredientListener listener;

    public IngredientAdapter(IngredientListener listener) {
        this.listener = listener;
    }

    public void setIngredients(ArrayList<Ingredient> values) {
        ingredients.clear();
        ingredients.addAll(values);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ingredient, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.name.setText(ingredient.getName());
        holder.quantity.setText(BaseActivity.quantityText(ingredient));
        holder.edit.setContentDescription(holder.itemView.getContext().getString(R.string.edit_named, ingredient.getName()));
        holder.delete.setContentDescription(holder.itemView.getContext().getString(R.string.delete_named, ingredient.getName()));
        holder.edit.setOnClickListener(view -> listener.onEdit(ingredient));
        holder.delete.setOnClickListener(view -> listener.onDelete(ingredient));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView quantity;
        final Button edit;
        final Button delete;

        public IngredientViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.ingredientName);
            quantity = view.findViewById(R.id.ingredientQuantity);
            edit = view.findViewById(R.id.editIngredient);
            delete = view.findViewById(R.id.deleteIngredient);
        }
    }
}
