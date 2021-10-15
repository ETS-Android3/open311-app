package com.iu.open311.ui.newissue.step1;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.iu.open311.R;
import com.iu.open311.ui.newissue.NewIssueViewModel;

import java.util.List;

public class Step1EntryAdapter extends ArrayAdapter<String> {

    private List<String> serviceGroupNames;
    private NewIssueViewModel viewModel;
    private Resources resources;

    public Step1EntryAdapter(Context context, List<String> serviceGroupNames,
            NewIssueViewModel viewModel, Resources resources
    ) {
        super(context, -1, serviceGroupNames);
        this.serviceGroupNames = serviceGroupNames;
        this.viewModel = viewModel;
        this.resources = resources;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent
    ) {
        LayoutInflater inflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.service_group_list_item, parent, false);
        CardView cardView = view.findViewById(R.id.cardView);
        TextView textView = (TextView) cardView.findViewById(R.id.serviceGroupName);
        ImageView imageView = (ImageView) cardView.findViewById(R.id.serviceGroupImage);
        textView.setText(serviceGroupNames.get(position));

        if (null != viewModel.getSelectedServiceCategoryGroup() &&
                viewModel.getSelectedServiceCategoryGroup().equals(getByPosition(position))) {
            cardView.setCardBackgroundColor(resources.getColor(R.color.accent));
        }


        String strippedName = serviceGroupNames.get(position)
                                               .toLowerCase()
                                               .replaceAll("[äöüß/]", "")
                                               .replace(" ", "");
        int imageId = getContext().getResources()
                                  .getIdentifier(strippedName, "drawable",
                                          getContext().getPackageName()
                                  );
        if (0 < imageId) {
            imageView.setImageResource(imageId);
        } else {
            imageView.setImageResource(R.drawable.info_square_fill);
        }

        return cardView;
    }

    public String getByPosition(int position) {
        return serviceGroupNames.get(position);
    }
}