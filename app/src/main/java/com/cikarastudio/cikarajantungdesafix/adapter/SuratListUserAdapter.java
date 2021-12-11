package com.cikarastudio.cikarajantungdesafix.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.model.SuratV2Model;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SuratListUserAdapter extends RecyclerView.Adapter<SuratListUserAdapter.SuratListUserViewHolder> {

    private Context mContext;
    private ArrayList<SuratV2Model> mSuratUserList;

    private SuratListUserAdapter.OnItemClickCallback onItemClickCallback;
    private SuratListUserAdapter.OnDeleteClick onDeleteClick;


    public SuratListUserAdapter(Context mContext, ArrayList<SuratV2Model> mSuratUserList) {
        this.mContext = mContext;
        this.mSuratUserList = mSuratUserList;
    }

    public void setOnItemClickCallback(SuratListUserAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setOnDeleteClick(SuratListUserAdapter.OnDeleteClick onDeleteClick) {
        this.onDeleteClick = onDeleteClick;
    }

    //setfilter
    public void setFilter(ArrayList<SuratV2Model> dataFilter) {
        mSuratUserList = new ArrayList<>();
        mSuratUserList.addAll(dataFilter);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SuratListUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_surat_list_user, parent, false);
        return new SuratListUserAdapter.SuratListUserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SuratListUserViewHolder holder, int position) {
        SuratV2Model currentItem = mSuratUserList.get(position);
        String namaSurat = currentItem.getNama_surat();
        String nomorSurat = currentItem.getNomor_surat();
        String statusSurat = currentItem.getStatus();
        String created_at = currentItem.getCreated_at();

        String tanggal = created_at.substring(8, 10);
        String bulan = created_at.substring(5, 7);
        String tahun = created_at.substring(0, 4);

        String resi_tanggal = tanggal + "-" + bulan + "-" + tahun;

        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        Date date = null;
        try {
            date = inputFormat.parse(resi_tanggal);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String tanggalfix = outputFormat.format(date);

        if (statusSurat.equals("selesai")) {
            holder.tv_keteranganSelesai.setVisibility(View.VISIBLE);
        } else {
            holder.tv_keteranganSelesai.setVisibility(View.GONE);
        }

        TextFuntion textFuntion = new TextFuntion();
        textFuntion.setTextDanNullData(holder.tv_namaSuratListUser, namaSurat);
        textFuntion.setTextDanNullData(holder.tv_nomorSuratListUser, nomorSurat);
        textFuntion.setTextDanNullData(holder.tv_statusSuratListUser, statusSurat);
        textFuntion.setTextDanNullData(holder.tv_tanggalPengajuanSuratListUser, tanggalfix);

        if (statusSurat.equals("proses")) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickCallback.onItemClicked(mSuratUserList.get(holder.getAdapterPosition()));
                }
            });

            holder.img_buttonDots.setVisibility(View.VISIBLE);
        } else {
            holder.img_buttonDots.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return mSuratUserList.size();
    }

    public interface OnItemClickCallback {
        void onItemClicked(SuratV2Model data);
    }

    public interface OnDeleteClick {
        void onItemClicked(SuratV2Model data);
    }

    public class SuratListUserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        public TextView tv_tanggalPengajuanSuratListUser;
        public TextView tv_namaSuratListUser;
        public TextView tv_nomorSuratListUser;
        public TextView tv_statusSuratListUser;
        public TextView tv_keteranganSelesai;
        public ImageView img_buttonDots;


        public SuratListUserViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_tanggalPengajuanSuratListUser = itemView.findViewById(R.id.tv_tanggalPengajuanSuratListUser);
            tv_namaSuratListUser = itemView.findViewById(R.id.tv_namaSuratListUser);
            tv_nomorSuratListUser = itemView.findViewById(R.id.tv_nomorSuratListUser);
            tv_statusSuratListUser = itemView.findViewById(R.id.tv_statusSuratListUser);
            tv_keteranganSelesai = itemView.findViewById(R.id.tv_keteranganSelesai);

            img_buttonDots = itemView.findViewById(R.id.img_buttonDots);

            img_buttonDots.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Log.d("calpalnx", "onClick: " + getAdapterPosition());
            showPopupMenu(view);
        }

        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_popup_edit:
                    Log.d("calpalnx", "onMenuItemClick: edit" + getAdapterPosition());
                    onItemClickCallback.onItemClicked(mSuratUserList.get(getAdapterPosition()));
                    return true;
                case R.id.action_popup_delete:
                    Log.d("calpalnx", "onMenuItemClick: delete" + getAdapterPosition());
                    onDeleteClick.onItemClicked(mSuratUserList.get(getAdapterPosition()));
//                    ProdukModel currentItem = mProdukList.get(getAdapterPosition());
//                    String id = currentItem.getId();
//                    Log.d("calpalnx", "onMenuItemClick: delete" + getAdapterPosition());
//                    LapakFragment lapakFragment = new LapakFragment();
//                    lapakFragment.hapusData(id);
                    return true;
            }
            return false;
        }
    }

}
