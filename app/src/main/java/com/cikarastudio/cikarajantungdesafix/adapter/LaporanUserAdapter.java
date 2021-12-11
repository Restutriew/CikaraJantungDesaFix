package com.cikarastudio.cikarajantungdesafix.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
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

    private LaporanUserAdapter.OnItemClickCallback onItemClickCallback;
    private LaporanUserAdapter.OnDeleteClick onDeleteClick;

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

    public void setOnItemClickCallback(LaporanUserAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setOnDeleteClick(LaporanUserAdapter.OnDeleteClick onDeleteClick) {
        this.onDeleteClick = onDeleteClick;
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
        Picasso.with(mContext.getApplicationContext()).load(imgLaporan).into(holder.img_gambarLaporan);

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


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(mLaporanUserList.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mLaporanUserList.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(LaporanUserModel data);
    }

    public interface OnDeleteClick {
        void onItemClicked(LaporanUserModel data);
    }

    public class LaporanUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        public ImageView img_gambarLaporan;
        public TextView tv_isiLaporan;
        public TextView tv_responlaporan;
        public TextView tv_statuslaporan;
        public TextView tv_kategoriLaporan;
        public TextView tv_tglLaporan;
        public TextView tv_waktuLaporan;
        public TextView tv_postingLaporan;
        public TextView tv_identitasLaporan;
        public ImageView img_buttonDots;


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

            img_buttonDots = itemView.findViewById(R.id.img_buttonDots);

            img_buttonDots.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Log.d("calpalnx", "onClick: " + getAdapterPosition());
            showPopupMenu(view);
        }

        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_popup_edit:
                    Log.d("calpalnx", "onMenuItemClick: edit" + getAdapterPosition());
                    onItemClickCallback.onItemClicked(mLaporanUserList.get(getAdapterPosition()));
                    return true;
                case R.id.action_popup_delete:
                    Log.d("calpalnx", "onMenuItemClick: delete" + getAdapterPosition());
                    onDeleteClick.onItemClicked(mLaporanUserList.get(getAdapterPosition()));
//                    ProdukModel currentItem = mProdukList.get(getAdapterPosition());
//                    String id = currentItem.getId();
//                    Log.d("calpalnx", "onMenuItemClick: delete" + getAdapterPosition());
//                    LapakFragment lapakFragment = new LapakFragment();
//                    lapakFragment.hapusData(id);
                    return true;
            }
            return false;
        }
    }

}
