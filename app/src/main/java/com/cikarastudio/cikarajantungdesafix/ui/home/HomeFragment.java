package com.cikarastudio.cikarajantungdesafix.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.ui.profil.ProfilActivity;


public class HomeFragment extends Fragment {

    CardView cr_fotoProfil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        cr_fotoProfil = root.findViewById(R.id.cr_fotoProfil);

        cr_fotoProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent keProfil = new Intent(getActivity(), ProfilActivity.class);
                startActivity(keProfil);
            }
        });

        return root;
    }
}