package com.example.organizze.config;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseConfiguration {

    private static FirebaseAuth authentication;

    public static FirebaseAuth getFirebaseAuthentication(){
        if(authentication==null)
            authentication = FirebaseAuth.getInstance();

        return authentication;
    }



}
