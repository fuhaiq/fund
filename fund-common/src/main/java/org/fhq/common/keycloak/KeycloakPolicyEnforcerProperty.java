package org.fhq.common.keycloak;

import lombok.Data;
import org.keycloak.representations.adapters.config.PolicyEnforcerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "keycloak.enforcer")
@Data
public class KeycloakPolicyEnforcerProperty {
    private PolicyEnforcerConfig policyEnforcerConfig;
}
