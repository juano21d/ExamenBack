-- Script para actualizar la base de datos y permitir el rol PROFESOR
-- Ejecutar en PostgreSQL como usuario postgres en la base de datos Final

-- 1. Eliminar la restricción actual del rol
ALTER TABLE estudiantes DROP CONSTRAINT estudiantes_rol_check;

-- 2. Agregar la nueva restricción que incluye PROFESOR
ALTER TABLE estudiantes ADD CONSTRAINT estudiantes_rol_check 
    CHECK (rol IN ('ADMIN', 'ESTUDIANTE', 'PROFESOR'));

-- 3. Verificar que la restricción se aplicó correctamente
SELECT constraint_name, search_condition
FROM user_constraints
WHERE table_name = 'ESTUDIANTES'
AND constraint_name = 'ESTUDIANTES_ROL_CHECK';

-- 4. Opcional: Ver todos los roles existentes en la tabla
SELECT DISTINCT rol FROM estudiantes;
