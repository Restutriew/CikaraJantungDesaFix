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
import com.cikarastudio.cikarajantungdesafix.model.KategoriModel;
import com.cikarastudio.cikarajantungdesafix.model.ProdukModel;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;

import java.util.ArrayList;

public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.KategoriViewHolder> {

    private Context mContext;
    private ArrayList<KategoriModel> mKategoriList;

    private KategoriAdapter.OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(KategoriAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(KategoriModel data);
    }

    public KategoriAdapter(Context mContext, ArrayList<KategoriModel> mKategoriList) {
        this.mContext = mContext;
        this.mKategoriList = mKategoriList;
    }

    @NonNull
    @Override
    public KategoriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_kategori, parent, false);
        return new KategoriViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull KategoriViewHolder holder, int position) {
        KategoriModel currentItem = mKategoriList.get(position);
        String namaKategori = currentItem.getNama_kategori();

        TextFuntion textFuntion = new TextFuntion();
        //data kategori
        textFuntion.setTextDanNullData(holder.namaKategori, namaKategori);

    }

    @Override
    public int getItemCount() {
        return mKategoriList.size();
    }

    public class KategoriViewHolder extends RecyclerView.ViewHolder {
        public TextView namaKategori;

        KategoriViewHolder(@NonNull View itemView) {
            super(itemView);
            namaKategori = itemView.findViewById(R.id.tv_namaKategori);
        }
    }
}
