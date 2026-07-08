package ec.edu.utn.repository;

import ec.edu.utn.model.Seleccion;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SeleccionRepository {

    @PersistenceContext(unitName = "EstadisticasPU")
    private EntityManager em;

    // Listar todas
    public List<Seleccion> listarTodas() {
        return em.createQuery("SELECT s FROM Seleccion s ORDER BY s.id", Seleccion.class).getResultList();
    }

    // Buscar por ID
    public Optional<Seleccion> buscarPorId(Long id) {
        return Optional.ofNullable(em.find(Seleccion.class, id));
    }

    // Listar por grupo
    public List<Seleccion> listarPorGrupo(String codigoGrupo) {
        return em.createQuery(
                "SELECT s FROM Seleccion s WHERE s.grupo.codigo = :codigo ORDER BY s.nombre", Seleccion.class)
                .setParameter("codigo", codigoGrupo)
                .getResultList();
    }

    // Crear
    @Transactional
    public Seleccion crear(Seleccion s) {
        em.persist(s);
        return s;
    }
}