package com.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AccountsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private LinearLayout accountsListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        accountsListLayout = findViewById(R.id.accounts_list_layout);

        // Initialize the back button and set up a click listener
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());

        // Initialize the Add Account button and set up a click listener
        findViewById(R.id.add_account_btn).setOnClickListener(v -> {
            // Navigate to AddAccountActivity
            Intent intent = new Intent(AccountsActivity.this, AddAccountActivity.class);
            startActivity(intent);
        });

        // Initialize the Add Transaction button and set up a click listener
        findViewById(R.id.add_transaction_btn).setOnClickListener(v -> {
            // Navigate to AddTransactionActivity
            Intent intent = new Intent(AccountsActivity.this, AddTransactionActivity.class);
            startActivity(intent);
        });

        // Load accounts from Firestore
        loadAccounts();
    }

    private void loadAccounts() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users").document(userId).collection("accounts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Account account = document.toObject(Account.class);
                            addAccountToView(account);
                        }
                    } else {
                        Toast.makeText(AccountsActivity.this, "Error getting accounts", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addAccountToView(Account account) {
        // Create a new LinearLayout for each account
        LinearLayout accountLayout = new LinearLayout(this);
        accountLayout.setOrientation(LinearLayout.HORIZONTAL);
        accountLayout.setPadding(8, 8, 8, 8);
        accountLayout.setBackgroundColor(getResources().getColor(R.color.white));
        accountLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        // Create a TextView for account name
        TextView accountNameTextView = new TextView(this);
        accountNameTextView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1));
        accountNameTextView.setText(account.getAccountName());
        accountNameTextView.setTextColor(getResources().getColor(R.color.black));
        accountNameTextView.setTextSize(16);

        // Create a TextView for account balance
        TextView accountBalanceTextView = new TextView(this);
        accountBalanceTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        accountBalanceTextView.setText(account.getInitialBalance());
        accountBalanceTextView.setTextColor(getResources().getColor(R.color.black));
        accountBalanceTextView.setTextSize(16);

        // Add TextViews to LinearLayout
        accountLayout.addView(accountNameTextView);
        accountLayout.addView(accountBalanceTextView);

        // Add account layout to the main layout
        accountsListLayout.addView(accountLayout);
    }
}
