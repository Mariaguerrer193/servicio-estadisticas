package ec.edu.utn.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "fases")
public class Fase {

    @Id
    @Column(length = 30)
    private String codigo;

    @Column(nullable = false, length = 60)
    private String nombre;

    @Column(length = 50)
    private String fechas;

    // Constructor vacío (obligatorio para JPA)
    public Fase() {
    }

    // Getters y setters

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechas() {
        return fechas;
    }

    public void setFechas(String fechas) {
        this.fechas = fechas;
    }
}