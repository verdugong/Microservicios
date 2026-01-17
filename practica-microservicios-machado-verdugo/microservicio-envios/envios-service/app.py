from flask import Flask, request, jsonify

app = Flask(__name__)

@app.get("/health")
def salud():
    return {"status": "ok"}, 200

@app.post("/shipping/calculate")
def calcular_envio():
    datos = request.get_json(silent=True) or {}
    productos = datos.get("products", [])
    destino = datos.get("destination", "")

    # Lógica simple: costo base + 0.5 por cada producto
    costo_base = 3.0
    costo_por_item = 0.5
    cantidad = len(productos)

    # Podrían aplicarse reglas por destino en el futuro
    costo_envio = round(costo_base + costo_por_item * cantidad, 2)

    return jsonify({"shippingCost": costo_envio, "destination": destino, "items": cantidad}), 200

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8082)
