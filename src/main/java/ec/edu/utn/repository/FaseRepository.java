package ec.edu.utn.repository;

import ec.edu.utn.model.Fase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class FaseRepository {

    @PersistenceContext(unitName = "EstadisticasPU")
    private EntityManager em;

    // Listar todas
    public List<Fase> listarTodas() {
        return em.createQuery("SELECT f FROM Fase f", Fase.class).getResultList();
    }

    // Buscar por código
    public Optional<Fase> buscarPorCodigo(String codigo) {
        return Optional.ofNullable(em.find(Fase.class, codigo));
    }
}