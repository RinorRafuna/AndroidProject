package com.AndroidProjectFinal;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.AndroidProjectFinal.Common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;


public class Welcome extends AppCompatActivity
{
    private Button btnIAmNew;
    private  Button btnIveBeenHere;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btnIAmNew = findViewById(R.id.btnIAmNew);
        btnIveBeenHere = findViewById(R.id.btnIveBeenHere);

        // init paper per rememberme
        Paper.init(this);



        final TextView txtWelcome = findViewById(R.id.txtWelcome);
        final Animation animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        Animation.AnimationListener animationListener = new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
            }
            @Override
            public void onAnimationEnd(Animation animation)
            {
                if(animation == animationFadeIn)
                {
                    // Start fade-out animation
                    txtWelcome.animate().translationYBy(-1000).setDuration(5000);
                    txtWelcome.startAnimation(animationFadeOut);

                }
                else if(animation == animationFadeOut)
                {
                    txtWelcome.animate().translationYBy(1000).setDuration(5000);
                    txtWelcome.startAnimation(animationFadeIn);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
            }
        };
        animationFadeIn.setAnimationListener(animationListener);
        animationFadeOut.setAnimationListener(animationListener);
        txtWelcome.startAnimation(animationFadeIn);

        btnIAmNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(getBaseContext(), SignUp.class);
                startActivity(intent);
            }
        });
        btnIveBeenHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                intent = new Intent(getBaseContext(), SignIn.class);
                startActivity(intent);
            }
        });

        // Check Remember me
        String user = Paper.book().read(Common.USER_KEY);
        String pass = Paper.book().read(Common.PASSWORD_KEY);

        if(user != null && pass != null)
        {
            if(!user.isEmpty() && !pass.isEmpty())
            {
                login(user, pass);
            }
        }

    }

    private void login(final String user, final String pass)
    {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("tblUsers");
        // AddListenerForSingleValueEvent eshte funksion i cili ndegjon te dhenat nje nga nje dhe ndihmon ne  detektimin e ndryshimeve ne te dhena ne nje path te caktuar
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            // datasnapshot eshte nje kopje e te dhenave te ruajtura ne firebase
            // metoda onDataChange kthen te dhenat si nje objekt datasnapshot dhe mund te perdoren per pune te ndryshme ato te dhena pastaj
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {


                try
                {
                    if(dataSnapshot.child(user).exists())
                    {
                        addUsers loginUser = dataSnapshot.child(user).getValue(addUsers.class);
                        loginUser.setUsername(user);
                        if(loginUser.getPassword().equals(pass))
                        {


                            Toast.makeText(Welcome.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                            intent = new Intent(getBaseContext(), Home.class);
                            Common.currentUser = loginUser;
                            intent.putExtra("current_user", loginUser.username);
                            intent.putExtra("current_password", pass);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(Welcome.this, "Wrong Password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(Welcome.this, "Username doesn't exists!", Toast.LENGTH_SHORT).show();
                }


            }
            // onCancelled ndihmon ne leximin e gabimeve gjate marrjes se te dhenave

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
