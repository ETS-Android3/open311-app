package com.iu.open311.ui.search;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.iu.open311.R;
import com.iu.open311.api.dto.ServiceRequest;
import com.iu.open311.common.ImageCache;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SearchEntryAdapter extends RecyclerView.Adapter<SearchEntryAdapter.ViewHolder> {

    private final List<ServiceRequest> serviceRequests = new ArrayList<>();
    private final List<ServiceRequest> allServiceRequests = new ArrayList<>();

    private Resources resources;
    private LifecycleOwner lifecycleOwner;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView serviceName;
        private final TextView status;
        private final TextView requestDateTime;
        private final ImageView imagePreview;

        public ViewHolder(View view) {
            super(view);
            serviceName = view.findViewById(R.id.serviceName);
            status = view.findViewById(R.id.status);
            requestDateTime = view.findViewById(R.id.requestDateTime);
            imagePreview = view.findViewById(R.id.imagePreview);
        }

        public TextView getServiceName() {
            return serviceName;
        }

        public TextView getStatus() {
            return status;
        }

        public TextView getRequestDateTime() {
            return requestDateTime;
        }

        public ImageView getImagePreview() {
            return imagePreview;
        }
    }

    public SearchEntryAdapter(Resources resources, LifecycleOwner lifecycleOwner
    ) {
        this.resources = resources;
        this.lifecycleOwner = lifecycleOwner;
    }

    public void sortServiceRequests(SearchOrderEnum searchOrder) {
        switch (searchOrder) {
            case CATEGORY_ASC:
                this.serviceRequests.sort(
                        Comparator.comparing(ServiceRequest::getServiceName,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        ));
                break;
            case CATEGORY_DESC:
                this.serviceRequests.sort(
                        Comparator.comparing(ServiceRequest::getServiceName,
                                Comparator.nullsFirst(Comparator.naturalOrder())
                        ).reversed());
                break;
            case STATUS_ASC:
                this.serviceRequests.sort(
                        Comparator.comparing(ServiceRequest::getStatus,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        ));
                break;
            case STATUS_DESC:
                this.serviceRequests.sort(
                        Comparator.comparing(ServiceRequest::getStatus,
                                Comparator.nullsFirst(Comparator.naturalOrder())
                        ).reversed());
                break;
            case DATE_DESC:
                this.serviceRequests.sort(
                        Comparator.comparing(ServiceRequest::getRequestedDatetime,
                                Comparator.nullsFirst(Comparator.naturalOrder())
                        ).reversed());
                break;
            default:
                this.serviceRequests.sort(
                        Comparator.comparing(ServiceRequest::getRequestedDatetime,
                                Comparator.nullsLast(Comparator.naturalOrder())
                        ));
                break;
        }

        notifyDataSetChanged();
    }

    public int filterServiceRequests(String searchString) {
        List<ServiceRequest> filteredServiceRequests =
                allServiceRequests.stream().filter(serviceRequest -> {
                    if (serviceRequest.serviceName.toLowerCase()
                                                  .contains(searchString.toLowerCase())) {
                        return true;
                    }
                    if (serviceRequest.description.toLowerCase()
                                                  .contains(
                                                          searchString.toLowerCase(Locale.ROOT))) {
                        return true;
                    }

                    return false;
                }).collect(Collectors.toList());

        this.serviceRequests.clear();
        this.serviceRequests.addAll(filteredServiceRequests);
        this.serviceRequests.sort(Comparator.comparing(ServiceRequest::getRequestedDatetime,
                Comparator.nullsLast(Comparator.naturalOrder())
        ));
        notifyDataSetChanged();

        return filteredServiceRequests.size();
    }

    public void setServiceRequests(List<ServiceRequest> serviceRequests) {
        this.allServiceRequests.clear();
        this.allServiceRequests.addAll(serviceRequests);
        this.serviceRequests.clear();
        this.serviceRequests.addAll(serviceRequests);
        this.serviceRequests.sort(
                Comparator.comparing(serviceRequest -> serviceRequest.requestedDatetime,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                                  .inflate(R.layout.service_request_list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getServiceName().setText(serviceRequests.get(position).serviceName);
        viewHolder.getStatus().setText(determineStatus(serviceRequests.get(position).status));

        viewHolder.getRequestDateTime()
                  .setText(serviceRequests.get(position).requestedDatetime.format(
                          DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                                           .withLocale(Locale.GERMAN)));

        String mediaUrl = serviceRequests.get(position).mediaUrl;
        if (null != mediaUrl) {
            ImageCache.getImage(mediaUrl).observe(lifecycleOwner, image -> {
                if (null != image) {
                    viewHolder.getImagePreview().setImageBitmap(image);
                }
            });
        }

    }

    public ServiceRequest getByPosition(int position) {
        return serviceRequests.get(position);
    }

    @Override
    public int getItemCount() {
        return null == this.serviceRequests ? 0 : this.serviceRequests.size();
    }

    private String determineStatus(String statusKey) {
        switch (statusKey.toLowerCase()) {
            case "pending":
                return resources.getString(R.string.status_pending);
            case "received":
                return resources.getString(R.string.status_received);
            case "in_process":
                return resources.getString(R.string.status_in_process);
            case "reviewed":
                return resources.getString(R.string.status_reviewed);
            case "processed":
                return resources.getString(R.string.status_processed);
            case "rejected":
                return resources.getString(R.string.status_rejected);
            case "closed":
                return resources.getString(R.string.status_closed);
            default:
                return statusKey;
        }
    }
}
