package ec.edu.utn.repository;

import ec.edu.utn.model.Auditoria;
import ec.edu.utn.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class AuditoriaRepository {

    @PersistenceContext(unitName = "EstadisticasPU")
    private EntityManager em;

    // Listar todas (más recientes primero)
    public List<Auditoria> listarTodas() {
        return em.createQuery("SELECT a FROM Auditoria a ORDER BY a.fechaHora DESC", Auditoria.class)
                .getResultList();
    }

    // Método reutilizable: otros repositorios lo llaman para dejar registro de una acción
    @Transactional
    public void registrar(Usuario usuario, String accion, String entidadAfectada, Long entidadId, String detalle) {
        Auditoria a = new Auditoria(usuario, accion, entidadAfectada, entidadId, detalle);
        em.persist(a);
    }
}
