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
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import migratedb.senamjantung.exceptions.NonexistentEntityException;
import migratedb.senamjantung.exceptions.PreexistingEntityException;

/**
 *
 * @author USER
 */
public class PegawaiJpaController implements Serializable {

    public PegawaiJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("migratedb_senamjantung_jar_0.0.1-SNAPSHOTPU");

    public PegawaiJpaController() {
    }

    
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pegawai pegawai) throws PreexistingEntityException, Exception {
        if (pegawai.getKucingCollection() == null) {
            pegawai.setKucingCollection(new ArrayList<Kucing>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Kucing> attachedKucingCollection = new ArrayList<Kucing>();
            for (Kucing kucingCollectionKucingToAttach : pegawai.getKucingCollection()) {
                kucingCollectionKucingToAttach = em.getReference(kucingCollectionKucingToAttach.getClass(), kucingCollectionKucingToAttach.getIDKucing());
                attachedKucingCollection.add(kucingCollectionKucingToAttach);
            }
            pegawai.setKucingCollection(attachedKucingCollection);
            em.persist(pegawai);
            for (Kucing kucingCollectionKucing : pegawai.getKucingCollection()) {
                Pegawai oldIDPegawaiOfKucingCollectionKucing = kucingCollectionKucing.getIDPegawai();
                kucingCollectionKucing.setIDPegawai(pegawai);
                kucingCollectionKucing = em.merge(kucingCollectionKucing);
                if (oldIDPegawaiOfKucingCollectionKucing != null) {
                    oldIDPegawaiOfKucingCollectionKucing.getKucingCollection().remove(kucingCollectionKucing);
                    oldIDPegawaiOfKucingCollectionKucing = em.merge(oldIDPegawaiOfKucingCollectionKucing);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPegawai(pegawai.getIDPegawai()) != null) {
                throw new PreexistingEntityException("Pegawai " + pegawai + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pegawai pegawai) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pegawai persistentPegawai = em.find(Pegawai.class, pegawai.getIDPegawai());
            Collection<Kucing> kucingCollectionOld = persistentPegawai.getKucingCollection();
            Collection<Kucing> kucingCollectionNew = pegawai.getKucingCollection();
            Collection<Kucing> attachedKucingCollectionNew = new ArrayList<Kucing>();
            for (Kucing kucingCollectionNewKucingToAttach : kucingCollectionNew) {
                kucingCollectionNewKucingToAttach = em.getReference(kucingCollectionNewKucingToAttach.getClass(), kucingCollectionNewKucingToAttach.getIDKucing());
                attachedKucingCollectionNew.add(kucingCollectionNewKucingToAttach);
            }
            kucingCollectionNew = attachedKucingCollectionNew;
            pegawai.setKucingCollection(kucingCollectionNew);
            pegawai = em.merge(pegawai);
            for (Kucing kucingCollectionOldKucing : kucingCollectionOld) {
                if (!kucingCollectionNew.contains(kucingCollectionOldKucing)) {
                    kucingCollectionOldKucing.setIDPegawai(null);
                    kucingCollectionOldKucing = em.merge(kucingCollectionOldKucing);
                }
            }
            for (Kucing kucingCollectionNewKucing : kucingCollectionNew) {
                if (!kucingCollectionOld.contains(kucingCollectionNewKucing)) {
                    Pegawai oldIDPegawaiOfKucingCollectionNewKucing = kucingCollectionNewKucing.getIDPegawai();
                    kucingCollectionNewKucing.setIDPegawai(pegawai);
                    kucingCollectionNewKucing = em.merge(kucingCollectionNewKucing);
                    if (oldIDPegawaiOfKucingCollectionNewKucing != null && !oldIDPegawaiOfKucingCollectionNewKucing.equals(pegawai)) {
                        oldIDPegawaiOfKucingCollectionNewKucing.getKucingCollection().remove(kucingCollectionNewKucing);
                        oldIDPegawaiOfKucingCollectionNewKucing = em.merge(oldIDPegawaiOfKucingCollectionNewKucing);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = pegawai.getIDPegawai();
                if (findPegawai(id) == null) {
                    throw new NonexistentEntityException("The pegawai with id " + id + " no longer exists.");
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
            Pegawai pegawai;
            try {
                pegawai = em.getReference(Pegawai.class, id);
                pegawai.getIDPegawai();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pegawai with id " + id + " no longer exists.", enfe);
            }
            Collection<Kucing> kucingCollection = pegawai.getKucingCollection();
            for (Kucing kucingCollectionKucing : kucingCollection) {
                kucingCollectionKucing.setIDPegawai(null);
                kucingCollectionKucing = em.merge(kucingCollectionKucing);
            }
            em.remove(pegawai);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pegawai> findPegawaiEntities() {
        return findPegawaiEntities(true, -1, -1);
    }

    public List<Pegawai> findPegawaiEntities(int maxResults, int firstResult) {
        return findPegawaiEntities(false, maxResults, firstResult);
    }

    private List<Pegawai> findPegawaiEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pegawai.class));
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

    public Pegawai findPegawai(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pegawai.class, id);
        } finally {
            em.close();
        }
    }

    public int getPegawaiCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pegawai> rt = cq.from(Pegawai.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
