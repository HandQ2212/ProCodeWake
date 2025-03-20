package com.example.procodewake2;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder> {
    private List<TimeAlarm> alarmList;
    private Context context;
    private OnAlarmToggleListener toggleListener;

    public interface OnAlarmToggleListener {
        void onAlarmToggled(TimeAlarm alarm, boolean isEnabled);
    }

    public AlarmAdapter(Context context, List<TimeAlarm> alarmList, OnAlarmToggleListener toggleListener) {
        this.context = context;
        this.alarmList = alarmList;
        this.toggleListener = toggleListener;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        TimeAlarm alarm = alarmList.get(position);
        String timeFormatted = String.format("%02d:%02d", alarm.getHour(), alarm.getMinute());
        holder.tvTime.setText(timeFormatted);
        holder.tvTopic.setText(alarm.getTopic());

        holder.switchActive.setChecked(alarm.isDuocBat());
        holder.switchActive.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (toggleListener != null) {
                toggleListener.onAlarmToggled(alarm, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public static class AlarmViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvTopic;
        Switch switchActive;

        public AlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTopic = itemView.findViewById(R.id.tvTopic);
            switchActive = itemView.findViewById(R.id.switchActive);
        }
    }
}
