package com.expensetracker;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;


public class AddAccountActivity extends AppCompatActivity {

    private EditText accountNameEditText;
    private EditText initialBalanceEditText;
    private Spinner accountTypeSpinner;
    private Button saveAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        // Initialize UI components
        accountNameEditText = findViewById(R.id.account_name_edit_text);
        initialBalanceEditText = findViewById(R.id.initial_balance_edit_text);
        accountTypeSpinner = findViewById(R.id.account_type_spinner);
        saveAccountButton = findViewById(R.id.save_account_button);

        // Set OnClickListener for the save button
        saveAccountButton.setOnClickListener(v -> saveAccount());

        // Initialize the back button and set up a click listener
        findViewById(R.id.back_button).setOnClickListener(v -> onBackPressed());
    }

    private void saveAccount() {
        // Implement the logic to save the account details
        String accountName = accountNameEditText.getText().toString();
        String initialBalance = initialBalanceEditText.getText().toString();
        String accountType = accountTypeSpinner.getSelectedItem().toString();

        // Add saving logic here (e.g., save to database)

        // Go back to previous activity after saving
        onBackPressed();
    }
}
