package com.kasemsan.accident.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kasemsan.accident.R;
import com.kasemsan.accident.entity.DataEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private final List<DataEntity> dataList;

    public HistoryAdapter(List<DataEntity> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itme_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataEntity dataEntity = dataList.get(position);

        holder.categoryTextView.setText("Event : " + dataEntity.getCategoryName());
        holder.scoreTextView.setText(String.valueOf("Score : " + String.format("%.2f%%", dataEntity.getProbabilityScore() * 100) ));

        // Format date
        long timestamp = dataEntity.getDateEvent();
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = sdf.format(date);
        holder.dateTextView.setText("Date  : " + formattedDate);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView categoryTextView;
        public final TextView scoreTextView;
        public final TextView dateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.text_category);
            scoreTextView = itemView.findViewById(R.id.text_score);
            dateTextView = itemView.findViewById(R.id.text_date);
        }
    }
}
