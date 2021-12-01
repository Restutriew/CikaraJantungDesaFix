package com.cikarastudio.cikarajantungdesafix.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.model.LaporanModel;
import com.cikarastudio.cikarajantungdesafix.model.ProdukModel;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LaporanAdapter extends RecyclerView.Adapter<LaporanAdapter.LaporanViewHolder> {

    private Context mContext;
    private ArrayList<LaporanModel> mLaporanList;

    public LaporanAdapter(Context mContext, ArrayList<LaporanModel> mLaporanList) {
        this.mContext = mContext;
        this.mLaporanList = mLaporanList;
    }

    //setfilter
    public void setFilter(ArrayList<LaporanModel> dataFilter) {
        mLaporanList = new ArrayList<>();
        mLaporanList.addAll(dataFilter);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LaporanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_laporan, parent, false);
        return new LaporanAdapter.LaporanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LaporanViewHolder holder, int position) {
        LaporanModel currentItem = mLaporanList.get(position);
        String namaUser = currentItem.getNama_penduduk();
        String waktuLaporan = currentItem.getWaktu();
        String tglLaporan = currentItem.getTanggal();
        String kategoriLaporan = currentItem.getKategori();
        String isiLaporan = currentItem.getIsi();
        String tanggapanLaporan = currentItem.getTanggapan();
        String jumlahLikeLaporan = currentItem.getJumlahlike();
        String photoUser = currentItem.getProfile_photo_path();
        String gambarLaporan = currentItem.getPhoto();

        Log.d("calpalnx", currentItem.getIsi());

        TextFuntion textFuntion = new TextFuntion();
        //data laporan
        textFuntion.setTextDanNullData(holder.tv_namaUserLaporan, namaUser);
        textFuntion.setTextDanNullData(holder.tv_waktuLaporan, waktuLaporan);
        textFuntion.setTextDanNullData(holder.tv_tglLaporan, tglLaporan);
        textFuntion.setTextDanNullData(holder.tv_kategoriLaporan, kategoriLaporan);
        textFuntion.setTextDanNullData(holder.tv_jumlahLikeLaporan, jumlahLikeLaporan + " suka");

        if (tanggapanLaporan.equals("null")) {
            holder.tv_responlaporan.setText("Belum Ada Tanggapan");
        } else {
            holder.tv_responlaporan.setText(tanggapanLaporan);
        }

        holder.tv_isiLaporan.setText(isiLaporan);

        String imgUser = "https://puteran.cikarastudio.com/public/img/user/" + photoUser;
        Picasso.with(mContext.getApplicationContext()).load(imgUser).fit().centerCrop().into(holder.img_potouser);

        String imgLaporan = "https://puteran.cikarastudio.com/public/img/penduduk/lapor/" + gambarLaporan;
        Picasso.with(mContext.getApplicationContext()).load(imgLaporan).fit().centerCrop().into(holder.img_gambarLaporan);

    }

    @Override
    public int getItemCount() {
        return mLaporanList.size();
    }

    public class LaporanViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_namaUserLaporan;
        public TextView tv_waktuLaporan;
        public TextView tv_tglLaporan;
        public TextView tv_kategoriLaporan;
        public TextView tv_isiLaporan;
        public TextView tv_responlaporan;
        public TextView tv_jumlahLikeLaporan;
        public ImageView img_potouser;
        public ImageView img_gambarLaporan;

        LaporanViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_namaUserLaporan = itemView.findViewById(R.id.tv_namaUserLaporan);
            tv_waktuLaporan = itemView.findViewById(R.id.tv_waktuLaporan);
            tv_tglLaporan = itemView.findViewById(R.id.tv_tglLaporan);
            tv_kategoriLaporan = itemView.findViewById(R.id.tv_kategoriLaporan);
            tv_isiLaporan = itemView.findViewById(R.id.tv_isiLaporan);
            tv_responlaporan = itemView.findViewById(R.id.tv_responlaporan);
            tv_jumlahLikeLaporan = itemView.findViewById(R.id.tv_jumlahLikeLaporan);
            img_potouser = itemView.findViewById(R.id.img_potouser);
            img_gambarLaporan = itemView.findViewById(R.id.img_gambarLaporan);
        }
    }
}
