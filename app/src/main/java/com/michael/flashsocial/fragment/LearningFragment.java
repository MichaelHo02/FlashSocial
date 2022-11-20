package com.michael.flashsocial.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.michael.flashsocial.R;
import com.michael.flashsocial.custom_rule.CycleRule;
import com.michael.flashsocial.database.PersonDB;
import com.michael.flashsocial.model.Person;
import com.michael.flashsocial.utils.NavigationUtil;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

public class LearningFragment extends Fragment implements CycleRule {

    private View view;
    private FrameLayout frameLayout;
    private MaterialTextView promptView;
    private MaterialButton nopeBtn;
    private MaterialButton yesBtn;
    private MaterialButton retryBtn;

    private List<Person> personList;
    private Person person;
    private Fragment fragment;

    public LearningFragment() {}

    public static LearningFragment newInstance() {
        LearningFragment fragment = new LearningFragment();
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
        view = inflater.inflate(R.layout.fragment_learning, container, false);
        initUI();
        initUIAction();

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
        handleLogicNopeYesBtn();
    }

    private void handleNopeCase(View view) {
        if (person != null) {
            person.incrementIncorrectGuess();
        }
        handleLogicNopeYesBtn();
    }

    private void handleLogicNopeYesBtn() {
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

    @Override
    public void initUI() {
        promptView = view.findViewById(R.id.frag_learn_prompt_question);
        nopeBtn = view.findViewById(R.id.frag_learn_nope_btn);
        yesBtn = view.findViewById(R.id.frag_learn_yes_btn);
        retryBtn = view.findViewById(R.id.frag_learn_retry_btn);
        frameLayout = view.findViewById(R.id.frag_learning_fl);
    }

    @Override
    public void initUIAction() {
        nopeBtn.setOnClickListener(this::handleNopeCase);
        yesBtn.setOnClickListener(this::handleYesCase);
        retryBtn.setOnClickListener(this::handleRetryCase);
    }
}