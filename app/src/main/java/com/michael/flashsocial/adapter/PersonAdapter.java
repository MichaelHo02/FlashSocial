package com.michael.flashsocial.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.michael.flashsocial.R;
import com.michael.flashsocial.custom_rule.IClickCallback;
import com.michael.flashsocial.utils.DataConverter;
import com.michael.flashsocial.model.Person;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ItemHolder> {
    private List<Person> personList;
    private IClickCallback clickCallback;

    public PersonAdapter(List<Person> personList, IClickCallback clickCallback) {
        this.personList = personList;
        this.clickCallback = clickCallback;
    }

    public void setData(List<Person> newList) {
        personList = newList;
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
        Person person = personList.get(position);
        if (person == null) return;
        holder.avt.setImageBitmap(DataConverter.convertByteArrToBitmap(person.getAvatar()));
        holder.name.setText(person.getFirstName() + " " + person.getLastName());
        holder.role.setText(person.getRole());
        holder.materialCardView.setOnClickListener(view -> clickCallback.onClickItem(person));
    }

    @Override
    public int getItemCount() {
        return personList != null ? personList.size() : 0;
    }

    static class ItemHolder extends RecyclerView.ViewHolder {
        MaterialCardView materialCardView;
        ShapeableImageView avt;
        TextView name;
        TextView role;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            materialCardView = itemView.findViewById(R.id.holder_item_card);
            avt = itemView.findViewById(R.id.holder_item_avt);
            name = itemView.findViewById(R.id.holder_item_name);
            role = itemView.findViewById(R.id.holder_item_role);
        }
    }
}
