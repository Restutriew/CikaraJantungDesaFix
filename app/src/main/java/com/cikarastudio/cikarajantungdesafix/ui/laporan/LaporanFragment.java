package com.cikarastudio.cikarajantungdesafix.ui.laporan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.ui.profil.ProfilActivity;

public class LaporanFragment extends Fragment {

    TextView et_keFormLaporan;
    ImageView img_profil;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_laporan, container, false);

        et_keFormLaporan = root.findViewById(R.id.et_keFormLaporan);
//        img_profil = root.findViewById(R.id.img_profil);

        et_keFormLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent keFormLaporan = new Intent(getActivity(),TambahLaporanActivity.class);
                startActivity(keFormLaporan);
            }
        });

//        img_profil.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent keProfil = new Intent(getActivity(), ProfilActivity.class);
//                startActivity(keProfil);
//            }
//        });

        return root;

    }

}