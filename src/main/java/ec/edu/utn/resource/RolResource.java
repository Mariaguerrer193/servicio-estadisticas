package ec.edu.utn.resource;

import ec.edu.utn.model.Rol;
import ec.edu.utn.repository.RolRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
public class RolResource {

    @Inject
    private RolRepository repo;

    // GET /api/roles — listar todos
    @GET
    public List<Rol> listar() {
        return repo.listarTodos();
    }

    // GET /api/roles/{id} — buscar uno
    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") Long id) {
        Optional<Rol> rol = repo.buscarPorId(id);
        if (rol.isPresent()) {
            return Response.ok(rol.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}