package com.cikarastudio.cikarajantungdesafix.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ForumModel implements Parcelable {

    public static final Creator<ForumModel> CREATOR = new Creator<ForumModel>() {
        @Override
        public ForumModel createFromParcel(Parcel in) {
            return new ForumModel(in);
        }

        @Override
        public ForumModel[] newArray(int size) {
            return new ForumModel[size];
        }
    };
    String id;
    String nama;
    String ket_forum;
    String poto;
    String status;
    String created_at;
    String updated_at;

    public ForumModel(String id, String nama, String ket_forum, String poto, String status, String created_at, String updated_at) {
        this.id = id;
        this.nama = nama;
        this.ket_forum = ket_forum;
        this.poto = poto;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    protected ForumModel(Parcel in) {
        id = in.readString();
        nama = in.readString();
        ket_forum = in.readString();
        poto = in.readString();
        status = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
    }

    public static Creator<ForumModel> getCREATOR() {
        return CREATOR;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKet_forum() {
        return ket_forum;
    }

    public void setKet_forum(String ket_forum) {
        this.ket_forum = ket_forum;
    }

    public String getPoto() {
        return poto;
    }

    public void setPoto(String poto) {
        this.poto = poto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        dest.writeString(nama);
        dest.writeString(ket_forum);
        dest.writeString(poto);
        dest.writeString(status);
        dest.writeString(created_at);
        dest.writeString(updated_at);
    }
}
