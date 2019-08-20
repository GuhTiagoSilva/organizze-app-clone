package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.FirebaseConfiguration;
import com.example.organizze.helper.Base64Custom;
import com.example.organizze.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class SignInActivity extends AppCompatActivity {

    private EditText name, email, password;
    private Button btSignIn;

    private FirebaseAuth userAuthentication;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        name = findViewById(R.id.etName);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        btSignIn = findViewById(R.id.btSignIn);

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textName = name.getText().toString();
                String textEmail = email.getText().toString();
                String textPassword = password.getText().toString();

                //verify edit texts
                if (!textName.isEmpty()) {
                    if (!textEmail.isEmpty()) {
                        if (!textPassword.isEmpty()) {
                            user = new User();
                            user.setName(textName);
                            user.setEmail(textEmail);
                            user.setPassword(textPassword);
                            createUser();
                        }
                    }
                } else {
                    Toast.makeText(SignInActivity.this, "Preencha os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void createUser() {
        userAuthentication = FirebaseConfiguration.getFirebaseAuthentication();
        userAuthentication.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userId = Base64Custom.encodeBase64(user.getEmail());
                    user.setUserId(userId);
                    user.save();
                    finish();
                } else {
                    String exception = "";

                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        exception = "Digite uma senha mais forte";

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        exception = "Digite um email válido";

                    } catch (FirebaseAuthUserCollisionException e) {
                        exception = "Essa conta já foi cadastrada";
                    } catch (Exception e) {
                        exception = "Erro ao cadastrar usuário" + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(SignInActivity.this, exception, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}