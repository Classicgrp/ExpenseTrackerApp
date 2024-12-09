package com.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    private EditText Name, Email, Password, ConfirmPassword;
    private Button btnreg;
    private TextView mSignin;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        // Adjusting window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Back Button Action
        ImageButton lefticon = findViewById(R.id.arrow_back);
        lefticon.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));

        progressBar = findViewById(R.id.progressBar);

        // Set up registration process
        registration();
    }

    private void registration() {
        // Find views by ID
        Name = findViewById(R.id.name);
        Email = findViewById(R.id.email_signup);
        Password = findViewById(R.id.password_signup);
        ConfirmPassword = findViewById(R.id.confirm_password_signup);
        btnreg = findViewById(R.id.btn_signup);
        mSignin = findViewById(R.id.login_here);

        // Button click to register a new user
        btnreg.setOnClickListener(view -> {
            // Find views by ID
            TextInputLayout nameLayout = findViewById(R.id.nameLayout);
            TextInputLayout emailLayout = findViewById(R.id.emailLayout);
            TextInputLayout passwordLayout = findViewById(R.id.passwordLayout);
            TextInputLayout confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);

            String my_name = Name.getText().toString().trim();
            String my_email = Email.getText().toString().trim();
            String Pass = Password.getText().toString().trim();
            String confirmPass = ConfirmPassword.getText().toString().trim();

            // Validate inputs
            boolean isValid = true;

            if (TextUtils.isEmpty(my_name)) {
                nameLayout.setError("Name is required");
                isValid = false;
            } else {
                nameLayout.setError(null); // Clear error
            }

            if (TextUtils.isEmpty(my_email)) {
                emailLayout.setError("Email is required");
                isValid = false;
            } else {
                emailLayout.setError(null); // Clear error
            }

            if (TextUtils.isEmpty(Pass)) {
                passwordLayout.setError("Password is required");
                isValid = false;
            } else {
                passwordLayout.setError(null); // Clear error
            }

            if (TextUtils.isEmpty(confirmPass)) {
                confirmPasswordLayout.setError("Confirm Password is required");
                isValid = false;
            } else if (!Pass.equals(confirmPass)) {
                confirmPasswordLayout.setError("Passwords do not match");
                isValid = false;
            } else {
                confirmPasswordLayout.setError(null); // Clear error
            }

            if (!isValid) {
                return;
            }

            progressBar.setVisibility(View.VISIBLE); // Show progress bar

            // Register user with Firebase Authentication
            mAuth.createUserWithEmailAndPassword(my_email, Pass)
                    .addOnCompleteListener(this, task -> {
                        progressBar.setVisibility(View.GONE); // Hide progress bar

                        if (task.isSuccessful()) {
                            // Get the user ID
                            String userId = mAuth.getCurrentUser().getUid();

                            // Create a User object and store in Firestore
                            User user = new User(my_name, my_email);

                            db.collection("users").document(userId)
                                    .set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(getApplicationContext(), "Registration complete", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                        finish(); // Close current activity
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(getApplicationContext(), "Failed to save user data", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Redirect to SigninActivity if user already has an account
        mSignin.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), SigninActivity.class)));
    }

    // User class to store user data
    public static class User {
        private String name;
        private String email;

        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }

        // Default constructor for Firestore
        public User() {}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
