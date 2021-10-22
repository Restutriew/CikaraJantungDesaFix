package com.cikarastudio.cikarajantungdesafix.ui.profil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cikarastudio.cikarajantungdesafix.session.SessionManager;
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;
import com.cikarastudio.cikarajantungdesafix.template.TextFuntion;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;
import com.cikarastudio.cikarajantungdesafix.ui.login.LoginActivity;
import com.cikarastudio.cikarajantungdesafix.R;
import com.github.florent37.fiftyshadesof.FiftyShadesOf;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ProfilActivity extends AppCompatActivity {

    SessionManager sessionManager;
    LoadingDialog loadingDialog;

    String id_user, profile_photo_path;

    ImageView img_back, img_dataDiri, img_dataKelahiran,
            img_dataPendidikan, img_dataKewarganegaraan,
            img_dataKeluarga, img_dataPerkawinan,
            img_dataKesehatan,

    img_photoprofile;

    CardView cr_dataDiri, cr_isiDataDiri,
            cr_dataKelahiran, cr_isiDataKelahiran,
            cr_dataPendidikan, cr_isiDataPendidikan,
            cr_dataKewarganegaraan, cr_isiDataKewarganegaraan,
            cr_dataKeluarga, cr_isiDataKeluarga,
            cr_dataPerkawinan, cr_isiDataPerkawinan,
            cr_dataKesehatan, cr_isiDataKesehatan,
            cr_logout, cr_fotoProfil;

    TextView
            et_nama1, et_desa,
            et_nik, et_nama, et_ktpEl, et_statusRekam, et_idCard, et_noKK, et_hubunganKeluarga,
            et_jenisKelamin, et_agama, et_statusPenduduk, et_noTelepon, et_alamatEmail, et_alamatSebelum,
            et_alamatSekarang, et_rt,

    //data kelahiran
    et_nomorAktakelahiran, et_tempatLahir, et_waktuKelahiran, et_tempatDilahirkan, et_jenisKelahiran,
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        sessionManager = new SessionManager(ProfilActivity.this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        id_user = user.get(sessionManager.ID);
        profile_photo_path = user.get(sessionManager.PROFILE_PHOTO_PATH);

        HttpsTrustManager.allowAllSSL();

        loadingDialog = new LoadingDialog(ProfilActivity.this);
        loadingDialog.startLoading();


        img_back = findViewById(R.id.img_back);
        cr_dataDiri = findViewById(R.id.cr_dataDiri);
        cr_isiDataDiri = findViewById(R.id.cr_isiDataDiri);
        img_dataDiri = findViewById(R.id.img_dataDiri);
        cr_dataKelahiran = findViewById(R.id.cr_dataKelahiran);
        cr_isiDataKelahiran = findViewById(R.id.cr_isiDataKelahiran);
        img_dataKelahiran = findViewById(R.id.img_dataKelahiran);
        cr_dataPendidikan = findViewById(R.id.cr_dataPendidikan);
        cr_isiDataPendidikan = findViewById(R.id.cr_isiDataPendidikan);
        img_dataPendidikan = findViewById(R.id.img_dataPendidikan);
        cr_dataKewarganegaraan = findViewById(R.id.cr_dataKewarganegaraan);
        cr_isiDataKewarganegaraan = findViewById(R.id.cr_isiDataKewarganegaraan);
        img_dataKewarganegaraan = findViewById(R.id.img_dataKewarganegaraan);
        cr_dataKeluarga = findViewById(R.id.cr_dataKeluarga);
        cr_isiDataKeluarga = findViewById(R.id.cr_isiDataKeluarga);
        img_dataKeluarga = findViewById(R.id.img_dataKeluarga);
        cr_dataPerkawinan = findViewById(R.id.cr_dataPerkawinan);
        cr_isiDataPerkawinan = findViewById(R.id.cr_isiDataPerkawinan);
        img_dataPerkawinan = findViewById(R.id.img_dataPerkawinan);
        cr_dataKesehatan = findViewById(R.id.cr_dataKesehatan);
        cr_isiDataKesehatan = findViewById(R.id.cr_isiDataKesehatan);
        img_dataKesehatan = findViewById(R.id.img_dataKesehatan);
        cr_logout = findViewById(R.id.cr_logout);

        img_photoprofile = findViewById(R.id.img_photoprofile);

        cr_fotoProfil = findViewById(R.id.cr_fotoProfil);
        et_desa = findViewById(R.id.et_desa);
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

        cr_dataDiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibleGoneBlack(cr_isiDataDiri, img_dataDiri);
            }
        });

        cr_dataKelahiran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibleGoneBlack(cr_isiDataKelahiran, img_dataKelahiran);
            }
        });

        cr_dataPendidikan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibleGoneBlack(cr_isiDataPendidikan, img_dataPendidikan);
            }
        });

        cr_dataKewarganegaraan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibleGoneBlack(cr_isiDataKewarganegaraan, img_dataKewarganegaraan);
            }
        });

        cr_dataKeluarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibleGoneBlack(cr_isiDataKeluarga, img_dataKeluarga);
            }
        });

        cr_dataPerkawinan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibleGoneBlack(cr_isiDataPerkawinan, img_dataPerkawinan);
            }
        });

        cr_dataKesehatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setVisibleGoneBlack(cr_isiDataKesehatan, img_dataKesehatan);
            }
        });

        cr_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogLogout();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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

    private void loadDataDiri() {
        String URL_READ = "https://jantungdesa.bunefit.com/api/penduduk/" + id_user;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

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

                            //data kelahiran
                            String res_nomorAktakelahiran = jsonObject.getString("no_akta").trim();
                            String res_tempatLahir = jsonObject.getString("tempat_lahir").trim();
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
                            textFuntion.setTextDanNullData(et_rt, res_rt);

                            //data kelahiran
                            textFuntion.setTextDanNullData(et_nomorAktakelahiran, res_nomorAktakelahiran);
                            textFuntion.setTextDanNullData(et_tempatLahir, res_tempatLahir);
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

                            String imageUrl = "https://jantungdesa.bunefit.com/public/img/user/" + profile_photo_path;
                            Picasso.with(ProfilActivity.this).load(imageUrl).fit().centerCrop().into(img_photoprofile);
                            //hilangkan loading
                                loadingDialog.dissmissDialog();

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

    private void setVisibleGoneBlack(CardView isiData, ImageView imgData) {
        if (isiData.getVisibility() == View.VISIBLE) {
            isiData.setVisibility(View.GONE);
            imgData.setImageResource(R.drawable.ic_baseline_arrow_forward_ios_24);
        } else {
            isiData.setVisibility(View.VISIBLE);
            imgData.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
        }

    }
}