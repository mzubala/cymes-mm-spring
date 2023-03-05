package pl.com.bottega.cymes.users;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import pl.com.bottega.cymes.sharedkernel.ClockProvider;

import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;

@Component
class JwtTokenService {

    private final ClockProvider clockProvider;
    private final String secret;
    private final JwtConfig jwtConfig;

    JwtTokenService(ClockProvider clockProvider, JwtConfig jwtConfig) {
        this.clockProvider = clockProvider;
        this.secret = Base64Utils.encodeToString(jwtConfig.secret().getBytes());
        this.jwtConfig = jwtConfig;
    }

    String generateTokenFor(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return Jwts.builder()
            .setSubject(user.getUsername())
            .setId(user.getId().toString())
            .claim("roles", user.getRoles())
            .setIssuedAt(Date.from(clockProvider.now()))
            .setExpiration(Date.from(clockProvider.now().plus(jwtConfig.tokenExpiresAfter())))
            .signWith(HS512, secret)
            .compact();
    }

    Authentication extractAuthentication(String token) {
        var parsedToken = Jwts.parser().setSigningKey(secret).parse(token);
        var claims = (DefaultClaims) parsedToken.getBody();
        Stream<String> roles = claims.get("roles", List.class).stream();
        return new JwtAuthentication(
            Long.valueOf(claims.getId()),
            claims.getSubject(),
            roles.map(r -> new SimpleGrantedAuthority("ROLE_" + r)).collect(Collectors.toList())
        );
    }
}

@RequiredArgsConstructor
class JwtAuthentication implements Authentication {

    private final Long userId;
    private final String userEmail;
    private final Collection<GrantedAuthority> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException("It's already authenticated");
    }

    @Override
    public String getName() {
        return userEmail;
    }
}

@ConfigurationProperties("jwt")
record JwtConfig(String secret, Duration tokenExpiresAfter) {

}