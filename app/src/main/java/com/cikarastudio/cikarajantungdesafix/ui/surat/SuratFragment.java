package com.cikarastudio.cikarajantungdesafix.ui.surat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.adapter.SuratListUserAdapter;
import com.cikarastudio.cikarajantungdesafix.model.SuratV2Model;
import com.cikarastudio.cikarajantungdesafix.session.SessionManager;
import com.cikarastudio.cikarajantungdesafix.ssl.HttpsTrustManager;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;
import com.cikarastudio.cikarajantungdesafix.ui.loadingdialog.LoadingDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SuratFragment extends Fragment implements View.OnClickListener {

    SessionManager sessionManager;
    LoadingDialog loadingDialog;
    String id_user, link, token;
    TextView tv_dashboardTotalPengajuanSurat, tv_dashboardSuratSelesaiSurat, tv_dashboardSuratDiprosesSurat, tv_dashboardSuratMenungguSurat;
    TextView tv_sortSuratSemua, tv_sortSuratKeterangan, tv_sortSuratPengantar, tv_sortSuratRekomendasi, tv_sortSuratLainnya,
            tv_sortSuratSelesai, tv_sortSuratDiproses, tv_sortSuratMenunggu;
    TextView tv_filterPopupSemua, tv_filterPopupSuratKeterangan, tv_filterPopupSuratPengantar, tv_filterPopupSuratRekomendasi, tv_filterPopupSuratLainnya,
            tv_filterPopupSuratSelesai, tv_filterPopupSuratProses, tv_filterPopupSuratMenunggu;
    BottomSheetDialog dialog;
    LinearLayout line_filterKategoriListSurat;
    RecyclerView rv_listSuratUser;
    SuratListUserAdapter suratListUserAdapter;
    ImageView img_tambahSurat;
    SearchView et_suratSearch;
    CardView cr_dashboardTotalSurat, cr_dashboardSelesaiSurat, cr_dashboardDiprosesSurat, cr_dashboardMenungguSurat;
    private ArrayList<SuratV2Model> suratUserlist;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_surat, container, false);

        sessionManager = new SessionManager(getActivity());
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        id_user = user.get(sessionManager.ID);

        Log.d("calpalnx", "onCreateView: " + id_user);

        HttpsTrustManager.allowAllSSL();

        //inisiasi link
        link = getString(R.string.link);

        //inisiasi token
        token = getString(R.string.token);

        loadingDialog = new LoadingDialog(getActivity());

        suratUserlist = new ArrayList<>();
        rv_listSuratUser = root.findViewById(R.id.rv_listSuratUser);
        LinearLayoutManager linearLayoutManageraaa = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv_listSuratUser.setLayoutManager(linearLayoutManageraaa);
        rv_listSuratUser.setHasFixedSize(true);

        tv_dashboardTotalPengajuanSurat = root.findViewById(R.id.tv_dashboardTotalPengajuanSurat);
        tv_dashboardSuratSelesaiSurat = root.findViewById(R.id.tv_dashboardSuratSelesaiSurat);
        tv_dashboardSuratDiprosesSurat = root.findViewById(R.id.tv_dashboardSuratDiprosesSurat);
        tv_dashboardSuratMenungguSurat = root.findViewById(R.id.tv_dashboardSuratMenungguSurat);
        et_suratSearch = root.findViewById(R.id.et_suratSearch);

        line_filterKategoriListSurat = root.findViewById(R.id.line_filterKategoriListSurat);
        line_filterKategoriListSurat.setOnClickListener(this);

        tv_sortSuratSemua = root.findViewById(R.id.tv_sortSuratSemua);
        tv_sortSuratSemua.setOnClickListener(this);

        tv_sortSuratKeterangan = root.findViewById(R.id.tv_sortSuratKeterangan);
        tv_sortSuratKeterangan.setOnClickListener(this);

        tv_sortSuratPengantar = root.findViewById(R.id.tv_sortSuratPengantar);
        tv_sortSuratPengantar.setOnClickListener(this);

        tv_sortSuratRekomendasi = root.findViewById(R.id.tv_sortSuratRekomendasi);
        tv_sortSuratRekomendasi.setOnClickListener(this);

        tv_sortSuratLainnya = root.findViewById(R.id.tv_sortSuratLainnya);
        tv_sortSuratLainnya.setOnClickListener(this);

        tv_sortSuratSelesai = root.findViewById(R.id.tv_sortSuratSelesai);
        tv_sortSuratSelesai.setOnClickListener(this);

        tv_sortSuratDiproses = root.findViewById(R.id.tv_sortSuratDiproses);
        tv_sortSuratDiproses.setOnClickListener(this);

        tv_sortSuratMenunggu = root.findViewById(R.id.tv_sortSuratMenunggu);
        tv_sortSuratMenunggu.setOnClickListener(this);

        cr_dashboardTotalSurat = root.findViewById(R.id.cr_dashboardTotalSurat);
        cr_dashboardTotalSurat.setOnClickListener(this);

        cr_dashboardSelesaiSurat = root.findViewById(R.id.cr_dashboardSelesaiSurat);
        cr_dashboardSelesaiSurat.setOnClickListener(this);

        cr_dashboardDiprosesSurat = root.findViewById(R.id.cr_dashboardDiprosesSurat);
        cr_dashboardDiprosesSurat.setOnClickListener(this);

        cr_dashboardMenungguSurat = root.findViewById(R.id.cr_dashboardMenungguSurat);
        cr_dashboardMenungguSurat.setOnClickListener(this);

        img_tambahSurat = root.findViewById(R.id.img_tambahSurat);
        img_tambahSurat.setOnClickListener(this);

        dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(R.layout.layout_bottom_sheet_filter_surat);
        dialog.setCanceledOnTouchOutside(false);
        tv_filterPopupSemua = dialog.findViewById(R.id.tv_filterPopupSemua);
        tv_filterPopupSuratKeterangan = dialog.findViewById(R.id.tv_filterPopupSuratKeterangan);
        tv_filterPopupSuratPengantar = dialog.findViewById(R.id.tv_filterPopupSuratPengantar);
        tv_filterPopupSuratRekomendasi = dialog.findViewById(R.id.tv_filterPopupSuratRekomendasi);
        tv_filterPopupSuratLainnya = dialog.findViewById(R.id.tv_filterPopupSuratLainnya);
        tv_filterPopupSuratSelesai = dialog.findViewById(R.id.tv_filterPopupSuratSelesai);
        tv_filterPopupSuratProses = dialog.findViewById(R.id.tv_filterPopupSuratProses);
        tv_filterPopupSuratMenunggu = dialog.findViewById(R.id.tv_filterPopupSuratMenunggu);

        tv_filterPopupSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSuratUser();
                gantiWarnaFilter(tv_sortSuratSemua, tv_sortSuratKeterangan, tv_sortSuratPengantar, tv_sortSuratRekomendasi, tv_sortSuratLainnya,
                        tv_sortSuratSelesai, tv_sortSuratDiproses, tv_sortSuratMenunggu);
                gantiWarnaFilter(tv_filterPopupSemua, tv_filterPopupSuratKeterangan, tv_filterPopupSuratPengantar, tv_filterPopupSuratRekomendasi, tv_filterPopupSuratLainnya,
                        tv_filterPopupSuratSelesai, tv_filterPopupSuratProses, tv_filterPopupSuratMenunggu);
            }
        });
        tv_filterPopupSuratKeterangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterKategoriSurat("surat keterangan");
                gantiWarnaFilter(tv_sortSuratKeterangan, tv_sortSuratSemua, tv_sortSuratPengantar, tv_sortSuratRekomendasi, tv_sortSuratLainnya,
                        tv_sortSuratSelesai, tv_sortSuratDiproses, tv_sortSuratMenunggu);
                gantiWarnaFilter(tv_filterPopupSuratKeterangan, tv_filterPopupSemua, tv_filterPopupSuratPengantar, tv_filterPopupSuratRekomendasi, tv_filterPopupSuratLainnya,
                        tv_filterPopupSuratSelesai, tv_filterPopupSuratProses, tv_filterPopupSuratMenunggu);
            }
        });
        tv_filterPopupSuratPengantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterKategoriSurat("surat pengantar");
                gantiWarnaFilter(tv_sortSuratPengantar, tv_sortSuratKeterangan, tv_sortSuratSemua, tv_sortSuratRekomendasi, tv_sortSuratLainnya,
                        tv_sortSuratSelesai, tv_sortSuratDiproses, tv_sortSuratMenunggu);
                gantiWarnaFilter(tv_filterPopupSuratPengantar, tv_filterPopupSuratKeterangan, tv_filterPopupSemua, tv_filterPopupSuratRekomendasi, tv_filterPopupSuratLainnya,
                        tv_filterPopupSuratSelesai, tv_filterPopupSuratProses, tv_filterPopupSuratMenunggu);
            }
        });
        tv_filterPopupSuratRekomendasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterKategoriSurat("surat rekomendasi");
                gantiWarnaFilter(tv_sortSuratRekomendasi, tv_sortSuratPengantar, tv_sortSuratKeterangan, tv_sortSuratSemua, tv_sortSuratLainnya,
                        tv_sortSuratSelesai, tv_sortSuratDiproses, tv_sortSuratMenunggu);
                gantiWarnaFilter(tv_filterPopupSuratRekomendasi, tv_filterPopupSuratPengantar, tv_filterPopupSuratKeterangan, tv_filterPopupSemua, tv_filterPopupSuratLainnya,
                        tv_filterPopupSuratSelesai, tv_filterPopupSuratProses, tv_filterPopupSuratMenunggu);
            }
        });
        tv_filterPopupSuratLainnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterKategoriSurat("surat lainnya");
                gantiWarnaFilter(tv_sortSuratLainnya, tv_sortSuratRekomendasi, tv_sortSuratPengantar, tv_sortSuratKeterangan, tv_sortSuratSemua,
                        tv_sortSuratSelesai, tv_sortSuratDiproses, tv_sortSuratMenunggu);
                gantiWarnaFilter(tv_filterPopupSuratLainnya, tv_filterPopupSuratRekomendasi, tv_filterPopupSuratPengantar, tv_filterPopupSuratKeterangan, tv_filterPopupSemua,
                        tv_filterPopupSuratSelesai, tv_filterPopupSuratProses, tv_filterPopupSuratMenunggu);
            }
        });
        tv_filterPopupSuratSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDashboard("selesai");
                gantiWarnaFilter(tv_sortSuratSelesai, tv_sortSuratDiproses, tv_sortSuratMenunggu,
                        tv_sortSuratLainnya, tv_sortSuratRekomendasi, tv_sortSuratPengantar, tv_sortSuratKeterangan, tv_sortSuratSemua);
                gantiWarnaFilter(tv_filterPopupSuratSelesai, tv_filterPopupSuratProses, tv_filterPopupSuratMenunggu,
                        tv_filterPopupSuratLainnya, tv_filterPopupSuratRekomendasi, tv_filterPopupSuratPengantar, tv_filterPopupSuratKeterangan, tv_filterPopupSemua);
            }
        });
        tv_filterPopupSuratProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDashboard("proses");
                gantiWarnaFilter(tv_sortSuratDiproses, tv_sortSuratSelesai, tv_sortSuratMenunggu,
                        tv_sortSuratLainnya, tv_sortSuratRekomendasi, tv_sortSuratPengantar, tv_sortSuratKeterangan, tv_sortSuratSemua);
                gantiWarnaFilter(tv_filterPopupSuratProses, tv_filterPopupSuratSelesai, tv_filterPopupSuratMenunggu,
                        tv_filterPopupSuratLainnya, tv_filterPopupSuratRekomendasi, tv_filterPopupSuratPengantar, tv_filterPopupSuratKeterangan, tv_filterPopupSemua);
            }
        });
        tv_filterPopupSuratMenunggu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterDashboard("menunggu");
                gantiWarnaFilter(tv_sortSuratMenunggu, tv_sortSuratDiproses, tv_sortSuratSelesai,
                        tv_sortSuratLainnya, tv_sortSuratRekomendasi, tv_sortSuratPengantar, tv_sortSuratKeterangan, tv_sortSuratSemua);
                gantiWarnaFilter(tv_filterPopupSuratMenunggu, tv_filterPopupSuratProses, tv_filterPopupSuratSelesai,
                        tv_filterPopupSuratLainnya, tv_filterPopupSuratRekomendasi, tv_filterPopupSuratPengantar, tv_filterPopupSuratKeterangan, tv_filterPopupSemua);
            }
        });


        CardView cr_okFilterPopUp = dialog.findViewById(R.id.cr_okFilterPopUp);
        cr_okFilterPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSuratUser();
        loadDashboardSurat();

        et_suratSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String nextText) {
                //Data akan berubah saat user menginputkan text/kata kunci pada SearchView
                if (suratUserlist.size() > 0) {
                    nextText = nextText.toLowerCase();
                    ArrayList<SuratV2Model> dataFilter = new ArrayList<>();
                    for (SuratV2Model data : suratUserlist) {
                        String nama = data.getNama_surat().toLowerCase();
                        if (nama.contains(nextText)) {
                            dataFilter.add(data);
                        }
                    }
                    suratListUserAdapter.setFilter(dataFilter);
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_tambahSurat:
                // do your code
                Intent keTambahSurat = new Intent(getActivity(), ListKategoriSuratActivity.class);
                startActivity(keTambahSurat);
                break;
            case R.id.cr_dashboardTotalSurat:
                // do your code
                loadSuratUser();
                break;
            case R.id.cr_dashboardSelesaiSurat:
                // do your code
                filterDashboard("selesai");
                break;
            case R.id.cr_dashboardDiprosesSurat:
                // do your code
                filterDashboard("proses");
                break;
            case R.id.cr_dashboardMenungguSurat:
                // do your code
                filterDashboard("menunggu");
                break;
            case R.id.tv_sortSuratSemua:
                // do your code
                loadSuratUser();
                gantiWarnaFilter(tv_sortSuratSemua, tv_sortSuratKeterangan, tv_sortSuratPengantar, tv_sortSuratRekomendasi, tv_sortSuratLainnya,
                        tv_sortSuratSelesai, tv_sortSuratDiproses, tv_sortSuratMenunggu);
                gantiWarnaFilter(tv_filterPopupSemua, tv_filterPopupSuratKeterangan, tv_filterPopupSuratPengantar, tv_filterPopupSuratRekomendasi, tv_filterPopupSuratLainnya,
                        tv_filterPopupSuratSelesai, tv_filterPopupSuratProses, tv_filterPopupSuratMenunggu);
                break;
            case R.id.tv_sortSuratKeterangan:
                // do your code
                filterKategoriSurat("surat keterangan");
                gantiWarnaFilter(tv_sortSuratKeterangan, tv_sortSuratSemua, tv_sortSuratPengantar, tv_sortSuratRekomendasi, tv_sortSuratLainnya,
                        tv_sortSuratSelesai, tv_sortSuratDiproses, tv_sortSuratMenunggu);
                gantiWarnaFilter(tv_filterPopupSuratKeterangan, tv_filterPopupSemua, tv_filterPopupSuratPengantar, tv_filterPopupSuratRekomendasi, tv_filterPopupSuratLainnya,
                        tv_filterPopupSuratSelesai, tv_filterPopupSuratProses, tv_filterPopupSuratMenunggu);
                break;
            case R.id.tv_sortSuratPengantar:
                // do your code
                filterKategoriSurat("surat pengantar");
                gantiWarnaFilter(tv_sortSuratPengantar, tv_sortSuratKeterangan, tv_sortSuratSemua, tv_sortSuratRekomendasi, tv_sortSuratLainnya,
                        tv_sortSuratSelesai, tv_sortSuratDiproses, tv_sortSuratMenunggu);
                gantiWarnaFilter(tv_filterPopupSuratPengantar, tv_filterPopupSuratKeterangan, tv_filterPopupSemua, tv_filterPopupSuratRekomendasi, tv_filterPopupSuratLainnya,
                        tv_filterPopupSuratSelesai, tv_filterPopupSuratProses, tv_filterPopupSuratMenunggu);
                break;
            case R.id.tv_sortSuratRekomendasi:
                // do your code
                filterKategoriSurat("surat rekomendasi");
                gantiWarnaFilter(tv_sortSuratRekomendasi, tv_sortSuratPengantar, tv_sortSuratKeterangan, tv_sortSuratSemua, tv_sortSuratLainnya,
                        tv_sortSuratSelesai, tv_sortSuratDiproses, tv_sortSuratMenunggu);
                gantiWarnaFilter(tv_filterPopupSuratRekomendasi, tv_filterPopupSuratPengantar, tv_filterPopupSuratKeterangan, tv_filterPopupSemua, tv_filterPopupSuratLainnya,
                        tv_filterPopupSuratSelesai, tv_filterPopupSuratProses, tv_filterPopupSuratMenunggu);
                break;
            case R.id.tv_sortSuratLainnya:
                // do your code
                filterKategoriSurat("surat lainnya");
                gantiWarnaFilter(tv_sortSuratLainnya, tv_sortSuratRekomendasi, tv_sortSuratPengantar, tv_sortSuratKeterangan, tv_sortSuratSemua,
                        tv_sortSuratSelesai, tv_sortSuratDiproses, tv_sortSuratMenunggu);
                gantiWarnaFilter(tv_filterPopupSuratLainnya, tv_filterPopupSuratRekomendasi, tv_filterPopupSuratPengantar, tv_filterPopupSuratKeterangan, tv_filterPopupSemua,
                        tv_filterPopupSuratSelesai, tv_filterPopupSuratProses, tv_filterPopupSuratMenunggu);
                break;
            case R.id.tv_sortSuratSelesai:
                // do your code
                filterDashboard("selesai");
                gantiWarnaFilter(tv_sortSuratSelesai, tv_sortSuratDiproses, tv_sortSuratMenunggu,
                        tv_sortSuratLainnya, tv_sortSuratRekomendasi, tv_sortSuratPengantar, tv_sortSuratKeterangan, tv_sortSuratSemua);
                gantiWarnaFilter(tv_filterPopupSuratSelesai, tv_filterPopupSuratProses, tv_filterPopupSuratMenunggu,
                        tv_filterPopupSuratLainnya, tv_filterPopupSuratRekomendasi, tv_filterPopupSuratPengantar, tv_filterPopupSuratKeterangan, tv_filterPopupSemua);
                break;
            case R.id.tv_sortSuratDiproses:
                // do your code
                filterDashboard("proses");
                gantiWarnaFilter(tv_sortSuratDiproses, tv_sortSuratSelesai, tv_sortSuratMenunggu,
                        tv_sortSuratLainnya, tv_sortSuratRekomendasi, tv_sortSuratPengantar, tv_sortSuratKeterangan, tv_sortSuratSemua);
                gantiWarnaFilter(tv_filterPopupSuratProses, tv_filterPopupSuratSelesai, tv_filterPopupSuratMenunggu,
                        tv_filterPopupSuratLainnya, tv_filterPopupSuratRekomendasi, tv_filterPopupSuratPengantar, tv_filterPopupSuratKeterangan, tv_filterPopupSemua);
                break;
            case R.id.tv_sortSuratMenunggu:
                // do your code
                filterDashboard("menunggu");
                gantiWarnaFilter(tv_sortSuratMenunggu, tv_sortSuratDiproses, tv_sortSuratSelesai,
                        tv_sortSuratLainnya, tv_sortSuratRekomendasi, tv_sortSuratPengantar, tv_sortSuratKeterangan, tv_sortSuratSemua);
                gantiWarnaFilter(tv_filterPopupSuratMenunggu, tv_filterPopupSuratProses, tv_filterPopupSuratSelesai,
                        tv_filterPopupSuratLainnya, tv_filterPopupSuratRekomendasi, tv_filterPopupSuratPengantar, tv_filterPopupSuratKeterangan, tv_filterPopupSemua);
                break;
            case R.id.line_filterKategoriListSurat:
                // do your code
                dialog.show();
                break;
            default:
                break;
        }
    }

    private void filterDashboard(String bahan) {
        if (suratUserlist.size() > 0) {
            bahan = bahan.toLowerCase();
            ArrayList<SuratV2Model> dataFilter = new ArrayList<>();
            for (SuratV2Model data : suratUserlist) {
                String status = data.getStatus().toLowerCase();
                if (status.contains(bahan)) {
                    dataFilter.add(data);
                }
            }
            suratListUserAdapter.setFilter(dataFilter);
        }
    }

    private void filterKategoriSurat(String bahan) {
        if (suratUserlist.size() > 0) {
            bahan = bahan.toLowerCase();
            ArrayList<SuratV2Model> dataFilter = new ArrayList<>();
            for (SuratV2Model data : suratUserlist) {
                String status = data.getKategori().toLowerCase();
                if (status.contains(bahan)) {
                    dataFilter.add(data);
                }
            }
            suratListUserAdapter.setFilter(dataFilter);
        }
    }

    private void gantiWarnaFilter(TextView tv1, TextView tv2, TextView tv3, TextView tv4, TextView tv5, TextView tv6, TextView tv7, TextView tv8) {
        tv1.setBackgroundResource(R.drawable.border_biru_muda);
        tv2.setBackgroundResource(R.drawable.border_putih_biru_muda);
        tv3.setBackgroundResource(R.drawable.border_putih_biru_muda);
        tv4.setBackgroundResource(R.drawable.border_putih_biru_muda);
        tv5.setBackgroundResource(R.drawable.border_putih_biru_muda);
        tv6.setBackgroundResource(R.drawable.border_putih_biru_muda);
        tv7.setBackgroundResource(R.drawable.border_putih_biru_muda);
        tv8.setBackgroundResource(R.drawable.border_putih_biru_muda);

        tv1.setTextColor(getResources().getColor(R.color.white));
        tv2.setTextColor(getResources().getColor(R.color.biru2));
        tv3.setTextColor(getResources().getColor(R.color.biru2));
        tv4.setTextColor(getResources().getColor(R.color.biru2));
        tv5.setTextColor(getResources().getColor(R.color.biru2));
        tv6.setTextColor(getResources().getColor(R.color.biru2));
        tv7.setTextColor(getResources().getColor(R.color.biru2));
        tv8.setTextColor(getResources().getColor(R.color.biru2));
    }

    private void loadDashboardSurat() {
        String URL_READ = link + "dashboarduser/surat/" + id_user;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            //data dashboard surat
                            String res_total = jsonObject.getString("total").trim();
                            String res_selesai = jsonObject.getString("selesai").trim();
                            String res_proses = jsonObject.getString("proses").trim();
                            String res_menunggu = jsonObject.getString("menunggu").trim();

                            TextFuntion textFuntion = new TextFuntion();
                            //data dashboard home
                            textFuntion.setTextDanNullData(tv_dashboardTotalPengajuanSurat, res_total);
                            textFuntion.setTextDanNullData(tv_dashboardSuratSelesaiSurat, res_selesai);
                            textFuntion.setTextDanNullData(tv_dashboardSuratDiprosesSurat, res_proses);
                            textFuntion.setTextDanNullData(tv_dashboardSuratMenungguSurat, res_menunggu);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Data Dashboard Tidak Ada!" + e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Tidak Ada Koneksi Internet!", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 10 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(jsonString, cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void loadSuratUser() {
        String URL_READ = link + "listsuratbyuser/" + id_user + "?token=" + token + "&sortby=DESC";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (suratUserlist.size() > 0) {
                            suratUserlist.clear();
                        }
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    //data surat user
                                    String res_id = jsonObject.getString("id").trim();
                                    String res_userId = jsonObject.getString("user_id").trim();
                                    String res_formatsuratIdSurat = jsonObject.getString("formatsurat_id").trim();
                                    String res_statusSurat = jsonObject.getString("status").trim();
                                    String res_nomorSurat = jsonObject.getString("nomor_surat").trim();
                                    String res_tglAwalSurat = jsonObject.getString("tgl_awal").trim();
                                    String res_tglAkhirSurat = jsonObject.getString("tgl_akhir").trim();
                                    String res_createdAtSurat = jsonObject.getString("created_at").trim();
                                    String res_updatedAtSurat = jsonObject.getString("created_at").trim();
                                    String res_namaSuratSurat = jsonObject.getString("nama_surat").trim();
                                    String res_kodeSurat = jsonObject.getString("kode").trim();
                                    String res_kategoriSurat = jsonObject.getString("kategori").trim();

                                    Log.d("calpalnx", "onResponse: " + res_kategoriSurat);

                                    suratUserlist.add(new SuratV2Model(res_id, res_userId, res_formatsuratIdSurat, res_statusSurat,
                                            res_nomorSurat, res_tglAwalSurat, res_tglAkhirSurat, res_createdAtSurat, res_updatedAtSurat,
                                            res_namaSuratSurat, res_kodeSurat, res_kategoriSurat));
                                    suratListUserAdapter = new SuratListUserAdapter(getContext(), suratUserlist);
                                    rv_listSuratUser.setAdapter(suratListUserAdapter);

                                    suratListUserAdapter.setOnItemClickCallback(new SuratListUserAdapter.OnItemClickCallback() {
                                        @Override
                                        public void onItemClicked(SuratV2Model data) {
                                            Intent transferDataFormatSurat = new Intent(getContext(), EditSuratActivity.class);
                                            transferDataFormatSurat.putExtra(EditSuratActivity.DATA_FORMAT_SURAT_USER, data);
                                            startActivity(transferDataFormatSurat);
                                        }
                                    });

                                    suratListUserAdapter.setOnDeleteClick(new SuratListUserAdapter.OnDeleteClick() {
                                        @Override
                                        public void onItemClicked(SuratV2Model data) {
                                            dialogDelete(data.getId());
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Data Surat Tidak Ada!" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Tidak Ada Koneksi Internet!", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 10 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(jsonString, cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(String response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void dialogDelete(String id_produk) {
        AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(getActivity());
        alertdialogBuilder.setTitle("Konfismasi Delete");
        alertdialogBuilder.setMessage("Apakah Anda Yakin Menghapus Data Ini?");
        alertdialogBuilder.setCancelable(false);
        alertdialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                hapusData(id_produk);
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

    private void hapusData(String id) {
        loadingDialog.startLoading();
        Log.d("calpalnx", String.valueOf(id));
        String URL_DELETEPRODUK = link + "hapussurat/" + id + "?token=" + token;
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, URL_DELETEPRODUK,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Toast.makeText(getActivity(), "Hapus Surat Sukses", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                                onResume();
                            } else {
                                Toast.makeText(getActivity(), "Hapus Surat Gagal!", Toast.LENGTH_LONG).show();
                                loadingDialog.dissmissDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Hapus Surat Gagal! : " + e.toString(), Toast.LENGTH_LONG).show();
                            loadingDialog.dissmissDialog();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Hapus Surat Gagal! : Cek Koneksi Anda, " + error, Toast.LENGTH_LONG).show();
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
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}