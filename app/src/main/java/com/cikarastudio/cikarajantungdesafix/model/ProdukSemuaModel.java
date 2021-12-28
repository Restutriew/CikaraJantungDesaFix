package com.cikarastudio.cikarajantungdesafix.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProdukSemuaModel implements Parcelable {

    public static final Creator<ProdukSemuaModel> CREATOR = new Creator<ProdukSemuaModel>() {
        @Override
        public ProdukSemuaModel createFromParcel(Parcel in) {
            return new ProdukSemuaModel(in);
        }

        @Override
        public ProdukSemuaModel[] newArray(int size) {
            return new ProdukSemuaModel[size];
        }
    };
    String id;
    String lapak_id;
    String nama;
    String keterangan;
    String gambar;
    Integer harga;
    Integer dilihat;
    String created_at;
    String updated_at;
    String link;

    public ProdukSemuaModel(String id, String lapak_id, String nama, String keterangan, String gambar, Integer harga, Integer dilihat, String created_at, String updated_at, String link) {
        this.id = id;
        this.lapak_id = lapak_id;
        this.nama = nama;
        this.keterangan = keterangan;
        this.gambar = gambar;
        this.harga = harga;
        this.dilihat = dilihat;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.link = link;
    }

    protected ProdukSemuaModel(Parcel in) {
        id = in.readString();
        lapak_id = in.readString();
        nama = in.readString();
        keterangan = in.readString();
        gambar = in.readString();
        harga = in.readInt();
        dilihat = in.readInt();
        created_at = in.readString();
        updated_at = in.readString();
        link = in.readString();
    }

    public static Creator<ProdukSemuaModel> getCREATOR() {
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(lapak_id);
        dest.writeString(nama);
        dest.writeString(keterangan);
        dest.writeString(gambar);
        dest.writeInt(harga);
        dest.writeInt(dilihat);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeString(link);
    }
}
