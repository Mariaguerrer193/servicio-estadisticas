package ec.edu.utn.resource;


import java.util.List;
import java.util.Optional;

import ec.edu.utn.dto.ResultadoRequest;
import ec.edu.utn.model.Partido;
import ec.edu.utn.repository.PartidoRepository;
import ec.edu.utn.security.RequiereAutenticacion;
import ec.edu.utn.security.RequiereRol;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/partidos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PartidoResource {

    @Inject
    private PartidoRepository repo;

    @Context
    private HttpServletRequest servletRequest;

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
    @RequiereRol("ADMINISTRADOR")
    @PUT
    @Path("/{id}/resultado")
    public Response registrarResultado(
        @PathParam("id") Long id,
        ResultadoRequest resultado) {

    if (resultado == null
            || resultado.getGolesLocal() == null
            || resultado.getGolesVisitante() == null) {

        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Los goles local y visitante son obligatorios.")
                .build();
    }

    if (resultado.getGolesLocal() < 0 || resultado.getGolesVisitante() < 0) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Los goles no pueden ser negativos.")
                .build();
    }

    Optional<Partido> actualizado = repo.registrarResultado(
            id,
            resultado.getGolesLocal(),
            resultado.getGolesVisitante()
    );

    if (actualizado.isPresent()) {
        return Response.ok(actualizado.get()).build();
    }

    return Response.status(Response.Status.NOT_FOUND).build();
}

    // PUT /api/partidos/{id}/revertir — revertir resultado (vuelve a PROGRAMADO)
    @RequiereAutenticacion
    @RequiereRol("ADMINISTRADOR")
    @PUT
    @Path("/{id}/revertir")
    public Response revertir(@PathParam("id") Long id) {
        Long usuarioId = (Long) servletRequest.getAttribute("userId");
        Optional<Partido> revertido = repo.revertir(id, usuarioId);
        if (revertido.isPresent()) {
            return Response.ok(revertido.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }


    // PUT /api/partidos/{id} — actualizar datos generales (fecha, sede, fase, grupo, selecciones)
    @RequiereAutenticacion
    @RequiereRol("ADMINISTRADOR")
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") Long id, Partido datos) {
        Long usuarioId = (Long) servletRequest.getAttribute("userId");
        Optional<Partido> actualizado = repo.actualizar(id, datos, usuarioId);
        if (actualizado.isPresent()) {
            return Response.ok(actualizado.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}