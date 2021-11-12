package com.cikarastudio.cikarajantungdesafix.ui.surat;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cikarastudio.cikarajantungdesafix.R;

public class SuratFragment extends Fragment {

    ImageView img_tambahSurat;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_surat, container, false);


        img_tambahSurat = root.findViewById(R.id.img_tambahSurat);
        img_tambahSurat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent keTambahSurat = new Intent(getActivity(), ListTambahSuratActivity.class);
                startActivity(keTambahSurat);
            }
        });


        return root;
    }

}