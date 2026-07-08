package ec.edu.utn.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "selecciones")
public class Seleccion {

    @Id
    private Long id;

    @Column(name = "codigo_fifa", nullable = false, length = 3)
    private String codigoFifa;

    @Column(nullable = false, length = 60)
    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "grupo_codigo", nullable = false)
    private Grupo grupo;

    @Column(length = 20)
    private String confederacion;

    @Column(name = "es_anfitrion", nullable = false)
    private Boolean esAnfitrion;

    @Column(length = 60)
    private String clasificacion;

    // Constructor vacío (obligatorio para JPA)
    public Seleccion() {
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoFifa() {
        return codigoFifa;
    }

    public void setCodigoFifa(String codigoFifa) {
        this.codigoFifa = codigoFifa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public String getConfederacion() {
        return confederacion;
    }

    public void setConfederacion(String confederacion) {
        this.confederacion = confederacion;
    }

    public Boolean getEsAnfitrion() {
        return esAnfitrion;
    }

    public void setEsAnfitrion(Boolean esAnfitrion) {
        this.esAnfitrion = esAnfitrion;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }
}