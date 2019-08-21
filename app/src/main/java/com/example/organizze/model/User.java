package com.example.organizze.model;

import com.example.organizze.config.FirebaseConfiguration;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

public class User {


    private String userId;
    private String name;
    private String email;
    private String password;
    private Double totalIncome = 0.00;
    private Double totalExpense = 0.00;


    public User() {

    }

    public void save() {
        DatabaseReference reference = FirebaseConfiguration.getFirebaseDatabase();
        reference.child("usuarios").child(this.userId).setValue(this);
    }

    @Exclude
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public Double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(Double totalExpense) {
        this.totalExpense = totalExpense;
    }
}
