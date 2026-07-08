package ec.edu.utn.resource;

import ec.edu.utn.model.Seleccion;
import ec.edu.utn.repository.SeleccionRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/selecciones")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SeleccionResource {

    @Inject
    private SeleccionRepository repo;

    // GET /api/selecciones — listar todas (o filtradas por grupo)
    @GET
    public List<Seleccion> listar(@QueryParam("grupo") String grupo) {
        if (grupo != null && !grupo.isBlank()) {
            return repo.listarPorGrupo(grupo);
        }
        return repo.listarTodas();
    }

    // GET /api/selecciones/{id} — buscar una
    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") Long id) {
        Optional<Seleccion> seleccion = repo.buscarPorId(id);
        if (seleccion.isPresent()) {
            return Response.ok(seleccion.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // POST /api/selecciones — crear una nueva
    @POST
    public Response crear(Seleccion seleccion) {
        Seleccion creada = repo.crear(seleccion);
        return Response.status(Response.Status.CREATED).entity(creada).build();
    }
}