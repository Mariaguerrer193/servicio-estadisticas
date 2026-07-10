package ec.edu.utn.repository;

import java.util.List;
import java.util.Optional;

import ec.edu.utn.model.Sede;
import ec.edu.utn.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class SedeRepository {

    @PersistenceContext(unitName = "EstadisticasPU")
    private EntityManager em;

    @Inject
    private AuditoriaRepository auditoriaRepo;

    @Inject
    private UsuarioRepository usuarioRepo;

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


    // Actualizar
    @Transactional
    public Optional<Sede> actualizar(Long id, Sede datos, Long usuarioId) {
        Sede sede = em.find(Sede.class, id);
        if (sede == null) {
            return Optional.empty();
        }
        sede.setNombre(datos.getNombre());
        sede.setCiudad(datos.getCiudad());
        sede.setPais(datos.getPais());
        sede.setCapacidadAprox(datos.getCapacidadAprox());

        Usuario usuario = usuarioId != null ? usuarioRepo.buscarPorId(usuarioId).orElse(null) : null;
        auditoriaRepo.registrar(usuario, "ACTUALIZAR_SEDE", "SEDE", String.valueOf(id), "Datos actualizados");

        return Optional.of(sede);
    }
}