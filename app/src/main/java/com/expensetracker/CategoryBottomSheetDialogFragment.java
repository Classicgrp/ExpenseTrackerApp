package com.expensetracker;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class CategoryBottomSheetDialogFragment extends BottomSheetDialogFragment {}
//
//    private RecyclerView recyclerView;
//    private ExpenseCategoriesAdapter adapter;
//    private List<ExpenseCategory> expenseCategories;
//
//    public static CategoryBottomSheetDialogFragment newInstance(ArrayList<ExpenseCategory> categories) {
//        CategoryBottomSheetDialogFragment fragment = new CategoryBottomSheetDialogFragment();
//        Bundle args = new Bundle();
//        args.putSerializable("categories", categories);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_category_bottom_sheet, container, false);
//
//        recyclerView = view.findViewById(R.id.recycler_view_expense_categories);
//        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // 3 columns
//
//        if (getArguments() != null) {
//            expenseCategories = (ArrayList<ExpenseCategory>) getArguments().getSerializable("categories");
//        }
//
//        // Add a dummy item for the add icon
//        expenseCategories.add(new ExpenseCategory("Add", R.drawable.add_icon));
//
//        adapter = new ExpenseCategoriesAdapter(expenseCategories);
//        recyclerView.setAdapter(adapter);
//
//        // Set click listener on the add category item
//        adapter.setOnItemClickListener(position -> {
//            if (expenseCategories.get(position).getName().equals("Add")) {
//                // Start AddCategoryActivity
//                Intent intent = new Intent(getActivity(), AddCategoryActivity.class);
//                startActivity(intent);
//            } else {
//                // Handle category selection (pass selected category back to activity/fragment)
//                dismiss();
//            }
//        });
//
//        return view;
//    }
//}
