package com.cikarastudio.cikarajantungdesafix.ui.surat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
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
import com.cikarastudio.cikarajantungdesafix.adapter.LaporanAdapter;
import com.cikarastudio.cikarajantungdesafix.adapter.SuratListUserAdapter;
import com.cikarastudio.cikarajantungdesafix.model.LaporanModel;
import com.cikarastudio.cikarajantungdesafix.model.LaporanUserModel;
import com.cikarastudio.cikarajantungdesafix.model.ProdukModel;
import com.cikarastudio.cikarajantungdesafix.model.SuratModel;
import com.cikarastudio.cikarajantungdesafix.model.SuratV2Model;
import com.cikarastudio.cikarajantungdesafix.session.SessionManager;
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SuratFragment extends Fragment implements View.OnClickListener {

    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    String id_user, link;
    TextView tv_dashboardTotalPengajuanSurat, tv_dashboardSuratSelesaiSurat, tv_dashboardSuratDiprosesSurat, tv_dashboardSuratMenungguSurat;
    RecyclerView rv_listSuratUser;
    private ArrayList<SuratV2Model> suratUserlist;
    SuratListUserAdapter suratListUserAdapter;
    ImageView img_tambahSurat;
    SearchView et_suratSearch;
    CardView cr_dashboardTotalSurat, cr_dashboardSelesaiSurat, cr_dashboardDiprosesSurat, cr_dashboardMenungguSurat;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_surat, container, false);

        sessionManager = new SessionManager(getActivity());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        id_user = user.get(sessionManager.ID);

        Log.d("calpalnx", "onCreateView: " + id_user);

        HttpsTrustManager.allowAllSSL();

        //inisiasi link
        link = getString(R.string.link);

        loadingDialog = new LoadingDialog(getActivity());

        suratUserlist = new ArrayList<>();
        rv_listSuratUser = root.findViewById(R.id.rv_listSuratUser);
        LinearLayoutManager linearLayoutManageraaa = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv_listSuratUser.setLayoutManager(linearLayoutManageraaa);
        rv_listSuratUser.setHasFixedSize(true);

        tv_dashboardTotalPengajuanSurat = root.findViewById(R.id.tv_dashboardTotalPengajuanSurat);
        tv_dashboardSuratSelesaiSurat = root.findViewById(R.id.tv_dashboardSuratSelesaiSurat);
        tv_dashboardSuratDiprosesSurat = root.findViewById(R.id.tv_dashboardSuratDiprosesSurat);
        tv_dashboardSuratMenungguSurat = root.findViewById(R.id.tv_dashboardSuratMenungguSurat);
        et_suratSearch = root.findViewById(R.id.et_suratSearch);

        cr_dashboardTotalSurat = root.findViewById(R.id.cr_dashboardTotalSurat);
        cr_dashboardTotalSurat.setOnClickListener(this);

        cr_dashboardSelesaiSurat = root.findViewById(R.id.cr_dashboardSelesaiSurat);
        cr_dashboardSelesaiSurat.setOnClickListener(this);

        cr_dashboardDiprosesSurat = root.findViewById(R.id.cr_dashboardDiprosesSurat);
        cr_dashboardDiprosesSurat.setOnClickListener(this);

        cr_dashboardMenungguSurat = root.findViewById(R.id.cr_dashboardMenungguSurat);
        cr_dashboardMenungguSurat.setOnClickListener(this);

        img_tambahSurat = root.findViewById(R.id.img_tambahSurat);
        img_tambahSurat.setOnClickListener(this);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadingDialog.startLoading();
        loadSuratUser();
        loadDashboardSurat();

        et_suratSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String nextText) {
                //Data akan berubah saat user menginputkan text/kata kunci pada SearchView
                nextText = nextText.toLowerCase();
                ArrayList<SuratV2Model> dataFilter = new ArrayList<>();
                for (SuratV2Model data : suratUserlist) {
                    String nama = data.getNama_surat().toLowerCase();
                    if (nama.contains(nextText)) {
                        dataFilter.add(data);
                    }
                }
                suratListUserAdapter.setFilter(dataFilter);
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_tambahSurat:
                // do your code
                Intent keTambahSurat = new Intent(getActivity(), ListKategoriSuratActivity.class);
                startActivity(keTambahSurat);
                break;
            case R.id.cr_dashboardTotalSurat:
                // do your code
                loadSuratUser();
                break;
            case R.id.cr_dashboardSelesaiSurat:
                // do your code
                filterDashboard("selesai");
                break;
            case R.id.cr_dashboardDiprosesSurat:
                // do your code
                filterDashboard("proses");
                break;
            case R.id.cr_dashboardMenungguSurat:
                // do your code
                filterDashboard("menunggu");
                break;
            default:
                break;
        }
    }

    private void filterDashboard(String bahan) {
        bahan = bahan.toLowerCase();
        ArrayList<SuratV2Model> dataFilter = new ArrayList<>();
        for (SuratV2Model data : suratUserlist) {
            String status = data.getStatus().toLowerCase();
            if (status.contains(bahan)) {
                dataFilter.add(data);
            }
        }
        suratListUserAdapter.setFilter(dataFilter);
    }

    private void loadDashboardSurat() {
        String URL_READ = link + "dashboarduser/surat/" + id_user;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            //data dashboard surat
                            String res_total = jsonObject.getString("total").trim();
                            String res_selesai = jsonObject.getString("selesai").trim();
                            String res_proses = jsonObject.getString("proses").trim();
                            String res_menunggu = jsonObject.getString("menunggu").trim();

                            TextFuntion textFuntion = new TextFuntion();
                            //data dashboard home

                            textFuntion.setTextDanNullData(tv_dashboardTotalPengajuanSurat, res_total);
                            textFuntion.setTextDanNullData(tv_dashboardSuratSelesaiSurat, res_selesai);
                            textFuntion.setTextDanNullData(tv_dashboardSuratDiprosesSurat, res_proses);
                            textFuntion.setTextDanNullData(tv_dashboardSuratMenungguSurat, res_menunggu);

                            //hilangkan loading
//                            loadingDialog.dissmissDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
//                            loadingDialog.dissmissDialog();
                            Toast.makeText(getActivity(), "Data Dashboard Tidak Ada!" + e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loadingDialog.dissmissDialog();
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

    private void loadSuratUser() {
        if (suratUserlist.size() > 0) {
            suratUserlist.clear();
        }
        String URL_READ = link + "listsuratbyuser/" + id_user;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    //data surat user
                                    String res_id = jsonObject.getString("id").trim();
                                    String res_userId = jsonObject.getString("user_id").trim();
                                    String res_formatsuratIdSurat = jsonObject.getString("formatsurat_id").trim();
                                    String res_statusSurat = jsonObject.getString("status").trim();
                                    String res_nomorSurat = jsonObject.getString("nomor_surat").trim();
                                    String res_tglAwalSurat = jsonObject.getString("tgl_awal").trim();
                                    String res_tglAkhirSurat = jsonObject.getString("tgl_akhir").trim();
                                    String res_createdAtSurat = jsonObject.getString("created_at").trim();
                                    String res_updatedAtSurat = jsonObject.getString("created_at").trim();
                                    String res_namaSuratSurat = jsonObject.getString("nama_surat").trim();


//                                    String res_keperluanSurat = jsonObject.getString("keperluan").trim();
//                                    String res_keteranganSurat = jsonObject.getString("keterangan").trim();
//                                    String res_tglAwalSurat = jsonObject.getString("tgl_awal").trim();
//                                    String res_tglAkhirSurat = jsonObject.getString("tgl_akhir").trim();
//                                    String res_atasNamaSurat = jsonObject.getString("atas_nama").trim();
//                                    String res_stafPemerintahanSurat = jsonObject.getString("staf_pemerintahan").trim();
//                                    String res_menjabatSurat = jsonObject.getString("menjabat").trim();
//                                    String res_tampilkanPotoSurat = jsonObject.getString("tampilkan_poto").trim();
//                                    String res_kepalaKKSurat = jsonObject.getString("kepala_kk").trim();
//                                    String res_noKKSurat = jsonObject.getString("no_kk").trim();
//                                    String res_rtTujuanSurat = jsonObject.getString("rt_tujuan").trim();
//                                    String res_rwTujuanSurat = jsonObject.getString("rw_tujuan").trim();
//                                    String res_dusunTujuanSurat = jsonObject.getString("dusun_tujuan").trim();
//                                    String res_desaTujuanSurat = jsonObject.getString("desa_tujuan").trim();
//                                    String res_kecamatanTujuanSurat = jsonObject.getString("kecamatan_tujuan").trim();
//                                    String res_kabupatenTujuanSurat = jsonObject.getString("kabupaten_tujuan").trim();
//                                    String res_alasanPindahSurat = jsonObject.getString("alasan_pindah").trim();
//                                    String res_tanggalPindahSurat = jsonObject.getString("tanggal_pindah").trim();
//                                    String res_jumlahPengikutSurat = jsonObject.getString("jumlah_pengikut").trim();
//                                    String res_barangSurat = jsonObject.getString("barang").trim();
//                                    String res_jenisSurat = jsonObject.getString("jenis").trim();
//                                    String res_namaSurat = jsonObject.getString("nama").trim();
//                                    String res_noIdentitasSurat = jsonObject.getString("nama").trim();
//                                    String res_tempatLahirSurat = jsonObject.getString("tempat_lahir").trim();
//                                    String res_tglLahirSurat = jsonObject.getString("tgl_lahir").trim();
//                                    String res_jkSurat = jsonObject.getString("jk").trim();
//                                    String res_agamaSurat = jsonObject.getString("agama").trim();
//                                    String res_alamatSurat = jsonObject.getString("alamat").trim();
//                                    String res_pekerjaanSurat = jsonObject.getString("pekerjaan").trim();
//                                    String res_ketuaAdatSurat = jsonObject.getString("ketua_adat").trim();
//                                    String res_perbedaanSurat = jsonObject.getString("perbedaan").trim();
//                                    String res_kartuIdentitasSurat = jsonObject.getString("kartu_identitas").trim();
//                                    String res_rincianSurat = jsonObject.getString("rincian").trim();
//                                    String res_usahaSurat = jsonObject.getString("usaha").trim();
//                                    String res_noJamkesosSurat = jsonObject.getString("no_jamkesos").trim();
//                                    String res_hariLahirSurat = jsonObject.getString("hari_lahir").trim();
//                                    String res_waktuLahirSurat = jsonObject.getString("waktu_lahir").trim();
//                                    String res_kelahiranKeSurat = jsonObject.getString("kelahiran_ke").trim();
//                                    String res_namaIbuSurat = jsonObject.getString("nama_ibu").trim();
//                                    String res_nikIbuSurat = jsonObject.getString("nik_ibu").trim();
//                                    String res_umurIbuSurat = jsonObject.getString("umur_ibu").trim();
//                                    String res_pekerjaanIbuSurat = jsonObject.getString("pekerjaan_ibu").trim();
//                                    String res_alamatIbuSurat = jsonObject.getString("alamat_ibu").trim();
//                                    String res_desaIbuSurat = jsonObject.getString("desa_ibu").trim();
//                                    String res_kecIbuSurat = jsonObject.getString("kec_ibu").trim();
//                                    String res_kabIbuSurat = jsonObject.getString("kab_ibu").trim();
//                                    String res_namaAyahSurat = jsonObject.getString("nama_ayah").trim();
//                                    String res_nikAyahSurat = jsonObject.getString("nik_ayah").trim();
//                                    String res_umurAyahSurat = jsonObject.getString("umur_ayah").trim();
//                                    String res_pekerjaanAyahSurat = jsonObject.getString("pekerjaan_ayah").trim();
//                                    String res_alamatAyahSurat = jsonObject.getString("alamat_ayah").trim();
//                                    String res_desaAyahSurat = jsonObject.getString("desa_ayah").trim();
//                                    String res_kecAyahSurat = jsonObject.getString("kec_ayah").trim();
//                                    String res_kabAyahSurat = jsonObject.getString("kab_ayah").trim();
//                                    String res_namaPelaporSurat = jsonObject.getString("nama_pelapor").trim();
//                                    String res_nikPelaporSurat = jsonObject.getString("nik_pelapor").trim();
//                                    String res_umurPelaporSurat = jsonObject.getString("umur_pelapor").trim();
//                                    String res_pekerjaanPelaporSurat = jsonObject.getString("pekerjaan_pelapor").trim();
//                                    String res_desaPelaporSurat = jsonObject.getString("desa_pelapor").trim();
//                                    String res_kecPelaporSurat = jsonObject.getString("kec_pelapor").trim();
//                                    String res_kabPelaporSurat = jsonObject.getString("kab_pelapor").trim();
//                                    String res_provPelaporSurat = jsonObject.getString("prov_pelapor").trim();
//                                    String res_hubPelaporSurat = jsonObject.getString("hub_pelapor").trim();
//                                    String res_tempatLahirPelaporSurat = jsonObject.getString("tempat_lahir_pelapor").trim();
//                                    String res_tanggalLahirPelaporSurat = jsonObject.getString("tanggal_lahir_pelapor").trim();
//                                    String res_namaSaksi1Surat = jsonObject.getString("nama_saksi1").trim();
//                                    String res_nikSaksi1Surat = jsonObject.getString("nik_saksi1").trim();
//                                    String res_tempatLahirSaksi1Surat = jsonObject.getString("tempat_lahir_saksi1").trim();
//                                    String res_tanggalLahirSaksi1Surat = jsonObject.getString("tanggal_lahir_saksi1").trim();
//                                    String res_umurSaksi1Surat = jsonObject.getString("umur_saksi1").trim();
//                                    String res_pekerjaanSaksi1Surat = jsonObject.getString("pekerjaan_saksi1").trim();
//                                    String res_desaSaksi1Surat = jsonObject.getString("desa_saksi1").trim();
//                                    String res_kecSaksi1Surat = jsonObject.getString("kec_saksi1").trim();
//                                    String res_kabSaksi1Surat = jsonObject.getString("kab_saksi1").trim();
//                                    String res_provSaksi1Surat = jsonObject.getString("prov_saksi1").trim();
//                                    String res_namaSaksi2Surat = jsonObject.getString("nama_saksi2").trim();
//                                    String res_nikSaksi2Surat = jsonObject.getString("nik_saksi2").trim();
//                                    String res_tempatLahirSaksi2Surat = jsonObject.getString("tempat_lahir_saksi2").trim();
//                                    String res_tanggalLahirSaksi2Surat = jsonObject.getString("tanggal_lahir_saksi2").trim();
//                                    String res_umurSaksi2Surat = jsonObject.getString("umur_saksi2").trim();
//                                    String res_pekerjaanSaksi2Surat = jsonObject.getString("pekerjaan_saksi2").trim();
//                                    String res_desaSaksi2Surat = jsonObject.getString("desa_saksi2").trim();
//                                    String res_kecSaksi2Surat = jsonObject.getString("kec_saksi2").trim();
//                                    String res_kabSaksi2Surat = jsonObject.getString("kab_saksi2").trim();
//                                    String res_provSaksi2Surat = jsonObject.getString("prov_saksi2").trim();


//                                    suratUserlist.add(new SuratModel(res_id, res_userId, res_formatsuratIdSurat,
//                                            res_statusSurat, res_nomorSurat, res_keperluanSurat, res_keteranganSurat, res_tglAwalSurat,
//                                            res_tglAkhirSurat, res_atasNamaSurat, res_stafPemerintahanSurat, res_menjabatSurat,
//                                            res_tampilkanPotoSurat, res_kepalaKKSurat, res_noKKSurat, res_rtTujuanSurat,
//                                            res_rwTujuanSurat, res_dusunTujuanSurat, res_desaTujuanSurat, res_kecamatanTujuanSurat,
//                                            res_kabupatenTujuanSurat, res_alasanPindahSurat, res_tanggalPindahSurat,
//                                            res_jumlahPengikutSurat, res_barangSurat, res_jenisSurat, res_namaSurat,
//                                            res_noIdentitasSurat, res_tempatLahirSurat, res_tglLahirSurat, res_jkSurat,
//                                            res_agamaSurat, res_alamatSurat, res_pekerjaanSurat, res_ketuaAdatSurat,
//                                            res_perbedaanSurat, res_kartuIdentitasSurat, res_rincianSurat, res_usahaSurat,
//                                            res_noJamkesosSurat, res_hariLahirSurat, res_waktuLahirSurat, res_kelahiranKeSurat,
//                                            res_namaIbuSurat, res_nikIbuSurat, res_umurIbuSurat, res_pekerjaanIbuSurat,
//                                            res_alamatIbuSurat, res_desaIbuSurat, res_kecIbuSurat, res_kabIbuSurat, res_namaAyahSurat,
//                                            res_nikAyahSurat, res_umurAyahSurat, res_pekerjaanAyahSurat, res_alamatAyahSurat,
//                                            res_desaAyahSurat, res_kecAyahSurat, res_kabAyahSurat, res_namaPelaporSurat,
//                                            res_nikPelaporSurat, res_umurPelaporSurat, res_pekerjaanPelaporSurat, res_desaPelaporSurat,
//                                            res_kecPelaporSurat, res_kabPelaporSurat, res_provPelaporSurat, res_hubPelaporSurat,
//                                            res_tempatLahirPelaporSurat, res_tanggalLahirPelaporSurat, res_namaSaksi1Surat, res_nikSaksi1Surat,
//                                            res_tempatLahirSaksi1Surat, res_tanggalLahirSaksi1Surat, res_umurSaksi1Surat, res_pekerjaanSaksi1Surat,
//                                            res_desaSaksi1Surat, res_kecSaksi1Surat, res_kabSaksi1Surat, res_provSaksi1Surat, res_namaSaksi2Surat,
//                                            res_nikSaksi2Surat, res_tempatLahirSaksi2Surat, res_tanggalLahirSaksi2Surat, res_umurSaksi2Surat,
//                                            res_pekerjaanSaksi2Surat, res_desaSaksi2Surat, res_kecSaksi2Surat, res_kabSaksi2Surat,
//                                            res_provSaksi2Surat, res_createdAtSurat, res_updatedAtSurat));


                                    suratUserlist.add(new SuratV2Model(res_id, res_userId, res_formatsuratIdSurat, res_statusSurat,
                                            res_nomorSurat, res_tglAwalSurat, res_tglAkhirSurat, res_createdAtSurat, res_updatedAtSurat, res_namaSuratSurat));
                                    suratListUserAdapter = new SuratListUserAdapter(getContext(), suratUserlist);
                                    rv_listSuratUser.setAdapter(suratListUserAdapter);
                                    //hilangkan loading
//                                    loadingDialog.dissmissDialog();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Data Surat Tidak Ada!", Toast.LENGTH_SHORT).show();
//                                loadingDialog.dissmissDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            loadingDialog.dissmissDialog();
                            Toast.makeText(getActivity(), "Data Surat Tidak Ada!" + e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                loadingDialog.dissmissDialog();
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