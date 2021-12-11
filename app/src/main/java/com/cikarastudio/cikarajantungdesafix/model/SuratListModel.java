package com.cikarastudio.cikarajantungdesafix.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SuratListModel implements Parcelable {

    public static final Creator<SuratListModel> CREATOR = new Creator<SuratListModel>() {
        @Override
        public SuratListModel createFromParcel(Parcel in) {
            return new SuratListModel(in);
        }

        @Override
        public SuratListModel[] newArray(int size) {
            return new SuratListModel[size];
        }
    };
    String id;
    String kode;
    String klasifikasisurat_id;
    String nama_surat;
    String nilai_masaberlaku;
    String status_masaberlaku;
    String layanan_mandiri;
    String file_surat;
    String kategori;
    String created_at;
    String updated_at;

    public SuratListModel(String id, String kode, String klasifikasisurat_id, String nama_surat, String nilai_masaberlaku, String status_masaberlaku, String layanan_mandiri, String file_surat, String kategori, String created_at, String updated_at) {
        this.id = id;
        this.kode = kode;
        this.klasifikasisurat_id = klasifikasisurat_id;
        this.nama_surat = nama_surat;
        this.nilai_masaberlaku = nilai_masaberlaku;
        this.status_masaberlaku = status_masaberlaku;
        this.layanan_mandiri = layanan_mandiri;
        this.file_surat = file_surat;
        this.kategori = kategori;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    protected SuratListModel(Parcel in) {
        id = in.readString();
        kode = in.readString();
        klasifikasisurat_id = in.readString();
        nama_surat = in.readString();
        nilai_masaberlaku = in.readString();
        status_masaberlaku = in.readString();
        layanan_mandiri = in.readString();
        file_surat = in.readString();
        kategori = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
    }

    public static Creator<SuratListModel> getCREATOR() {
        return CREATOR;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getKlasifikasisurat_id() {
        return klasifikasisurat_id;
    }

    public void setKlasifikasisurat_id(String klasifikasisurat_id) {
        this.klasifikasisurat_id = klasifikasisurat_id;
    }

    public String getNama_surat() {
        return nama_surat;
    }

    public void setNama_surat(String nama_surat) {
        this.nama_surat = nama_surat;
    }

    public String getNilai_masaberlaku() {
        return nilai_masaberlaku;
    }

    public void setNilai_masaberlaku(String nilai_masaberlaku) {
        this.nilai_masaberlaku = nilai_masaberlaku;
    }

    public String getStatus_masaberlaku() {
        return status_masaberlaku;
    }

    public void setStatus_masaberlaku(String status_masaberlaku) {
        this.status_masaberlaku = status_masaberlaku;
    }

    public String getLayanan_mandiri() {
        return layanan_mandiri;
    }

    public void setLayanan_mandiri(String layanan_mandiri) {
        this.layanan_mandiri = layanan_mandiri;
    }

    public String getFile_surat() {
        return file_surat;
    }

    public void setFile_surat(String file_surat) {
        this.file_surat = file_surat;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
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
        dest.writeString(kode);
        dest.writeString(klasifikasisurat_id);
        dest.writeString(nama_surat);
        dest.writeString(nilai_masaberlaku);
        dest.writeString(status_masaberlaku);
        dest.writeString(layanan_mandiri);
        dest.writeString(file_surat);
        dest.writeString(kategori);
        dest.writeString(created_at);
        dest.writeString(updated_at);
    }
}
