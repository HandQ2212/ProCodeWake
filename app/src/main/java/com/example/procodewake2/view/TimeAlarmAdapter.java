package com.example.procodewake2.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.BaseAdapter;

import com.example.procodewake2.R;
import com.example.procodewake2.model.TimeAlarm;

import java.util.List;

public class TimeAlarmAdapter extends BaseAdapter {
    private Context context;
    private List<TimeAlarm> alarmList;

    public TimeAlarmAdapter(Context context, List<TimeAlarm> alarmList) {
        this.context = context;
        this.alarmList = alarmList;
    }

    @Override
    public int getCount() {
        return alarmList.size();
    }

    @Override
    public Object getItem(int position) {
        return alarmList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_alarm, parent, false);
        }

        TextView tvTime = convertView.findViewById(R.id.tvTime);
        TextView tvTopic = convertView.findViewById(R.id.tvTopic);
        Switch switchActive = convertView.findViewById(R.id.switchActive);

        TimeAlarm alarm = alarmList.get(position);
        tvTime.setText(String.format("%02d:%02d", alarm.getHour(), alarm.getMinute()));
        tvTopic.setText(alarm.getTopic());
        switchActive.setChecked(alarm.isDuocBat());

        //Xử lý bật/tắt báo thức
        switchActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alarm.setDuocBat(isChecked);
            }
        });

        return convertView;
    }
}
