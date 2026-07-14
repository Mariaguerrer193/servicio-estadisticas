package ec.edu.utn.resource;

import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/{path : .*}")
public class CorsOptionsResource {

    @OPTIONS
    public Response opciones() {
        return Response.ok().build();
    }
}