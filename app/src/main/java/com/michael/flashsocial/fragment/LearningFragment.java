package com.michael.flashsocial.fragment;

import android.animation.LayoutTransition;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.michael.flashsocial.R;
import com.michael.flashsocial.database.PersonDB;
import com.michael.flashsocial.model.Person;
import com.michael.flashsocial.utils.DataConverter;
import com.michael.flashsocial.utils.NavigationUtil;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LearningFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LearningFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FrameLayout frameLayout;
    MaterialTextView promptView;
    MaterialButton nopeBtn;
    MaterialButton yesBtn;
    MaterialButton retryBtn;

    List<Person> personList;
    Person person;
    Fragment fragment;

    public LearningFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LearningFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LearningFragment newInstance(String param1, String param2) {
        LearningFragment fragment = new LearningFragment();
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
        View view = inflater.inflate(R.layout.fragment_learning, container, false);

        promptView = view.findViewById(R.id.frag_learn_prompt_question);
        nopeBtn = view.findViewById(R.id.frag_learn_nope_btn);
        yesBtn = view.findViewById(R.id.frag_learn_yes_btn);
        retryBtn = view.findViewById(R.id.frag_learn_retry_btn);

        nopeBtn.setOnClickListener(this::handleNopeCase);
        yesBtn.setOnClickListener(this::handleYesCase);
        retryBtn.setOnClickListener(this::handleRetryCase);

        frameLayout = view.findViewById(R.id.frag_learning_fl);

        personList = PersonDB.getInstance(this.getContext()).itemDao().getAllChosenPeople();

        if (personList.size() != 0) {
            chooseNewPerson();
        } else {
            promptView.setVisibility(View.INVISIBLE);
            nopeBtn.setVisibility(View.INVISIBLE);
            yesBtn.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    private void handleRetryCase(View view) {
        fragment = null;
        personList = PersonDB.getInstance(this.getContext()).itemDao().getAllChosenPeople();
        if (personList.size() != 0) {
            promptView.setVisibility(View.VISIBLE);
            nopeBtn.setVisibility(View.VISIBLE);
            yesBtn.setVisibility(View.VISIBLE);
            chooseNewPerson();
        } else {
            Snackbar.make(view, "The list is empty. Please add people in.", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    private void handleYesCase(View view) {
        if (person != null) {
            person.incrementCorrectGuess();
        }
        PersonDB.getInstance(this.getContext()).itemDao().updateItem(person);
        if (personList.size() == 0) {
            NavigationUtil.removeFragment(this.requireActivity(), fragment);
            promptView.setVisibility(View.INVISIBLE);
            nopeBtn.setVisibility(View.INVISIBLE);
            yesBtn.setVisibility(View.INVISIBLE);
        } else {
            chooseNewPerson();
        }
    }

    private void handleNopeCase(View view) {
        if (person != null) {
            person.incrementIncorrectGuess();
        }
        PersonDB.getInstance(this.getContext()).itemDao().updateItem(person);
        if (personList.size() == 0) {
            NavigationUtil.removeFragment(this.requireActivity(), fragment);
            promptView.setVisibility(View.INVISIBLE);
            nopeBtn.setVisibility(View.INVISIBLE);
            yesBtn.setVisibility(View.INVISIBLE);
        } else {
            chooseNewPerson();
        }
    }

    private void chooseNewPerson() {
        Random rand = new Random();
        int idx = rand.nextInt(personList.size());
        person = personList.get(idx);

        int icon = 0;
        String prompt = "";
        String hint = "";
        switch (rand.nextInt(3)) {
            case 0:
                icon = R.drawable.ic_baseline_cake_24;
                prompt = "Date of birth";
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
                hint = sdf.format(person.getDob());
                break;
            case 1:
                icon = R.drawable.ic_baseline_business_center_24;
                prompt = "Role";
                hint = person.getRole();
                break;
            case 2:
                icon = R.drawable.ic_baseline_star_24;
                prompt = "Unique feature";
                hint = person.getUniqueFeature();
                break;
        }

        fragment = LearningCardFragment.newInstance(
                icon, prompt, hint, person
        );
        NavigationUtil.changeFragment(this.requireActivity(), R.id.frag_learning_fl, fragment);
        personList.remove(idx);
    }
}