package ec.edu.utn.repository;

import ec.edu.utn.model.Partido;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PartidoRepository {

    @PersistenceContext(unitName = "EstadisticasPU")
    private EntityManager em;

    // Listar todos (calendario completo)
    public List<Partido> listarTodos() {
        return em.createQuery("SELECT p FROM Partido p ORDER BY p.fechaHoraUtc", Partido.class).getResultList();
    }

    // Buscar por ID
    public Optional<Partido> buscarPorId(Long id) {
        return Optional.ofNullable(em.find(Partido.class, id));
    }

    // Listar por grupo
    public List<Partido> listarPorGrupo(String codigoGrupo) {
        return em.createQuery(
                "SELECT p FROM Partido p WHERE p.grupo.codigo = :codigo ORDER BY p.fechaHoraUtc", Partido.class)
                .setParameter("codigo", codigoGrupo)
                .getResultList();
    }

    // Crear
    @Transactional
    public Partido crear(Partido p) {
        em.persist(p);
        return p;
    }

    // Registrar resultado oficial (RF11)
    @Transactional
    public Optional<Partido> registrarResultado(Long id, Integer golesLocal, Integer golesVisitante) {
        Partido partido = em.find(Partido.class, id);
        if (partido == null) {
            return Optional.empty();
        }
        partido.setGolesLocal(golesLocal);
        partido.setGolesVisitante(golesVisitante);
        partido.setEstado("FINALIZADO");
        return Optional.of(partido);
    }
}