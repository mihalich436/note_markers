package com.easymarkersapp.easymarkersapp.service;

import com.easymarkersapp.easymarkersapp.dto.marker.MarkerRequest;
import com.easymarkersapp.easymarkersapp.model.*;
import com.easymarkersapp.easymarkersapp.repository.MarkerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MarkerService {
    @Autowired
    private MarkerRepository markerRepository;
    @Autowired
    private MapService mapService;
    @Autowired
    private MarkerMessageService markerMessageService;

    public Marker save(Marker marker) {
        String number = marker.getNumber();
        if (number != null) {
            marker.setNumber(number.substring(0, Math.min(3, number.length())));
        }
        return markerRepository.save(marker);
    }

    /**
     * Находит маркер по markerId, проверяет:
     *  - что маркер существует,
     *  - что маркер действительно принадлежит mapId из URL,
     *  - что у пользователя есть доступ к этой карте с нужной ролью.
     */
    @Transactional
    public Marker findByIdAndCheckAccess(Long markerId, Long mapId, User user, AccessRole requiredRole) {
        Optional<Marker> markerOptional = markerRepository.findById(markerId);
        if (markerOptional.isEmpty()) return null;
        Marker marker = markerOptional.get();

        // маркер должен принадлежать именно той карте, что в URL
        if (!marker.getMapId().equals(mapId)) return null;

        ProjectAccess access = mapService.getRoleByIdAndUser(mapId, user);
        if (access != null && access.getRole().hasAccess(requiredRole)) {
            return marker;
        }
        return null;
    }

    @Transactional
    public Marker updateByIdAndCheckAccess(Long markerId, Long mapId, MarkerRequest request,
                                           User user, AccessRole requiredRole) {
        Marker marker = findByIdAndCheckAccess(markerId, mapId, user, requiredRole);
        if (marker == null) return null;

        boolean visibilityPrev = marker.getVisibility();
        request.updateMarker(marker);
        Marker updatedMarker = this.save(marker);
        if (!visibilityPrev && updatedMarker.getVisibility()) {
            updatedMarker.getMessages();
        }
        return updatedMarker;
    }

    @Transactional
    public Marker copyAndCheckAccess(MarkerRequest request, Long markerId, Long mapId,
                                     User user, AccessRole requiredRole) {
        Marker marker = findByIdAndCheckAccess(markerId, mapId, user, requiredRole);
        if (marker == null) return null;

        Marker copiedMarker = marker.copy();
        request.updateMarker(copiedMarker);
        this.save(copiedMarker);
        if (marker.getMessages() != null) {
            List<MarkerMessage> copiedMessages = marker.getMessages().stream()
                    .map(m -> m.copyWithoutId(copiedMarker.getId()))
                    .collect(Collectors.toList());
            copiedMarker.setMessages(copiedMessages);
            copiedMarker.getMessages().forEach(m -> markerMessageService.save(m));
        }
        return copiedMarker;
    }

    public void deleteById(Long id) {
        markerRepository.deleteById(id);
    }

    @Transactional
    public boolean deleteByIdAndCheckAccess(Long markerId, Long mapId, User user) {
        Marker marker = findByIdAndCheckAccess(markerId, mapId, user, AccessRole.EDITOR);
        if (marker == null) return false;
        markerRepository.delete(marker);
        return true;
    }

    public boolean existsByIdAndCheckAccess(Long id, User user) {
        Optional<Marker> markerOptional = markerRepository.findById(id);
        return markerOptional.filter(marker -> mapService.existsByIdAndUser(marker.getMapId(), user)).isPresent();
    }
}