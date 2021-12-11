package com.cikarastudio.cikarajantungdesafix.ui.lapak;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
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
                cekInputanTambahProduk();
            }
        });

        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        et_hargaProduk.addTextChangedListener(new TextWatcher() {
            private String setEditText = et_hargaProduk.getText().toString().trim();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(setEditText)) {
                    et_hargaProduk.removeTextChangedListener(this);
                    String replace = s.toString().replaceAll("[Rp. ]", "");
                    if (!replace.isEmpty()) {
                        setEditText = formatrupiah(Double.parseDouble(replace));
                    } else {
                        setEditText = "";
                    }
                    et_hargaProduk.setText(setEditText);
                    et_hargaProduk.setSelection(setEditText.length());
                    et_hargaProduk.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable view) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

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

    private void cekInputanTambahProduk() {
        if (et_namaProduk.isShown() && et_namaProduk.getText().toString().equals("")) {
            et_namaProduk.setError("Form ini harus diisi!");
            et_namaProduk.requestFocus();
        } else if (et_hargaProduk.isShown() && et_hargaProduk.getText().toString().equals("")) {
            et_hargaProduk.setError("Form ini harus diisi!");
            et_hargaProduk.requestFocus();
        } else if (et_keteranganProduk.isShown() && et_keteranganProduk.getText().toString().equals("")) {
            et_keteranganProduk.setError("Form ini harus diisi!");
            et_keteranganProduk.requestFocus();
        } else {
            addProduk();
        }
    }

    private void addProduk() {
        loadingDialog.startLoading();
        String URL_TAMBAHPRODUK = link + "produk";
        final String namaProduk = et_namaProduk.getText().toString().trim();
        final String hargaProduka = et_hargaProduk.getText().toString().trim();
        final String keteranganProduk = et_keteranganProduk.getText().toString().trim();
        final String lapak_id = id_lapak;
        final String dilihat = "0";
        final String gambar = "data:image/png;base64," + getStringImage(bitmap);

        final String hargaProduk = hargaProduka.replace("Rp. ", "").replace(".", "");

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

    private String formatrupiah(Double number) {
        Locale localeID = new Locale("IND", "ID");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(localeID);
        String formatrupiah = numberFormat.format(number);
        String[] split = formatrupiah.split(",");
        int length = split[0].length();
        return split[0].substring(0, 2) + ". " + split[0].substring(2, length);
    }
}