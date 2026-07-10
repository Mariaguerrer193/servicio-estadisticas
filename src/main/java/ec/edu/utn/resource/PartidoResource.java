package ec.edu.utn.resource;

import ec.edu.utn.model.Partido;
import ec.edu.utn.repository.PartidoRepository;
import ec.edu.utn.security.RequiereAutenticacion;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/partidos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PartidoResource {

    @Inject
    private PartidoRepository repo;

    // GET /api/partidos — calendario completo (o filtrado por grupo)
    @GET
    public List<Partido> listar(@QueryParam("grupo") String grupo) {
        if (grupo != null && !grupo.isBlank()) {
            return repo.listarPorGrupo(grupo);
        }
        return repo.listarTodos();
    }

    // GET /api/partidos/{id} — detalle de un partido
    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") Long id) {
        Optional<Partido> partido = repo.buscarPorId(id);
        if (partido.isPresent()) {
            return Response.ok(partido.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // POST /api/partidos — crear uno nuevo (para fases eliminatorias futuras)
    @POST
    public Response crear(Partido partido) {
        Partido creado = repo.crear(partido);
        return Response.status(Response.Status.CREATED).entity(creado).build();
    }

    // PUT /api/partidos/{id}/resultado — registrar resultado oficial (RF11)
    @RequiereAutenticacion
    @PUT
    @Path("/{id}/resultado")
    public Response registrarResultado(@PathParam("id") Long id, ResultadoRequest resultado) {
        Optional<Partido> actualizado = repo.registrarResultado(id, resultado.golesLocal, resultado.golesVisitante);
        if (actualizado.isPresent()) {
            return Response.ok(actualizado.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // Clase interna simple para recibir el JSON del resultado
    public static class ResultadoRequest {
        public Integer golesLocal;
        public Integer golesVisitante;
    }
}