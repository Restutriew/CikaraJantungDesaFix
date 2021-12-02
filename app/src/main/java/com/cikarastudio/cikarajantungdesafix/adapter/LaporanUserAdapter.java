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
import com.cikarastudio.cikarajantungdesafix.model.LaporanUserModel;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LaporanUserAdapter extends RecyclerView.Adapter<LaporanUserAdapter.LaporanUserViewHolder> {

    private Context mContext;
    private ArrayList<LaporanUserModel> mLaporanUserList;

    public LaporanUserAdapter(Context mContext, ArrayList<LaporanUserModel> mLaporanUserList) {
        this.mContext = mContext;
        this.mLaporanUserList = mLaporanUserList;
    }

    //setfilter
    public void setFilter(ArrayList<LaporanUserModel> dataFilter) {
        mLaporanUserList = new ArrayList<>();
        mLaporanUserList.addAll(dataFilter);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public LaporanUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_laporan_saya, parent, false);
        return new LaporanUserAdapter.LaporanUserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LaporanUserViewHolder holder, int position) {
        LaporanUserModel currentItem = mLaporanUserList.get(position);
        String gambarLaporan = currentItem.getPhoto();
        String isiLaporan = currentItem.getIsi();
        String tanggapanLaporan = currentItem.getTanggapan();
        String kategoriLaporan = currentItem.getKategori();
        String statusLaporan = currentItem.getStatus();
        String identitasLaporan = currentItem.getIdentitas();
        String postingLaporan = currentItem.getPosting();
        String updatedAtLaporan = currentItem.getUpdated_at();

        String jam = updatedAtLaporan.substring(11, 13);
        String menit = updatedAtLaporan.substring(14, 16);

        String tanggal = updatedAtLaporan.substring(8, 10);
        String bulan = updatedAtLaporan.substring(5, 7);
        String tahun = updatedAtLaporan.substring(0, 4);

        String waktuLaporan = jam + "." + menit;
        String tanggalLaporan = tanggal + "-" + bulan + "-" + tahun;




        Log.d("calpalnx", currentItem.getIsi());

        String imgLaporan = "https://puteran.cikarastudio.com/public/img/penduduk/lapor/" + gambarLaporan;
        Picasso.with(mContext.getApplicationContext()).load(imgLaporan).fit().centerCrop().into(holder.img_gambarLaporan);

        holder.tv_isiLaporan.setText(isiLaporan);

        if (tanggapanLaporan.equals("null")) {
            holder.tv_responlaporan.setText("Belum Ada Tanggapan");
        } else {
            holder.tv_responlaporan.setText(tanggapanLaporan);
        }

        TextFuntion textFuntion = new TextFuntion();
        //data laporan
        textFuntion.setTextDanNullData(holder.tv_kategoriLaporan, kategoriLaporan);
        textFuntion.setTextDanNullData(holder.tv_statuslaporan, statusLaporan);
        textFuntion.setTextDanNullData(holder.tv_identitasLaporan, identitasLaporan);
        textFuntion.setTextDanNullData(holder.tv_postingLaporan, postingLaporan);
        textFuntion.setTextDanNullData(holder.tv_waktuLaporan, waktuLaporan);
        textFuntion.setTextDanNullData(holder.tv_tglLaporan, tanggalLaporan);

    }

    @Override
    public int getItemCount() {
        return mLaporanUserList.size();
    }

    public class LaporanUserViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_gambarLaporan;
        public TextView tv_isiLaporan;
        public TextView tv_responlaporan;
        public TextView tv_statuslaporan;
        public TextView tv_kategoriLaporan;
        public TextView tv_tglLaporan;
        public TextView tv_waktuLaporan;
        public TextView tv_postingLaporan;
        public TextView tv_identitasLaporan;


        LaporanUserViewHolder(@NonNull View itemView) {
            super(itemView);
            img_gambarLaporan = itemView.findViewById(R.id.img_gambarLaporan);
            tv_isiLaporan = itemView.findViewById(R.id.tv_isiLaporan);
            tv_responlaporan = itemView.findViewById(R.id.tv_responlaporan);
            tv_statuslaporan = itemView.findViewById(R.id.tv_statuslaporan);
            tv_kategoriLaporan = itemView.findViewById(R.id.tv_kategoriLaporan);
            tv_tglLaporan = itemView.findViewById(R.id.tv_tglLaporan);
            tv_waktuLaporan = itemView.findViewById(R.id.tv_waktuLaporan);
            tv_postingLaporan = itemView.findViewById(R.id.tv_postingLaporan);
            tv_identitasLaporan = itemView.findViewById(R.id.tv_identitasLaporan);
        }
    }
}
