package ec.edu.utn.repository;

import ec.edu.utn.model.Rol;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RolRepository {

    @PersistenceContext(unitName = "EstadisticasPU")
    private EntityManager em;

    // Listar todos
    public List<Rol> listarTodos() {
        return em.createQuery("SELECT r FROM Rol r ORDER BY r.id", Rol.class).getResultList();
    }

    // Buscar por ID
    public Optional<Rol> buscarPorId(Long id) {
        return Optional.ofNullable(em.find(Rol.class, id));
    }
}