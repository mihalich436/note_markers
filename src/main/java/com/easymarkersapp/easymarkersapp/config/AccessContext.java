package com.easymarkersapp.easymarkersapp.config;

import org.springframework.security.core.Authentication;

public class AccessContext {
    private AccessContext() {}

    /** Обычный залогиненный пользователь. */
    public static boolean isUser(Authentication a) {
        return a != null && a.isAuthenticated() && a.getPrincipal() instanceof com.easymarkersapp.easymarkersapp.model.User;
    }

    /** Аноним с share-токеном. */
    public static boolean isShare(Authentication a) {
        return a instanceof ShareTokenAuthentication;
    }

    /** projectId, если доступ через share. */
    public static Long shareProjectId(Authentication auth) {
        if (auth instanceof UserAuthentication ua) return ua.getShareProjectId();
        if (auth instanceof ShareTokenAuthentication sa) return sa.getProjectId();
        return null;
    }

}
