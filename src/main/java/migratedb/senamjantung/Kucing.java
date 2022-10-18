/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package migratedb.senamjantung;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "kucing")
@NamedQueries({
    @NamedQuery(name = "Kucing.findAll", query = "SELECT k FROM Kucing k"),
    @NamedQuery(name = "Kucing.findByIDKucing", query = "SELECT k FROM Kucing k WHERE k.iDKucing = :iDKucing"),
    @NamedQuery(name = "Kucing.findByNamaKucing", query = "SELECT k FROM Kucing k WHERE k.namaKucing = :namaKucing"),
    @NamedQuery(name = "Kucing.findByUmur", query = "SELECT k FROM Kucing k WHERE k.umur = :umur"),
    @NamedQuery(name = "Kucing.findByJenisKelamin", query = "SELECT k FROM Kucing k WHERE k.jenisKelamin = :jenisKelamin"),
    @NamedQuery(name = "Kucing.findByRas", query = "SELECT k FROM Kucing k WHERE k.ras = :ras"),
    @NamedQuery(name = "Kucing.findByWarnaKucing", query = "SELECT k FROM Kucing k WHERE k.warnaKucing = :warnaKucing")})
public class Kucing implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_Kucing")
    private String iDKucing;
    @Column(name = "Nama_Kucing")
    private String namaKucing;
    @Column(name = "Umur")
    private String umur;
    @Column(name = "Jenis_Kelamin")
    private String jenisKelamin;
    @Column(name = "Ras")
    private String ras;
    @Column(name = "Warna_Kucing")
    private String warnaKucing;
    @JoinColumn(name = "ID_Pemilik", referencedColumnName = "ID_Pemilik")
    @ManyToOne
    private Pemilik iDPemilik;
    @JoinColumn(name = "ID_Pegawai", referencedColumnName = "ID_Pegawai")
    @ManyToOne
    private Pegawai iDPegawai;
    @JoinColumn(name = "ID_Kucing", referencedColumnName = "ID_Kucing", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private RekamMedis rekamMedis;

    public Kucing() {
    }

    public Kucing(String iDKucing) {
        this.iDKucing = iDKucing;
    }

    public String getIDKucing() {
        return iDKucing;
    }

    public void setIDKucing(String iDKucing) {
        this.iDKucing = iDKucing;
    }

    public String getNamaKucing() {
        return namaKucing;
    }

    public void setNamaKucing(String namaKucing) {
        this.namaKucing = namaKucing;
    }

    public String getUmur() {
        return umur;
    }

    public void setUmur(String umur) {
        this.umur = umur;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getRas() {
        return ras;
    }

    public void setRas(String ras) {
        this.ras = ras;
    }

    public String getWarnaKucing() {
        return warnaKucing;
    }

    public void setWarnaKucing(String warnaKucing) {
        this.warnaKucing = warnaKucing;
    }

    public Pemilik getIDPemilik() {
        return iDPemilik;
    }

    public void setIDPemilik(Pemilik iDPemilik) {
        this.iDPemilik = iDPemilik;
    }

    public Pegawai getIDPegawai() {
        return iDPegawai;
    }

    public void setIDPegawai(Pegawai iDPegawai) {
        this.iDPegawai = iDPegawai;
    }

    public RekamMedis getRekamMedis() {
        return rekamMedis;
    }

    public void setRekamMedis(RekamMedis rekamMedis) {
        this.rekamMedis = rekamMedis;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDKucing != null ? iDKucing.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Kucing)) {
            return false;
        }
        Kucing other = (Kucing) object;
        if ((this.iDKucing == null && other.iDKucing != null) || (this.iDKucing != null && !this.iDKucing.equals(other.iDKucing))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "migratedb.senamjantung.Kucing[ iDKucing=" + iDKucing + " ]";
    }
    
}
