# 🚀 gRPC Challenge - Kotlin + Spring Boot

Este proyecto es un servicio gRPC desarrollado en **Kotlin** con **Spring Boot 3**, siguiendo principios de Clean Code y buenas prácticas para desarrollo modular.
Incluye validaciones, respuesta dinámica, generación de actividades simuladas y dockerización completa.


## ✨ Características

- 📦 Arquitectura modular y limpia (`config`, `domain`, `grpc`, `service`, `proto`)
- ✅ Validaciones con clases especializadas
- 🧪 Pruebas unitarias usando `JUnit` y `Mockito`
- 🐳 Dockerfile y docker-compose incluidos
- ⚙️ Simulación de actividad de usuario en streaming
- ⚠️ Manejo de errores y validación personalizada


## 📁 Estructura del Proyecto

```
src/
 ├── main/
 │   ├── kotlin/com/lafuente/test/grpcchallengelafuente/
 │   │   ├── config/              # Propiedades configurables
 │   │   ├── domain/              # Lógica de dominio (modelos, repositorios)
 │   │   ├── grpc/server/         # Inicialización del servidor gRPC
 │   │   ├── service/             # Lógica del servicio gRPC y factories
 │   │   └── GrpcChallengeLafuenteApplication.kt
 │   ├── proto/                   # Definiciones .proto
 │   └── resources/               # application.yml y configuraciones
 └── test/
     └── kotlin/com/lafuente/test/grpcchallengelafuente/
         └── UserServiceImplTest.kt
```


## ▶️ Ejecución Local

```bash
./gradlew bootRun
```

El servidor gRPC se iniciará por defecto en el puerto `9090`.

## 🧪 Ejecutar Tests

```bash
./gradlew test
```


## 🐳 Docker

### Crear imagen
```bash
docker build -t grpc-challenge-lafuente .
```

### Ejecutar contenedor
```bash
docker run -p 9090:9090 grpc-challenge-lafuente
```

### O usando `docker-compose`
```bash
docker-compose up --build
```


## 📄 Protocolo gRPC

Archivo: [`user.proto`](src/main/proto/user.proto)

Contiene definiciones de:

- `GetUserRequest`
- `UserResponse`
- `UserActivity`
- Servicio: `UserService` con métodos `GetUser` y `StreamUserActivity`


## ✅ Pruebas Unitarias

Ubicación: `src/test/kotlin/.../UserServiceImplTest.kt`

Incluye pruebas para:

- Validación de solicitudes
- Generación de respuestas
- Manejo de errores simulados


## ⚙️ Configuración

Archivo: `application.yml`

Propiedades destacadas:

```yaml
grpc:
  server:
    port: 9090

user:
  activity:
    intervalMillis: 1000
```


---

Autoria: Ing. Manuel Saavedra
