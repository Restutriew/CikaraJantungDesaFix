package com.cikarastudio.cikarajantungdesafix.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.model.ProdukSemuaModel;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProdukSemuaAdapter extends RecyclerView.Adapter<ProdukSemuaAdapter.ProdukSemuaViewHolder> {

    private Context mContext;
    private ArrayList<ProdukSemuaModel> mProdukSemuaList;
    //metode Onclick
    private ProdukSemuaAdapter.OnItemClickCallback onItemClickCallback;

    public ProdukSemuaAdapter(Context mContext, ArrayList<ProdukSemuaModel> mProdukSemuaList) {
        this.mContext = mContext;
        this.mProdukSemuaList = mProdukSemuaList;
    }

    public void setOnItemClickCallback(ProdukSemuaAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ProdukSemuaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_produk_semua, parent, false);
        return new ProdukSemuaAdapter.ProdukSemuaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdukSemuaViewHolder holder, int position) {
        ProdukSemuaModel currentItem = mProdukSemuaList.get(position);
        String namaProduk = currentItem.getNama();
        String gambarProduk = currentItem.getGambar();
        Integer hargaProduk = currentItem.getHarga();
        Integer dilihatProduk = currentItem.getDilihat();

        TextFuntion textFuntion = new TextFuntion();
        //data diri
        textFuntion.setTextDanNullData(holder.tv_namaItemSemuaProduk, namaProduk);
        textFuntion.setRupiah(holder.tv_hargaItemSemuaProduk, hargaProduk);
        textFuntion.setAngka(holder.tv_kaliDilihatItemSemuaProduk, dilihatProduk);

        String imageUrl = "https://puteran.cikarastudio.com/public/img/penduduk/produk/" + gambarProduk;
        Picasso.with(mContext.getApplicationContext()).load(imageUrl).fit().centerCrop().into(holder.img_gambarItemSemuaProduk);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(mProdukSemuaList.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        Integer limit;
        if (mProdukSemuaList.size() < 5) {
            limit = mProdukSemuaList.size();
        } else {
            limit = 5;
        }
        return limit;
    }

    public interface OnItemClickCallback {
        void onItemClicked(ProdukSemuaModel data);
    }

    public class ProdukSemuaViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_namaItemSemuaProduk;
        public TextView tv_kaliDilihatItemSemuaProduk;
        public TextView tv_hargaItemSemuaProduk;
        public ImageView img_gambarItemSemuaProduk;

        public ProdukSemuaViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_namaItemSemuaProduk = itemView.findViewById(R.id.tv_namaItemSemuaProduk);
            tv_kaliDilihatItemSemuaProduk = itemView.findViewById(R.id.tv_kaliDilihatItemSemuaProduk);
            tv_hargaItemSemuaProduk = itemView.findViewById(R.id.tv_hargaItemSemuaProduk);
            img_gambarItemSemuaProduk = itemView.findViewById(R.id.img_gambarItemSemuaProduk);
        }
    }
}
