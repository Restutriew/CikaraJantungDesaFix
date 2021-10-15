package com.cikarastudio.cikarajantungdesafix.ui.laporan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.cikarastudio.cikarajantungdesafix.R;

public class TambahLaporanActivity extends AppCompatActivity {

    Spinner sp_jenisLaporan;
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_laporan);

        sp_jenisLaporan = findViewById(R.id.sp_jenisLaporan);
        img_back= findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        ArrayAdapter<String> jenisLaporanAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.jenis_laporan));
        sp_jenisLaporan.setAdapter(jenisLaporanAdapter);

    }
}