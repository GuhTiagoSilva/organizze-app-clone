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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ExpenseActivity extends AppCompatActivity {
    private EditText date, category, value, description;
    private Movimentation movimentation;
    private DatabaseReference reference = FirebaseConfiguration.getFirebaseDatabase();
    private FirebaseAuth auth = FirebaseConfiguration.getFirebaseAuthentication();
    private Double totalExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        date = findViewById(R.id.etDate);
        category = findViewById(R.id.etCategory);
        value = findViewById(R.id.etValue);
        description = findViewById(R.id.etDescription);

        //fill the etDate with current date
        date.setText(DateUtil.getCurrentDate());

        getTotalExpense();
    }

    public void saveExpense(View view) {
        if (validateExpenseFields()) {
            movimentation = new Movimentation();
            String chosenDate = date.getText().toString();
            Double valueGenerated = Double.parseDouble(value.getText().toString());

            movimentation.setValue(valueGenerated);
            movimentation.setCategory(category.getText().toString());
            movimentation.setDescription(description.getText().toString());
            movimentation.setDate(chosenDate);
            movimentation.setType("d");

            Double updatedExpense = totalExpense + valueGenerated;
            updateExpense(updatedExpense);

            movimentation.save(chosenDate);
        }
    }
    public Boolean validateExpenseFields() {
        String textValue = value.getText().toString();
        String textCategory = category.getText().toString();
        String textDate = date.getText().toString();
        String textDescription = description.getText().toString();

        if (!textValue.isEmpty()) {
            if (!textCategory.isEmpty()) {
                if (!textDate.isEmpty()) {
                    if (!textDescription.isEmpty()) {
                        return true;
                    } else {
                        Toast.makeText(ExpenseActivity.this, "Valor não foi preenchido", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(ExpenseActivity.this, "Valor não foi preenchido", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(ExpenseActivity.this, "Valor não foi preenchido", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(ExpenseActivity.this, "Valor não foi preenchido", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public void getTotalExpense() {
        String userEmail = auth.getCurrentUser().getEmail();
        String userId = Base64Custom.encodeBase64(userEmail);
        DatabaseReference userRef = reference.child("usuarios").child(userId);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                totalExpense = user.getTotalExpense();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    public void updateExpense(Double expense) {
        String userEmail = auth.getCurrentUser().getEmail();
        String userId = Base64Custom.encodeBase64(userEmail);
        DatabaseReference userRef = reference.child("usuarios").child(userId);
        userRef.child("totalExpense").setValue(expense);
    }
}