# Práctica Microservicios (Gateway + Catálogo + Envíos + Órdenes)

Este repositorio implementa la arquitectura acordada:
- Gateway (Nginx) como único punto de entrada (puerto 8080)
- Frontend (React) servido detrás del Gateway
- Catálogo (Spring Boot + PostgreSQL)
- Envíos (Python + Flask)
- Órdenes (Jakarta EE + MicroProfile con Quarkus + MariaDB)
- Orquestación con Docker Compose

## Requisitos
- Docker y Docker Compose
- Java 17 y Maven Wrapper para empaquetar el catálogo la primera vez

## Primer build del Catálogo
El `Dockerfile` del catálogo espera un JAR en `target/`. Empaqueta:

```bash
cd microservicio-catalogo/catalogo-service
./mvnw -q -DskipTests package
```

## Levantar toda la arquitectura
Desde la raíz del proyecto:

```bash
docker compose up -d --build
```

Servicios clave:
- Gateway: http://localhost:8080
- API productos (vía gateway): http://localhost:8080/api/products
- API órdenes (vía gateway): POST http://localhost:8080/api/orders

## Probar Envíos (interno)
```bash
curl -X POST http://localhost:8080/api/orders \
  -H 'Content-Type: application/json' \
  -d '{"products":[1,2],"destination":"Quito"}'
```

## Notas
- Sólo el gateway expone puertos al host.
- Las dependencias entre servicios usan nombres DNS de Compose (p.ej. `catalog-service`).
- Healthchecks incluidos para DBs y servicios críticos.
- Si estás en Apple Silicon y ves problemas, puedes forzar `platform: linux/amd64` por servicio.
