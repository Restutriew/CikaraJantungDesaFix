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
import com.cikarastudio.cikarajantungdesafix.model.PerangkatDesaModel;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PerangkatDesaAdapter extends RecyclerView.Adapter<PerangkatDesaAdapter.PerangkatDesaViewHolder> {

    private Context mContext;
    private ArrayList<PerangkatDesaModel> mPerangkatDesaList;

    public PerangkatDesaAdapter(Context mContext, ArrayList<PerangkatDesaModel> mPerangkatDesaList) {
        this.mContext = mContext;
        this.mPerangkatDesaList = mPerangkatDesaList;
    }

    @NonNull
    @Override
    public PerangkatDesaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_perangkat_desa, parent, false);
        return new PerangkatDesaAdapter.PerangkatDesaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PerangkatDesaViewHolder holder, int position) {
        PerangkatDesaModel currentItem = mPerangkatDesaList.get(position);
        String namaPerangkatDesa = currentItem.getNama_pegawai();
        String jabatanPerangkatDesa = currentItem.getJabatan();
        String photoPerangkatDesa = currentItem.getPhoto();

        TextFuntion textFuntion = new TextFuntion();
        //data diri
        textFuntion.setTextDanNullData(holder.tv_namaPerangkatDesa, namaPerangkatDesa);
        textFuntion.setTextDanNullData(holder.tv_jabatanPerangkatDesa, jabatanPerangkatDesa);

        String imageUrl = "https://puteran.cikarastudio.com/public/img/desa/staf/" + photoPerangkatDesa;
        Picasso.with(mContext.getApplicationContext()).load(imageUrl).fit().centerCrop().into(holder.img_photoperangkatDesa);

    }

    @Override
    public int getItemCount() {
        return mPerangkatDesaList.size();
    }

    public class PerangkatDesaViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_namaPerangkatDesa;
        public TextView tv_jabatanPerangkatDesa;
        public ImageView img_photoperangkatDesa;

        PerangkatDesaViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_namaPerangkatDesa = itemView.findViewById(R.id.tv_namaPerangkatDesa);
            tv_jabatanPerangkatDesa = itemView.findViewById(R.id.tv_jabatanPerangkatDesa);
            img_photoperangkatDesa = itemView.findViewById(R.id.img_photoperangkatDesa);

        }
    }
}
