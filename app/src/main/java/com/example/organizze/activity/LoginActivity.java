package com.example.organizze.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.organizze.R;
import com.example.organizze.config.FirebaseConfiguration;
import com.example.organizze.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button btLogin;
    private User user;
    private FirebaseAuth authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.etEmailLogin);
        password = findViewById(R.id.etPasswordLogin);
        btLogin = findViewById(R.id.btLogin);


        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = email.getText().toString();
                String textPassword = password.getText().toString();

                if (!textEmail.isEmpty() && !textPassword.isEmpty()) {
                    user = new User();
                    user.setEmail(textEmail);
                    user.setPassword(textPassword);
                    validateLogin();

                } else {
                    Toast.makeText(LoginActivity.this, "Preencha os campos ", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void validateLogin() {
        authentication = FirebaseConfiguration.getFirebaseAuthentication();
        authentication.signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    openMainPage();
                } else {
                    String exception = "";

                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e) {
                        exception = "Usuário não está cadastrado";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        exception = "Email e senha não correspondem ao usuário cadastrado";
                    } catch (Exception e) {
                        exception = "Erro ao logar: " + e.getMessage();
                        e.printStackTrace();
                    }


                    Toast.makeText(LoginActivity.this, exception, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openMainPage(){
        startActivity(new Intent(this, PrincipalActivity.class));
    }


}
