package com.cikarastudio.cikarajantungdesafix.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.adapter.ArtikelAdapter;
import com.cikarastudio.cikarajantungdesafix.adapter.KategoriAdapter;
import com.cikarastudio.cikarajantungdesafix.adapter.LaporanAdapter;
import com.cikarastudio.cikarajantungdesafix.model.ArtikelModel;
import com.cikarastudio.cikarajantungdesafix.model.KategoriModel;
import com.cikarastudio.cikarajantungdesafix.model.LaporanModel;
import com.cikarastudio.cikarajantungdesafix.session.SessionManager;
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;
import com.cikarastudio.cikarajantungdesafix.ui.forum.ForumFragment;
import com.cikarastudio.cikarajantungdesafix.ui.laporan.LaporanUserActivity;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;
import com.cikarastudio.cikarajantungdesafix.ui.profil.ProfilActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class HomeFragment extends Fragment {

    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    String id_user, profile_photo_path, link, linkGambar;
    ImageView img_photouser;
    TextView tv_nama;
    RecyclerView rv_artikel;
    private ArrayList<ArtikelModel> artikelList;
    private ArtikelAdapter artikelAdapter;
    CardView cr_fotoProfil,
            cr_dashboardLaporanDibuat, cr_dashboardForumDiikuti, cr_dashboardJumlahProduk, cr_dashboardSuratDibuat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        sessionManager = new SessionManager(getActivity());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        id_user = user.get(sessionManager.ID);
        profile_photo_path = user.get(sessionManager.PROFILE_PHOTO_PATH);

        HttpsTrustManager.allowAllSSL();

        //inisiasi link
        link = getString(R.string.link);
        linkGambar = getString(R.string.linkGambar);

        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoading();

        img_photouser = root.findViewById(R.id.img_photouser);
        tv_nama = root.findViewById(R.id.tv_nama);

        artikelList = new ArrayList<>();
        rv_artikel = root.findViewById(R.id.rv_artikel);
        LinearLayoutManager linearLayoutManageraaa = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv_artikel.setLayoutManager(linearLayoutManageraaa);
//        rv_laporanAll.setLayoutManager(new LinearLayoutManager(getContext()));
//        LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false); // last argument (true) is flag for reverse layout
//        rv_laporanAll.setLayoutManager(lm);
//        rv_laporanAll.setNestedScrollingEnabled(false);
        rv_artikel.setHasFixedSize(true);

        loadDataDiri();
        loadArtikel();

        cr_dashboardLaporanDibuat = root.findViewById(R.id.cr_dashboardLaporanDibuat);
        cr_dashboardLaporanDibuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keLaporanDibuat = new Intent(getActivity(), LaporanUserActivity.class);
                startActivity(keLaporanDibuat);
            }
        });

        cr_dashboardForumDiikuti = root.findViewById(R.id.cr_dashboardForumDiikuti);
        cr_dashboardForumDiikuti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cr_fotoProfil = root.findViewById(R.id.cr_fotoProfil);
        cr_fotoProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent keProfil = new Intent(getActivity(), ProfilActivity.class);
                startActivity(keProfil);
            }
        });

        return root;
    }

    private void loadArtikel() {
        String URL_READ = link + "listartikel";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    //            "id": 3,
                                    //            "user_id": 1,
                                    //            "kategoriartikel_id": 3,
                                    //            "judul_artikel": "launching aplikasi sidesa (sistem informasi desa)",
                                    //            "slug": "launching-aplikasi-sidesa-sistem-informasi-desa",
                                    //            "isi_artikel": "<p>MCSE boot camps have its supporters and its detractors. Some people do not understand why you should have to spend money on boot camp when you can get the MCSE study materials yourself at a fraction of the camp price. However, who has the willpower</p>\r\n\r\n<p>MCSE boot camps have its supporters and its detractors. Some people do not understand why you should have to spend money on boot camp when you can get the MCSE study materials yourself at a fraction of the camp price. However, who has the willpower to actually sit through a self-imposed MCSE training. who has the willpower to actually</p>",
                                    //            "view": 1,
                                    //            "gambar_artikel": "1616749110_about_banner.png",
                                    //            "created_at": "2021-03-26T01:58:30.000000Z",
                                    //            "updated_at": "2021-03-26T02:26:32.000000Z"

                                    //data artikel
                                    String res_id = jsonObject.getString("id").trim();
                                    String res_userId = jsonObject.getString("user_id").trim();
                                    String res_kategoriArtikelId = jsonObject.getString("kategoriartikel_id").trim();
                                    String res_judulArtikel = jsonObject.getString("judul_artikel").trim();
                                    String res_slug = jsonObject.getString("slug").trim();
                                    String res_isiArtikel = jsonObject.getString("isi_artikel").trim();
                                    String res_view = jsonObject.getString("view").trim();
                                    String res_gambarArtikel = jsonObject.getString("gambar_artikel").trim();
                                    String res_createdAt = jsonObject.getString("created_at").trim();
                                    String res_updatedAt = jsonObject.getString("updated_at").trim();

                                    String resi_gambarArtikel = res_gambarArtikel.replace(" ", "%20");

//                                    String jam = res_updateAtLaporan.substring(11, 13);
//                                    String menit = res_updateAtLaporan.substring(14, 16);

//                                    String tanggal = res_updateAtLaporan.substring(8, 10);
//                                    String bulan = res_updateAtLaporan.substring(5, 7);
//                                    String tahun = res_updateAtLaporan.substring(0, 4);

//                                    String resi_waktu = jam + "." + menit;
//                                    String resi_tanggal = tanggal + "-" + bulan + "-" + tahun;

//                                    Log.d("calpalnx", String.valueOf(resi_tanggal));
//                                    Log.d("calpalnx", String.valueOf(resi_waktu));

                                    artikelList.add(new ArtikelModel(res_id, res_userId, res_kategoriArtikelId, res_judulArtikel, res_slug, res_isiArtikel, res_view, resi_gambarArtikel, res_createdAt,
                                            res_updatedAt));
                                    artikelAdapter = new ArtikelAdapter(getContext(), artikelList);
                                    rv_artikel.setAdapter(artikelAdapter);

                                    //hilangkan loading
                                    loadingDialog.dissmissDialog();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Data Artikel Tidak Ada!", Toast.LENGTH_SHORT).show();
                                loadingDialog.dissmissDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingDialog.dissmissDialog();
                            Toast.makeText(getActivity(), "Data Artikel Tidak Ada!", Toast.LENGTH_LONG).show();
                        }

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dissmissDialog();
                Toast.makeText(getActivity(), "Tidak Ada Koneksi Internet!" + error, Toast.LENGTH_LONG).show();
            }
        }) {
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void loadDataDiri() {
        String URL_READ = link + "penduduk/" + id_user;
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
                            textFuntion.setTextDanNullData(tv_nama, res_nama);
//                            textFuntion.setTextDanNullData(et_nik, res_nik);
//                            textFuntion.setTextDanNullData(et_nama, res_nama);
//                            textFuntion.setTextDanNullData(et_ktpEl, res_ktpEl);
//                            textFuntion.setTextDanNullData(et_statusRekam, res_statusRekam);
//                            textFuntion.setTextDanNullData(et_idCard, res_idCard);
//                            textFuntion.setTextDanNullData(et_noKK, res_noKK);
//                            textFuntion.setTextDanNullData(et_ktpEl, res_ktpEl);
//                            textFuntion.setTextDanNullData(et_hubunganKeluarga, res_hubunganKeluarga);
//                            textFuntion.setTextDanNullData(et_jenisKelamin, res_jenisKelamin);
//                            textFuntion.setTextDanNullData(et_agama, res_agama);
//                            textFuntion.setTextDanNullData(et_statusPenduduk, res_statusPenduduk);
//                            textFuntion.setTextDanNullData(et_noTelepon, res_noTelepon);
//                            textFuntion.setTextDanNullData(et_alamatEmail, res_alamatEmail);
//                            textFuntion.setTextDanNullData(et_alamatSebelum, res_alamatSebelum);
//                            textFuntion.setTextDanNullData(et_alamatSekarang, res_alamatSekarang);
//                            textFuntion.setTextDanNullData(et_rt, res_rt);
//
//                            //data kelahiran
//                            textFuntion.setTextDanNullData(et_nomorAktakelahiran, res_nomorAktakelahiran);
//                            textFuntion.setTextDanNullData(et_tempatLahir, res_tempatLahir);
//                            textFuntion.setTextDanNullData(et_waktuKelahiran, res_waktuKelahiran);
//                            textFuntion.setTextDanNullData(et_tempatDilahirkan, res_tempatDilahirkan);
//                            textFuntion.setTextDanNullData(et_jenisKelahiran, res_jenisKelahiran);
//                            textFuntion.setTextDanNullData(et_anakKe, res_anakKe);
//                            textFuntion.setTextDanNullData(et_penolongKelahiran, res_penolongKelahiran);
//                            textFuntion.setTextDanNullData(et_beratLahir, res_beratLahir);
//                            textFuntion.setTextDanNullData(et_panjangLahir, res_panjangLahir);
//
//                            //data pendidikan
//                            textFuntion.setTextDanNullData(et_pendidikanKK, res_pendidikanKK);
//                            textFuntion.setTextDanNullData(et_pendidikanTempuh, res_pendidikanTempuh);
//                            textFuntion.setTextDanNullData(et_pekerjaan, res_pekerjaan);
//
//                            //data kewarganegaraan
//                            textFuntion.setTextDanNullData(et_statusKewarganegaraan, res_statusKewarganegaraan);
//                            textFuntion.setTextDanNullData(et_noPaspor, res_noPaspor);
//                            textFuntion.setTextDanNullData(et_tglAkhirPaspor, res_tglAkhirPaspor);
//
//                            //data keluarga
//                            textFuntion.setTextDanNullData(et_nikAyah, res_nikAyah);
//                            textFuntion.setTextDanNullData(et_namaAyah, res_namaAyah);
//                            textFuntion.setTextDanNullData(et_nikIbu, res_nikIbu);
//                            textFuntion.setTextDanNullData(et_namaIbu, res_namaIbu);
//
//                            //data perkawinan
//                            textFuntion.setTextDanNullData(et_statusPerkawinan, res_statusPerkawinan);
//                            textFuntion.setTextDanNullData(et_noBukuNikah, res_noBukuNikah);
//                            textFuntion.setTextDanNullData(et_tglPerkawinan, res_tglPerkawinan);
//                            textFuntion.setTextDanNullData(et_aktaPerceraian, res_aktaPerceraian);
//                            textFuntion.setTextDanNullData(et_tglPerceraian, res_tglPerceraian);
//
//                            //data kesehatan
//                            textFuntion.setTextDanNullData(et_golonganDarah, res_golonganDarah);
//                            textFuntion.setTextDanNullData(et_cacat, res_cacat);
//                            textFuntion.setTextDanNullData(et_sakitMenahun, res_sakitMenahun);
//                            textFuntion.setTextDanNullData(et_akseptorKB, res_akseptorKB);
//                            textFuntion.setTextDanNullData(et_asuransi, res_asuransi);

                            String resi_gambar = profile_photo_path.replace(" ", "%20");

                            String imageUrl = linkGambar + "user/" + resi_gambar;
                            Picasso.with(getActivity()).load(imageUrl).fit().centerCrop().into(img_photouser);
                            //hilangkan loading
                            loadingDialog.dissmissDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingDialog.dissmissDialog();
                            Toast.makeText(getActivity(), "Data Akun Tidak Ada!" + e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dissmissDialog();
                Toast.makeText(getActivity(), "Tidak Ada Koneksi Internet!" + error, Toast.LENGTH_LONG).show();
            }
        }) {
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }


}