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

public class ProdukSemuaListAdapter extends RecyclerView.Adapter<ProdukSemuaListAdapter.ProdukSemuaListViewHolder> {

    private Context mContext;
    private ArrayList<ProdukSemuaModel> mProdukSemuaList;

    private ProdukSemuaListAdapter.OnItemClickCallback onItemClickCallback;

    public ProdukSemuaListAdapter(Context mContext, ArrayList<ProdukSemuaModel> mProdukSemuaList) {
        this.mContext = mContext;
        this.mProdukSemuaList = mProdukSemuaList;
    }

    public void setOnItemClickCallback(ProdukSemuaListAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    //setfilter
    public void setFilter(ArrayList<ProdukSemuaModel> dataFilter) {
        mProdukSemuaList = new ArrayList<>();
        mProdukSemuaList.addAll(dataFilter);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProdukSemuaListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_produk_semua, parent, false);
        return new ProdukSemuaListAdapter.ProdukSemuaListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdukSemuaListViewHolder holder, int position) {
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
        return mProdukSemuaList.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(ProdukSemuaModel data);
    }

    public class ProdukSemuaListViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_namaItemSemuaProduk;
        public TextView tv_kaliDilihatItemSemuaProduk;
        public TextView tv_hargaItemSemuaProduk;
        public ImageView img_gambarItemSemuaProduk;

        public ProdukSemuaListViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_namaItemSemuaProduk = itemView.findViewById(R.id.tv_namaItemSemuaProduk);
            tv_kaliDilihatItemSemuaProduk = itemView.findViewById(R.id.tv_kaliDilihatItemSemuaProduk);
            tv_hargaItemSemuaProduk = itemView.findViewById(R.id.tv_hargaItemSemuaProduk);
            img_gambarItemSemuaProduk = itemView.findViewById(R.id.img_gambarItemSemuaProduk);
        }
    }
}
