package com.cikarastudio.cikarajantungdesafix.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LaporanUserModel implements Parcelable {
    public static final Creator<LaporanUserModel> CREATOR = new Creator<LaporanUserModel>() {
        @Override
        public LaporanUserModel createFromParcel(Parcel in) {
            return new LaporanUserModel(in);
        }

        @Override
        public LaporanUserModel[] newArray(int size) {
            return new LaporanUserModel[size];
        }
    };
    String id;
    String user_id;
    String isi;
    String kategori;
    String status;
    String tanggapan;
    String identitas;
    String posting;
    String photo;
    String created_at;
    String updated_at;

    public LaporanUserModel(String id, String user_id, String isi, String kategori, String status, String tanggapan, String identitas, String posting, String photo, String created_at, String updated_at) {
        this.id = id;
        this.user_id = user_id;
        this.isi = isi;
        this.kategori = kategori;
        this.status = status;
        this.tanggapan = tanggapan;
        this.identitas = identitas;
        this.posting = posting;
        this.photo = photo;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    protected LaporanUserModel(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        isi = in.readString();
        kategori = in.readString();
        status = in.readString();
        tanggapan = in.readString();
        identitas = in.readString();
        posting = in.readString();
        photo = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
    }

    public static Creator<LaporanUserModel> getCREATOR() {
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

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTanggapan() {
        return tanggapan;
    }

    public void setTanggapan(String tanggapan) {
        this.tanggapan = tanggapan;
    }

    public String getIdentitas() {
        return identitas;
    }

    public void setIdentitas(String identitas) {
        this.identitas = identitas;
    }

    public String getPosting() {
        return posting;
    }

    public void setPosting(String posting) {
        this.posting = posting;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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
        dest.writeString(isi);
        dest.writeString(kategori);
        dest.writeString(status);
        dest.writeString(tanggapan);
        dest.writeString(identitas);
        dest.writeString(posting);
        dest.writeString(photo);
        dest.writeString(created_at);
        dest.writeString(updated_at);
    }
}
