package com.example.organizze.model;

import com.example.organizze.config.FirebaseConfiguration;
import com.example.organizze.helper.Base64Custom;
import com.example.organizze.helper.DateUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Movimentation {

    private String key;
    private String category;
    private String date;
    private String description;
    private String type;
    private Double value;

    public Movimentation() {
    }

    public void save(String chosenDate){
        FirebaseAuth auth = FirebaseConfiguration.getFirebaseAuthentication();

        DatabaseReference reference = FirebaseConfiguration.getFirebaseDatabase();
        reference.child("movimentacao").child(Base64Custom.encodeBase64(auth.getCurrentUser().getEmail())).child(DateUtil.monthYearChosenDate(chosenDate)).push().setValue(this);



    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
