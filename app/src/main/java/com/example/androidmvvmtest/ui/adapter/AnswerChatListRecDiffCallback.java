package com.example.androidmvvmtest.ui.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.androidmvvmtest.network.bean.response.ChatRecordsResponse;

import java.util.Objects;

/**
 * @Author wuleizhenshang
 * @Email wuleizhenshang@163.com
 * @Date 2024/9/22 19:48
 * @Description: 聊天记录列表差异回调
 */
public class AnswerChatListRecDiffCallback extends DiffUtil.ItemCallback<ChatRecordsResponse.Data.ChatBean> {

    @Override
    public boolean areItemsTheSame(@NonNull ChatRecordsResponse.Data.ChatBean oldItem, @NonNull ChatRecordsResponse.Data.ChatBean newItem) {
        return Objects.equals(oldItem.getId(), newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull ChatRecordsResponse.Data.ChatBean oldItem, @NonNull ChatRecordsResponse.Data.ChatBean newItem) {
        return oldItem.getAnswer().equals(newItem.getAnswer())
                && oldItem.getQuestion().equals(newItem.getQuestion());
    }
}
