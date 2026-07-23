from db.conexion import conectar

conn = conectar()
if conn:
    print("✅ Conexión exitosa a Supabase.")
    conn.close()
else:
    print("❌ No se pudo conectar.")
