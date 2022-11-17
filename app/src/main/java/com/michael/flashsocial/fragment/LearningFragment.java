package com.michael.flashsocial.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.michael.flashsocial.R;

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

    MaterialCardView detailView;
    MaterialCardView overlayView;


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
        overlayView = view.findViewById(R.id.frag_learn_overlay_card);
        detailView = view.findViewById(R.id.frag_learn_detail_card);
        overlayView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                detailView.setVisibility(View.VISIBLE);
                ViewAnimationUtils.createCircularReveal(
                        (View) detailView,
                        (int) motionEvent.getX(),
                        (int) motionEvent.getY(),
                        0f,
                        (float) Math.hypot(view.getWidth(), view.getHeight())
                ).setDuration(1000).start();
                overlayView.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        detailView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                overlayView.setVisibility(View.VISIBLE);
                ViewAnimationUtils.createCircularReveal(
                        (View) overlayView,
                        (int) motionEvent.getX(),
                        (int) motionEvent.getY(),
                        0f,
                        (float) Math.hypot(view.getWidth(), view.getHeight())
                ).setDuration(1000).start();
                detailView.setVisibility(View.INVISIBLE);
                return true;
            }
        });


        return view;
    }
}