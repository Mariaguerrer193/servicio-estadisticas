package ec.edu.utn.resource;

import ec.edu.utn.model.Sede;
import ec.edu.utn.repository.SedeRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import ec.edu.utn.security.RequiereAutenticacion;
import ec.edu.utn.security.RequiereRol;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Context;

@Path("/sedes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SedeResource {

    @Inject
    private SedeRepository repo;

    @Context
    private HttpServletRequest servletRequest;

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

    // PUT /api/sedes/{id} — actualizar
    @RequiereAutenticacion
    @RequiereRol("ADMINISTRADOR")
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") Long id, Sede datos) {
        Long usuarioId = (Long) servletRequest.getAttribute("userId");
        Optional<Sede> actualizada = repo.actualizar(id, datos, usuarioId);
        if (actualizada.isPresent()) {
            return Response.ok(actualizada.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}