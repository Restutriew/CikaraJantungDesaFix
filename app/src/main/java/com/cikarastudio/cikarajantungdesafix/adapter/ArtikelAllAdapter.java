package com.cikarastudio.cikarajantungdesafix.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.model.ArtikelModel;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArtikelAllAdapter extends RecyclerView.Adapter<ArtikelAllAdapter.ArtikelAllViewHolder> {

    private Context mContext;
    private ArrayList<ArtikelModel> mArtikelAllList;

    private ArtikelAllAdapter.OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(ArtikelAllAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(ArtikelModel data);
    }

    public ArtikelAllAdapter(Context mContext, ArrayList<ArtikelModel> mArtikelAllList) {
        this.mContext = mContext;
        this.mArtikelAllList = mArtikelAllList;
    }

    @NonNull
    @Override
    public ArtikelAllViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_artikel, parent, false);
        return new ArtikelAllAdapter.ArtikelAllViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtikelAllViewHolder holder, int position) {
        ArtikelModel currentItem = mArtikelAllList.get(position);
        String judulArtikel = currentItem.getJudul_artikel();
        String isiArtikel = currentItem.getIsi_artikel();
        String imgArtikel = currentItem.getGambar_artikel();

        TextFuntion textFuntion = new TextFuntion();
        //data kategori
        textFuntion.setTextDanNullData(holder.tv_judulArtikel, judulArtikel);

        String linkgambar = mContext.getString(R.string.linkGambar);
        Log.d("calpalnx", "onBindViewHolder: " + linkgambar);
        String imageUrl = linkgambar + "pengaturan/artikel/" + imgArtikel;
        Picasso.with(mContext.getApplicationContext()).load(imageUrl).fit().centerCrop().into(holder.img_gambarArtikel);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tv_isiArtikel.setText(Html.fromHtml(isiArtikel, Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tv_isiArtikel.setText(Html.fromHtml(isiArtikel));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(mArtikelAllList.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mArtikelAllList.size();
    }

    public class ArtikelAllViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_judulArtikel;
        public TextView tv_isiArtikel;
        public ImageView img_gambarArtikel;

        public ArtikelAllViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_judulArtikel = itemView.findViewById(R.id.tv_judulArtikel);
            tv_isiArtikel = itemView.findViewById(R.id.tv_isiArtikel);
            img_gambarArtikel = itemView.findViewById(R.id.img_gambarArtikel);
        }
    }
}
