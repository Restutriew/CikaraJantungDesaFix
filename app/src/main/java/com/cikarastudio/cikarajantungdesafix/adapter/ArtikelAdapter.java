package com.cikarastudio.cikarajantungdesafix.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
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

public class ArtikelAdapter extends RecyclerView.Adapter<ArtikelAdapter.ArtikelViewHolder> {

    String judulArtikel, isiArtikel, imgArtikel, slugArtikel, namaKategoriArtikel, linkArtikel;

    private Context mContext;
    private ArrayList<ArtikelModel> mArtikelList;

    private ArtikelAdapter.OnItemClickCallback onItemClickCallback;

    public ArtikelAdapter(Context mContext, ArrayList<ArtikelModel> mArtikelList) {
        this.mContext = mContext;
        this.mArtikelList = mArtikelList;
    }

    public void setOnItemClickCallback(ArtikelAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ArtikelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_artikel_home, parent, false);
        return new ArtikelAdapter.ArtikelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtikelViewHolder holder, int position) {
        ArtikelModel currentItem = mArtikelList.get(position);
        judulArtikel = currentItem.getJudul_artikel();
        isiArtikel = currentItem.getIsi_artikel();
        imgArtikel = currentItem.getGambar_artikel();
        slugArtikel = currentItem.getSlug();
        namaKategoriArtikel = currentItem.getNama_kategori();

        linkArtikel = mContext.getString(R.string.linkArtikel);

        TextFuntion textFuntion = new TextFuntion();
        //data kategori
        textFuntion.setTextDanNullData(holder.tv_judulArtikel, judulArtikel);
        textFuntion.setTextDanNullData(holder.tv_namaKategoriArtikel, namaKategoriArtikel);

        String linkgambar = mContext.getString(R.string.linkGambar);
        String imageUrl = linkgambar + "pengaturan/artikel/" + imgArtikel;
        Picasso.with(mContext.getApplicationContext()).load(imageUrl).into(holder.img_gambarArtikel);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tv_isiArtikel.setText(Html.fromHtml(isiArtikel, Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tv_isiArtikel.setText(Html.fromHtml(isiArtikel));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(mArtikelList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        Integer limit;
        if (mArtikelList.size() < 5) {
            limit = mArtikelList.size();
        } else {
            limit = 5;
        }
        return limit;
    }

    public interface OnItemClickCallback {
        void onItemClicked(ArtikelModel data);
    }

    public class ArtikelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_judulArtikel;
        public TextView tv_isiArtikel;
        public TextView tv_namaKategoriArtikel;
        public ImageView img_gambarArtikel;
        public ImageView line_buttonShare;

        ArtikelViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_judulArtikel = itemView.findViewById(R.id.tv_judulArtikel);
            tv_isiArtikel = itemView.findViewById(R.id.tv_isiArtikel);
            tv_namaKategoriArtikel = itemView.findViewById(R.id.tv_namaKategoriArtikel);
            img_gambarArtikel = itemView.findViewById(R.id.img_gambarArtikel);
            line_buttonShare = itemView.findViewById(R.id.line_buttonShare);

            line_buttonShare.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.line_buttonShare:
                    // do your code
                    Intent share = new Intent(android.content.Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    share.putExtra(Intent.EXTRA_SUBJECT, judulArtikel);
                    share.putExtra(Intent.EXTRA_TEXT, linkArtikel + slugArtikel);
                    mContext.startActivity(Intent.createChooser(share, "Share Berita"));
                    break;
                default:
                    break;
            }
        }
    }
}
