package com.easymarkersapp.easymarkersapp.service;

import com.easymarkersapp.easymarkersapp.dto.message.MessageUpdateRequest;
import com.easymarkersapp.easymarkersapp.dto.message.MarkerMessageUpdateResult;
import com.easymarkersapp.easymarkersapp.model.*;
import com.easymarkersapp.easymarkersapp.repository.MarkerRepository;
import com.easymarkersapp.easymarkersapp.repository.MarkerMessageRepository;
import jakarta.transaction.Transactional;
import org.antlr.v4.runtime.misc.Pair;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MarkerMessageService {
    @Autowired
    private MarkerMessageRepository messageRepository;
    @Autowired
    private MarkerRepository markerRepository;
    @Autowired
    private MapService mapService;

    public MarkerMessage save(MarkerMessage message) {
        String cleanText = Jsoup.clean(message.getText(), Safelist.relaxed());
        message.setText(cleanText);
        return messageRepository.save(message);
    }

    /**
     * Общая проверка: сообщение существует, принадлежит маркеру из URL,
     * маркер принадлежит карте из URL, у пользователя есть доступ к этой карте.
     */
    private Pair<MarkerMessage, ProjectAccess> loadAndCheckAccess(Long messageId, Long markerId, Long mapId, User user) {
        Optional<MarkerMessage> messageOptional = messageRepository.findById(messageId);
        if (messageOptional.isEmpty()) return null;
        MarkerMessage message = messageOptional.get();

        if (!message.getMarkerId().equals(markerId)) return null;

        Optional<Marker> markerOptional = markerRepository.findById(markerId);
        if (markerOptional.isEmpty()) return null;
        Marker marker = markerOptional.get();

        if (!marker.getMapId().equals(mapId)) return null;

        ProjectAccess access = mapService.getRoleByIdAndUser(mapId, user);
        if (access == null) return null;

        return new Pair<>(message, access);
    }

    @Transactional
    public MarkerMessageUpdateResult updateTextAndCheckAccess(Long messageId, Long markerId, Long mapId,
                                                              MessageUpdateRequest request, User user) {
        Pair<MarkerMessage, ProjectAccess> result = loadAndCheckAccess(messageId, markerId, mapId, user);
        if (result == null) return null;

        MarkerMessage message = result.a;
        ProjectAccess access = result.b;
        if (access.getRole().hasAccess(AccessRole.EDITOR)
                || message.getUserId().equals(user.getId())) {
            request.updateMessage(message);
            this.save(message);
            return new MarkerMessageUpdateResult(mapId, message);
        }
        return null;
    }

    @Transactional
    public MarkerMessageUpdateResult updateVisibilityAndCheckAccess(Long messageId, Long markerId, Long mapId,
                                                                    MessageUpdateRequest request, User user) {
        Pair<MarkerMessage, ProjectAccess> result = loadAndCheckAccess(messageId, markerId, mapId, user);
        if (result == null) return null;

        MarkerMessage message = result.a;
        ProjectAccess access = result.b;

        if (access.getRole().hasAccess(AccessRole.ADMIN)
                || message.getUserId().equals(user.getId())) {
            request.updateMessage(message);
            this.save(message);
            return new MarkerMessageUpdateResult(mapId, message);
        }
        return null;
    }

    @Transactional
    public boolean deleteByIdAndCheckAccess(Long messageId, Long markerId, Long mapId, User user) {
        Pair<MarkerMessage, ProjectAccess> result = loadAndCheckAccess(messageId, markerId, mapId, user);
        if (result == null) return false;

        MarkerMessage message = result.a;
        ProjectAccess access = result.b;

        if (access.getRole().hasAccess(AccessRole.EDITOR)
                || message.getUserId().equals(user.getId())) {
            messageRepository.delete(message);

            return true;
        }
        return false;
    }
}