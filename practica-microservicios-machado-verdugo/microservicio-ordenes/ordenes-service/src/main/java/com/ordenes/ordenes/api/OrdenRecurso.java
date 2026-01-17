package com.ordenes.ordenes.api;

import com.ordenes.ordenes.cliente.CatalogoCliente;
import com.ordenes.ordenes.cliente.EnvioCliente;
import com.ordenes.ordenes.modelo.Orden;
import com.ordenes.ordenes.modelo.ProductoDTO;
import com.ordenes.ordenes.modelo.SolicitudOrden;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.*;

@Path("/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OrdenRecurso {

    @Inject
    @RestClient
    EnvioCliente envioCliente;

    @Inject
    @RestClient
    CatalogoCliente catalogoCliente;

    @PersistenceContext
    EntityManager em;

    @POST
    @Transactional
    public Response crear(SolicitudOrden solicitud) {
        if (solicitud == null || solicitud.getProducts() == null || solicitud.getProducts().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "Lista de productos vacía")).build();
        }

        // Paso A: calcular envío en servicio Python
        Map<String, Object> bodyEnvio = new HashMap<>();
        bodyEnvio.put("products", solicitud.getProducts());
        bodyEnvio.put("destination", Optional.ofNullable(solicitud.getDestination()).orElse(""));
        Map<String, Object> respEnvio = envioCliente.calcular(bodyEnvio);
        double costoEnvio = ((Number) respEnvio.getOrDefault("shippingCost", 0.0)).doubleValue();

        // Paso B: consultar precios y descontar stock en el catálogo
        double subtotal = 0.0;
        for (Long idProd : solicitud.getProducts()) {
            ProductoDTO p = catalogoCliente.obtener(idProd);
            if (p == null || p.price == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("error", "Producto no válido: " + idProd)).build();
            }
            subtotal += p.price;
            catalogoCliente.descontar(idProd, 1);
        }

        // Paso C: persistir la orden
        Orden orden = new Orden();
        orden.setCostoEnvio(costoEnvio);
        orden.setTotal(subtotal + costoEnvio);
        em.persist(orden);

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("id", orden.getId());
        respuesta.put("total", orden.getTotal());
        respuesta.put("shippingCost", orden.getCostoEnvio());
        return Response.status(Response.Status.CREATED).entity(respuesta).build();
    }
}
