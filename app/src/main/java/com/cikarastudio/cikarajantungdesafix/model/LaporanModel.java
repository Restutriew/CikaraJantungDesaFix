package com.cikarastudio.cikarajantungdesafix.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LaporanModel implements Parcelable {

    public static final Creator<LaporanModel> CREATOR = new Creator<LaporanModel>() {
        @Override
        public LaporanModel createFromParcel(Parcel in) {
            return new LaporanModel(in);
        }

        @Override
        public LaporanModel[] newArray(int size) {
            return new LaporanModel[size];
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
    String waktu;
    String tanggal;
    String profile_photo_path;
    String nama_penduduk;
    String datalike;
    String jumlahlike;
    String statusLike;

    public LaporanModel(String id, String user_id, String isi, String kategori, String status, String tanggapan, String identitas, String posting,
                        String photo, String waktu, String tanggal, String profile_photo_path, String nama_penduduk, String datalike, String jumlahlike, String statusLike) {
        this.id = id;
        this.user_id = user_id;
        this.isi = isi;
        this.kategori = kategori;
        this.status = status;
        this.tanggapan = tanggapan;
        this.identitas = identitas;
        this.posting = posting;
        this.photo = photo;
        this.waktu = waktu;
        this.tanggal = tanggal;
        this.profile_photo_path = profile_photo_path;
        this.nama_penduduk = nama_penduduk;
        this.datalike = datalike;
        this.jumlahlike = jumlahlike;
        this.statusLike = statusLike;
    }

    protected LaporanModel(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        isi = in.readString();
        kategori = in.readString();
        status = in.readString();
        tanggapan = in.readString();
        identitas = in.readString();
        posting = in.readString();
        photo = in.readString();
        waktu = in.readString();
        tanggal = in.readString();
        profile_photo_path = in.readString();
        nama_penduduk = in.readString();
        datalike = in.readString();
        jumlahlike = in.readString();
        statusLike = in.readString();
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

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getProfile_photo_path() {
        return profile_photo_path;
    }

    public void setProfile_photo_path(String profile_photo_path) {
        this.profile_photo_path = profile_photo_path;
    }

    public String getNama_penduduk() {
        return nama_penduduk;
    }

    public void setNama_penduduk(String nama_penduduk) {
        this.nama_penduduk = nama_penduduk;
    }


    public String getDatalike() {
        return datalike;
    }

    public void setDatalike(String datalike) {
        this.datalike = datalike;
    }

    public String getJumlahlike() {
        return jumlahlike;
    }

    public void setJumlahlike(String jumlahlike) {
        this.jumlahlike = jumlahlike;
    }

    public String getStatusLike() {
        return statusLike;
    }

    public void setStatusLike(String statusLike) {
        this.statusLike = statusLike;
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
        dest.writeString(waktu);
        dest.writeString(tanggal);
        dest.writeString(profile_photo_path);
        dest.writeString(nama_penduduk);
        dest.writeString(datalike);
        dest.writeString(jumlahlike);
        dest.writeString(statusLike);
    }
}