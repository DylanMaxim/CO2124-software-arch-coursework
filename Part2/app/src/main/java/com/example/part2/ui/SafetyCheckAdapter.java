package com.example.part2.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.part2.R;
import com.example.part2.data.SafetyCheckWithDefects;

import java.util.ArrayList;
import java.util.List;

public class SafetyCheckAdapter extends RecyclerView.Adapter<SafetyCheckAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(SafetyCheckWithDefects item);
        void onItemLongClick(SafetyCheckWithDefects item);
    }

    private List<SafetyCheckWithDefects> items = new ArrayList<>();
    private final OnItemClickListener listener;

    public SafetyCheckAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<SafetyCheckWithDefects> items) {
        this.items = items;
        // notify the adapter that data changed so the list refreshes
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_safety_check, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SafetyCheckWithDefects item = items.get(position);

        int defectCount = item.defects.size();
        String defectLabel = defectCount == 1 ? "1 Defect" : defectCount + " Defects";

        String text = item.safetyCheck.getDate() + " - " +
                item.safetyCheck.getVehicleRegistration() + " - " +
                defectLabel;

        holder.textRow.setText(text);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClick(item);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textRow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textRow = itemView.findViewById(R.id.textRow);
        }
    }
}