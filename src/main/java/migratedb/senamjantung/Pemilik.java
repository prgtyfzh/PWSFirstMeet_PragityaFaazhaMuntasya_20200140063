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
@Table(name = "pemilik")
@NamedQueries({
    @NamedQuery(name = "Pemilik.findAll", query = "SELECT p FROM Pemilik p"),
    @NamedQuery(name = "Pemilik.findByIDPemilik", query = "SELECT p FROM Pemilik p WHERE p.iDPemilik = :iDPemilik"),
    @NamedQuery(name = "Pemilik.findByNamaPemilik", query = "SELECT p FROM Pemilik p WHERE p.namaPemilik = :namaPemilik"),
    @NamedQuery(name = "Pemilik.findByJenisKelamin", query = "SELECT p FROM Pemilik p WHERE p.jenisKelamin = :jenisKelamin"),
    @NamedQuery(name = "Pemilik.findByNoTelepon", query = "SELECT p FROM Pemilik p WHERE p.noTelepon = :noTelepon"),
    @NamedQuery(name = "Pemilik.findByAlamat", query = "SELECT p FROM Pemilik p WHERE p.alamat = :alamat")})
public class Pemilik implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_Pemilik")
    private String iDPemilik;
    @Column(name = "Nama_Pemilik")
    private String namaPemilik;
    @Column(name = "Jenis_Kelamin")
    private String jenisKelamin;
    @Column(name = "No_Telepon")
    private String noTelepon;
    @Column(name = "Alamat")
    private String alamat;
    @OneToMany(mappedBy = "iDPemilik")
    private Collection<Kucing> kucingCollection;

    public Pemilik() {
    }

    public Pemilik(String iDPemilik) {
        this.iDPemilik = iDPemilik;
    }

    public String getIDPemilik() {
        return iDPemilik;
    }

    public void setIDPemilik(String iDPemilik) {
        this.iDPemilik = iDPemilik;
    }

    public String getNamaPemilik() {
        return namaPemilik;
    }

    public void setNamaPemilik(String namaPemilik) {
        this.namaPemilik = namaPemilik;
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
        hash += (iDPemilik != null ? iDPemilik.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pemilik)) {
            return false;
        }
        Pemilik other = (Pemilik) object;
        if ((this.iDPemilik == null && other.iDPemilik != null) || (this.iDPemilik != null && !this.iDPemilik.equals(other.iDPemilik))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "migratedb.senamjantung.Pemilik[ iDPemilik=" + iDPemilik + " ]";
    }
    
}
