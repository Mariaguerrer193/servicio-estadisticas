package ec.edu.utn.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "partidos")
public class Partido {

    @Id
    private Long id;

    @Column(name = "numero_partido_fifa")
    private Integer numeroPartidoFifa;

    @Column(nullable = false, length = 30)
    private String fase;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "grupo_codigo")
    private Grupo grupo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seleccion_local_id", nullable = false)
    private Seleccion seleccionLocal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seleccion_visitante_id", nullable = false)
    private Seleccion seleccionVisitante;

    @Column(name = "fecha_hora_utc", nullable = false)
    private OffsetDateTime fechaHoraUtc;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sede_id", nullable = false)
    private Sede sede;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "goles_local")
    private Integer golesLocal;

    @Column(name = "goles_visitante")
    private Integer golesVisitante;

    // Constructor vacío (obligatorio para JPA)
    public Partido() {
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroPartidoFifa() {
        return numeroPartidoFifa;
    }

    public void setNumeroPartidoFifa(Integer numeroPartidoFifa) {
        this.numeroPartidoFifa = numeroPartidoFifa;
    }

    public String getFase() {
        return fase;
    }

    public void setFase(String fase) {
        this.fase = fase;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Seleccion getSeleccionLocal() {
        return seleccionLocal;
    }

    public void setSeleccionLocal(Seleccion seleccionLocal) {
        this.seleccionLocal = seleccionLocal;
    }

    public Seleccion getSeleccionVisitante() {
        return seleccionVisitante;
    }

    public void setSeleccionVisitante(Seleccion seleccionVisitante) {
        this.seleccionVisitante = seleccionVisitante;
    }

    public OffsetDateTime getFechaHoraUtc() {
        return fechaHoraUtc;
    }

    public void setFechaHoraUtc(OffsetDateTime fechaHoraUtc) {
        this.fechaHoraUtc = fechaHoraUtc;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getGolesLocal() {
        return golesLocal;
    }

    public void setGolesLocal(Integer golesLocal) {
        this.golesLocal = golesLocal;
    }

    public Integer getGolesVisitante() {
        return golesVisitante;
    }

    public void setGolesVisitante(Integer golesVisitante) {
        this.golesVisitante = golesVisitante;
    }
}