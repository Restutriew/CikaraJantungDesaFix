package com.cikarastudio.cikarajantungdesafix.model;

import android.os.Parcel;
import android.os.Parcelable;

public class KategoriModel implements Parcelable {

    public static final Creator<KategoriModel> CREATOR = new Creator<KategoriModel>() {
        @Override
        public KategoriModel createFromParcel(Parcel in) {
            return new KategoriModel(in);
        }

        @Override
        public KategoriModel[] newArray(int size) {
            return new KategoriModel[size];
        }
    };
    String id;
    String nama_kategori;

    public KategoriModel(String id, String nama_kategori) {
        this.id = id;
        this.nama_kategori = nama_kategori;
    }

    protected KategoriModel(Parcel in) {
        id = in.readString();
        nama_kategori = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nama_kategori);
    }
}
