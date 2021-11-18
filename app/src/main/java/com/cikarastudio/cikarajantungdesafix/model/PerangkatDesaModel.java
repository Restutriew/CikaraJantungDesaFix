package com.cikarastudio.cikarajantungdesafix.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PerangkatDesaModel implements Parcelable {

    String id;
    String nama_pegawai;
    String nik;
    String nipd;
    String nip;
    String tempat_lahir;
    String tgl_lahir;
    String jk;
    String pendidikan;
    String agama;
    String golongan;
    String nosk_pengangkatan;
    String tglsk_pengangkatan;
    String nosk_pemberhentian;
    String tglsk_pemberhentian;
    String masa_jabatan;
    String jabatan;
    String status_pegawai;
    String created_at;
    String updated_at;
    String photo;

    public PerangkatDesaModel(String id, String nama_pegawai, String nik, String nipd, String nip, String tempat_lahir, String tgl_lahir, String jk, String pendidikan, String agama, String golongan, String nosk_pengangkatan, String tglsk_pengangkatan, String nosk_pemberhentian, String tglsk_pemberhentian, String masa_jabatan, String jabatan, String status_pegawai, String created_at, String updated_at, String photo) {
        this.id = id;
        this.nama_pegawai = nama_pegawai;
        this.nik = nik;
        this.nipd = nipd;
        this.nip = nip;
        this.tempat_lahir = tempat_lahir;
        this.tgl_lahir = tgl_lahir;
        this.jk = jk;
        this.pendidikan = pendidikan;
        this.agama = agama;
        this.golongan = golongan;
        this.nosk_pengangkatan = nosk_pengangkatan;
        this.tglsk_pengangkatan = tglsk_pengangkatan;
        this.nosk_pemberhentian = nosk_pemberhentian;
        this.tglsk_pemberhentian = tglsk_pemberhentian;
        this.masa_jabatan = masa_jabatan;
        this.jabatan = jabatan;
        this.status_pegawai = status_pegawai;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_pegawai() {
        return nama_pegawai;
    }

    public void setNama_pegawai(String nama_pegawai) {
        this.nama_pegawai = nama_pegawai;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNipd() {
        return nipd;
    }

    public void setNipd(String nipd) {
        this.nipd = nipd;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getTempat_lahir() {
        return tempat_lahir;
    }

    public void setTempat_lahir(String tempat_lahir) {
        this.tempat_lahir = tempat_lahir;
    }

    public String getTgl_lahir() {
        return tgl_lahir;
    }

    public void setTgl_lahir(String tgl_lahir) {
        this.tgl_lahir = tgl_lahir;
    }

    public String getJk() {
        return jk;
    }

    public void setJk(String jk) {
        this.jk = jk;
    }

    public String getPendidikan() {
        return pendidikan;
    }

    public void setPendidikan(String pendidikan) {
        this.pendidikan = pendidikan;
    }

    public String getAgama() {
        return agama;
    }

    public void setAgama(String agama) {
        this.agama = agama;
    }

    public String getGolongan() {
        return golongan;
    }

    public void setGolongan(String golongan) {
        this.golongan = golongan;
    }

    public String getNosk_pengangkatan() {
        return nosk_pengangkatan;
    }

    public void setNosk_pengangkatan(String nosk_pengangkatan) {
        this.nosk_pengangkatan = nosk_pengangkatan;
    }

    public String getTglsk_pengangkatan() {
        return tglsk_pengangkatan;
    }

    public void setTglsk_pengangkatan(String tglsk_pengangkatan) {
        this.tglsk_pengangkatan = tglsk_pengangkatan;
    }

    public String getNosk_pemberhentian() {
        return nosk_pemberhentian;
    }

    public void setNosk_pemberhentian(String nosk_pemberhentian) {
        this.nosk_pemberhentian = nosk_pemberhentian;
    }

    public String getTglsk_pemberhentian() {
        return tglsk_pemberhentian;
    }

    public void setTglsk_pemberhentian(String tglsk_pemberhentian) {
        this.tglsk_pemberhentian = tglsk_pemberhentian;
    }

    public String getMasa_jabatan() {
        return masa_jabatan;
    }

    public void setMasa_jabatan(String masa_jabatan) {
        this.masa_jabatan = masa_jabatan;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getStatus_pegawai() {
        return status_pegawai;
    }

    public void setStatus_pegawai(String status_pegawai) {
        this.status_pegawai = status_pegawai;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public static Creator<PerangkatDesaModel> getCREATOR() {
        return CREATOR;
    }

    protected PerangkatDesaModel(Parcel in) {
        id = in.readString();
        nama_pegawai = in.readString();
        nik = in.readString();
        nipd = in.readString();
        nip = in.readString();
        tempat_lahir = in.readString();
        tgl_lahir = in.readString();
        jk = in.readString();
        pendidikan = in.readString();
        agama = in.readString();
        golongan = in.readString();
        nosk_pengangkatan = in.readString();
        tglsk_pengangkatan = in.readString();
        nosk_pemberhentian = in.readString();
        tglsk_pemberhentian = in.readString();
        masa_jabatan = in.readString();
        jabatan = in.readString();
        status_pegawai = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        photo = in.readString();
    }

    public static final Creator<PerangkatDesaModel> CREATOR = new Creator<PerangkatDesaModel>() {
        @Override
        public PerangkatDesaModel createFromParcel(Parcel in) {
            return new PerangkatDesaModel(in);
        }

        @Override
        public PerangkatDesaModel[] newArray(int size) {
            return new PerangkatDesaModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nama_pegawai);
        dest.writeString(nik);
        dest.writeString(nipd);
        dest.writeString(nip);
        dest.writeString(tempat_lahir);
        dest.writeString(tgl_lahir);
        dest.writeString(jk);
        dest.writeString(pendidikan);
        dest.writeString(agama);
        dest.writeString(golongan);
        dest.writeString(nosk_pengangkatan);
        dest.writeString(tglsk_pengangkatan);
        dest.writeString(nosk_pemberhentian);
        dest.writeString(tglsk_pemberhentian);
        dest.writeString(masa_jabatan);
        dest.writeString(jabatan);
        dest.writeString(status_pegawai);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeString(photo);
    }
}
