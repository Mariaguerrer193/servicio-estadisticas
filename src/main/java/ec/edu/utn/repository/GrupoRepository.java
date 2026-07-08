package ec.edu.utn.repository;

import ec.edu.utn.model.Grupo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class GrupoRepository {

    @PersistenceContext(unitName = "EstadisticasPU")
    private EntityManager em;

    // Listar todos
    public List<Grupo> listarTodos() {
        return em.createQuery("SELECT g FROM Grupo g ORDER BY g.codigo", Grupo.class).getResultList();
    }

    // Buscar por código
    public Optional<Grupo> buscarPorCodigo(String codigo) {
        return Optional.ofNullable(em.find(Grupo.class, codigo));
    }

    // Crear
    @Transactional
    public Grupo crear(Grupo g) {
        em.persist(g);
        return g;
    }
}