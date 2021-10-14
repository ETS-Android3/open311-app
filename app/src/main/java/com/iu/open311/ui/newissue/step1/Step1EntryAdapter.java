package com.iu.open311.ui.newissue.step1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iu.open311.R;

import java.util.List;

public class Step1EntryAdapter extends RecyclerView.Adapter<Step1EntryAdapter.ViewHolder> {

    private final List<String> serviceGroupNames;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView serviceGroupImage;
        private final TextView serviceGroupName;

        public ViewHolder(View view) {
            super(view);
            serviceGroupImage = view.findViewById(R.id.serviceGroupImage);
            serviceGroupName = view.findViewById(R.id.serviceGroupName);
        }

        public ImageView getServiceGroupImage() {
            return serviceGroupImage;
        }

        public TextView getServiceGroupName() {
            return serviceGroupName;
        }

    }

    public Step1EntryAdapter(List<String> serviceGroupNames, Context context) {
        this.serviceGroupNames = serviceGroupNames;
        this.context = context;
    }

    public void setServiceGroupNames(List<String> serviceGroupNames) {
        this.serviceGroupNames.clear();
        this.serviceGroupNames.addAll(serviceGroupNames);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                                  .inflate(R.layout.service_group_list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getServiceGroupName().setText(serviceGroupNames.get(position));

        String strippedName = serviceGroupNames.get(position)
                                               .toLowerCase()
                                               .replaceAll("[äöüß/]", "")
                                               .replace(" ", "");
        int imageId = context.getResources()
                             .getIdentifier(strippedName, "drawable", context.getPackageName());
        if (0 < imageId) {
            viewHolder.getServiceGroupImage().setImageResource(imageId);
        } else {
            viewHolder.getServiceGroupImage().setImageResource(R.drawable.info_square_fill);
        }
    }

    public String getByPosition(int position) {
        return serviceGroupNames.get(position);
    }

    @Override
    public int getItemCount() {
        return null == this.serviceGroupNames ? 0 : this.serviceGroupNames.size();
    }
}
