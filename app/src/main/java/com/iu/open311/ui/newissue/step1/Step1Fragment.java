package com.iu.open311.ui.newissue.step1;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.iu.open311.R;
import com.iu.open311.common.RecyclerViewTouchListener;
import com.iu.open311.database.Database;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Step1Fragment extends Fragment implements Step {
    private RecyclerView recyclerView;
    private Step1EntryAdapter entryAdapter;
    private Step1ViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        getActivity().setTitle(R.string.new_issue_step1);
        View view = inflater.inflate(R.layout.fragment_new_issue_1, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        entryAdapter = new Step1EntryAdapter(new ArrayList<>(), view.getContext());
        recyclerView.setAdapter(entryAdapter);

        viewModel = new ViewModelProvider(this,
                new Step1ViewModelFactory(Database.getInstance(getContext()))
        ).get(Step1ViewModel.class);

        loadServiceCategoryGroups();
        handleCategoryClick();

        return view;
    }

    @Override
    public VerificationError verifyStep() {
        if (null == viewModel.getSelectedServiceCategoryGroup()) {
            return new VerificationError(getResources().getString(R.string.error_step1));
        }

        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    private void handleCategoryClick() {
        recyclerView.addOnItemTouchListener(
                new RecyclerViewTouchListener(getContext(), recyclerView,
                        new RecyclerViewTouchListener.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                for (int childCount = recyclerView.getChildCount(), i = 0;
                                     i < childCount; ++i
                                ) {
                                    final RecyclerView.ViewHolder holder =
                                            recyclerView.getChildViewHolder(
                                                    recyclerView.getChildAt(i));
                                    CardView cardView = holder.itemView.findViewById(R.id.cardView);
                                    cardView.setCardBackgroundColor(Color.WHITE);
                                }

                                CardView cardView = view.findViewById(R.id.cardView);
                                cardView.setCardBackgroundColor(
                                        getResources().getColor(R.color.accent));

                                viewModel.setSelectedServiceCategoryGroup(
                                        entryAdapter.getByPosition(position));
                            }

                            @Override
                            public void onLongClick(View view, int position) {
                                // implement long click if necessary
                            }
                        }
                ));
    }

    private void loadServiceCategoryGroups() {
        viewModel.loadServiceCategories();

        viewModel.getServiceCategories().observe(getViewLifecycleOwner(), serviceCategories -> {
            if (null == serviceCategories) {
                return;
            }

            if (serviceCategories.isEmpty()) {
                Snackbar.make(recyclerView,
                        getResources().getString(R.string.error_load_categories),
                        Snackbar.LENGTH_LONG
                ).show();
            } else {
                List<String> groups = serviceCategories.stream()
                                                       .map(serviceCategory -> serviceCategory.group)
                                                       .distinct()
                                                       .sorted()
                                                       .collect(Collectors.toList());
                entryAdapter.setServiceGroupNames(groups);
            }
        });
    }
}
