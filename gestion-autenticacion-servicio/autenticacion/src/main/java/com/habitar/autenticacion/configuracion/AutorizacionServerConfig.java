package com.habitar.autenticacion.configuracion;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;


import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import com.habitar.autenticacion.servicio.AutenticacionServicio;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;



/* SACADO DE LA DOCUMENTACION DE SPRING
 * 
This is a minimal configuration for getting started quickly. To understand what each component is used for, see the following descriptions:

1 A Spring Security filter chain for the Protocol Endpoints.
2 A Spring Security filter chain for authentication.
3 An instance of UserDetailsService for retrieving users to authenticate.
4 An instance of RegisteredClientRepository for managing clients.
5 An instance of com.nimbusds.jose.jwk.source.JWKSource for signing access tokens.
6 An instance of java.security.KeyPair with keys generated on startup used to create the JWKSource above.
6 An instance of JwtDecoder for decoding signed access tokens.
7 An instance of AuthorizationServerSettings to configure Spring Authorization Server.*/


@Configuration
public class AutorizacionServerConfig {

		

	@Bean 
	@Order(1)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
			throws Exception {
		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
				OAuth2AuthorizationServerConfigurer.authorizationServer();

		http
			.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
			.with(authorizationServerConfigurer, (authorizationServer) ->
				authorizationServer
					.oidc(Customizer.withDefaults())	// Enable OpenID Connect 1.0
			)
			.authorizeHttpRequests((authorize) ->
				authorize
					.anyRequest().authenticated()
			)
			// Redirect to the login page when not authenticated from the
			// authorization endpoint
			.exceptionHandling((exceptions) -> exceptions
				.defaultAuthenticationEntryPointFor(
					new LoginUrlAuthenticationEntryPoint("/login"),
					new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
				)
			);

		return http.build();
	}

	@Bean 
	@Order(2)
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
			throws Exception {
		http
			.authorizeHttpRequests((authorize) -> authorize
				.anyRequest().authenticated()
			)
			// Form login handles the redirect to the login page from the
			// authorization server filter chain
			.formLogin(Customizer.withDefaults());

		return http.build();
	}
/*  ///BEAN QUE SETEA EL LOGIN DESDE UN USUARIO HARDCODEADO YA NO SRIVE SOLO FUE TEST
	@Bean 
	public UserDetailsService userDetailsService() {
		UserDetails userDetails = User.builder() //el de la documentacion ta deprecado xd
				//esta es la credenciales de los usuarios (esto obtener desde postgres)
				.username("zargo")
				.password("{noop}12345") //noop es q no ta encriptado
				.roles("USER")
				.build();

		return new InMemoryUserDetailsManager(userDetails);
	}
*/
	@Bean
    public AuthenticationManager authenticationManager(AutenticacionServicio autenticacionServicio) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(autenticacionServicio);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authenticationProvider);
    }
	
	
	@Bean 
	public RegisteredClientRepository registeredClientRepository() {
		RegisteredClient oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
				.clientId("cliente-apigateway") //esta es la autenticacion de la app cliente
				.clientSecret("{noop}12345")
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
				.redirectUri("http://127.0.0.1:8080/login/oauth2/code/cliente-apigateway")
				.redirectUri("http://127.0.0.1:8080/authorized")
				.postLogoutRedirectUri("http://127.0.0.1:8080/")
				.scope(OidcScopes.OPENID) //rol que permite iniciar sesion y generar token
				.scope(OidcScopes.PROFILE)
				.scope("read")
				.scope("write")
				.clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
				.build();

		return new InMemoryRegisteredClientRepository(oidcClient);
	}

	@Bean 
	public JWKSource<SecurityContext> jwkSource() {
		KeyPair keyPair = generateRsaKey(); //genera la llave rsa
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAKey rsaKey = new RSAKey.Builder(publicKey)
				.privateKey(privateKey)
				.keyID(UUID.randomUUID().toString())
				.build();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return new ImmutableJWKSet<>(jwkSet);
	}

	private static KeyPair generateRsaKey() { 
		KeyPair keyPair;
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();
		}
		catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
		return keyPair;
	}

	@Bean 
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	@Bean 
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}
	
	  @Bean 
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder(); // ¡PARA PRUEBAS TEMPORALES SOLAMENTE!
	    }
	  
	  
	
	/*
   //↓ este metodo (bean) genera la cadena de filtros de seugrodad
	@Bean // es un bean - en la clases @configuacion se generan @beans
	@Order(Ordered.HIGHEST_PRECEDENCE) // @Order eso indica 
	//que tiene que ejecutarse antes (HIGHEST_PRECEDENCE) de otros metodos del mismo tipo
	//1
	public SecurityFilterChain servicioCadenaDeSeguridad (HttpSecurity http) throws Exception{
		http.getConfigurer(OAuth2AuthorizationServerConfigurer.class) 
		//↑ aqui se asigna el configurador del servidor de autenticación, es OAuth2 
            .oidc(org.springframework.security.config.Customizer.withDefaults());
		// ↑ el metodo oidc configura OpenID Connect 1.0
		
		//↓ aqui va configurar como se gestionan las excepciones HttpSecurity 
		//SE VAN A APLICAR 
		//CONFIGURACIONES PARA LA GESTION DE EXCEPCIONES 
		//authenticationEntryPoint y  oauth2ResourceServer
		//Estos metodos de HttpSecurity implementan las I.F. 
		//Customizer<ExceptionHandlingConfigurer> y 
		//Customizer<OAuth2ResourceServerConfigurer> 
		//respecticamente
			http.exceptionHandling((excepciones) -> { 
				excepciones.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));
				//↑  si un usuario no está autenticado se redirige al endpoint /login
			});
			//↓ AQUI SE DEFINEN LAS CONFIGURACIONES PARA 
			//la gestión de el token JWT segun OAuth2
			// implementa Customizer<OAuth2ResourceServerConfigurer>
			http.oauth2ResourceServer((resourceServer) -> {
				resourceServer.jwt(org.springframework.security.config.Customizer.withDefaults());
			});

    return http.build(); //Este metodo un genera SecurityFilterChain
    
   
   //  una  SecurityFilterChain es una
    //  interfaz que representa una cadena ordenada
     //  de filtros de seguridad
     
     
	}
	
	
	@Bean //otro bean generado
	public RegisteredClientRepository clienteRegistradoRepositorio() {
	    RegisteredClient clienteHabitar = RegisteredClient //todos los que siguen abajo son
	    													//modificadores tipo pipeline
	    													//que configuran el registro
	    		
	    		.withId(UUID.randomUUID().toString()) //aqui genera el id interno para el servidor de autenticacion
	            .clientId("habitar-apigateway") // aqui se le pasa el id de la aplicacion que se comunica 
	            // con el servidor de autorizacion osea mi apigateway
	            .clientSecret("123456789") //este es el secret de mi api gateway ...podría ver si habilito un keyvault o algo asi pero luego
	            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
	            //↑ esto dice que el metodo es que el cluente mete en la cabecera de autorizacion
	            //el id y el secret (esquema Basico)
	            .authorizationGrantTypes(grantTypes -> {
	                grantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE); //ermite el flujo de cdoigo de autorización
	                grantTypes.add(AuthorizationGrantType.REFRESH_TOKEN); //para obtener nuevos tokens de acceso 
	                grantTypes.add(AuthorizationGrantType.CLIENT_CREDENTIALS); // Para autenticación de desde mi apigateway
	            }) //en este se configura los flujos (grants) OAuth 2.
	            .redirectUris(redirecccionUris -> redirecccionUris.add("http://127.0.0.1:8080/login/oauth2/code/oidc"))
	            //↑ aqui se definen redirecciones luego del logeo
	            .scopes(scopes -> { //el alcance de los permisos el cliente 
	                scopes.add("openid");
	                scopes.add("profile");
	                scopes.add("colaborador.read");
	                scopes.add("proyecto.read");
	                scopes.add("documento.read");
	            }).clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
	            .tokenSettings(TokenSettings.builder().
	            accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED).build())
	            .build();

	    return new InMemoryRegisteredClientRepository(clienteHabitar);
	}
	
	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		KeyPair keyPair = generateRsaKey();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAKey rsaKey = new RSAKey.Builder(publicKey)
				.privateKey(privateKey)
				.keyID(UUID.randomUUID().toString())
				.build();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return new ImmutableJWKSet<SecurityContext>(jwkSet);
	}

	private static KeyPair generateRsaKey() { 
		KeyPair keyPair;
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();
		}
		catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
		return keyPair;
	}

	@Bean 
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	@Bean 
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}
*/
	
}
