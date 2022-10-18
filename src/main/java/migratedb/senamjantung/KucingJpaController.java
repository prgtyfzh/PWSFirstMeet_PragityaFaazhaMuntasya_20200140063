/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package migratedb.senamjantung;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import migratedb.senamjantung.exceptions.IllegalOrphanException;
import migratedb.senamjantung.exceptions.NonexistentEntityException;
import migratedb.senamjantung.exceptions.PreexistingEntityException;

/**
 *
 * @author USER
 */
public class KucingJpaController implements Serializable {

    public KucingJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("migratedb_senamjantung_jar_0.0.1-SNAPSHOTPU");

    public KucingJpaController() {
    }

    
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Kucing kucing) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        RekamMedis rekamMedisOrphanCheck = kucing.getRekamMedis();
        if (rekamMedisOrphanCheck != null) {
            Kucing oldKucingOfRekamMedis = rekamMedisOrphanCheck.getKucing();
            if (oldKucingOfRekamMedis != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The RekamMedis " + rekamMedisOrphanCheck + " already has an item of type Kucing whose rekamMedis column cannot be null. Please make another selection for the rekamMedis field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pemilik IDPemilik = kucing.getIDPemilik();
            if (IDPemilik != null) {
                IDPemilik = em.getReference(IDPemilik.getClass(), IDPemilik.getIDPemilik());
                kucing.setIDPemilik(IDPemilik);
            }
            Pegawai IDPegawai = kucing.getIDPegawai();
            if (IDPegawai != null) {
                IDPegawai = em.getReference(IDPegawai.getClass(), IDPegawai.getIDPegawai());
                kucing.setIDPegawai(IDPegawai);
            }
            RekamMedis rekamMedis = kucing.getRekamMedis();
            if (rekamMedis != null) {
                rekamMedis = em.getReference(rekamMedis.getClass(), rekamMedis.getIdRm());
                kucing.setRekamMedis(rekamMedis);
            }
            em.persist(kucing);
            if (IDPemilik != null) {
                IDPemilik.getKucingCollection().add(kucing);
                IDPemilik = em.merge(IDPemilik);
            }
            if (IDPegawai != null) {
                IDPegawai.getKucingCollection().add(kucing);
                IDPegawai = em.merge(IDPegawai);
            }
            if (rekamMedis != null) {
                rekamMedis.setKucing(kucing);
                rekamMedis = em.merge(rekamMedis);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findKucing(kucing.getIDKucing()) != null) {
                throw new PreexistingEntityException("Kucing " + kucing + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Kucing kucing) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Kucing persistentKucing = em.find(Kucing.class, kucing.getIDKucing());
            Pemilik IDPemilikOld = persistentKucing.getIDPemilik();
            Pemilik IDPemilikNew = kucing.getIDPemilik();
            Pegawai IDPegawaiOld = persistentKucing.getIDPegawai();
            Pegawai IDPegawaiNew = kucing.getIDPegawai();
            RekamMedis rekamMedisOld = persistentKucing.getRekamMedis();
            RekamMedis rekamMedisNew = kucing.getRekamMedis();
            List<String> illegalOrphanMessages = null;
            if (rekamMedisNew != null && !rekamMedisNew.equals(rekamMedisOld)) {
                Kucing oldKucingOfRekamMedis = rekamMedisNew.getKucing();
                if (oldKucingOfRekamMedis != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The RekamMedis " + rekamMedisNew + " already has an item of type Kucing whose rekamMedis column cannot be null. Please make another selection for the rekamMedis field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (IDPemilikNew != null) {
                IDPemilikNew = em.getReference(IDPemilikNew.getClass(), IDPemilikNew.getIDPemilik());
                kucing.setIDPemilik(IDPemilikNew);
            }
            if (IDPegawaiNew != null) {
                IDPegawaiNew = em.getReference(IDPegawaiNew.getClass(), IDPegawaiNew.getIDPegawai());
                kucing.setIDPegawai(IDPegawaiNew);
            }
            if (rekamMedisNew != null) {
                rekamMedisNew = em.getReference(rekamMedisNew.getClass(), rekamMedisNew.getIdRm());
                kucing.setRekamMedis(rekamMedisNew);
            }
            kucing = em.merge(kucing);
            if (IDPemilikOld != null && !IDPemilikOld.equals(IDPemilikNew)) {
                IDPemilikOld.getKucingCollection().remove(kucing);
                IDPemilikOld = em.merge(IDPemilikOld);
            }
            if (IDPemilikNew != null && !IDPemilikNew.equals(IDPemilikOld)) {
                IDPemilikNew.getKucingCollection().add(kucing);
                IDPemilikNew = em.merge(IDPemilikNew);
            }
            if (IDPegawaiOld != null && !IDPegawaiOld.equals(IDPegawaiNew)) {
                IDPegawaiOld.getKucingCollection().remove(kucing);
                IDPegawaiOld = em.merge(IDPegawaiOld);
            }
            if (IDPegawaiNew != null && !IDPegawaiNew.equals(IDPegawaiOld)) {
                IDPegawaiNew.getKucingCollection().add(kucing);
                IDPegawaiNew = em.merge(IDPegawaiNew);
            }
            if (rekamMedisOld != null && !rekamMedisOld.equals(rekamMedisNew)) {
                rekamMedisOld.setKucing(null);
                rekamMedisOld = em.merge(rekamMedisOld);
            }
            if (rekamMedisNew != null && !rekamMedisNew.equals(rekamMedisOld)) {
                rekamMedisNew.setKucing(kucing);
                rekamMedisNew = em.merge(rekamMedisNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = kucing.getIDKucing();
                if (findKucing(id) == null) {
                    throw new NonexistentEntityException("The kucing with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Kucing kucing;
            try {
                kucing = em.getReference(Kucing.class, id);
                kucing.getIDKucing();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The kucing with id " + id + " no longer exists.", enfe);
            }
            Pemilik IDPemilik = kucing.getIDPemilik();
            if (IDPemilik != null) {
                IDPemilik.getKucingCollection().remove(kucing);
                IDPemilik = em.merge(IDPemilik);
            }
            Pegawai IDPegawai = kucing.getIDPegawai();
            if (IDPegawai != null) {
                IDPegawai.getKucingCollection().remove(kucing);
                IDPegawai = em.merge(IDPegawai);
            }
            RekamMedis rekamMedis = kucing.getRekamMedis();
            if (rekamMedis != null) {
                rekamMedis.setKucing(null);
                rekamMedis = em.merge(rekamMedis);
            }
            em.remove(kucing);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Kucing> findKucingEntities() {
        return findKucingEntities(true, -1, -1);
    }

    public List<Kucing> findKucingEntities(int maxResults, int firstResult) {
        return findKucingEntities(false, maxResults, firstResult);
    }

    private List<Kucing> findKucingEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Kucing.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Kucing findKucing(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Kucing.class, id);
        } finally {
            em.close();
        }
    }

    public int getKucingCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Kucing> rt = cq.from(Kucing.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
