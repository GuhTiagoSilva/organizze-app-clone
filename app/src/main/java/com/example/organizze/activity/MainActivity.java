package com.example.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.organizze.R;
import com.example.organizze.activity.LoginActivity;
import com.example.organizze.activity.SignInActivity;
import com.example.organizze.config.FirebaseConfiguration;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class MainActivity extends IntroActivity {

    private FirebaseAuth authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonNextVisible(false);
        setButtonBackVisible(false);

        addSlide(new FragmentSlide.Builder().background(android.R.color.white).fragment(R.layout.intro_1).build());
        addSlide(new FragmentSlide.Builder().background(android.R.color.white).fragment(R.layout.intro_2).build());
        addSlide(new FragmentSlide.Builder().background(android.R.color.white).fragment(R.layout.intro_3).build());
        addSlide(new FragmentSlide.Builder().background(android.R.color.white).fragment(R.layout.intro_4).build());
        addSlide(new FragmentSlide.Builder().background(android.R.color.white).fragment(R.layout.intro_sign_in).build());



    }

    @Override
    protected void onStart() {
        verifyLoggedUser();
        super.onStart();
    }

    public void enter(View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void createAccount(View view){
        startActivity(new Intent(this, SignInActivity.class));
    }

    public void verifyLoggedUser(){
        authentication = FirebaseConfiguration.getFirebaseAuthentication();
        //authentication.signOut();
        //authentication.signOut();
        if(authentication.getCurrentUser()!=null){
            openMainPage();
        }
    }

    public void openMainPage(){
        startActivity(new Intent(this, PrincipalActivity.class));
    }
}
