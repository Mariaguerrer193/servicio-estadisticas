package ec.edu.utn.resource;

import ec.edu.utn.model.Usuario;
import ec.edu.utn.repository.UsuarioRepository;
import ec.edu.utn.security.JwtUtil;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    private UsuarioRepository usuarioRepo;

    // POST /api/auth/login
    @POST
    @Path("/login")
    public Response login(LoginRequest req) {
        Optional<Usuario> usuario = usuarioRepo.verificarCredenciales(req.username, req.password);

        if (usuario.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Usuario o contraseña incorrectos\"}")
                    .build();
        }

        String token = JwtUtil.generarToken(usuario.get());

        LoginResponse resp = new LoginResponse();
        resp.token = token;
        resp.username = usuario.get().getUsername();
        resp.nombre = usuario.get().getNombre();
        resp.rol = usuario.get().getRol().getNombre();

        return Response.ok(resp).build();
    }

    // Clase para recibir el JSON de entrada (username + password)
    public static class LoginRequest {
        public String username;
        public String password;
    }

    // Clase para devolver el token + datos básicos del usuario
    public static class LoginResponse {
        public String token;
        public String username;
        public String nombre;
        public String rol;
    }
}