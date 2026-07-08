package ec.edu.utn.resource;

import ec.edu.utn.model.Sede;
import ec.edu.utn.repository.SedeRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/sedes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SedeResource {

    @Inject
    private SedeRepository repo;

    // GET /api/sedes — listar todas
    @GET
    public List<Sede> listar() {
        return repo.listarTodas();
    }

    // GET /api/sedes/{id} — buscar una
    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") Long id) {
        Optional<Sede> sede = repo.buscarPorId(id);
        if (sede.isPresent()) {
            return Response.ok(sede.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // POST /api/sedes — crear una nueva
    @POST
    public Response crear(Sede sede) {
        Sede creada = repo.crear(sede);
        return Response.status(Response.Status.CREATED).entity(creada).build();
    }
}