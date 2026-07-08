package ec.edu.utn.repository;

import ec.edu.utn.model.Sede;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SedeRepository {

    @PersistenceContext(unitName = "EstadisticasPU")
    private EntityManager em;

    // Listar todas
    public List<Sede> listarTodas() {
        return em.createQuery("SELECT s FROM Sede s", Sede.class).getResultList();
    }

    // Buscar por ID
    public Optional<Sede> buscarPorId(Long id) {
        return Optional.ofNullable(em.find(Sede.class, id));
    }

    // Crear
    @Transactional
    public Sede crear(Sede s) {
        em.persist(s);
        return s;
    }
}