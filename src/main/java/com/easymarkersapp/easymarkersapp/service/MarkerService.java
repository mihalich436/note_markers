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
    private MessageService messageService;

    public Marker save(Marker marker) {
        String number = marker.getNumber();
        if (number != null) {
            marker.setNumber(number.substring(0, Math.min(3, number.length())));
        }
        return markerRepository.save(marker);
    }

    @Transactional
    public Marker findByIdAndCheckAccess(Long id, User user, AccessRole requiredRole) {
        Optional<Marker> markerOptional = markerRepository.findById(id);
        if (markerOptional.isPresent()) {
            Marker marker = markerOptional.get();
            ProjectAccess access = mapService.getRoleByIdAndUser(marker.getMapId(), user);
            if (access != null && access.getRole().hasAccess(requiredRole)) {
                return marker;
            }
        }
        return null;
    }

    @Transactional
    public Marker updateByIdAndCheckAccess(MarkerRequest request, Long id, User user, AccessRole requiredRole) {
        Optional<Marker> markerOptional = markerRepository.findById(id);
        if (markerOptional.isPresent()) {
            Marker marker = markerOptional.get();
            boolean visibilityPrev = marker.getVisibility();
            ProjectAccess access = mapService.getRoleByIdAndUser(marker.getMapId(), user);
            if (access != null && access.getRole().hasAccess(requiredRole)) {
                request.updateMarker(marker);
                Marker updatedMarker = this.save(marker);
                if (!visibilityPrev && updatedMarker.getVisibility()) {
                    updatedMarker.getMessages();
                }
                return updatedMarker;
            }
        }
        return null;
    }

    @Transactional
    public Marker copyAndCheckAccess(MarkerRequest request, Long id, User user, AccessRole requiredRole) {
        Optional<Marker> markerOptional = markerRepository.findById(id);
        if (markerOptional.isPresent()) {
            Marker marker = markerOptional.get();
            ProjectAccess access = mapService.getRoleByIdAndUser(marker.getMapId(), user);
            if (access != null && access.getRole().hasAccess(requiredRole)) {
                Marker copiedMarker = marker.copy();
                request.updateMarker(copiedMarker);
                this.save(copiedMarker);
                if (marker.getMessages() != null) {
                    List<Message> copiedMessages = marker.getMessages().stream()
                            .map(m -> m.copyWithoutId(copiedMarker.getId()))
                            .collect(Collectors.toList());
                    copiedMarker.setMessages(copiedMessages);
                    copiedMarker.getMessages().forEach(m -> messageService.save(m));
                }
                return copiedMarker;
            }
        }
        return null;
    }

    public void deleteById(Long id) {
        markerRepository.deleteById(id);
    }

    @Transactional
    public boolean deleteByIdAndCheckAccess(Long id, User user) {
        Optional<Marker> markerOptional = markerRepository.findById(id);
        if (markerOptional.isPresent()) {
            Marker marker = markerOptional.get();
            ProjectAccess access = mapService.getRoleByIdAndUser(marker.getMapId(), user);
            if (access != null && access.getRole().hasAccess(AccessRole.EDITOR)) {
                markerRepository.delete(marker);
                return true;
            }
        }
        return false;
    }

    public boolean existsByIdAndCheckAccess(Long id, User user) {
        Optional<Marker> markerOptional = markerRepository.findById(id);
        return markerOptional.filter(marker -> mapService.existsByIdAndUser(marker.getMapId(), user)).isPresent();
    }
}
