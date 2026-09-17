package com.easymarkersapp.easymarkersapp.config;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class ShareTokenAuthentication extends AbstractAuthenticationToken {
    private final Long projectId;
    private final String token;

    public ShareTokenAuthentication(Long projectId, String token) {
        super(List.<GrantedAuthority>of(new SimpleGrantedAuthority("ROLE_READONLY")));
        this.projectId = projectId;
        this.token = token;
        setAuthenticated(true);
    }

    public Long getProjectId() { return projectId; }
    public String getToken()   { return token; }

    @Override
    public Object getCredentials() { return token; }
    @Override
    public Object getPrincipal()   { return null; }
}
