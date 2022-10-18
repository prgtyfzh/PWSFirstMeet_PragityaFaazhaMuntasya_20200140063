/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package migratedb.senamjantung;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "pegawai")
@NamedQueries({
    @NamedQuery(name = "Pegawai.findAll", query = "SELECT p FROM Pegawai p"),
    @NamedQuery(name = "Pegawai.findByIDPegawai", query = "SELECT p FROM Pegawai p WHERE p.iDPegawai = :iDPegawai"),
    @NamedQuery(name = "Pegawai.findByNamaPegawai", query = "SELECT p FROM Pegawai p WHERE p.namaPegawai = :namaPegawai"),
    @NamedQuery(name = "Pegawai.findByJenisKelamin", query = "SELECT p FROM Pegawai p WHERE p.jenisKelamin = :jenisKelamin"),
    @NamedQuery(name = "Pegawai.findByNoTelepon", query = "SELECT p FROM Pegawai p WHERE p.noTelepon = :noTelepon"),
    @NamedQuery(name = "Pegawai.findByAlamat", query = "SELECT p FROM Pegawai p WHERE p.alamat = :alamat")})
public class Pegawai implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_Pegawai")
    private String iDPegawai;
    @Column(name = "Nama_Pegawai")
    private String namaPegawai;
    @Column(name = "Jenis_Kelamin")
    private String jenisKelamin;
    @Column(name = "No_Telepon")
    private String noTelepon;
    @Column(name = "Alamat")
    private String alamat;
    @OneToMany(mappedBy = "iDPegawai")
    private Collection<Kucing> kucingCollection;

    public Pegawai() {
    }

    public Pegawai(String iDPegawai) {
        this.iDPegawai = iDPegawai;
    }

    public String getIDPegawai() {
        return iDPegawai;
    }

    public void setIDPegawai(String iDPegawai) {
        this.iDPegawai = iDPegawai;
    }

    public String getNamaPegawai() {
        return namaPegawai;
    }

    public void setNamaPegawai(String namaPegawai) {
        this.namaPegawai = namaPegawai;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public Collection<Kucing> getKucingCollection() {
        return kucingCollection;
    }

    public void setKucingCollection(Collection<Kucing> kucingCollection) {
        this.kucingCollection = kucingCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDPegawai != null ? iDPegawai.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pegawai)) {
            return false;
        }
        Pegawai other = (Pegawai) object;
        if ((this.iDPegawai == null && other.iDPegawai != null) || (this.iDPegawai != null && !this.iDPegawai.equals(other.iDPegawai))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "migratedb.senamjantung.Pegawai[ iDPegawai=" + iDPegawai + " ]";
    }
    
}
