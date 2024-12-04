package com.expensetracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class CategoryBottomSheetDialog extends BottomSheetDialogFragment {

    private List<String> categories;
    private OnCategorySelectedListener listener;

    public CategoryBottomSheetDialog(List<String> categories, OnCategorySelectedListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_category_bottom_sheet, container, false);

        // Populate the categories in the bottom sheet
        ViewGroup categoryContainer = view.findViewById(R.id.categoryContainer);
        for (String category : categories) {
            TextView tvCategory = new TextView(getContext());
            tvCategory.setText(category);
            tvCategory.setPadding(16, 16, 16, 16);
            tvCategory.setTextSize(16f);
            tvCategory.setOnClickListener(v -> {
                listener.onCategorySelected(category);
                dismiss();
            });
            categoryContainer.addView(tvCategory);
        }

        return view;
    }

    public interface OnCategorySelectedListener {
        void onCategorySelected(String category);
    }
}
