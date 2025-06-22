# Spring Boot Microservices Example

Este repositorio contiene una arquitectura básica de microservicios usando Spring Boot, Spring Cloud, Eureka, Config Server, Gateway y Feign Client. Incluye los siguientes módulos:

- ✅ **Config Server** (`microservice-config`)
- ✅ **Eureka Server** (`microservice-eureka`)
- ✅ **API Gateway** (`microservice-gateway`)
- ✅ **Servicio de Cursos** (`msvc-course`)
- ✅ **Servicio de Estudiantes** (`msvc-student`)

---

## 🛠️ Tecnologías

- Java 21  
- Spring Boot  
- Spring Cloud `2025.0.0`  
- Eureka Discovery Server & Client  
- Spring Cloud Config Server  
- Spring Cloud Gateway  
- Feign Client  
- MySQL  

---

## 📦 Estructura de Microservicios

### 1. Config Server (`microservice-config`)
**Clase principal:** `MicroserviceConfigApplication`

**Dependencia clave:**
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
```

Anotación requerida:
```java
@EnableConfigServer
```
### 2. Eureka Server (microservice-eureka)
Clase principal: MicroserviceEurekaApplication

Dependencia clave:
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```
Anotación requerida:
```java
@EnableEurekaServer
```
### 3. API Gateway (microservice-gateway)
Clase principal: MicroserviceGatewayApplication

Dependencia correcta:
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```
🚫 No usar:
```xml
<!-- spring-cloud-starter-gateway-server-webmvc -->
```
application.yml correcto:

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: courses
          uri: lb://msvc-course
          predicates:
            - Path=/api/course/**
        - id: students
          uri: lb://msvc-student
          predicates:
            - Path=/api/student/**
      discovery:
        locator:
          enabled: true
```
### 4. Servicio de Cursos (msvc-course)
Usa Feign Client para comunicarse con msvc-student:

@FeignClient(name = "msvc-student")
public interface IStudentClient {
    @GetMapping("/api/student/search-by-course/{idCourse}")
    List<StudentDTO> findAllStudentsByCourse(@PathVariable Long idCourse);
}
⚠️ Importante: El @FeignClient usa name para que Spring lo resuelva por Eureka. No usar url si estás trabajando con descubrimiento de servicios.

### 5. Servicio de Estudiantes (msvc-student)
Exponen los endpoints bajo /api/student/** como se define en el Gateway.

⚠️ Consideraciones Importantes
❌ No mezclar WebMVC con WebFlux en el Gateway.

✅ Usar spring-cloud-starter-gateway en lugar de spring-cloud-starter-gateway-server-webmvc.

✅ Todas las apps deben tener bien configurado su application.yml:

yaml
spring:
  application:
    name: msvc-nombre
  config:
    import: optional:configserver:http://localhost:8888

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
🚀 Lanzamiento de Servicios
Orden recomendado:

🟢 Config Server (microservice-config)

🟢 Eureka Server (microservice-eureka)

🟢 Gateway (microservice-gateway)

🟢 Servicios (msvc-student, msvc-course, etc)

🧩 Troubleshooting
Problema	Solución
❌ 404 en FeignClient	Verificar que el @GetMapping coincida con el path completo del endpoint.
❌ Connection refused en Eureka	Asegurarse que el Config Server esté levantado si se usa config.import.
❌ 404 Not Found en Gateway	Confirmar que las rutas del Gateway coincidan con los paths reales de los servicios.

