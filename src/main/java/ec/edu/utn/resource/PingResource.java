package ec.edu.utn.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/ping")
@Produces(MediaType.APPLICATION_JSON)
public class PingResource {

    @GET
    public String ping() {
        return "{\"mensaje\": \"Servicio de Estadisticas funcionando\"}";
    }
}