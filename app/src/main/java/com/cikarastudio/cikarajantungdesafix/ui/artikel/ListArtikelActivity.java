package com.cikarastudio.cikarajantungdesafix.ui.artikel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.cikarastudio.cikarajantungdesafix.adapter.ArtikelAllAdapter;
import com.cikarastudio.cikarajantungdesafix.model.ArtikelModel;
import com.cikarastudio.cikarajantungdesafix.ui.laporan.LaporanUserActivity;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;
import com.cikarastudio.cikarajantungdesafix.ui.profil.ProfilActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListArtikelActivity extends AppCompatActivity implements View.OnClickListener {

    LoadingDialog loadingDialog;
    private ArrayList<ArtikelModel> artikelList;
    private ArtikelAllAdapter artikelAllAdapter;
    String link, linkGambar, token;
    RecyclerView rv_artikel;
    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_artikel);

        link = getString(R.string.link);
        linkGambar = getString(R.string.linkGambar);

        //inisiasi token
        token = getString(R.string.token);

        loadingDialog = new LoadingDialog(ListArtikelActivity.this);

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

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingDialog.startLoading();
        loadArtikel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                // do your code
                finish();
                break;
            default:
                break;
        }
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

                                    artikelList.add(new ArtikelModel(res_id, res_userId, res_kategoriArtikelId, res_judulArtikel, res_slug, res_isiArtikel, res_view, resi_gambarArtikel, res_createdAt,
                                            res_updatedAt));
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