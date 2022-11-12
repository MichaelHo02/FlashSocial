package com.michael.flashsocial.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.michael.flashsocial.R;
import com.michael.flashsocial.model.GroupItem;

import java.util.List;

public class GroupItemAdapter extends RecyclerView.Adapter<GroupItemAdapter.GroupItemHolder> {
    private List<GroupItem> groupItemList;

    public GroupItemAdapter(List<GroupItem> groupItemList) {
        this.groupItemList = groupItemList;
    }

    @NonNull
    @Override
    public GroupItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_group_item, parent, false);
        return new GroupItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupItemHolder holder, int position) {
        GroupItem groupItem = groupItemList.get(position);
        if (groupItem == null) return;
        System.out.println(groupItem.name);
        System.out.println(holder);
        holder.textView.setText(groupItem.name);
    }

    @Override
    public int getItemCount() {
        return groupItemList != null ? groupItemList.size() : 0;
    }

    static class GroupItemHolder extends RecyclerView.ViewHolder {
        MaterialCardView materialCardView;
        TextView textView;

        public GroupItemHolder(@NonNull View itemView) {
            super(itemView);
            materialCardView = itemView.findViewById(R.id.holder_group_item_card);
            textView = itemView.findViewById(R.id.holder_group_item_text);
        }
    }
}
