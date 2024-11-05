# Dockerfile para payroll-postgres
FROM postgres
ENV POSTGRES_PASSWORD=password

# Copiar el script de inicialización al directorio de inicio de PostgreSQL
COPY init.sql /docker-entrypoint-initdb.d/
