package com.cikarastudio.cikarajantungdesafix.ui.lapak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import com.cikarastudio.cikarajantungdesafix.session.SessionManager;
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;
import com.cikarastudio.cikarajantungdesafix.ui.profil.ProfilActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TambahLapakActivity extends AppCompatActivity {

    ImageView img_back;
    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    CardView cr_simpanTambahLapak;
    EditText et_namaLapakTambah, et_alamatLapakTambah, et_tentangLapakTambah, et_telpLapakTambah;

    String id_user, token , link;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_lapak);

        sessionManager = new SessionManager(TambahLapakActivity.this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        id_user = user.get(sessionManager.ID);

        loadingDialog = new LoadingDialog(TambahLapakActivity.this);

        HttpsTrustManager.allowAllSSL();

        //inisiasi link
        link = getString(R.string.link);

        token = getString(R.string.token);

        et_namaLapakTambah = findViewById(R.id.et_namaLapakTambah);
        et_alamatLapakTambah = findViewById(R.id.et_alamatLapakTambah);
        et_tentangLapakTambah = findViewById(R.id.et_tentangLapakTambah);
        et_telpLapakTambah = findViewById(R.id.et_telpLapakTambah);

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cr_simpanTambahLapak = findViewById(R.id.cr_simpanTambahLapak);
        cr_simpanTambahLapak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoading();
                tambahLapak();
            }
        });
    }

    private void tambahLapak() {
        String URL_TAMBAHLAPAK = link + "tambahlapak";
        final String namalapak = et_namaLapakTambah.getText().toString().trim();
        final String alamatLapak = et_alamatLapakTambah.getText().toString().trim();
        final String tentangLapak = et_tentangLapakTambah.getText().toString().trim();
        final String telp = et_telpLapakTambah.getText().toString().trim();
        final String gambar = "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";
        final String status_lapak = "menunggu";
        String uploadBase64 = "data:image/png;base64," + gambar;

        Log.d("calpalnx", String.valueOf(id_user));
        Log.d("calpalnx", String.valueOf(namalapak));
        Log.d("calpalnx", String.valueOf(alamatLapak));
        Log.d("calpalnx", String.valueOf(tentangLapak));
        Log.d("calpalnx", String.valueOf(telp));
        Log.d("calpalnx", String.valueOf(gambar));
        Log.d("calpalnx", String.valueOf(token));
        Log.d("calpalnx", String.valueOf(status_lapak));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_TAMBAHLAPAK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(TambahLapakActivity.this, "Daftar Lapak Sukses", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                                finish();
                            } else {
                                Toast.makeText(TambahLapakActivity.this, "Daftar Lapak Gagal!", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TambahLapakActivity.this, "Daftar Lapak Gagal!", Toast.LENGTH_LONG).show();
                            loadingDialog.dissmissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TambahLapakActivity.this, "Daftar Lapak Gagal! : Cek Koneksi Anda" + error.toString(), Toast.LENGTH_LONG).show();
                        loadingDialog.dissmissDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", id_user);
                params.put("nama_lapak", namalapak);
                params.put("tentang", tentangLapak);
                params.put("alamat", alamatLapak);
                params.put("status_lapak", status_lapak);
                params.put("telp", telp);
                params.put("image", uploadBase64);
                params.put("token", token);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}