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
import migratedb.senamjantung.exceptions.NonexistentEntityException;
import migratedb.senamjantung.exceptions.PreexistingEntityException;

/**
 *
 * @author USER
 */
public class PemilikJpaController implements Serializable {

    public PemilikJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pemilik pemilik) throws PreexistingEntityException, Exception {
        if (pemilik.getKucingCollection() == null) {
            pemilik.setKucingCollection(new ArrayList<Kucing>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Kucing> attachedKucingCollection = new ArrayList<Kucing>();
            for (Kucing kucingCollectionKucingToAttach : pemilik.getKucingCollection()) {
                kucingCollectionKucingToAttach = em.getReference(kucingCollectionKucingToAttach.getClass(), kucingCollectionKucingToAttach.getIDKucing());
                attachedKucingCollection.add(kucingCollectionKucingToAttach);
            }
            pemilik.setKucingCollection(attachedKucingCollection);
            em.persist(pemilik);
            for (Kucing kucingCollectionKucing : pemilik.getKucingCollection()) {
                Pemilik oldIDPemilikOfKucingCollectionKucing = kucingCollectionKucing.getIDPemilik();
                kucingCollectionKucing.setIDPemilik(pemilik);
                kucingCollectionKucing = em.merge(kucingCollectionKucing);
                if (oldIDPemilikOfKucingCollectionKucing != null) {
                    oldIDPemilikOfKucingCollectionKucing.getKucingCollection().remove(kucingCollectionKucing);
                    oldIDPemilikOfKucingCollectionKucing = em.merge(oldIDPemilikOfKucingCollectionKucing);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPemilik(pemilik.getIDPemilik()) != null) {
                throw new PreexistingEntityException("Pemilik " + pemilik + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pemilik pemilik) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pemilik persistentPemilik = em.find(Pemilik.class, pemilik.getIDPemilik());
            Collection<Kucing> kucingCollectionOld = persistentPemilik.getKucingCollection();
            Collection<Kucing> kucingCollectionNew = pemilik.getKucingCollection();
            Collection<Kucing> attachedKucingCollectionNew = new ArrayList<Kucing>();
            for (Kucing kucingCollectionNewKucingToAttach : kucingCollectionNew) {
                kucingCollectionNewKucingToAttach = em.getReference(kucingCollectionNewKucingToAttach.getClass(), kucingCollectionNewKucingToAttach.getIDKucing());
                attachedKucingCollectionNew.add(kucingCollectionNewKucingToAttach);
            }
            kucingCollectionNew = attachedKucingCollectionNew;
            pemilik.setKucingCollection(kucingCollectionNew);
            pemilik = em.merge(pemilik);
            for (Kucing kucingCollectionOldKucing : kucingCollectionOld) {
                if (!kucingCollectionNew.contains(kucingCollectionOldKucing)) {
                    kucingCollectionOldKucing.setIDPemilik(null);
                    kucingCollectionOldKucing = em.merge(kucingCollectionOldKucing);
                }
            }
            for (Kucing kucingCollectionNewKucing : kucingCollectionNew) {
                if (!kucingCollectionOld.contains(kucingCollectionNewKucing)) {
                    Pemilik oldIDPemilikOfKucingCollectionNewKucing = kucingCollectionNewKucing.getIDPemilik();
                    kucingCollectionNewKucing.setIDPemilik(pemilik);
                    kucingCollectionNewKucing = em.merge(kucingCollectionNewKucing);
                    if (oldIDPemilikOfKucingCollectionNewKucing != null && !oldIDPemilikOfKucingCollectionNewKucing.equals(pemilik)) {
                        oldIDPemilikOfKucingCollectionNewKucing.getKucingCollection().remove(kucingCollectionNewKucing);
                        oldIDPemilikOfKucingCollectionNewKucing = em.merge(oldIDPemilikOfKucingCollectionNewKucing);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = pemilik.getIDPemilik();
                if (findPemilik(id) == null) {
                    throw new NonexistentEntityException("The pemilik with id " + id + " no longer exists.");
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
            Pemilik pemilik;
            try {
                pemilik = em.getReference(Pemilik.class, id);
                pemilik.getIDPemilik();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pemilik with id " + id + " no longer exists.", enfe);
            }
            Collection<Kucing> kucingCollection = pemilik.getKucingCollection();
            for (Kucing kucingCollectionKucing : kucingCollection) {
                kucingCollectionKucing.setIDPemilik(null);
                kucingCollectionKucing = em.merge(kucingCollectionKucing);
            }
            em.remove(pemilik);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pemilik> findPemilikEntities() {
        return findPemilikEntities(true, -1, -1);
    }

    public List<Pemilik> findPemilikEntities(int maxResults, int firstResult) {
        return findPemilikEntities(false, maxResults, firstResult);
    }

    private List<Pemilik> findPemilikEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pemilik.class));
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

    public Pemilik findPemilik(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pemilik.class, id);
        } finally {
            em.close();
        }
    }

    public int getPemilikCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pemilik> rt = cq.from(Pemilik.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
