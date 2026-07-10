package ec.edu.utn.repository;

import java.util.List;
import java.util.Optional;

import ec.edu.utn.model.Seleccion;
import ec.edu.utn.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class SeleccionRepository {

    @PersistenceContext(unitName = "EstadisticasPU")
    private EntityManager em;
@Inject
    private AuditoriaRepository auditoriaRepo;

    @Inject
    private UsuarioRepository usuarioRepo;


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


    // Actualizar (RF10)
    @Transactional
    public Optional<Seleccion> actualizar(Long id, Seleccion datos, Long usuarioId) {
        Seleccion seleccion = em.find(Seleccion.class, id);
        if (seleccion == null) {
            return Optional.empty();
        }
        seleccion.setNombre(datos.getNombre());
        seleccion.setConfederacion(datos.getConfederacion());
        seleccion.setEsAnfitrion(datos.getEsAnfitrion());
        seleccion.setClasificacion(datos.getClasificacion());
        if (datos.getGrupo() != null) {
            seleccion.setGrupo(datos.getGrupo());
        }

        Usuario usuario = usuarioId != null ? usuarioRepo.buscarPorId(usuarioId).orElse(null) : null;
       auditoriaRepo.registrar(usuario, "ACTUALIZAR_SELECCION", "SELECCION", String.valueOf(id), "Datos actualizados");

        return Optional.of(seleccion);
    }
}