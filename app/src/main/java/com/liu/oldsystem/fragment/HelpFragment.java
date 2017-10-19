package com.liu.oldsystem.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.liu.oldsystem.R;
import com.liu.oldsystem.help.ContactActivity;
import com.liu.oldsystem.help.FallActivity;
import com.liu.oldsystem.help.MessageActivity;
import com.liu.oldsystem.help.TelphoneActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {

    public HelpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        Button fall_click = view.findViewById(R.id.fall_btn);
        Button people_click = view.findViewById(R.id.people_btn);
        Button tel_click = view.findViewById(R.id.tel_btn);
        Button sms_click = view.findViewById(R.id.sms_btn);
        fall_click.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),FallActivity.class);
                startActivity(intent);
            }
        });
        people_click.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),ContactActivity.class);
                startActivity(intent);
            }
        });
        tel_click.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),TelphoneActivity.class);
                startActivity(intent);
            }
        });
        sms_click.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),MessageActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
