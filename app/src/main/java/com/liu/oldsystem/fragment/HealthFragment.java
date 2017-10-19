package com.liu.oldsystem.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.liu.oldsystem.R;
import com.liu.oldsystem.health.HeartRateActivity;
import com.liu.oldsystem.health.MemoActivity;
import com.liu.oldsystem.health.SleepActivity;
import com.liu.oldsystem.health.StepActivity;
import com.liu.oldsystem.location.FixPositionActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HealthFragment extends Fragment {


    public HealthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_health, container, false);
        Button pedometer_btn_click = view.findViewById(R.id.pedometer_btn);
        pedometer_btn_click.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), StepActivity.class);
                startActivity(intent);
            }
        });
        Button memo_btn_click = view.findViewById(R.id.memoire_btn);
        memo_btn_click.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), MemoActivity.class);
                startActivity(intent);
            }
        });
        Button sleep_btn_click = view.findViewById(R.id.sleep_btn);
        sleep_btn_click.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), SleepActivity.class);
                startActivity(intent);
            }
        });
        Button heart_btn_click = view.findViewById(R.id.heart_rate_btn);
        heart_btn_click.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), HeartRateActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
