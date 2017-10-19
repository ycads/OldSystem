package com.liu.oldsystem.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.liu.oldsystem.R;
import com.liu.oldsystem.help.FallActivity;
import com.liu.oldsystem.location.FixPositionActivity;
import com.liu.oldsystem.location.NavigationActivity;
import com.liu.oldsystem.location.ShakeActivity;
import com.liu.oldsystem.location.WeatherMainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment {


    public LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        Button position_click = view.findViewById(R.id.poision_btn);
        position_click.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), FixPositionActivity.class);
                startActivity(intent);
            }
        });
        Button navigation_click = view.findViewById(R.id.navigation_btn);
        navigation_click.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), NavigationActivity.class);
                startActivity(intent);
            }
        });
        Button weather_click = view.findViewById(R.id.weather_btn);
        weather_click.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), WeatherMainActivity.class);
                startActivity(intent);
            }
        });
        Button shake_click = view.findViewById(R.id.shake_btn);
        shake_click.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), ShakeActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
