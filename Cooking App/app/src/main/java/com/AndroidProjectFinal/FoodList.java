package com.AndroidProjectFinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.AndroidProjectFinal.Common.ItemClickListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


// kjo klase perdoret per shfaqjen e ushqimeve ne baze te kategorise se caktuar te zgjedhur
public class FoodList extends AppCompatActivity
{
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    String categoryId="";
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        // Firebase
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Food");
        btn = findViewById(R.id.btn1);
        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Get intent here
        if(getIntent() != null)
        {
            categoryId = getIntent().getStringExtra("CategoryId");
            //categoryId = getIntent().getExtras().get("CategoryId").toString();
        }
        if(!categoryId.isEmpty() && categoryId != null)
        {
            loadFoodList(categoryId);
        }

    }


    // metoda loadFoodist perdoret per ruajtjen e ushqimeve te cilat shfaqen

    private void loadFoodList(String categoryId)
    {

        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>
                (Food.class, R.layout.food_item, FoodViewHolder.class,
                        databaseReference.orderByChild("CategoryId").equalTo(categoryId))
        {

            @Override
            // metode e cila perdoret per te populluar me te dhena recycler view
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {

                viewHolder.food_name.setText(model.getName());
                Picasso.get().load(model.getImage()).into(viewHolder.food_image);

                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        // start new activity
                        Intent intent = new Intent(FoodList.this, FoodDetails.class);
                        intent.putExtra("FoodId", adapter.getRef(position).getKey()); // send food id to new activity
                        startActivity(intent);
                  }
                });

            }
        };
       recyclerView.setAdapter(adapter);

    }

}
