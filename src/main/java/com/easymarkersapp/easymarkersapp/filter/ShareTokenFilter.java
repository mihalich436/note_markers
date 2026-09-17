package com.easymarkersapp.easymarkersapp.filter;

import com.easymarkersapp.easymarkersapp.config.ShareTokenAuthentication;
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
        Authentication current = SecurityContextHolder.getContext().getAuthentication();
        boolean anonymous = (current == null) || (current instanceof AnonymousAuthenticationToken);

        if (anonymous) {
            String share = request.getParameter("share");
            shareService.resolveProjectId(share).ifPresent(pid ->
                    SecurityContextHolder.getContext().setAuthentication(
                            new ShareTokenAuthentication(pid, share)));
        }
        filterChain.doFilter(request, response);
    }
}
