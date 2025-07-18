package com.reitansora.authvalidation.model;

import com.reitansora.authvalidation.entity.PlanEntity;
import com.reitansora.authvalidation.entity.ProfileEntity;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@Builder
public class UserResponse {

    private final String userId;
    private final String email;
    private final String planName;
    private final Long maxBitrate;
    private final Timestamp createdAt;
}
