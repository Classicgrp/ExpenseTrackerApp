package com.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private Button signUpbtn, googleBtn;
    private TextView Signintxt;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        // Check if the user is already signed in
        if (mAuth.getCurrentUser() != null) {
            // If logged in, navigate to the HomeActivity and finish MainActivity
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish(); // Close MainActivity so the user can't go back to it
            return;
        }

        // Find views
        signUpbtn = findViewById(R.id.signup_btn);
        Signintxt = findViewById(R.id.signin);

        Button button = findViewById(R.id.signup_btn);
        button.setBackgroundResource(R.drawable.button_background_transparent);


        // Sign-up button click listener
        signUpbtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), SignupActivity.class)));

        // Create and set a SpannableString to the TextView for the "Already a member?" link
        String fullText = "Already a member? Login";
        SpannableString spannableString = new SpannableString(fullText);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(MainActivity.this, R.color.green)),
                fullText.indexOf("Login"),
                fullText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Signintxt.setText(spannableString);

        // Sign-in text click listener
        Signintxt.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), SigninActivity.class)));
    }
}
