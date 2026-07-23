@echo off
SETLOCAL

SET DB_NAME=gestor_turnos
SET DB_USER=postgres
SET DB_PASSWORD=admin
SET SQL_FILE=C:\Users\Mkjdf983\Documents\Tecnicatura\ProyectoIntegradorPython-2025-Solucion202\tablecreator\create_appointments_table.sql
SET PSQL_PATH=C:\Program Files\PostgreSQL\17\bin\psql.exe

SET PGPASSWORD=%DB_PASSWORD%

echo 🔌 Terminando conexiones activas...
"%PSQL_PATH%" -U %DB_USER% -d postgres -c "SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = '%DB_NAME%' AND pid <> pg_backend_pid();"

echo 🧨 Borrando base de datos %DB_NAME%...
"%PSQL_PATH%" -U %DB_USER% -d postgres -c "DROP DATABASE IF EXISTS %DB_NAME%;"

echo 🛠️ Creando base de datos %DB_NAME%...
"%PSQL_PATH%" -U %DB_USER% -d postgres -c "CREATE DATABASE %DB_NAME%;"

echo 📄 Ejecutando script SQL sobre %DB_NAME%...
"%PSQL_PATH%" -U %DB_USER% -d %DB_NAME% -f "%SQL_FILE%"

echo ✅ Base de datos %DB_NAME% recreada correctamente.
pause
ENDLOCAL
