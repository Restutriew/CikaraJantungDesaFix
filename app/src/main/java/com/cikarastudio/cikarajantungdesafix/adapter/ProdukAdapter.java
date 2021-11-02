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
import com.cikarastudio.cikarajantungdesafix.model.ProdukModel;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;
import com.cikarastudio.cikarajantungdesafix.ui.lapak.LapakFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ProdukViewHolder> {

    private Context mContext;
    private ArrayList<ProdukModel> mProdukList;

    //metode Onclick
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(ProdukModel data);
    }

    public ProdukAdapter(Context mContext, ArrayList<ProdukModel> mProdukList) {
        this.mContext = mContext;
        this.mProdukList = mProdukList;
    }


    //setfilter
    public void setFilter(ArrayList<ProdukModel> dataFilter) {
        mProdukList = new ArrayList<>();
        mProdukList.addAll(dataFilter);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_produk, parent, false);
        return new ProdukAdapter.ProdukViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdukViewHolder holder, int position) {
        ProdukModel currentItem = mProdukList.get(position);
        String namaProduk = currentItem.getNama();
        String keteranganProduk = currentItem.getKeterangan();
        String gambarProduk = currentItem.getGambar();
        Integer hargaProduk = currentItem.getHarga();
        Integer dilihatProduk = currentItem.getDilihat();

        TextFuntion textFuntion = new TextFuntion();
        //data diri
        textFuntion.setTextDanNullData(holder.nama, namaProduk);
        textFuntion.setRupiah(holder.harga, hargaProduk);
        textFuntion.setTextDanNullData(holder.keterangan, keteranganProduk);
        textFuntion.setAngka(holder.kaliDilihat, dilihatProduk);

        Log.d("calpalnx", gambarProduk);

        String imageUrl = "https://jantungdesa.cikarastudio.com/public/img/penduduk/produk/" + gambarProduk;
        Picasso.with(mContext.getApplicationContext()).load(imageUrl).fit().centerCrop().into(holder.gambar, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(mProdukList.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mProdukList.size();
    }

    public class ProdukViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        public TextView nama;
        public TextView harga;
        public TextView kaliDilihat;
        public TextView keterangan;
        public ImageView gambar;
        public ImageView img_buttonDots;

        ProdukViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.tv_namaItemProduk);
            harga = itemView.findViewById(R.id.tv_hargaItemProduk);
            kaliDilihat = itemView.findViewById(R.id.tv_kaliDilihatItemProduk);
            keterangan = itemView.findViewById(R.id.tv_keteranganItemProduk);
            gambar = itemView.findViewById(R.id.img_gambarItemProduk);
            img_buttonDots = itemView.findViewById(R.id.img_buttonDots);

            img_buttonDots.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d("calpalnx", "onClick: " + getAdapterPosition());
            showPopupMenu(view);
        }

        private void showPopupMenu(View view){
            PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.action_popup_edit:
                    Log.d("calpalnx", "onMenuItemClick: edit" + getAdapterPosition());
                    onItemClickCallback.onItemClicked(mProdukList.get(getAdapterPosition()));
                    return true;
                case R.id.action_popup_delete:
                    ProdukModel currentItem = mProdukList.get(getAdapterPosition());
                    String id = currentItem.getId();
                    Log.d("calpalnx", "onMenuItemClick: delete" + getAdapterPosition());
//                    LapakFragment lapakFragment = new LapakFragment();
//                    lapakFragment.hapusData(id);
                    return true;
            }
            return false;
        }
    }
}
