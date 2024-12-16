package com.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    private Button signUpbtn;
    private TextView Signintxt;
    private LinearLayout googleButton;
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        // Check if the user is already signed in
        if (mAuth.getCurrentUser() != null) {
            // Navigate to the HomeActivity
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish(); // Close MainActivity
            return;
        }

        // Initialize Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Find views
        signUpbtn = findViewById(R.id.signup_btn);
        Signintxt = findViewById(R.id.signin);
        googleButton = findViewById(R.id.google_btn);

        // Google button click listener
        googleButton.setOnClickListener(view -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        // Sign-up button click listener
        signUpbtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), SignupActivity.class)));

        // Create and set a SpannableString for the "Already a member?" link
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

    // Handle sign-in results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    Log.d("GoogleSignIn", "firebaseAuthWithGoogle: " + account.getIdToken());
                    firebaseAuthWithGoogle(account.getIdToken());
                }
            } catch (Exception e) {
                Log.w("GoogleSignIn", "Google sign-in failed", e);
            }
        }
    }

    // Authenticate with Firebase
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-in success
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Log.d("GoogleSignIn", "Sign-in successful: " + user.getDisplayName());
                            // Navigate to the HomeActivity
                            startActivity(new Intent(MainActivity.this, HomeActivity.class));
                            finish();
                        }
                    } else {
                        // Sign-in failed
                        Log.w("GoogleSignIn", "Sign-in with Firebase failed", task.getException());
                    }
                });
    }
}
