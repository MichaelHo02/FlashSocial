package com.michael.flashsocial.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.michael.flashsocial.R;
import com.michael.flashsocial.adapter.PersonAdapter;
import com.michael.flashsocial.custom_rule.CycleRule;
import com.michael.flashsocial.database.PersonDB;
import com.michael.flashsocial.model.Person;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StatFragment extends Fragment implements CycleRule {
    private View view;
    private LinearLayout linearLayout1;
    private LinearLayout linearLayout2;
    private MaterialTextView textView1;
    private MaterialTextView textView2;
    private MaterialTextView textView3;
    private MaterialTextView textView4;
    private MaterialTextView textView5;
    private RecyclerView rv1;
    private RecyclerView rv2;
    private PersonAdapter personAdapter1;
    private PersonAdapter personAdapter2;

    public StatFragment() {}

    public static StatFragment newInstance() {
        StatFragment fragment = new StatFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_stat, container, false);
        initUI();
        initUIAction();
        return view;
    }

    @Override
    public void initUI() {
        linearLayout1 = view.findViewById(R.id.frag_stat_linear_layout_1);
        linearLayout2 = view.findViewById(R.id.frag_stat_linear_layout_2);
        textView1 = view.findViewById(R.id.frag_stat_text_view_1);
        textView2 = view.findViewById(R.id.frag_stat_text_view_2);
        textView3 = view.findViewById(R.id.frag_stat_text_view_3);
        textView4 = view.findViewById(R.id.frag_stat_text_view_4);
        textView5 = view.findViewById(R.id.frag_stat_text_view_5);
        rv1 = view.findViewById(R.id.frag_stat_rv_1);
        rv2 = view.findViewById(R.id.frag_stat_rv_2);
    }

    @Override
    public void initUIAction() {
        List<Person> personList = PersonDB.getInstance(this.getContext()).itemDao().getAllChosenPeople();
        if (personList.isEmpty()) {
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.INVISIBLE);
            return;
        }

        int totalGuess = personList.stream().mapToInt((person -> person.getCorrectGuess() + person.getIncorrectGuess())).sum();
        textView1.setText(totalGuess + " cards");

        if (totalGuess < 1) {
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.INVISIBLE);
            return;
        }

        int totalRightGuess = personList.stream().mapToInt((Person::getCorrectGuess)).sum();
        textView2.setText(totalRightGuess + " guesses");

        Person highestAccuratePerson = personList.stream().max(Comparator.comparing(Person::getCorrectGuess)).orElseThrow(NoSuchFieldError::new);
        textView3.setText(highestAccuratePerson.getFirstName() + " " + highestAccuratePerson.getLastName());

        Person lowestAccuratePerson = personList.stream().min(Comparator.comparing(Person::getCorrectGuess)).orElseThrow(NoSuchFieldError::new);
        textView4.setText(lowestAccuratePerson.getFirstName() + " " + lowestAccuratePerson.getLastName());

        int avgAccuracy = totalRightGuess * 100 / totalGuess;
        textView5.setText(avgAccuracy + "%");

        List<Person> personTopList = personList
                .stream()
                .sorted(Comparator.comparingInt(Person::getCorrectGuess).reversed())
                .collect(Collectors.toList())
                .subList(0, Math.min(personList.size(), 5));
        List<Person> personBottomList = personList
                .stream()
                .sorted(Comparator.comparingInt(Person::getCorrectGuess))
                .collect(Collectors.toList())
                .subList(0, Math.min(personList.size(), 5));
        personAdapter1 = new PersonAdapter(personTopList, ((pos, person) -> {}), (pos, person) -> {}, false);
        personAdapter2 = new PersonAdapter(personBottomList, ((pos, person) -> {}), (pos, person) -> {}, false);
        rv1.setAdapter(personAdapter1);
        rv2.setAdapter(personAdapter2);

        rv1.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rv2.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
}