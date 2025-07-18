package com.reitansora.authvalidation.controller;

import com.reitansora.authvalidation.model.UserResponse;
import com.reitansora.authvalidation.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/secured")
    public UserResponse secured(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        System.out.println(userPrincipal.getCreatedAt());
        return UserResponse.builder()
                .userId(userPrincipal.getUserId())
                .email(userPrincipal.getEmail())
                .planName(userPrincipal.getPlan().getName())
                .maxBitrate(userPrincipal.getPlan().getStreamingQuality().getMaxBitrate())
                .createdAt(userPrincipal.getCreatedAt())
                .build();
    }
}