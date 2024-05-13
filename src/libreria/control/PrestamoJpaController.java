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
import libreria.model.Usuarios;
import libreria.model.Libros;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import libreria.control.exceptions.NonexistentEntityException;
import libreria.model.Prestamo;

/**
 *
 * @author Yeoshua
 */
public class PrestamoJpaController implements Serializable {

    public PrestamoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Prestamo prestamo) {
        if (prestamo.getLibrosCollection() == null) {
            prestamo.setLibrosCollection(new ArrayList<Libros>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios usuariosIdusuario = prestamo.getUsuariosIdusuario();
            if (usuariosIdusuario != null) {
                usuariosIdusuario = em.getReference(usuariosIdusuario.getClass(), usuariosIdusuario.getIdusuario());
                prestamo.setUsuariosIdusuario(usuariosIdusuario);
            }
            Collection<Libros> attachedLibrosCollection = new ArrayList<Libros>();
            for (Libros librosCollectionLibrosToAttach : prestamo.getLibrosCollection()) {
                librosCollectionLibrosToAttach = em.getReference(librosCollectionLibrosToAttach.getClass(), librosCollectionLibrosToAttach.getCodigo());
                attachedLibrosCollection.add(librosCollectionLibrosToAttach);
            }
            prestamo.setLibrosCollection(attachedLibrosCollection);
            em.persist(prestamo);
            if (usuariosIdusuario != null) {
                usuariosIdusuario.getPrestamoCollection().add(prestamo);
                usuariosIdusuario = em.merge(usuariosIdusuario);
            }
            for (Libros librosCollectionLibros : prestamo.getLibrosCollection()) {
                librosCollectionLibros.getPrestamoCollection().add(prestamo);
                librosCollectionLibros = em.merge(librosCollectionLibros);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Prestamo prestamo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Prestamo persistentPrestamo = em.find(Prestamo.class, prestamo.getIdprestamo());
            Usuarios usuariosIdusuarioOld = persistentPrestamo.getUsuariosIdusuario();
            Usuarios usuariosIdusuarioNew = prestamo.getUsuariosIdusuario();
            Collection<Libros> librosCollectionOld = persistentPrestamo.getLibrosCollection();
            Collection<Libros> librosCollectionNew = prestamo.getLibrosCollection();
            if (usuariosIdusuarioNew != null) {
                usuariosIdusuarioNew = em.getReference(usuariosIdusuarioNew.getClass(), usuariosIdusuarioNew.getIdusuario());
                prestamo.setUsuariosIdusuario(usuariosIdusuarioNew);
            }
            Collection<Libros> attachedLibrosCollectionNew = new ArrayList<Libros>();
            for (Libros librosCollectionNewLibrosToAttach : librosCollectionNew) {
                librosCollectionNewLibrosToAttach = em.getReference(librosCollectionNewLibrosToAttach.getClass(), librosCollectionNewLibrosToAttach.getCodigo());
                attachedLibrosCollectionNew.add(librosCollectionNewLibrosToAttach);
            }
            librosCollectionNew = attachedLibrosCollectionNew;
            prestamo.setLibrosCollection(librosCollectionNew);
            prestamo = em.merge(prestamo);
            if (usuariosIdusuarioOld != null && !usuariosIdusuarioOld.equals(usuariosIdusuarioNew)) {
                usuariosIdusuarioOld.getPrestamoCollection().remove(prestamo);
                usuariosIdusuarioOld = em.merge(usuariosIdusuarioOld);
            }
            if (usuariosIdusuarioNew != null && !usuariosIdusuarioNew.equals(usuariosIdusuarioOld)) {
                usuariosIdusuarioNew.getPrestamoCollection().add(prestamo);
                usuariosIdusuarioNew = em.merge(usuariosIdusuarioNew);
            }
            for (Libros librosCollectionOldLibros : librosCollectionOld) {
                if (!librosCollectionNew.contains(librosCollectionOldLibros)) {
                    librosCollectionOldLibros.getPrestamoCollection().remove(prestamo);
                    librosCollectionOldLibros = em.merge(librosCollectionOldLibros);
                }
            }
            for (Libros librosCollectionNewLibros : librosCollectionNew) {
                if (!librosCollectionOld.contains(librosCollectionNewLibros)) {
                    librosCollectionNewLibros.getPrestamoCollection().add(prestamo);
                    librosCollectionNewLibros = em.merge(librosCollectionNewLibros);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = prestamo.getIdprestamo();
                if (findPrestamo(id) == null) {
                    throw new NonexistentEntityException("The prestamo with id " + id + " no longer exists.");
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
            Prestamo prestamo;
            try {
                prestamo = em.getReference(Prestamo.class, id);
                prestamo.getIdprestamo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The prestamo with id " + id + " no longer exists.", enfe);
            }
            Usuarios usuariosIdusuario = prestamo.getUsuariosIdusuario();
            if (usuariosIdusuario != null) {
                usuariosIdusuario.getPrestamoCollection().remove(prestamo);
                usuariosIdusuario = em.merge(usuariosIdusuario);
            }
            Collection<Libros> librosCollection = prestamo.getLibrosCollection();
            for (Libros librosCollectionLibros : librosCollection) {
                librosCollectionLibros.getPrestamoCollection().remove(prestamo);
                librosCollectionLibros = em.merge(librosCollectionLibros);
            }
            em.remove(prestamo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Prestamo> findPrestamoEntities() {
        return findPrestamoEntities(true, -1, -1);
    }

    public List<Prestamo> findPrestamoEntities(int maxResults, int firstResult) {
        return findPrestamoEntities(false, maxResults, firstResult);
    }

    private List<Prestamo> findPrestamoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Prestamo.class));
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

    public Prestamo findPrestamo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Prestamo.class, id);
        } finally {
            em.close();
        }
    }

    public int getPrestamoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Prestamo> rt = cq.from(Prestamo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
