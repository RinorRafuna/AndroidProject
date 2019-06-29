package com.AndroidProjectFinal.Common;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.AndroidProjectFinal.R;

// Kjo klase trashegon RecyclerView dhe implementon View on click listener dhe perdoret ne Home.class per shfaqjen e permbajtjes se kategorive te ushqimeve

// recyclerView perdoret per shfaqjen e te dhenave ne nje scrolling liste dhe perdoret per sasi te madhe te te dhenave ose te dhenat qe ndryshojne shpesh

// cardview perdoret per shfaqjen e nje grumbullli te te dhenave ne nje scrolling liste

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView txtMenuName;
    public ImageView imageView;

    private ItemClickListener itemClickListener;

    public MenuViewHolder(@NonNull View itemView)
    {
        super(itemView);

        txtMenuName = itemView.findViewById(R.id.menu_name);
        imageView = itemView.findViewById(R.id.menu_image);

        itemView.setOnClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view)
    {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
