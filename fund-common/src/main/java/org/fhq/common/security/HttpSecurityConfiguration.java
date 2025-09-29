package org.fhq.common.security;

import lombok.RequiredArgsConstructor;
import org.fhq.common.FundComponents;
import org.fhq.common.keycloak.KeycloakPolicyEnforcerFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@ComponentScan(basePackageClasses = FundComponents.class)
@EnableWebSecurity
@RequiredArgsConstructor
public class HttpSecurityConfiguration {

    private final KeycloakPolicyEnforcerFilter keycloakPolicyEnforcerFilter;

    private final ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.oauth2ResourceServer(oauth2 -> oauth2
                .authenticationEntryPoint(resourceAuthExceptionEntryPoint)
                .jwt(Customizer.withDefaults()));
        http.addFilterAfter(keycloakPolicyEnforcerFilter, BearerTokenAuthenticationFilter.class);
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}