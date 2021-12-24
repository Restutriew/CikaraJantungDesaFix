package com.cikarastudio.cikarajantungdesafix.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cikarastudio.cikarajantungdesafix.R;
import com.cikarastudio.cikarajantungdesafix.model.ChatModel;
import com.cikarastudio.cikarajantungdesafix.template.kima.text.TextFuntion;

import java.util.ArrayList;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String id_user;
    String userIDChat;
    private Context mContext;
    private ArrayList<ChatModel> mChatList;

    public ChatAdapter(Context mContext, ArrayList<ChatModel> mChatList, String id_user) {
        this.mContext = mContext;
        this.mChatList = mChatList;
        this.id_user = id_user;
    }

    @Override
    public int getItemViewType(int position) {
        if (mChatList.get(position).getUser_id().equals(id_user)) {
            return 0;
        }
        return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view;
        if (viewType == 0) {
            view = layoutInflater.inflate(R.layout.item_chat_user, parent, false);
            return new ViewHolderUser(view);
        }
        view = layoutInflater.inflate(R.layout.item_chat, parent, false);
        return new ViewHolderChat(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatModel currentItem = mChatList.get(position);
        String idChat = currentItem.getId();
        userIDChat = currentItem.getUser_id();
        String forumIDChat = currentItem.getForum_id();
        String isiChat = currentItem.getIsi();
        String createdAtChat = currentItem.getCreated_at();
        String updatedAtChat = currentItem.getUpdated_at();
        String namaPendudukChat = currentItem.getNama_penduduk();

        String jam = createdAtChat.substring(11, 13);
        String menit = createdAtChat.substring(14, 16);

        String tanggal = createdAtChat.substring(8, 10);
        String bulan = createdAtChat.substring(5, 7);
        String tahun = createdAtChat.substring(0, 4);

        String resi_waktu = jam + "." + menit;
        String resi_tanggal = tanggal + "-" + bulan + "-" + tahun;

//        id_userChat = userIDChat;
        Log.d("calpalnx", "onBindViewHolder: " + userIDChat);

        TextFuntion textFuntion = new TextFuntion();
        //data diri


        if (mChatList.get(position).getUser_id().equals(id_user)) {
            ViewHolderUser viewHolderChatUser = (ViewHolderUser) holder;
            viewHolderChatUser.tv_isiUserChat.setText(isiChat);
            viewHolderChatUser.tv_waktuUserChat.setText(resi_waktu);
        } else {
            ViewHolderChat viewHolderChat = (ViewHolderChat) holder;
            textFuntion.setTextDanNullData(viewHolderChat.tv_namaChat, namaPendudukChat);
            viewHolderChat.tv_isiChat.setText(isiChat);
            viewHolderChat.tv_waktuChat.setText(resi_waktu);
        }

    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    class ViewHolderChat extends RecyclerView.ViewHolder {
        public TextView tv_namaChat;
        public TextView tv_isiChat;
        public TextView tv_waktuChat;

        ViewHolderChat(@NonNull View itemView) {
            super(itemView);
            tv_namaChat = itemView.findViewById(R.id.tv_namaChat);
            tv_isiChat = itemView.findViewById(R.id.tv_isiChat);
            tv_waktuChat = itemView.findViewById(R.id.tv_waktuChat);
        }
    }

    class ViewHolderUser extends RecyclerView.ViewHolder {
        public TextView tv_isiUserChat;
        public TextView tv_waktuUserChat;

        ViewHolderUser(@NonNull View itemView) {
            super(itemView);
            tv_isiUserChat = itemView.findViewById(R.id.tv_isiUserChat);
            tv_waktuUserChat = itemView.findViewById(R.id.tv_waktuUserChat);
        }
    }
}
