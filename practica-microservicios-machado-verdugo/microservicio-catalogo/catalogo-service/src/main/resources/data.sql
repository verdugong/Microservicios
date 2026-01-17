INSERT INTO products (id, name, price, stock) VALUES (1, 'Cuaderno cuadriculado', 2.50, 100) ON CONFLICT (id) DO NOTHING;
INSERT INTO products (id, name, price, stock) VALUES (2, 'Lapicero azul', 1.00, 200) ON CONFLICT (id) DO NOTHING;
INSERT INTO products (id, name, price, stock) VALUES (3, 'Marcador permanente', 1.75, 150) ON CONFLICT (id) DO NOTHING;
