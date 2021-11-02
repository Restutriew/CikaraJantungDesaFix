package com.cikarastudio.cikarajantungdesafix.ui.surat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.cikarastudio.cikarajantungdesafix.R;

public class ListTambahSuratActivity extends AppCompatActivity {

    LinearLayout line_ketPengantar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tambah_surat);

        line_ketPengantar = findViewById(R.id.line_ketPengantar);

        line_ketPengantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent keSuratKK = new Intent(ListTambahSuratActivity.this, TambahSuratActivity.class);
                startActivity(keSuratKK);
            }
        });
    }
}