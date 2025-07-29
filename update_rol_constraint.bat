@echo off
echo Conectando a PostgreSQL para actualizar la restriccion de roles...
echo.

REM Comando para eliminar la restriccion CHECK del rol
"C:\Program Files\PostgreSQL\15\bin\psql.exe" -U postgres -d Final -c "ALTER TABLE estudiantes DROP CONSTRAINT IF EXISTS estudiantes_rol_check;"

REM Comando para agregar la nueva restriccion que incluye PROFESOR
"C:\Program Files\PostgreSQL\15\bin\psql.exe" -U postgres -d Final -c "ALTER TABLE estudiantes ADD CONSTRAINT estudiantes_rol_check CHECK (rol IN ('ADMIN', 'ESTUDIANTE', 'PROFESOR'));"

echo.
echo Restriccion actualizada exitosamente!
echo Ahora puedes ejecutar la aplicacion Spring Boot.
pause
