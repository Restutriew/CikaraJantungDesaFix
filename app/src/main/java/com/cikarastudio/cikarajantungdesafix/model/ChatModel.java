package com.cikarastudio.cikarajantungdesafix.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatModel implements Parcelable {
    public static final Creator<ChatModel> CREATOR = new Creator<ChatModel>() {
        @Override
        public ChatModel createFromParcel(Parcel in) {
            return new ChatModel(in);
        }

        @Override
        public ChatModel[] newArray(int size) {
            return new ChatModel[size];
        }
    };
    String id;
    String user_id;
    String forum_id;
    String isi;
    String created_at;
    String updated_at;
    String nama_penduduk;

    public ChatModel(String id, String user_id, String forum_id, String isi, String created_at, String updated_at, String nama_penduduk) {
        this.id = id;
        this.user_id = user_id;
        this.forum_id = forum_id;
        this.isi = isi;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.nama_penduduk = nama_penduduk;
    }

    protected ChatModel(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        forum_id = in.readString();
        isi = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        nama_penduduk = in.readString();
    }

    public static Creator<ChatModel> getCREATOR() {
        return CREATOR;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getForum_id() {
        return forum_id;
    }

    public void setForum_id(String forum_id) {
        this.forum_id = forum_id;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getNama_penduduk() {
        return nama_penduduk;
    }

    public void setNama_penduduk(String nama_penduduk) {
        this.nama_penduduk = nama_penduduk;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user_id);
        dest.writeString(forum_id);
        dest.writeString(isi);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeString(nama_penduduk);
    }
}
