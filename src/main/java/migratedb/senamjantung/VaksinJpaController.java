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
public class VaksinJpaController implements Serializable {

    public VaksinJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Vaksin vaksin) throws PreexistingEntityException, Exception {
        if (vaksin.getRekamMedisCollection() == null) {
            vaksin.setRekamMedisCollection(new ArrayList<RekamMedis>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<RekamMedis> attachedRekamMedisCollection = new ArrayList<RekamMedis>();
            for (RekamMedis rekamMedisCollectionRekamMedisToAttach : vaksin.getRekamMedisCollection()) {
                rekamMedisCollectionRekamMedisToAttach = em.getReference(rekamMedisCollectionRekamMedisToAttach.getClass(), rekamMedisCollectionRekamMedisToAttach.getIdRm());
                attachedRekamMedisCollection.add(rekamMedisCollectionRekamMedisToAttach);
            }
            vaksin.setRekamMedisCollection(attachedRekamMedisCollection);
            em.persist(vaksin);
            for (RekamMedis rekamMedisCollectionRekamMedis : vaksin.getRekamMedisCollection()) {
                Vaksin oldIDVaksinOfRekamMedisCollectionRekamMedis = rekamMedisCollectionRekamMedis.getIDVaksin();
                rekamMedisCollectionRekamMedis.setIDVaksin(vaksin);
                rekamMedisCollectionRekamMedis = em.merge(rekamMedisCollectionRekamMedis);
                if (oldIDVaksinOfRekamMedisCollectionRekamMedis != null) {
                    oldIDVaksinOfRekamMedisCollectionRekamMedis.getRekamMedisCollection().remove(rekamMedisCollectionRekamMedis);
                    oldIDVaksinOfRekamMedisCollectionRekamMedis = em.merge(oldIDVaksinOfRekamMedisCollectionRekamMedis);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findVaksin(vaksin.getIDVaksin()) != null) {
                throw new PreexistingEntityException("Vaksin " + vaksin + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Vaksin vaksin) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Vaksin persistentVaksin = em.find(Vaksin.class, vaksin.getIDVaksin());
            Collection<RekamMedis> rekamMedisCollectionOld = persistentVaksin.getRekamMedisCollection();
            Collection<RekamMedis> rekamMedisCollectionNew = vaksin.getRekamMedisCollection();
            Collection<RekamMedis> attachedRekamMedisCollectionNew = new ArrayList<RekamMedis>();
            for (RekamMedis rekamMedisCollectionNewRekamMedisToAttach : rekamMedisCollectionNew) {
                rekamMedisCollectionNewRekamMedisToAttach = em.getReference(rekamMedisCollectionNewRekamMedisToAttach.getClass(), rekamMedisCollectionNewRekamMedisToAttach.getIdRm());
                attachedRekamMedisCollectionNew.add(rekamMedisCollectionNewRekamMedisToAttach);
            }
            rekamMedisCollectionNew = attachedRekamMedisCollectionNew;
            vaksin.setRekamMedisCollection(rekamMedisCollectionNew);
            vaksin = em.merge(vaksin);
            for (RekamMedis rekamMedisCollectionOldRekamMedis : rekamMedisCollectionOld) {
                if (!rekamMedisCollectionNew.contains(rekamMedisCollectionOldRekamMedis)) {
                    rekamMedisCollectionOldRekamMedis.setIDVaksin(null);
                    rekamMedisCollectionOldRekamMedis = em.merge(rekamMedisCollectionOldRekamMedis);
                }
            }
            for (RekamMedis rekamMedisCollectionNewRekamMedis : rekamMedisCollectionNew) {
                if (!rekamMedisCollectionOld.contains(rekamMedisCollectionNewRekamMedis)) {
                    Vaksin oldIDVaksinOfRekamMedisCollectionNewRekamMedis = rekamMedisCollectionNewRekamMedis.getIDVaksin();
                    rekamMedisCollectionNewRekamMedis.setIDVaksin(vaksin);
                    rekamMedisCollectionNewRekamMedis = em.merge(rekamMedisCollectionNewRekamMedis);
                    if (oldIDVaksinOfRekamMedisCollectionNewRekamMedis != null && !oldIDVaksinOfRekamMedisCollectionNewRekamMedis.equals(vaksin)) {
                        oldIDVaksinOfRekamMedisCollectionNewRekamMedis.getRekamMedisCollection().remove(rekamMedisCollectionNewRekamMedis);
                        oldIDVaksinOfRekamMedisCollectionNewRekamMedis = em.merge(oldIDVaksinOfRekamMedisCollectionNewRekamMedis);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = vaksin.getIDVaksin();
                if (findVaksin(id) == null) {
                    throw new NonexistentEntityException("The vaksin with id " + id + " no longer exists.");
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
            Vaksin vaksin;
            try {
                vaksin = em.getReference(Vaksin.class, id);
                vaksin.getIDVaksin();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The vaksin with id " + id + " no longer exists.", enfe);
            }
            Collection<RekamMedis> rekamMedisCollection = vaksin.getRekamMedisCollection();
            for (RekamMedis rekamMedisCollectionRekamMedis : rekamMedisCollection) {
                rekamMedisCollectionRekamMedis.setIDVaksin(null);
                rekamMedisCollectionRekamMedis = em.merge(rekamMedisCollectionRekamMedis);
            }
            em.remove(vaksin);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Vaksin> findVaksinEntities() {
        return findVaksinEntities(true, -1, -1);
    }

    public List<Vaksin> findVaksinEntities(int maxResults, int firstResult) {
        return findVaksinEntities(false, maxResults, firstResult);
    }

    private List<Vaksin> findVaksinEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Vaksin.class));
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

    public Vaksin findVaksin(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Vaksin.class, id);
        } finally {
            em.close();
        }
    }

    public int getVaksinCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Vaksin> rt = cq.from(Vaksin.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
