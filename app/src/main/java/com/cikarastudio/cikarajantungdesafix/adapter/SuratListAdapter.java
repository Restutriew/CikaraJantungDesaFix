package com.cikarastudio.cikarajantungdesafix.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.model.SuratListModel;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;

import java.util.ArrayList;

public class SuratListAdapter extends RecyclerView.Adapter<SuratListAdapter.SuratListViewHolder> {

    private Context mContext;
    private ArrayList<SuratListModel> mSuratList;

    //metode Onclick
    private SuratListAdapter.OnItemClickCallback onItemClickCallback;

    public SuratListAdapter(Context mContext, ArrayList<SuratListModel> mSuratList) {
        this.mContext = mContext;
        this.mSuratList = mSuratList;
    }

    public void setOnItemClickCallback(SuratListAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    //setfilter
    public void setFilter(ArrayList<SuratListModel> dataFilter) {
        mSuratList = new ArrayList<>();
        mSuratList.addAll(dataFilter);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SuratListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_surat_list, parent, false);
        return new SuratListAdapter.SuratListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SuratListViewHolder holder, int position) {
        SuratListModel currentItem = mSuratList.get(position);
        String namaSurat = currentItem.getNama_surat();
        TextFuntion textFuntion = new TextFuntion();
        textFuntion.setTextDanNullData(holder.tv_namaSuratList, namaSurat);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(mSuratList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSuratList.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(SuratListModel data);
    }

    public class SuratListViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_namaSuratList;

        public SuratListViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_namaSuratList = itemView.findViewById(R.id.tv_namaSuratList);
        }
    }
}
