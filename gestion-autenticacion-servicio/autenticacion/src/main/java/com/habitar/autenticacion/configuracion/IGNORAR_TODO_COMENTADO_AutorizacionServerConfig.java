/*
 * package com.habitar.autenticacion.configuracion;
 * 
 * import java.security.KeyPair; import java.security.KeyPairGenerator;
 * 
 * import java.security.interfaces.RSAPrivateKey; import
 * java.security.interfaces.RSAPublicKey; import java.util.UUID;
 * 
 * 
 * import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
 * 
 * import org.springframework.security.crypto.password.PasswordEncoder; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration;
 * 
 * import org.springframework.core.annotation.Order; import
 * org.springframework.http.MediaType; import
 * org.springframework.security.authentication.AuthenticationManager; import
 * org.springframework.security.authentication.ProviderManager; import
 * org.springframework.security.authentication.dao.DaoAuthenticationProvider;
 * import org.springframework.security.config.Customizer; import
 * org.springframework.security.config.annotation.web.builders.HttpSecurity;
 * 
 * import org.springframework.security.oauth2.core.AuthorizationGrantType;
 * import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
 * import org.springframework.security.oauth2.core.oidc.OidcScopes; import
 * org.springframework.security.oauth2.jwt.JwtDecoder; import
 * org.springframework.security.oauth2.server.authorization.client.
 * InMemoryRegisteredClientRepository; import
 * org.springframework.security.oauth2.server.authorization.client.
 * RegisteredClient; import
 * org.springframework.security.oauth2.server.authorization.client.
 * RegisteredClientRepository; import
 * org.springframework.security.oauth2.server.authorization.config.annotation.
 * web.configuration.OAuth2AuthorizationServerConfiguration; import
 * org.springframework.security.oauth2.server.authorization.config.annotation.
 * web.configurers.OAuth2AuthorizationServerConfigurer; import
 * org.springframework.security.oauth2.server.authorization.settings.
 * AuthorizationServerSettings; import
 * org.springframework.security.oauth2.server.authorization.settings.
 * ClientSettings;
 * 
 * 
 * import org.springframework.security.web.SecurityFilterChain; import
 * org.springframework.security.web.authentication.
 * LoginUrlAuthenticationEntryPoint; import
 * org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
 * 
 * import com.habitar.autenticacion.servicio.AutenticacionServicio; import
 * com.nimbusds.jose.jwk.JWKSet; import com.nimbusds.jose.jwk.RSAKey; import
 * com.nimbusds.jose.jwk.source.ImmutableJWKSet; import
 * com.nimbusds.jose.jwk.source.JWKSource; import
 * com.nimbusds.jose.proc.SecurityContext;
 * 
 * 
 * 
 * SACADO DE LA DOCUMENTACION DE SPRING
 * 
 * This is a minimal configuration for getting started quickly. To understand
 * what each component is used for, see the following descriptions:
 * 
 * 1 A Spring Security filter chain for the Protocol Endpoints. 2 A Spring
 * Security filter chain for authentication. 3 An instance of UserDetailsService
 * for retrieving users to authenticate. 4 An instance of
 * RegisteredClientRepository for managing clients. 5 An instance of
 * com.nimbusds.jose.jwk.source.JWKSource for signing access tokens. 6 An
 * instance of java.security.KeyPair with keys generated on startup used to
 * create the JWKSource above. 6 An instance of JwtDecoder for decoding signed
 * access tokens. 7 An instance of AuthorizationServerSettings to configure
 * Spring Authorization Server.
 * 
 * 
 * @Configuration public class FAILED_AutorizacionServerConfig {
 * 
 * 
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
 * // No configuramos el formulario de login aquí, confiamos en
 * defaultSecurityFilterChain return http.build(); }
 * 
 * @Bean public RegisteredClientRepository registeredClientRepository() {
 * RegisteredClient oidcClient =
 * RegisteredClient.withId(UUID.randomUUID().toString())
 * .clientId("cliente-apigateway") //esta es la autenticacion de la app cliente
 * .clientSecret("{noop}12345")
 * .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
 * .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
 * .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
 * .redirectUri("https://www.google.com/") // Redireccionando a Google para
 * prueba //.redirectUri("http://127.0.0.1:8080/authorized")
 * .postLogoutRedirectUri("http://127.0.0.1:8080/") .scope(OidcScopes.OPENID)
 * //rol que permite iniciar sesion y generar token .scope(OidcScopes.PROFILE)
 * .scope("read") .scope("write")
 * .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).
 * build()) .build();
 * 
 * return new InMemoryRegisteredClientRepository(oidcClient); }
 * 
 * @Bean public JWKSource<SecurityContext> jwkSource() { KeyPair keyPair =
 * generateRsaKey(); //genera la llave rsa RSAPublicKey publicKey =
 * (RSAPublicKey) keyPair.getPublic(); RSAPrivateKey privateKey =
 * (RSAPrivateKey) keyPair.getPrivate(); RSAKey rsaKey = new
 * RSAKey.Builder(publicKey) .privateKey(privateKey)
 * .keyID(UUID.randomUUID().toString()) .build(); JWKSet jwkSet = new
 * JWKSet(rsaKey); return new ImmutableJWKSet<>(jwkSet); }
 * 
 * 
 * private static KeyPair generateRsaKey() { KeyPair keyPair; try {
 * KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
 * keyPairGenerator.initialize(2048); keyPair =
 * keyPairGenerator.generateKeyPair(); } catch (Exception ex) { throw new
 * IllegalStateException(ex); } return keyPair; }
 * 
 * @Bean public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
 * return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource); }
 * 
 * @Bean public AuthorizationServerSettings authorizationServerSettings() {
 * return AuthorizationServerSettings.builder().build(); }
 * 
 * 
 * 
 * }
 * 
 * 
 */


