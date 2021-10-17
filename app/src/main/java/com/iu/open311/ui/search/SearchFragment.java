package com.iu.open311.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iu.open311.R;
import com.iu.open311.api.Client;
import com.iu.open311.common.RecyclerViewTouchListener;
import com.iu.open311.databinding.FragmentSearchBinding;

public class SearchFragment extends Fragment {
    private FragmentSearchBinding binding;
    private RecyclerView recyclerView;
    private SearchEntryAdapter entryAdapter;
    private Client client;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        client = Client.getInstance(getContext());

        initListAdapter();
        handleOnItemClick();

        // loading results
        binding.recyclerList.loading.setVisibility(View.VISIBLE);
        client.loadRequests();
        observeServiceRequests();

        // search
        handleSearch();

        // Sorting
        handleSortButton();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initListAdapter() {
        recyclerView = binding.getRoot().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        entryAdapter = new SearchEntryAdapter(getResources(), getViewLifecycleOwner());
        recyclerView.setAdapter(entryAdapter);
    }

    private void handleOnItemClick() {
        recyclerView.addOnItemTouchListener(
                new RecyclerViewTouchListener(getContext(), recyclerView,
                        new RecyclerViewTouchListener.ClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                // show details
                            }

                            @Override
                            public void onLongClick(View view, int position) {
                                // implement long click if necessary
                            }
                        }
                ));
    }

    private void handleSortButton() {
        binding.btnSort.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(getActivity(), view);
            popupMenu.getMenuInflater().inflate(R.menu.search_filter, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.sortCategoryAsc:
                        entryAdapter.sortServiceRequests(SearchOrderEnum.CATEGORY_ASC);
                        break;

                    case R.id.sortCategoryDesc:
                        entryAdapter.sortServiceRequests(SearchOrderEnum.CATEGORY_DESC);
                        break;
                    case R.id.sortDateAsc:
                        entryAdapter.sortServiceRequests(SearchOrderEnum.DATE_ASC);
                        break;
                    case R.id.sortDateDesc:
                        entryAdapter.sortServiceRequests(SearchOrderEnum.DATE_DESC);
                        break;
                    case R.id.sortStatusAsc:
                        entryAdapter.sortServiceRequests(SearchOrderEnum.STATUS_ASC);
                        break;
                    case R.id.sortStatusDesc:
                        entryAdapter.sortServiceRequests(SearchOrderEnum.STATUS_DESC);
                        break;
                }
                return true;
            });
            popupMenu.setForceShowIcon(true);
            popupMenu.show();
        });
    }

    private void handleSearch() {
        binding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int searchCount = entryAdapter.filterServiceRequests(editable.toString());
                binding.searchCount.setText(String.valueOf(searchCount));
            }
        });
    }

    private void observeServiceRequests() {
        client.getServiceRequests().observe(getViewLifecycleOwner(), serviceRequests -> {
            if (null == serviceRequests) {
                return;
            }
            binding.recyclerList.loading.setVisibility(View.GONE);
            binding.searchCountWrapper.setVisibility(View.VISIBLE);
            binding.filterWrapper.setVisibility(View.VISIBLE);
            binding.searchCount.setText(String.valueOf(serviceRequests.size()));
            entryAdapter.setServiceRequests(serviceRequests);
        });
    }
}