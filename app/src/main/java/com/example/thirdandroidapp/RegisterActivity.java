package com.example.thirdandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    Button btnSignup, btnLogin;
    ImageView imarrow;

    EditText etName, et1Phone,et1Email, et1Password, et2Password;
    String name,Phone, email, password, confirmPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button btnSignup = findViewById(R.id.btnSignup);

        //Initialize widget
        imarrow = findViewById(R.id.arrow);
        etName = findViewById(R.id.etName);
        et1Email = findViewById(R.id.et1Email);
        et1Phone=findViewById(R.id.et1Phone);
        et1Password = findViewById(R.id.et1Password);

        et2Password = findViewById(R.id.et2Password);
        progressBar = findViewById(R.id.progressBar);

        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);


        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();

        });

        imarrow.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btnSignup.setOnClickListener(v -> {
            name = etName.getText().toString();
            Phone=et1Phone.getText().toString();
            email = et1Email.getText().toString();
            password = et1Password.getText().toString();
            confirmPassword = et2Password.getText().toString();

            String regex = "^(.+)@(.+)$";
            //Compile regular expression to get the pattern
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(email);

            if (TextUtils.isEmpty(name) || name.length() < 6) {
                etName.setError("Enter valid input");
            } else if (TextUtils.isEmpty(Phone)||Phone.length()<13){
                et1Phone.setError("Enter valid Phone");
            } else if (TextUtils.isEmpty(email) || !pattern.matcher(email).matches()) {
                et1Email.setError("Enter valid email");
            } else if (TextUtils.isEmpty(password) || password.length() < 6) {
                et1Password.setError("Enter valid password");
            } else if (TextUtils.isEmpty(confirmPassword) || !confirmPassword.equals(password)) {
                et2Password.setError("Password not match");
            } else {
                progressBar.setVisibility(View.VISIBLE);
                registerUser();
            }
        });

    }

    private void registerUser() {
        // Write a message to the database

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration was successful
                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            // You can navigate to the user's profile or another activity here

                            String userId;
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser != null) {
                                userId = currentUser.getUid();
                                // Now, userId contains the unique identifier for the currently signed-in user.
                            } else {
                                // There is no user signed in. Handle this case as needed.
                                Toast.makeText(RegisterActivity.this, "No user id found", Toast.LENGTH_SHORT).show();
                                return;
                            }


                            myRef.child(userId).child("Name").setValue(name);
                            myRef.child(userId).child("Phone").setValue(Phone);
                            myRef.child(userId).child("Email").setValue(email);
                            myRef.child(userId).child("Password").setValue(password);


                            // Read from the database
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Toast.makeText(RegisterActivity.this, "Account create successful ☺", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Failed to read value
                                    Log.w("TAG", "Failed to read value.", error.toException());
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                        } else {
                            // Registration failed
                            String message = task.getException().getMessage();
                            Toast.makeText(RegisterActivity.this, "Registration failed. " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
