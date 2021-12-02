package com.cikarastudio.cikarajantungdesafix.ui.lapak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TambahProdukActivity extends AppCompatActivity {

    ImageView img_back, img_tambahProduk, img_tambahPhotoProduk;
    LoadingDialog loadingDialog;
    CardView cr_tambahProduk;
    EditText et_namaProduk, et_hargaProduk, et_keteranganProduk;
    String id_lapak, token, link;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_produk);

        cr_tambahProduk = findViewById(R.id.cr_tambahProduk);
        et_namaProduk = findViewById(R.id.et_namaProduk);
        et_hargaProduk = findViewById(R.id.et_hargaProduk);
        et_keteranganProduk = findViewById(R.id.et_keteranganProduk);
        img_tambahProduk = findViewById(R.id.img_tambahProduk);

        //allow ssl
        HttpsTrustManager.allowAllSSL();

        //inisiasi link
        link = getString(R.string.link);

        loadingDialog = new LoadingDialog(TambahProdukActivity.this);

        Intent intent = getIntent();
        id_lapak = intent.getStringExtra("id_lapak");

        token = getString(R.string.token);

        img_tambahPhotoProduk = findViewById(R.id.img_tambahPhotoProduk);
        img_tambahPhotoProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        cr_tambahProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoading();
                addProduk();
            }
        });

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                img_tambahProduk.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bitmap) {

        if (bitmap == null) {
            bitmap = ((BitmapDrawable) img_tambahProduk.getDrawable()).getBitmap();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }

    private void addProduk() {
        String URL_TAMBAHPRODUK = link + "produk";
        final String namaProduk = et_namaProduk.getText().toString().trim();
        final String hargaProduk = et_hargaProduk.getText().toString().trim();
        final String keteranganProduk = et_keteranganProduk.getText().toString().trim();
        final String lapak_id = id_lapak;
        final String dilihat = "0";
        final String gambar = "data:image/png;base64," + getStringImage(bitmap);

        Log.d("calpalnx", String.valueOf(namaProduk));
        Log.d("calpalnx", String.valueOf(hargaProduk));
        Log.d("calpalnx", String.valueOf(keteranganProduk));
        Log.d("calpalnx", String.valueOf(lapak_id));
        Log.d("calpalnx", String.valueOf(gambar));
        Log.d("calpalnx", String.valueOf(token));
        Log.d("calpalnx", String.valueOf(dilihat));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_TAMBAHPRODUK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(TambahProdukActivity.this, "Tambah Produk Sukses", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                                finish();
                            } else {
                                Toast.makeText(TambahProdukActivity.this, "Tambah Produk Gagal!", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TambahProdukActivity.this, "Tambah Produk Gagal!" + e.toString(), Toast.LENGTH_LONG).show();
                            loadingDialog.dissmissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TambahProdukActivity.this, "Tambah Produk Gagal! : Cek Koneksi Anda" + error, Toast.LENGTH_LONG).show();
                        loadingDialog.dissmissDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("lapak_id", lapak_id);
                params.put("nama", namaProduk);
                params.put("keterangan", keteranganProduk);
                params.put("image", gambar);
                params.put("harga", hargaProduk);
                params.put("token", token);
                params.put("dilihat", dilihat);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}