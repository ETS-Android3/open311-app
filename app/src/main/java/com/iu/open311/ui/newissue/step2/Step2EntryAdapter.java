package com.iu.open311.ui.newissue.step2;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.iu.open311.R;
import com.iu.open311.ui.newissue.NewIssueViewModel;

import java.util.List;

public class Step2EntryAdapter extends ArrayAdapter<String> {

    private final List<String> serviceCategoryNames;
    private final NewIssueViewModel viewModel;
    private final Resources resources;

    public Step2EntryAdapter(Context context, List<String> serviceCategoryNames,
            NewIssueViewModel viewModel, Resources resources
    ) {
        super(context, -1, serviceCategoryNames);
        this.serviceCategoryNames = serviceCategoryNames;
        this.viewModel = viewModel;
        this.resources = resources;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent
    ) {
        LayoutInflater inflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.service_category_list_item, parent, false);
        CardView cardView = view.findViewById(R.id.cardView);
        TextView textView = (TextView) cardView.findViewById(R.id.serviceCategoryName);
        textView.setText(serviceCategoryNames.get(position));

        if (null != viewModel.getSelectedServiceCategory() &&
                viewModel.getSelectedServiceCategory().equals(getByPosition(position))) {
            cardView.setCardBackgroundColor(resources.getColor(R.color.accent));
        }

        return cardView;
    }

    public String getByPosition(int position) {
        return serviceCategoryNames.get(position);
    }
}