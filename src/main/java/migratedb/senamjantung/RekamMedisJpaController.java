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
import migratedb.senamjantung.exceptions.IllegalOrphanException;
import migratedb.senamjantung.exceptions.NonexistentEntityException;
import migratedb.senamjantung.exceptions.PreexistingEntityException;

/**
 *
 * @author USER
 */
public class RekamMedisJpaController implements Serializable {

    public RekamMedisJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(RekamMedis rekamMedis) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vaksin IDVaksin = rekamMedis.getIDVaksin();
            if (IDVaksin != null) {
                IDVaksin = em.getReference(IDVaksin.getClass(), IDVaksin.getIDVaksin());
                rekamMedis.setIDVaksin(IDVaksin);
            }
            Kucing kucing = rekamMedis.getKucing();
            if (kucing != null) {
                kucing = em.getReference(kucing.getClass(), kucing.getIDKucing());
                rekamMedis.setKucing(kucing);
            }
            em.persist(rekamMedis);
            if (IDVaksin != null) {
                IDVaksin.getRekamMedisCollection().add(rekamMedis);
                IDVaksin = em.merge(IDVaksin);
            }
            if (kucing != null) {
                RekamMedis oldRekamMedisOfKucing = kucing.getRekamMedis();
                if (oldRekamMedisOfKucing != null) {
                    oldRekamMedisOfKucing.setKucing(null);
                    oldRekamMedisOfKucing = em.merge(oldRekamMedisOfKucing);
                }
                kucing.setRekamMedis(rekamMedis);
                kucing = em.merge(kucing);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findRekamMedis(rekamMedis.getIdRm()) != null) {
                throw new PreexistingEntityException("RekamMedis " + rekamMedis + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(RekamMedis rekamMedis) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RekamMedis persistentRekamMedis = em.find(RekamMedis.class, rekamMedis.getIdRm());
            Vaksin IDVaksinOld = persistentRekamMedis.getIDVaksin();
            Vaksin IDVaksinNew = rekamMedis.getIDVaksin();
            Kucing kucingOld = persistentRekamMedis.getKucing();
            Kucing kucingNew = rekamMedis.getKucing();
            List<String> illegalOrphanMessages = null;
            if (kucingOld != null && !kucingOld.equals(kucingNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Kucing " + kucingOld + " since its rekamMedis field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (IDVaksinNew != null) {
                IDVaksinNew = em.getReference(IDVaksinNew.getClass(), IDVaksinNew.getIDVaksin());
                rekamMedis.setIDVaksin(IDVaksinNew);
            }
            if (kucingNew != null) {
                kucingNew = em.getReference(kucingNew.getClass(), kucingNew.getIDKucing());
                rekamMedis.setKucing(kucingNew);
            }
            rekamMedis = em.merge(rekamMedis);
            if (IDVaksinOld != null && !IDVaksinOld.equals(IDVaksinNew)) {
                IDVaksinOld.getRekamMedisCollection().remove(rekamMedis);
                IDVaksinOld = em.merge(IDVaksinOld);
            }
            if (IDVaksinNew != null && !IDVaksinNew.equals(IDVaksinOld)) {
                IDVaksinNew.getRekamMedisCollection().add(rekamMedis);
                IDVaksinNew = em.merge(IDVaksinNew);
            }
            if (kucingNew != null && !kucingNew.equals(kucingOld)) {
                RekamMedis oldRekamMedisOfKucing = kucingNew.getRekamMedis();
                if (oldRekamMedisOfKucing != null) {
                    oldRekamMedisOfKucing.setKucing(null);
                    oldRekamMedisOfKucing = em.merge(oldRekamMedisOfKucing);
                }
                kucingNew.setRekamMedis(rekamMedis);
                kucingNew = em.merge(kucingNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = rekamMedis.getIdRm();
                if (findRekamMedis(id) == null) {
                    throw new NonexistentEntityException("The rekamMedis with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            RekamMedis rekamMedis;
            try {
                rekamMedis = em.getReference(RekamMedis.class, id);
                rekamMedis.getIdRm();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rekamMedis with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Kucing kucingOrphanCheck = rekamMedis.getKucing();
            if (kucingOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This RekamMedis (" + rekamMedis + ") cannot be destroyed since the Kucing " + kucingOrphanCheck + " in its kucing field has a non-nullable rekamMedis field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Vaksin IDVaksin = rekamMedis.getIDVaksin();
            if (IDVaksin != null) {
                IDVaksin.getRekamMedisCollection().remove(rekamMedis);
                IDVaksin = em.merge(IDVaksin);
            }
            em.remove(rekamMedis);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<RekamMedis> findRekamMedisEntities() {
        return findRekamMedisEntities(true, -1, -1);
    }

    public List<RekamMedis> findRekamMedisEntities(int maxResults, int firstResult) {
        return findRekamMedisEntities(false, maxResults, firstResult);
    }

    private List<RekamMedis> findRekamMedisEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(RekamMedis.class));
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

    public RekamMedis findRekamMedis(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(RekamMedis.class, id);
        } finally {
            em.close();
        }
    }

    public int getRekamMedisCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<RekamMedis> rt = cq.from(RekamMedis.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
