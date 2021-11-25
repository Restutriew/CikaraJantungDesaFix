package com.cikarastudio.cikarajantungdesafix.ui.surat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.ui.lapak.EditLapakActivity;

public class ListKategoriSuratActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView img_back;
    CardView cr_suratKeterangan, cr_suratPengantar, cr_suratRekomendasi, cr_suratLainnya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_kategori_surat);

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        cr_suratKeterangan = findViewById(R.id.cr_suratKeterangan);
        cr_suratKeterangan.setOnClickListener(this);

        cr_suratPengantar = findViewById(R.id.cr_suratPengantar);
        cr_suratPengantar.setOnClickListener(this);

        cr_suratRekomendasi = findViewById(R.id.cr_suratRekomendasi);
        cr_suratRekomendasi.setOnClickListener(this);

        cr_suratLainnya = findViewById(R.id.cr_suratLainnya);
        cr_suratLainnya.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                // do your code
                finish();
                break;
            case R.id.cr_suratKeterangan:
                // do your code
                Intent keListTambahSuratKeterangan = new Intent(ListKategoriSuratActivity.this, ListTambahSuratActivity.class);
                keListTambahSuratKeterangan.putExtra("kategori_surat", "surat keterangan");
                startActivity(keListTambahSuratKeterangan);
                break;
            case R.id.cr_suratPengantar:
                // do your code
                Intent keListTambahSuratPengantar = new Intent(ListKategoriSuratActivity.this, ListTambahSuratActivity.class);
                keListTambahSuratPengantar.putExtra("kategori_surat", "surat pengantar");
                startActivity(keListTambahSuratPengantar);
                break;
            case R.id.cr_suratRekomendasi:
                // do your code
                Intent keListTambahSuratRekomendasi = new Intent(ListKategoriSuratActivity.this, ListTambahSuratActivity.class);
                keListTambahSuratRekomendasi.putExtra("kategori_surat", "surat rekomendasi");
                startActivity(keListTambahSuratRekomendasi);
                break;
            case R.id.cr_suratLainnya:
                // do your code
                Intent keListTambahSuratLainnya = new Intent(ListKategoriSuratActivity.this, ListTambahSuratActivity.class);
                keListTambahSuratLainnya.putExtra("kategori_surat", "surat lainnya");
                startActivity(keListTambahSuratLainnya);
                break;
            default:
                break;
        }
    }
}