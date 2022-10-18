/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package migratedb.senamjantung;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "rekam_medis")
@NamedQueries({
    @NamedQuery(name = "RekamMedis.findAll", query = "SELECT r FROM RekamMedis r"),
    @NamedQuery(name = "RekamMedis.findByIdRm", query = "SELECT r FROM RekamMedis r WHERE r.idRm = :idRm"),
    @NamedQuery(name = "RekamMedis.findByIDKucing", query = "SELECT r FROM RekamMedis r WHERE r.iDKucing = :iDKucing"),
    @NamedQuery(name = "RekamMedis.findByVaksinPertama", query = "SELECT r FROM RekamMedis r WHERE r.vaksinPertama = :vaksinPertama"),
    @NamedQuery(name = "RekamMedis.findByVaksinBerikutnya", query = "SELECT r FROM RekamMedis r WHERE r.vaksinBerikutnya = :vaksinBerikutnya")})
public class RekamMedis implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_RM")
    private String idRm;
    @Column(name = "ID_Kucing")
    private String iDKucing;
    @Column(name = "Vaksin_Pertama")
    private String vaksinPertama;
    @Column(name = "Vaksin_Berikutnya")
    private String vaksinBerikutnya;
    @JoinColumn(name = "ID_Vaksin", referencedColumnName = "ID_Vaksin")
    @ManyToOne
    private Vaksin iDVaksin;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "rekamMedis")
    private Kucing kucing;

    public RekamMedis() {
    }

    public RekamMedis(String idRm) {
        this.idRm = idRm;
    }

    public String getIdRm() {
        return idRm;
    }

    public void setIdRm(String idRm) {
        this.idRm = idRm;
    }

    public String getIDKucing() {
        return iDKucing;
    }

    public void setIDKucing(String iDKucing) {
        this.iDKucing = iDKucing;
    }

    public String getVaksinPertama() {
        return vaksinPertama;
    }

    public void setVaksinPertama(String vaksinPertama) {
        this.vaksinPertama = vaksinPertama;
    }

    public String getVaksinBerikutnya() {
        return vaksinBerikutnya;
    }

    public void setVaksinBerikutnya(String vaksinBerikutnya) {
        this.vaksinBerikutnya = vaksinBerikutnya;
    }

    public Vaksin getIDVaksin() {
        return iDVaksin;
    }

    public void setIDVaksin(Vaksin iDVaksin) {
        this.iDVaksin = iDVaksin;
    }

    public Kucing getKucing() {
        return kucing;
    }

    public void setKucing(Kucing kucing) {
        this.kucing = kucing;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRm != null ? idRm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RekamMedis)) {
            return false;
        }
        RekamMedis other = (RekamMedis) object;
        if ((this.idRm == null && other.idRm != null) || (this.idRm != null && !this.idRm.equals(other.idRm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "migratedb.senamjantung.RekamMedis[ idRm=" + idRm + " ]";
    }
    
}
