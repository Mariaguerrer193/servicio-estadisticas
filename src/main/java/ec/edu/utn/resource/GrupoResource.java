package ec.edu.utn.resource;

import ec.edu.utn.model.Grupo;
import ec.edu.utn.repository.GrupoRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/grupos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GrupoResource {

    @Inject
    private GrupoRepository repo;

    // GET /api/grupos — listar todos
    @GET
    public List<Grupo> listar() {
        return repo.listarTodos();
    }

    // GET /api/grupos/{codigo} — buscar uno
    @GET
    @Path("/{codigo}")
    public Response buscar(@PathParam("codigo") String codigo) {
        Optional<Grupo> grupo = repo.buscarPorCodigo(codigo);
        if (grupo.isPresent()) {
            return Response.ok(grupo.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // POST /api/grupos — crear uno nuevo
    @POST
    public Response crear(Grupo grupo) {
        Grupo creado = repo.crear(grupo);
        return Response.status(Response.Status.CREATED).entity(creado).build();
    }
}