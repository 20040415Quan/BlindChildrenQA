package com.example.androidmvvmtest.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.androidmvvmtest.network.bean.response.ChatRecordsResponse;
import com.example.androidmvvmtest.R;

import java.util.List;

public class ChatAdapter extends ArrayAdapter<ChatRecordsResponse.Data.Record> {
    private static class ViewHolder {
        TextView chatText;
    }

    public ChatAdapter(Context context, List<ChatRecordsResponse.Data.Record> records) {
        super(context, 0, records);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatRecordsResponse.Data.Record record = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(record.isUser() ? R.layout.chat_left : R.layout.chat_right, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.chatText = convertView.findViewById(R.id.tv_chat);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.chatText.setText(record.getQuestion()); // 显示问题

        return convertView;
    }
}