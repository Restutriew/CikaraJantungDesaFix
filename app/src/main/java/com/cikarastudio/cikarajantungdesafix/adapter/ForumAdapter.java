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
import com.cikarastudio.cikarajantungdesafix.model.ForumModel;
import com.cikarastudio.cikarajantungdesafix.model.ProdukModel;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ForumViewHolder> {

    private Context mContext;
    private ArrayList<ForumModel> mForumList;

    private ForumAdapter.OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(ForumAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public interface OnItemClickCallback {
        void onItemClicked(ForumModel data);
    }

    public ForumAdapter(Context mContext, ArrayList<ForumModel> mForumList) {
        this.mContext = mContext;
        this.mForumList = mForumList;
    }

    //setfilter
    public void setFilter(ArrayList<ForumModel> dataFilter) {
        mForumList = new ArrayList<>();
        mForumList.addAll(dataFilter);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_forum, parent, false);
        return new ForumAdapter.ForumViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ForumViewHolder holder, int position) {
        ForumModel currentItem = mForumList.get(position);
        String namaForum = currentItem.getNama();
        String keteranganForum = currentItem.getKet_forum();
        String gambarForum = currentItem.getPoto();

        TextFuntion textFuntion = new TextFuntion();
        //data diri
        textFuntion.setTextDanNullData(holder.tv_judulForum, namaForum);
        textFuntion.setTextDanNullData(holder.tv_keteranganForum, keteranganForum);


        String imageUrl = "https://puteran.cikarastudio.com/public/img/layanan/forum/" + gambarForum;
        Picasso.with(mContext.getApplicationContext()).load(imageUrl).fit().centerCrop().into(holder.img_gambarForum);
    }

    @Override
    public int getItemCount() {
        return mForumList.size();
    }

    public class ForumViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_judulForum;
        public TextView tv_keteranganForum;
        public ImageView img_gambarForum;

        ForumViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_judulForum = itemView.findViewById(R.id.tv_judulForum);
            tv_keteranganForum = itemView.findViewById(R.id.tv_keteranganForum);
            img_gambarForum = itemView.findViewById(R.id.img_gambarForum);
        }
    }
}
