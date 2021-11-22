package com.cikarastudio.cikarajantungdesafix.model;

import android.os.Parcel;
import android.os.Parcelable;

public class KategoriArtikelModel implements Parcelable {

    String id;
    String nama_kategori;
    String keterangan;
    String created_at;
    String updated_at;

    public KategoriArtikelModel(String id, String nama_kategori, String keterangan, String created_at, String updated_at) {
        this.id = id;
        this.nama_kategori = nama_kategori;
        this.keterangan = keterangan;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }

    public void setNama_kategori(String nama_kategori) {
        this.nama_kategori = nama_kategori;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
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

    public static Creator<KategoriArtikelModel> getCREATOR() {
        return CREATOR;
    }

    protected KategoriArtikelModel(Parcel in) {
        id = in.readString();
        nama_kategori = in.readString();
        keterangan = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
    }

    public static final Creator<KategoriArtikelModel> CREATOR = new Creator<KategoriArtikelModel>() {
        @Override
        public KategoriArtikelModel createFromParcel(Parcel in) {
            return new KategoriArtikelModel(in);
        }

        @Override
        public KategoriArtikelModel[] newArray(int size) {
            return new KategoriArtikelModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(nama_kategori);
        parcel.writeString(keterangan);
        parcel.writeString(created_at);
        parcel.writeString(updated_at);
    }
}
