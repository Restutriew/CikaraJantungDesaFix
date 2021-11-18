package com.cikarastudio.cikarajantungdesafix.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.model.StatusAduanModel;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;

import java.util.ArrayList;

import kotlin._Assertions;

public class StatusAduanAdapter extends RecyclerView.Adapter<StatusAduanAdapter.StatusAduanViewHolder> {

    private Context mContext;
    private ArrayList<StatusAduanModel> mStatusAduanList;

    public StatusAduanAdapter(Context mContext, ArrayList<StatusAduanModel> mStatusAduanList) {
        this.mContext = mContext;
        this.mStatusAduanList = mStatusAduanList;
    }

    @NonNull
    @Override
    public StatusAduanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_aduan_proses, parent, false);
        return new StatusAduanAdapter.StatusAduanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusAduanViewHolder holder, int position) {
        StatusAduanModel currentItem = mStatusAduanList.get(position);
        String keyAduan = currentItem.getKey();
        String isiAduan = currentItem.getIsi();
        String statusAduan = currentItem.getStatus();

        Log.d("calpalnx", currentItem.getIsi());

        TextFuntion textFuntion = new TextFuntion();
        //data status aduan

        String res_key;

        if ("jk".equals(keyAduan)) {
            res_key = "jenis kelamin";
        }else if ("no_akta".equals(keyAduan)){
            res_key = "nomor akta kelahiran";
        }else if ("tgl_lahir".equals(keyAduan)){
            res_key = "tanggal lahir";
        }else if ("status_warganegara".equals(keyAduan)){
            res_key = "status kewarganegaraan";
        }else if ("tgl_akhirpaspor".equals(keyAduan)){
            res_key = "tanggal akhir paspor";
        }else if ("no_telp".equals(keyAduan)){
            res_key = "nomor telepon";
        }else if ("no_bukunikah".equals(keyAduan)){
            res_key = "nomor buku nikah";
        }else if ("tgl_perkawinan".equals(keyAduan)){
            res_key = "tanggal perkawinan";
        }else if ("tgl_perceraian".equals(keyAduan)){
            res_key = "tanggal perceraian";
        }else {
            res_key = keyAduan.replace("_", " ");
        }

        textFuntion.setTextDanNullData(holder.tv_keyAduan, res_key);
        textFuntion.setTextDanNullData(holder.tv_isiAduan, isiAduan);
        textFuntion.setTextDanNullData(holder.tv_statusAduan, statusAduan);

        if (position % 2 == 1) {
            holder.lb_itemAduan.setBackgroundColor(Color.WHITE);
        } else {
            holder.lb_itemAduan.setBackgroundResource(R.color.kuningprosespudar);
        }
    }

    @Override
    public int getItemCount() {
        return mStatusAduanList.size();
    }

    public class StatusAduanViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_keyAduan;
        public TextView tv_isiAduan;
        public TextView tv_statusAduan;
        public LinearLayout lb_itemAduan;

        StatusAduanViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_keyAduan = itemView.findViewById(R.id.tv_keyAduan);
            tv_isiAduan = itemView.findViewById(R.id.tv_isiAduan);
            tv_statusAduan = itemView.findViewById(R.id.tv_statusAduan);
            lb_itemAduan = itemView.findViewById(R.id.lb_itemAduan);
        }
    }
}
