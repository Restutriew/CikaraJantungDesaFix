package com.cikarastudio.cikarajantungdesafix.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SuratV2Model implements Parcelable {

    public static final Creator<SuratV2Model> CREATOR = new Creator<SuratV2Model>() {
        @Override
        public SuratV2Model createFromParcel(Parcel in) {
            return new SuratV2Model(in);
        }

        @Override
        public SuratV2Model[] newArray(int size) {
            return new SuratV2Model[size];
        }
    };

    String id;
    String user_id;
    String formatsurat_id;
    String status;
    String nomor_surat;
    String tgl_awal;
    String tgl_akhir;
    String created_at;
    String updated_at;
    String nama_surat;
    String kode;

    public SuratV2Model(String id, String user_id, String formatsurat_id, String status, String nomor_surat, String tgl_awal, String tgl_akhir, String created_at, String updated_at, String nama_surat, String kode) {
        this.id = id;
        this.user_id = user_id;
        this.formatsurat_id = formatsurat_id;
        this.status = status;
        this.nomor_surat = nomor_surat;
        this.tgl_awal = tgl_awal;
        this.tgl_akhir = tgl_akhir;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.nama_surat = nama_surat;
        this.kode = kode;
    }

    protected SuratV2Model(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        formatsurat_id = in.readString();
        status = in.readString();
        nomor_surat = in.readString();
        tgl_awal = in.readString();
        tgl_akhir = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        nama_surat = in.readString();
        kode = in.readString();
    }

    public static Creator<SuratV2Model> getCREATOR() {
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

    public String getFormatsurat_id() {
        return formatsurat_id;
    }

    public void setFormatsurat_id(String formatsurat_id) {
        this.formatsurat_id = formatsurat_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNomor_surat() {
        return nomor_surat;
    }

    public void setNomor_surat(String nomor_surat) {
        this.nomor_surat = nomor_surat;
    }

    public String getTgl_awal() {
        return tgl_awal;
    }

    public void setTgl_awal(String tgl_awal) {
        this.tgl_awal = tgl_awal;
    }

    public String getTgl_akhir() {
        return tgl_akhir;
    }

    public void setTgl_akhir(String tgl_akhir) {
        this.tgl_akhir = tgl_akhir;
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

    public String getNama_surat() {
        return nama_surat;
    }

    public void setNama_surat(String nama_surat) {
        this.nama_surat = nama_surat;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(user_id);
        parcel.writeString(formatsurat_id);
        parcel.writeString(status);
        parcel.writeString(nomor_surat);
        parcel.writeString(tgl_awal);
        parcel.writeString(tgl_akhir);
        parcel.writeString(created_at);
        parcel.writeString(updated_at);
        parcel.writeString(nama_surat);
        parcel.writeString(kode);
    }
}
