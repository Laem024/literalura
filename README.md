# 📚 Proyecto: Challenge LiterAlura para Gestión de Libros y Autores

Este proyecto es una Aplicacion de consola desarrollada en **Java** utilizando **Spring Boot**, **JPA** y **PostgreSQL**, que permite gestionar libros y sus respectivos autores.

---

## 🚀 Características
- Creación, actualización y consulta de libros.
- Gestión de autores asociados a los libros.
- Uso de **Spring Data JPA** para la persistencia en **PostgreSQL**.
- Mapeo de entidades con **JPA**.
- Manejo de relaciones **@ManyToOne** y **@OneToMany**.

---

## 🛠️ Tecnologías Usadas
- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **PostgreSQL**
- **jackson-databind**

---

## 📂 Estructura del Proyecto
```
📂 src/
 ├── main/
 │   ├── java/com/aluracursos/literalura/
 │   │   ├── model/       # Entidades JPA
 │   │   ├── principal    # Menu de ejecucion de la aplicación
 │   │   ├── repository/  # Repositorios JPA
 │   │   ├── service/     # Servicios de negocio
 │   │   ├── LiteraluraApplication.java  # Clase principal
 │   ├── resources/
 │   │   ├── application.properties  # Configuración de base de datos
```

---

## ⚙️ Configuración de la Base de Datos

Asegúrate de configurar tu archivo `application.properties` correctamente:

```properties
spring.datasource.url=jdbc:postgresql://${POSTGRES_HOST}/literalura
spring.datasource.username=${POSTGRES_USER}
spring.datasource.password=${POSTGRES_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.format-sql=true
```

🔹 **Nota**: Sustituye `${POSTGRES_HOST}`, `${POSTGRES_USER}` y `${POSTGRES_PASSWORD}` con tus credenciales de PostgreSQL.

---

## 🏗️ Instalación y Ejecución

1️⃣ **Clonar el repositorio:**
```bash
git clone https://github.com/Laem024/literalura.git
cd literalura
```

2️⃣ **Configurar la base de datos PostgreSQL:**
```sql
CREATE DATABASE literalura;
```

3️⃣ **Compilar y ejecutar el proyecto:**
```bash
mvn spring-boot:run
```

---


¡Gracias por usar este proyecto! 🎉

