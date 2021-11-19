package com.cikarastudio.cikarajantungdesafix.ui.artikel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.model.ArtikelModel;
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;
import com.squareup.picasso.Picasso;

public class DetailArtikelActivity extends AppCompatActivity {

    public static final String ARTIKEL_DATA = "extra_data";
    ImageView img_back, img_share, img_gambarArtikelDeskripsi;
    TextView tv_judulArtikelDeskripsi, tv_isiArtikelDeskripsi;
    String linkGambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_artikel);

        ArtikelModel modelData = getIntent().getParcelableExtra(ARTIKEL_DATA);
        String judul = modelData.getJudul_artikel();
        String isi = modelData.getIsi_artikel();
        String gambar = modelData.getGambar_artikel();

        HttpsTrustManager.allowAllSSL();

        //inisiasi link
        linkGambar = getString(R.string.linkGambar);

        tv_judulArtikelDeskripsi = findViewById(R.id.tv_judulArtikelDeskripsi);
        tv_isiArtikelDeskripsi = findViewById(R.id.tv_isiArtikelDeskripsi);
        img_gambarArtikelDeskripsi = findViewById(R.id.img_gambarArtikelDeskripsi);


        TextFuntion textFuntion = new TextFuntion();
        //data kategori
        textFuntion.setTextDanNullData(tv_judulArtikelDeskripsi, judul);

        String imageUrl = linkGambar + "pengaturan/artikel/" + gambar;
        Picasso.with(getApplicationContext()).load(imageUrl).fit().centerCrop().into(img_gambarArtikelDeskripsi);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_isiArtikelDeskripsi.setText(Html.fromHtml(isi, Html.FROM_HTML_MODE_COMPACT));
        } else {
            tv_isiArtikelDeskripsi.setText(Html.fromHtml(isi));
        }

    }
}