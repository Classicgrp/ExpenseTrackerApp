package com.expensetracker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class AddExpenseFragment extends Fragment {

    private EditText editTextAmount;
    private ImageView iconCalendar;
    private ImageView iconClock;
    private EditText editTextDescription;
    private TextView textSelectedDate;
    private TextView textSelectedTime;
    private String selectedDate;
    private String selectedTime;
    private TextView selectCategory;
    private TextView selectedCategoryTextView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        editTextAmount = view.findViewById(R.id.edit_text_amount);
        iconCalendar = view.findViewById(R.id.icon_calendar);
        iconClock = view.findViewById(R.id.icon_clock);
        editTextDescription = view.findViewById(R.id.edit_text_description);
        textSelectedDate = view.findViewById(R.id.text_selected_date);
        textSelectedTime = view.findViewById(R.id.text_selected_time);

        selectCategory = view.findViewById(R.id.text_category);
        selectedCategoryTextView = view.findViewById(R.id.selected_category_text_view);

        Button btnSaveExpense = view.findViewById(R.id.save_expense);

        // Set the default date and time
        Calendar calendar = Calendar.getInstance();
        selectedDate = calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                (calendar.get(Calendar.MONTH) + 1) + "/" +
                calendar.get(Calendar.YEAR);
        selectedTime = calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                (calendar.get(Calendar.MINUTE) < 10 ? "0" + calendar.get(Calendar.MINUTE) : calendar.get(Calendar.MINUTE));
        textSelectedDate.setText(selectedDate);
        textSelectedTime.setText(selectedTime);

        // Set up DatePicker
        iconCalendar.setOnClickListener(v -> {
            DatePickerDialog datePicker = new DatePickerDialog(getContext(),
                    (view1, year, month, dayOfMonth) -> {
                        selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        textSelectedDate.setText(selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePicker.show();
        });

        // Set up TimePicker
        iconClock.setOnClickListener(v -> {
            TimePickerDialog timePicker = new TimePickerDialog(getContext(),
                    (view12, hourOfDay, minute) -> {
                        selectedTime = hourOfDay + ":" + (minute < 10 ? "0" + minute : minute);
                        textSelectedTime.setText(selectedTime);
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true);
            timePicker.show();
        });

        // Set up Category Picker Dialog
        String[] categories = {
                "Bills and Utilities", "Education", "Medical", "Rent", "Taxes", "Insurance",
                "Food and Dining", "Shopping", "Investments", "Personal Care", "Traveling",
                "Household", "Entertainment", "Sports", "Presents", "General", "Others"
        };
        selectCategory.setOnClickListener(v -> {
            // Show the bottom sheet dialog when the TextView is clicked
            showCategoryBottomSheet();
        });


        // Save expense to Firestore
        btnSaveExpense.setOnClickListener(v -> {
            String amountString = editTextAmount.getText().toString();
            String description = editTextDescription.getText().toString();
            String category = selectedCategoryTextView.getText().toString();

            // Ensure all fields are filled and a valid category is selected
            if (amountString.isEmpty() || selectedDate == null || selectedTime == null || category.isEmpty() || category.equals("Select Category")) {
                Toast.makeText(getContext(), "Please fill in all fields, including a valid category", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    // Convert amount to double
                    double amount = Double.parseDouble(amountString);

                    // Get the current user ID
                    String userId = mAuth.getCurrentUser().getUid();

                    // Parse the selectedDate and selectedTime into a Calendar object
                    String[] dateParts = selectedDate.split("/"); // Format: dd/MM/yyyy
                    String[] timeParts = selectedTime.split(":"); // Format: HH:mm

                    int day = Integer.parseInt(dateParts[0]);
                    int month = Integer.parseInt(dateParts[1]) - 1;  // Calendar months are 0-indexed
                    int year = Integer.parseInt(dateParts[2]);

                    int hour = Integer.parseInt(timeParts[0]);
                    int minute = Integer.parseInt(timeParts[1]);

                    // Create a Calendar instance for the selected date and time
                    Calendar calendarForTimestamp = Calendar.getInstance();
                    calendarForTimestamp.set(year, month, day, hour, minute, 0); // Set date and time to the selected values
                    calendarForTimestamp.set(Calendar.MILLISECOND, 0); // Set milliseconds to 0

                    Date selectedDateTime = calendarForTimestamp.getTime();

                    // Generate a unique ID for the document
                    String id = UUID.randomUUID().toString();

                    // Create the Expense object with the selected date and time
                    Expense expense = new Expense(id, amount, description, category, selectedDateTime, "expense");

                    // Save expense under the user's expenses subcollection
                    db.collection("users")
                            .document(userId)
                            .collection("expenses")
                            .document(id)
                            .set(expense)
                            .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Expense saved", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Error saving expense: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Invalid amount entered. Please enter a valid number.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void showCategoryBottomSheet() {
        BottomSheetCategoryDialog bottomSheet = new BottomSheetCategoryDialog();
        bottomSheet.setOnCategorySelectedListener(category -> {
            // When a category is selected, update the TextView with the category name
            selectedCategoryTextView.setText(category.getName());
        });

        // Show the bottom sheet dialog
        bottomSheet.show(getChildFragmentManager(), bottomSheet.getTag());
    }

}

