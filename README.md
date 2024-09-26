## Sistema Acceso Alimentario

### Stack tecnológico

- Lenguaje de Programación: Java 17
- Framework: [SpringBoot](https://spring.io/projects/spring-boot)
- ORM: Hibernate con [SpringDataJPA](https://spring.io/projects/spring-data-jpa)
- Base de Datos: [MySQL](https://www.mysql.com/)/[MaríaDB](https://mariadb.org/)
- Template Engine: [Handlebars](https://handlebarsjs.com/)
- Soporte Web:
    - Modelado de interfaces con [Spring MVC](https://spring.io/guides/gs/serving-web-content) integrado a handlebars
    - [Restful Web Service](https://spring.io/guides/gs/rest-service) Spring

### Pruebas

- Tests: implementación algoritmo contraseña
- Servidor web http://localhost:8080/map: implementación mapa con API a OpenStreetMap
- Formularios dinámicos http://localhost:8080/formulario/abm/0 (ONG configuración de formularios, 0: persona humana, 1: persona jurídica): ESTO YA NO SE USA
- Alta Colaboradores: http://localhost:8080/colaborador/alta/0 (0: p.humana; 1: p.juridica)