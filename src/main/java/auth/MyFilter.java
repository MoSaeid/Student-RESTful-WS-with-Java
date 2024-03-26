package auth;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import java.util.Base64;
import java.util.List;

/**
 * This class implements the ContainerRequestFilter and ContainerResponseFilter interfaces
 * to provide filtering functionality for incoming and outgoing requests.
 */
@jakarta.ws.rs.ext.Provider
public class MyFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) {
        List<String> authHeader = requestContext.getHeaders().get("Authorization");
        String token, credentials;

        if (authHeader != null && !authHeader.isEmpty()) {
            token = authHeader.get(0).replaceFirst("Basic ", "");
            credentials = new String(Base64.getDecoder().decode(token));
            System.out.println("Credentials: " + credentials);
            String[] params = credentials.split(":");
            if (params[0].equals("admin") && params[1].equals("1234")) {
                return;
            }
        }
        throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }

    @Override
    public void filter(ContainerRequestContext crc, ContainerResponseContext crc1) {
        crc1.getHeaders().add("PoweredBy", "Mohammed & Akram");
    }
}