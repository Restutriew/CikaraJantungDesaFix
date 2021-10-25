package com.cikarastudio.cikarajantungdesafix.ui.lapak;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class LapakFragment extends Fragment {

    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    RecyclerView recyclerView;
    String id_user, id_lapak;
    LinearLayout line_tokojaya;
    TextView tv_tambahProduk, tv_sortProdukAlfabet, tv_sortProdukHarga, tv_sortProdukPopuler,
    //lapak
    tv_namaLapak, tv_alamatLapak, tv_telpLapak, tv_tentangLapak, tv_statusLapak;
    ImageView img_lapak;
    private ArrayList<ProdukModel> produkList;
    private ProdukAdapter produkAdapter;
    SearchView et_produkSearch;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lapak, container, false);

        sessionManager = new SessionManager(getActivity());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        id_user = user.get(sessionManager.ID);

        HttpsTrustManager.allowAllSSL();

        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoading();

        line_tokojaya = root.findViewById(R.id.line_tokojaya);
        tv_tambahProduk = root.findViewById(R.id.tv_tambahProduk);

        //initiate lapak
        tv_namaLapak = root.findViewById(R.id.tv_namaLapak);
        tv_alamatLapak = root.findViewById(R.id.tv_alamatLapak);
        tv_telpLapak = root.findViewById(R.id.tv_telpLapak);
        tv_tentangLapak = root.findViewById(R.id.tv_tentangLapak);
        tv_statusLapak = root.findViewById(R.id.tv_statusLapak);
        img_lapak = root.findViewById(R.id.img_lapak);

        //list produk inisiasi
        produkList = new ArrayList<>();
        recyclerView = root.findViewById(R.id.rv_listProduk);
        recyclerView.setHasFixedSize(true);
        loadLapak();
        loadProduct();


        //search data produk
        et_produkSearch = root.findViewById(R.id.et_produkSearch);
        et_produkSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String nextText) {
                //Data akan berubah saat user menginputkan text/kata kunci pada SearchView
                nextText = nextText.toLowerCase();
                ArrayList<ProdukModel> dataFilter = new ArrayList<>();
                for (ProdukModel data : produkList) {
                    String nama = data.getNama().toLowerCase();
                    if (nama.contains(nextText)) {
                        dataFilter.add(data);
                    }
                }
                produkAdapter.setFilter(dataFilter);
                return true;
            }
        });

        line_tokojaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent keEditLapak = new Intent(getActivity(), TambahLapakActivity.class);
                startActivity(keEditLapak);
            }
        });

        tv_tambahProduk.setOnClickListener(new View.OnClickListener() {
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

        tv_sortProdukAlfabet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_sortProdukAlfabet.setBackgroundResource(R.drawable.border_biru_putih);
                tv_sortProdukHarga.setBackgroundResource(R.drawable.border_putih_biru);
                tv_sortProdukPopuler.setBackgroundResource(R.drawable.border_putih_biru);
                tv_sortProdukAlfabet.setTextColor(getResources().getColor(R.color.white));
                tv_sortProdukHarga.setTextColor(getResources().getColor(R.color.biru_cikara));
                tv_sortProdukPopuler.setTextColor(getResources().getColor(R.color.biru_cikara));
                sortAlfabet();
            }
        });

        tv_sortProdukHarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_sortProdukHarga.setBackgroundResource(R.drawable.border_biru_putih);
                tv_sortProdukAlfabet.setBackgroundResource(R.drawable.border_putih_biru);
                tv_sortProdukPopuler.setBackgroundResource(R.drawable.border_putih_biru);
                tv_sortProdukHarga.setTextColor(getResources().getColor(R.color.white));
                tv_sortProdukAlfabet.setTextColor(getResources().getColor(R.color.biru_cikara));
                tv_sortProdukPopuler.setTextColor(getResources().getColor(R.color.biru_cikara));

                sortHarga();
            }
        });

        tv_sortProdukPopuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_sortProdukPopuler.setBackgroundResource(R.drawable.border_biru_putih);
                tv_sortProdukHarga.setBackgroundResource(R.drawable.border_putih_biru);
                tv_sortProdukAlfabet.setBackgroundResource(R.drawable.border_putih_biru);
                tv_sortProdukPopuler.setTextColor(getResources().getColor(R.color.white));
                tv_sortProdukHarga.setTextColor(getResources().getColor(R.color.biru_cikara));
                tv_sortProdukAlfabet.setTextColor(getResources().getColor(R.color.biru_cikara));
                sortAngka();
            }
        });

        return root;
    }

    private void loadLapak() {
        String URL_READ = "https://jantungdesa.bunefit.com/api/lapakuser/" + id_user;
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

                            id_lapak = res_id;

                            TextFuntion textFuntion = new TextFuntion();
                            //data diri
                            textFuntion.setTextDanNullData(tv_namaLapak, res_namaLapak);
                            textFuntion.setTextDanNullData(tv_alamatLapak, res_alamat);
                            textFuntion.setTextDanNullData(tv_telpLapak, res_telp);
                            textFuntion.setTextDanNullData(tv_tentangLapak, res_tentang);
                            textFuntion.setTextDanNullData(tv_statusLapak, res_statusLapak);

                            String imageUrl = "https://jantungdesa.bunefit.com/public/img/penduduk/lapak/" + res_logo;
                            Picasso.with(getActivity()).load(imageUrl).fit().centerCrop().into(img_lapak);
                            //hilangkan loading
                            loadingDialog.dissmissDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingDialog.dissmissDialog();
                            Toast.makeText(getActivity(), "Data Lapak Tidak Ada! Silahkan Daftarkan Lapak Anda!" , Toast.LENGTH_LONG).show();
                            Intent keTambahLapak = new Intent(getActivity(),TambahLapakActivity.class);
                            startActivity(keTambahLapak);
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

    private void loadProduct() {
        String URL_READ = "https://jantungdesa.bunefit.com/api/produklapak/" + id_user;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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

                                    produkList.add(new ProdukModel(res_id, res_lapakID, res_nama, res_keterangan, res_gambar, res_harga, res_dilihat));
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                    produkAdapter = new ProdukAdapter(getContext(), produkList);
                                    recyclerView.setAdapter(produkAdapter);
                                    //sort alfabet
                                    sortAlfabet();
                                    produkAdapter.setOnItemClickCallback(new ProdukAdapter.OnItemClickCallback() {
                                        @Override
                                        public void onItemClicked(ProdukModel data) {
                                            Intent transferDataProduk = new Intent(getActivity(), EditProdukActivity.class);
                                            transferDataProduk.putExtra(EditProdukActivity.DATA_PRODUK, data);
                                            startActivity(transferDataProduk);
                                        }
                                    });
                                    //hilangkan loading
                                    loadingDialog.dissmissDialog();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Data Produk Tidak Ada!", Toast.LENGTH_SHORT).show();
                                loadingDialog.dissmissDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingDialog.dissmissDialog();
                            Toast.makeText(getActivity(), "Data Produk Tidak Ada! Silahkan Tambahkan Produk Anda!", Toast.LENGTH_LONG).show();
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


    //sort alfabet
    private void sortAlfabet() {
        Collections.sort(produkList, new Comparator<ProdukModel>() {
            @Override
            public int compare(ProdukModel t1, ProdukModel t2) {
                return t1.getNama().compareToIgnoreCase(t2.getNama());
            }
        });
        produkAdapter.notifyDataSetChanged();
    }

    //sort harga
    private void sortHarga() {
        Collections.sort(produkList, new Comparator<ProdukModel>() {
            public int compare(ProdukModel o1, ProdukModel o2) {
                return o1.getHarga() - o2.getHarga();
            }
        });
        produkAdapter.notifyDataSetChanged();
    }

    //sort angka
    private void sortAngka() {
        Collections.sort(produkList, new Comparator<ProdukModel>() {
            public int compare(ProdukModel o1, ProdukModel o2) {
                return o2.getDilihat() - o1.getDilihat();
            }
        });
        produkAdapter.notifyDataSetChanged();
    }

}