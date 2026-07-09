package ec.edu.utn.resource;

import ec.edu.utn.model.Fase;
import ec.edu.utn.repository.FaseRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/fases")
@Produces(MediaType.APPLICATION_JSON)
public class FaseResource {

    @Inject
    private FaseRepository repo;

    // GET /api/fases — listar todas
    @GET
    public List<Fase> listar() {
        return repo.listarTodas();
    }

    // GET /api/fases/{codigo} — buscar una
    @GET
    @Path("/{codigo}")
    public Response buscar(@PathParam("codigo") String codigo) {
        Optional<Fase> fase = repo.buscarPorCodigo(codigo);
        if (fase.isPresent()) {
            return Response.ok(fase.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}