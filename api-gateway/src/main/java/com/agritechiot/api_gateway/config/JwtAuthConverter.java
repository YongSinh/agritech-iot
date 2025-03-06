package com.agritechiot.api_gateway.config;

import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class JwtAuthConverter implements Converter<Jwt, Mono<? extends AbstractAuthenticationToken>> {
    private static final Logger LOG = LoggerFactory.getLogger(JwtAuthConverter.class);
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    private final JwtAuthConverterProperties properties;

    @Override
    public Mono<? extends AbstractAuthenticationToken> convert(@NotNull Jwt jwt) {
        LOG.debug("Converting JWT to AuthenticationToken: {}", jwt);
        return Mono.justOrEmpty(buildAuthenticationToken(jwt))
                .onErrorResume(e -> {
                    LOG.error("Error converting JWT to AuthenticationToken", e);
                    return Mono.empty();
                });
    }

    private AbstractAuthenticationToken buildAuthenticationToken(Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                        Optional.of(jwtGrantedAuthoritiesConverter.convert(jwt)).stream().flatMap(Collection::stream),
                        extractResourceRoles(jwt).stream())
                .collect(Collectors.toSet());
        return new JwtAuthenticationToken(jwt, authorities, getPrincipalClaimName(jwt));
    }


    private String getPrincipalClaimName(Jwt jwt) {
        String claimName = Optional.ofNullable(properties.getPrincipalAttribute())
                .orElse(JwtClaimNames.SUB);
        String principal = jwt.getClaim(claimName);
        LOG.debug("Extracted principal claim: {}", principal);
        return principal;
    }

    @SuppressWarnings("unchecked")
    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        return Optional.ofNullable(jwt.getClaimAsMap("realm_access"))
                .map(realmAccess -> (List<String>) realmAccess.get("roles"))
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(Objects::nonNull)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                .collect(Collectors.toSet());
    }
}
