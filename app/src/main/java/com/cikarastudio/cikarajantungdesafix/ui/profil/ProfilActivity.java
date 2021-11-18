package com.cikarastudio.cikarajantungdesafix.ui.profil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cikarastudio.cikarajantungdesafix.adapter.StatusAduanAdapter;
import com.cikarastudio.cikarajantungdesafix.adapter.StatusAduanSelesaiAdapter;
import com.cikarastudio.cikarajantungdesafix.model.StatusAduanModel;
import com.cikarastudio.cikarajantungdesafix.session.SessionManager;
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;
import com.cikarastudio.cikarajantungdesafix.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfilActivity extends AppCompatActivity implements View.OnClickListener {

    SessionManager sessionManager;
    LoadingDialog loadingDialog;

    RecyclerView rv_isiAduan, rv_aduanSelesai;
    private ArrayList<StatusAduanModel> statusAduanList;
    StatusAduanAdapter statusAduanAdapter;
    private ArrayList<StatusAduanModel> statusAduanSelesaiList;
    StatusAduanSelesaiAdapter statusAduanSelesaiAdapter;

    String id_user, link, linkGambar, linkGambarProfil, token;

    private Bitmap bitmap;

    ImageView
            img_back, img_dataDiri, img_dataKelahiran,
            img_dataPendidikan, img_dataKewarganegaraan,
            img_dataKeluarga, img_dataPerkawinan,
            img_dataKesehatan, img_statusAduan,
    //poto profil
    img_photoprofile, img_editPhotoProfil,

    //aduan data diri
    img_menuAduanNIK, img_menuAduanNamaLengkap, img_menuAduanKTPEl, img_menuAduanStatusRekam,
            img_menuAduanIdCard, img_menuAduanNoKK, img_menuAduanHubunganKeluarga, img_menuAduanJenisKelamin,
            img_menuAduanAgama, img_menuAduanStatusPenduduk, img_menuAduanNoTelp, img_menuAduanAlamatEmail,
            img_menuAduanAlamatSebelum, img_menuAduanAlamatSekarang, img_menuAduanRT,

    //aduan data kelahiran
    img_menuAduanNomorAktaKelahiran, img_menuAduanTempatLahir, img_menuAduanTanggalLahir,
            img_menuAduanWaktuKelahiran, img_menuAduanTempatDilahirkan, img_menuAduanJenisKelahiran,
            img_menuAduanAnakKe, img_menuAduanPenolongKelahiran, img_menuAduanBeratLahir,
            img_menuAduanPanjangLahir,

    //aduan pendidikan
    img_menuAduanPendidikanKK, img_menuAduanPendidikanTempuh, img_menuAduanPekerjaan,

    //aduan kewarganegaraan
    img_menuAduanStatusKewarganegaraan, img_menuAduanNoPaspor, img_menuAduanTglAkhirPaspor,

    //aduan keluarga
    img_menuAduanNIKAyah, img_menuAduanNamaAyah, img_menuAduanNIKIbu, img_menuAduanNamaIbu,

    //aduan perkawinan
    img_menuAduanStatusPerkawinan, img_menuAduanNoBukuNikah, img_menuAduanTglPerkawinan,
            img_menuAduanAktaPerceraian, img_menuAduanTglPerceraian,

    //aduan kesehatan
    img_menuAduanGolonganDarah, img_menuAduanCacat, img_menuAduanSakitMenahun,
            img_menuAduanAkseptorKB, img_menuAduanAsuransi;

    CardView cr_dataDiri, cr_isiDataDiri,
            cr_dataKelahiran, cr_isiDataKelahiran,
            cr_dataPendidikan, cr_isiDataPendidikan,
            cr_dataKewarganegaraan, cr_isiDataKewarganegaraan,
            cr_dataKeluarga, cr_isiDataKeluarga,
            cr_dataPerkawinan, cr_isiDataPerkawinan,
            cr_dataKesehatan, cr_isiDataKesehatan,
            cr_dataStatusAduan, cr_isiDataStatusAduan,
            cr_logout, cr_fotoProfil;

    TextView
            et_nama1, et_desa,
            et_nik, et_nama, et_ktpEl, et_statusRekam, et_idCard, et_noKK, et_hubunganKeluarga,
            et_jenisKelamin, et_agama, et_statusPenduduk, et_noTelepon, et_alamatEmail, et_alamatSebelum,
            et_alamatSekarang, et_rt,

    //data kelahiran
    et_nomorAktakelahiran, et_tempatLahir, et_tanggalLahir, et_waktuKelahiran, et_tempatDilahirkan, et_jenisKelahiran,
            et_anakKe, et_penolongKelahiran, et_beratLahir, et_panjangLahir,

    //data pendidikan
    et_pendidikanKK, et_pendidikanTempuh, et_pekerjaan,

    //data kewarganegaraan
    et_statusKewarganegaraan, et_noPaspor, et_tglAkhirPaspor,

    //data keluarga
    et_nikAyah, et_namaAyah, et_nikIbu, et_namaIbu,

    //data perkawinan
    et_statusPerkawinan, et_noBukuNikah, et_tglPerkawinan, et_aktaPerceraian, et_tglPerceraian,

    //data kesehatan
    et_golonganDarah, et_cacat, et_sakitMenahun, et_akseptorKB, et_asuransi;

    //persentase data
    TextRoundCornerProgressBar pb_presentaseKelengkapan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        sessionManager = new SessionManager(ProfilActivity.this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        id_user = user.get(sessionManager.ID);

        HttpsTrustManager.allowAllSSL();

        //inisiasi link
        link = getString(R.string.link);
        linkGambar = getString(R.string.linkGambar);

        //inisiasi token
        token = getString(R.string.token);

        loadingDialog = new LoadingDialog(ProfilActivity.this);
        loadingDialog.startLoading();


        cr_isiDataDiri = findViewById(R.id.cr_isiDataDiri);
        img_dataDiri = findViewById(R.id.img_dataDiri);
        cr_isiDataKelahiran = findViewById(R.id.cr_isiDataKelahiran);
        img_dataKelahiran = findViewById(R.id.img_dataKelahiran);
        cr_isiDataPendidikan = findViewById(R.id.cr_isiDataPendidikan);
        img_dataPendidikan = findViewById(R.id.img_dataPendidikan);
        cr_isiDataKewarganegaraan = findViewById(R.id.cr_isiDataKewarganegaraan);
        img_dataKewarganegaraan = findViewById(R.id.img_dataKewarganegaraan);
        cr_isiDataKeluarga = findViewById(R.id.cr_isiDataKeluarga);
        img_dataKeluarga = findViewById(R.id.img_dataKeluarga);
        cr_isiDataPerkawinan = findViewById(R.id.cr_isiDataPerkawinan);
        img_dataPerkawinan = findViewById(R.id.img_dataPerkawinan);
        cr_isiDataKesehatan = findViewById(R.id.cr_isiDataKesehatan);
        img_dataKesehatan = findViewById(R.id.img_dataKesehatan);
        cr_isiDataStatusAduan = findViewById(R.id.cr_isiDataStatusAduan);
        img_statusAduan = findViewById(R.id.img_statusAduan);

        pb_presentaseKelengkapan = findViewById(R.id.pb_presentaseKelengkapan);

        cr_fotoProfil = findViewById(R.id.cr_fotoProfil);
        et_nama1 = findViewById(R.id.et_nama1);

        //data diri
        et_nik = findViewById(R.id.et_nik);
        et_nama = findViewById(R.id.et_nama);
        et_ktpEl = findViewById(R.id.et_ktpEl);
        et_statusRekam = findViewById(R.id.et_statusRekam);
        et_idCard = findViewById(R.id.et_idCard);
        et_noKK = findViewById(R.id.et_noKK);
        et_hubunganKeluarga = findViewById(R.id.et_hubunganKeluarga);
        et_jenisKelamin = findViewById(R.id.et_jenisKelamin);
        et_agama = findViewById(R.id.et_agama);
        et_statusPenduduk = findViewById(R.id.et_statusPenduduk);
        et_noTelepon = findViewById(R.id.et_noTelepon);
        et_alamatEmail = findViewById(R.id.et_alamatEmail);
        et_alamatSebelum = findViewById(R.id.et_alamatSebelum);
        et_alamatSekarang = findViewById(R.id.et_alamatSekarang);
        et_rt = findViewById(R.id.et_rt);

        //data kelahiran
        et_nomorAktakelahiran = findViewById(R.id.et_nomorAktakelahiran);
        et_tempatLahir = findViewById(R.id.et_tempatLahir);
        et_tanggalLahir = findViewById(R.id.et_tanggalLahir);
        et_waktuKelahiran = findViewById(R.id.et_waktuKelahiran);
        et_tempatDilahirkan = findViewById(R.id.et_tempatDilahirkan);
        et_jenisKelahiran = findViewById(R.id.et_jenisKelahiran);
        et_anakKe = findViewById(R.id.et_anakKe);
        et_penolongKelahiran = findViewById(R.id.et_penolongKelahiran);
        et_beratLahir = findViewById(R.id.et_beratLahir);
        et_panjangLahir = findViewById(R.id.et_panjangLahir);

        //data pendidikan
        et_pendidikanKK = findViewById(R.id.et_pendidikanKK);
        et_pendidikanTempuh = findViewById(R.id.et_pendidikanTempuh);
        et_pekerjaan = findViewById(R.id.et_pekerjaan);

        //data kewarganegaraan
        et_statusKewarganegaraan = findViewById(R.id.et_statusKewarganegaraan);
        et_noPaspor = findViewById(R.id.et_noPaspor);
        et_tglAkhirPaspor = findViewById(R.id.et_tglAkhirPaspor);

        //data keluarga
        et_nikAyah = findViewById(R.id.et_nikAyah);
        et_namaAyah = findViewById(R.id.et_namaAyah);
        et_nikIbu = findViewById(R.id.et_nikIbu);
        et_namaIbu = findViewById(R.id.et_namaIbu);

        //data perkawinan
        et_statusPerkawinan = findViewById(R.id.et_statusPerkawinan);
        et_noBukuNikah = findViewById(R.id.et_noBukuNikah);
        et_tglPerkawinan = findViewById(R.id.et_tglPerkawinan);
        et_aktaPerceraian = findViewById(R.id.et_aktaPerceraian);
        et_tglPerceraian = findViewById(R.id.et_tglPerceraian);

        //data kesehatan
        et_golonganDarah = findViewById(R.id.et_golonganDarah);
        et_cacat = findViewById(R.id.et_cacat);
        et_sakitMenahun = findViewById(R.id.et_sakitMenahun);
        et_akseptorKB = findViewById(R.id.et_akseptorKB);
        et_asuransi = findViewById(R.id.et_asuransi);

        loadDataDiri();
        loadDataPercentage();
        loadDataStatusAduanProses();
        loadDataStatusAduanSelesai();
        loadPhotoProfil();

        statusAduanList = new ArrayList<>();
        rv_isiAduan = findViewById(R.id.rv_isiAduan);
        LinearLayoutManager linearLayoutManageraaa = new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv_isiAduan.setLayoutManager(linearLayoutManageraaa);
        rv_isiAduan.setHasFixedSize(true);

        statusAduanSelesaiList = new ArrayList<>();
        rv_aduanSelesai = findViewById(R.id.rv_aduanSelesai);
        LinearLayoutManager linearLayoutManageraaabbb = new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv_aduanSelesai.setLayoutManager(linearLayoutManageraaabbb);
        rv_aduanSelesai.setHasFixedSize(true);

        //onclick photo profil
        img_editPhotoProfil = findViewById(R.id.img_editPhotoProfil);
        img_editPhotoProfil.setOnClickListener(this);

        img_photoprofile = findViewById(R.id.img_photoprofile);
        img_photoprofile.setOnClickListener(this);

        //onclick visible gone
        cr_dataDiri = findViewById(R.id.cr_dataDiri);
        cr_dataDiri.setOnClickListener(this); // calling onClick() method

        cr_dataKelahiran = findViewById(R.id.cr_dataKelahiran);
        cr_dataKelahiran.setOnClickListener(this);

        cr_dataPendidikan = findViewById(R.id.cr_dataPendidikan);
        cr_dataPendidikan.setOnClickListener(this);

        cr_dataKewarganegaraan = findViewById(R.id.cr_dataKewarganegaraan);
        cr_dataKewarganegaraan.setOnClickListener(this);

        cr_dataKeluarga = findViewById(R.id.cr_dataKeluarga);
        cr_dataKeluarga.setOnClickListener(this);

        cr_dataPerkawinan = findViewById(R.id.cr_dataPerkawinan);
        cr_dataPerkawinan.setOnClickListener(this);

        cr_dataKesehatan = findViewById(R.id.cr_dataKesehatan);
        cr_dataKesehatan.setOnClickListener(this);

        cr_dataStatusAduan = findViewById(R.id.cr_dataStatusAduan);
        cr_dataStatusAduan.setOnClickListener(this);

        //onclick aduan
        img_menuAduanNIK = findViewById(R.id.img_menuAduanNIK);
        img_menuAduanNIK.setOnClickListener(this);

        img_menuAduanNamaLengkap = findViewById(R.id.img_menuAduanNamaLengkap);
        img_menuAduanNamaLengkap.setOnClickListener(this);

        img_menuAduanKTPEl = findViewById(R.id.img_menuAduanKTPEl);
        img_menuAduanKTPEl.setOnClickListener(this);

        img_menuAduanStatusRekam = findViewById(R.id.img_menuAduanStatusRekam);
        img_menuAduanStatusRekam.setOnClickListener(this);

        img_menuAduanIdCard = findViewById(R.id.img_menuAduanIdCard);
        img_menuAduanIdCard.setOnClickListener(this);

        img_menuAduanNoKK = findViewById(R.id.img_menuAduanNoKK);
        img_menuAduanNoKK.setOnClickListener(this);

        img_menuAduanHubunganKeluarga = findViewById(R.id.img_menuAduanHubunganKeluarga);
        img_menuAduanHubunganKeluarga.setOnClickListener(this);

        img_menuAduanJenisKelamin = findViewById(R.id.img_menuAduanJenisKelamin);
        img_menuAduanJenisKelamin.setOnClickListener(this);

        img_menuAduanAgama = findViewById(R.id.img_menuAduanAgama);
        img_menuAduanAgama.setOnClickListener(this);

        img_menuAduanStatusPenduduk = findViewById(R.id.img_menuAduanStatusPenduduk);
        img_menuAduanStatusPenduduk.setOnClickListener(this);

        img_menuAduanNoTelp = findViewById(R.id.img_menuAduanNoTelp);
        img_menuAduanNoTelp.setOnClickListener(this);

        img_menuAduanAlamatEmail = findViewById(R.id.img_menuAduanAlamatEmail);
        img_menuAduanAlamatEmail.setOnClickListener(this);

        img_menuAduanAlamatSebelum = findViewById(R.id.img_menuAduanAlamatSebelum);
        img_menuAduanAlamatSebelum.setOnClickListener(this);

        img_menuAduanAlamatSekarang = findViewById(R.id.img_menuAduanAlamatSekarang);
        img_menuAduanAlamatSekarang.setOnClickListener(this);

        img_menuAduanRT = findViewById(R.id.img_menuAduanRT);
        img_menuAduanRT.setOnClickListener(this);

        //aduan data kelahiran
        img_menuAduanNomorAktaKelahiran = findViewById(R.id.img_menuAduanNomorAktaKelahiran);
        img_menuAduanNomorAktaKelahiran.setOnClickListener(this);

        img_menuAduanTempatLahir = findViewById(R.id.img_menuAduanTempatLahir);
        img_menuAduanTempatLahir.setOnClickListener(this);

        img_menuAduanTanggalLahir = findViewById(R.id.img_menuAduanTanggalLahir);
        img_menuAduanTanggalLahir.setOnClickListener(this);

        img_menuAduanWaktuKelahiran = findViewById(R.id.img_menuAduanWaktuKelahiran);
        img_menuAduanWaktuKelahiran.setOnClickListener(this);

        img_menuAduanTempatDilahirkan = findViewById(R.id.img_menuAduanTempatDilahirkan);
        img_menuAduanTempatDilahirkan.setOnClickListener(this);

        img_menuAduanJenisKelahiran = findViewById(R.id.img_menuAduanJenisKelahiran);
        img_menuAduanJenisKelahiran.setOnClickListener(this);

        img_menuAduanAnakKe = findViewById(R.id.img_menuAduanAnakKe);
        img_menuAduanAnakKe.setOnClickListener(this);

        img_menuAduanPenolongKelahiran = findViewById(R.id.img_menuAduanPenolongKelahiran);
        img_menuAduanPenolongKelahiran.setOnClickListener(this);

        img_menuAduanBeratLahir = findViewById(R.id.img_menuAduanBeratLahir);
        img_menuAduanBeratLahir.setOnClickListener(this);

        img_menuAduanPanjangLahir = findViewById(R.id.img_menuAduanPanjangLahir);
        img_menuAduanPanjangLahir.setOnClickListener(this);

        //aduan pendidikan
        img_menuAduanPendidikanKK = findViewById(R.id.img_menuAduanPendidikanKK);
        img_menuAduanPendidikanKK.setOnClickListener(this);

        img_menuAduanPendidikanTempuh = findViewById(R.id.img_menuAduanPendidikanTempuh);
        img_menuAduanPendidikanTempuh.setOnClickListener(this);

        img_menuAduanPekerjaan = findViewById(R.id.img_menuAduanPekerjaan);
        img_menuAduanPekerjaan.setOnClickListener(this);

        //aduan kewarganegaraan
        img_menuAduanStatusKewarganegaraan = findViewById(R.id.img_menuAduanStatusKewarganegaraan);
        img_menuAduanStatusKewarganegaraan.setOnClickListener(this);

        img_menuAduanNoPaspor = findViewById(R.id.img_menuAduanNoPaspor);
        img_menuAduanNoPaspor.setOnClickListener(this);

        img_menuAduanTglAkhirPaspor = findViewById(R.id.img_menuAduanTglAkhirPaspor);
        img_menuAduanTglAkhirPaspor.setOnClickListener(this);

        //aduan keluarga
        img_menuAduanNIKAyah = findViewById(R.id.img_menuAduanNIKAyah);
        img_menuAduanNIKAyah.setOnClickListener(this);

        img_menuAduanNamaAyah = findViewById(R.id.img_menuAduanNamaAyah);
        img_menuAduanNamaAyah.setOnClickListener(this);

        img_menuAduanNIKIbu = findViewById(R.id.img_menuAduanNIKIbu);
        img_menuAduanNIKIbu.setOnClickListener(this);

        img_menuAduanNamaIbu = findViewById(R.id.img_menuAduanNamaIbu);
        img_menuAduanNamaIbu.setOnClickListener(this);

        //aduan perkawinan
        img_menuAduanStatusPerkawinan = findViewById(R.id.img_menuAduanStatusPerkawinan);
        img_menuAduanStatusPerkawinan.setOnClickListener(this);

        img_menuAduanNoBukuNikah = findViewById(R.id.img_menuAduanNoBukuNikah);
        img_menuAduanNoBukuNikah.setOnClickListener(this);

        img_menuAduanTglPerkawinan = findViewById(R.id.img_menuAduanTglPerkawinan);
        img_menuAduanTglPerkawinan.setOnClickListener(this);

        img_menuAduanAktaPerceraian = findViewById(R.id.img_menuAduanAktaPerceraian);
        img_menuAduanAktaPerceraian.setOnClickListener(this);

        img_menuAduanTglPerceraian = findViewById(R.id.img_menuAduanTglPerceraian);
        img_menuAduanTglPerceraian.setOnClickListener(this);

        //aduan kesehatan
        img_menuAduanGolonganDarah = findViewById(R.id.img_menuAduanGolonganDarah);
        img_menuAduanGolonganDarah.setOnClickListener(this);

        img_menuAduanCacat = findViewById(R.id.img_menuAduanCacat);
        img_menuAduanCacat.setOnClickListener(this);

        img_menuAduanSakitMenahun = findViewById(R.id.img_menuAduanSakitMenahun);
        img_menuAduanSakitMenahun.setOnClickListener(this);

        img_menuAduanAkseptorKB = findViewById(R.id.img_menuAduanAkseptorKB);
        img_menuAduanAkseptorKB.setOnClickListener(this);

        img_menuAduanAsuransi = findViewById(R.id.img_menuAduanAsuransi);
        img_menuAduanAsuransi.setOnClickListener(this);

        cr_logout = findViewById(R.id.cr_logout);
        cr_logout.setOnClickListener(this);

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_editPhotoProfil:
                // do your code
                chooseFile();
                break;
            case R.id.cr_dataDiri:
                // do your code
                setVisibleGoneBlack(cr_isiDataDiri, img_dataDiri, cr_isiDataKelahiran, img_dataKelahiran,
                        cr_isiDataPendidikan, img_dataPendidikan, cr_isiDataKewarganegaraan, img_dataKewarganegaraan,
                        cr_isiDataKeluarga, img_dataKeluarga, cr_isiDataPerkawinan, img_dataPerkawinan,
                        cr_isiDataKesehatan, img_dataKesehatan, cr_isiDataStatusAduan, img_statusAduan);
                break;
            case R.id.cr_dataKelahiran:
                // do your code
                setVisibleGoneBlack(cr_isiDataKelahiran, img_dataKelahiran, cr_isiDataDiri, img_dataDiri,
                        cr_isiDataPendidikan, img_dataPendidikan, cr_isiDataKewarganegaraan, img_dataKewarganegaraan,
                        cr_isiDataKeluarga, img_dataKeluarga, cr_isiDataPerkawinan, img_dataPerkawinan,
                        cr_isiDataKesehatan, img_dataKesehatan, cr_isiDataStatusAduan, img_statusAduan);
                break;
            case R.id.cr_dataPendidikan:
                // do your code
                setVisibleGoneBlack(cr_isiDataPendidikan, img_dataPendidikan, cr_isiDataKelahiran, img_dataKelahiran,
                        cr_isiDataDiri, img_dataDiri, cr_isiDataKewarganegaraan, img_dataKewarganegaraan,
                        cr_isiDataKeluarga, img_dataKeluarga, cr_isiDataPerkawinan, img_dataPerkawinan,
                        cr_isiDataKesehatan, img_dataKesehatan, cr_isiDataStatusAduan, img_statusAduan);
                break;
            case R.id.cr_dataKewarganegaraan:
                // do your code
                setVisibleGoneBlack(cr_isiDataKewarganegaraan, img_dataKewarganegaraan, cr_isiDataDiri, img_dataDiri,
                        cr_isiDataKelahiran, img_dataKelahiran, cr_isiDataPendidikan, img_dataPendidikan,
                        cr_isiDataKeluarga, img_dataKeluarga, cr_isiDataPerkawinan, img_dataPerkawinan,
                        cr_isiDataKesehatan, img_dataKesehatan, cr_isiDataStatusAduan, img_statusAduan);
                break;
            case R.id.cr_dataKeluarga:
                // do your code
                setVisibleGoneBlack(cr_isiDataKeluarga, img_dataKeluarga, cr_isiDataDiri, img_dataDiri,
                        cr_isiDataKelahiran, img_dataKelahiran, cr_isiDataPendidikan, img_dataPendidikan,
                        cr_isiDataKewarganegaraan, img_dataKewarganegaraan, cr_isiDataPerkawinan, img_dataPerkawinan,
                        cr_isiDataKesehatan, img_dataKesehatan, cr_isiDataStatusAduan, img_statusAduan);
                break;
            case R.id.cr_dataPerkawinan:
                // do your code
                setVisibleGoneBlack(cr_isiDataPerkawinan, img_dataPerkawinan, cr_isiDataDiri, img_dataDiri,
                        cr_isiDataKelahiran, img_dataKelahiran, cr_isiDataPendidikan, img_dataPendidikan,
                        cr_isiDataKewarganegaraan, img_dataKewarganegaraan, cr_isiDataKeluarga, img_dataKeluarga,
                        cr_isiDataKesehatan, img_dataKesehatan, cr_isiDataStatusAduan, img_statusAduan);
                break;
            case R.id.cr_dataKesehatan:
                // do your code
                setVisibleGoneBlack(cr_isiDataKesehatan, img_dataKesehatan, cr_isiDataDiri, img_dataDiri,
                        cr_isiDataKelahiran, img_dataKelahiran, cr_isiDataPendidikan, img_dataPendidikan,
                        cr_isiDataKewarganegaraan, img_dataKewarganegaraan, cr_isiDataKeluarga, img_dataKeluarga,
                        cr_isiDataPerkawinan, img_dataPerkawinan, cr_isiDataStatusAduan, img_statusAduan);
                break;
            case R.id.cr_dataStatusAduan:
                // do your code
                setVisibleGoneBlack(cr_isiDataStatusAduan, img_statusAduan, cr_isiDataKesehatan, img_dataKesehatan,
                        cr_isiDataDiri, img_dataDiri, cr_isiDataKelahiran, img_dataKelahiran,
                        cr_isiDataPendidikan, img_dataPendidikan, cr_isiDataKewarganegaraan, img_dataKewarganegaraan,
                        cr_isiDataKeluarga, img_dataKeluarga, cr_isiDataPerkawinan, img_dataPerkawinan);
                break;
            case R.id.img_menuAduanNIK:
                // do your code
                showPopupMenuAduan(v, "nik", "NIK");
                break;
            case R.id.img_menuAduanNamaLengkap:
                // do your code
                showPopupMenuAduan(v, "nama_penduduk", "Nama Lengkap");
                break;
            case R.id.img_menuAduanKTPEl:
                // do your code
                showPopupMenuAduan(v, "status_ktp", "KTP Elektronik");
                break;
            case R.id.img_menuAduanStatusRekam:
                // do your code
                showPopupMenuAduan(v, "status_rekam", "Status Rekam KTP");
                break;
            case R.id.img_menuAduanIdCard:
                // do your code
                showPopupMenuAduan(v, "id_card", "ID Card");
                break;
            case R.id.img_menuAduanNoKK:
                // do your code
                showPopupMenuAduan(v, "kk_sebelum", "Nomor KK");
                break;
            case R.id.img_menuAduanHubunganKeluarga:
                // do your code
                showPopupMenuAduan(v, "hubungan_keluarga", "Hubungan Dalam Keluarga");
                break;
            case R.id.img_menuAduanJenisKelamin:
                // do your code
                showPopupMenuAduan(v, "jk", "Jenis Kelamin");
                break;
            case R.id.img_menuAduanAgama:
                // do your code
                showPopupMenuAduan(v, "agama", "Agama");
                break;
            case R.id.img_menuAduanStatusPenduduk:
                // do your code
                showPopupMenuAduan(v, "status_penduduk", "Status Penduduk");
                break;
            case R.id.img_menuAduanNoTelp:
                // do your code
                showPopupMenuAduan(v, "no_telp", "Nomor Telepon");
                break;
            case R.id.img_menuAduanAlamatEmail:
                // do your code
                showPopupMenuAduan(v, "email", "Alamat Email");
                break;
            case R.id.img_menuAduanAlamatSebelum:
                // do your code
                showPopupMenuAduan(v, "alamat_sebelum", "Alamat Sebelum");
                break;
            case R.id.img_menuAduanAlamatSekarang:
                // do your code
                showPopupMenuAduan(v, "alamat_sekarang", "Alamat Sekarang");
                break;
            case R.id.img_menuAduanRT:
                // do your code
                showPopupMenuAduan(v, "nama_rt", "Nomor RT");
                break;
            case R.id.img_menuAduanNomorAktaKelahiran:
                // do your code
                showPopupMenuAduan(v, "no_akta", "Nomor Akta Kelahiran");
                break;
            case R.id.img_menuAduanTempatLahir:
                // do your code
                showPopupMenuAduan(v, "tempat_lahir", "Tempat Lahir");
                break;
            case R.id.img_menuAduanTanggalLahir:
                // do your code
                showPopupMenuAduan(v, "tgl_lahir", "Tanggal Lahir");
                break;
            case R.id.img_menuAduanWaktuKelahiran:
                // do your code
                showPopupMenuAduan(v, "waktu_lahir", "Waktu Lahir");
                break;
            case R.id.img_menuAduanTempatDilahirkan:
                // do your code
                showPopupMenuAduan(v, "tempat_dilahirkan", "Tempat Dilahirkan");
                break;
            case R.id.img_menuAduanJenisKelahiran:
                // do your code
                showPopupMenuAduan(v, "jenis_kelahiran", "Jenis Kelahiran");
                break;
            case R.id.img_menuAduanAnakKe:
                // do your code
                showPopupMenuAduan(v, "anak_ke", "Anak Ke");
                break;
            case R.id.img_menuAduanPenolongKelahiran:
                // do your code
                showPopupMenuAduan(v, "penolong_kelahiran", "Penolong Kelahiran");
                break;
            case R.id.img_menuAduanBeratLahir:
                // do your code
                showPopupMenuAduan(v, "berat_lahir", "Berat Lahir");
                break;
            case R.id.img_menuAduanPanjangLahir:
                // do your code
                showPopupMenuAduan(v, "panjang_lahir", "Panjang Lahir");
                break;
            case R.id.img_menuAduanPendidikanKK:
                // do your code
                showPopupMenuAduan(v, "pendidikan_kk", "Pendidikan Dalam KK");
                break;
            case R.id.img_menuAduanPendidikanTempuh:
                // do your code
                showPopupMenuAduan(v, "pendidikan_tempuh", "Pendidikan Ditempuh");
                break;
            case R.id.img_menuAduanPekerjaan:
                // do your code
                showPopupMenuAduan(v, "pekerjaan", "Pekerjaan");
                break;
            case R.id.img_menuAduanStatusKewarganegaraan:
                // do your code
                showPopupMenuAduan(v, "status_warganegara", "Status Kewarganegaraan");
                break;
            case R.id.img_menuAduanNoPaspor:
                // do your code
                showPopupMenuAduan(v, "nomor_paspor", "Nomor Paspor");
                break;
            case R.id.img_menuAduanTglAkhirPaspor:
                // do your code
                showPopupMenuAduan(v, "tgl_akhirpaspor", "Tanggal Akhir Paspor");
                break;
            case R.id.img_menuAduanNIKAyah:
                // do your code
                showPopupMenuAduan(v, "nik_ayah", "NIK Ayah");
                break;
            case R.id.img_menuAduanNamaAyah:
                // do your code
                showPopupMenuAduan(v, "nama_ayah", "Nama Ayah");
                break;
            case R.id.img_menuAduanNIKIbu:
                // do your code
                showPopupMenuAduan(v, "nik_ibu", "NIK Ibu");
                break;
            case R.id.img_menuAduanNamaIbu:
                // do your code
                showPopupMenuAduan(v, "nama_ibu", "Nama Ibu");
                break;
            case R.id.img_menuAduanStatusPerkawinan:
                // do your code
                showPopupMenuAduan(v, "status_perkawinan", "Status Perkawinan");
                break;
            case R.id.img_menuAduanNoBukuNikah:
                // do your code
                showPopupMenuAduan(v, "no_bukunikah", "Nomor Buku Nikah");
                break;
            case R.id.img_menuAduanTglPerkawinan:
                // do your code
                showPopupMenuAduan(v, "tgl_perkawinan", "Tanggal Perkawinan");
                break;
            case R.id.img_menuAduanAktaPerceraian:
                // do your code
                showPopupMenuAduan(v, "akta_perceraian", "Akta Perceraian");
                break;
            case R.id.img_menuAduanTglPerceraian:
                // do your code
                showPopupMenuAduan(v, "tgl_perceraian", "Tanggal Perceraian");
                break;
            case R.id.img_menuAduanGolonganDarah:
                // do your code
                showPopupMenuAduan(v, "golongan_darah", "Golongan Darah");
                break;
            case R.id.img_menuAduanCacat:
                // do your code
                showPopupMenuAduan(v, "cacat", "Cacat");
                break;
            case R.id.img_menuAduanSakitMenahun:
                // do your code
                showPopupMenuAduan(v, "sakit_menahun", "Sakit Menahun");
                break;
            case R.id.img_menuAduanAkseptorKB:
                // do your code
                showPopupMenuAduan(v, "akseptor_kb", "Akseptor KB");
                break;
            case R.id.img_menuAduanAsuransi:
                // do your code
                showPopupMenuAduan(v, "asuransi", "Asuransi");
                break;
            case R.id.cr_logout:
                // do your code
                dialogLogout();
                break;
            case R.id.img_back:
                // do your code
                finish();
                break;
            case R.id.img_photoprofile:
                // do your code
                showdialogPhotoProfile();
                break;
            default:
                break;
        }
    }

    private void showdialogPhotoProfile() {
        Dialog dialogFotoProfil = new Dialog(this);
        dialogFotoProfil.setContentView(R.layout.dialog_photo_profile);
        dialogFotoProfil.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_aduan);
        dialogFotoProfil.setCanceledOnTouchOutside(false);

        ImageView img_zoomPhotoProfil = dialogFotoProfil.findViewById(R.id.img_zoomPhotoProfil);
        Log.d("calpalnx", "showdialogPhotoProfile: " + linkGambarProfil);
        Picasso.with(ProfilActivity.this).load(linkGambarProfil).fit().centerCrop().into(img_zoomPhotoProfil);

        dialogFotoProfil.show();
    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                img_photoprofile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            uploadImage(getStringImage(bitmap));
        }
    }

    private void uploadImage(String base64photo) {
        loadingDialog.startLoading();
        String uploadBase64 = "data:image/png;base64," + base64photo;
        Log.d("calpalnx", id_user);
        Log.d("calpalnx", base64photo);
        Log.d("calpalnx", token);
        String URL_EDITPHOTOPROFIL = link + "editphoto";
        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, URL_EDITPHOTOPROFIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(ProfilActivity.this, "Ganti Photo Profil Sukses", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();

                            } else {
                                Toast.makeText(ProfilActivity.this, "Ganti Photo Profil Gagal!", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProfilActivity.this, "Ganti Photo Profil Gagal!" + e.toString(), Toast.LENGTH_LONG).show();
                            loadingDialog.dissmissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfilActivity.this, "Ganti Photo Profil Gagal!" + error, Toast.LENGTH_LONG).show();
                        loadingDialog.dissmissDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", id_user);
                params.put("image", uploadBase64);
                params.put("token", token);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public String getStringImage(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }

    private void showPopupMenuAduan(View view, String key, String keyshow) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.aduan_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.aduan_ajukan:
                    showDialogAduan(key, keyshow);
                    return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void showDialogAduan(String key, String keyshow) {
        Dialog dialogAduan = new Dialog(this);
        dialogAduan.setContentView(R.layout.dialog_aduan_data_penduduk);
        dialogAduan.getWindow().setBackgroundDrawableResource(R.drawable.bg_dialog_aduan);

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.80);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.50);
        dialogAduan.getWindow().setLayout(width, height);

        TextView et_dialogAduanKey = dialogAduan.findViewById(R.id.et_dialogAduanKey);
        et_dialogAduanKey.setText("Kesalahan " + keyshow);

        EditText et_dialogAduanIsi = dialogAduan.findViewById(R.id.et_dialogAduanIsi);
        et_dialogAduanIsi.requestFocus();

        TextView tv_itungChar = dialogAduan.findViewById(R.id.tv_itungChar);

        et_dialogAduanIsi.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // this will show characters remaining
                tv_itungChar.setText(50 - s.toString().length() + "/50");
            }
        });

        //showkeyboard
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        CardView cr_dialogAduanKirim = dialogAduan.findViewById(R.id.cr_dialogAduanKirim);
        cr_dialogAduanKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide keyboard
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                loadingDialog.startLoading();
                String isiAduanKey = key;
                String isiAduanIsi = et_dialogAduanIsi.getText().toString().trim();
                String statusAduan = "proses";
                addAduanDataDiri(isiAduanKey, isiAduanIsi, statusAduan);

                Log.d("calpalnx", String.valueOf(isiAduanIsi));
                Log.d("calpalnx", String.valueOf(isiAduanKey));
                Log.d("calpalnx", String.valueOf(id_user));
                Log.d("calpalnx", String.valueOf(token));
            }
        });
        dialogAduan.show();
    }

    private void addAduanDataDiri(String isiAduanKey, String isiAduanIsi, String statusAduan) {
        String URL_TAMBAHADUAN = link + "laporankesalahan";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_TAMBAHADUAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(ProfilActivity.this, "Pengajuan Sukses", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(ProfilActivity.this, ProfilActivity.class);
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(i);
                                overridePendingTransition(0, 0);
                                loadingDialog.dissmissDialog();

                            } else {
                                Toast.makeText(ProfilActivity.this, "Pengajuan Gagal! Anda Telah Melakukan Pengajuan Untuk Data Ini", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProfilActivity.this, "Pengajuan Gagal!" + e.toString(), Toast.LENGTH_LONG).show();
                            loadingDialog.dissmissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfilActivity.this, "Pengajuan Gagal! : Cek Koneksi Anda" + error, Toast.LENGTH_LONG).show();
                        loadingDialog.dissmissDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", id_user);
                params.put("key", isiAduanKey);
                params.put("isi", isiAduanIsi);
                params.put("token", token);
                params.put("status", statusAduan);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loadPhotoProfil() {
        String URL_READ = link + "user/" + id_user + "?token=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            //data dashboard laporan
                            String profile_photo_path = jsonObject.getString("profile_photo_path").trim();
                            String resi_gambar = profile_photo_path.replace(" ", "%20");

                            Log.d("calpalnx", resi_gambar);

                            String imageUrl = linkGambar + "user/" + resi_gambar;
                            Picasso.with(ProfilActivity.this).load(imageUrl).fit().centerCrop().into(img_photoprofile);
                            linkGambarProfil = imageUrl;

                            //hilangkan loading
                            loadingDialog.dissmissDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingDialog.dissmissDialog();
//                            Toast.makeText(getActivity(), "Data Akun Tidak Ada!" + e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dissmissDialog();
                Toast.makeText(ProfilActivity.this, "Tidak Ada Koneksi Internet!" + error, Toast.LENGTH_LONG).show();
            }
        }) {
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(ProfilActivity.this);
        requestQueue.add(stringRequest);
    }

    private void loadDataPercentage() {
        String URL_READ = link + "persentasependuduk?user=" + id_user + "&token=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            //data dashboard laporan
                            String persentase = jsonObject.getString("persentase").trim();
                            Float int_persentase = Float.parseFloat(persentase);
                            pb_presentaseKelengkapan.setProgress(int_persentase);
                            pb_presentaseKelengkapan.setProgressText(persentase + "%");

                            //hilangkan loading
                            loadingDialog.dissmissDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingDialog.dissmissDialog();
                            Toast.makeText(ProfilActivity.this, "Data Persentase Tidak Ada!" + e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dissmissDialog();
                Toast.makeText(ProfilActivity.this, "Tidak Ada Koneksi Internet!" + error, Toast.LENGTH_LONG).show();
            }
        }) {
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loadDataStatusAduanProses() {
        String URL_READSTATUSADUAN = link + "listaduan?token=" + token + "&user_id=" + id_user + "&status=proses";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READSTATUSADUAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    //data aduan proses
                                    String res_id = jsonObject.getString("id").trim();
                                    String res_userId = jsonObject.getString("user_id").trim();
                                    String res_keyAduan = jsonObject.getString("key").trim();
                                    String res_isiAduan = jsonObject.getString("isi").trim();
                                    String res_statusAduan = jsonObject.getString("status").trim();
                                    String res_notifAduan = jsonObject.getString("notif").trim();
                                    String res_createdAt = jsonObject.getString("created_at").trim();
                                    String res_updatedAt = jsonObject.getString("updated_at").trim();

                                    statusAduanList.add(new StatusAduanModel(res_id, res_userId, res_keyAduan, res_isiAduan, res_statusAduan,
                                            res_notifAduan, res_createdAt, res_updatedAt));
                                    statusAduanAdapter = new StatusAduanAdapter(ProfilActivity.this, statusAduanList);
                                    rv_isiAduan.setAdapter(statusAduanAdapter);
                                    cr_dataStatusAduan.setVisibility(View.VISIBLE);

                                    if ("nik".equals(res_keyAduan)) {
                                        img_menuAduanNIK.setEnabled(false);
                                        img_menuAduanNIK.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("nama_penduduk".equals(res_keyAduan)) {
                                        img_menuAduanNIK.setEnabled(false);
                                        img_menuAduanNIK.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("status_ktp".equals(res_keyAduan)) {
                                        img_menuAduanKTPEl.setEnabled(false);
                                        img_menuAduanKTPEl.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("status_rekam".equals(res_keyAduan)) {
                                        img_menuAduanStatusRekam.setEnabled(false);
                                        img_menuAduanStatusRekam.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("id_card".equals(res_keyAduan)) {
                                        img_menuAduanIdCard.setEnabled(false);
                                        img_menuAduanIdCard.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("kk_sebelum".equals(res_keyAduan)) {
                                        img_menuAduanNoKK.setEnabled(false);
                                        img_menuAduanNoKK.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("hubungan_keluarga".equals(res_keyAduan)) {
                                        img_menuAduanHubunganKeluarga.setEnabled(false);
                                        img_menuAduanHubunganKeluarga.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("jk".equals(res_keyAduan)) {
                                        img_menuAduanJenisKelamin.setEnabled(false);
                                        img_menuAduanJenisKelamin.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("agama".equals(res_keyAduan)) {
                                        img_menuAduanAgama.setEnabled(false);
                                        img_menuAduanAgama.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("status_penduduk".equals(res_keyAduan)) {
                                        img_menuAduanStatusPenduduk.setEnabled(false);
                                        img_menuAduanStatusPenduduk.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("no_telp".equals(res_keyAduan)) {
                                        img_menuAduanNoTelp.setEnabled(false);
                                        img_menuAduanNoTelp.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("email".equals(res_keyAduan)) {
                                        img_menuAduanAlamatEmail.setEnabled(false);
                                        img_menuAduanAlamatEmail.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("alamat_sebelum".equals(res_keyAduan)) {
                                        img_menuAduanAlamatSebelum.setEnabled(false);
                                        img_menuAduanAlamatSebelum.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("alamat_sekarang".equals(res_keyAduan)) {
                                        img_menuAduanAlamatSekarang.setEnabled(false);
                                        img_menuAduanAlamatSekarang.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("nama_rt".equals(res_keyAduan)) {
                                        img_menuAduanRT.setEnabled(false);
                                        img_menuAduanRT.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("no_akta".equals(res_keyAduan)) {
                                        img_menuAduanNomorAktaKelahiran.setEnabled(false);
                                        img_menuAduanNomorAktaKelahiran.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("tempat_lahir".equals(res_keyAduan)) {
                                        img_menuAduanTempatLahir.setEnabled(false);
                                        img_menuAduanTempatLahir.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("tgl_lahir".equals(res_keyAduan)) {
                                        img_menuAduanTanggalLahir.setEnabled(false);
                                        img_menuAduanTanggalLahir.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("waktu_lahir".equals(res_keyAduan)) {
                                        img_menuAduanWaktuKelahiran.setEnabled(false);
                                        img_menuAduanWaktuKelahiran.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("tempat_dilahirkan".equals(res_keyAduan)) {
                                        img_menuAduanTempatDilahirkan.setEnabled(false);
                                        img_menuAduanTempatDilahirkan.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("jenis_kelahiran".equals(res_keyAduan)) {
                                        img_menuAduanJenisKelahiran.setEnabled(false);
                                        img_menuAduanJenisKelahiran.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("anak_ke".equals(res_keyAduan)) {
                                        img_menuAduanAnakKe.setEnabled(false);
                                        img_menuAduanAnakKe.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("penolong_kelahiran".equals(res_keyAduan)) {
                                        img_menuAduanPenolongKelahiran.setEnabled(false);
                                        img_menuAduanPenolongKelahiran.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("berat_lahir".equals(res_keyAduan)) {
                                        img_menuAduanBeratLahir.setEnabled(false);
                                        img_menuAduanBeratLahir.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("panjang_lahir".equals(res_keyAduan)) {
                                        img_menuAduanPanjangLahir.setEnabled(false);
                                        img_menuAduanPanjangLahir.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("pendidikan_kk".equals(res_keyAduan)) {
                                        img_menuAduanPendidikanKK.setEnabled(false);
                                        img_menuAduanPendidikanKK.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("pendidikan_tempuh".equals(res_keyAduan)) {
                                        img_menuAduanPendidikanTempuh.setEnabled(false);
                                        img_menuAduanPendidikanTempuh.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("pekerjaan".equals(res_keyAduan)) {
                                        img_menuAduanPekerjaan.setEnabled(false);
                                        img_menuAduanPekerjaan.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("status_warganegara".equals(res_keyAduan)) {
                                        img_menuAduanStatusKewarganegaraan.setEnabled(false);
                                        img_menuAduanStatusKewarganegaraan.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("nomor_paspor".equals(res_keyAduan)) {
                                        img_menuAduanNoPaspor.setEnabled(false);
                                        img_menuAduanNoPaspor.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("tgl_akhirpaspor".equals(res_keyAduan)) {
                                        img_menuAduanTglAkhirPaspor.setEnabled(false);
                                        img_menuAduanTglAkhirPaspor.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("nik_ayah".equals(res_keyAduan)) {
                                        img_menuAduanNIKAyah.setEnabled(false);
                                        img_menuAduanNIKAyah.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("nama_ayah".equals(res_keyAduan)) {
                                        img_menuAduanNamaAyah.setEnabled(false);
                                        img_menuAduanNamaAyah.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("nik_ibu".equals(res_keyAduan)) {
                                        img_menuAduanNIKIbu.setEnabled(false);
                                        img_menuAduanNIKIbu.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("nama_ibu".equals(res_keyAduan)) {
                                        img_menuAduanNamaIbu.setEnabled(false);
                                        img_menuAduanNamaIbu.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("status_perkawinan".equals(res_keyAduan)) {
                                        img_menuAduanStatusPerkawinan.setEnabled(false);
                                        img_menuAduanStatusPerkawinan.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("no_bukunikah".equals(res_keyAduan)) {
                                        img_menuAduanNoBukuNikah.setEnabled(false);
                                        img_menuAduanNoBukuNikah.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("tgl_perkawinan".equals(res_keyAduan)) {
                                        img_menuAduanTglPerkawinan.setEnabled(false);
                                        img_menuAduanTglPerkawinan.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("akta_perceraian".equals(res_keyAduan)) {
                                        img_menuAduanAktaPerceraian.setEnabled(false);
                                        img_menuAduanAktaPerceraian.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("tgl_perceraian".equals(res_keyAduan)) {
                                        img_menuAduanTglPerceraian.setEnabled(false);
                                        img_menuAduanTglPerceraian.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("golongan_darah".equals(res_keyAduan)) {
                                        img_menuAduanGolonganDarah.setEnabled(false);
                                        img_menuAduanGolonganDarah.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("cacat".equals(res_keyAduan)) {
                                        img_menuAduanCacat.setEnabled(false);
                                        img_menuAduanCacat.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("sakit_menahun".equals(res_keyAduan)) {
                                        img_menuAduanSakitMenahun.setEnabled(false);
                                        img_menuAduanSakitMenahun.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("akseptor_kb".equals(res_keyAduan)) {
                                        img_menuAduanAkseptorKB.setEnabled(false);
                                        img_menuAduanAkseptorKB.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    } else if ("asuransi".equals(res_keyAduan)) {
                                        img_menuAduanAsuransi.setEnabled(false);
                                        img_menuAduanAsuransi.setImageResource(R.drawable.ic_baseline_access_time_24);
                                    }

                                    //hilangkan loading
                                    loadingDialog.dissmissDialog();
                                }
                            } else {
//                                Toast.makeText(ProfilActivity.this, "Data Aduan Tidak Ada!", Toast.LENGTH_SHORT).show();
                                loadingDialog.dissmissDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingDialog.dissmissDialog();
                            Toast.makeText(ProfilActivity.this, "Data Aduan Tidak Ada!", Toast.LENGTH_LONG).show();
                        }

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dissmissDialog();
                Toast.makeText(ProfilActivity.this, "Tidak Ada Koneksi Internet!" + error, Toast.LENGTH_LONG).show();
            }
        }) {
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(ProfilActivity.this);
        requestQueue.add(stringRequest);
    }

    private void loadDataStatusAduanSelesai() {
        String URL_READSTATUSADUAN = link + "listaduan?token=" + token + "&user_id=" + id_user + "&status=selesai";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READSTATUSADUAN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    //data aduan proses
                                    String res_id = jsonObject.getString("id").trim();
                                    String res_userId = jsonObject.getString("user_id").trim();
                                    String res_keyAduan = jsonObject.getString("key").trim();
                                    String res_isiAduan = jsonObject.getString("isi").trim();
                                    String res_statusAduan = jsonObject.getString("status").trim();
                                    String res_notifAduan = jsonObject.getString("notif").trim();
                                    String res_createdAt = jsonObject.getString("created_at").trim();
                                    String res_updatedAt = jsonObject.getString("updated_at").trim();

                                    if ("nik".equals(res_keyAduan)) {
                                        img_menuAduanNIK.setEnabled(false);
                                        img_menuAduanNIK.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("nama_penduduk".equals(res_keyAduan)) {
                                        img_menuAduanNIK.setEnabled(false);
                                        img_menuAduanNIK.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("status_ktp".equals(res_keyAduan)) {
                                        img_menuAduanKTPEl.setEnabled(false);
                                        img_menuAduanKTPEl.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("status_rekam".equals(res_keyAduan)) {
                                        img_menuAduanStatusRekam.setEnabled(false);
                                        img_menuAduanStatusRekam.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("id_card".equals(res_keyAduan)) {
                                        img_menuAduanIdCard.setEnabled(false);
                                        img_menuAduanIdCard.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("kk_sebelum".equals(res_keyAduan)) {
                                        img_menuAduanNoKK.setEnabled(false);
                                        img_menuAduanNoKK.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("hubungan_keluarga".equals(res_keyAduan)) {
                                        img_menuAduanHubunganKeluarga.setEnabled(false);
                                        img_menuAduanHubunganKeluarga.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("jk".equals(res_keyAduan)) {
                                        img_menuAduanJenisKelamin.setEnabled(false);
                                        img_menuAduanJenisKelamin.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("agama".equals(res_keyAduan)) {
                                        img_menuAduanAgama.setEnabled(false);
                                        img_menuAduanAgama.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("status_penduduk".equals(res_keyAduan)) {
                                        img_menuAduanStatusPenduduk.setEnabled(false);
                                        img_menuAduanStatusPenduduk.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("no_telp".equals(res_keyAduan)) {
                                        img_menuAduanNoTelp.setEnabled(false);
                                        img_menuAduanNoTelp.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("email".equals(res_keyAduan)) {
                                        img_menuAduanAlamatEmail.setEnabled(false);
                                        img_menuAduanAlamatEmail.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("alamat_sebelum".equals(res_keyAduan)) {
                                        img_menuAduanAlamatSebelum.setEnabled(false);
                                        img_menuAduanAlamatSebelum.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("alamat_sekarang".equals(res_keyAduan)) {
                                        img_menuAduanAlamatSekarang.setEnabled(false);
                                        img_menuAduanAlamatSekarang.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("nama_rt".equals(res_keyAduan)) {
                                        img_menuAduanRT.setEnabled(false);
                                        img_menuAduanRT.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("no_akta".equals(res_keyAduan)) {
                                        img_menuAduanNomorAktaKelahiran.setEnabled(false);
                                        img_menuAduanNomorAktaKelahiran.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("tempat_lahir".equals(res_keyAduan)) {
                                        img_menuAduanTempatLahir.setEnabled(false);
                                        img_menuAduanTempatLahir.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("tgl_lahir".equals(res_keyAduan)) {
                                        img_menuAduanTanggalLahir.setEnabled(false);
                                        img_menuAduanTanggalLahir.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("waktu_lahir".equals(res_keyAduan)) {
                                        img_menuAduanWaktuKelahiran.setEnabled(false);
                                        img_menuAduanWaktuKelahiran.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("tempat_dilahirkan".equals(res_keyAduan)) {
                                        img_menuAduanTempatDilahirkan.setEnabled(false);
                                        img_menuAduanTempatDilahirkan.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("jenis_kelahiran".equals(res_keyAduan)) {
                                        img_menuAduanJenisKelahiran.setEnabled(false);
                                        img_menuAduanJenisKelahiran.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("anak_ke".equals(res_keyAduan)) {
                                        img_menuAduanAnakKe.setEnabled(false);
                                        img_menuAduanAnakKe.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("penolong_kelahiran".equals(res_keyAduan)) {
                                        img_menuAduanPenolongKelahiran.setEnabled(false);
                                        img_menuAduanPenolongKelahiran.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("berat_lahir".equals(res_keyAduan)) {
                                        img_menuAduanBeratLahir.setEnabled(false);
                                        img_menuAduanBeratLahir.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("panjang_lahir".equals(res_keyAduan)) {
                                        img_menuAduanPanjangLahir.setEnabled(false);
                                        img_menuAduanPanjangLahir.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("pendidikan_kk".equals(res_keyAduan)) {
                                        img_menuAduanPendidikanKK.setEnabled(false);
                                        img_menuAduanPendidikanKK.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("pendidikan_tempuh".equals(res_keyAduan)) {
                                        img_menuAduanPendidikanTempuh.setEnabled(false);
                                        img_menuAduanPendidikanTempuh.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("pekerjaan".equals(res_keyAduan)) {
                                        img_menuAduanPekerjaan.setEnabled(false);
                                        img_menuAduanPekerjaan.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("status_warganegara".equals(res_keyAduan)) {
                                        img_menuAduanStatusKewarganegaraan.setEnabled(false);
                                        img_menuAduanStatusKewarganegaraan.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("nomor_paspor".equals(res_keyAduan)) {
                                        img_menuAduanNoPaspor.setEnabled(false);
                                        img_menuAduanNoPaspor.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("tgl_akhirpaspor".equals(res_keyAduan)) {
                                        img_menuAduanTglAkhirPaspor.setEnabled(false);
                                        img_menuAduanTglAkhirPaspor.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("nik_ayah".equals(res_keyAduan)) {
                                        img_menuAduanNIKAyah.setEnabled(false);
                                        img_menuAduanNIKAyah.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("nama_ayah".equals(res_keyAduan)) {
                                        img_menuAduanNamaAyah.setEnabled(false);
                                        img_menuAduanNamaAyah.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("nik_ibu".equals(res_keyAduan)) {
                                        img_menuAduanNIKIbu.setEnabled(false);
                                        img_menuAduanNIKIbu.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("nama_ibu".equals(res_keyAduan)) {
                                        img_menuAduanNamaIbu.setEnabled(false);
                                        img_menuAduanNamaIbu.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("status_perkawinan".equals(res_keyAduan)) {
                                        img_menuAduanStatusPerkawinan.setEnabled(false);
                                        img_menuAduanStatusPerkawinan.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("no_bukunikah".equals(res_keyAduan)) {
                                        img_menuAduanNoBukuNikah.setEnabled(false);
                                        img_menuAduanNoBukuNikah.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("tgl_perkawinan".equals(res_keyAduan)) {
                                        img_menuAduanTglPerkawinan.setEnabled(false);
                                        img_menuAduanTglPerkawinan.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("akta_perceraian".equals(res_keyAduan)) {
                                        img_menuAduanAktaPerceraian.setEnabled(false);
                                        img_menuAduanAktaPerceraian.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("tgl_perceraian".equals(res_keyAduan)) {
                                        img_menuAduanTglPerceraian.setEnabled(false);
                                        img_menuAduanTglPerceraian.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("golongan_darah".equals(res_keyAduan)) {
                                        img_menuAduanGolonganDarah.setEnabled(false);
                                        img_menuAduanGolonganDarah.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("cacat".equals(res_keyAduan)) {
                                        img_menuAduanCacat.setEnabled(false);
                                        img_menuAduanCacat.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("sakit_menahun".equals(res_keyAduan)) {
                                        img_menuAduanSakitMenahun.setEnabled(false);
                                        img_menuAduanSakitMenahun.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("akseptor_kb".equals(res_keyAduan)) {
                                        img_menuAduanAkseptorKB.setEnabled(false);
                                        img_menuAduanAkseptorKB.setImageResource(R.drawable.ic_baseline_check_24);
                                    } else if ("asuransi".equals(res_keyAduan)) {
                                        img_menuAduanAsuransi.setEnabled(false);
                                        img_menuAduanAsuransi.setImageResource(R.drawable.ic_baseline_check_24);
                                    }

                                    if (res_notifAduan.equals("aktif")) {
                                        statusAduanSelesaiList.add(new StatusAduanModel(res_id, res_userId, res_keyAduan, res_isiAduan, res_statusAduan,
                                                res_notifAduan, res_createdAt, res_updatedAt));
                                        statusAduanSelesaiAdapter = new StatusAduanSelesaiAdapter(ProfilActivity.this, statusAduanSelesaiList);
                                        rv_aduanSelesai.setAdapter(statusAduanSelesaiAdapter);
                                        statusAduanSelesaiAdapter.setOnItemClickCallback(new StatusAduanSelesaiAdapter.OnItemClickCallback() {
                                            @Override
                                            public void onItemClicked(StatusAduanModel data) {
                                                dialogHapusAduanSelesai(data.getId());
                                            }
                                        });

                                    } else {

                                    }

                                    //hilangkan loading
                                    loadingDialog.dissmissDialog();
                                }
                            } else {
                                loadingDialog.dissmissDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingDialog.dissmissDialog();
                            Toast.makeText(ProfilActivity.this, "Data Aduan Tidak Ada!", Toast.LENGTH_LONG).show();
                        }

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dissmissDialog();
                Toast.makeText(ProfilActivity.this, "Tidak Ada Koneksi Internet!" + error, Toast.LENGTH_LONG).show();
            }
        }) {
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(ProfilActivity.this);
        requestQueue.add(stringRequest);
    }

    private void dialogHapusAduanSelesai(String aduanID) {
        AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(ProfilActivity.this);
        alertdialogBuilder.setTitle("Konfismasi Hapus Notifikasi");
        alertdialogBuilder.setMessage("Apakah Anda Yakin Menghapus Notifikasi Ini?");
        alertdialogBuilder.setCancelable(false);
        alertdialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String notif = "non-aktif";
                ubahNotifAduan(aduanID, notif);
            }
        });
        alertdialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = alertdialogBuilder.create();
        alertDialog.show();
    }

    private void ubahNotifAduan(String aduanID, String notif) {
        String URL_UBAHNOTIF = link + "ubahnotifaduan";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UBAHNOTIF,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(ProfilActivity.this, "Hapus Notifikasi Sukses", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                                Intent i = new Intent(ProfilActivity.this, ProfilActivity.class);
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(i);
                                overridePendingTransition(0, 0);
                            } else {
                                Toast.makeText(ProfilActivity.this, "Hapus Notifikasi Gagal!", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProfilActivity.this, "Hapus Notifikasi Gagal!" + e.toString(), Toast.LENGTH_LONG).show();
                            loadingDialog.dissmissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfilActivity.this, "Hapus Notifikasi Gagal! : Cek Koneksi Anda" + error, Toast.LENGTH_LONG).show();
                        loadingDialog.dissmissDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", aduanID);
                params.put("token", token);
                params.put("notif", notif);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void loadDataDiri() {
        String URL_READ = link + "penduduk/" + id_user;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    //data diri
                                    String res_nik = jsonObject.getString("nik").trim();
                                    String res_nama = jsonObject.getString("nama_penduduk").trim();
                                    String res_ktpEl = jsonObject.getString("status_ktp").trim();
                                    String res_statusRekam = jsonObject.getString("status_rekam").trim();
                                    String res_idCard = jsonObject.getString("id_card").trim();
                                    String res_noKK = jsonObject.getString("kk_sebelum").trim();
                                    String res_hubunganKeluarga = jsonObject.getString("hubungan_keluarga").trim();
                                    String res_jenisKelamin = jsonObject.getString("jk").trim();
                                    String res_agama = jsonObject.getString("agama").trim();
                                    String res_statusPenduduk = jsonObject.getString("status_penduduk").trim();
                                    String res_noTelepon = jsonObject.getString("no_telp").trim();
                                    String res_alamatEmail = jsonObject.getString("email").trim();
                                    String res_alamatSebelum = jsonObject.getString("alamat_sebelum").trim();
                                    String res_alamatSekarang = jsonObject.getString("alamat_sekarang").trim();
                                    String res_rt = jsonObject.getString("rt_id").trim();
                                    String res_namaRt = jsonObject.getString("nama_rt").trim();

                                    //data kelahiran
                                    String res_nomorAktakelahiran = jsonObject.getString("no_akta").trim();
                                    String res_tempatLahir = jsonObject.getString("tempat_lahir").trim();
                                    String res_tglLahir = jsonObject.getString("tgl_lahir").trim();
                                    String res_waktuKelahiran = jsonObject.getString("waktu_lahir").trim();
                                    String res_tempatDilahirkan = jsonObject.getString("tempat_dilahirkan").trim();
                                    String res_jenisKelahiran = jsonObject.getString("jenis_kelahiran").trim();
                                    String res_anakKe = jsonObject.getString("anak_ke").trim();
                                    String res_penolongKelahiran = jsonObject.getString("penolong_kelahiran").trim();
                                    String res_beratLahir = jsonObject.getString("berat_lahir").trim();
                                    String res_panjangLahir = jsonObject.getString("panjang_lahir").trim();

                                    //data pendidikan
                                    String res_pendidikanKK = jsonObject.getString("pendidikan_kk").trim();
                                    String res_pendidikanTempuh = jsonObject.getString("pendidikan_tempuh").trim();
                                    String res_pekerjaan = jsonObject.getString("pekerjaan").trim();

                                    //data kewarganegaraan
                                    String res_statusKewarganegaraan = jsonObject.getString("status_warganegara").trim();
                                    String res_noPaspor = jsonObject.getString("nomor_paspor").trim();
                                    String res_tglAkhirPaspor = jsonObject.getString("tgl_akhirpaspor").trim();

                                    //data keluarga
                                    String res_nikAyah = jsonObject.getString("nik_ayah").trim();
                                    String res_namaAyah = jsonObject.getString("nama_ayah").trim();
                                    String res_nikIbu = jsonObject.getString("nik_ibu").trim();
                                    String res_namaIbu = jsonObject.getString("nama_ibu").trim();

                                    //data perkawinan
                                    String res_statusPerkawinan = jsonObject.getString("status_perkawinan").trim();
                                    String res_noBukuNikah = jsonObject.getString("no_bukunikah").trim();
                                    String res_tglPerkawinan = jsonObject.getString("tgl_perkawinan").trim();
                                    String res_aktaPerceraian = jsonObject.getString("akta_perceraian").trim();
                                    String res_tglPerceraian = jsonObject.getString("tgl_perceraian").trim();

                                    //data kesehatan
                                    String res_golonganDarah = jsonObject.getString("golongan_darah").trim();
                                    String res_cacat = jsonObject.getString("cacat").trim();
                                    String res_sakitMenahun = jsonObject.getString("sakit_menahun").trim();
                                    String res_akseptorKB = jsonObject.getString("akseptor_kb").trim();
                                    String res_asuransi = jsonObject.getString("asuransi").trim();


                                    TextFuntion textFuntion = new TextFuntion();
                                    //data diri
                                    textFuntion.setTextDanNullData(et_nama1, res_nama);
                                    textFuntion.setTextDanNullData(et_nik, res_nik);
                                    textFuntion.setTextDanNullData(et_nama, res_nama);
                                    textFuntion.setTextDanNullData(et_ktpEl, res_ktpEl);
                                    textFuntion.setTextDanNullData(et_statusRekam, res_statusRekam);
                                    textFuntion.setTextDanNullData(et_idCard, res_idCard);
                                    textFuntion.setTextDanNullData(et_noKK, res_noKK);
                                    textFuntion.setTextDanNullData(et_ktpEl, res_ktpEl);
                                    textFuntion.setTextDanNullData(et_hubunganKeluarga, res_hubunganKeluarga);
                                    textFuntion.setTextDanNullData(et_jenisKelamin, res_jenisKelamin);
                                    textFuntion.setTextDanNullData(et_agama, res_agama);
                                    textFuntion.setTextDanNullData(et_statusPenduduk, res_statusPenduduk);
                                    textFuntion.setTextDanNullData(et_noTelepon, res_noTelepon);
                                    textFuntion.setTextDanNullData(et_alamatEmail, res_alamatEmail);
                                    textFuntion.setTextDanNullData(et_alamatSebelum, res_alamatSebelum);
                                    textFuntion.setTextDanNullData(et_alamatSekarang, res_alamatSekarang);
                                    textFuntion.setTextDanNullData(et_rt, res_namaRt);

                                    //data kelahiran
                                    textFuntion.setTextDanNullData(et_nomorAktakelahiran, res_nomorAktakelahiran);
                                    textFuntion.setTextDanNullData(et_tempatLahir, res_tempatLahir);
                                    textFuntion.setTextDanNullData(et_tanggalLahir, res_tglLahir);
                                    textFuntion.setTextDanNullData(et_waktuKelahiran, res_waktuKelahiran);
                                    textFuntion.setTextDanNullData(et_tempatDilahirkan, res_tempatDilahirkan);
                                    textFuntion.setTextDanNullData(et_jenisKelahiran, res_jenisKelahiran);
                                    textFuntion.setTextDanNullData(et_anakKe, res_anakKe);
                                    textFuntion.setTextDanNullData(et_penolongKelahiran, res_penolongKelahiran);
                                    textFuntion.setTextDanNullData(et_beratLahir, res_beratLahir);
                                    textFuntion.setTextDanNullData(et_panjangLahir, res_panjangLahir);

                                    //data pendidikan
                                    textFuntion.setTextDanNullData(et_pendidikanKK, res_pendidikanKK);
                                    textFuntion.setTextDanNullData(et_pendidikanTempuh, res_pendidikanTempuh);
                                    textFuntion.setTextDanNullData(et_pekerjaan, res_pekerjaan);

                                    //data kewarganegaraan
                                    textFuntion.setTextDanNullData(et_statusKewarganegaraan, res_statusKewarganegaraan);
                                    textFuntion.setTextDanNullData(et_noPaspor, res_noPaspor);
                                    textFuntion.setTextDanNullData(et_tglAkhirPaspor, res_tglAkhirPaspor);

                                    //data keluarga
                                    textFuntion.setTextDanNullData(et_nikAyah, res_nikAyah);
                                    textFuntion.setTextDanNullData(et_namaAyah, res_namaAyah);
                                    textFuntion.setTextDanNullData(et_nikIbu, res_nikIbu);
                                    textFuntion.setTextDanNullData(et_namaIbu, res_namaIbu);

                                    //data perkawinan
                                    textFuntion.setTextDanNullData(et_statusPerkawinan, res_statusPerkawinan);
                                    textFuntion.setTextDanNullData(et_noBukuNikah, res_noBukuNikah);
                                    textFuntion.setTextDanNullData(et_tglPerkawinan, res_tglPerkawinan);
                                    textFuntion.setTextDanNullData(et_aktaPerceraian, res_aktaPerceraian);
                                    textFuntion.setTextDanNullData(et_tglPerceraian, res_tglPerceraian);

                                    //data kesehatan
                                    textFuntion.setTextDanNullData(et_golonganDarah, res_golonganDarah);
                                    textFuntion.setTextDanNullData(et_cacat, res_cacat);
                                    textFuntion.setTextDanNullData(et_sakitMenahun, res_sakitMenahun);
                                    textFuntion.setTextDanNullData(et_akseptorKB, res_akseptorKB);
                                    textFuntion.setTextDanNullData(et_asuransi, res_asuransi);

                                    //hilangkan loading
                                    loadingDialog.dissmissDialog();
                                }
                            } else {
                                Toast.makeText(ProfilActivity.this, "Data Akun Tidak Ada!", Toast.LENGTH_SHORT).show();
                                loadingDialog.dissmissDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingDialog.dissmissDialog();
                            Toast.makeText(ProfilActivity.this, "Data Akun Tidak Ada!" + e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dissmissDialog();
                Toast.makeText(ProfilActivity.this, "Tidak Ada Koneksi Internet!" + error, Toast.LENGTH_LONG).show();
            }
        }) {
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void setVisibleGoneBlack(CardView isiData1, ImageView imgData1, CardView isiData2, ImageView imgData2,
                                     CardView isiData3, ImageView imgData3, CardView isiData4, ImageView imgData4,
                                     CardView isiData5, ImageView imgData5, CardView isiData6, ImageView imgData6,
                                     CardView isiData7, ImageView imgData7, CardView isidata8, ImageView imgdata8) {
        if (isiData1.getVisibility() == View.VISIBLE) {
            isiData1.setVisibility(View.GONE);
            imgData1.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);
        } else {
            isiData1.setVisibility(View.VISIBLE);
            imgData1.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
            isiData2.setVisibility(View.GONE);
            imgData2.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);
            isiData3.setVisibility(View.GONE);
            imgData3.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);
            isiData4.setVisibility(View.GONE);
            imgData4.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);
            isiData5.setVisibility(View.GONE);
            imgData5.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);
            isiData6.setVisibility(View.GONE);
            imgData6.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);
            isiData7.setVisibility(View.GONE);
            imgData7.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);
            isidata8.setVisibility(View.GONE);
            imgdata8.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);
        }

    }

    private void dialogLogout() {
        AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(ProfilActivity.this);
        alertdialogBuilder.setTitle("Konfismasi Logout");
        alertdialogBuilder.setIcon(R.drawable.ic_baseline_exit_to_app_24);
        alertdialogBuilder.setMessage("Apakah Anda Yakin Ingin Logout?");
        alertdialogBuilder.setCancelable(false);
        alertdialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sessionManager.logout();
                finishAffinity();
            }
        });
        alertdialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = alertdialogBuilder.create();
        alertDialog.show();
    }

}