package com.cikarastudio.cikarajantungdesafix.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.model.KategoriArtikelModel;
import com.cikarastudio.cikarajantungdesafix.model.KategoriModel;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;

import java.util.ArrayList;

public class KategoriArtikelAdapter extends RecyclerView.Adapter<KategoriArtikelAdapter.KategoriArtikelViewHolder> {

    private Context mContext;
    private ArrayList<KategoriArtikelModel> mKategoriArtikelList;

    private int selected_position = -1;

    private KategoriArtikelAdapter.OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(KategoriArtikelAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(KategoriArtikelModel data);
    }

    public KategoriArtikelAdapter(Context mContext, ArrayList<KategoriArtikelModel> mKategoriArtikelList) {
        this.mContext = mContext;
        this.mKategoriArtikelList = mKategoriArtikelList;
    }

    @NonNull
    @Override
    public KategoriArtikelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_kategori, parent, false);
        return new KategoriArtikelAdapter.KategoriArtikelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull KategoriArtikelViewHolder holder, int position) {
        KategoriArtikelModel currentItem = mKategoriArtikelList.get(position);
        String namaKategori = currentItem.getNama_kategori();

        TextFuntion textFuntion = new TextFuntion();
        //data kategori
        textFuntion.setTextDanNullData(holder.namaKategori, namaKategori);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(mKategoriArtikelList.get(holder.getAdapterPosition()));
                selected_position = holder.getAdapterPosition();
                notifyDataSetChanged();
            }

        });

        if (selected_position == position) {
            holder.namaKategori.setBackgroundResource(R.drawable.border_biru_muda);
            holder.namaKategori.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            holder.namaKategori.setBackgroundResource(R.drawable.border_putih_biru_muda);
            holder.namaKategori.setTextColor(mContext.getResources().getColor(R.color.biru2));
        }
    }

    @Override
    public int getItemCount() {
        return mKategoriArtikelList.size();
    }

    public class KategoriArtikelViewHolder extends RecyclerView.ViewHolder {
        public TextView namaKategori;

        public KategoriArtikelViewHolder(final View itemView) {
            super(itemView);
            namaKategori = itemView.findViewById(R.id.tv_namaKategori);
        }
    }
}
