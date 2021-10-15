package com.cikarastudio.cikarajantungdesafix.ui.lapak;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.databinding.FragmentLapakBinding;

public class LapakFragment extends Fragment {

    LinearLayout line_tokojaya, line_tehManis;
    TextView tv_tambahProduk;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lapak, container, false);

        line_tokojaya = root.findViewById(R.id.line_tokojaya);
        tv_tambahProduk = root.findViewById(R.id.tv_tambahProduk);
        line_tehManis = root.findViewById(R.id.line_tehManis);


        line_tokojaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent keEditLapak = new Intent(getActivity(), TambahLapakActivity.class);
                startActivity(keEditLapak);
            }
        });

        tv_tambahProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent keTambahProduk = new Intent(getActivity(), TambahProdukActivity.class);
                startActivity(keTambahProduk);
            }
        });

        line_tehManis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent keEditProduk = new Intent(getActivity(), EditProdukActivity.class);
                startActivity(keEditProduk);

            }
        });

        return root;
    }
}