package com.easymarkersapp.easymarkersapp.service;

import com.easymarkersapp.easymarkersapp.model.ProjectShareLink;
import com.easymarkersapp.easymarkersapp.repository.ProjectShareLinkRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Service
public class ProjectShareService {
    private static final SecureRandom RANDOM = new SecureRandom();
    @Autowired
    private ProjectShareLinkRepository shareLinkRepository;

    @Transactional
    public ProjectShareLink createOrGet(Long projectId) {
        return shareLinkRepository.findByProjectId(projectId)
                .orElseGet(() -> {
                    ProjectShareLink link = new ProjectShareLink();
                    link.setProjectId(projectId);
                    link.setToken(generateToken());
                    return shareLinkRepository.save(link);
                });
    }

    public Optional<ProjectShareLink> findByProjectId(Long projectId) {
        return shareLinkRepository.findByProjectId(projectId);
    }

    @Transactional
    public void revoke(Long projectId) {
        shareLinkRepository.findByProjectId(projectId).ifPresent(l -> shareLinkRepository.delete(l));
    }

    /**
     * Возвращает projectId, если токен валиден и активен.
     */
    @Transactional
    public Optional<Long> resolveProjectId(String token) {
        if (token == null || token.isBlank()) return Optional.empty();
        return shareLinkRepository.findByToken(token)
                .map(ProjectShareLink::getProjectId);
    }

    private String generateToken() {
        byte[] buf = new byte[24]; // 192 бита
        RANDOM.nextBytes(buf);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buf);
    }
}
