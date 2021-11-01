package com.cikarastudio.cikarajantungdesafix.ui.laporan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.adapter.KategoriAdapter;
import com.cikarastudio.cikarajantungdesafix.adapter.LaporanAdapter;
import com.cikarastudio.cikarajantungdesafix.adapter.ProdukAdapter;
import com.cikarastudio.cikarajantungdesafix.model.KategoriModel;
import com.cikarastudio.cikarajantungdesafix.model.LaporanModel;
import com.cikarastudio.cikarajantungdesafix.model.ProdukModel;
import com.cikarastudio.cikarajantungdesafix.session.SessionManager;
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;
import com.cikarastudio.cikarajantungdesafix.ui.lapak.EditProdukActivity;
import com.cikarastudio.cikarajantungdesafix.ui.lapak.TambahLapakActivity;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;
import com.cikarastudio.cikarajantungdesafix.ui.profil.ProfilActivity;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LaporanFragment extends Fragment {

    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    RecyclerView rv_kategori, rv_laporanAll;
    private ArrayList<KategoriModel> kategoriList;
    private KategoriAdapter kategoriAdapter;

    private ArrayList<LaporanModel> laporanList;
    private LaporanAdapter laporanAdapter;

    private  ArrayList<String> cateList;

    CardView cr_keLaporanSaya;

    String id_user, link, linkGambar;
    TextView et_keFormLaporan;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_laporan, container, false);

        sessionManager = new SessionManager(getActivity());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        id_user = user.get(sessionManager.ID);

        HttpsTrustManager.allowAllSSL();

        //inisiasi link
        link = getString(R.string.link);
        linkGambar = getString(R.string.linkGambar);

        loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.startLoading();

        cateList=new ArrayList<>();

        kategoriList = new ArrayList<>();
        rv_kategori = root.findViewById(R.id.rv_kategori);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getContext());
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        rv_kategori.setLayoutManager(layoutManager);
        rv_kategori.setHasFixedSize(true);

        laporanList = new ArrayList<>();
        rv_laporanAll = root.findViewById(R.id.rv_laporanAll);
//        rv_laporanAll.setLayoutManager(new LinearLayoutManager(getContext()));
        LinearLayoutManager lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true); // last argument (true) is flag for reverse layout
        rv_laporanAll.setLayoutManager(lm);
        rv_laporanAll.setHasFixedSize(true);

        loadLaporan();
        loadDataKategori();

        Log.d("calpalnx", String.valueOf(cateList));

        et_keFormLaporan = root.findViewById(R.id.et_keFormLaporan);
        et_keFormLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent keTambahLaporan = new Intent(getActivity(), TambahLaporanActivity.class);
                keTambahLaporan.putExtra("cateList", cateList);
                startActivity(keTambahLaporan);
            }
        });

        cr_keLaporanSaya = root.findViewById(R.id.cr_keLaporanSaya);
        cr_keLaporanSaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keLaporanSaya = new Intent(getActivity(),LaporanUserActivity.class);
                startActivity(keLaporanSaya);
            }
        });

        return root;


    }

    private void loadLaporan() {
        String URL_READ = link + "lapor";
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
                                    String res_updateAtLaporan = jsonObject.getString("updated_at").trim();
                                    String res_potoProfilLaporan = jsonObject.getString("profile_photo_path").trim();
                                    String res_namaPendudukLaporan = jsonObject.getString("nama_penduduk").trim();

                                    String resi_photoLaporan = res_photoLaporan.replace(" ", "%20");
                                    String resi_potoProfilLaporan = res_potoProfilLaporan.replace(" ", "%20");

                                    String jam = res_updateAtLaporan.substring(11, 13);
                                    String menit = res_updateAtLaporan.substring(14, 16);

                                    String tanggal = res_updateAtLaporan.substring(8, 10);
                                    String bulan = res_updateAtLaporan.substring(5, 7);
                                    String tahun = res_updateAtLaporan.substring(0, 4);

                                    String resi_waktu = jam + "." + menit;
                                    String resi_tanggal = tanggal + "-" + bulan + "-" + tahun;

                                    Log.d("calpalnx", String.valueOf(resi_tanggal));
                                    Log.d("calpalnx", String.valueOf(resi_waktu));

                                    if(res_identitasLaporan.equals("ya") && res_postingLaporan.equals("ya")) {
                                        laporanList.add(new LaporanModel(res_id, res_userId, res_isiLaporan, res_kategoriLaporan, res_statusLaporan, res_tanggapanLaporan, res_identitasLaporan, res_postingLaporan,
                                                resi_photoLaporan, resi_waktu, resi_tanggal, resi_potoProfilLaporan, res_namaPendudukLaporan));
                                        laporanAdapter = new LaporanAdapter(getContext(), laporanList);
                                        rv_laporanAll.setAdapter(laporanAdapter);
                                    } else {
//                                        Toast.makeText(getActivity(), "Data Laporan Tidak Ada!", Toast.LENGTH_SHORT).show();
                                    }

                                    //hilangkan loading
                                    loadingDialog.dissmissDialog();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Data Laporan Tidak Ada!", Toast.LENGTH_SHORT).show();
                                loadingDialog.dissmissDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingDialog.dissmissDialog();
                            Toast.makeText(getActivity(), "Data Laporan Tidak Ada!", Toast.LENGTH_LONG).show();
                        }

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dissmissDialog();
                Toast.makeText(getActivity(), "Tidak Ada Koneksi Internet!" + error, Toast.LENGTH_LONG).show();
            }
        }) {
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void loadDataKategori() {
        String URL_READ = link + "kategori/laporan";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    //data kategori

                                    String res_id = jsonObject.getString("id").trim();
                                    String res_namaKategori = jsonObject.getString("nama_kategori").trim();

                                    TextFuntion textFuntion = new TextFuntion();

                                    cateList.add(textFuntion.convertUpperCase(res_namaKategori));

                                    kategoriList.add(new KategoriModel(res_id, res_namaKategori));
                                    kategoriAdapter = new KategoriAdapter(getContext(), kategoriList);
                                    rv_kategori.setAdapter(kategoriAdapter);

                                    kategoriAdapter.setOnItemClickCallback(new KategoriAdapter.OnItemClickCallback() {
                                        @Override
                                        public void onItemClicked(KategoriModel data) {
//                                            Intent transferDataProduk = new Intent(getActivity(), EditProdukActivity.class);
//                                            transferDataProduk.putExtra(EditProdukActivity.DATA_KATEGORI, data);
//                                            startActivity(transferDataProduk);
                                        }
                                    });
                                    //hilangkan loading
                                    loadingDialog.dissmissDialog();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Data Kategori Tidak Ada!", Toast.LENGTH_SHORT).show();
                                loadingDialog.dissmissDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            loadingDialog.dissmissDialog();
                            Toast.makeText(getActivity(), "Data Kategori Tidak Ada!", Toast.LENGTH_LONG).show();
                        }

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dissmissDialog();
                Toast.makeText(getActivity(), "Tidak Ada Koneksi Internet!" + error, Toast.LENGTH_LONG).show();
            }
        }) {
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}