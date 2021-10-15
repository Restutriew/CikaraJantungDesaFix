package com.cikarastudio.cikarajantungdesafix.ui.lapak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TambahProdukActivity extends AppCompatActivity {

    CardView cr_tambahProduk;
    EditText et_namaProduk,et_hargaProduk,et_keteranganProduk;
    private static String URL_TAMBAHBUMIL = "https://jantungdesa.bunefit.com/api/produk";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_produk);

        cr_tambahProduk = findViewById(R.id.cr_tambahProduk);
        et_namaProduk = findViewById(R.id.et_namaProduk);
        et_hargaProduk = findViewById(R.id.et_hargaProduk);
        et_keteranganProduk = findViewById(R.id.et_keteranganProduk);


        cr_tambahProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduk();
            }
        });


    }

    private void addProduk() {
        HttpsTrustManager.allowAllSSL();
        final String namaProduk = et_namaProduk.getText().toString().trim();
        final String hargaProduk = et_hargaProduk.getText().toString().trim();
        final String keteranganProduk = et_keteranganProduk.getText().toString().trim();
        final String lapak_id = "2";
        final String gambar = "testing.jpg";

//        String tglLahir = et_tambahBumilTglLahir.getText().toString().trim();
//        String tanggal = tglLahir.substring(0, 2);
//        String bulan = tglLahir.substring(3, 5);
//        String tahun = tglLahir.substring(6, 10);
//        final String tglLahirFixBumil = tahun + "-" + bulan + "-" + tanggal;
//        final String goldarBumil = et_golDarah.getSelectedItem().toString();
//        final String agamaBumil = et_agama.getSelectedItem().toString();
//        final String pekerjaanBumil = et_tambahBumilPekerjaan.getText().toString().trim();
//        final String pendidikanBumil = et_pendidikan.getSelectedItem().toString();

//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Calendar cal = Calendar.getInstance();
//        final String tanggalSekarang = dateFormat.format(cal.getTime());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_TAMBAHBUMIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(TambahProdukActivity.this, "Tambah Sukses", Toast.LENGTH_LONG).show();
//                                loadingDialog.dissmissDialog();
//                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TambahProdukActivity.this, "Tambah Gagal Hamil Gagal!", Toast.LENGTH_LONG).show();
//                            loadingDialog.dissmissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TambahProdukActivity.this, "Tambah Data Bumil Gagal! : Cek Koneksi Anda" + error, Toast.LENGTH_LONG).show();
//                        loadingDialog.dissmissDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("lapak_id", lapak_id);
                params.put("nama", namaProduk);
                params.put("keterangan", keteranganProduk);
                params.put("gambar", gambar);
                params.put("harga", hargaProduk);
//                params.put("goldar", goldarBumil);
//                params.put("agama", agamaBumil);
//                params.put("pekerjaan", pekerjaanBumil);
//                params.put("pendidikan", pendidikanBumil);
//                params.put("created_at", tanggalSekarang);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}