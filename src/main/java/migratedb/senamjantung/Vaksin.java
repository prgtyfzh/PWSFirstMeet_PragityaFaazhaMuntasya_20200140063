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
@Table(name = "vaksin")
@NamedQueries({
    @NamedQuery(name = "Vaksin.findAll", query = "SELECT v FROM Vaksin v"),
    @NamedQuery(name = "Vaksin.findByIDVaksin", query = "SELECT v FROM Vaksin v WHERE v.iDVaksin = :iDVaksin"),
    @NamedQuery(name = "Vaksin.findByNamaVaksin", query = "SELECT v FROM Vaksin v WHERE v.namaVaksin = :namaVaksin"),
    @NamedQuery(name = "Vaksin.findByDosis", query = "SELECT v FROM Vaksin v WHERE v.dosis = :dosis")})
public class Vaksin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_Vaksin")
    private String iDVaksin;
    @Column(name = "Nama_Vaksin")
    private String namaVaksin;
    @Column(name = "Dosis")
    private Integer dosis;
    @OneToMany(mappedBy = "iDVaksin")
    private Collection<RekamMedis> rekamMedisCollection;

    public Vaksin() {
    }

    public Vaksin(String iDVaksin) {
        this.iDVaksin = iDVaksin;
    }

    public String getIDVaksin() {
        return iDVaksin;
    }

    public void setIDVaksin(String iDVaksin) {
        this.iDVaksin = iDVaksin;
    }

    public String getNamaVaksin() {
        return namaVaksin;
    }

    public void setNamaVaksin(String namaVaksin) {
        this.namaVaksin = namaVaksin;
    }

    public Integer getDosis() {
        return dosis;
    }

    public void setDosis(Integer dosis) {
        this.dosis = dosis;
    }

    public Collection<RekamMedis> getRekamMedisCollection() {
        return rekamMedisCollection;
    }

    public void setRekamMedisCollection(Collection<RekamMedis> rekamMedisCollection) {
        this.rekamMedisCollection = rekamMedisCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDVaksin != null ? iDVaksin.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vaksin)) {
            return false;
        }
        Vaksin other = (Vaksin) object;
        if ((this.iDVaksin == null && other.iDVaksin != null) || (this.iDVaksin != null && !this.iDVaksin.equals(other.iDVaksin))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "migratedb.senamjantung.Vaksin[ iDVaksin=" + iDVaksin + " ]";
    }
    
}
