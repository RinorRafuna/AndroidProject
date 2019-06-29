package com.AndroidProjectFinal;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.AndroidProjectFinal.Common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;


// klasa per shfaqjen e detajeve te ushqimeve -- rating dialog listener perdoret per rating te ushqimit
public class FoodDetails extends AppCompatActivity implements RatingDialogListener
{
    TextView txtWriteReview, txtIngredients, txtDirections, txtPrep, txtDifficulty, txtHowMany;
    ImageView img_food;
    CollapsingToolbarLayout collapsingToolbarLayout;

    RatingBar ratingBar;
    Rating rating;
    String foodId="";
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    DatabaseReference tblRating;

    private VideoView video_food;
    private Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        // Firebase
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Food");
        tblRating = database.getReference("Rating");

        // init view
        txtIngredients = findViewById(R.id.txtIngredients);
        img_food = findViewById(R.id.img_food);
        txtDirections = findViewById(R.id.txtDirections);
        txtDifficulty = findViewById(R.id.txtDifficulty);
        txtPrep = findViewById(R.id.txtPrep);
        txtHowMany = findViewById(R.id.txtHowMany);
        ratingBar = findViewById(R.id.ratingBar);
        txtWriteReview = findViewById(R.id.txtWriteReview);


        video_food = findViewById(R.id.video_food);


        // kur ben scroll pamja behet collapsed dhe fotoja largohet por emri i ushqimit mbetet gjate tere pamjes
        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        // get food id from intent
        if(getIntent() != null)
        {
            foodId = getIntent().getStringExtra("FoodId");
        }
        if(!foodId.isEmpty())
        {
            //video_food.setVideoPath("android.resource://"+getPackageName()+"/"+R.raw.vegetarian_food);
            getDetailFood(foodId);
            getRatingFood(foodId);
        }

        txtWriteReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showRatingDialog();
            }
        });

    }
    // rating average funksioni
    private void getRatingFood(String foodId)
    {
        Query foodRating = tblRating.orderByChild("foodId").equalTo(foodId);
        foodRating.addValueEventListener(new ValueEventListener() {
            int count=0, sum=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Rating item = postSnapshot.getValue(Rating.class);
                    sum += Integer.parseInt(item.getRateValue());
                    count++;
                }
                if(count != 0)
                {
                    float average = sum / count;
                    ratingBar.setRating(average);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // dialogu kur klikon ne rate
    private void showRatingDialog()
    {
        new AppRatingDialog.Builder().setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not Good", "Quite Ok", "Very Good", "Excellent"))
                .setDefaultRating(1).setTitle("Rate this food")
                .setDescription("Please select some stars and give your feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your comment here...")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(FoodDetails.this).show();
    }


    // metoda per marrjen e te dhenave te ushqimeve
    private void getDetailFood(String foodId)
    {
        databaseReference.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                Food food = dataSnapshot.getValue(Food.class);


                // Set image
                Picasso.get().load(food.getImage()).into(img_food);
                collapsingToolbarLayout.setTitle(food.getName());
                txtIngredients.setText(food.getIngredients());
                txtDirections.setText(food.getDirections());
                txtPrep.setText(food.getPrep());
                txtHowMany.setText(food.getHowMany());
                txtDifficulty.setText(food.getDifficulty());



                MediaController mediaController = new MediaController(FoodDetails.this);
                mediaController.setAnchorView(video_food);
                video_food.setMediaController(mediaController);
                video_food.setKeepScreenOn(true);
                videoUri = Uri.parse(food.getVideo());
                video_food.setVideoURI(videoUri);
                video_food.start();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onNegativeButtonClicked()
    {

    }

    @Override
    public void onPositiveButtonClicked(int value, @NotNull String comments)
    {
        // get Rating and upload to firebase
        rating = new Rating(Common.currentUser.getUsername(), foodId,
                String.valueOf(value), comments);
        tblRating.child(Common.currentUser.getUsername()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(Common.currentUser.getUsername()).exists())
                {
                    // Remove old value
                    tblRating.child(Common.currentUser.getUsername()).removeValue();
                    // Update new value
                    tblRating.child(Common.currentUser.getUsername()).setValue(rating);
                }
                else
                {
                    // add new value
                    tblRating.child(Common.currentUser.getUsername()).setValue(rating);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
