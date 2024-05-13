/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libreria.control;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import libreria.model.Prestamo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import libreria.control.exceptions.NonexistentEntityException;
import libreria.model.Libros;

/**
 *
 * @author Yeoshua
 */
public class LibrosJpaController implements Serializable {

    public LibrosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Libros libros) {
        if (libros.getPrestamoCollection() == null) {
            libros.setPrestamoCollection(new ArrayList<Prestamo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Prestamo> attachedPrestamoCollection = new ArrayList<Prestamo>();
            for (Prestamo prestamoCollectionPrestamoToAttach : libros.getPrestamoCollection()) {
                prestamoCollectionPrestamoToAttach = em.getReference(prestamoCollectionPrestamoToAttach.getClass(), prestamoCollectionPrestamoToAttach.getIdprestamo());
                attachedPrestamoCollection.add(prestamoCollectionPrestamoToAttach);
            }
            libros.setPrestamoCollection(attachedPrestamoCollection);
            em.persist(libros);
            for (Prestamo prestamoCollectionPrestamo : libros.getPrestamoCollection()) {
                prestamoCollectionPrestamo.getLibrosCollection().add(libros);
                prestamoCollectionPrestamo = em.merge(prestamoCollectionPrestamo);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Libros libros) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Libros persistentLibros = em.find(Libros.class, libros.getCodigo());
            Collection<Prestamo> prestamoCollectionOld = persistentLibros.getPrestamoCollection();
            Collection<Prestamo> prestamoCollectionNew = libros.getPrestamoCollection();
            Collection<Prestamo> attachedPrestamoCollectionNew = new ArrayList<Prestamo>();
            for (Prestamo prestamoCollectionNewPrestamoToAttach : prestamoCollectionNew) {
                prestamoCollectionNewPrestamoToAttach = em.getReference(prestamoCollectionNewPrestamoToAttach.getClass(), prestamoCollectionNewPrestamoToAttach.getIdprestamo());
                attachedPrestamoCollectionNew.add(prestamoCollectionNewPrestamoToAttach);
            }
            prestamoCollectionNew = attachedPrestamoCollectionNew;
            libros.setPrestamoCollection(prestamoCollectionNew);
            libros = em.merge(libros);
            for (Prestamo prestamoCollectionOldPrestamo : prestamoCollectionOld) {
                if (!prestamoCollectionNew.contains(prestamoCollectionOldPrestamo)) {
                    prestamoCollectionOldPrestamo.getLibrosCollection().remove(libros);
                    prestamoCollectionOldPrestamo = em.merge(prestamoCollectionOldPrestamo);
                }
            }
            for (Prestamo prestamoCollectionNewPrestamo : prestamoCollectionNew) {
                if (!prestamoCollectionOld.contains(prestamoCollectionNewPrestamo)) {
                    prestamoCollectionNewPrestamo.getLibrosCollection().add(libros);
                    prestamoCollectionNewPrestamo = em.merge(prestamoCollectionNewPrestamo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = libros.getCodigo();
                if (findLibros(id) == null) {
                    throw new NonexistentEntityException("The libros with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Libros libros;
            try {
                libros = em.getReference(Libros.class, id);
                libros.getCodigo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The libros with id " + id + " no longer exists.", enfe);
            }
            Collection<Prestamo> prestamoCollection = libros.getPrestamoCollection();
            for (Prestamo prestamoCollectionPrestamo : prestamoCollection) {
                prestamoCollectionPrestamo.getLibrosCollection().remove(libros);
                prestamoCollectionPrestamo = em.merge(prestamoCollectionPrestamo);
            }
            em.remove(libros);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Libros> findLibrosEntities() {
        return findLibrosEntities(true, -1, -1);
    }

    public List<Libros> findLibrosEntities(int maxResults, int firstResult) {
        return findLibrosEntities(false, maxResults, firstResult);
    }

    private List<Libros> findLibrosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Libros.class));
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

    public Libros findLibros(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Libros.class, id);
        } finally {
            em.close();
        }
    }

    public int getLibrosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Libros> rt = cq.from(Libros.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
