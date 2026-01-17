package com.ordenes.ordenes.cliente;

import com.ordenes.ordenes.modelo.ProductoDTO;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "catalogo")
@Path("/products")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface CatalogoCliente {

    @GET
    @Path("/{id}")
    ProductoDTO obtener(@PathParam("id") Long id);

    @POST
    @Path("/{id}/decrease-stock")
    ProductoDTO descontar(@PathParam("id") Long id, @QueryParam("quantity") int cantidad);
}
