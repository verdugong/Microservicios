import React, { useEffect, useState } from "react";

function App() {
  const [products, setProducts] = useState([]);
  const [cart, setCart] = useState([]);
  const [destination, setDestination] = useState("");
  const [orderResult, setOrderResult] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetch("/api/products")
      .then(res => res.json())
      .then(data => setProducts(data))
      .catch(err => console.error("Error cargando productos:", err));
  }, []);

  const addToCart = (productId) => {
    setCart([...cart, productId]);
  };

  const removeFromCart = (index) => {
    setCart(cart.filter((_, i) => i !== index));
  };

  const handleBuy = async () => {
    if (cart.length === 0) {
      alert("El carrito está vacío");
      return;
    }
    setLoading(true);
    setOrderResult(null);

    try {
      const response = await fetch("/api/orders", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          products: cart,
          destination: destination || "Local"
        })
      });

      const data = await response.json();

      if (response.ok) {
        setOrderResult({ success: true, data });
        setCart([]);
        // Recargar productos para ver stock actualizado
        const prodResponse = await fetch("/api/products");
        const prodData = await prodResponse.json();
        setProducts(prodData);
      } else {
        setOrderResult({ success: false, error: data.error || "Error al crear orden" });
      }
    } catch (err) {
      setOrderResult({ success: false, error: err.message });
    } finally {
      setLoading(false);
    }
  };

  const getProductName = (id) => {
    const product = products.find(p => p.id === id);
    return product ? product.name : `Producto ${id}`;
  };

  return (
    <div style={{ padding: "20px", fontFamily: "Arial, sans-serif", maxWidth: "800px", margin: "0 auto" }}>
      <h1>🛒 Tienda de Microservicios</h1>
      
      <h2>Catálogo de Productos</h2>
      <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(200px, 1fr))", gap: "15px" }}>
        {products.map(p => (
          <div key={p.id} style={{ border: "1px solid #ddd", padding: "15px", borderRadius: "8px" }}>
            <h3>{p.name}</h3>
            <p><strong>Precio:</strong> ${p.price}</p>
            <p><strong>Stock:</strong> {p.stock}</p>
            <button 
              onClick={() => addToCart(p.id)}
              disabled={p.stock <= 0}
              style={{ 
                padding: "8px 16px", 
                backgroundColor: p.stock > 0 ? "#4CAF50" : "#ccc", 
                color: "white", 
                border: "none", 
                borderRadius: "4px",
                cursor: p.stock > 0 ? "pointer" : "not-allowed"
              }}
            >
              {p.stock > 0 ? "Agregar al carrito" : "Sin stock"}
            </button>
          </div>
        ))}
      </div>

      <hr style={{ margin: "30px 0" }} />

      <h2>🛍️ Carrito ({cart.length} items)</h2>
      {cart.length === 0 ? (
        <p>El carrito está vacío</p>
      ) : (
        <ul>
          {cart.map((id, index) => (
            <li key={index}>
              {getProductName(id)} 
              <button 
                onClick={() => removeFromCart(index)}
                style={{ marginLeft: "10px", color: "red", cursor: "pointer" }}
              >
                ❌
              </button>
            </li>
          ))}
        </ul>
      )}

      <div style={{ marginTop: "20px" }}>
        <label>
          <strong>Destino de envío:</strong>
          <input 
            type="text" 
            value={destination} 
            onChange={(e) => setDestination(e.target.value)}
            placeholder="Ej: Madrid, Barcelona..."
            style={{ marginLeft: "10px", padding: "8px", width: "200px" }}
          />
        </label>
      </div>

      <button 
        onClick={handleBuy}
        disabled={loading || cart.length === 0}
        style={{ 
          marginTop: "20px",
          padding: "12px 24px", 
          backgroundColor: "#2196F3", 
          color: "white", 
          border: "none", 
          borderRadius: "4px",
          fontSize: "16px",
          cursor: loading || cart.length === 0 ? "not-allowed" : "pointer"
        }}
      >
        {loading ? "Procesando..." : "🛒 Comprar"}
      </button>

      {orderResult && (
        <div style={{ 
          marginTop: "20px", 
          padding: "15px", 
          borderRadius: "8px",
          backgroundColor: orderResult.success ? "#d4edda" : "#f8d7da",
          color: orderResult.success ? "#155724" : "#721c24"
        }}>
          {orderResult.success ? (
            <>
              <h3>✅ ¡Orden creada exitosamente!</h3>
              <p><strong>ID de Orden:</strong> {orderResult.data.id}</p>
              <p><strong>Costo de Envío:</strong> ${orderResult.data.shippingCost}</p>
              <p><strong>Total:</strong> ${orderResult.data.total}</p>
            </>
          ) : (
            <>
              <h3>❌ Error al crear la orden</h3>
              <p>{orderResult.error}</p>
            </>
          )}
        </div>
      )}
    </div>
  );
}

export default App;

