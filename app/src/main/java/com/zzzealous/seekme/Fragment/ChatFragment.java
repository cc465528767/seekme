package com.zzzealous.seekme.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zzzealous.seekme.Activity.Mine.MineAddressActivity;
import com.zzzealous.seekme.Activity.Mine.MineMineActivity;
import com.zzzealous.seekme.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    private LinearLayout addlayout;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_chat, container, false);
     /*  addlayout = view.findViewById(R.id.address_button);
       addlayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(getActivity(), MineAddressActivity.class);
               startActivity(intent);
           }
       });*/
    return view;
    }


}
