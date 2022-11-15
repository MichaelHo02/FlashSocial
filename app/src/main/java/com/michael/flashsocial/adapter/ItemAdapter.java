package com.michael.flashsocial.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.michael.flashsocial.R;
import com.michael.flashsocial.model.DataConverter;
import com.michael.flashsocial.model.GroupItem;
import com.michael.flashsocial.model.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {
    private List<Item> itemList;

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    public void setData(List<Item> newList) {
        itemList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Item item = itemList.get(position);
        if (item == null) return;
        holder.avt.setImageBitmap(DataConverter.convertByteArrToBitmap(item.getAvatar()));
        holder.name.setText(item.getFirstName() + " " + item.getLastName());
        holder.role.setText(item.getRole());
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        ShapeableImageView avt;
        TextView name;
        TextView role;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            avt = itemView.findViewById(R.id.holder_item_avt);
            name = itemView.findViewById(R.id.holder_item_name);
            role = itemView.findViewById(R.id.holder_item_role);
        }
    }
}
