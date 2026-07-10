package ec.edu.utn.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Priority;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@RequiereAutenticacion
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AutenticacionFilter implements ContainerRequestFilter {


    @Context
    private HttpServletRequest servletRequest;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authHeader = requestContext.getHeaderString("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            rechazar(requestContext, "Falta el token de autenticación");
            return;
        }

        String token = authHeader.substring("Bearer ".length());

        try {
           DecodedJWT decoded = JwtUtil.validarToken(token);
            // Guardamos los datos del usuario en la petición, para que los endpoints los puedan usar
            requestContext.setProperty("userId", decoded.getClaim("userId").asLong());
            requestContext.setProperty("username", decoded.getSubject());
            requestContext.setProperty("rol", decoded.getClaim("rol").asString());

            servletRequest.setAttribute("userId", decoded.getClaim("userId").asLong());
            servletRequest.setAttribute("username", decoded.getSubject());
            servletRequest.setAttribute("rol", decoded.getClaim("rol").asString());
        } catch (JWTVerificationException e) {
            rechazar(requestContext, "Token inválido o expirado");
        }
    }

    private void rechazar(ContainerRequestContext requestContext, String mensaje) {
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"" + mensaje + "\"}")
                        .build()
        );
    }
}