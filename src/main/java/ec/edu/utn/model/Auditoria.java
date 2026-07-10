package ec.edu.utn.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "auditoria")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false, length = 50)
    private String accion;

    @Column(name = "entidad_afectada", nullable = false, length = 50)
    private String entidadAfectada;

    @Column(name = "entidad_id", length = 50)
    private String entidadId;

    @Column(length = 255)
    private String detalle;

    @com.fasterxml.jackson.annotation.JsonFormat(shape = com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING)
    @Column(name = "fecha_hora", nullable = false)
    private OffsetDateTime fechaHora;

    // Constructor vacío (obligatorio para JPA)
    public Auditoria() {
    }

    // Constructor de conveniencia, para crear registros fácilmente desde otros repositorios
    public Auditoria(Usuario usuario, String accion, String entidadAfectada, String entidadId, String detalle) {
        this.usuario = usuario;
        this.accion = accion;
        this.entidadAfectada = entidadAfectada;
        this.entidadId = entidadId;
        this.detalle = detalle;
        this.fechaHora = OffsetDateTime.now();
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getEntidadAfectada() {
        return entidadAfectada;
    }

    public void setEntidadAfectada(String entidadAfectada) {
        this.entidadAfectada = entidadAfectada;
    }

    public String getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(String entidadId) {
        this.entidadId = entidadId;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public OffsetDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(OffsetDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}