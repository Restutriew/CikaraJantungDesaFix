package com.cikarastudio.cikarajantungdesafix.ui.lapak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditLapakActivity extends AppCompatActivity {

    ImageView img_back, img_editLapak;
    LoadingDialog loadingDialog;
    CardView cr_simpanEditLapak;
    EditText et_namaLapakEdit, et_alamatLapakEdit, et_tentangLapakEdit, et_telpLapakEdit;
    String id_lapak, nama_lapak, alamat_lapak, tentang_lapak, telp_lapak, logo_lapak, status_lapak,
            token, link, linkGambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lapak);

        loadingDialog = new LoadingDialog(EditLapakActivity.this);

        HttpsTrustManager.allowAllSSL();

        //inisiasi link
        link = getString(R.string.link);
        linkGambar = getString(R.string.linkGambar);

        token = getString(R.string.token);

        Intent intent = getIntent();
        id_lapak = intent.getStringExtra("id_lapak");
        nama_lapak = intent.getStringExtra("nama_lapak");
        alamat_lapak = intent.getStringExtra("alamat_lapak");
        tentang_lapak = intent.getStringExtra("tentang_lapak");
        telp_lapak = intent.getStringExtra("telp_lapak");
        logo_lapak = intent.getStringExtra("logo_lapak");
        status_lapak = intent.getStringExtra("status_lapak");

        et_namaLapakEdit = findViewById(R.id.et_namaLapakEdit);
        et_alamatLapakEdit = findViewById(R.id.et_alamatLapakEdit);
        et_tentangLapakEdit = findViewById(R.id.et_tentangLapakEdit);
        et_telpLapakEdit = findViewById(R.id.et_telpLapakEdit);
        img_editLapak = findViewById(R.id.img_editLapak);

        et_namaLapakEdit.setText(nama_lapak);
        et_alamatLapakEdit.setText(alamat_lapak);
        et_tentangLapakEdit.setText(tentang_lapak);
        et_telpLapakEdit.setText(telp_lapak);

        String imageUrl = linkGambar + "penduduk/lapak/" + logo_lapak;
        Picasso.with(EditLapakActivity.this).load(imageUrl).fit().centerCrop().into(img_editLapak);

        cr_simpanEditLapak = findViewById(R.id.cr_simpanEditLapak);
        cr_simpanEditLapak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editLapak();
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

    private void editLapak() {
        loadingDialog.startLoading();
        final String inp_lapak_id = id_lapak;
        final String inp_namaLapak = et_namaLapakEdit.getText().toString().trim();
        final String inp_alamatLapak = et_alamatLapakEdit.getText().toString().trim();
        final String inp_tentangLapak = et_tentangLapakEdit.getText().toString().trim();
        final String inp_telpLapak = et_telpLapakEdit.getText().toString().trim();
        final String inp_logoLapak = logo_lapak;
        final String inp_statusLapak = "menunggu";

        Log.d("calpalnx", String.valueOf(inp_lapak_id));
        Log.d("calpalnx", String.valueOf(inp_namaLapak));
        Log.d("calpalnx", String.valueOf(inp_alamatLapak));
        Log.d("calpalnx", String.valueOf(inp_tentangLapak));
        Log.d("calpalnx", String.valueOf(inp_telpLapak));
        Log.d("calpalnx", String.valueOf(inp_logoLapak));
        Log.d("calpalnx", String.valueOf(inp_statusLapak));

        String URL_EDITPRODUK = link + "lapak/" + inp_lapak_id;
        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, URL_EDITPRODUK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(EditLapakActivity.this, "Edit Lapak Sukses", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                                finish();
                            } else {
                                Toast.makeText(EditLapakActivity.this, "Edit Lapak Gagal!", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditLapakActivity.this, "Edit Lapak Gagal! : " + e.toString(), Toast.LENGTH_LONG).show();
                            loadingDialog.dissmissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditLapakActivity.this, "Tambah Lapak Gagal! : Cek Koneksi Anda, " + error, Toast.LENGTH_LONG).show();
                        loadingDialog.dissmissDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama_lapak", inp_namaLapak);
                params.put("tentang", inp_tentangLapak);
                params.put("alamat", inp_alamatLapak);
                params.put("status_lapak", inp_statusLapak);
                params.put("telp", inp_telpLapak);
                params.put("logo", inp_logoLapak);
                params.put("token", token);
                return params;

            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}