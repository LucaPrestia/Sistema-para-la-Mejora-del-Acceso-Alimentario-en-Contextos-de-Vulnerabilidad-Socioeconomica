package ar.utn.sistema.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationSuccessHandlerConfig successHandler) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/","/login", "/register", "/mapa*","/img/**","/VulnerablesXLocalidad").permitAll() // sin autenticacion solo se puede entrar a estos dos templates, si se quisiera especificar por carpeta se podría hacer algo así: .requestMatchers("/public/**").permitAll()
                        .anyRequest().authenticated() // a cualquier otro template no*/
                )
                .csrf(csrf -> csrf.disable())
                .formLogin(login -> login
                        .loginPage("/login") // página de login personalizada
                        .loginProcessingUrl("/login") // URL de procesamiento del login
                        .successHandler(successHandler)
                        // .defaultSuccessUrl("/home", true)// redirección tras éxito
                        .failureUrl("/login?error=true")// redirección tras fallo
                        .permitAll() // Permite acceso a todos para el login
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // URL para logout
                        .logoutSuccessUrl("/login?logout=true") // redirección tras logout
                        .deleteCookies("JSESSIONID") // borra las cookies de sesión
                        .invalidateHttpSession(true)// invalida la sesión
                        .permitAll()// permite acceso a todos para logout
                );

        return http.build();
    }

}
