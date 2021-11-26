package com.cikarastudio.cikarajantungdesafix.ui.surat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.model.SuratListModel;
import com.cikarastudio.cikarajantungdesafix.session.SessionManager;
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TambahSuratActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DATA_FORMAT_SURAT = "extra_data";
    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    TextFuntion textFuntion;
    ImageView img_back;
    String link, id_user, status, idSurat, namaSurat, kodeSurat;
    TextView tv_namaSurat;
    EditText et_keperluanSurat, et_keteranganSurat, et_kepalaKKSurat, et_noKKSurat, et_rtTujuanSurat,
            et_rwTujuanSurat, et_dusunTujuanSurat, et_desaTujuanSurat, et_kecamatanTujuanSurat,
            et_kabupatenTujuanSurat, et_alasanPindahSurat, et_tanggalPindahSurat, et_jumlahPengikutSurat,
            et_barangSurat, et_jenisSurat, et_namaSurat, et_noIdentitasSurat, et_tempatLahirSurat,
            et_tglLahirSurat, et_jenisKelaminSurat, et_alamatSurat, et_pekerjaanSurat, et_ketuaAdatSurat,
            et_agamaSurat, et_perbedaanSurat, et_kartuIdentitasSurat, et_rincianSurat, et_usahaSurat,
            et_noJamkesosSurat, et_hariLahirSurat, et_waktuLahirSurat, et_kelahiranKeSurat, et_namaIbuSurat,
            et_nikIbuSurat, et_umurIbuSurat, et_pekerjaanIbuSurat, et_alamatIbuSurat, et_desaIbuSurat,
            et_kecamatanIbuSurat, et_kabupatenIbuSurat, et_namaAyahSurat, et_nikAyahSurat, et_umurAyahSurat,
            et_pekerjaanAyahSurat, et_alamatAyahSurat, et_desaAyahSurat, et_kecamatanAyahSurat,
            et_kabupatenAyahSurat, et_namaPelaporSurat, et_nikPelaporSurat, et_umurPelaporSurat,
            et_pekerjaanPelaporSurat, et_desaPelaporSurat, et_kecamatanPelaporSurat, et_kabupatenPelaporSurat,
            et_provinsiPelaporSurat, et_hubPelaporSurat, et_tempatLahirPelaporSurat, et_tanggalLahirPelaporSurat,
            et_namaSaksi1Surat, et_nikSaksi1Surat, et_tempatLahirSaksi1Surat, et_tanggalLahirSaksi1Surat,
            et_umurSaksi1Surat, et_pekerjaanSaksi1Surat, et_desaSaksi1Surat, et_kecamatanSaksi1Surat,
            et_kabupatenSaksi1Surat, et_provinsiSaksi1Surat, et_namaSaksi2Surat, et_nikSaksi2Surat,
            et_tempatLahirSaksi2Surat, et_tanggalLahirSaksi2Surat, et_umurSaksi2Surat, et_pekerjaanSaksi2Surat,
            et_desaSaksi2Surat, et_kecamatanSaksi2Surat, et_kabupatenSaksi2Surat, et_provinsiSaksi2Surat;

    TextInputLayout til_keperluanSurat, til_keteranganSurat, til_kepalaKKSurat, til_noKKSurat, til_rtTujuanSurat,
            til_rwRujuanSurat, til_dusunTujuanSurat, til_desaTujuanSurat, til_kecamatanTujuanSurat,
            til_kabupatenTujuanSurat, til_alasanPindahSurat, til_tanggalPindahSurat, til_jumlahPengikutSurat,
            til_barangSurat, til_jenisSurat, til_namaSurat, til_noIdentitasSurat, til_tempatLahirSurat,
            til_tglLahirSurat, til_jenisKelaminSurat, til_alamatSurat, til_pekerjaanSurat, til_ketuaAdatSurat,
            til_agamaSurat, til_perbedaanSurat, til_kartuIdentitasSurat, til_rincianSurat, til_usahaSurat,
            til_noJamkesosSurat, til_hariLahirSurat, til_waktuLahirSurat, til_kelahiranKeSurat, til_namaIbuSurat,
            til_nikIbuSurat, til_umurIbuSurat, til_pekerjaanIbuSurat, til_alamatIbuSurat, til_desaIbuSurat,
            til_kecamatanIbuSurat, til_kabupatenIbuSurat, til_namaAyahSurat, til_nikAyahSurat, til_umurAyahSurat,
            til_pekerjaanAyahSurat, til_alamatAyahSurat, til_desaAyahSurat, til_kecamatanAyahSurat,
            til_kabupatenAyahSurat, til_namaPelaporSurat, til_nikPelaporSurat, til_umurPelaporSurat,
            til_pekerjaanPelaporSurat, til_desaPelaporSurat, til_kecamatanPelaporSurat, til_kabupatenPelaporSurat,
            til_provinsiPelaporSurat, til_hubPelaporSurat, til_tempatLahirPelaporSurat, til_tanggalLahirPelaporSurat,
            til_namaSaksi1Surat, til_nikSaksi1Surat, til_tempatLahirSaksi1Surat, til_tanggalLahirSaksi1Surat,
            til_umurSaksi1Surat, til_pekerjaanSaksi1Surat, til_desaSaksi1Surat, til_kecamatanSaksi1Surat,
            til_kabupatenSaksi1Surat, til_provinsiSaksi1Surat, til_namaSaksi2Surat, til_nikSaksi2Surat,
            til_tempatLahirSaksi2Surat, til_tanggalLahirSaksi2Surat, til_umurSaksi2Surat, til_pekerjaanSaksi2Surat,
            til_desaSaksi2Surat, til_kecamatanSaksi2Surat, til_kabupatenSaksi2Surat, til_provinsiSaksi2Surat;
    CardView cr_ajukanSurat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_surat);

        SuratListModel modelData = getIntent().getParcelableExtra(DATA_FORMAT_SURAT);
        idSurat = modelData.getId();
        kodeSurat = modelData.getKode();
        namaSurat = modelData.getNama_surat();

        sessionManager = new SessionManager(TambahSuratActivity.this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        id_user = user.get(sessionManager.ID);

        status = "menunggu";

        HttpsTrustManager.allowAllSSL();

        loadingDialog = new LoadingDialog(TambahSuratActivity.this);
        loadingDialog.startLoading();

        //inisiasi link
        link = getString(R.string.link);

        tv_namaSurat = findViewById(R.id.tv_namaSurat);
        textFuntion = new TextFuntion();
        textFuntion.setTextDanNullData(tv_namaSurat, namaSurat);

        loadFormIsian();

        //edittext
        et_keperluanSurat = findViewById(R.id.et_keperluanSurat);
        et_keteranganSurat = findViewById(R.id.et_keteranganSurat);
        et_kepalaKKSurat = findViewById(R.id.et_kepalaKKSurat);
        et_noKKSurat = findViewById(R.id.et_noKKSurat);
        et_rtTujuanSurat = findViewById(R.id.et_rtTujuanSurat);
        et_rwTujuanSurat = findViewById(R.id.et_rwTujuanSurat);
        et_dusunTujuanSurat = findViewById(R.id.et_dusunTujuanSurat);
        et_desaTujuanSurat = findViewById(R.id.et_desaTujuanSurat);
        et_kecamatanTujuanSurat = findViewById(R.id.et_kecamatanTujuanSurat);
        et_kabupatenTujuanSurat = findViewById(R.id.et_kabupatenTujuanSurat);
        et_alasanPindahSurat = findViewById(R.id.et_alasanPindahSurat);
        et_tanggalPindahSurat = findViewById(R.id.et_tanggalPindahSurat);
        et_jumlahPengikutSurat = findViewById(R.id.et_jumlahPengikutSurat);
        et_barangSurat = findViewById(R.id.et_barangSurat);
        et_jenisSurat = findViewById(R.id.et_jenisSurat);
        et_namaSurat = findViewById(R.id.et_namaSurat);
        et_noIdentitasSurat = findViewById(R.id.et_noIdentitasSurat);
        et_tempatLahirSurat = findViewById(R.id.et_tempatLahirSurat);
        et_tglLahirSurat = findViewById(R.id.et_tglLahirSurat);
        et_jenisKelaminSurat = findViewById(R.id.et_jenisKelaminSurat);
        et_alamatSurat = findViewById(R.id.et_alamatSurat);
        et_pekerjaanSurat = findViewById(R.id.et_pekerjaanSurat);
        et_ketuaAdatSurat = findViewById(R.id.et_ketuaAdatSurat);
        et_agamaSurat = findViewById(R.id.et_agamaSurat);
        et_perbedaanSurat = findViewById(R.id.et_perbedaanSurat);
        et_kartuIdentitasSurat = findViewById(R.id.et_kartuIdentitasSurat);
        et_rincianSurat = findViewById(R.id.et_rincianSurat);
        et_usahaSurat = findViewById(R.id.et_usahaSurat);
        et_noJamkesosSurat = findViewById(R.id.et_noJamkesosSurat);
        et_hariLahirSurat = findViewById(R.id.et_hariLahirSurat);
        et_waktuLahirSurat = findViewById(R.id.et_waktuLahirSurat);
        et_kelahiranKeSurat = findViewById(R.id.et_kelahiranKeSurat);
        et_namaIbuSurat = findViewById(R.id.et_namaIbuSurat);
        et_nikIbuSurat = findViewById(R.id.et_nikIbuSurat);
        et_umurIbuSurat = findViewById(R.id.et_umurIbuSurat);
        et_pekerjaanIbuSurat = findViewById(R.id.et_pekerjaanIbuSurat);
        et_alamatIbuSurat = findViewById(R.id.et_alamatIbuSurat);
        et_desaIbuSurat = findViewById(R.id.et_desaIbuSurat);
        et_kecamatanIbuSurat = findViewById(R.id.et_kecamatanIbuSurat);
        et_kabupatenIbuSurat = findViewById(R.id.et_kabupatenIbuSurat);
        et_namaAyahSurat = findViewById(R.id.et_namaAyahSurat);
        et_nikAyahSurat = findViewById(R.id.et_nikAyahSurat);
        et_umurAyahSurat = findViewById(R.id.et_umurAyahSurat);
        et_pekerjaanAyahSurat = findViewById(R.id.et_pekerjaanAyahSurat);
        et_alamatAyahSurat = findViewById(R.id.et_alamatAyahSurat);
        et_desaAyahSurat = findViewById(R.id.et_desaAyahSurat);
        et_kecamatanAyahSurat = findViewById(R.id.et_kecamatanAyahSurat);
        et_kabupatenAyahSurat = findViewById(R.id.et_kabupatenAyahSurat);
        et_namaPelaporSurat = findViewById(R.id.et_namaPelaporSurat);
        et_nikPelaporSurat = findViewById(R.id.et_nikPelaporSurat);
        et_umurPelaporSurat = findViewById(R.id.et_umurPelaporSurat);
        et_pekerjaanPelaporSurat = findViewById(R.id.et_pekerjaanPelaporSurat);
        et_desaPelaporSurat = findViewById(R.id.et_desaPelaporSurat);
        et_kecamatanPelaporSurat = findViewById(R.id.et_kecamatanPelaporSurat);
        et_kabupatenPelaporSurat = findViewById(R.id.et_kabupatenPelaporSurat);
        et_provinsiPelaporSurat = findViewById(R.id.et_provinsiPelaporSurat);
        et_hubPelaporSurat = findViewById(R.id.et_hubPelaporSurat);
        et_tempatLahirPelaporSurat = findViewById(R.id.et_tempatLahirPelaporSurat);
        et_tanggalLahirPelaporSurat = findViewById(R.id.et_tanggalLahirPelaporSurat);
        et_namaSaksi1Surat = findViewById(R.id.et_namaSaksi1Surat);
        et_nikSaksi1Surat = findViewById(R.id.et_nikSaksi1Surat);
        et_tempatLahirSaksi1Surat = findViewById(R.id.et_tempatLahirSaksi1Surat);
        et_tanggalLahirSaksi1Surat = findViewById(R.id.et_tanggalLahirSaksi1Surat);
        et_umurSaksi1Surat = findViewById(R.id.et_umurSaksi1Surat);
        et_pekerjaanSaksi1Surat = findViewById(R.id.et_pekerjaanSaksi1Surat);
        et_desaSaksi1Surat = findViewById(R.id.et_desaSaksi1Surat);
        et_kecamatanSaksi1Surat = findViewById(R.id.et_kecamatanSaksi1Surat);
        et_kabupatenSaksi1Surat = findViewById(R.id.et_kabupatenSaksi1Surat);
        et_provinsiSaksi1Surat = findViewById(R.id.et_provinsiSaksi1Surat);
        et_namaSaksi2Surat = findViewById(R.id.et_namaSaksi2Surat);
        et_nikSaksi2Surat = findViewById(R.id.et_nikSaksi2Surat);
        et_tempatLahirSaksi2Surat = findViewById(R.id.et_tempatLahirSaksi2Surat);
        et_tanggalLahirSaksi2Surat = findViewById(R.id.et_tanggalLahirSaksi2Surat);
        et_umurSaksi2Surat = findViewById(R.id.et_umurSaksi2Surat);
        et_pekerjaanSaksi2Surat = findViewById(R.id.et_pekerjaanSaksi2Surat);
        et_desaSaksi2Surat = findViewById(R.id.et_desaSaksi2Surat);
        et_kecamatanSaksi2Surat = findViewById(R.id.et_kecamatanSaksi2Surat);
        et_kabupatenSaksi2Surat = findViewById(R.id.et_kabupatenSaksi2Surat);
        et_provinsiSaksi2Surat = findViewById(R.id.et_provinsiSaksi2Surat);

        //textinputlayout
        til_keperluanSurat = findViewById(R.id.til_keperluanSurat);
        til_keteranganSurat = findViewById(R.id.til_keteranganSurat);
        til_kepalaKKSurat = findViewById(R.id.til_kepalaKKSurat);
        til_noKKSurat = findViewById(R.id.til_noKKSurat);
        til_rtTujuanSurat = findViewById(R.id.til_rtTujuanSurat);
        til_rwRujuanSurat = findViewById(R.id.til_rwRujuanSurat);
        til_dusunTujuanSurat = findViewById(R.id.til_dusunTujuanSurat);
        til_desaTujuanSurat = findViewById(R.id.til_desaTujuanSurat);
        til_kecamatanTujuanSurat = findViewById(R.id.til_kecamatanTujuanSurat);
        til_kabupatenTujuanSurat = findViewById(R.id.til_kabupatenTujuanSurat);
        til_alasanPindahSurat = findViewById(R.id.til_alasanPindahSurat);
        til_tanggalPindahSurat = findViewById(R.id.til_tanggalPindahSurat);
        til_jumlahPengikutSurat = findViewById(R.id.til_jumlahPengikutSurat);
        til_barangSurat = findViewById(R.id.til_barangSurat);
        til_jenisSurat = findViewById(R.id.til_jenisSurat);
        til_namaSurat = findViewById(R.id.til_namaSurat);
        til_noIdentitasSurat = findViewById(R.id.til_noIdentitasSurat);
        til_tempatLahirSurat = findViewById(R.id.til_tempatLahirSurat);
        til_tglLahirSurat = findViewById(R.id.til_tglLahirSurat);
        til_jenisKelaminSurat = findViewById(R.id.til_jenisKelaminSurat);
        til_alamatSurat = findViewById(R.id.til_alamatSurat);
        til_pekerjaanSurat = findViewById(R.id.til_pekerjaanSurat);
        til_ketuaAdatSurat = findViewById(R.id.til_ketuaAdatSurat);
        til_agamaSurat = findViewById(R.id.til_agamaSurat);
        til_perbedaanSurat = findViewById(R.id.til_perbedaanSurat);
        til_kartuIdentitasSurat = findViewById(R.id.til_kartuIdentitasSurat);
        til_rincianSurat = findViewById(R.id.til_rincianSurat);
        til_usahaSurat = findViewById(R.id.til_usahaSurat);
        til_noJamkesosSurat = findViewById(R.id.til_noJamkesosSurat);
        til_hariLahirSurat = findViewById(R.id.til_hariLahirSurat);
        til_waktuLahirSurat = findViewById(R.id.til_waktuLahirSurat);
        til_kelahiranKeSurat = findViewById(R.id.til_kelahiranKeSurat);
        til_namaIbuSurat = findViewById(R.id.til_namaIbuSurat);
        til_nikIbuSurat = findViewById(R.id.til_nikIbuSurat);
        til_umurIbuSurat = findViewById(R.id.til_umurIbuSurat);
        til_pekerjaanIbuSurat = findViewById(R.id.til_pekerjaanIbuSurat);
        til_alamatIbuSurat = findViewById(R.id.til_alamatIbuSurat);
        til_desaIbuSurat = findViewById(R.id.til_desaIbuSurat);
        til_kecamatanIbuSurat = findViewById(R.id.til_kecamatanIbuSurat);
        til_kabupatenIbuSurat = findViewById(R.id.til_kabupatenIbuSurat);
        til_namaAyahSurat = findViewById(R.id.til_namaAyahSurat);
        til_nikAyahSurat = findViewById(R.id.til_nikAyahSurat);
        til_umurAyahSurat = findViewById(R.id.til_umurAyahSurat);
        til_pekerjaanAyahSurat = findViewById(R.id.til_pekerjaanAyahSurat);
        til_alamatAyahSurat = findViewById(R.id.til_alamatAyahSurat);
        til_desaAyahSurat = findViewById(R.id.til_desaAyahSurat);
        til_kecamatanAyahSurat = findViewById(R.id.til_kecamatanAyahSurat);
        til_kabupatenAyahSurat = findViewById(R.id.til_kabupatenAyahSurat);
        til_namaPelaporSurat = findViewById(R.id.til_namaPelaporSurat);
        til_nikPelaporSurat = findViewById(R.id.til_nikPelaporSurat);
        til_umurPelaporSurat = findViewById(R.id.til_umurPelaporSurat);
        til_pekerjaanPelaporSurat = findViewById(R.id.til_pekerjaanPelaporSurat);
        til_desaPelaporSurat = findViewById(R.id.til_desaPelaporSurat);
        til_kecamatanPelaporSurat = findViewById(R.id.til_kecamatanPelaporSurat);
        til_kabupatenPelaporSurat = findViewById(R.id.til_kabupatenPelaporSurat);
        til_provinsiPelaporSurat = findViewById(R.id.til_provinsiPelaporSurat);
        til_hubPelaporSurat = findViewById(R.id.til_hubPelaporSurat);
        til_tempatLahirPelaporSurat = findViewById(R.id.til_tempatLahirPelaporSurat);
        til_tanggalLahirPelaporSurat = findViewById(R.id.til_tanggalLahirPelaporSurat);
        til_namaSaksi1Surat = findViewById(R.id.til_namaSaksi1Surat);
        til_nikSaksi1Surat = findViewById(R.id.til_nikSaksi1Surat);
        til_tempatLahirSaksi1Surat = findViewById(R.id.til_tempatLahirSaksi1Surat);
        til_tanggalLahirSaksi1Surat = findViewById(R.id.til_tanggalLahirSaksi1Surat);
        til_umurSaksi1Surat = findViewById(R.id.til_umurSaksi1Surat);
        til_pekerjaanSaksi1Surat = findViewById(R.id.til_pekerjaanSaksi1Surat);
        til_desaSaksi1Surat = findViewById(R.id.til_desaSaksi1Surat);
        til_kecamatanSaksi1Surat = findViewById(R.id.til_kecamatanSaksi1Surat);
        til_kabupatenSaksi1Surat = findViewById(R.id.til_kabupatenSaksi1Surat);
        til_provinsiSaksi1Surat = findViewById(R.id.til_provinsiSaksi1Surat);
        til_namaSaksi2Surat = findViewById(R.id.til_namaSaksi2Surat);
        til_nikSaksi2Surat = findViewById(R.id.til_nikSaksi2Surat);
        til_tempatLahirSaksi2Surat = findViewById(R.id.til_tempatLahirSaksi2Surat);
        til_tanggalLahirSaksi2Surat = findViewById(R.id.til_tanggalLahirSaksi2Surat);
        til_umurSaksi2Surat = findViewById(R.id.til_umurSaksi2Surat);
        til_pekerjaanSaksi2Surat = findViewById(R.id.til_pekerjaanSaksi2Surat);
        til_desaSaksi2Surat = findViewById(R.id.til_desaSaksi2Surat);
        til_kecamatanSaksi2Surat = findViewById(R.id.til_kecamatanSaksi2Surat);
        til_kabupatenSaksi2Surat = findViewById(R.id.til_kabupatenSaksi2Surat);
        til_provinsiSaksi2Surat = findViewById(R.id.til_provinsiSaksi2Surat);

        cr_ajukanSurat = findViewById(R.id.cr_ajukanSurat);
        cr_ajukanSurat.setOnClickListener(this);

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                // do your code
                finish();
                break;
            case R.id.cr_ajukanSurat:
                // do your code
                addAjuanSurat();
                break;
            default:
                break;
        }
    }

    private void loadFormIsian() {
        String URL_READ = link + "formatsuratbykode/" + kodeSurat + "?versi=3";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    //data form isian surat
                                    String res_form = jsonObject.getString("form").trim();
                                    String res_label = jsonObject.getString("label").trim();

                                    filterFormSurat(res_form, res_label);

                                    Log.d("calpalnx", "onResponse: " + res_form);

                                    //hilangkan loading
                                    loadingDialog.dissmissDialog();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Data List Surat Tidak Ada!", Toast.LENGTH_SHORT).show();
                                loadingDialog.dissmissDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingDialog.dissmissDialog();
                            Toast.makeText(getApplicationContext(), "Data List Surat Tidak Ada!", Toast.LENGTH_LONG).show();
                        }

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dissmissDialog();
                Toast.makeText(getApplicationContext(), "Tidak Ada Koneksi Internet!" + error, Toast.LENGTH_LONG).show();
            }
        }) {
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void filterFormSurat(String res_form, String res_label) {
        switch (res_form) {
            case "keperluan":
                til_keperluanSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_keperluanSurat, res_label);
                break;
            case "keterangan":
                til_keteranganSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_keteranganSurat, res_label);
                break;
            case "kepala_kk":
                til_kepalaKKSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_kepalaKKSurat, res_label);
                break;
            case "no_kk":
                til_noKKSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_noKKSurat, res_label);
                break;
            case "rt_tujuan":
                til_rtTujuanSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_rtTujuanSurat, res_label);
                break;
            case "rw_tujuan":
                til_rwRujuanSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_rwRujuanSurat, res_label);
                break;
            case "dusun_tujuan":
                til_dusunTujuanSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_dusunTujuanSurat, res_label);
                break;
            case "desa_tujuan":
                til_desaTujuanSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_desaTujuanSurat, res_label);
                break;
            case "kecamatan_tujuan":
                til_kecamatanTujuanSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_kecamatanTujuanSurat, res_label);
                break;
            case "kabupaten_tujuan":
                til_kabupatenTujuanSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_kabupatenTujuanSurat, res_label);
                break;
            case "alasan_pindah":
                til_alasanPindahSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_alasanPindahSurat, res_label);
                break;
            case "tanggal_pindah":
                til_tanggalPindahSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_tanggalPindahSurat, res_label);
                break;
            case "jumlah_pengikut":
                til_jumlahPengikutSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_jumlahPengikutSurat, res_label);
                break;
            case "barang":
                til_barangSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_barangSurat, res_label);
                break;
            case "jenis":
                til_jenisSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_jenisSurat, res_label);
                break;
            case "nama":
                til_namaSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_namaSurat, res_label);
                break;
            case "no_identitas":
                til_noIdentitasSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_noIdentitasSurat, res_label);
                break;
            case "tempat_lahir":
                til_tempatLahirSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_tempatLahirSurat, res_label);
                break;
            case "tgl_lahir":
                til_tglLahirSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_tglLahirSurat, res_label);
                break;
            case "jk":
                til_jenisKelaminSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_jenisKelaminSurat, res_label);
                break;
            case "alamat":
                til_alamatSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_alamatSurat, res_label);
                break;
            case "pekerjaan":
                til_pekerjaanSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_pekerjaanSurat, res_label);
                break;
            case "ketua_adat":
                til_ketuaAdatSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_ketuaAdatSurat, res_label);
                break;
            case "agama":
                til_agamaSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_agamaSurat, res_label);
                break;
            case "perbedaan":
                til_perbedaanSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_perbedaanSurat, res_label);
                break;
            case "kartu_identitas":
                til_kartuIdentitasSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_kartuIdentitasSurat, res_label);
                break;
            case "rincian":
                til_rincianSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_rincianSurat, res_label);
                break;
            case "usaha":
                til_usahaSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_usahaSurat, res_label);
                break;
            case "no_jamkesos":
                til_noJamkesosSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_noJamkesosSurat, res_label);
                break;
            case "hari_lahir":
                til_hariLahirSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_hariLahirSurat, res_label);
                break;
            case "waktu_lahir":
                til_waktuLahirSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_waktuLahirSurat, res_label);
                break;
            case "kelahiran_ke":
                til_kelahiranKeSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_kelahiranKeSurat, res_label);
                break;
            case "nama_ibu":
                til_namaIbuSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_namaIbuSurat, res_label);
                break;
            case "nik_ibu":
                til_nikIbuSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_nikIbuSurat, res_label);
                break;
            case "umur_ibu":
                til_umurIbuSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_umurIbuSurat, res_label);
                break;
            case "pekerjaan_ibu":
                til_pekerjaanIbuSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_pekerjaanIbuSurat, res_label);
                break;
            case "alamat_ibu":
                til_alamatIbuSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_alamatIbuSurat, res_label);
                break;
            case "desa_ibu":
                til_desaIbuSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_desaIbuSurat, res_label);
                break;
            case "kec_ibu":
                til_kecamatanIbuSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_kecamatanIbuSurat, res_label);
                break;
            case "kab_ibu":
                til_kabupatenIbuSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_kabupatenIbuSurat, res_label);
                break;
            case "nama_ayah":
                til_namaAyahSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_namaAyahSurat, res_label);
                break;
            case "nik_ayah":
                til_nikAyahSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_nikAyahSurat, res_label);
                break;
            case "umur_ayah":
                til_umurAyahSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_umurAyahSurat, res_label);
                break;
            case "pekerjaan_ayah":
                til_pekerjaanAyahSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_pekerjaanAyahSurat, res_label);
                break;
            case "alamat_ayah":
                til_alamatAyahSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_alamatAyahSurat, res_label);
                break;
            case "desa_ayah":
                til_desaAyahSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_desaAyahSurat, res_label);
                break;
            case "kec_ayah":
                til_kecamatanAyahSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_kecamatanAyahSurat, res_label);
                break;
            case "kab_ayah":
                til_kabupatenAyahSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_kabupatenAyahSurat, res_label);
                break;
            case "nama_pelapor":
                til_namaPelaporSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_namaPelaporSurat, res_label);
                break;
            case "nik_pelapor":
                til_nikPelaporSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_nikPelaporSurat, res_label);
                break;
            case "umur_pelapor":
                til_umurPelaporSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_umurPelaporSurat, res_label);
                break;
            case "pekerjaan_pelapor":
                til_pekerjaanPelaporSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_pekerjaanPelaporSurat, res_label);
                break;
            case "desa_pelapor":
                til_desaPelaporSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_desaPelaporSurat, res_label);
                break;
            case "kec_pelapor":
                til_kecamatanPelaporSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_kecamatanPelaporSurat, res_label);
                break;
            case "kab_pelapor":
                til_kabupatenPelaporSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_kabupatenPelaporSurat, res_label);
                break;
            case "prov_pelapor":
                til_provinsiPelaporSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_provinsiPelaporSurat, res_label);
                break;
            case "hub_pelapor":
                til_hubPelaporSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_hubPelaporSurat, res_label);
                break;
            case "tempat_lahir_pelapor":
                til_tempatLahirPelaporSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_tempatLahirPelaporSurat, res_label);
                break;
            case "tanggal_lahir_pelapor":
                til_tanggalLahirPelaporSurat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_tanggalLahirPelaporSurat, res_label);
                break;
            case "nama_saksi1":
                til_namaSaksi1Surat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_namaSaksi1Surat, res_label);
                break;
            case "nik_saksi1":
                til_nikSaksi1Surat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_nikSaksi1Surat, res_label);
                break;
            case "tempat_lahir_saksi1":
                til_tempatLahirSaksi1Surat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_tempatLahirSaksi1Surat, res_label);
                break;
            case "tanggal_lahir_saksi1":
                til_tanggalLahirSaksi1Surat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_tanggalLahirSaksi1Surat, res_label);
                break;
            case "umur_saksi1":
                til_umurSaksi1Surat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_umurSaksi1Surat, res_label);
                break;
            case "pekerjaan_saksi1":
                til_pekerjaanSaksi1Surat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_pekerjaanSaksi1Surat, res_label);
                break;
            case "desa_saksi1":
                til_desaSaksi1Surat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_desaSaksi1Surat, res_label);
                break;
            case "kec_saksi1":
                til_kecamatanSaksi1Surat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_kecamatanSaksi1Surat, res_label);
                break;
            case "kab_saksi1":
                til_kabupatenSaksi1Surat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_kabupatenSaksi1Surat, res_label);
                break;
            case "prov_saksi1":
                til_provinsiSaksi1Surat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_provinsiSaksi1Surat, res_label);
                break;
            case "nama_saksi2":
                til_namaSaksi2Surat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_namaSaksi2Surat, res_label);
                break;
            case "nik_saksi2":
                til_nikSaksi2Surat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_nikSaksi2Surat, res_label);
                break;
            case "tempat_lahir_saksi2":
                til_tempatLahirSaksi2Surat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_tempatLahirSaksi2Surat, res_label);
                break;
            case "tanggal_lahir_saksi2":
                til_tanggalLahirSaksi2Surat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_tanggalLahirSaksi2Surat, res_label);
                break;
            case "umur_saksi2":
                til_umurSaksi2Surat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_umurSaksi2Surat, res_label);
                break;
            case "pekerjaan_saksi2":
                til_pekerjaanSaksi2Surat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_pekerjaanSaksi2Surat, res_label);
                break;
            case "desa_saksi2":
                til_desaSaksi2Surat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_desaSaksi2Surat, res_label);
                break;
            case "kec_saksi2":
                til_kecamatanSaksi2Surat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_kecamatanSaksi2Surat, res_label);
                break;
            case "kab_saksi2":
                til_kabupatenSaksi2Surat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_kabupatenSaksi2Surat, res_label);
                break;
            case "prov_saksi2":
                til_provinsiSaksi2Surat.setVisibility(View.VISIBLE);
                textFuntion.setHintData(til_provinsiSaksi2Surat, res_label);
                break;
            default:
                break;
        }

    }

    private void addAjuanSurat() {

        final String keperluan = et_keperluanSurat.getText().toString().trim();
        final String keterangan = et_keteranganSurat.getText().toString().trim();
        final String kepala_kk = et_kepalaKKSurat.getText().toString().trim();
        final String no_kk = et_noKKSurat.getText().toString().trim();
        final String rt_tujuan = et_rtTujuanSurat.getText().toString().trim();
        final String rw_tujuan = et_rwTujuanSurat.getText().toString().trim();
        final String dusun_tujuan = et_dusunTujuanSurat.getText().toString().trim();
        final String desa_tujuan = et_desaTujuanSurat.getText().toString().trim();
        final String kecamatan_tujuan = et_kecamatanTujuanSurat.getText().toString().trim();
        final String kabupaten_tujuan = et_kabupatenTujuanSurat.getText().toString().trim();
        final String alasan_pindah = et_alasanPindahSurat.getText().toString().trim();
        final String tanggal_pindah = et_tanggalPindahSurat.getText().toString().trim();
        final String jumlah_pengikut = et_jumlahPengikutSurat.getText().toString().trim();
        final String barang = et_barangSurat.getText().toString().trim();
        final String jenis = et_jenisSurat.getText().toString().trim();
        final String nama = et_namaSurat.getText().toString().trim();
        final String no_identitas = et_noIdentitasSurat.getText().toString().trim();
        final String tempat_lahir = et_tempatLahirSurat.getText().toString().trim();
        final String tgl_lahir = et_tglLahirSurat.getText().toString().trim();
        final String jk = et_jenisKelaminSurat.getText().toString().trim();
        final String alamat = et_alamatSurat.getText().toString().trim();
        final String pekerjaan = et_pekerjaanSurat.getText().toString().trim();
        final String ketua_adat = et_ketuaAdatSurat.getText().toString().trim();
        final String agama = et_agamaSurat.getText().toString().trim();
        final String perbedaan = et_perbedaanSurat.getText().toString().trim();
        final String kartu_identitas = et_kartuIdentitasSurat.getText().toString().trim();
        final String rincian = et_rincianSurat.getText().toString().trim();
        final String usaha = et_usahaSurat.getText().toString().trim();
        final String no_jamkesos = et_noJamkesosSurat.getText().toString().trim();
        final String hari_lahir = et_hariLahirSurat.getText().toString().trim();
        final String waktu_lahir = et_waktuLahirSurat.getText().toString().trim();
        final String kelahiran_ke = et_kelahiranKeSurat.getText().toString().trim();
        final String nama_ibu = et_namaIbuSurat.getText().toString().trim();
        final String nik_ibu = et_nikIbuSurat.getText().toString().trim();
        final String umur_ibu = et_umurIbuSurat.getText().toString().trim();
        final String pekerjaan_ibu = et_pekerjaanIbuSurat.getText().toString().trim();
        final String alamat_ibu = et_alamatIbuSurat.getText().toString().trim();
        final String desa_ibu = et_desaIbuSurat.getText().toString().trim();
        final String kec_ibu = et_kecamatanIbuSurat.getText().toString().trim();
        final String kab_ibu = et_kabupatenIbuSurat.getText().toString().trim();
        final String nama_ayah = et_namaAyahSurat.getText().toString().trim();
        final String nik_ayah = et_nikAyahSurat.getText().toString().trim();
        final String umur_ayah = et_umurAyahSurat.getText().toString().trim();
        final String pekerjaan_ayah = et_pekerjaanAyahSurat.getText().toString().trim();
        final String alamat_ayah = et_alamatAyahSurat.getText().toString().trim();
        final String desa_ayah = et_desaAyahSurat.getText().toString().trim();
        final String kec_ayah = et_kecamatanAyahSurat.getText().toString().trim();
        final String kab_ayah = et_kabupatenAyahSurat.getText().toString().trim();
        final String nama_pelapor = et_namaPelaporSurat.getText().toString().trim();
        final String nik_pelapor = et_nikPelaporSurat.getText().toString().trim();
        final String umur_pelapor = et_umurPelaporSurat.getText().toString().trim();
        final String pekerjaan_pelapor = et_pekerjaanPelaporSurat.getText().toString().trim();
        final String desa_pelapor = et_desaPelaporSurat.getText().toString().trim();
        final String kec_pelapor = et_kecamatanPelaporSurat.getText().toString().trim();
        final String kab_pelapor = et_kabupatenPelaporSurat.getText().toString().trim();
        final String prov_pelapor = et_provinsiPelaporSurat.getText().toString().trim();
        final String hub_pelapor = et_hubPelaporSurat.getText().toString().trim();
        final String tempat_lahir_pelapor = et_tempatLahirPelaporSurat.getText().toString().trim();
        final String tanggal_lahir_pelapor = et_tanggalLahirPelaporSurat.getText().toString().trim();
        final String nama_saksi1 = et_namaSaksi1Surat.getText().toString().trim();
        final String nik_saksi1 = et_nikSaksi1Surat.getText().toString().trim();
        final String tempat_lahir_saksi1 = et_tempatLahirSaksi1Surat.getText().toString().trim();
        final String tanggal_lahir_saksi1 = et_tanggalLahirSaksi1Surat.getText().toString().trim();
        final String umur_saksi1 = et_umurSaksi1Surat.getText().toString().trim();
        final String pekerjaan_saksi1 = et_pekerjaanSaksi1Surat.getText().toString().trim();
        final String desa_saksi1 = et_desaSaksi1Surat.getText().toString().trim();
        final String kec_saksi1 = et_kecamatanSaksi1Surat.getText().toString().trim();
        final String kab_saksi1 = et_kabupatenSaksi1Surat.getText().toString().trim();
        final String prov_saksi1 = et_provinsiSaksi1Surat.getText().toString().trim();
        final String nama_saksi2 = et_namaSaksi2Surat.getText().toString().trim();
        final String nik_saksi2 = et_nikSaksi2Surat.getText().toString().trim();
        final String tempat_lahir_saksi2 = et_tempatLahirSaksi2Surat.getText().toString().trim();
        final String tanggal_lahir_saksi2 = et_tanggalLahirSaksi2Surat.getText().toString().trim();
        final String umur_saksi2 = et_umurSaksi2Surat.getText().toString().trim();
        final String pekerjaan_saksi2 = et_pekerjaanSaksi2Surat.getText().toString().trim();
        final String desa_saksi2 = et_desaSaksi2Surat.getText().toString().trim();
        final String kec_saksi2 = et_kecamatanSaksi2Surat.getText().toString().trim();
        final String kab_saksi2 = et_kabupatenSaksi2Surat.getText().toString().trim();
        final String prov_saksi2 = et_provinsiSaksi2Surat.getText().toString().trim();

        String URL_TAMBAHADUAN = link + "buatsurat";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_TAMBAHADUAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(TambahSuratActivity.this, "Pengajuan Surat Sukses", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(TambahSuratActivity.this, "Pengajuan Gagal! Anda Telah Melakukan Pengajuan Untuk Data Ini", Toast.LENGTH_LONG).show();
                            }
                            loadingDialog.dissmissDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TambahSuratActivity.this, "Pengajuan Gagal!" + e.toString(), Toast.LENGTH_LONG).show();
                            loadingDialog.dissmissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TambahSuratActivity.this, "Pengajuan Gagal! : Cek Koneksi Anda" + error.toString(), Toast.LENGTH_LONG).show();
                        loadingDialog.dissmissDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", id_user);
                params.put("formatsurat_id", idSurat);
                params.put("status", status);
                params.put("keperluan", keperluan);
                params.put("keterangan", keterangan);
                params.put("kepala_kk", kepala_kk);
                params.put("no_kk", no_kk);
                params.put("rt_tujuan", rt_tujuan);
                params.put("rw_tujuan", rw_tujuan);
                params.put("dusun_tujuan", dusun_tujuan);
                params.put("desa_tujuan", desa_tujuan);
                params.put("kecamatan_tujuan", kecamatan_tujuan);
                params.put("kabupaten_tujuan", kabupaten_tujuan);
                params.put("alasan_pindah", alasan_pindah);
                params.put("tanggal_pindah", tanggal_pindah);
                params.put("jumlah_pengikut", jumlah_pengikut);
                params.put("barang", barang);
                params.put("jenis", jenis);
                params.put("nama", nama);
                params.put("no_identitas", no_identitas);
                params.put("tempat_lahir", tempat_lahir);
                params.put("tgl_lahir", tgl_lahir);
                params.put("jk", jk);
                params.put("alamat", alamat);
                params.put("pekerjaan", pekerjaan);
                params.put("ketua_adat", ketua_adat);
                params.put("agama", agama);
                params.put("perbedaan", perbedaan);
                params.put("kartu_identitas", kartu_identitas);
                params.put("rincian", rincian);
                params.put("usaha", usaha);
                params.put("no_jamkesos", no_jamkesos);
                params.put("hari_lahir", hari_lahir);
                params.put("waktu_lahir", waktu_lahir);
                params.put("kelahiran_ke", kelahiran_ke);
                params.put("nama_ibu", nama_ibu);
                params.put("nik_ibu", nik_ibu);
                params.put("umur_ibu", umur_ibu);
                params.put("pekerjaan_ibu", pekerjaan_ibu);
                params.put("alamat_ibu", alamat_ibu);
                params.put("desa_ibu", desa_ibu);
                params.put("kec_ibu", kec_ibu);
                params.put("kab_ibu", kab_ibu);
                params.put("nama_ayah", nama_ayah);
                params.put("nik_ayah", nik_ayah);
                params.put("umur_ayah", umur_ayah);
                params.put("pekerjaan_ayah", pekerjaan_ayah);
                params.put("alamat_ayah", alamat_ayah);
                params.put("desa_ayah", desa_ayah);
                params.put("kec_ayah", kec_ayah);
                params.put("kab_ayah", kab_ayah);
                params.put("nama_pelapor", nama_pelapor);
                params.put("nik_pelapor", nik_pelapor);
                params.put("umur_pelapor", umur_pelapor);
                params.put("pekerjaan_pelapor", pekerjaan_pelapor);
                params.put("desa_pelapor", desa_pelapor);
                params.put("kec_pelapor", kec_pelapor);
                params.put("kab_pelapor", kab_pelapor);
                params.put("prov_pelapor", prov_pelapor);
                params.put("hub_pelapor", hub_pelapor);
                params.put("tempat_lahir_pelapor", tempat_lahir_pelapor);
                params.put("tanggal_lahir_pelapor", tanggal_lahir_pelapor);
                params.put("nama_saksi1", nama_saksi1);
                params.put("nik_saksi1", nik_saksi1);
                params.put("tempat_lahir_saksi1", tempat_lahir_saksi1);
                params.put("tanggal_lahir_saksi1", tanggal_lahir_saksi1);
                params.put("umur_saksi1", umur_saksi1);
                params.put("pekerjaan_saksi1", pekerjaan_saksi1);
                params.put("desa_saksi1", desa_saksi1);
                params.put("kec_saksi1", kec_saksi1);
                params.put("kab_saksi1", kab_saksi1);
                params.put("prov_saksi1", prov_saksi1);
                params.put("nama_saksi2", nama_saksi2);
                params.put("nik_saksi2", nik_saksi2);
                params.put("tempat_lahir_saksi2", tempat_lahir_saksi2);
                params.put("tanggal_lahir_saksi2", tanggal_lahir_saksi2);
                params.put("umur_saksi2", umur_saksi2);
                params.put("pekerjaan_saksi2", pekerjaan_saksi2);
                params.put("desa_saksi2", desa_saksi2);
                params.put("kec_saksi2", kec_saksi2);
                params.put("kab_saksi2", kab_saksi2);
                params.put("prov_saksi2", prov_saksi2);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}