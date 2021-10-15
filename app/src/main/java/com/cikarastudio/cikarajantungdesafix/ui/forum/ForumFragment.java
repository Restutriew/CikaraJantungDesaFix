package com.cikarastudio.cikarajantungdesafix.ui.forum;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.cikarastudio.cikarajantungdesafix.R;

public class ForumFragment extends Fragment {

    LinearLayout line_sampah;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_forum, container, false);

        line_sampah = root.findViewById(R.id.line_sampah);

        line_sampah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent keChat = new Intent(getActivity(),ChatForumActivity.class);
                startActivity(keChat);
            }
        });

        return root;
    }
}