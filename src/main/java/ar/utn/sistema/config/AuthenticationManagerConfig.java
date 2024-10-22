package ar.utn.sistema.config;

import ar.utn.sistema.services.UsuarioSesionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthenticationManagerConfig {

    private final UsuarioSesionService usuarioSesionService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationManagerConfig(UsuarioSesionService usuarioSesionService, PasswordEncoder passwordEncoder) {
        this.usuarioSesionService = usuarioSesionService;
        this.passwordEncoder = passwordEncoder;
    }

    // con este bean la verificación de la contraseña se realiza automáticamente:
    /* Flujo de autenticación:
        -> Solicitud de inicio de sesión: Cuando el usuario envía un formulario de inicio de sesión, Spring Security recibe el nombre de usuario y la contraseña.
        -> Carga de usuario: Spring Security llama al método loadUserByUsername, donde obtiene el usuario y la contraseña codificada de la base de datos.
        -> Verificación: Spring Security compara la contraseña ingresada (sin codificar) con la contraseña almacenada (codificada) utilizando el PasswordEncoder ya configurado. Si coinciden, el usuario se autentica correctamente.
    */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(usuarioSesionService)
                .passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }
}
