package com.reitansora.authvalidation.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.reitansora.authvalidation.entity.PlanEntity;
import com.reitansora.authvalidation.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtToPrincipalConverter {

    private final PlanRepository planRepository;

    public UserPrincipal convert(DecodedJWT decodedJWT) {
        String planName = decodedJWT.getClaim("planName").asString();
        Optional<PlanEntity> plan = planRepository.findByName(planName);
        return UserPrincipal.builder()
                .userId(String.valueOf(decodedJWT.getSubject()))
                .email(decodedJWT.getClaim("email").asString())
                .plan(plan.orElse(null))
                .createdAt(getCreatedAtTimestamp(decodedJWT))
                .build();
    }

    private Timestamp getCreatedAtTimestamp(DecodedJWT decodedJWT) {
        String createdAtStr = decodedJWT.getClaim("createdAt").asString();
        if (createdAtStr == null || createdAtStr.isEmpty()) {
            // If createdAt is not present, use current timestamp
            return new Timestamp(Instant.now().toEpochMilli());
        }
        return Timestamp.valueOf(createdAtStr);
    }

    private List<SimpleGrantedAuthority> getAuthorities(DecodedJWT decodedJWT) {
        var claim = decodedJWT.getClaim("authorities");
        if (claim.isNull() || claim.isMissing()) return List.of();
        return claim.asList(SimpleGrantedAuthority.class);
    }

}
