package com.cikarastudio.cikarajantungdesafix.ui.lapak;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.Build;
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
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.model.ProdukModel;
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;
import com.cikarastudio.cikarajantungdesafix.template.kima.deletereq.CustomHurlStack;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditProdukActivity extends AppCompatActivity {

    public static final String DATA_PRODUK = "extra_data";
    LoadingDialog loadingDialog;
    String id, id_lapak, nama, keterangan, gambar,
            token, link, linkGambar;
    Integer harga, dilihat;
    EditText et_namaProduk, et_hargaProduk, et_keteranganProduk;
    ImageView img_back, img_editProduk, img_editPhotoProduk;
    ImageView img_noGambar;
    CardView cr_simpanProduk;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_produk);

        //allow ssl
        HttpsTrustManager.allowAllSSL();

        //inisiasi link
        link = getString(R.string.link);
        linkGambar = getString(R.string.linkGambar);

        //inisiasi loadingDialog
        loadingDialog = new LoadingDialog(EditProdukActivity.this);

        //string token
        token = getString(R.string.token);

        //get data produk dari list
        ProdukModel dataProduk = getIntent().getParcelableExtra(DATA_PRODUK);
        id = dataProduk.getId();
        id_lapak = dataProduk.getLapak_id();
        nama = dataProduk.getNama();
        keterangan = dataProduk.getKeterangan();
        gambar = dataProduk.getGambar();
        harga = dataProduk.getHarga();
        dilihat = dataProduk.getDilihat();

        et_namaProduk = findViewById(R.id.et_namaProduk);
        et_hargaProduk = findViewById(R.id.et_hargaProduk);
        et_keteranganProduk = findViewById(R.id.et_keteranganProduk);
        img_editProduk = findViewById(R.id.img_tambahProduk);

        img_noGambar = findViewById(R.id.img_noGambar);
        img_noGambar.setVisibility(View.GONE);

        et_namaProduk.setText(nama);
//        et_hargaProduk.setText(String.valueOf(harga));
        TextFuntion textFuntion = new TextFuntion();
        textFuntion.setRupiah(et_hargaProduk, harga);
        et_keteranganProduk.setText(keterangan);

        String imageUrl = linkGambar + "penduduk/produk/" + gambar;
        Picasso.with(EditProdukActivity.this).load(imageUrl).fit().centerCrop().into(img_editProduk);

        img_editPhotoProduk = findViewById(R.id.img_tambahPhotoProduk);
        img_editPhotoProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        cr_simpanProduk = findViewById(R.id.cr_tambahProduk);
        cr_simpanProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekInputanEditProduk();
            }
        });


        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                img_editProduk.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bitmap) {

        if (bitmap == null) {
            bitmap = ((BitmapDrawable) img_editProduk.getDrawable()).getBitmap();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }

    private void dialogDelete() {
        AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(EditProdukActivity.this);
        alertdialogBuilder.setTitle("Konfismasi Delete");
        alertdialogBuilder.setMessage("Apakah Anda Yakin Menghapus Data Ini?");
        alertdialogBuilder.setCancelable(false);
        alertdialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                loadingDialog.startLoading();
                hapusData();
            }
        });
        alertdialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = alertdialogBuilder.create();
        alertDialog.show();
    }

    private void hapusData() {
        Log.d("calpalnx", String.valueOf(id));
        String URL_DELETEPRODUK = link + "produk/" + id;
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, URL_DELETEPRODUK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Toast.makeText(EditProdukActivity.this, "Hapus Produk Sukses", Toast.LENGTH_LONG).show();
//                                loadingDialog.dissmissDialog();
//                                finish();
                            } else {
                                Toast.makeText(EditProdukActivity.this, "Hapus Produk Gagal!", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditProdukActivity.this, "Hapus Produk Gagal! : " + e.toString(), Toast.LENGTH_LONG).show();
                            loadingDialog.dissmissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProdukActivity.this, "Hapus Produk Gagal! : Cek Koneksi Anda, " + error, Toast.LENGTH_LONG).show();
                        loadingDialog.dissmissDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;
            }
        };

        HttpStack httpStack;
        if (Build.VERSION.SDK_INT > 19) {
            httpStack = new CustomHurlStack();
            //disable okhttphurlstack
//        } else if (Build.VERSION.SDK_INT >= 9 && Build.VERSION.SDK_INT <= 19)
//        {
//            httpStack = new OkHttpHurlStack();
        } else {
            httpStack = new HttpClientStack(AndroidHttpClient.newInstance("Android"));
        }
        RequestQueue requestQueue = Volley.newRequestQueue(EditProdukActivity.this, httpStack);
        requestQueue.add(stringRequest);

    }


    private void cekInputanEditProduk() {
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
            simpanEditProduk();
        }
    }

    private void simpanEditProduk() {
        loadingDialog.startLoading();
        final String inp_lapak_id = id_lapak;
        final String inp_namaProduk = et_namaProduk.getText().toString().trim();
        final String inp_keteranganProduk = et_keteranganProduk.getText().toString().trim();
        final String hargaProduka = et_hargaProduk.getText().toString().trim();
        final String inp_dilihatProduk = String.valueOf(dilihat);

        final String inp_hargaProduk = hargaProduka.replace("Rp. ", "").replace(".", "");

        final String inp_gambarProduk = "data:image/png;base64," + getStringImage(bitmap);

        Log.d("calpalnx", String.valueOf(id));
        Log.d("calpalnx", String.valueOf(inp_lapak_id));
        Log.d("calpalnx", String.valueOf(inp_namaProduk));
        Log.d("calpalnx", String.valueOf(inp_keteranganProduk));
        Log.d("calpalnx", String.valueOf(inp_gambarProduk));
        Log.d("calpalnx", String.valueOf(inp_hargaProduk));
        Log.d("calpalnx", String.valueOf(inp_dilihatProduk));

        String URL_EDITPRODUK = link + "produk/" + id;
        StringRequest stringRequest = new StringRequest(Request.Method.PATCH, URL_EDITPRODUK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Log.d("calpalnx", "onResponse: edit produk sukses ");
                                Toast.makeText(EditProdukActivity.this, "Edit Produk Sukses", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                                finish();
                            } else {
                                Toast.makeText(EditProdukActivity.this, "Edit Produk Gagal!", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditProdukActivity.this, "Edit Produk Gagal! : " + e.toString(), Toast.LENGTH_LONG).show();
                            loadingDialog.dissmissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProdukActivity.this, "Edit Produk Gagal! : Cek Koneksi Anda, " + error, Toast.LENGTH_LONG).show();
                        loadingDialog.dissmissDialog();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", inp_namaProduk);
                params.put("harga", inp_hargaProduk);
                params.put("keterangan", inp_keteranganProduk);
                params.put("image", inp_gambarProduk);
                params.put("token", token);
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
