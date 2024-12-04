package com.expensetracker;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private LinearLayout accountSettingsLayout;
    private LinearLayout resetDataLayout;
    private LinearLayout helpSupportLayout;
    private Spinner themeSpinner;
    private Spinner currencySpinner;
    private Spinner languageSpinner;
    private String selectedLanguage = "English"; // Default language

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize views
        accountSettingsLayout = findViewById(R.id.account_settings_layout);
        resetDataLayout = findViewById(R.id.reset_data_layout);
        helpSupportLayout = findViewById(R.id.help_support_layout);
        themeSpinner = findViewById(R.id.theme_spinner);
        currencySpinner = findViewById(R.id.currency_spinner);
        languageSpinner = findViewById(R.id.language_spinner);

        // Set up adapters for spinners
        setupSpinner(themeSpinner, R.array.theme_options);
        setupSpinner(currencySpinner, R.array.currency_options);
        setupLanguageSpinner();  // This ensures that the spinner reflects the current selected language

        // Set click listeners for other actions
        accountSettingsLayout.setOnClickListener(v -> navigateToProfile());
//        resetDataLayout.setOnClickListener(v -> deleteUserData());
        helpSupportLayout.setOnClickListener(v -> navigateToHelp());
    }

    private void setupSpinner(Spinner spinner, int arrayResId) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                arrayResId, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // Custom method to setup language spinner with previous language choice
    private void setupLanguageSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.language_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        // Set the current selected language in the spinner
        int position = adapter.getPosition(selectedLanguage); // find the position of the selected language
        languageSpinner.setSelection(position);

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (!selectedItem.equals(selectedLanguage)) {
                    selectedLanguage = selectedItem;  // Store the selected language
                    if (selectedItem.equals("English")) {
                        setLocale("en");
                    } else if (selectedItem.equals("Swahili")) {
                        setLocale("sw");
                    }
                    // Recreate the activity to apply the new language
                    recreate();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    // Method to change the locale based on language code
    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        // Update the application's configuration to use the new locale
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
    }

    // Method to navigate to profile activity
    private void navigateToProfile() {
        Intent intent = new Intent(SettingsActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    private void navigateToHelp() {
        Intent intent = new Intent(SettingsActivity.this, HelpActivity.class);
        startActivity(intent);
    }
}
