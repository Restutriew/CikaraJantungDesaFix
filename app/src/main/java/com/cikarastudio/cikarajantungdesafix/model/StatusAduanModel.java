package com.cikarastudio.cikarajantungdesafix.model;

import android.os.Parcel;
import android.os.Parcelable;

public class StatusAduanModel implements Parcelable {
    public static final Creator<StatusAduanModel> CREATOR = new Creator<StatusAduanModel>() {
        @Override
        public StatusAduanModel createFromParcel(Parcel in) {
            return new StatusAduanModel(in);
        }

        @Override
        public StatusAduanModel[] newArray(int size) {
            return new StatusAduanModel[size];
        }
    };
    String id;
    String user_id;
    String key;
    String isi;
    String status;
    String notif;
    String created_at;
    String updated_at;

    public StatusAduanModel(String id, String user_id, String key, String isi, String status, String notif, String created_at, String updated_at) {
        this.id = id;
        this.user_id = user_id;
        this.key = key;
        this.isi = isi;
        this.status = status;
        this.notif = notif;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    protected StatusAduanModel(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        key = in.readString();
        isi = in.readString();
        status = in.readString();
        notif = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
    }

    public static Creator<StatusAduanModel> getCREATOR() {
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotif() {
        return notif;
    }

    public void setNotif(String notif) {
        this.notif = notif;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user_id);
        dest.writeString(key);
        dest.writeString(isi);
        dest.writeString(status);
        dest.writeString(notif);
        dest.writeString(created_at);
        dest.writeString(updated_at);
    }
}
