package com.ordenes.ordenes.modelo;

import java.util.List;

public class SolicitudOrden {
    private List<Long> products;
    private String destination;

    public List<Long> getProducts() { return products; }
    public String getDestination() { return destination; }
    public void setProducts(List<Long> products) { this.products = products; }
    public void setDestination(String destination) { this.destination = destination; }
}
