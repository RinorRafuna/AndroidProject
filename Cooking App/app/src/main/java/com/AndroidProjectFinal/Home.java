package com.AndroidProjectFinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.AndroidProjectFinal.Common.Category1;
import com.AndroidProjectFinal.Common.Common;
import com.AndroidProjectFinal.Common.ItemClickListener;
import com.AndroidProjectFinal.Common.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


import io.paperdb.Paper;

// kjo eshte klasa e cila perdoret per shfaqjen e kategorise se ushqimeve


// navigatinView e implementuar eshte e gjeneruar automatikisht nga lloji i activity te cilin e kemi krijuar ne rastin tone navigation drawer activity
public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    TextView txtFullName;
    private String currentUser;
    private String currentPassword;

    RecyclerView recycler_menu;
    // layoutmanager percakton poziten e te dhenave ne app
    RecyclerView.LayoutManager layoutManager;

    // FirebaseRecyclerAdapter lidh(bind) nje query ne recyclerview. Kur te dhenat shtohen, fshihen ose ndryshohen kote te dhena edhe ne app automatikisht ndryshohen ne kohe reale (real time)
    FirebaseRecyclerAdapter<Category1, MenuViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        // Init Firebase
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Category");


        // Init paper per ruajtjen e password dhe username
        Paper.init(this);




        // shfaqe pjesen te category food kur klikon te menuja kryesore dhe shfaqen te dhenat e userit, profile, signout
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Set Name for User ne meny te kategoria e ushqimeve
        View headerView = navigationView.getHeaderView(0);
        txtFullName = headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getFirstName() + " " + Common.currentUser.getLastName());

        // Load Menu

        recycler_menu = findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadMenu();


    }

    private void loadMenu()
    {
        adapter = new FirebaseRecyclerAdapter<Category1, MenuViewHolder>(Category1.class, R.layout.menu_item, MenuViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category1 model, int position)
            {
                viewHolder.txtMenuName.setText(model.getName());
                // picasso librari e cila perdoret per marrjen e fotove nga databaza
                Picasso.get().load(model.getImage())
                        .into(viewHolder.imageView);
                final Category1 clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick)
                    {
                        Intent intent = new Intent(Home.this, FoodList.class);
                        // Because CategoryId is key, so we just get key of this item
                        intent.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        recycler_menu.setAdapter(adapter);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        currentUser = getIntent().getExtras().get("current_user").toString();
        currentPassword = getIntent().getExtras().get("current_password").toString();

        if (id == R.id.nav_profile)
        {
            Intent intent = new Intent(Home.this, userProfile.class);
            intent.putExtra("current_user1", currentUser);
            intent.putExtra("current_password1", currentPassword);
            startActivity(intent);
        }
         else if (id == R.id.nav_signOut)
        {
            // delete remmber user and pass
            Paper.book().destroy();

            //Log out
            Intent intent = new Intent(Home.this, SignIn.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
