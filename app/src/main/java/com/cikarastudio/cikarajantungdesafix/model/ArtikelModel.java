package com.cikarastudio.cikarajantungdesafix.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ArtikelModel implements Parcelable {

    String id;
    String user_id;
    String kategoriartikel_id;
    String judul_artikel;
    String slug;
    String isi_artikel;
    String view;
    String gambar_artikel;
    String created_at;
    String updated_at;

    public ArtikelModel(String id, String user_id, String kategoriartikel_id, String judul_artikel, String slug, String isi_artikel, String view, String gambar_artikel, String created_at, String updated_at) {
        this.id = id;
        this.user_id = user_id;
        this.kategoriartikel_id = kategoriartikel_id;
        this.judul_artikel = judul_artikel;
        this.slug = slug;
        this.isi_artikel = isi_artikel;
        this.view = view;
        this.gambar_artikel = gambar_artikel;
        this.created_at = created_at;
        this.updated_at = updated_at;
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

    public String getKategoriartikel_id() {
        return kategoriartikel_id;
    }

    public void setKategoriartikel_id(String kategoriartikel_id) {
        this.kategoriartikel_id = kategoriartikel_id;
    }

    public String getJudul_artikel() {
        return judul_artikel;
    }

    public void setJudul_artikel(String judul_artikel) {
        this.judul_artikel = judul_artikel;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getIsi_artikel() {
        return isi_artikel;
    }

    public void setIsi_artikel(String isi_artikel) {
        this.isi_artikel = isi_artikel;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getGambar_artikel() {
        return gambar_artikel;
    }

    public void setGambar_artikel(String gambar_artikel) {
        this.gambar_artikel = gambar_artikel;
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

    public static Creator<ArtikelModel> getCREATOR() {
        return CREATOR;
    }

    protected ArtikelModel(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        kategoriartikel_id = in.readString();
        judul_artikel = in.readString();
        slug = in.readString();
        isi_artikel = in.readString();
        view = in.readString();
        gambar_artikel = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
    }

    public static final Creator<ArtikelModel> CREATOR = new Creator<ArtikelModel>() {
        @Override
        public ArtikelModel createFromParcel(Parcel in) {
            return new ArtikelModel(in);
        }

        @Override
        public ArtikelModel[] newArray(int size) {
            return new ArtikelModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user_id);
        dest.writeString(kategoriartikel_id);
        dest.writeString(judul_artikel);
        dest.writeString(slug);
        dest.writeString(isi_artikel);
        dest.writeString(view);
        dest.writeString(gambar_artikel);
        dest.writeString(created_at);
        dest.writeString(updated_at);
    }
}
