package com.example.pantrywise;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private Context context;
    private ArrayList<Ingredient> ingredients;
    private DatabaseHelper databaseHelper;

    public IngredientAdapter(
            Context context,
            ArrayList<Ingredient> ingredients,
            DatabaseHelper databaseHelper) {

        this.context = context;
        this.ingredients = ingredients;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.item_ingredient,
                        parent,
                        false
                );

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull IngredientViewHolder holder,
            int position) {

        Ingredient ingredient = ingredients.get(position);

        holder.txtName.setText(ingredient.name);

        holder.txtDetails.setText(
                ingredient.quantity + " " +
                        ingredient.unit
        );

        if (ingredient.expiryDate == null ||
                ingredient.expiryDate.isEmpty()) {

            holder.txtExpiry.setText(
                    "Expiry: Not provided"
            );

        } else {

            holder.txtExpiry.setText(
                    "Expiry: " +
                            ingredient.expiryDate
            );
        }

        holder.btnEdit.setOnClickListener(view -> {

            Intent intent = new Intent(
                    context,
                    EditIngredientActivity.class
            );

            intent.putExtra(
                    "ingredient_id",
                    ingredient.id
            );

            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(view -> {

            new AlertDialog.Builder(context)
                    .setTitle("Delete Ingredient")
                    .setMessage(
                            "Are you sure you want to delete " +
                                    ingredient.name + "?"
                    )
                    .setPositiveButton(
                            "Yes",
                            (dialog, which) -> {

                                boolean deleted =
                                        databaseHelper.deleteIngredient(
                                                ingredient.id
                                        );

                                if (deleted) {

                                    int currentPosition =
                                            holder.getAdapterPosition();

                                    if (currentPosition !=
                                            RecyclerView.NO_POSITION) {

                                        ingredients.remove(
                                                currentPosition
                                        );

                                        notifyItemRemoved(
                                                currentPosition
                                        );
                                    }

                                    Toast.makeText(
                                            context,
                                            "Ingredient deleted",
                                            Toast.LENGTH_SHORT
                                    ).show();

                                } else {

                                    Toast.makeText(
                                            context,
                                            "Could not delete ingredient",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                    )
                    .setNegativeButton(
                            "No",
                            null
                    )
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public static class IngredientViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtName;
        TextView txtDetails;
        TextView txtExpiry;
        Button btnEdit;
        Button btnDelete;

        public IngredientViewHolder(@NonNull View itemView) {

            super(itemView);

            txtName = itemView.findViewById(
                    R.id.txtIngredientName
            );

            txtDetails = itemView.findViewById(
                    R.id.txtIngredientDetails
            );

            txtExpiry = itemView.findViewById(
                    R.id.txtIngredientExpiry
            );

            btnEdit = itemView.findViewById(
                    R.id.btnEdit
            );

            btnDelete = itemView.findViewById(
                    R.id.btnDelete
            );
        }
    }
}