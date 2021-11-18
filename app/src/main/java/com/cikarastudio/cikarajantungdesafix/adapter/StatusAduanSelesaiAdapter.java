package com.cikarastudio.cikarajantungdesafix.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.model.ProdukModel;
import com.cikarastudio.cikarajantungdesafix.model.StatusAduanModel;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;

import java.util.ArrayList;

public class StatusAduanSelesaiAdapter extends RecyclerView.Adapter<StatusAduanSelesaiAdapter.StatusAduanSelesaiViewHolder> {

    private Context mContext;
    private ArrayList<StatusAduanModel> mStatusAduanList;

    //metode Onclick
    private StatusAduanSelesaiAdapter.OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(StatusAduanSelesaiAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(StatusAduanModel data);
    }

    public StatusAduanSelesaiAdapter(Context mContext, ArrayList<StatusAduanModel> mStatusAduanList) {
        this.mContext = mContext;
        this.mStatusAduanList = mStatusAduanList;
    }

    @NonNull
    @Override
    public StatusAduanSelesaiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_aduan_selesai, parent, false);
        return new StatusAduanSelesaiAdapter.StatusAduanSelesaiViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusAduanSelesaiViewHolder holder, int position) {
        StatusAduanModel currentItem = mStatusAduanList.get(position);
        String keyAduan = currentItem.getKey();
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

        textFuntion.setTextDanNullData(holder.tv_keyAduanSelesai, res_key);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(mStatusAduanList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStatusAduanList.size();
    }

    public class StatusAduanSelesaiViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_keyAduanSelesai;

        StatusAduanSelesaiViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_keyAduanSelesai = itemView.findViewById(R.id.tv_keyAduanSelesai);
        }
    }
}
