# 📜 Presentación del Proyecto: Gestor de Gastos Grupales

## 💡 Concepto General

Esta es una **aplicación web para el seguimiento y la división de gastos en grupo**, diseñada como una herramienta intuitiva para simplificar la gestión financiera en actividades compartidas como viajes, cenas, o la convivencia en un piso.

La aplicación permite a los usuarios registrarse, crear grupos, añadir los gastos que han pagado y ver automáticamente cómo se calculan las deudas entre los miembros. El objetivo es eliminar la complejidad de "¿quién debe dinero a quién?", proporcionando un resumen claro y preciso de las finanzas del grupo.

---

## ⚙️ Características Técnicas y Dependencias

| Componente | Tecnologías/Archivos Principales | Descripción |
| :--- | :--- | :--- |
| **Backend** | `Java 17`, `Spring Boot`, `Maven` | Construido sobre el ecosistema de Spring, proporciona una base robusta para la gestión de usuarios, grupos, gastos y deudas. |
| **Frontend** | `HTML`, `CSS`, `JavaScript`, `Thymeleaf` | La interfaz de usuario es una aplicación web clásica renderizada en el servidor, utilizando Thymeleaf para integrar dinámicamente los datos del backend en las vistas. |
| **Base de Datos** | `PostgreSQL` | Utiliza una base de datos PostgreSQL. La aplicación está configurada para operar en un **esquema dedicado** (`Newtricount`), asegurando el aislamiento completo de los datos. |
| **Persistencia**| `Spring Data JPA`, `Hibernate` | Gestiona el acceso a los datos y el mapeo objeto-relacional, simplificando todas las operaciones con la base de datos. |
| **Internacionalización** | `messages_es.properties` | La aplicación está preparada para soportar múltiples idiomas, con el español configurado por defecto. |

---

## 🖥️ Estructura de la Aplicación

La aplicación se organiza en varias vistas clave que permiten un flujo de usuario lógico e intuitivo.

### 1. Página de Autenticación

*   **Propósito:** Dar la bienvenida a los usuarios y gestionar el acceso a la plataforma.
*   **Contenido:** Proporciona formularios para el **registro de nuevos usuarios** y el **inicio de sesión** de usuarios existentes. El acceso a las funcionalidades principales está protegido.

### 2. Vista Principal del Usuario (Dashboard)

*   **Propósito:** Es el panel de control del usuario una vez que ha iniciado sesión.
*   **Visualización:** Muestra una lista de todos los grupos a los que pertenece el usuario. Desde aquí, puede acceder a los detalles de cada grupo o crear uno nuevo.

### 3. Vista de Grupo

*   **Propósito:** Permite visualizar y gestionar la actividad de un grupo específico.
*   **Organización:** Muestra la lista de miembros, todos los gastos registrados y, lo más importante, un resumen de las **deudas calculadas** (quién debe a quién y cuánto).

### 4. Formulario de Creación y Edición

*   **Propósito:** Permite la inserción de nuevos datos en la aplicación.
*   **Formularios:** Existen formularios dedicados para crear nuevos grupos, añadir miembros a un grupo y registrar nuevos gastos, especificando quién pagó y la cantidad.

---

## 🚀 Flujo de Trabajo del Usuario

El uso de la aplicación sigue un proceso sencillo y efectivo:

1.  **Registro/Inicio de Sesión:** Un nuevo usuario se registra, o un usuario existente inicia sesión.
2.  **Creación de un Grupo:** Desde el panel principal, el usuario crea un nuevo grupo para un evento o propósito específico (ej. "Viaje a la playa").
3.  **Añadir Miembros:** El usuario añade a otros miembros al grupo.
4.  **Registrar un Gasto:** Cuando un miembro paga algo (ej. "Cena en el restaurante"), lo añade como un gasto en el grupo, indicando el importe.
5.  **Visualización de Deudas:** La aplicación recalcula automáticamente las deudas. Todos los miembros pueden ver en tiempo real quién debe dinero a quién para saldar las cuentas.

---

# ⚙️ Instalación y Ejecución

## 🧩 Explicación

Para ejecutar este proyecto, necesitas un entorno de desarrollo con Java y Maven, así como una instancia de PostgreSQL. La aplicación se conectará a la base de datos y creará automáticamente el esquema (`Newtricount`) y las tablas necesarias en el primer arranque.

## 🚀 Instalación

Sigue estos pasos para poner en marcha el proyecto en tu máquina local:

1.  **Requisitos Previos**
    *   Java Development Kit (JDK) 17 o superior.
    *   Apache Maven.
    *   Una base de datos PostgreSQL en funcionamiento.

2.  **Clonar el Repositorio**

    ```bash
    git clone <URL_DEL_REPOSITORIO>
    cd gastos
    ```

3.  **Configurar la Conexión a la Base de Datos**

    La aplicación está diseñada para recibir las credenciales de la base de datos a través de variables de entorno. Para un desarrollo local, puedes añadirlas directamente al final del archivo `src/main/resources/application.properties`:

    ```properties
    # Ejemplo de configuración para base de datos local
    spring.datasource.url=jdbc:postgresql://localhost:5432/nombre_tu_db
    spring.datasource.username=tu_usuario
    spring.datasource.password=tu_contraseña
    ```

4.  **Compilar el Proyecto**

    Usa Maven para compilar el proyecto. Este comando descargará las dependencias y empaquetará la aplicación en un archivo JAR.

    ```bash
    mvn clean install
    ```

5.  **Ejecutar la Aplicación**

    Una vez compilado, puedes iniciar el servidor de Spring Boot:

    ```bash
    java -jar target/gastos-0.0.1-SNAPSHOT.jar
    ```

    La aplicación se iniciará y estará accesible en `http://localhost:8080`.

---

## 💾 Cómo se guardan los datos

Esta aplicación utiliza una base de datos PostgreSQL para la persistencia de datos, con una configuración que garantiza el aislamiento y la facilidad de despliegue.

- **Creación Automática del Esquema:** En el primer arranque, la aplicación ejecutará un script (`schema.sql`) para crear el esquema `Newtricount` si no existe.
- **Gestión de Tablas:** Inmediatamente después, `Hibernate` creará automáticamente todas las tablas necesarias (`users`, `groups`, `expenses`, etc.) dentro de ese esquema.
- **Aislamiento:** Todas las operaciones de la base de datos están contenidas dentro del esquema `Newtricount`, lo que evita conflictos con otras aplicaciones que puedan estar en la misma instancia de la base de datos.
