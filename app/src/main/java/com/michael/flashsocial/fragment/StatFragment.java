package com.michael.flashsocial.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textview.MaterialTextView;
import com.michael.flashsocial.R;
import com.michael.flashsocial.database.PersonDB;
import com.michael.flashsocial.model.Person;

import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    MaterialTextView textView1;
    MaterialTextView textView2;
    MaterialTextView textView3;
    MaterialTextView textView4;
    MaterialTextView textView5;
    RecyclerView textView6;
    RecyclerView textView7;

    public StatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatFragment newInstance(String param1, String param2) {
        StatFragment fragment = new StatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stat, container, false);

        textView1 = view.findViewById(R.id.frag_stat_text_view_1);
        textView2 = view.findViewById(R.id.frag_stat_text_view_2);
        textView3 = view.findViewById(R.id.frag_stat_text_view_3);
        textView4 = view.findViewById(R.id.frag_stat_text_view_4);
        textView5 = view.findViewById(R.id.frag_stat_text_view_5);
        textView6 = view.findViewById(R.id.frag_stat_text_view_6);
        textView7 = view.findViewById(R.id.frag_stat_text_view_7);

        List<Person> personList = PersonDB.getInstance(this.getContext()).itemDao().getAllChosenPeople();
        if (personList.isEmpty()) {
            return view;
        }

        int totalGuess = personList.stream().mapToInt((person -> person.getCorrectGuess() + person.getIncorrectGuess())).sum();
        textView1.setText(totalGuess + " cards");

        if (totalGuess < 1) {
            return view;
        }

        int totalRightGuess = personList.stream().mapToInt((Person::getCorrectGuess)).sum();
        textView2.setText(totalRightGuess + " guesses");

        Person highestAccuratePerson = personList.stream().max(Comparator.comparing(Person::getCorrectGuess)).orElseThrow(NoSuchFieldError::new);
        textView3.setText(highestAccuratePerson.getFirstName() + " " + highestAccuratePerson.getLastName());

        Person lowestAccuratePerson = personList.stream().min(Comparator.comparing(Person::getCorrectGuess)).orElseThrow(NoSuchFieldError::new);
        textView4.setText(lowestAccuratePerson.getFirstName() + " " + lowestAccuratePerson.getLastName());

        int avgAccuracy = totalRightGuess * 100 / totalGuess;
        textView5.setText(avgAccuracy + "%");
        return view;
    }
}