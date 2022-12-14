package com.michael.flashsocial.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.michael.flashsocial.R;
import com.michael.flashsocial.custom_rule.IClickCallback;
import com.michael.flashsocial.model.Person;
import com.michael.flashsocial.utils.DataConverter;

import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ItemHolder> {
    private List<Person> personList;
    private final IClickCallback cardClickCallback;
    private final IClickCallback btnClickCallback;
    private IClickCallback delClickCallback;
    private final boolean showBtn;


    public PersonAdapter(List<Person> personList, IClickCallback cardClickCallback, IClickCallback btnClickCallback, IClickCallback delClickCallback) {
        this.personList = personList;
        this.cardClickCallback = cardClickCallback;
        this.btnClickCallback = btnClickCallback;
        this.delClickCallback = delClickCallback;
        this.showBtn = true;
    }

    public PersonAdapter(List<Person> personList, IClickCallback cardClickCallback, IClickCallback btnClickCallback, boolean showBtn) {
        this.personList = personList;
        this.cardClickCallback = cardClickCallback;
        this.btnClickCallback = btnClickCallback;
        this.showBtn = showBtn;
    }

    public void setData(List<Person> newList) {
        personList = newList;
        notifyDataSetChanged();
    }

    public void setData(int pos, Person person) {
        personList.set(pos, person);
        notifyItemChanged(pos, person);
    }

    public void setData(Person person) {
        int pos = personList.indexOf(person);
        personList.remove(person);
        notifyItemRemoved(pos);
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
        holder.materialCardView.setOnClickListener(view -> cardClickCallback.onClickItem(position, person));
        holder.learnBtn.setOnClickListener(view -> btnClickCallback.onClickItem(position, person));
        holder.deleteIcon.setOnClickListener(view -> delClickCallback.onClickItem(position, person));
        if (!showBtn) {
            holder.learnBtn.setVisibility(View.INVISIBLE);
            holder.materialCardView.setClickable(false);
        }
        if (person.isChooseToLearn()) {
            holder.learnBtn.setIconResource(R.drawable.ic_baseline_school_24);
        } else {
            holder.learnBtn.setIconResource(R.drawable.ic_outline_school_24);
        }
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
        MaterialButton learnBtn;
        ShapeableImageView deleteIcon;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                if (view.getScrollX() != 0) {
                    view.scrollTo(0, 0);
                }

            });
            materialCardView = itemView.findViewById(R.id.holder_item_card);
            avt = itemView.findViewById(R.id.holder_item_avt);
            name = itemView.findViewById(R.id.holder_item_name);
            role = itemView.findViewById(R.id.holder_item_role);
            learnBtn = itemView.findViewById(R.id.holder_item_action);
            deleteIcon = itemView.findViewById(R.id.holder_item_del);
        }
    }
}
