package com.easymarkersapp.easymarkersapp.filter;

import com.easymarkersapp.easymarkersapp.config.ShareTokenAuthentication;
import com.easymarkersapp.easymarkersapp.config.UserAuthentication;
import com.easymarkersapp.easymarkersapp.model.User;
import com.easymarkersapp.easymarkersapp.service.ProjectShareService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ShareTokenFilter extends OncePerRequestFilter {
    @Autowired
    private ProjectShareService shareService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String share = request.getParameter("share");
        Long shareProjectId = shareService.resolveProjectId(share).orElse(null);

        if (shareProjectId != null) {
            Authentication current = SecurityContextHolder.getContext().getAuthentication();

            if (current instanceof UserAuthentication userAuth) {
                // Уже залогинен — дополняем share-контекстом
                if (userAuth.getShareProjectId() == null) {
                    SecurityContextHolder.getContext().setAuthentication(
                            new UserAuthentication((User) userAuth.getPrincipal(), shareProjectId));
                }
            } else if (current == null || current instanceof AnonymousAuthenticationToken) {
                // Аноним — выдаём share-only аутентификацию
                SecurityContextHolder.getContext().setAuthentication(
                        new ShareTokenAuthentication(shareProjectId, share));
            }
            // Если current — что-то другое, не трогаем.
        }
        filterChain.doFilter(request, response);
    }
}
