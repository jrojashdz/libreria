/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libreria.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Yeoshua
 */
@Entity
@Table(name = "libros")
@NamedQueries({
    @NamedQuery(name = "Libros.findAll", query = "SELECT l FROM Libros l"),
    @NamedQuery(name = "Libros.findByCodigo", query = "SELECT l FROM Libros l WHERE l.codigo = :codigo"),
    @NamedQuery(name = "Libros.findByNumeropaginas", query = "SELECT l FROM Libros l WHERE l.numeropaginas = :numeropaginas"),
    @NamedQuery(name = "Libros.findByAnio", query = "SELECT l FROM Libros l WHERE l.anio = :anio"),
    @NamedQuery(name = "Libros.findByPrecio", query = "SELECT l FROM Libros l WHERE l.precio = :precio")})
public class Libros implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "codigo")
    private Integer codigo;
    @Lob
    @Column(name = "nombre")
    private String nombre;
    @Lob
    @Column(name = "editorial")
    private String editorial;
    @Lob
    @Column(name = "autor")
    private String autor;
    @Lob
    @Column(name = "genero")
    private String genero;
    @Lob
    @Column(name = "paisautor")
    private String paisautor;
    @Column(name = "numeropaginas")
    private Integer numeropaginas;
    @Column(name = "anio")
    private Integer anio;
    @Column(name = "precio")
    private Integer precio;
    @JoinTable(name = "libros_has_prestamo", joinColumns = {
        @JoinColumn(name = "libros_codigo", referencedColumnName = "codigo")}, inverseJoinColumns = {
        @JoinColumn(name = "prestamo_idprestamo", referencedColumnName = "idprestamo")})
    @ManyToMany
    private Collection<Prestamo> prestamoCollection;

    public Libros() {
    }

    public Libros(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getPaisautor() {
        return paisautor;
    }

    public void setPaisautor(String paisautor) {
        this.paisautor = paisautor;
    }

    public Integer getNumeropaginas() {
        return numeropaginas;
    }

    public void setNumeropaginas(Integer numeropaginas) {
        this.numeropaginas = numeropaginas;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public Collection<Prestamo> getPrestamoCollection() {
        return prestamoCollection;
    }

    public void setPrestamoCollection(Collection<Prestamo> prestamoCollection) {
        this.prestamoCollection = prestamoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codigo != null ? codigo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Libros)) {
            return false;
        }
        Libros other = (Libros) object;
        if ((this.codigo == null && other.codigo != null) || (this.codigo != null && !this.codigo.equals(other.codigo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "libreria.model.Libros[ codigo=" + codigo + " ]";
    }
    
}
