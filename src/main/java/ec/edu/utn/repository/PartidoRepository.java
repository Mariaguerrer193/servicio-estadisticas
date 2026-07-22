package ec.edu.utn.repository;

import java.util.List;
import java.util.Optional;

import ec.edu.utn.model.Partido;
import ec.edu.utn.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;


@ApplicationScoped
public class PartidoRepository {

    @PersistenceContext(unitName = "EstadisticasPU")
    private EntityManager em;

    @Inject
    private AuditoriaRepository auditoriaRepo;

    @Inject
    private UsuarioRepository usuarioRepo;

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

        String detalle = "Resultado: " + golesLocal + " - " + golesVisitante;
        auditoriaRepo.registrar(null, "REGISTRAR_RESULTADO", "PARTIDO", String.valueOf(partido.getId()), detalle);

        return Optional.of(partido);
    }

    // Revertir resultado: vuelve el partido a PROGRAMADO y limpia los goles
    @Transactional
    public Optional<Partido> revertir(Long id, Long usuarioId) {
        Partido partido = em.find(Partido.class, id);
        if (partido == null) {
            return Optional.empty();
        }
        partido.setEstado("PROGRAMADO");
        partido.setGolesLocal(null);
        partido.setGolesVisitante(null);

        Usuario usuario = usuarioId != null ? usuarioRepo.buscarPorId(usuarioId).orElse(null) : null;
        auditoriaRepo.registrar(usuario, "REVERTIR_RESULTADO", "PARTIDO", String.valueOf(id), "Resultado revertido, estado vuelto a PROGRAMADO");

        return Optional.of(partido);
    }


        // Cambiar estado directamente (para el panel administrativo)
    @Transactional
    public Optional<Partido> cambiarEstado(Long id, String nuevoEstado, Long usuarioId) {
        Partido partido = em.find(Partido.class, id);
        if (partido == null) {
            return Optional.empty();
        }
        partido.setEstado(nuevoEstado);

        // Si se cancela o se vuelve a PROGRAMADO/EN_CURSO, limpiamos los goles
        if (!"FINALIZADO".equals(nuevoEstado)) {
            partido.setGolesLocal(null);
            partido.setGolesVisitante(null);
        }

        Usuario usuario = usuarioId != null ? usuarioRepo.buscarPorId(usuarioId).orElse(null) : null;
        auditoriaRepo.registrar(usuario, "CAMBIAR_ESTADO", "PARTIDO", String.valueOf(id), "Estado cambiado a " + nuevoEstado);

        return Optional.of(partido);
    }




    // Actualizar datos generales del partido (fecha, sede, fase, grupo, selecciones)
    // Distinto de registrarResultado(), que es solo para el marcador oficial
    @Transactional
    public Optional<Partido> actualizar(Long id, Partido datos, Long usuarioId) {
        Partido partido = em.find(Partido.class, id);
        if (partido == null) {
            return Optional.empty();
        }
        partido.setNumeroPartidoFifa(datos.getNumeroPartidoFifa());
        partido.setFase(datos.getFase());
        partido.setGrupo(datos.getGrupo());
        partido.setSeleccionLocal(datos.getSeleccionLocal());
        partido.setSeleccionVisitante(datos.getSeleccionVisitante());
        partido.setFechaHoraUtc(datos.getFechaHoraUtc());
        partido.setSede(datos.getSede());

        Usuario usuario = usuarioId != null ? usuarioRepo.buscarPorId(usuarioId).orElse(null) : null;
        auditoriaRepo.registrar(usuario, "ACTUALIZAR_PARTIDO", "PARTIDO", String.valueOf(id), "Datos generales actualizados");

        return Optional.of(partido);
    }


    // Listar partidos FINALIZADOS de un grupo (para calcular posiciones/estadísticas)
    public List<Partido> listarFinalizadosPorGrupo(String codigoGrupo) {
        return em.createQuery(
                "SELECT p FROM Partido p WHERE p.grupo.codigo = :codigo AND p.estado = 'FINALIZADO'", Partido.class)
                .setParameter("codigo", codigoGrupo)
                .getResultList();
    }

    // Listar TODOS los partidos finalizados de una selección (sin importar el grupo, útil para fases eliminatorias futuras)
    public List<Partido> listarFinalizadosPorSeleccion(Long seleccionId) {
        return em.createQuery(
                "SELECT p FROM Partido p WHERE (p.seleccionLocal.id = :id OR p.seleccionVisitante.id = :id) AND p.estado = 'FINALIZADO'",
                Partido.class)
                .setParameter("id", seleccionId)
                .getResultList();
    }
}