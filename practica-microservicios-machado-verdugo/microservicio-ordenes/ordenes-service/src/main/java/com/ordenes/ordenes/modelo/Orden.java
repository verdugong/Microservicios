package com.ordenes.ordenes.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordenes")
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total")
    private Double total;

    @Column(name = "costo_envio")
    private Double costoEnvio;

    @Column(name = "fecha")
    private LocalDateTime fecha = LocalDateTime.now();

    public Long getId() { return id; }
    public Double getTotal() { return total; }
    public Double getCostoEnvio() { return costoEnvio; }
    public LocalDateTime getFecha() { return fecha; }

    public void setId(Long id) { this.id = id; }
    public void setTotal(Double total) { this.total = total; }
    public void setCostoEnvio(Double costoEnvio) { this.costoEnvio = costoEnvio; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
