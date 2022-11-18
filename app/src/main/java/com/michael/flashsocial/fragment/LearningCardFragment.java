package com.michael.flashsocial.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.michael.flashsocial.R;
import com.michael.flashsocial.custom_rule.CycleRule;
import com.michael.flashsocial.model.Person;
import com.michael.flashsocial.utils.DataConverter;

import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LearningCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LearningCardFragment extends Fragment implements CycleRule {


    private static final String ARG_PARAM1 = "icon";
    private static final String ARG_PARAM2 = "prompt";
    private static final String ARG_PARAM3 = "hint";
    private static final String ARG_PARAM4 = "person";
    private int mParam1;
    private String mParam2;
    private String mParam3;
    private Person mParam4;

    private View view;

    MaterialCardView detailView;
    MaterialCardView overlayView;

    ShapeableImageView avtFront;
    ShapeableImageView iconFront;
    MaterialTextView promptView;
    MaterialTextView hintView;

    ShapeableImageView avtBack;
    MaterialTextView fullNameView;
    MaterialTextView dobView;
    MaterialTextView roleView;
    MaterialTextView uniqueFeatureView;

    public LearningCardFragment() {}

    public static LearningCardFragment newInstance(int param1, String param2, String param3, Person param4) {
        LearningCardFragment fragment = new LearningCardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putSerializable(ARG_PARAM4, param4);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = (Person) getArguments().getSerializable(ARG_PARAM4);
        }

        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setExitTransition(inflater.inflateTransition(R.transition.slide_left));
        setEnterTransition(inflater.inflateTransition(R.transition.slide_right));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_learning_card, container, false);
        initUI();
        initUIAction();
        return view;
    }

    @Override
    public void initUI() {
        overlayView = view.findViewById(R.id.frag_learn_overlay_card);
        detailView = view.findViewById(R.id.frag_learn_detail_card);

        avtFront = view.findViewById(R.id.frag_learn_avt_front);
        iconFront = view.findViewById(R.id.frag_learn_icon_front);
        promptView = view.findViewById(R.id.frag_learn_prompt);
        hintView = view.findViewById(R.id.frag_learn_hint);

        avtBack = view.findViewById(R.id.frag_learn_avt_back);
        fullNameView = view.findViewById(R.id.frag_learn_full_name);
        dobView = view.findViewById(R.id.frag_learn_dob);
        roleView = view.findViewById(R.id.frag_learn_role);
        uniqueFeatureView = view.findViewById(R.id.frag_learn_unique_feature);
    }

    @Override
    public void initUIAction() {
        overlayView.setOnTouchListener(this::handleOnTouchCard);

        avtFront.setImageBitmap(DataConverter.convertByteArrToBitmap(mParam4.getAvatar()));
        iconFront.setImageResource(mParam1);
        promptView.setText(mParam2);
        hintView.setText(mParam3);
        Log.e("Line count", String.valueOf(hintView.getLineCount()));
        if (mParam3.length() < 32) {
            hintView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }

        avtBack.setImageBitmap(DataConverter.convertByteArrToBitmap(mParam4.getAvatar()));
        fullNameView.setText(mParam4.getFirstName() + ", " + mParam4.getLastName());
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        dobView.setText(sdf.format(mParam4.getDob()));
        roleView.setText(mParam4.getRole());
        uniqueFeatureView.setText(mParam4.getUniqueFeature());
    }

    private boolean handleOnTouchCard(View view, MotionEvent motionEvent) {
        overlayView.setVisibility(View.GONE);
        detailView.setVisibility(View.VISIBLE);
        ViewAnimationUtils.createCircularReveal(
                detailView,
                (int) motionEvent.getX(),
                (int) motionEvent.getY(),
                0f,
                (float) Math.hypot(view.getWidth(), view.getHeight())
        ).setDuration(1000).start();
        return false;
    }
}