package com.cikarastudio.cikarajantungdesafix.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProdukModel implements Parcelable {

    public static final Creator<ProdukModel> CREATOR = new Creator<ProdukModel>() {
        @Override
        public ProdukModel createFromParcel(Parcel in) {
            return new ProdukModel(in);
        }

        @Override
        public ProdukModel[] newArray(int size) {
            return new ProdukModel[size];
        }
    };
    String id;
    String lapak_id;
    String nama;
    String keterangan;
    String gambar;
    Integer harga;
    Integer dilihat;

    public ProdukModel(String id, String lapak_id, String nama, String keterangan, String gambar, Integer harga, Integer dilihat) {
        this.id = id;
        this.lapak_id = lapak_id;
        this.nama = nama;
        this.keterangan = keterangan;
        this.gambar = gambar;
        this.harga = harga;
        this.dilihat = dilihat;
    }

    protected ProdukModel(Parcel in) {
        id = in.readString();
        lapak_id = in.readString();
        nama = in.readString();
        keterangan = in.readString();
        gambar = in.readString();
        harga = in.readInt();
        dilihat = in.readInt();
    }

    public static Creator<ProdukModel> getCREATOR() {
        return CREATOR;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLapak_id() {
        return lapak_id;
    }

    public void setLapak_id(String lapak_id) {
        this.lapak_id = lapak_id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public Integer getHarga() {
        return harga;
    }

    public void setHarga(Integer harga) {
        this.harga = harga;
    }

    public Integer getDilihat() {
        return dilihat;
    }

    public void setDilihat(Integer dilihat) {
        this.dilihat = dilihat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(lapak_id);
        parcel.writeString(nama);
        parcel.writeString(keterangan);
        parcel.writeString(gambar);
        parcel.writeInt(harga);
        parcel.writeInt(dilihat);
    }
}
