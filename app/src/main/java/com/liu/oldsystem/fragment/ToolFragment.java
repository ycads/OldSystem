package com.liu.oldsystem.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.liu.oldsystem.R;
import com.liu.oldsystem.health.MemoActivity;
import com.liu.oldsystem.health.StepActivity;
import com.liu.oldsystem.tool.FlashLightActivity;
import com.liu.oldsystem.tool.MagnifierActivity;
import com.liu.oldsystem.tool.ScreenLightActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ToolFragment extends Fragment {


    public ToolFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tool, container, false);
        Button flashlight_btn_click = view.findViewById(R.id.flashlight_btn);
        flashlight_btn_click.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), FlashLightActivity.class);
                startActivity(intent);
            }
        });
        Button brightness_btn_click = view.findViewById(R.id.brightness_btn);
        brightness_btn_click.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), ScreenLightActivity.class);
                startActivity(intent);
            }
        });
        Button magnifier_btn_click = view.findViewById(R.id.magnifier_btn);
        magnifier_btn_click.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), MagnifierActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
