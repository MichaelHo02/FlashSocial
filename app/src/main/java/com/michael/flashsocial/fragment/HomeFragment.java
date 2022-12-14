package com.michael.flashsocial.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.michael.flashsocial.R;
import com.michael.flashsocial.activity.ItemCreationActivity;
import com.michael.flashsocial.activity.ItemDetailActivity;
import com.michael.flashsocial.adapter.PersonAdapter;
import com.michael.flashsocial.custom_rule.CycleRule;
import com.michael.flashsocial.database.PersonDB;
import com.michael.flashsocial.model.Person;
import com.michael.flashsocial.utils.NavigationUtil;
import com.michael.flashsocial.utils.RequestSignal;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements CycleRule {

    private RecyclerView recyclerView;
    private MaterialToolbar materialToolbar;
    private View view;

    private List<Person> personList;
    private PersonAdapter personAdapter;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initUI();
        initUIAction();
        return view;
    }

    @Override
    public void initUI() {
        materialToolbar = view.findViewById(R.id.frag_home_toolbar);
        recyclerView = view.findViewById(R.id.frag_home_rv);
        recycleViewInit();
        toolbarViewInit();
    }

    @Override
    public void initUIAction() {
        materialToolbar.setOnMenuItemClickListener(this::handleMenuItemClick);
    }

    private void toolbarViewInit() {
        materialToolbar.inflateMenu(R.menu.fragment_home_menu);
    }

    private void recycleViewInit() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        personList = getItemList();
        personAdapter = new PersonAdapter(personList, (pos, person) -> {
            Intent intent = new Intent(view.getContext(), ItemDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", person);
            intent.putExtras(bundle);
            startActivityForResult(intent, RequestSignal.ITEM_DETAIL_ACTIVITY);
        }, (pos, person) -> {
            person.toggleLearn();
            PersonDB.getInstance(this.getContext()).itemDao().updateItem(person);
            personAdapter.setData(pos, person);
        }, ((pos, person) -> {
            PersonDB.getInstance(this.getContext()).itemDao().deleteItem(person);
            personAdapter.setData(person);
            Snackbar.make(view, "Item successfully removed!", Snackbar.LENGTH_LONG).show();
        }));
        recyclerView.setAdapter(personAdapter);
        new ItemTouchHelper(setItemTouchHelper(ItemTouchHelper.LEFT)).attachToRecyclerView(recyclerView);
        new ItemTouchHelper(setItemTouchHelper(ItemTouchHelper.RIGHT)).attachToRecyclerView(recyclerView);

    }

    private boolean handleMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.frag_home_add:
                return navigateAddItem();
            case R.id.frag_home_del_all:
                personAdapter.setData(new ArrayList<>());
                return handleDeleteAll();
        }
        return false;
    }

    private boolean handleDeleteAll() {
        PersonDB.getInstance(this.getContext()).itemDao().deleteAll();
        return true;
    }

    private boolean navigateAddItem() {
        NavigationUtil.navigateActivity(
                this,
                view.getContext(),
                ItemCreationActivity.class,
                RequestSignal.ITEM_CREATION_ACTIVITY
        );
        return true;
    }

    private List<Person> getItemList() {
        return PersonDB.getInstance(this.getContext()).itemDao().getAllPeople();
    }

    private ItemTouchHelper.SimpleCallback setItemTouchHelper(int swipeDirs) {
        return new ItemTouchHelper.SimpleCallback(0, swipeDirs) {
            private final int limitScrollX = dipToPx(requireActivity());
            private int curScrollX = 0;
            private int curScrollXActive = 0;
            private int initXWhenActive = 0;
            private boolean isActive = false;

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {}

            @Override
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return Integer.MAX_VALUE;
            }

            @Override
            public float getSwipeEscapeVelocity(float defaultValue) {
                return Integer.MAX_VALUE;
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    if (dX == 0) {
                        curScrollX = viewHolder.itemView.getScrollX();
                        isActive = true;
                    }

                    if (isCurrentlyActive) {
                        int scrollOffset = curScrollX + ((int) -dX);
                        if (scrollOffset > limitScrollX) {
                            scrollOffset = limitScrollX;
                        } else if (scrollOffset < 0) {
                            scrollOffset = 0;
                        }
                        viewHolder.itemView.scrollTo(scrollOffset, 0);
                    } else {
                        if (isActive) {
                            isActive = false;
                            curScrollXActive = viewHolder.itemView.getScrollX();
                            initXWhenActive = (int) dX;
                        }

                        if (viewHolder.itemView.getScrollX() < limitScrollX) {
                            viewHolder.itemView.scrollTo((int) (curScrollXActive * dX / initXWhenActive), 0);
                        }
                    }
                }
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                if (viewHolder.itemView.getScrollX() > limitScrollX) {
                    viewHolder.itemView.scrollTo(limitScrollX, 0);
                } else if (viewHolder.itemView.getScrollX() < 0) {
                    viewHolder.itemView.scrollTo(0, 0);
                }
            }

            private int dipToPx(Context context) {
                return (int) ((float) 100 * context.getResources().getDisplayMetrics().density);
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RequestSignal.ITEM_CREATION_ACTIVITY || requestCode == RequestSignal.ITEM_DETAIL_ACTIVITY) {
                personList = PersonDB.getInstance(this.getContext()).itemDao().getAllPeople();
                personAdapter.setData(personList);
            }
        }
    }
}


