package ec.edu.utn.security;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

@RequiereRol({})
@Provider
@Priority(Priorities.AUTHORIZATION)
public class RolFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method metodo = resourceInfo.getResourceMethod();
        RequiereRol anotacion = metodo.getAnnotation(RequiereRol.class);

        if (anotacion == null) {
            return; // este endpoint no exige un rol específico
        }

        String rolUsuario = (String) requestContext.getProperty("rol");

        boolean tienePermiso = rolUsuario != null &&
                Arrays.asList(anotacion.value()).contains(rolUsuario);

        if (!tienePermiso) {
            requestContext.abortWith(
                    Response.status(Response.Status.FORBIDDEN)
                            .entity("{\"error\": \"No tienes permiso para realizar esta acción\"}")
                            .build()
            );
        }
    }
}