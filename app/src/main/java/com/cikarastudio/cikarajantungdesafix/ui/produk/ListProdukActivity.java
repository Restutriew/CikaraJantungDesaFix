package com.cikarastudio.cikarajantungdesafix.ui.produk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.cikarastudio.cikarajantungdesafix.adapter.ProdukSemuaListAdapter;
import com.cikarastudio.cikarajantungdesafix.model.ProdukSemuaModel;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ListProdukActivity extends AppCompatActivity implements View.OnClickListener {

    LoadingDialog loadingDialog;
    ImageView img_back;
    String link, linkGambar, token;
    RecyclerView rv_listProdukAll;
    SearchView et_produkSearchAll;
    TextView tv_sortProdukAlfabetAll, tv_sortProdukHargaAll, tv_sortProdukPopulerAll, tv_sortProdukWaktuAll;
    ProdukSemuaListAdapter produkSemuaListAdapter;
    private ArrayList<ProdukSemuaModel> produkSemuaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_produk);

        link = getString(R.string.link);
        linkGambar = getString(R.string.linkGambar);

        //inisiasi token
        token = getString(R.string.token);

        loadingDialog = new LoadingDialog(ListProdukActivity.this);
        loadingDialog.startLoading();

        produkSemuaList = new ArrayList<>();
        rv_listProdukAll = findViewById(R.id.rv_listProdukAll);
        LinearLayoutManager linearLayoutManageraaa = new LinearLayoutManager(ListProdukActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv_listProdukAll.setLayoutManager(linearLayoutManageraaa);
        rv_listProdukAll.setHasFixedSize(true);

        et_produkSearchAll = findViewById(R.id.et_produkSearchAll);

        tv_sortProdukAlfabetAll = findViewById(R.id.tv_sortProdukAlfabetAll);
        tv_sortProdukAlfabetAll.setOnClickListener(this);

        tv_sortProdukHargaAll = findViewById(R.id.tv_sortProdukHargaAll);
        tv_sortProdukHargaAll.setOnClickListener(this);

        tv_sortProdukPopulerAll = findViewById(R.id.tv_sortProdukPopulerAll);
        tv_sortProdukPopulerAll.setOnClickListener(this);

        tv_sortProdukWaktuAll = findViewById(R.id.tv_sortProdukWaktuAll);
        tv_sortProdukWaktuAll.setOnClickListener(this);

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        loadProdukAll();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                // do your code
                finish();
                break;
            case R.id.tv_sortProdukAlfabetAll:
                // do your code
                sortAlfabet();
                setFilterBG(tv_sortProdukAlfabetAll, tv_sortProdukHargaAll, tv_sortProdukPopulerAll, tv_sortProdukWaktuAll);
                break;
            case R.id.tv_sortProdukHargaAll:
                // do your code
                sortHarga();
                setFilterBG(tv_sortProdukHargaAll, tv_sortProdukAlfabetAll, tv_sortProdukPopulerAll, tv_sortProdukWaktuAll);
                break;
            case R.id.tv_sortProdukPopulerAll:
                // do your code
                sortAngka();
                setFilterBG(tv_sortProdukPopulerAll, tv_sortProdukHargaAll, tv_sortProdukAlfabetAll, tv_sortProdukWaktuAll);
                break;
            case R.id.tv_sortProdukWaktuAll:
                // do your code
                loadProdukAll();
                setFilterBG(tv_sortProdukWaktuAll, tv_sortProdukPopulerAll, tv_sortProdukHargaAll, tv_sortProdukAlfabetAll);
                break;
            default:
                break;
        }
    }

    private void setFilterBG(TextView tv1, TextView tv2, TextView tv3, TextView tv4) {
        tv1.setBackgroundResource(R.drawable.border_biru_muda);
        tv2.setBackgroundResource(R.drawable.border_putih_biru_muda);
        tv3.setBackgroundResource(R.drawable.border_putih_biru_muda);
        tv4.setBackgroundResource(R.drawable.border_putih_biru_muda);
        tv1.setTextColor(getResources().getColor(R.color.white));
        tv2.setTextColor(getResources().getColor(R.color.biru2));
        tv3.setTextColor(getResources().getColor(R.color.biru2));
        tv4.setTextColor(getResources().getColor(R.color.biru2));
    }

    @Override
    protected void onResume() {
        super.onResume();
        et_produkSearchAll.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String nextText) {
                //Data akan berubah saat user menginputkan text/kata kunci pada SearchView
                if (produkSemuaList.size() > 0) {
                    nextText = nextText.toLowerCase();
                    ArrayList<ProdukSemuaModel> dataFilter = new ArrayList<>();
                    for (ProdukSemuaModel data : produkSemuaList) {
                        String nama = data.getNama().toLowerCase();
                        if (nama.contains(nextText)) {
                            dataFilter.add(data);
                        }
                    }
                    produkSemuaListAdapter.setFilter(dataFilter);
                }
                return true;
            }
        });
    }

    private void loadProdukAll() {
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

                                    produkSemuaListAdapter = new ProdukSemuaListAdapter(ListProdukActivity.this, produkSemuaList);
                                    rv_listProdukAll.setAdapter(produkSemuaListAdapter);

                                    produkSemuaListAdapter.setOnItemClickCallback(new ProdukSemuaListAdapter.OnItemClickCallback() {
                                        @Override
                                        public void onItemClicked(ProdukSemuaModel data) {
                                            String linkProdukJadi = "https://puteran.cikarastudio.com/" + data.getLink();
                                            Intent keLinkProduk = new Intent(Intent.ACTION_VIEW, Uri.parse(linkProdukJadi));
                                            startActivity(keLinkProduk);
                                        }
                                    });
                                    loadingDialog.dissmissDialog();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ListProdukActivity.this, "Data Produk Tidak Ada!", Toast.LENGTH_LONG).show();
                            loadingDialog.dissmissDialog();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListProdukActivity.this, "Tidak Ada Koneksi Internet!", Toast.LENGTH_LONG).show();
                loadingDialog.dissmissDialog();
            }
        }) {
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(ListProdukActivity.this);
        requestQueue.add(stringRequest);
    }


    //sort alfabet
    private void sortAlfabet() {
        if (produkSemuaList.size() > 0) {
            Collections.sort(produkSemuaList, new Comparator<ProdukSemuaModel>() {
                @Override
                public int compare(ProdukSemuaModel t1, ProdukSemuaModel t2) {
                    return t1.getNama().compareToIgnoreCase(t2.getNama());
                }
            });
            produkSemuaListAdapter.notifyDataSetChanged();
        }
    }

    //sort harga
    private void sortHarga() {
        if (produkSemuaList.size() > 0) {
            Collections.sort(produkSemuaList, new Comparator<ProdukSemuaModel>() {
                public int compare(ProdukSemuaModel o1, ProdukSemuaModel o2) {
                    return o1.getHarga() - o2.getHarga();
                }
            });
            produkSemuaListAdapter.notifyDataSetChanged();
        }
    }

    //sort angka
    private void sortAngka() {
        if (produkSemuaList.size() > 0) {
            Collections.sort(produkSemuaList, new Comparator<ProdukSemuaModel>() {
                public int compare(ProdukSemuaModel o1, ProdukSemuaModel o2) {
                    return o2.getDilihat() - o1.getDilihat();
                }
            });
            produkSemuaListAdapter.notifyDataSetChanged();
        }

    }
}