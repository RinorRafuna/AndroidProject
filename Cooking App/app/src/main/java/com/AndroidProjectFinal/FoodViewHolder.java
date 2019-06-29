package com.AndroidProjectFinal;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.AndroidProjectFinal.Common.ItemClickListener;

// Kjo klase trashegon RecyclerView dhe implementon View on click listener dhe perdoret ne FoodList.class per shfaqjen e permbajtjes se ushqimeve ne baze te kategorise se tyre

// recyclerView perdoret per shfaqjen e te dhenave ne nje scrolling liste dhe perdoret per sasi te madhe te te dhenave ose te dhenat qe ndryshojne shpesh

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView food_name;
    public ImageView food_image;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodViewHolder(@NonNull View itemView)
    {
        super(itemView);
        food_image = itemView.findViewById(R.id.food_image);
        food_name = itemView.findViewById(R.id.food_name);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}