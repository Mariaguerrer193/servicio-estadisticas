package ec.edu.utn.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "grupos")
public class Grupo {

    @Id
    @Column(length = 1)
    private String codigo;

    @Column(nullable = false, length = 50)
    private String nombre;

    // Constructor vacío (obligatorio para JPA)
    public Grupo() {
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
}