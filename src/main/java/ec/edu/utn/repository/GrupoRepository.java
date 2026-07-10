package ec.edu.utn.repository;

import java.util.List;
import java.util.Optional;

import ec.edu.utn.model.Grupo;
import ec.edu.utn.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class GrupoRepository {

    @PersistenceContext(unitName = "EstadisticasPU")
    private EntityManager em;

    @Inject
    private AuditoriaRepository auditoriaRepo;

    @Inject
    private UsuarioRepository usuarioRepo;

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


    // Actualizar
    @Transactional
    public Optional<Grupo> actualizar(String codigo, Grupo datos, Long usuarioId) {
        Grupo grupo = em.find(Grupo.class, codigo);
        if (grupo == null) {
            return Optional.empty();
        }
        grupo.setNombre(datos.getNombre());

        Usuario usuario = usuarioId != null ? usuarioRepo.buscarPorId(usuarioId).orElse(null) : null;
        auditoriaRepo.registrar(usuario, "ACTUALIZAR_GRUPO", "GRUPO", codigo, "Datos actualizados");

        return Optional.of(grupo);
    }



}