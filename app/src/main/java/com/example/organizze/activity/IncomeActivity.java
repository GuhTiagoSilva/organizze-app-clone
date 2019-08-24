package com.example.organizze.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.FirebaseConfiguration;
import com.example.organizze.helper.Base64Custom;
import com.example.organizze.helper.DateUtil;
import com.example.organizze.model.Movimentation;
import com.example.organizze.model.User;
import com.google.android.gms.common.data.DataBufferObserverSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class IncomeActivity extends AppCompatActivity {

    private EditText value, date, category, description;
    private Movimentation movimentation;
    private Double totalIncome;
    private FirebaseAuth userAuth = FirebaseConfiguration.getFirebaseAuthentication();
    private DatabaseReference firebaseReference = FirebaseConfiguration.getFirebaseDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        value = findViewById(R.id.textValue);
        date = findViewById(R.id.textDate);
        category = findViewById(R.id.textCategory);
        description = findViewById(R.id.textCategory);

        date.setText(DateUtil.getCurrentDate());
        getTotalIncome();

    }

    public void saveIncome(View view) {

        if (validateFields()) {
            String chosenDate = date.getText().toString();
            Double gotValue = Double.parseDouble(value.getText().toString());
            movimentation = new Movimentation();
            movimentation.setType("r");
            movimentation.setDate(chosenDate);
            movimentation.setDescription(description.getText().toString());
            movimentation.setCategory(category.getText().toString());
            movimentation.setValue(gotValue);

            Double updatedIncome = totalIncome + gotValue;
            updateIncome(updatedIncome);

            movimentation.save(chosenDate);
        }
    }

    public Boolean validateFields() {
        String textValue = value.getText().toString();
        String textDate = date.getText().toString();
        String textCategory = category.getText().toString();
        String textDescription = description.getText().toString();

        if (!textValue.isEmpty()) {
            if (!textDate.isEmpty()) {
                if (!textCategory.isEmpty()) {
                    if (!textDescription.isEmpty()) {
                        return true;
                    } else {
                        Toast.makeText(IncomeActivity.this, "Preencha os campos vazios", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                } else {
                    Toast.makeText(IncomeActivity.this, "Preencha os campos vazios", Toast.LENGTH_SHORT).show();
                    return false;
                }

            } else {
                Toast.makeText(IncomeActivity.this, "Preencha os campos vazios", Toast.LENGTH_SHORT).show();
                return false;
            }

        } else {
            Toast.makeText(IncomeActivity.this, "Preencha os campos vazios", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void getTotalIncome() {
        String userEmail = userAuth.getCurrentUser().getEmail();
        String userId = Base64Custom.encodeBase64(userEmail);
        DatabaseReference userRef = firebaseReference.child("usuarios").child(userId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                totalIncome = user.getTotalIncome();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void updateIncome(Double income) {
        String userEmail = userAuth.getCurrentUser().getEmail();
        String userId = Base64Custom.encodeBase64(userEmail);

        DatabaseReference userRef = firebaseReference.child("usuarios").child(userId);
        userRef.child("totalIncome").setValue(income);
    }


}
