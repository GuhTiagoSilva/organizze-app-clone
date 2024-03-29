package com.example.organizze.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseConfiguration {

    private static FirebaseAuth authentication;
    private static DatabaseReference database;


    public static FirebaseAuth getFirebaseAuthentication() {
        if (authentication == null)
            authentication = FirebaseAuth.getInstance();

        return authentication;
    }

    public static DatabaseReference getFirebaseDatabase() {
        if (database == null)
            database = FirebaseDatabase.getInstance().getReference();
        return database;
    }


}
