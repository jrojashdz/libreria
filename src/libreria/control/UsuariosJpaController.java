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
import libreria.control.exceptions.IllegalOrphanException;
import libreria.control.exceptions.NonexistentEntityException;
import libreria.model.Usuarios;

/**
 *
 * @author Yeoshua
 */
public class UsuariosJpaController implements Serializable {

    public UsuariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) {
        if (usuarios.getPrestamoCollection() == null) {
            usuarios.setPrestamoCollection(new ArrayList<Prestamo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Prestamo> attachedPrestamoCollection = new ArrayList<Prestamo>();
            for (Prestamo prestamoCollectionPrestamoToAttach : usuarios.getPrestamoCollection()) {
                prestamoCollectionPrestamoToAttach = em.getReference(prestamoCollectionPrestamoToAttach.getClass(), prestamoCollectionPrestamoToAttach.getIdprestamo());
                attachedPrestamoCollection.add(prestamoCollectionPrestamoToAttach);
            }
            usuarios.setPrestamoCollection(attachedPrestamoCollection);
            em.persist(usuarios);
            for (Prestamo prestamoCollectionPrestamo : usuarios.getPrestamoCollection()) {
                Usuarios oldUsuariosIdusuarioOfPrestamoCollectionPrestamo = prestamoCollectionPrestamo.getUsuariosIdusuario();
                prestamoCollectionPrestamo.setUsuariosIdusuario(usuarios);
                prestamoCollectionPrestamo = em.merge(prestamoCollectionPrestamo);
                if (oldUsuariosIdusuarioOfPrestamoCollectionPrestamo != null) {
                    oldUsuariosIdusuarioOfPrestamoCollectionPrestamo.getPrestamoCollection().remove(prestamoCollectionPrestamo);
                    oldUsuariosIdusuarioOfPrestamoCollectionPrestamo = em.merge(oldUsuariosIdusuarioOfPrestamoCollectionPrestamo);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getIdusuario());
            Collection<Prestamo> prestamoCollectionOld = persistentUsuarios.getPrestamoCollection();
            Collection<Prestamo> prestamoCollectionNew = usuarios.getPrestamoCollection();
            List<String> illegalOrphanMessages = null;
            for (Prestamo prestamoCollectionOldPrestamo : prestamoCollectionOld) {
                if (!prestamoCollectionNew.contains(prestamoCollectionOldPrestamo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Prestamo " + prestamoCollectionOldPrestamo + " since its usuariosIdusuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Prestamo> attachedPrestamoCollectionNew = new ArrayList<Prestamo>();
            for (Prestamo prestamoCollectionNewPrestamoToAttach : prestamoCollectionNew) {
                prestamoCollectionNewPrestamoToAttach = em.getReference(prestamoCollectionNewPrestamoToAttach.getClass(), prestamoCollectionNewPrestamoToAttach.getIdprestamo());
                attachedPrestamoCollectionNew.add(prestamoCollectionNewPrestamoToAttach);
            }
            prestamoCollectionNew = attachedPrestamoCollectionNew;
            usuarios.setPrestamoCollection(prestamoCollectionNew);
            usuarios = em.merge(usuarios);
            for (Prestamo prestamoCollectionNewPrestamo : prestamoCollectionNew) {
                if (!prestamoCollectionOld.contains(prestamoCollectionNewPrestamo)) {
                    Usuarios oldUsuariosIdusuarioOfPrestamoCollectionNewPrestamo = prestamoCollectionNewPrestamo.getUsuariosIdusuario();
                    prestamoCollectionNewPrestamo.setUsuariosIdusuario(usuarios);
                    prestamoCollectionNewPrestamo = em.merge(prestamoCollectionNewPrestamo);
                    if (oldUsuariosIdusuarioOfPrestamoCollectionNewPrestamo != null && !oldUsuariosIdusuarioOfPrestamoCollectionNewPrestamo.equals(usuarios)) {
                        oldUsuariosIdusuarioOfPrestamoCollectionNewPrestamo.getPrestamoCollection().remove(prestamoCollectionNewPrestamo);
                        oldUsuariosIdusuarioOfPrestamoCollectionNewPrestamo = em.merge(oldUsuariosIdusuarioOfPrestamoCollectionNewPrestamo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuarios.getIdusuario();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getIdusuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Prestamo> prestamoCollectionOrphanCheck = usuarios.getPrestamoCollection();
            for (Prestamo prestamoCollectionOrphanCheckPrestamo : prestamoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuarios (" + usuarios + ") cannot be destroyed since the Prestamo " + prestamoCollectionOrphanCheckPrestamo + " in its prestamoCollection field has a non-nullable usuariosIdusuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
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

    public Usuarios findUsuarios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
