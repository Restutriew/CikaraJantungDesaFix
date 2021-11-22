package com.cikarastudio.cikarajantungdesafix.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
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
import com.cikarastudio.cikarajantungdesafix.adapter.ArtikelAllAdapter;
import com.cikarastudio.cikarajantungdesafix.ui.artikel.DetailArtikelActivity;
import com.cikarastudio.cikarajantungdesafix.ui.artikel.ListArtikelActivity;
import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.adapter.ArtikelAdapter;
import com.cikarastudio.cikarajantungdesafix.adapter.PerangkatDesaAdapter;
import com.cikarastudio.cikarajantungdesafix.model.ArtikelModel;
import com.cikarastudio.cikarajantungdesafix.model.PerangkatDesaModel;
import com.cikarastudio.cikarajantungdesafix.session.SessionManager;
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;
import com.cikarastudio.cikarajantungdesafix.ui.laporan.LaporanUserActivity;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;
import com.cikarastudio.cikarajantungdesafix.ui.profil.ProfilActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class HomeFragment extends Fragment implements View.OnClickListener {

    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    String id_user, link, linkGambar, token;
    ImageView img_photouser;
    TextView tv_nama, tv_lihatSelengkapnyaArtikel;
    RecyclerView rv_artikel, rv_perangkatDesa;
    private ArrayList<ArtikelModel> artikelList;
    private ArtikelAdapter artikelAdapter;
    private ArrayList<PerangkatDesaModel> perangkatDesaList;
    private PerangkatDesaAdapter perangkatDesaAdapter;
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

        HttpsTrustManager.allowAllSSL();

        //inisiasi link
        link = getString(R.string.link);
        linkGambar = getString(R.string.linkGambar);

        //inisiasi token
        token = getString(R.string.token);

        loadingDialog = new LoadingDialog(getActivity());

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
        rv_artikel.setHasFixedSize(true);

        perangkatDesaList = new ArrayList<>();
        rv_perangkatDesa = root.findViewById(R.id.rv_perangkatDesa);
        LinearLayoutManager rvKategoriAdapter = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_perangkatDesa.setLayoutManager(rvKategoriAdapter);
        rv_perangkatDesa.setHasFixedSize(true);

        loadArtikel();
        loadPerangkatDesa();

        tv_lihatSelengkapnyaArtikel = root.findViewById(R.id.tv_lihatSelengkapnyaArtikel);
        tv_lihatSelengkapnyaArtikel.setOnClickListener(this);

        cr_dashboardLaporanDibuat = root.findViewById(R.id.cr_dashboardLaporanDibuat);
        cr_dashboardLaporanDibuat.setOnClickListener(this);

        cr_dashboardForumDiikuti = root.findViewById(R.id.cr_dashboardForumDiikuti);
        cr_dashboardForumDiikuti.setOnClickListener(this);

        cr_fotoProfil = root.findViewById(R.id.cr_fotoProfil);
        cr_fotoProfil.setOnClickListener(this);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadingDialog.startLoading();
        loadDataDiri();
        loadPotoProfil();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_lihatSelengkapnyaArtikel:
                // do your code
                Intent keListArtikel = new Intent(getActivity(), ListArtikelActivity.class);
                startActivity(keListArtikel);
                break;
            case R.id.cr_dashboardLaporanDibuat:
                // do your code
                Intent keLaporanDibuat = new Intent(getActivity(), LaporanUserActivity.class);
                startActivity(keLaporanDibuat);
                break;
            case R.id.cr_dashboardForumDiikuti:
                // do your code
                break;
            case R.id.cr_fotoProfil:
                // do your code
                Intent keProfil = new Intent(getActivity(), ProfilActivity.class);
                startActivity(keProfil);
                break;
            default:
                break;
        }
    }

    private void loadPotoProfil() {
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

                            Log.d("calpalnx", profile_photo_path);

                            String imageUrl = linkGambar + "user/" + resi_gambar;
                            Picasso.with(getActivity()).load(imageUrl).fit().centerCrop().into(img_photouser);
                            //hilangkan loading
                            loadingDialog.dissmissDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingDialog.dissmissDialog();
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

    private void loadArtikel() {
        String URL_READ = link + "list/artikel?token=" + token + "&kategori=semua";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

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
                                    String res_namaKategori = jsonObject.getString("nama_kategori").trim();

                                    String resi_gambarArtikel = res_gambarArtikel.replace(" ", "%20");

                                    artikelList.add(new ArtikelModel(res_id, res_userId, res_kategoriArtikelId, res_judulArtikel, res_slug, res_isiArtikel, res_view, resi_gambarArtikel, res_createdAt,
                                            res_updatedAt, res_namaKategori));
                                    artikelAdapter = new ArtikelAdapter(getContext(), artikelList);
                                    rv_artikel.setAdapter(artikelAdapter);
                                    artikelAdapter.setOnItemClickCallback(new ArtikelAdapter.OnItemClickCallback() {
                                        @Override
                                        public void onItemClicked(ArtikelModel data) {
                                            Intent transferArtikel = new Intent(getActivity(), DetailArtikelActivity.class);
                                            transferArtikel.putExtra(DetailArtikelActivity.ARTIKEL_DATA, data);
                                            startActivity(transferArtikel);
                                        }
                                    });

                                    //hilangkan loading
                                    loadingDialog.dissmissDialog();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Data Berita Tidak Ada!", Toast.LENGTH_SHORT).show();
                                loadingDialog.dissmissDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingDialog.dissmissDialog();
                            Toast.makeText(getActivity(), "Data Berita Tidak Ada!", Toast.LENGTH_LONG).show();
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
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    //data diri
                                    String res_nama = jsonObject.getString("nama_penduduk").trim();

                                    TextFuntion textFuntion = new TextFuntion();
                                    //data diri
                                    textFuntion.setTextDanNullData(tv_nama, res_nama);
                                    //hilangkan loading
                                    loadingDialog.dissmissDialog();
                                }
                            } else {
//                                Toast.makeText(getActivity(), "Data Akun Tidak Ada!", Toast.LENGTH_SHORT).show();
                                loadingDialog.dissmissDialog();
                            }
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

    private void loadPerangkatDesa() {
        String URL_READ = link + "list/perangkatdesa?token=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    //data perangkat desa

                                    String res_id = jsonObject.getString("id").trim();
                                    String res_namaPegawai = jsonObject.getString("nama_pegawai").trim();
                                    String res_nikPegawai = jsonObject.getString("nik").trim();
                                    String res_nipdPegawai = jsonObject.getString("nipd").trim();
                                    String res_nipPegawai = jsonObject.getString("nip").trim();
                                    String res_tempatLahirPegawai = jsonObject.getString("tempat_lahir").trim();
                                    String res_tglLahirPegawai = jsonObject.getString("tgl_lahir").trim();
                                    String res_jenisKelaminPegawai = jsonObject.getString("jk").trim();
                                    String res_pendidikanPegawai = jsonObject.getString("pendidikan").trim();
                                    String res_agamaPegawai = jsonObject.getString("agama").trim();
                                    String res_golonganPegawai = jsonObject.getString("golongan").trim();
                                    String res_noskPengangkatanPegawai = jsonObject.getString("nosk_pengangkatan").trim();
                                    String res_tglskPengangkatanPegawai = jsonObject.getString("tglsk_pengangkatan").trim();
                                    String res_noskPemberhentianPegawai = jsonObject.getString("nosk_pemberhentian").trim();
                                    String res_tglskPemberhentianPegawai = jsonObject.getString("tglsk_pemberhentian").trim();
                                    String res_masaJabatanPegawai = jsonObject.getString("masa_jabatan").trim();
                                    String res_jabatanPegawai = jsonObject.getString("jabatan").trim();
                                    String res_statusPegawaiPegawai = jsonObject.getString("status_pegawai").trim();
                                    String res_createdAtPegawai = jsonObject.getString("created_at").trim();
                                    String res_updatedAtPegawai = jsonObject.getString("updated_at").trim();
                                    String res_photoPegawai = jsonObject.getString("photo").trim();

                                    String resi_photoPegawai = res_photoPegawai.replace(" ", "%20");

                                    perangkatDesaList.add(new PerangkatDesaModel(res_id, res_namaPegawai, res_nikPegawai, res_nipdPegawai,
                                            res_nipPegawai, res_tempatLahirPegawai, res_tglLahirPegawai, res_jenisKelaminPegawai,
                                            res_pendidikanPegawai, res_agamaPegawai, res_golonganPegawai, res_noskPengangkatanPegawai,
                                            res_tglskPengangkatanPegawai, res_noskPemberhentianPegawai, res_tglskPemberhentianPegawai,
                                            res_masaJabatanPegawai, res_jabatanPegawai, res_statusPegawaiPegawai, res_createdAtPegawai,
                                            res_updatedAtPegawai, resi_photoPegawai));
                                    perangkatDesaAdapter = new PerangkatDesaAdapter(getContext(), perangkatDesaList);
                                    rv_perangkatDesa.setAdapter(perangkatDesaAdapter);

                                    //hilangkan loading
                                    loadingDialog.dissmissDialog();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Data Perangkat Desa Tidak Ada!", Toast.LENGTH_SHORT).show();
                                loadingDialog.dissmissDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingDialog.dissmissDialog();
                            Toast.makeText(getActivity(), "Data Perangkat Desa Tidak Ada!", Toast.LENGTH_LONG).show();
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