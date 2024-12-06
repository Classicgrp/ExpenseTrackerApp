package com.expensetracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.expensetracker.databinding.FragmentMonthBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MonthFragment extends Fragment {

    private FragmentMonthBinding binding;
    private FirebaseFirestore db;
    private CollectionReference expensesRef, incomeRef;

    private PieChart expenseChart, incomeChart;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firestore
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid();
            db = FirebaseFirestore.getInstance();
            expensesRef = db.collection("users").document(userId).collection("expenses");
            incomeRef = db.collection("users").document(userId).collection("income");
        } else {
            Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMonthBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize charts
        expenseChart = binding.expensePieChart;
        incomeChart = binding.incomePieChart;

        // Fetch and populate charts for the month
        fetchMonthData();

        return view;
    }

    private void fetchMonthData() {
        // Get the current month (you can adjust this for a specific month if needed)
        long currentTime = System.currentTimeMillis();
        Date startDate = new Date(currentTime - (currentTime % (30L * 24 * 60 * 60 * 1000))); // Start of the current month
        Date endDate = new Date(currentTime + (30L * 24 * 60 * 60 * 1000)); // End of the current month

        // Fetch expenses and income for the selected month
        fetchTransactions(expensesRef, startDate, endDate, expenseChart);
        fetchTransactions(incomeRef, startDate, endDate, incomeChart);
    }

    private void fetchTransactions(CollectionReference ref, Date startDate, Date endDate, PieChart chart) {
        ref.whereGreaterThanOrEqualTo("date", startDate)
                .whereLessThan("date", endDate)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        List<PieEntry> entries = new ArrayList<>();

                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            FinancialTransaction transaction = document.toObject(FinancialTransaction.class);
                            if (transaction != null) {
                                entries.add(new PieEntry(transaction.getAmount().floatValue(), transaction.getCategory()));
                            }
                        }

                        // Create a dataset and set it to the chart
                        PieDataSet dataSet = new PieDataSet(entries, "Transactions");
                        PieData data = new PieData(dataSet);
                        chart.setData(data);
                        chart.invalidate(); // Refresh the chart
                    } else {
                        Toast.makeText(getContext(), "No transactions found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to fetch transactions", Toast.LENGTH_SHORT).show();
                });
    }
}
