package com.cikarastudio.cikarajantungdesafix.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.adapter.ArtikelAdapter;
import com.cikarastudio.cikarajantungdesafix.adapter.PerangkatDesaAdapter;
import com.cikarastudio.cikarajantungdesafix.adapter.ProdukSemuaAdapter;
import com.cikarastudio.cikarajantungdesafix.model.ArtikelModel;
import com.cikarastudio.cikarajantungdesafix.model.PerangkatDesaModel;
import com.cikarastudio.cikarajantungdesafix.model.ProdukSemuaModel;
import com.cikarastudio.cikarajantungdesafix.session.SessionManager;
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;
import com.cikarastudio.cikarajantungdesafix.ui.artikel.DetailArtikelActivity;
import com.cikarastudio.cikarajantungdesafix.ui.artikel.ListArtikelActivity;
import com.cikarastudio.cikarajantungdesafix.ui.laporan.LaporanUserActivity;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;
import com.cikarastudio.cikarajantungdesafix.ui.produk.ListProdukActivity;
import com.cikarastudio.cikarajantungdesafix.ui.profil.ProfilActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment implements View.OnClickListener {

    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    String id_user, link, linkGambar, token;
    CircleImageView img_photouser;
    TextView tv_nama, tv_lihatSelengkapnyaArtikel, tv_email, tv_lihatSelengkapnyaProduk;
    RecyclerView rv_artikel, rv_perangkatDesa, rv_produkSemua;
    TextView tv_dashboardLaporanDibuat, tv_dashboardForumDiikuti, tv_dashboardJumlahProduk, tv_dashboardSuratDibuat;
    CardView cr_fotoProfil,
            cr_dashboardLaporanDibuat, cr_dashboardForumDiikuti, cr_dashboardJumlahProduk, cr_dashboardSuratDibuat;
    private ArrayList<ArtikelModel> artikelList;
    private ArtikelAdapter artikelAdapter;
    private ArrayList<PerangkatDesaModel> perangkatDesaList;
    private PerangkatDesaAdapter perangkatDesaAdapter;
    private ArrayList<ProdukSemuaModel> produkSemuaList;
    private ProdukSemuaAdapter produkSemuaAdapter;

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

        tv_nama = root.findViewById(R.id.tv_nama);
        tv_email = root.findViewById(R.id.tv_email);

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

        produkSemuaList = new ArrayList<>();
        rv_produkSemua = root.findViewById(R.id.rv_produkSemua);
        LinearLayoutManager linearLayoutManageraaabbb = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv_produkSemua.setLayoutManager(linearLayoutManageraaabbb);
        rv_produkSemua.setHasFixedSize(true);

        perangkatDesaList = new ArrayList<>();
        rv_perangkatDesa = root.findViewById(R.id.rv_perangkatDesa);
        LinearLayoutManager rvKategoriAdapter = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_perangkatDesa.setLayoutManager(rvKategoriAdapter);
        rv_perangkatDesa.setHasFixedSize(true);

        loadArtikel();
        loadPerangkatDesa();
        loadSemuaProduk();

        tv_dashboardLaporanDibuat = root.findViewById(R.id.tv_dashboardLaporanDibuat);
        tv_dashboardForumDiikuti = root.findViewById(R.id.tv_dashboardForumDiikuti);
        tv_dashboardJumlahProduk = root.findViewById(R.id.tv_dashboardJumlahProduk);
        tv_dashboardSuratDibuat = root.findViewById(R.id.tv_dashboardSuratDibuat);

        tv_lihatSelengkapnyaArtikel = root.findViewById(R.id.tv_lihatSelengkapnyaArtikel);
        tv_lihatSelengkapnyaArtikel.setOnClickListener(this);

        tv_lihatSelengkapnyaProduk = root.findViewById(R.id.tv_lihatSelengkapnyaProduk);
        tv_lihatSelengkapnyaProduk.setOnClickListener(this);

        cr_dashboardLaporanDibuat = root.findViewById(R.id.cr_dashboardLaporanDibuat);
        cr_dashboardLaporanDibuat.setOnClickListener(this);

        cr_dashboardForumDiikuti = root.findViewById(R.id.cr_dashboardForumDiikuti);
        cr_dashboardForumDiikuti.setOnClickListener(this);

        cr_dashboardJumlahProduk = root.findViewById(R.id.cr_dashboardJumlahProduk);
        cr_dashboardJumlahProduk.setOnClickListener(this);

        cr_dashboardSuratDibuat = root.findViewById(R.id.cr_dashboardSuratDibuat);
        cr_dashboardSuratDibuat.setOnClickListener(this);

        cr_fotoProfil = root.findViewById(R.id.cr_fotoProfil);
        cr_fotoProfil.setOnClickListener(this);

        img_photouser = root.findViewById(R.id.img_photouser);
        img_photouser.setOnClickListener(this);

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        loadDataDiri();
        loadPotoProfil();
        loadDashboarHome();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_lihatSelengkapnyaArtikel:
                // do your code
                Intent keListArtikel = new Intent(getActivity(), ListArtikelActivity.class);
                startActivity(keListArtikel);
                break;
                case R.id.tv_lihatSelengkapnyaProduk:
                // do your code
                Intent keListProduk = new Intent(getActivity(), ListProdukActivity.class);
                startActivity(keListProduk);
                break;
            case R.id.cr_dashboardLaporanDibuat:
                // do your code
                Intent keLaporanDibuat = new Intent(getActivity(), LaporanUserActivity.class);
                startActivity(keLaporanDibuat);
                break;
            case R.id.cr_dashboardForumDiikuti:
                // do your code
                Navigation.findNavController(v).navigate(R.id.navigation_forum);
                break;
            case R.id.cr_dashboardJumlahProduk:
                // do your code
                Navigation.findNavController(v).navigate(R.id.navigation_lapak);
                break;
            case R.id.cr_dashboardSuratDibuat:
                // do your code
                Navigation.findNavController(v).navigate(R.id.navigation_surat);
                break;
//            case R.id.cr_fotoProfil:
//                // do your code
//                Intent keProfil = new Intent(getActivity(), ProfilActivity.class);
//                startActivity(keProfil);
//                break;
            case R.id.img_photouser:
                // do your code
                Intent keProfilA = new Intent(getActivity(), ProfilActivity.class);
                startActivity(keProfilA);
                break;
            default:
                break;
        }
    }

    private void loadPotoProfil() {
//        {{SERVER}}/user/{{USERID}}?token={{TOKEN}}
        String URL_READ = link + "user/" + id_user + "?token=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            //data dashboard laporan
                            String profile_photo_path = jsonObject.getString("profile_photo_path").trim();
                            String email = jsonObject.getString("email").trim();

                            tv_email.setText(email);

                            String resi_gambar = profile_photo_path.replace(" ", "%20");


                            String imageUrl = linkGambar + "user/" + resi_gambar;
                            Picasso.with(getActivity()).load(imageUrl).fit().centerCrop().into(img_photouser);
                            //hilangkan loading

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Tidak Ada Koneksi Internet!", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 10 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(jsonString, cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void loadDashboarHome() {
        String URL_READ = link + "dashboarduser/home/" + id_user;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            //data dashboard home
                            String res_laporan = jsonObject.getString("laporan").trim();
                            String res_surat = jsonObject.getString("surat").trim();
                            String res_produk = jsonObject.getString("produk").trim();
                            String res_forum = jsonObject.getString("forum").trim();

                            TextFuntion textFuntion = new TextFuntion();
                            //data dashboard home
                            textFuntion.setTextDanNullData(tv_dashboardLaporanDibuat, res_laporan);
                            textFuntion.setTextDanNullData(tv_dashboardForumDiikuti, res_forum);
                            textFuntion.setTextDanNullData(tv_dashboardJumlahProduk, res_produk);
                            textFuntion.setTextDanNullData(tv_dashboardSuratDibuat, res_surat);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Data Dashboard Tidak Ada!" + e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Tidak Ada Koneksi Internet!", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 10 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(jsonString, cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
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
                        if (artikelList.size() > 0) {
                            artikelList.clear();
                        }
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
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Data Berita Tidak Ada!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Tidak Ada Koneksi Internet!", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 10 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(jsonString, cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void loadDataDiri() {
        String URL_READ = link + "penduduk/" + id_user + "?token=" + token;
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
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Tidak Ada Koneksi Internet!", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 10 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(jsonString, cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
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
                        if (perangkatDesaList.size() > 0) {
                            perangkatDesaList.clear();
                        }
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

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Data Perangkat Desa Tidak Ada!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Tidak Ada Koneksi Internet!", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 10 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(jsonString, cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }

        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void loadSemuaProduk() {
        String URL_READ = link + "produk?token=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (produkSemuaList.size() > 0) {
                            produkSemuaList.clear();
                        }
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    //data perangkat desa
                                    String res_link = jsonObject.getString("link").trim();

                                    JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                                    String res_id = dataJsonObject.getString("id").trim();
                                    String res_lapakIdProduk = dataJsonObject.getString("lapak_id").trim();
                                    String res_namaProduk = dataJsonObject.getString("nama").trim();
                                    String res_keteranganProduk = dataJsonObject.getString("keterangan").trim();
                                    String res_gambarProduk = dataJsonObject.getString("gambar").trim();

                                    Integer res_hargaProduk = Integer.parseInt(dataJsonObject.getString("harga"));
                                    Integer res_dilihatProduk = Integer.parseInt(dataJsonObject.getString("dilihat"));
                                    String res_createdAtProduk = dataJsonObject.getString("created_at").trim();
                                    String res_updatedAtProduk = dataJsonObject.getString("updated_at").trim();

                                    String resi_photoProduk = res_gambarProduk.replace(" ", "%20");

                                    produkSemuaList.add(new ProdukSemuaModel(res_id, res_lapakIdProduk, res_namaProduk, res_keteranganProduk,
                                            resi_photoProduk, res_hargaProduk, res_dilihatProduk, res_createdAtProduk,
                                            res_updatedAtProduk, res_link));

                                    produkSemuaAdapter = new ProdukSemuaAdapter(getContext(), produkSemuaList);
                                    rv_produkSemua.setAdapter(produkSemuaAdapter);

                                    produkSemuaAdapter.setOnItemClickCallback(new ProdukSemuaAdapter.OnItemClickCallback() {
                                        @Override
                                        public void onItemClicked(ProdukSemuaModel data) {
                                            String linkProdukJadi = "https://puteran.cikarastudio.com/" + data.getLink();
                                            Intent keLinkProduk = new Intent(Intent.ACTION_VIEW, Uri.parse(linkProdukJadi));
                                            startActivity(keLinkProduk);
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Data Produk Tidak Ada!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Tidak Ada Koneksi Internet!", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 10 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(jsonString, cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }

        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}