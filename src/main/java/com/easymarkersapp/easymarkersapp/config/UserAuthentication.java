package com.easymarkersapp.easymarkersapp.config;

import com.easymarkersapp.easymarkersapp.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

public class UserAuthentication extends UsernamePasswordAuthenticationToken {

    /** projectId, к которому пользователь получил read-only доступ через share-ссылку. */
    private final Long shareProjectId;

    public UserAuthentication(User user, Long shareProjectId) {
        this(user, shareProjectId, new ArrayList<>());
    }

    public UserAuthentication(User user,
                              Long shareProjectId,
                              Collection<? extends GrantedAuthority> authorities) {
        super(user, null, authorities);
        this.shareProjectId = shareProjectId;
    }

    /** null, если пользователь не пришёл по share-ссылке. */
    public Long getShareProjectId() {
        return shareProjectId;
    }

    public boolean hasShareContext() {
        return shareProjectId != null;
    }
}
