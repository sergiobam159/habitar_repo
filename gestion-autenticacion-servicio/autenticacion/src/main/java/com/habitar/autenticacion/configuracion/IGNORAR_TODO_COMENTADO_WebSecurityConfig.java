/*
 * package com.habitar.autenticacion.configuracion;
 * 
 * import org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.core.annotation.Order; import
 * org.springframework.http.MediaType; import
 * org.springframework.security.authentication.AuthenticationManager; import
 * org.springframework.security.authentication.ProviderManager; import
 * org.springframework.security.authentication.dao.DaoAuthenticationProvider;
 * import org.springframework.security.config.Customizer; import
 * org.springframework.security.config.annotation.authentication.configuration.
 * AuthenticationConfiguration; import
 * org.springframework.security.config.annotation.web.builders.HttpSecurity;
 * import org.springframework.security.config.annotation.web.configuration.
 * EnableWebSecurity; import
 * org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import
 * org.springframework.security.crypto.password.PasswordEncoder; import
 * org.springframework.security.oauth2.server.authorization.config.annotation.
 * web.configurers.OAuth2AuthorizationServerConfigurer; import
 * org.springframework.security.web.SecurityFilterChain; import
 * org.springframework.security.web.authentication.
 * LoginUrlAuthenticationEntryPoint; import
 * org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
 * 
 * import com.habitar.autenticacion.servicio.AutenticacionServicio;
 * 
 * @Configuration
 * 
 * @EnableWebSecurity public class FAILED_WebSecurityConfig {
 * 
 * private final AutenticacionServicio autenticacionServicio;
 * 
 * public FAILED_WebSecurityConfig(AutenticacionServicio autenticacionServicio)
 * { this.autenticacionServicio = autenticacionServicio; }
 * 
 * @Bean public AuthenticationManager
 * authenticationManager(AuthenticationConfiguration
 * authenticationConfiguration) throws Exception { return
 * authenticationConfiguration.getAuthenticationManager(); }
 * 
 * @Bean
 * 
 * @Order(2) public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity
 * http) throws Exception { http .securityMatcher(request ->
 * !request.getRequestURI().startsWith("/oauth2/")) // Aplica este filtro a
 * rutas NO oauth2 .authorizeHttpRequests((authorize) -> authorize
 * .anyRequest().authenticated() ) .formLogin(Customizer.withDefaults())
 * .userDetailsService(autenticacionServicio) .csrf(Customizer.withDefaults());
 * return http.build(); }
 * 
 * @Bean
 * 
 * @Order(1) public SecurityFilterChain
 * authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
 * OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new
 * OAuth2AuthorizationServerConfigurer();
 * 
 * http .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
 * .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated()
 * ) .csrf(csrf -> csrf.ignoringRequestMatchers(authorizationServerConfigurer.
 * getEndpointsMatcher())) .apply(authorizationServerConfigurer);
 * 
 * http.exceptionHandling((exceptions) -> exceptions
 * .defaultAuthenticationEntryPointFor( new
 * LoginUrlAuthenticationEntryPoint("/login"), new
 * MediaTypeRequestMatcher(MediaType.TEXT_HTML) ) ); return http.build(); }
 * 
 * @Bean public PasswordEncoder passwordEncoder() { return new
 * BCryptPasswordEncoder(); } }
 */


