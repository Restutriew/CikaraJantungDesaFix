package com.cikarastudio.cikarajantungdesafix.ui.surat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
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
import com.cikarastudio.cikarajantungdesafix.adapter.SuratListAdapter;
import com.cikarastudio.cikarajantungdesafix.model.ProdukModel;
import com.cikarastudio.cikarajantungdesafix.model.SuratListModel;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListTambahSuratActivity extends AppCompatActivity implements View.OnClickListener {

    LoadingDialog loadingDialog;
    ImageView img_back;
    String kategori_surat, link;
    private ArrayList<SuratListModel> suratList;
    private SuratListAdapter suratListAdapter;
    RecyclerView rv_suratList;
    SearchView et_suratListSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tambah_surat);

        Intent intent = getIntent();
        kategori_surat = intent.getStringExtra("kategori_surat");

        loadingDialog = new LoadingDialog(ListTambahSuratActivity.this);
        loadingDialog.startLoading();

        //inisiasi link
        link = getString(R.string.link);

        suratList = new ArrayList<>();
        rv_suratList = findViewById(R.id.rv_suratList);
        LinearLayoutManager linearLayoutManageraaa = new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv_suratList.setLayoutManager(linearLayoutManageraaa);
        rv_suratList.setHasFixedSize(true);

        loadDataListSurat();

        et_suratListSearch = findViewById(R.id.et_suratListSearch);
        et_suratListSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String nextText) {
                //Data akan berubah saat user menginputkan text/kata kunci pada SearchView
                nextText = nextText.toLowerCase();
                ArrayList<SuratListModel> dataFilter = new ArrayList<>();
                for (SuratListModel data : suratList) {
                    String nama = data.getNama_surat().toLowerCase();
                    if (nama.contains(nextText)) {
                        dataFilter.add(data);
                    }
                }
                suratListAdapter.setFilter(dataFilter);
                return true;
            }
        });

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                // do your code
                finish();
                break;
//            case R.id.cr_suratKeterangan:
//                // do your code
//                Intent keEditLapak = new Intent(ListKategoriSuratActivity.this, ListTambahSuratActivity.class);
//                keEditLapak.putExtra("kategori_surat", "surat keterangan");
//                break;
//            case R.id.cr_suratPengantar:
//                // do your code
//                finish();
//                break;
//            case R.id.cr_suratRekomendasi:
//                // do your code
//                finish();
//                break;
//            case R.id.cr_suratLainnya:
//                // do your code
//                finish();
//                break;
            default:
                break;
        }

    }

    private void loadDataListSurat() {
        String URL_READ = link + "listformatsurat?kategori=" + kategori_surat;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    //data format surat
                                    String res_id = jsonObject.getString("id").trim();
                                    String res_kode = jsonObject.getString("kode").trim();
                                    String res_klasifikasisuratId = jsonObject.getString("klasifikasisurat_id").trim();
                                    String res_namaSurat = jsonObject.getString("nama_surat").trim();
                                    String res_nilaiMasaBerlaku = jsonObject.getString("nilai_masaberlaku").trim();
                                    String res_statusMasaBerlaku = jsonObject.getString("status_masaberlaku").trim();
                                    String res_layananMandiri = jsonObject.getString("layanan_mandiri").trim();
                                    String res_fileSurat = jsonObject.getString("file_surat").trim();
                                    String res_kategori = jsonObject.getString("kategori").trim();
                                    String res_createdAt = jsonObject.getString("created_at").trim();
                                    String res_updatedAt = jsonObject.getString("updated_at").trim();

                                    suratList.add(new SuratListModel(res_id, res_kode, res_klasifikasisuratId, res_namaSurat,
                                            res_nilaiMasaBerlaku, res_statusMasaBerlaku, res_layananMandiri,res_fileSurat,
                                            res_kategori,res_createdAt,res_updatedAt));
                                    rv_suratList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                    suratListAdapter = new SuratListAdapter(getApplicationContext(), suratList);
                                    rv_suratList.setAdapter(suratListAdapter);
                                    //sort alfabet
                                    suratListAdapter.setOnItemClickCallback(new SuratListAdapter.OnItemClickCallback() {
                                        @Override
                                        public void onItemClicked(SuratListModel data) {
                                            Intent transferDataFormatSurat = new Intent(getApplicationContext(), TambahSuratActivity.class);
                                            transferDataFormatSurat.putExtra(TambahSuratActivity.DATA_FORMAT_SURAT, data);
                                            startActivity(transferDataFormatSurat);
                                        }
                                    });
                                    //hilangkan loading
                                    loadingDialog.dissmissDialog();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Data List Surat Tidak Ada!", Toast.LENGTH_SHORT).show();
                                loadingDialog.dissmissDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingDialog.dissmissDialog();
                            Toast.makeText(getApplicationContext(), "Data List Surat Tidak Ada!", Toast.LENGTH_LONG).show();
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