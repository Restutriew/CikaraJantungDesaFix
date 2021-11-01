package com.cikarastudio.cikarajantungdesafix.ui.laporan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.session.SessionManager;
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;
import com.cikarastudio.cikarajantungdesafix.ui.lapak.TambahLapakActivity;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TambahLaporanActivity extends AppCompatActivity {

    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    String id_user, link, linkGambar, token;
    Spinner sp_jenisLaporan, sp_identitasLaporan, sp_postingLaporan;
    EditText et_isilaporan;
    ImageView img_back;
    CardView cr_tambahLaporan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_laporan);

        sp_jenisLaporan = findViewById(R.id.sp_jenisLaporan);
        sp_identitasLaporan = findViewById(R.id.sp_identitasLaporan);
        sp_postingLaporan = findViewById(R.id.sp_postingLaporan);
        et_isilaporan= findViewById(R.id.et_isilaporan);
        cr_tambahLaporan = findViewById(R.id.cr_tambahLaporan);

        sessionManager = new SessionManager(TambahLaporanActivity.this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        id_user = user.get(sessionManager.ID);

        HttpsTrustManager.allowAllSSL();

        //inisiasi link
        link = getString(R.string.link);
        linkGambar = getString(R.string.linkGambar);
        token = getString(R.string.token);


        loadingDialog = new LoadingDialog(TambahLaporanActivity.this);

        ArrayList<String> kategoriList = (ArrayList<String>) getIntent().getSerializableExtra("cateList");

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ArrayAdapter<String> jenisLaporanAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, kategoriList);
        sp_jenisLaporan.setAdapter(jenisLaporanAdapter);

        ArrayAdapter<String> yaTidakAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.ya_tidak));
        sp_identitasLaporan.setAdapter(yaTidakAdapter);
        sp_postingLaporan.setAdapter(yaTidakAdapter);

        cr_tambahLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahDataLaporan();
            }
        });

    }

    private void tambahDataLaporan() {
        loadingDialog.startLoading();
        String URL_TAMBAH = link + "lapor";
        final String isiLaporan = et_isilaporan.getText().toString().trim();
        final String jenisLaporan = sp_jenisLaporan.getSelectedItem().toString().toLowerCase();
        final String statusLaporan = "menunggu";
        final String photoLaporan = "kimalaporan.jpg";
        final String identitasLaporan = sp_identitasLaporan.getSelectedItem().toString().toLowerCase();
        final String postingLaporan = sp_postingLaporan.getSelectedItem().toString().toLowerCase();

        Log.d("calpalnx", String.valueOf(id_user));
        Log.d("calpalnx", String.valueOf(isiLaporan));
        Log.d("calpalnx", String.valueOf(jenisLaporan));
        Log.d("calpalnx", String.valueOf(statusLaporan));
        Log.d("calpalnx", String.valueOf(token));
        Log.d("calpalnx", String.valueOf(photoLaporan));
        Log.d("calpalnx", String.valueOf(identitasLaporan));
        Log.d("calpalnx", String.valueOf(postingLaporan));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_TAMBAH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(TambahLaporanActivity.this, "Pengajuan Laporan Sukses", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                                finish();
                            } else {
                                Toast.makeText(TambahLaporanActivity.this, "Pengajuan Laporan Gagal!", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TambahLaporanActivity.this, "Pengajuan Laporan Gagal!", Toast.LENGTH_LONG).show();
                            loadingDialog.dissmissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TambahLaporanActivity.this, "Pengajuan Laporan Gagal! : Cek Koneksi Anda", Toast.LENGTH_LONG).show();
                        loadingDialog.dissmissDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", id_user);
                params.put("isi", isiLaporan);
                params.put("kategori", jenisLaporan);
                params.put("status", statusLaporan);
                params.put("token", token);
                params.put("photo", photoLaporan);
                params.put("identitas", identitasLaporan);
                params.put("posting", postingLaporan);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}