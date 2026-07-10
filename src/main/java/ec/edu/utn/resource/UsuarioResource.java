package ec.edu.utn.resource;

import ec.edu.utn.model.Rol;
import ec.edu.utn.model.Usuario;
import ec.edu.utn.repository.RolRepository;
import ec.edu.utn.repository.UsuarioRepository;
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


@Path("/usuarios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    private UsuarioRepository repo;

    @Inject
    private RolRepository rolRepo;

    @Context
    private HttpServletRequest servletRequest;

    // GET /api/usuarios — listar todos (sin exponer password_hash, gracias a @JsonIgnore)
    @GET
    public List<Usuario> listar() {
        return repo.listarTodos();
    }

    // GET /api/usuarios/{id} — buscar uno
    @GET
    @Path("/{id}")
    public Response buscar(@PathParam("id") Long id) {
        Optional<Usuario> usuario = repo.buscarPorId(id);
        if (usuario.isPresent()) {
            return Response.ok(usuario.get()).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // POST /api/usuarios — crear uno nuevo
    @POST
    public Response crear(RegistroRequest req) {
        Optional<Rol> rol = rolRepo.buscarPorId(req.rolId);
        if (rol.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"El rol especificado no existe\"}")
                    .build();
        }

        Usuario u = new Usuario();
        u.setUsername(req.username);
        u.setNombre(req.nombre);
        u.setEmail(req.email);
        u.setRol(rol.get());

        try {
            Usuario creado = repo.crear(u, req.password);
            return Response.status(Response.Status.CREATED).entity(creado).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    // Clase interna para recibir el JSON de registro (incluye password en texto plano, solo de entrada)
    public static class RegistroRequest {
        public String username;
        public String nombre;
        public String email;
        public String password;
        public Long rolId;
    }

    // PUT /api/usuarios/{id} — actualizar datos básicos (NO contraseña)
    @RequiereAutenticacion
    @RequiereRol("ADMINISTRADOR")
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") Long id, ActualizarRequest req) {
        Optional<Rol> rol = rolRepo.buscarPorId(req.rolId);
        if (rol.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"El rol especificado no existe\"}")
                    .build();
        }

        Long usuarioQueEdita = (Long) servletRequest.getAttribute("userId");

        try {
            Optional<Usuario> actualizado = repo.actualizar(id, req.nombre, req.email, rol.get(), usuarioQueEdita);
            if (actualizado.isPresent()) {
                return Response.ok(actualizado.get()).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    // Clase para recibir el JSON de actualización (sin password)
    public static class ActualizarRequest {
        public String nombre;
        public String email;
        public Long rolId;
    }
}