package ec.edu.utn;

import ec.edu.utn.resource.*;
import ec.edu.utn.security.AutenticacionFilter;
import ec.edu.utn.security.CorsFilter;
import ec.edu.utn.security.RolFilter;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

        // Recursos de tu API
        classes.add(PingResource.class);
        classes.add(SedeResource.class);
        classes.add(GrupoResource.class);
        classes.add(FaseResource.class);
        classes.add(SeleccionResource.class);
        classes.add(PartidoResource.class);
        classes.add(RolResource.class);
        classes.add(UsuarioResource.class);
        classes.add(AuditoriaResource.class);
        classes.add(AuthResource.class);
        classes.add(CorsOptionsResource.class);

        // Filtros de seguridad
        classes.add(AutenticacionFilter.class);
        classes.add(RolFilter.class);
        classes.add(CorsFilter.class);

        // Documentación OpenAPI
        classes.add(OpenApiResource.class);

        return classes;
    }
}