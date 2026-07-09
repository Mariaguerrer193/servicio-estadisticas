package ec.edu.utn.resource;

import ec.edu.utn.model.Auditoria;
import ec.edu.utn.repository.AuditoriaRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/auditoria")
@Produces(MediaType.APPLICATION_JSON)
public class AuditoriaResource {

    @Inject
    private AuditoriaRepository repo;

    // GET /api/auditoria — listar historial de acciones administrativas
    @GET
    public List<Auditoria> listar() {
        return repo.listarTodas();
    }
}
