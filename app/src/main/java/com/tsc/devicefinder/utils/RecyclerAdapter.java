package com.tsc.devicefinder.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tsc.devicefinder.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private List<Device> deviceList;

    public RecyclerAdapter(List<Device> deviceList) {
        this.deviceList = deviceList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.deviceName.setText(deviceList.get(position).getDeviceName());
        holder.ownerInfo.setText(deviceList.get(position).getOwnerName());
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView deviceName;
        TextView ownerInfo;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.deviceName);
            ownerInfo =  itemView.findViewById(R.id.ownerInfo);
        }
    }
}
