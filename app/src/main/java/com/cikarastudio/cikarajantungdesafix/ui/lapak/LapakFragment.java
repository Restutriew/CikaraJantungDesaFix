package com.cikarastudio.cikarajantungdesafix.ui.lapak;

import static android.graphics.Color.parseColor;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
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
import com.cikarastudio.cikarajantungdesafix.adapter.ProdukAdapter;
import com.cikarastudio.cikarajantungdesafix.model.ProdukModel;
import com.cikarastudio.cikarajantungdesafix.session.SessionManager;
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class LapakFragment extends Fragment {

    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    RecyclerView recyclerView;
    String id_user, link, linkGambar,
    //data lapak
    id_lapak, nama_lapak, alamat_lapak, tentang_lapak, telp_lapak, logo_lapak, status_lapak,
            token;

    LinearLayout line_editLapak;
    TextView tv_tambahProduk, tv_sortProdukAlfabet, tv_sortProdukHarga, tv_sortProdukPopuler, tv_sortProdukWaktu,
    //lapak
    tv_namaLapak, tv_alamatLapak, tv_telpLapak, tv_tentangLapak, tv_statusLapak,
    //atas
    tv_namaLapakAtas,
    //expand collapse
    tv_alamatLapakFull, tv_alamatLapakBtnLihat, tv_tentangLapakFull, tv_tentangLapakBtnLihat,
    //lapak menunggu
    tv_lapakMenunggu;
    ImageView img_lapak, img_lapakAtas, img_editLapak;
    SearchView et_produkSearch;
    ScrollView scroll_lapak;
    LinearLayout line_borderLapak, line_statusLapak;
    LinearLayout line_tambahProduk;
    CardView cr_judulLapak;
    private ArrayList<ProdukModel> produkList;
    private ProdukAdapter produkAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lapak, container, false);

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
        loadLapak();

        line_editLapak = root.findViewById(R.id.line_editLapak);
        tv_tambahProduk = root.findViewById(R.id.tv_tambahProduk);
        tv_lapakMenunggu = root.findViewById(R.id.tv_lapakMenunggu);
        line_tambahProduk = root.findViewById(R.id.line_tambahProduk);

        //initiate lapak
        tv_namaLapak = root.findViewById(R.id.tv_namaLapak);
        tv_alamatLapak = root.findViewById(R.id.tv_alamatLapak);
        tv_telpLapak = root.findViewById(R.id.tv_telpLapak);
        tv_tentangLapak = root.findViewById(R.id.tv_tentangLapak);
        tv_statusLapak = root.findViewById(R.id.tv_statusLapak);
        img_lapak = root.findViewById(R.id.img_lapak);
        line_statusLapak = root.findViewById(R.id.line_statusLapak);

        //initate expand collapse
        tv_alamatLapakFull = root.findViewById(R.id.tv_alamatLapakFull);
        tv_alamatLapakBtnLihat = root.findViewById(R.id.tv_alamatLapakBtnLihat);
        SpannableString content = new SpannableString("Lihat Selengkapnya");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tv_alamatLapakBtnLihat.setText(content);
        tv_alamatLapakBtnLihat.setTextColor(parseColor("#1597E5"));

        tv_tentangLapakFull = root.findViewById(R.id.tv_tentangLapakFull);
        tv_tentangLapakBtnLihat = root.findViewById(R.id.tv_tentangLapakBtnLihat);
        tv_tentangLapakBtnLihat.setText(content);
        tv_tentangLapakBtnLihat.setTextColor(parseColor("#1597E5"));

        //initiate atas
        tv_namaLapakAtas = root.findViewById(R.id.tv_namaLapakAtas);
        img_lapakAtas = root.findViewById(R.id.img_lapakAtas);

        //list produk inisiasi
        produkList = new ArrayList<>();
        recyclerView = root.findViewById(R.id.rv_listProduk);
        LinearLayoutManager linearLayoutManageraaa = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2) {
//            @Override
//            public boolean canScrollVertically() {
//                return false;
//            }
//        };
        recyclerView.setLayoutManager(linearLayoutManageraaa);
        recyclerView.setHasFixedSize(true);


        //hide show judul
        scroll_lapak = root.findViewById(R.id.scroll_lapak);
        line_borderLapak = root.findViewById(R.id.line_borderLapak);
        cr_judulLapak = root.findViewById(R.id.cr_judulLapak);

        //search data produk
        et_produkSearch = root.findViewById(R.id.et_produkSearch);

        img_editLapak = root.findViewById(R.id.img_editLapak);
        img_editLapak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent keEditLapak = new Intent(getActivity(), EditLapakActivity.class);
                keEditLapak.putExtra("id_lapak", id_lapak);
                keEditLapak.putExtra("nama_lapak", nama_lapak);
                keEditLapak.putExtra("alamat_lapak", alamat_lapak);
                keEditLapak.putExtra("tentang_lapak", tentang_lapak);
                keEditLapak.putExtra("telp_lapak", telp_lapak);
                keEditLapak.putExtra("logo_lapak", logo_lapak);
                keEditLapak.putExtra("status_lapak", status_lapak);
                startActivity(keEditLapak);
            }
        });

        line_tambahProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent keTambahProduk = new Intent(getActivity(), TambahProdukActivity.class);
                //kirim data ke activity lain
                keTambahProduk.putExtra("id_lapak", id_lapak);
                startActivity(keTambahProduk);

            }
        });

        tv_sortProdukAlfabet = root.findViewById(R.id.tv_sortProdukAlfabet);
        tv_sortProdukHarga = root.findViewById(R.id.tv_sortProdukHarga);
        tv_sortProdukPopuler = root.findViewById(R.id.tv_sortProdukPopuler);
        tv_sortProdukWaktu = root.findViewById(R.id.tv_sortProdukWaktu);

        tv_sortProdukAlfabet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_sortProdukAlfabet.setBackgroundResource(R.drawable.border_biru_muda);
                tv_sortProdukHarga.setBackgroundResource(R.drawable.border_putih_biru_muda);
                tv_sortProdukPopuler.setBackgroundResource(R.drawable.border_putih_biru_muda);
                tv_sortProdukWaktu.setBackgroundResource(R.drawable.border_putih_biru_muda);
                tv_sortProdukAlfabet.setTextColor(getResources().getColor(R.color.white));
                tv_sortProdukHarga.setTextColor(getResources().getColor(R.color.biru2));
                tv_sortProdukPopuler.setTextColor(getResources().getColor(R.color.biru2));
                tv_sortProdukWaktu.setTextColor(getResources().getColor(R.color.biru2));
                sortAlfabet();
            }
        });

        tv_sortProdukHarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_sortProdukHarga.setBackgroundResource(R.drawable.border_biru_muda);
                tv_sortProdukAlfabet.setBackgroundResource(R.drawable.border_putih_biru_muda);
                tv_sortProdukPopuler.setBackgroundResource(R.drawable.border_putih_biru_muda);
                tv_sortProdukWaktu.setBackgroundResource(R.drawable.border_putih_biru_muda);
                tv_sortProdukHarga.setTextColor(getResources().getColor(R.color.white));
                tv_sortProdukAlfabet.setTextColor(getResources().getColor(R.color.biru2));
                tv_sortProdukPopuler.setTextColor(getResources().getColor(R.color.biru2));
                tv_sortProdukWaktu.setTextColor(getResources().getColor(R.color.biru2));
                sortHarga();
            }
        });

        tv_sortProdukPopuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_sortProdukPopuler.setBackgroundResource(R.drawable.border_biru_muda);
                tv_sortProdukHarga.setBackgroundResource(R.drawable.border_putih_biru_muda);
                tv_sortProdukAlfabet.setBackgroundResource(R.drawable.border_putih_biru_muda);
                tv_sortProdukWaktu.setBackgroundResource(R.drawable.border_putih_biru_muda);
                tv_sortProdukPopuler.setTextColor(getResources().getColor(R.color.white));
                tv_sortProdukHarga.setTextColor(getResources().getColor(R.color.biru2));
                tv_sortProdukAlfabet.setTextColor(getResources().getColor(R.color.biru2));
                tv_sortProdukWaktu.setTextColor(getResources().getColor(R.color.biru2));
                sortAngka();
            }
        });

        tv_sortProdukWaktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_sortProdukWaktu.setBackgroundResource(R.drawable.border_biru_muda);
                tv_sortProdukPopuler.setBackgroundResource(R.drawable.border_putih_biru_muda);
                tv_sortProdukHarga.setBackgroundResource(R.drawable.border_putih_biru_muda);
                tv_sortProdukAlfabet.setBackgroundResource(R.drawable.border_putih_biru_muda);
                tv_sortProdukWaktu.setTextColor(getResources().getColor(R.color.white));
                tv_sortProdukPopuler.setTextColor(getResources().getColor(R.color.biru2));
                tv_sortProdukHarga.setTextColor(getResources().getColor(R.color.biru2));
                tv_sortProdukAlfabet.setTextColor(getResources().getColor(R.color.biru2));
                loadProduct();
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLapak();
        loadProduct();

        et_produkSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String nextText) {
                //Data akan berubah saat user menginputkan text/kata kunci pada SearchView
                if (produkList.size() > 0) {
                    nextText = nextText.toLowerCase();
                    ArrayList<ProdukModel> dataFilter = new ArrayList<>();
                    for (ProdukModel data : produkList) {
                        String nama = data.getNama().toLowerCase();
                        if (nama.contains(nextText)) {
                            dataFilter.add(data);
                        }
                    }
                    produkAdapter.setFilter(dataFilter);
                }
                return true;
            }
        });
    }


    private void loadLapak() {
        String URL_READ = link + "lapakuser/" + id_user + "?token=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //data lapak
                            String res_id = jsonObject.getString("id").trim();
                            String res_userID = jsonObject.getString("user_id").trim();
                            String res_namaLapak = jsonObject.getString("nama_lapak").trim();
                            String res_alamat = jsonObject.getString("alamat").trim();
                            String res_tentang = jsonObject.getString("tentang").trim();
                            String res_telp = jsonObject.getString("telp").trim();
                            String res_logo = jsonObject.getString("logo").trim();
                            String res_statusLapak = jsonObject.getString("status_lapak").trim();
                            String res_createdAt = jsonObject.getString("created_at").trim();
                            String res_updatedAt = jsonObject.getString("updated_at").trim();

                            TextFuntion textFuntion = new TextFuntion();
                            //data diri
                            textFuntion.setTextDanNullData(tv_namaLapak, res_namaLapak);
                            textFuntion.setTextDanNullData(tv_telpLapak, res_telp);
                            textFuntion.setTextDanNullData(tv_alamatLapak, res_alamat);
                            textFuntion.setTextDanNullData(tv_alamatLapakFull, res_alamat);
                            textFuntion.setTextDanNullData(tv_tentangLapak, res_tentang);
                            textFuntion.setTextDanNullData(tv_tentangLapakFull, res_tentang);
                            textFuntion.setTextDanNullData(tv_namaLapakAtas, res_namaLapak);

                            String resi_gambar = res_logo.replace(" ", "%20");

                            id_lapak = res_id;
                            nama_lapak = res_namaLapak;
                            alamat_lapak = res_alamat;
                            tentang_lapak = res_tentang;
                            telp_lapak = res_telp;
                            logo_lapak = resi_gambar;
                            status_lapak = res_statusLapak;

                            String imageUrl = linkGambar + "penduduk/lapak/" + resi_gambar;
                            Picasso.with(getActivity()).load(imageUrl).fit().centerCrop().into(img_lapak);
                            Picasso.with(getActivity()).load(imageUrl).fit().centerCrop().into(img_lapakAtas);

                            if (status_lapak.equals("menunggu")) {
                                line_tambahProduk.setVisibility(View.GONE);
                                tv_lapakMenunggu.setVisibility(View.VISIBLE);
                                textFuntion.setTextDanNullData(tv_statusLapak, "Menunggu Konfirmasi");
                                line_statusLapak.setBackgroundColor(getResources().getColor(R.color.abu));
                            } else {
                                textFuntion.setTextDanNullData(tv_statusLapak, "Lapak Sudah Dikonfirmasi");
                                line_tambahProduk.setVisibility(View.VISIBLE);
                                tv_lapakMenunggu.setVisibility(View.GONE);
                            }

                            if (alamat_lapak.length() > 35) {
                                tv_alamatLapakBtnLihat.setVisibility(View.VISIBLE);

                                tv_alamatLapakBtnLihat.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (tv_alamatLapak.isShown()) {
                                            tv_alamatLapak.setVisibility(View.GONE);
                                            tv_alamatLapakFull.setVisibility(View.VISIBLE);
                                            SpannableString content = new SpannableString("Lihat Lebih Sedikit");
                                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                                            tv_alamatLapakBtnLihat.setText(content);
                                            tv_alamatLapakBtnLihat.setTextColor(parseColor("#1597E5"));
//                                        tv_alamatLapakBtnLihat.setText("Lihat Lebih Sedikit");
                                        } else {
                                            tv_alamatLapak.setVisibility(View.VISIBLE);
                                            tv_alamatLapakFull.setVisibility(View.GONE);
                                            SpannableString content = new SpannableString("Lihat Selengkapnya");
                                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                                            tv_alamatLapakBtnLihat.setText(content);
                                            tv_alamatLapakBtnLihat.setTextColor(parseColor("#1597E5"));
//                                        tv_alamatLapakBtnLihat.setText("Lihat Selengkapnya");
                                        }
                                    }
                                });
                            }
                            if (tentang_lapak.length() > 35) {
                                tv_tentangLapakBtnLihat.setVisibility(View.VISIBLE);

                                tv_tentangLapakBtnLihat.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (tv_tentangLapak.isShown()) {
                                            tv_tentangLapak.setVisibility(View.GONE);
                                            tv_tentangLapakFull.setVisibility(View.VISIBLE);
                                            SpannableString content = new SpannableString("Lihat Lebih Sedikit");
                                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                                            tv_tentangLapakBtnLihat.setText(content);
                                            tv_tentangLapakBtnLihat.setTextColor(parseColor("#1597E5"));
//                                        tv_alamatLapakBtnLihat.setText("Lihat Lebih Sedikit");
                                        } else {
                                            tv_tentangLapak.setVisibility(View.VISIBLE);
                                            tv_tentangLapakFull.setVisibility(View.GONE);
                                            SpannableString content = new SpannableString("Lihat Selengkapnya");
                                            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                                            tv_tentangLapakBtnLihat.setText(content);
                                            tv_tentangLapakBtnLihat.setTextColor(parseColor("#1597E5"));
//                                        tv_alamatLapakBtnLihat.setText("Lihat Selengkapnya");
                                        }
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(getActivity(), "Data Lapak Tidak Ada! Silahkan Daftarkan Lapak Anda!", Toast.LENGTH_LONG).show();
                            Intent keTambahLapak = new Intent(getActivity(), TambahLapakActivity.class);
                            keTambahLapak.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(keTambahLapak);
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
                    final long cacheHitButRefreshed = 5 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
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
                        Log.d("bangsat cache", "parseNetworkResponse: 1");
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        Log.d("bangsat cache", "parseNetworkResponse: 2");
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    Log.d("bangsat cache", "parseNetworkResponse: 3");
                    return Response.success(jsonString, cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    Log.d("bangsat cache", "parseNetworkResponse: 4");
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

    private void loadProduct() {
        String URL_READ = link + "produklapak/" + id_user + "?token=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (produkList.size() > 0) {
                            produkList.clear();
                        }
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    //data produk
                                    String res_id = jsonObject.getString("id").trim();
                                    String res_lapakID = jsonObject.getString("lapak_id").trim();
                                    String res_nama = jsonObject.getString("nama").trim();
                                    String res_keterangan = jsonObject.getString("keterangan").trim();
                                    String res_gambar = jsonObject.getString("gambar").trim();
                                    Integer res_harga = Integer.parseInt(jsonObject.getString("harga"));
                                    Integer res_dilihat = Integer.parseInt(jsonObject.getString("dilihat"));
                                    //String res_createdAt = jsonObject.getString("created_at").trim();
                                    //String res_updatedAt = jsonObject.getString("updated_at").trim();

                                    String resi_gambar = res_gambar.replace(" ", "%20");
                                    Log.d("calpalnx", resi_gambar);

                                    produkList.add(new ProdukModel(res_id, res_lapakID, res_nama, res_keterangan, resi_gambar, res_harga, res_dilihat));
                                    produkAdapter = new ProdukAdapter(getContext(), produkList);
                                    recyclerView.setAdapter(produkAdapter);
                                    produkAdapter.setOnItemClickCallback(new ProdukAdapter.OnItemClickCallback() {
                                        @Override
                                        public void onItemClicked(ProdukModel data) {
                                            Intent transferDataProduk = new Intent(getActivity(), EditProdukActivity.class);
                                            transferDataProduk.putExtra(EditProdukActivity.DATA_PRODUK, data);
                                            startActivity(transferDataProduk);
//                                            dialogDelete(data.getId());
                                        }
                                    });
                                    produkAdapter.setOnDeleteClick(new ProdukAdapter.OnDeleteClick() {
                                        @Override
                                        public void onItemClicked(ProdukModel data) {
                                            dialogDelete(data.getId());
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
//                            Toast.makeText(getActivity(), "Data Produk Tidak Ada! Silahkan Tambahkan Produk Anda!", Toast.LENGTH_LONG).show();
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

    private void dialogDelete(String id_produk) {
        AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(getActivity());
        alertdialogBuilder.setTitle("Konfismasi Delete");
        alertdialogBuilder.setMessage("Apakah Anda Yakin Menghapus Data Ini?");
        alertdialogBuilder.setCancelable(false);
        alertdialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                hapusData(id_produk);
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

    private void hapusData(String id) {
        loadingDialog.startLoading();
        Log.d("calpalnx", String.valueOf(id));
        String URL_DELETEPRODUK = link + "produk/" + id + "?token=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, URL_DELETEPRODUK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Toast.makeText(getActivity(), "Hapus Produk Sukses", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                                onResume();
                            } else {
                                Toast.makeText(getActivity(), "Hapus Produk Gagal!", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Hapus Produk Gagal! : " + e.toString(), Toast.LENGTH_LONG).show();
                            loadingDialog.dissmissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Hapus Produk Gagal! : Cek Koneksi Anda, " + error, Toast.LENGTH_LONG).show();
                        loadingDialog.dissmissDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;
            }
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    //sort alfabet
    private void sortAlfabet() {
        if (produkList.size() > 0) {
            Collections.sort(produkList, new Comparator<ProdukModel>() {
                @Override
                public int compare(ProdukModel t1, ProdukModel t2) {
                    return t1.getNama().compareToIgnoreCase(t2.getNama());
                }
            });
            produkAdapter.notifyDataSetChanged();
        }
    }

    //sort harga
    private void sortHarga() {
        if (produkList.size() > 0) {
            Collections.sort(produkList, new Comparator<ProdukModel>() {
                public int compare(ProdukModel o1, ProdukModel o2) {
                    return o1.getHarga() - o2.getHarga();
                }
            });
            produkAdapter.notifyDataSetChanged();
        }
    }

    //sort angka
    private void sortAngka() {
        if (produkList.size() > 0) {
            Collections.sort(produkList, new Comparator<ProdukModel>() {
                public int compare(ProdukModel o1, ProdukModel o2) {
                    return o2.getDilihat() - o1.getDilihat();
                }
            });
            produkAdapter.notifyDataSetChanged();
        }

    }

}