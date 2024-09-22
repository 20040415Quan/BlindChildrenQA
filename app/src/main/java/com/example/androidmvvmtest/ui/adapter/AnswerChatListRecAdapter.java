package com.example.androidmvvmtest.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.androidmvvmtest.R;
import com.example.androidmvvmtest.databinding.LayoutRecChatListBinding;
import com.example.androidmvvmtest.network.bean.response.ChatRecordsResponse;

/**
 * @Author wuleizhenshang
 * @Email wuleizhenshang@163.com
 * @Date 2024/9/22 19:30
 * @Description: 聊天记录适配器
 */
public class AnswerChatListRecAdapter extends BaseQuickAdapter<ChatRecordsResponse.Data.ChatBean, AnswerChatListRecAdapter.ViewHolder> {

    public AnswerChatListRecAdapter() {
        // 使用布局的ID作为适配器参数
        super(R.layout.layout_rec_chat_list);
        // 设置DiffCallback
        setDiffCallback(new AnswerChatListRecDiffCallback());
    }

    @Override
    protected void convert(@NonNull AnswerChatListRecAdapter.ViewHolder viewHolder, ChatRecordsResponse.Data.ChatBean record) {
        // 在这里通过ViewHolder的binding对象设置UI内容
        LayoutRecChatListBinding binding = viewHolder.binding;

        Glide.with(binding.imgMe).load(R.drawable.icon_ai).circleCrop().into(binding.imgMe);

        binding.tvMe.setText(record.getQuestion());

        Glide.with(binding.imgAi).load(R.drawable.icon).circleCrop().into(binding.imgAi);

        binding.tvAi.setText(record.getAnswer());
    }

    // 自定义ViewHolder，持有ViewBinding
    public static class ViewHolder extends BaseViewHolder {

        public LayoutRecChatListBinding binding;

        public ViewHolder(@NonNull LayoutRecChatListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // 该静态方法用于创建ViewHolder对象
        public static ViewHolder create(ViewGroup parent) {
            LayoutRecChatListBinding binding = LayoutRecChatListBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 通过ViewBinding创建ViewHolder
        return ViewHolder.create(parent);
    }
}
