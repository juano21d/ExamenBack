# Sistema de Control de Estudiantes

Sistema web completo para el control de estudiantes con autenticación segura, gestión de roles (administrador y estudiante), y API REST documentada.

## 🚀 Características

### Autenticación y Seguridad

- ✅ Autenticación con JWT
- ✅ Roles diferenciados (ADMIN y ESTUDIANTE)
- ✅ Rutas protegidas según el rol
- ✅ Encriptación de contraseñas con BCrypt

### Funcionalidades por Rol

#### Administrador

- ✅ Puede iniciar sesión
- ✅ Gestionar (crear, listar, editar, eliminar) todos los estudiantes
- ✅ Buscar estudiantes por carrera, semestre o nombre
- ✅ Ver estadísticas del sistema

#### Estudiante

- ✅ Puede iniciar sesión
- ✅ Solo puede ver su propia información
- ✅ Puede actualizar su perfil personal
- ✅ No puede crear ni eliminar otros estudiantes

### Datos del Estudiante

- ID (generado automáticamente)
- Nombre y Apellido
- Email (único)
- Cédula (única)
- Carrera
- Semestre (1-12)
- Fecha de nacimiento
- Teléfono (opcional)
- Dirección (opcional)
- Contraseña (encriptada)
- Rol (ADMIN/ESTUDIANTE)
- Estado activo
- Fechas de creación y actualización

## 🛠️ Tecnologías Utilizadas

### Backend

- **Spring Boot 3.2.0** - Framework principal
- **Spring Security** - Autenticación y autorización
- **Spring Data JPA** - Persistencia de datos
- **PostgreSQL** - Base de datos
- **JWT (JSON Web Tokens)** - Autenticación
- **Maven** - Gestión de dependencias
- **Swagger/OpenAPI** - Documentación de API
- **Spring Boot Actuator** - Monitoreo

### Dependencias Principales

```xml
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-security
- spring-boot-starter-validation
- spring-boot-starter-actuator
- postgresql
- jjwt (JSON Web Token)
- springdoc-openapi
```

## 📋 Requisitos Previos

- Java 17 o superior
- Maven 3.6+
- PostgreSQL 12+
- IDE (IntelliJ IDEA, VS Code, Eclipse)

## ⚙️ Configuración

### 1. Base de Datos

Crear base de datos PostgreSQL:

```sql
CREATE DATABASE Final;
```

### 2. Configuración de Aplicación

El archivo `application.properties` está configurado con:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/Final
spring.datasource.username=postgres
spring.datasource.password=086113432

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8081
jwt.expiration=3600000
jwt.secret=miClaveSecretaMuySegura123
```

## 🚀 Instalación

1. Instala Java 17+ y PostgreSQL.
2. Abre la carpeta `Backend` en tu IDE favorito (IntelliJ, Eclipse, VS Code).
3. Configura la base de datos en `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/estudiantes
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_contraseña
   ```
4. Compila el proyecto:
   - Con Maven:
     ```sh
     mvn clean install
     ```
   - O desde el IDE: botón "Build" o "Compilar".

## ▶️ Ejecución

1. Inicia el backend:
   - Con Maven:
     ```sh
     mvn spring-boot:run
     ```
   - O desde el IDE: botón "Run" o "Ejecutar" en `ControlEstudiantesApplication.java`.
2. El backend estará disponible en [http://localhost:8081](http://localhost:8081)

## 🧪 Pruebas

Para ejecutar las pruebas automatizadas:
- Con Maven:
  ```sh
  mvn test
  ```
- O desde el IDE: botón "Run Test" en los archivos de test.

## ℹ️ Notas
- El backend usa JWT para autenticación.
- Puedes modificar los datos iniciales en los scripts SQL de la carpeta raíz.

## 📚 Documentación de API

### Endpoints Principales

#### Autenticación

- `POST /api/auth/login` - Login común para ambos roles

#### Gestión de Estudiantes (requiere autenticación)

- `GET /api/estudiantes` - **Solo ADMIN** - Listar todos los estudiantes
- `POST /api/estudiantes/crear` - **Solo ADMIN** - Crear nuevo estudiante
- `GET /api/estudiantes/{id}` - **ADMIN puede ver cualquiera / ESTUDIANTE solo el suyo**
- `PUT /api/estudiantes/{id}` - **ADMIN puede editar cualquiera / ESTUDIANTE solo el suyo**
- `DELETE /api/estudiantes/{id}` - **Solo ADMIN** - Eliminar estudiante
- `GET /api/estudiantes/perfil` - Ver perfil propio (ambos roles)

#### Búsquedas (Solo ADMIN)

- `GET /api/estudiantes/buscar/carrera/{carrera}` - Buscar por carrera
- `GET /api/estudiantes/buscar/semestre/{semestre}` - Buscar por semestre
- `GET /api/estudiantes/buscar/nombre?nombre={nombre}` - Buscar por nombre

### Usuarios de Prueba

La aplicación crea automáticamente usuarios de prueba:

**Administrador:**

- Email: `admin@sistema.com`
- Contraseña: `admin123`

**Estudiantes:**

- Email: `estudiante1@test.com` / Contraseña: `123456`
- Email: `estudiante2@test.com` / Contraseña: `123456`

## 🔐 Uso de la API

### 1. Autenticación

```bash
POST /api/auth/login
Content-Type: application/json

{
  "email": "admin@sistema.com",
  "password": "admin123"
}
```

**Respuesta:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer",
  "id": 1,
  "email": "admin@sistema.com",
  "nombreCompleto": "Administrador Sistema",
  "rol": "ADMIN",
  "expiracion": 1640995200000
}
```

### 2. Usar Token en Requests

```bash
Authorization: Bearer {token}
```

### 3. Ejemplo: Crear Estudiante (Solo Admin)

```bash
POST /api/estudiantes/crear
Authorization: Bearer {token}
Content-Type: application/json

{
  "nombre": "Carlos",
  "apellido": "López",
  "email": "carlos@test.com",
  "cedula": "11223344",
  "carrera": "Ingeniería Industrial",
  "semestre": 4,
  "fechaNacimiento": "1999-05-10",
  "telefono": "555123456",
  "direccion": "Calle 50 #25-30",
  "password": "123456"
}
```

## 🧪 Pruebas

### Ejecutar Tests

```bash
mvn test
```

### Pruebas Incluidas

- Test de contexto de Spring Boot
- Tests de integración de controladores
- Tests de servicios
- Tests de seguridad

## 📊 Monitoreo

### Endpoints de Actuator Disponibles

- `/actuator/health` - Estado de la aplicación
- `/actuator/info` - Información de la aplicación
- `/actuator/env` - Variables de entorno
- `/actuator/beans` - Beans de Spring
- `/actuator/mappings` - Mapeo de endpoints

## 🔧 Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/estudiantes/controlEstudiantes/
│   │   ├── config/          # Configuraciones
│   │   ├── controller/      # Controladores REST
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # Entidades JPA
│   │   ├── exception/      # Manejo de excepciones
│   │   ├── repository/     # Repositorios JPA
│   │   ├── security/       # Configuración de seguridad
│   │   ├── service/        # Lógica de negocio
│   │   └── ControlEstudiantesApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/estudiantes/controlEstudiantes/
        └── Tests unitarios e integración
```

## 🛡️ Seguridad

- **JWT Tokens**: Expiran en 1 hora (configurable)
- **CORS**: Configurado para desarrollo
- **Validaciones**: Campos obligatorios y formatos
- **Encriptación**: BCrypt para contraseñas
- **Roles**: Control de acceso basado en roles

## 🐛 Solución de Problemas

### Error de Conexión a Base de Datos

1. Verificar que PostgreSQL esté ejecutándose
2. Confirmar credenciales en `application.properties`
3. Verificar que la base de datos `Final` exista

### Token JWT Inválido

1. Verificar que el token no haya expirado
2. Incluir `Bearer ` antes del token
3. Verificar que el secreto JWT sea correcto

### Problemas de CORS

1. Verificar configuración en `WebSecurityConfig`
2. Ajustar `cors.allowed-origins` en properties

## 📈 Próximas Mejoras

- [ ] Paginación en listados
- [ ] Filtros avanzados
- [ ] Exportación de datos
- [ ] Notificaciones por email
- [ ] Dashboard con estadísticas
- [ ] API para carga masiva de estudiantes
