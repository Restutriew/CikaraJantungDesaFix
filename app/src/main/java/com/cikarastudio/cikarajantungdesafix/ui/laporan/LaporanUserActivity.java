package com.cikarastudio.cikarajantungdesafix.ui.laporan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.cikarastudio.cikarajantungdesafix.adapter.LaporanUserAdapter;
import com.cikarastudio.cikarajantungdesafix.model.LaporanModel;
import com.cikarastudio.cikarajantungdesafix.model.LaporanUserModel;
import com.cikarastudio.cikarajantungdesafix.model.ProdukModel;
import com.cikarastudio.cikarajantungdesafix.session.SessionManager;
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;
import com.cikarastudio.cikarajantungdesafix.ui.profil.ProfilActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LaporanUserActivity extends AppCompatActivity {

    ImageView img_back;
    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    String id_user, link;
    TextView tv_totalDashboard, tv_selesaiDashboard, tv_diprosesDashboard, tv_menungguDashboard;
    RecyclerView rv_laporanUser;
    private ArrayList<LaporanUserModel> laporanUserList;
    private LaporanUserAdapter laporanUserAdapter;
    CardView cr_dashboradTotalLaporan, cr_dashboardLaporanSelesai,
            cr_dashboardLaporanDiproses, cr_dashboardLaporanMenunggu;
    SearchView et_laporanUserSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_user);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        id_user = user.get(sessionManager.ID);

        HttpsTrustManager.allowAllSSL();

        //inisiasi link
        link = getString(R.string.link);

        tv_totalDashboard = findViewById(R.id.tv_totalDashboard);
        tv_selesaiDashboard = findViewById(R.id.tv_selesaiDashboard);
        tv_diprosesDashboard = findViewById(R.id.tv_diprosesDashboard);
        tv_menungguDashboard = findViewById(R.id.tv_menungguDashboard);
        et_laporanUserSearch = findViewById(R.id.et_laporanUserSearch);

        loadingDialog = new LoadingDialog(this);

        laporanUserList = new ArrayList<>();
        rv_laporanUser = findViewById(R.id.rv_laporanUser);
        LinearLayoutManager linearLayoutManageraaa = new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv_laporanUser.setLayoutManager(linearLayoutManageraaa);
        rv_laporanUser.setHasFixedSize(true);

        cr_dashboradTotalLaporan = findViewById(R.id.cr_dashboradTotalLaporan);
        cr_dashboradTotalLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                laporanUserAdapter = new LaporanUserAdapter(getApplicationContext(), laporanUserList);
                rv_laporanUser.setAdapter(laporanUserAdapter);
            }
        });

        cr_dashboardLaporanSelesai = findViewById(R.id.cr_dashboardLaporanSelesai);
        cr_dashboardLaporanSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDashboard("selesai");
            }
        });

        cr_dashboardLaporanDiproses = findViewById(R.id.cr_dashboardLaporanDiproses);
        cr_dashboardLaporanDiproses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDashboard("proses");
            }
        });

        cr_dashboardLaporanMenunggu = findViewById(R.id.cr_dashboardLaporanMenunggu);
        cr_dashboardLaporanMenunggu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDashboard("menunggu");
            }
        });

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingDialog.startLoading();
        loadLaporanUser();
        loadDataDashboardLaporanUser();

        et_laporanUserSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String nextText) {
                //Data akan berubah saat user menginputkan text/kata kunci pada SearchView
                nextText = nextText.toLowerCase();
                ArrayList<LaporanUserModel> dataFilter = new ArrayList<>();
                for (LaporanUserModel data : laporanUserList) {
                    String nama = data.getIsi().toLowerCase();
                    if (nama.contains(nextText)) {
                        dataFilter.add(data);
                    }
                }
                laporanUserAdapter.setFilter(dataFilter);
                return true;
            }
        });

    }

    private void filterDashboard(String bahan) {
        bahan = bahan.toLowerCase();
        ArrayList<LaporanUserModel> dataFilter = new ArrayList<>();
        for (LaporanUserModel data : laporanUserList) {
            String status = data.getStatus().toLowerCase();
            if (status.contains(bahan)) {
                dataFilter.add(data);
            }
        }
        laporanUserAdapter.setFilter(dataFilter);
    }

    private void loadLaporanUser() {
        if (laporanUserList.size() > 0) {
            laporanUserList.clear();
        }
        String URL_READ = link + "lapor/user/" + id_user;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    //data laporan
                                    String res_id = jsonObject.getString("id").trim();
                                    String res_userId = jsonObject.getString("user_id").trim();
                                    String res_isiLaporan = jsonObject.getString("isi").trim();
                                    String res_kategoriLaporan = jsonObject.getString("kategori").trim();
                                    String res_statusLaporan = jsonObject.getString("status").trim();
                                    String res_tanggapanLaporan = jsonObject.getString("tanggapan").trim();
                                    String res_identitasLaporan = jsonObject.getString("identitas").trim();
                                    String res_postingLaporan = jsonObject.getString("posting").trim();
                                    String res_photoLaporan = jsonObject.getString("photo").trim();
                                    String res_createdAtLaporan = jsonObject.getString("created_at").trim();
                                    String res_updateAtLaporan = jsonObject.getString("updated_at").trim();

                                    String resi_photoLaporan = res_photoLaporan.replace(" ", "%20");

                                    laporanUserList.add(new LaporanUserModel(res_id, res_userId, res_isiLaporan, res_kategoriLaporan, res_statusLaporan, res_tanggapanLaporan, res_identitasLaporan, res_postingLaporan,
                                            resi_photoLaporan, res_createdAtLaporan, res_updateAtLaporan));
                                    laporanUserAdapter = new LaporanUserAdapter(getApplicationContext(), laporanUserList);
                                    rv_laporanUser.setAdapter(laporanUserAdapter);

                                    //hilangkan loading
                                    loadingDialog.dissmissDialog();
                                }
                            } else {
                                Toast.makeText(LaporanUserActivity.this, "Data Laporan Tidak Ada!", Toast.LENGTH_SHORT).show();
                                loadingDialog.dissmissDialog();
                            }
                        } catch (
                                JSONException e) {
                            e.printStackTrace();
                            loadingDialog.dissmissDialog();
                            Toast.makeText(LaporanUserActivity.this, "Data Laporan Tidak Ada!" + e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dissmissDialog();
                Toast.makeText(LaporanUserActivity.this, "Tidak Ada Koneksi Internet!" + error, Toast.LENGTH_LONG).show();
            }
        }) {
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(LaporanUserActivity.this);
        requestQueue.add(stringRequest);
    }

    private void loadDataDashboardLaporanUser() {
        String URL_READ = link + "dashboarduser/lapor/" + id_user;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            //data dashboard laporan
                            String res_total = jsonObject.getString("total").trim();
                            String res_selesai = jsonObject.getString("selesai").trim();
                            String res_proses = jsonObject.getString("proses").trim();
                            String res_menunggu = jsonObject.getString("menunggu").trim();

                            TextFuntion textFuntion = new TextFuntion();
                            //data diri
                            textFuntion.setTextDanNullData(tv_totalDashboard, res_total);

                            textFuntion.setTextDanNullData(tv_selesaiDashboard, res_selesai);
                            textFuntion.setTextDanNullData(tv_diprosesDashboard, res_proses);
                            textFuntion.setTextDanNullData(tv_menungguDashboard, res_menunggu);

                            //hilangkan loading
                            loadingDialog.dissmissDialog();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingDialog.dissmissDialog();
                            Toast.makeText(LaporanUserActivity.this, "Data Dashboard Tidak Ada!" + e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dissmissDialog();
                Toast.makeText(LaporanUserActivity.this, "Tidak Ada Koneksi Internet!" + error, Toast.LENGTH_LONG).show();
            }
        }) {
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}