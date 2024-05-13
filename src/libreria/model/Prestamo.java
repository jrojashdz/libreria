/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libreria.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Yeoshua
 */
@Entity
@Table(name = "prestamo")
@NamedQueries({
    @NamedQuery(name = "Prestamo.findAll", query = "SELECT p FROM Prestamo p"),
    @NamedQuery(name = "Prestamo.findByIdprestamo", query = "SELECT p FROM Prestamo p WHERE p.idprestamo = :idprestamo"),
    @NamedQuery(name = "Prestamo.findByFecha", query = "SELECT p FROM Prestamo p WHERE p.fecha = :fecha"),
    @NamedQuery(name = "Prestamo.findByFechadevolucion", query = "SELECT p FROM Prestamo p WHERE p.fechadevolucion = :fechadevolucion"),
    @NamedQuery(name = "Prestamo.findByEstatus", query = "SELECT p FROM Prestamo p WHERE p.estatus = :estatus")})
public class Prestamo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idprestamo")
    private Integer idprestamo;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "fechadevolucion")
    @Temporal(TemporalType.DATE)
    private Date fechadevolucion;
    @Column(name = "estatus")
    private String estatus;
    @ManyToMany(mappedBy = "prestamoCollection")
    private Collection<Libros> librosCollection;
    @JoinColumn(name = "usuarios_idusuario", referencedColumnName = "idusuario")
    @ManyToOne(optional = false)
    private Usuarios usuariosIdusuario;

    public Prestamo() {
    }

    public Prestamo(Integer idprestamo) {
        this.idprestamo = idprestamo;
    }

    public Integer getIdprestamo() {
        return idprestamo;
    }

    public void setIdprestamo(Integer idprestamo) {
        this.idprestamo = idprestamo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechadevolucion() {
        return fechadevolucion;
    }

    public void setFechadevolucion(Date fechadevolucion) {
        this.fechadevolucion = fechadevolucion;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Collection<Libros> getLibrosCollection() {
        return librosCollection;
    }

    public void setLibrosCollection(Collection<Libros> librosCollection) {
        this.librosCollection = librosCollection;
    }

    public Usuarios getUsuariosIdusuario() {
        return usuariosIdusuario;
    }

    public void setUsuariosIdusuario(Usuarios usuariosIdusuario) {
        this.usuariosIdusuario = usuariosIdusuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idprestamo != null ? idprestamo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prestamo)) {
            return false;
        }
        Prestamo other = (Prestamo) object;
        if ((this.idprestamo == null && other.idprestamo != null) || (this.idprestamo != null && !this.idprestamo.equals(other.idprestamo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "libreria.model.Prestamo[ idprestamo=" + idprestamo + " ]";
    }
    
}
