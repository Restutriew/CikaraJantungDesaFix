package com.cikarastudio.cikarajantungdesafix.ui.artikel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.cikarastudio.cikarajantungdesafix.adapter.ArtikelAllAdapter;
import com.cikarastudio.cikarajantungdesafix.adapter.KategoriAdapter;
import com.cikarastudio.cikarajantungdesafix.adapter.KategoriArtikelAdapter;
import com.cikarastudio.cikarajantungdesafix.model.ArtikelModel;
import com.cikarastudio.cikarajantungdesafix.model.KategoriArtikelModel;
import com.cikarastudio.cikarajantungdesafix.model.KategoriModel;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListArtikelActivity extends AppCompatActivity implements View.OnClickListener {

    LoadingDialog loadingDialog;
    String link, linkGambar, token;
    RecyclerView rv_artikel, rv_kategoriListArtikel;
    ImageView img_back;
    LinearLayout line_filterKategoriListArtikel;
    private ArrayList<ArtikelModel> artikelList;
    private ArtikelAllAdapter artikelAllAdapter;
    private ArrayList<KategoriArtikelModel> kategoriArtikelList;
    private KategoriArtikelAdapter kategoriArtikelAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_artikel);

        link = getString(R.string.link);
        linkGambar = getString(R.string.linkGambar);

        //inisiasi token
        token = getString(R.string.token);

        loadingDialog = new LoadingDialog(ListArtikelActivity.this);

        loadingDialog.startLoading();
        loadKategoriArtikel();
        loadArtikel();

        kategoriArtikelList = new ArrayList<>();
        rv_kategoriListArtikel = findViewById(R.id.rv_kategoriListArtikel);
        LinearLayoutManager rvKategoriAdapter = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_kategoriListArtikel.setLayoutManager(rvKategoriAdapter);
        rv_kategoriListArtikel.setHasFixedSize(true);

        artikelList = new ArrayList<>();
        rv_artikel = findViewById(R.id.rv_listArtikelAll);
        LinearLayoutManager linearLayoutManageraaa = new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv_artikel.setLayoutManager(linearLayoutManageraaa);
        rv_artikel.setHasFixedSize(true);

        line_filterKategoriListArtikel = findViewById(R.id.line_filterKategoriListArtikel);
        line_filterKategoriListArtikel.setOnClickListener(this);

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                // do your code
                finish();
                break;
            case R.id.line_filterKategoriListArtikel:
                // do your code
                final BottomSheetDialog dialog = new BottomSheetDialog(ListArtikelActivity.this);
                dialog.setContentView(R.layout.layout_bottom_sheet);
                dialog.setCanceledOnTouchOutside(false);
                RecyclerView rv_kategoriFilterPopUp = dialog.findViewById(R.id.rv_kategoriFilterPopUp);
                FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
                layoutManager.setFlexWrap(FlexWrap.WRAP);
                rv_kategoriFilterPopUp.setAdapter(kategoriArtikelAdapter);
                rv_kategoriFilterPopUp.setLayoutManager(layoutManager);
                rv_kategoriFilterPopUp.setHasFixedSize(true);
                kategoriArtikelAdapter.setOnItemClickCallback(new KategoriArtikelAdapter.OnItemClickCallback() {
                    @Override
                    public void onItemClicked(KategoriArtikelModel data) {
                        Log.d("calpalnx", "onItemClicked: " + data.getNama_kategori());
                        if (data.getNama_kategori().equals("semua")) {
                            loadArtikel();
                        } else {
                            filterDashboard(data.getNama_kategori());
                        }
                    }
                });

                CardView cr_okFilterPopUp = dialog.findViewById(R.id.cr_okFilterPopUp);
                cr_okFilterPopUp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            default:
                break;
        }
    }

    private void filterDashboard(String bahan) {
        if (artikelList.size() > 0) {
            bahan = bahan.toLowerCase();
            ArrayList<ArtikelModel> dataFilter = new ArrayList<>();
            for (ArtikelModel data : artikelList) {
                String namaKategori = data.getNama_kategori().toLowerCase();
                if (namaKategori.contains(bahan)) {
                    dataFilter.add(data);
                }
            }
            artikelAllAdapter.setFilter(dataFilter);
        }
    }

    private void loadKategoriArtikel() {
        String URL_READ = link + "list/kategoriartikel?token=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    //data kategori
                                    String res_id = jsonObject.getString("id").trim();
                                    String res_namaKategori = jsonObject.getString("nama_kategori").trim();
//                                    String res_keteranganKategori = jsonObject.getString("keterangan").trim();
                                    String res_keteranganKategori = "keterangan";
//                                    String res_createdAt = jsonObject.getString("created_at").trim();
                                    String res_createdAt = "created_at";
                                    String res_updatedAt = "updated_at";

                                    kategoriArtikelList.add(new KategoriArtikelModel(res_id, res_namaKategori, res_keteranganKategori, res_createdAt, res_updatedAt));
                                    kategoriArtikelAdapter = new KategoriArtikelAdapter(getApplicationContext(), kategoriArtikelList);
                                    rv_kategoriListArtikel.setAdapter(kategoriArtikelAdapter);

                                    kategoriArtikelAdapter.setOnItemClickCallback(new KategoriArtikelAdapter.OnItemClickCallback() {
                                        @Override
                                        public void onItemClicked(KategoriArtikelModel data) {
                                            Log.d("calpalnx", "onItemClicked: " + data.getNama_kategori());
                                            if (data.getNama_kategori().equals("semua")) {
                                                loadArtikel();
                                            } else {
                                                filterDashboard(data.getNama_kategori());
                                            }
                                        }
                                    });
                                    //hilangkan loading
                                    loadingDialog.dissmissDialog();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Data Kategori Tidak Ada!", Toast.LENGTH_SHORT).show();
                                loadingDialog.dissmissDialog();
                            }
                            Log.d("calpalnx", "onResponse: fucker kategori ");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingDialog.dissmissDialog();
                            Toast.makeText(getApplicationContext(), "Data Kategori Tidak Ada!" + e.toString(), Toast.LENGTH_LONG).show();
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
                                    artikelAllAdapter = new ArtikelAllAdapter(getApplicationContext(), artikelList);
                                    rv_artikel.setAdapter(artikelAllAdapter);
                                    artikelAllAdapter.setOnItemClickCallback(new ArtikelAllAdapter.OnItemClickCallback() {
                                        @Override
                                        public void onItemClicked(ArtikelModel data) {
                                            Intent transferArtikel = new Intent(getApplicationContext(), DetailArtikelActivity.class);
                                            transferArtikel.putExtra(DetailArtikelActivity.ARTIKEL_DATA, data);
                                            startActivity(transferArtikel);
                                        }
                                    });

                                    //hilangkan loading
                                    loadingDialog.dissmissDialog();
                                }
                                Log.d("calpalnx", "onResponse: fucker ");
                            } else {
                                Toast.makeText(getApplicationContext(), "Data Berita Tidak Ada!", Toast.LENGTH_SHORT).show();
                                loadingDialog.dissmissDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingDialog.dissmissDialog();
                            Toast.makeText(getApplicationContext(), "Data Berita Tidak Ada!", Toast.LENGTH_LONG).show();
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


}