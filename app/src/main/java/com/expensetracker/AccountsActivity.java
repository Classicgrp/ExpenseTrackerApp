package com.expensetracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class AccountsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);

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
    }
}
