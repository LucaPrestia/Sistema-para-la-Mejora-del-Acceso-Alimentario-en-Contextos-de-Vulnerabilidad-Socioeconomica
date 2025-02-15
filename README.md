## Sistema Acceso Alimentario

### Stack tecnológico

- Lenguaje de Programación: Java 17
- Framework: [SpringBoot](https://spring.io/projects/spring-boot)
- ORM: Hibernate con [SpringDataJPA](https://spring.io/projects/spring-data-jpa)
- Base de Datos: [SQL Server](https://www.microsoft.com/es-ar/sql-server/sql-server-downloads)
- Template Engine: [Thymeleaf](https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html)
- Soporte Web:
    - Modelado de interfaces con [Spring MVC](https://spring.io/guides/gs/serving-web-content) integrado a handlebars
    - [Restful Web Service](https://spring.io/guides/gs/rest-service) Spring

### Pruebas

- Tests: implementación algoritmo contraseña
- Servidor web http://localhost:8080/map: implementación mapa con API a OpenStreetMap
- Formularios dinámicos http://localhost:8080/formulario/abm/0 (ONG configuración de formularios, 0: persona humana, 1: persona jurídica): ESTO YA NO SE USA
- Alta Colaboradores: http://localhost:8080/colaborador/alta/0 (0: p.humana; 1: p.juridica)

### Comentado provisoriamente
- se comentaron algunas funciones y anotaciones del broker Rabbit para que no lance errores si no tenemos instalado el programa (lineas comentadas en archivos service/(Temperatura, Movimiento, Heladera))
- se comentaron los repositorios de la base de datos porque hasta la entrega 5 no tenemos que agregar persistencia
- se accede siempre primero al login, por ahora los unicos usuarios agregados a la base por default son los sgtes: 
  - user: colaboradorP pass: colaboradorP123
  - user: colaboradorJ pass: colaboradorJ123
  - user: admin pass: admin123
### Config JPA y DB
- creamos con click derecho en database una nueva base de nombre base_dds
- después desplegar las carpetas del explorador de objetos en el panel izquierdo, ir a seguridad-usuarios, hacer click derecho y crear usuario.
  Se debe seleccionar el SQL SERVER AUTHENTICATION, y nombre y pass con admin_dds. Darle ok
- Después dentro de la base de datos, desplegar carpetas, ir a seguridad-usuarios y click derecho nuevo usuario.
nombre: admin_dds, nombre usuario seleccionar el anteriormente creado, y en defaul
- Credenciales:
  - nombre: admin_dds 
  - pass: admin_dds
- si lanza error en el usuario admin_dds, ir al object explorer click derecho propiedades
  ir a security
  si lanza error por TCP/IP: ir al SQL SERVER CONFIGURATION MANAGER
  ir al servidor de la base que se levantó en SQL SERVER NETWORK CONFIGURATION
  hacer click derecho en TCP e ir a properties
  ir a la solapa de IP ADDRESSES
  ir al final de todo a la sección de IPAII y, si no hay un puerto seleccionado, poner el puerto 1433 en TCP PORT
  reiniciar el servicio desde Servicios 
- si lanza error en el usuario admin_dds, ir al object explorer click derecho propiedades
  ir a security
  tildar en server authentication la opción SQL SERVER AND WINDOWS AUTHENTICATION MODE
  reiniciar el servicios desde Servicios
- DESDE SECURITY LOGIN USER ADMIN_DDS: ir a Server Roles y MARCAR TODO, e ir a Securables y marcar TODOS los grants en explicits
- hice unos cambios en clases de tarjeta, para que todo salga bien, hacer un create-drop, bajar el sistema y si no se eliminaron las tablas, hacer un drop table de tarjeta_movimientos y de movimiento_tarjeta, levantar con create primero, bajar el sistema, y después levantar con update